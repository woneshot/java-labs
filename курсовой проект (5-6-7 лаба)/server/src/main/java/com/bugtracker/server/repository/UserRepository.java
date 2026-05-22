package com.bugtracker.server.repository;

import com.bugtracker.common.entity.User;
import com.bugtracker.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

public class UserRepository implements BaseRepository<User> {

    @Override
    public User save(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // открываем сессию (соединение с БД)
            transaction = session.beginTransaction();
            // начинаем транзакцию (группировка команд, либо все выполнятся либо ничего)
            session.persist(user);
            // persist() вставляет новую запись (INSERT)
            transaction.commit();
            // коммитим (отправляем в БД окончательно)
            return user;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            // откатываем все изменения если ошибка
            throw new RuntimeException("ошибка при сохранении пользователя", e);
        }
    }

    @Override
    public User update(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            // merge() обновляет существующую запись (UPDATE)
            // отличие от persist: persist работает только с новыми, merge — с новыми и старыми
            transaction.commit();
            return user;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при обновлении пользователя", e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Optional — контейнер, может быть пользователь или пусто
            // если findById вернёт null, Optional оборачивает его "безопасно"
            return Optional.ofNullable(session.get(User.class, id));
            // get() — это SELECT WHERE id = ?
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске пользователя", e);
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // "FROM User" — это HQL (Hibernate Query Language), похож на SQL но пишем имена классов
            // вместо "SELECT * FROM users" пишем "FROM User"
            return session.createQuery("FROM User", User.class).list();
            // .list() получает все записи (SELECT *)
        } catch (Exception e) {
            throw new RuntimeException("ошибка при получении всех пользователей", e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            // сначала получаем пользователя
            if (user != null) {
                session.remove(user);
                // remove() удаляет (DELETE)
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при удалении пользователя", e);
        }
    }

    // кастомные методы
    public Optional<User> findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // :username — это параметр запроса (защита от SQL-инъекций)
            // .setParameter() подставляет значение
            return session.createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResultOptional();
            // uniqueResultOptional() — получить ровно одну запись или пусто
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске по username", e);
        }
    }

    public Optional<User> findByEmail(String email) { // аналогично прошлому методу
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResultOptional();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске по email", e);
        }
    }
}
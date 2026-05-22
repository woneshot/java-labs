package com.bugtracker.server.repository;

import com.bugtracker.common.entity.Role;
import com.bugtracker.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

public class RoleRepository implements BaseRepository<Role> {

    // подробные комменты в классе UserRepository, тут все аналогично

    @Override
    public Role save(Role role) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(role);
            transaction.commit();
            return role;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при сохранении роли", e);
        }
    }

    @Override
    public Role update(Role role) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(role);
            transaction.commit();
            return role;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при обновлении роли", e);
        }
    }

    @Override
    public Optional<Role> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Role.class, id));
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске роли", e);
        }
    }

    @Override
    public List<Role> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Role", Role.class).list();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при получении всех ролей", e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Role role = session.get(Role.class, id);
            if (role != null) {
                session.remove(role);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при удалении роли", e);
        }
    }

    public Optional<Role> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Role WHERE name = :name", Role.class)
                    .setParameter("name", name)
                    .uniqueResultOptional();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске роли по имени", e);
        }
    }
}
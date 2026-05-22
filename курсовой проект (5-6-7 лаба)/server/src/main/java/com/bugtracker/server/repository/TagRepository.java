package com.bugtracker.server.repository;

import com.bugtracker.common.entity.Tag;
import com.bugtracker.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

public class TagRepository implements BaseRepository<Tag> {

    // подробные комменты в классе UserRepository, тут все аналогично

    @Override
    public Tag save(Tag tag) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(tag);
            transaction.commit();
            return tag;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при сохранении тега", e);
        }
    }

    @Override
    public Tag update(Tag tag) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(tag);
            transaction.commit();
            return tag;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при обновлении тега", e);
        }
    }

    @Override
    public Optional<Tag> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Tag.class, id));
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске тега", e);
        }
    }

    @Override
    public List<Tag> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Tag", Tag.class).list();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при получении всех тегов", e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Tag tag = session.get(Tag.class, id);
            if (tag != null) {
                session.remove(tag);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при удалении тега", e);
        }
    }

    public Optional<Tag> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Tag WHERE name = :name", Tag.class)
                    .setParameter("name", name)
                    .uniqueResultOptional();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске тега по имени", e);
        }
    }
}
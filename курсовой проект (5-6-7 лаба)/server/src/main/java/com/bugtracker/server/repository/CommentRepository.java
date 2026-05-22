package com.bugtracker.server.repository;

import com.bugtracker.common.entity.Comment;
import com.bugtracker.common.entity.Task;
import com.bugtracker.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

public class CommentRepository implements BaseRepository<Comment> {

    // подробные комменты в классе UserRepository, тут все аналогично

    @Override
    public Comment save(Comment comment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(comment);
            transaction.commit();
            return comment;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при сохранении комментария", e);
        }
    }

    @Override
    public Comment update(Comment comment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(comment);
            transaction.commit();
            return comment;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при обновлении комментария", e);
        }
    }

    @Override
    public Optional<Comment> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Comment.class, id));
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске комментария", e);
        }
    }

    @Override
    public List<Comment> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Comment", Comment.class).list();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при получении всех комментариев", e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Comment comment = session.get(Comment.class, id);
            if (comment != null) {
                session.remove(comment);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при удалении комментария", e);
        }
    }

    public List<Comment> findByTask(Task task) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Comment WHERE task = :task ORDER BY createdAt ASC", Comment.class)
                    .setParameter("task", task)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске комментариев задачи", e);
        }
    }
}
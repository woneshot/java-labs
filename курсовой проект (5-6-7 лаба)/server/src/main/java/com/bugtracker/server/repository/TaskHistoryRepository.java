package com.bugtracker.server.repository;

import com.bugtracker.common.entity.Task;
import com.bugtracker.common.entity.TaskHistory;
import com.bugtracker.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

public class TaskHistoryRepository implements BaseRepository<TaskHistory> {

    // подробные комменты в классе UserRepository, тут все аналогично

    @Override
    public TaskHistory save(TaskHistory history) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(history);
            transaction.commit();
            return history;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при сохранении истории", e);
        }
    }

    @Override
    public TaskHistory update(TaskHistory history) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(history);
            transaction.commit();
            return history;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при обновлении истории", e);
        }
    }

    @Override
    public Optional<TaskHistory> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(TaskHistory.class, id));
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске истории", e);
        }
    }

    @Override
    public List<TaskHistory> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM TaskHistory", TaskHistory.class).list();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при получении всей истории", e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            TaskHistory history = session.get(TaskHistory.class, id);
            if (history != null) {
                session.remove(history);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при удалении истории", e);
        }
    }

    public List<TaskHistory> findByTask(Task task) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM TaskHistory WHERE task = :task ORDER BY changedAt DESC", TaskHistory.class)
                    .setParameter("task", task)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске истории задачи", e);
        }
    }
}
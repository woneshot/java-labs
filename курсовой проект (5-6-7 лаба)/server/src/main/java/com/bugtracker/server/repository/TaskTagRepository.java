package com.bugtracker.server.repository;

import com.bugtracker.common.entity.Tag;
import com.bugtracker.common.entity.Task;
import com.bugtracker.common.entity.TaskTag;
import com.bugtracker.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

public class TaskTagRepository implements BaseRepository<TaskTag> {

    // подробные комменты в классе UserRepository, тут все аналогично

    @Override
    public TaskTag save(TaskTag taskTag) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(taskTag);
            transaction.commit();
            return taskTag;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при сохранении связи задача-тег", e);
        }
    }

    @Override
    public TaskTag update(TaskTag taskTag) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(taskTag);
            transaction.commit();
            return taskTag;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при обновлении связи задача-тег", e);
        }
    }

    @Override
    public Optional<TaskTag> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(TaskTag.class, id));
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске связи задача-тег", e);
        }
    }

    @Override
    public List<TaskTag> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM TaskTag", TaskTag.class).list();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при получении всех связей задача-тег", e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            TaskTag taskTag = session.get(TaskTag.class, id);
            if (taskTag != null) {
                session.remove(taskTag);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при удалении связи задача-тег", e);
        }
    }

    public List<TaskTag> findByTask(Task task) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM TaskTag WHERE task = :task", TaskTag.class)
                    .setParameter("task", task)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске тегов задачи", e);
        }
    }

    public Optional<TaskTag> findByTaskAndTag(Task task, Tag tag) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM TaskTag WHERE task = :task AND tag = :tag", TaskTag.class)
                    .setParameter("task", task)
                    .setParameter("tag", tag)
                    .uniqueResultOptional();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске связи задача-тег", e);
        }
    }
}
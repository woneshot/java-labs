package com.bugtracker.server.repository;

import com.bugtracker.common.entity.Project;
import com.bugtracker.common.entity.Task;
import com.bugtracker.common.entity.User;
import com.bugtracker.common.enums.TaskPriority;
import com.bugtracker.common.enums.TaskStatus;
import com.bugtracker.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

public class TaskRepository implements BaseRepository<Task> {

    // подробные комменты в классе UserRepository, тут все аналогично

    @Override
    public Task save(Task task) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(task);
            transaction.commit();
            return task;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при сохранении задачи", e);
        }
    }

    @Override
    public Task update(Task task) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(task);
            transaction.commit();
            return task;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при обновлении задачи", e);
        }
    }

    @Override
    public Optional<Task> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Task.class, id));
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске задачи", e);
        }
    }

    @Override
    public List<Task> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Task", Task.class).list();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при получении всех задач", e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Task task = session.get(Task.class, id);
            if (task != null) {
                session.remove(task);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при удалении задачи", e);
        }
    }

    public List<Task> findByProject(Project project) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Task WHERE project = :project", Task.class)
                    .setParameter("project", project)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске задач проекта", e);
        }
    }

    public List<Task> findByAssignee(User assignee) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Task WHERE assignee = :assignee", Task.class)
                    .setParameter("assignee", assignee)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске задач исполнителя", e);
        }
    }

    public List<Task> findByStatus(TaskStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Task WHERE status = :status", Task.class)
                    .setParameter("status", status)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске задач по статусу", e);
        }
    }

    public List<Task> findByPriority(TaskPriority priority) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Task WHERE priority = :priority", Task.class)
                    .setParameter("priority", priority)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске задач по приоритету", e);
        }
    }

    public List<Task> searchByTitle(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Task WHERE title LIKE :keyword", Task.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске задач", e);
        }
    }
}
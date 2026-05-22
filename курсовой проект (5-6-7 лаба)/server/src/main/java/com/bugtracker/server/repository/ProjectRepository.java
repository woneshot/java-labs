package com.bugtracker.server.repository;

import com.bugtracker.common.entity.Project;
import com.bugtracker.common.entity.User;
import com.bugtracker.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

public class ProjectRepository implements BaseRepository<Project> {

    // подробные комменты в классе UserRepository, тут все аналогично

    @Override
    public Project save(Project project) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(project);
            transaction.commit();
            return project;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при сохранении проекта", e);
        }
    }

    @Override
    public Project update(Project project) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(project);
            transaction.commit();
            return project;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при обновлении проекта", e);
        }
    }

    @Override
    public Optional<Project> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Project.class, id));
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске проекта", e);
        }
    }

    @Override
    public List<Project> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Project", Project.class).list();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при получении всех проектов", e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Project project = session.get(Project.class, id);
            if (project != null) {
                session.remove(project);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при удалении проекта", e);
        }
    }

    public List<Project> findByOwner(User owner) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Project WHERE owner = :owner", Project.class)
                    .setParameter("owner", owner)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске проектов по владельцу", e);
        }
    }
}
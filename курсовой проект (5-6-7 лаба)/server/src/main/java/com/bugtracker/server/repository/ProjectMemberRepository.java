package com.bugtracker.server.repository;

import com.bugtracker.common.entity.Project;
import com.bugtracker.common.entity.ProjectMember;
import com.bugtracker.common.entity.User;
import com.bugtracker.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

public class ProjectMemberRepository implements BaseRepository<ProjectMember> {

    // подробные комменты в классе UserRepository, тут все аналогично

    @Override
    public ProjectMember save(ProjectMember member) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(member);
            transaction.commit();
            return member;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при сохранении участника", e);
        }
    }

    @Override
    public ProjectMember update(ProjectMember member) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(member);
            transaction.commit();
            return member;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при обновлении участника", e);
        }
    }

    @Override
    public Optional<ProjectMember> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(ProjectMember.class, id));
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске участника", e);
        }
    }

    @Override
    public List<ProjectMember> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM ProjectMember", ProjectMember.class).list();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при получении всех участников", e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ProjectMember member = session.get(ProjectMember.class, id);
            if (member != null) {
                session.remove(member);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("ошибка при удалении участника", e);
        }
    }

    public List<ProjectMember> findByProject(Project project) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM ProjectMember WHERE project = :project", ProjectMember.class)
                    .setParameter("project", project)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске участников проекта", e);
        }
    }

    public List<ProjectMember> findByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM ProjectMember WHERE user = :user", ProjectMember.class)
                    .setParameter("user", user)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске проектов пользователя", e);
        }
    }

    public Optional<ProjectMember> findByProjectAndUser(Project project, User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM ProjectMember WHERE project = :project AND user = :user", ProjectMember.class)
                    .setParameter("project", project)
                    .setParameter("user", user)
                    .uniqueResultOptional();
        } catch (Exception e) {
            throw new RuntimeException("ошибка при поиске участника", e);
        }
    }
}
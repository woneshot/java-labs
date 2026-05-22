package com.bugtracker.server;

import com.bugtracker.common.entity.Role;
import com.bugtracker.server.util.HibernateUtil;
import org.hibernate.Session;
import java.util.List;

public class TestConnection {

    public static void main(String[] args) {
        System.out.println("Проверка подключения к БД...");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Role> roles = session.createQuery("FROM Role", Role.class).list();

            System.out.println("Подключение успешно!");
            System.out.println("Роли в базе:");
            for (Role role : roles) {
                System.out.println("  - " + role.getName() + ": " + role.getDescription());
            }
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
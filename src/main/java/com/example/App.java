package com.example;

import com.example.data.entities.Category;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class App {

    static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("MangoesLeisureFacilityDb");

    public static void main(String[] args) {

        createCategory("Jobless");
        createCategory("Disabled");
        createCategory("Student");

        Optional<Category> category = readCategory(2);
        System.out.print("Second Category: " + category);

        List<Category> categories = readAllCategories();

        System.out.println("\nAll Categories:");
        categories.forEach(System.out::println);


        ENTITY_MANAGER_FACTORY.close();
    }

    private static void createCategory(String name) {

        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();

        EntityTransaction entityTransaction = null;
        try {

            entityTransaction = entityManager.getTransaction();

            entityTransaction.begin();

            Category category = new Category(name);

            entityManager.persist(category);

            entityTransaction.commit();

        } catch (Exception exception) {

            if (entityTransaction != null) entityTransaction.rollback();

            exception.printStackTrace();

        } finally {
            entityManager.close();
        }

    }

    public static Optional<Category> readCategory(Integer id) {

        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();

        String query = "SELECT c FROM Category c WHERE c.id = :id";

        TypedQuery<Category> typedQuery = entityManager.createQuery(query, Category.class);
        typedQuery.setParameter("id", id);

        try {
            return Optional.of(typedQuery.getSingleResult());

        } catch (Exception exception) {

            exception.printStackTrace();

        } finally {
            entityManager.close();
        }

        return Optional.empty();
    }

    public static List<Category> readAllCategories() {

        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();

        String query = "SELECT  c FROM Category c";

        TypedQuery<Category> typedQuery = entityManager.createQuery(query, Category.class);

        try {
            return typedQuery.getResultList();

        } catch (Exception exception) {

            exception.printStackTrace();

        } finally {
            entityManager.close();
        }

        return Collections.emptyList();
    }
}

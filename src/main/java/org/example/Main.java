package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.example.config.CustomPersistenceUnitInfo;
import org.example.entity.Product;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        var persistenceProvider = new HibernatePersistenceProvider();
        var customPersistenceUnitInfo = new CustomPersistenceUnitInfo();

        var props = new HashMap<>();
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.hbm2ddl.auto", "update");


        EntityManagerFactory emf =
                persistenceProvider.createContainerEntityManagerFactory(customPersistenceUnitInfo, props);

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // find vs getReference
            var reference = em.getReference(Product.class, 1); // Get only reference (no db exec unless used)

            System.out.println(reference); // DB request

            reference.setName("chocolate");

            System.out.println(reference);

            em.refresh(reference); // Mirror the entity from DB

            reference.setName("apple");

            em.detach(reference); // Remove entity from context

            em.merge(reference); // Merge entity to context


            /*
             persist
             find
             remove
             merge
             refresh
             detach
             getReference
             */

            em.getTransaction().commit();
        } finally {
            em.close();
        }

    }

}
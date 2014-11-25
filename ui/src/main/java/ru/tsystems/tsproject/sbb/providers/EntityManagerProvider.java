package ru.tsystems.tsproject.sbb.providers;

import javax.persistence.EntityManager;

/**
 * Created by apple on 27.10.2014.
 */
public class EntityManagerProvider {
    private static ThreadLocal<EntityManager> entityManagerThreadLocal = new ThreadLocal<EntityManager>();
    public EntityManager getEntityManager() {
        return entityManagerThreadLocal.get();
    }

    public static void setCurrentEntityManager(EntityManager em) {
        entityManagerThreadLocal.set(em);
    }
}

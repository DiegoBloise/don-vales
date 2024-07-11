package br.com.don.factory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@ApplicationScoped
public class EntityManagerProducer {

    private EntityManagerFactory factory;


    public EntityManagerProducer() {
        this.factory = Persistence.createEntityManagerFactory("don");
    }


    @Produces
    @ApplicationScoped
    public EntityManager createEntityManager() {
        return this.factory.createEntityManager();
    }


    public void closeEntityManager(@Disposes EntityManager manager) {
        if (manager.isOpen()) {
            manager.close();
        }
    }
}

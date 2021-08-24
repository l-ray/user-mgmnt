package de.lray.service.admin;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class Resources {

    // Expose an entity manager using the resource producer pattern
    @PersistenceContext()
    private EntityManager em;

    @Produces
    public EntityManager entityManager(){
        return em;
    }
}

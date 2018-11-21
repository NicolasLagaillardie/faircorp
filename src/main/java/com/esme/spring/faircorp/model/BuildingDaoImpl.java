package com.esme.spring.faircorp.model;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class BuildingDaoImpl implements BuildingDaoCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Building> findOnBuildings() {
        String jpql = "select bd from Room bd where bd.id = :value";
        return em.createQuery(jpql, Building.class)
                .getResultList();
    }
}

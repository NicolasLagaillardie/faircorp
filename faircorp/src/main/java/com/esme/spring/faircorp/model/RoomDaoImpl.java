package com.esme.spring.faircorp.model;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class RoomDaoImpl implements RoomDaoCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Room> findOnRooms() {
        String jpql = "select rm from Room rm where rm.id = :value";
        return em.createQuery(jpql, Room.class)
                .getResultList();
    }
}

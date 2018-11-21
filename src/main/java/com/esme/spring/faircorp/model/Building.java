package com.esme.spring.faircorp.model;

import javax.persistence.*;
import java.util.List;

@Entity
//@Table(name = "SP_Room")
public class Building {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length=255)
    private String name;

    @OneToMany (mappedBy = "building")
    private List<Room> rooms;

    public Building() {
    }

    public Building(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Room> getRooms() {return rooms;}

    public List<Light> getLights() {
        List<Light> Lights = rooms.get(0).getLights();
        for (int i = 1; i < rooms.size(); i++) {
            Lights.addAll(rooms.get(0).getLights());
        }
        return Lights;
    }
}
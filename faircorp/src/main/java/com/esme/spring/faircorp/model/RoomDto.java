package com.esme.spring.faircorp.model;

import javax.persistence.Column;
import java.util.List;

public class RoomDto {

    private Long id;
    private String name;
    private Integer floor;

    public RoomDto(Object room) {
    }

    public RoomDto(Room room) {
        this.id = room.getId();
        this.floor = room.getFloor();
        this.name = room.getName();
    }

    public Long getId() {
        return id;
    }

    public Integer getFloor() {
        return floor;
    }

    public String getName() {
        return name;
    }

}

package com.esme.spring.faircorp.model;

import javax.persistence.Column;
import java.util.List;

public class BuildingDto {

    private Long id;
    private String name;

    public BuildingDto(Object room) {
    }

    public BuildingDto(Building building) {
        this.id = building.getId();
        this.name = building.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}

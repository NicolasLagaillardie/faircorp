package com.esme.spring.faircorp.model;

public class LightDto {

    private Long id;
    private Long roomId;
    private Integer level;
    private Status status;

    public LightDto(Object light) {
    }

    public LightDto(Light light) {
        this.id = light.getId();
        this.level = light.getLevel();
        this.status = light.getStatus();
        this.roomId = light.getRoom().getId();
    }

    public Long getId() {
        return id;
    }

    public Integer getLevel() {
        return level;
    }

    public Status getStatus() {
        return status;
    }

    public Long getRoomId() {
        return roomId;
    }
}
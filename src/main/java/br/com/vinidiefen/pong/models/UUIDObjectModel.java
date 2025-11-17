package br.com.vinidiefen.pong.models;

import java.util.UUID;

import br.com.vinidiefen.pong.repositories.annotations.Column;

public abstract class UUIDObjectModel {
    
    @Column(name = "id", type = "UUID", primaryKey = true)
    protected UUID id;

    public UUIDObjectModel() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}

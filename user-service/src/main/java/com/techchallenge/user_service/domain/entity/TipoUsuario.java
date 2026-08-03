package com.techchallenge.user_service.domain.entity;

import java.util.UUID;

public class TipoUsuario {

    private UUID id;
    private TipoUsuarioEnum tipo;
    private boolean ativo;

    public TipoUsuario(UUID id, TipoUsuarioEnum tipo, boolean ativo) {
        this.id = id;
        this.tipo = tipo;
        this.ativo = ativo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TipoUsuarioEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuarioEnum tipo) {
        this.tipo = tipo;
    }

    public boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}

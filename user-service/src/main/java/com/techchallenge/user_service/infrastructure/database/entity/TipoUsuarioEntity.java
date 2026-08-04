package com.techchallenge.user_service.infrastructure.database.entity;

import com.techchallenge.user_service.domain.entity.TipoUsuarioEnum;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tipos_usuario")
public class TipoUsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoUsuarioEnum tipo;

    @Column(nullable = false)
    private boolean ativo;

    public TipoUsuarioEntity() {
    }

    public TipoUsuarioEntity(UUID id, TipoUsuarioEnum tipo, boolean ativo) {
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

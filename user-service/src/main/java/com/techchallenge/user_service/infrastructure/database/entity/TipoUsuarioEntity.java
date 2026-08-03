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
}

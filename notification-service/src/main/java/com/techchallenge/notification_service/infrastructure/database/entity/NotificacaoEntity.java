package com.techchallenge.notification_service.infrastructure.database.entity;

import com.techchallenge.notification_service.domain.entity.StatusNotificacao;
import com.techchallenge.notification_service.domain.entity.TipoNotificacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notificacoes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "id_consulta")
    private UUID consultaId;

    @Column(nullable = false, name = "id_paciente")
    private UUID pacienteId;

    @Column(nullable = false, name = "data_hora_consulta")
    private LocalDateTime dataHoraConsulta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoNotificacao tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status_notificacao")
    private StatusNotificacao status;

    @Column(nullable = false, name = "enviado_em")
    private LocalDateTime enviadoEm;

    @Column(nullable = false, name = "criado_em")
    private LocalDateTime criadoEm;
}

package com.techchallenge.appointment_service.infrastructure.database.entity;

import com.techchallenge.appointment_service.domain.entity.StatusConsulta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "consultas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsultaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "id_paciente")
    private UUID pacienteId;

    @Column(nullable = false, name = "id_medico")
    private UUID medicoId;

    @Column(nullable = true, name = "id_enfermeiro")
    private UUID enfermeiroId;

    @Column(nullable = false, name = "data_hora")
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusConsulta status;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String observacoes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(nullable = false)
    private LocalDateTime atualizadoEm;
}

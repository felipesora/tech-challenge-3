package com.techchallenge.appointment_service.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class Consulta {

    private UUID id;
    private UUID pacienteId;
    private UUID medicoId;
    private UUID enfermeiroId;
    private LocalDateTime dataHora;
    private StatusConsulta status;
    private String observacoes;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Consulta(UUID id,
                    UUID pacienteId,
                    UUID medicoId,
                    UUID enfermeiroId,
                    LocalDateTime dataHora,
                    StatusConsulta status,
                    String observacoes,
                    LocalDateTime criadoEm,
                    LocalDateTime atualizadoEm) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.enfermeiroId = enfermeiroId;
        this.dataHora = dataHora;
        this.status = status;
        this.observacoes = observacoes;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(UUID pacienteId) {
        this.pacienteId = pacienteId;
    }

    public UUID getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(UUID medicoId) {
        this.medicoId = medicoId;
    }

    public UUID getEnfermeiroId() {
        return enfermeiroId;
    }

    public void setEnfermeiroId(UUID enfermeiroId) {
        this.enfermeiroId = enfermeiroId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusConsulta getStatus() {
        return status;
    }

    public void setStatus(StatusConsulta status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}

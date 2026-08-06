package com.techchallenge.notification_service.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class Notificacao {

    private UUID id;
    private UUID consultaId;
    private UUID pacienteId;

    private LocalDateTime dataHoraConsulta;

    private TipoNotificacao tipo;
    private StatusNotificacao status;

    private LocalDateTime enviadoEm;
    private LocalDateTime criadoEm;

    public Notificacao(UUID id,
                       UUID consultaId,
                       UUID pacienteId,
                       LocalDateTime dataHoraConsulta,
                       TipoNotificacao tipo,
                       StatusNotificacao status,
                       LocalDateTime enviadoEm,
                       LocalDateTime criadoEm) {
        this.id = id;
        this.consultaId = consultaId;
        this.pacienteId = pacienteId;
        this.dataHoraConsulta = dataHoraConsulta;
        this.tipo = tipo;
        this.status = status;
        this.enviadoEm = enviadoEm;
        this.criadoEm = criadoEm;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(UUID consultaId) {
        this.consultaId = consultaId;
    }

    public UUID getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(UUID pacienteId) {
        this.pacienteId = pacienteId;
    }

    public LocalDateTime getDataHoraConsulta() {
        return dataHoraConsulta;
    }

    public void setDataHoraConsulta(LocalDateTime dataHoraConsulta) {
        this.dataHoraConsulta = dataHoraConsulta;
    }

    public TipoNotificacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacao tipo) {
        this.tipo = tipo;
    }

    public StatusNotificacao getStatus() {
        return status;
    }

    public void setStatus(StatusNotificacao status) {
        this.status = status;
    }

    public LocalDateTime getEnviadoEm() {
        return enviadoEm;
    }

    public void setEnviadoEm(LocalDateTime enviadoEm) {
        this.enviadoEm = enviadoEm;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}

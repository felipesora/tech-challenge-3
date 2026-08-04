package com.techchallenge.user_service.domain.entity;

import java.util.UUID;

public class Usuario {

    private UUID id;
    private String nome;
    private String email;
    private String cpf;
    private String senhaHash;
    private TipoUsuario tipoUsuario;
    private boolean ativo;

    public Usuario(UUID id, String nome, String email, String cpf, String senhaHash, TipoUsuario tipoUsuario, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.senhaHash = senhaHash;
        this.tipoUsuario = tipoUsuario;
        this.ativo = ativo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}

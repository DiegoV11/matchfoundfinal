package com.example.matchfoundfinal.dto;

import java.io.Serializable;

public class UsuarioDTO implements Serializable {
    private String username;
    private String correo;
    private String tag;
    private String descripcion;
    private String agente;
    private String rol;
    private String rankImage;
    private int cantReportes;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCantReportes() {
        return cantReportes;
    }

    public void setCantReportes(int cantReportes) {
        this.cantReportes = cantReportes;
    }

    public String getRankImage() {
        return rankImage;
    }

    public void setRankImage(String rankImage) {
        this.rankImage = rankImage;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getRango() {
        return rango;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }

    private String rango;

    public UsuarioDTO() {
    }

    public UsuarioDTO(String username, String correo, String tag, String descripcion, String agente, String rol,String rango,String rankImage,int cantReportes,String id) {
        this.username = username;
        this.correo = correo;
        this.tag = tag;
        this.descripcion = descripcion;
        this.agente = agente;
        this.rol = rol;
        this.rango = rango;
        this.rankImage=rankImage;
        this.cantReportes=cantReportes;
        this.id=id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAgente() {
        return agente;
    }

    public void setAgente(String agente) {
        this.agente = agente;
    }
}


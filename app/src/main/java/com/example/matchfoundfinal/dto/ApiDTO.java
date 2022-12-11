package com.example.matchfoundfinal.dto;

public class ApiDTO {
    private String status;
    private DataApiDTO data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataApiDTO getData() {
        return data;
    }

    public void setData(DataApiDTO data) {
        this.data = data;
    }
}

package com.example.matchfoundfinal.dto;

import java.util.ArrayList;

public class DataApiDTO {
    private String curenttier;
    private String currenttierpatched;
    private String ranking_in_tier;
    private String mmr_change_to_last_game;
    private String elo;
    private String name;
    private String tag;
    private String old;
    private ImagesDTO images;

    public String getCurenttier() {
        return curenttier;
    }

    public void setCurenttier(String curenttier) {
        this.curenttier = curenttier;
    }

    public String getCurrenttierpatched() {
        return currenttierpatched;
    }

    public void setCurrenttierpatched(String currenttierpatched) {
        this.currenttierpatched = currenttierpatched;
    }

    public String getRanking_in_tier() {
        return ranking_in_tier;
    }

    public void setRanking_in_tier(String ranking_in_tier) {
        this.ranking_in_tier = ranking_in_tier;
    }

    public String getMmr_change_to_last_game() {
        return mmr_change_to_last_game;
    }

    public void setMmr_change_to_last_game(String mmr_change_to_last_game) {
        this.mmr_change_to_last_game = mmr_change_to_last_game;
    }

    public String getElo() {
        return elo;
    }

    public void setElo(String elo) {
        this.elo = elo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getOld() {
        return old;
    }

    public void setOld(String old) {
        this.old = old;
    }

    public ImagesDTO getImages() {
        return images;
    }

    public void setImages(ImagesDTO images) {
        this.images = images;
    }
}

package com.example.punchcard.bean;

public class Energy {
    private int id;
    private int energy;
    private  String date;
    private int login;
    private int punchCard;

    public Energy(){

    }

    public Energy( int energy ,String date,int login ,int punchCard){
        this.energy = energy;
        this.date = date;
        this.login = login;
        this.punchCard = punchCard;
    }

    public int getId() {
        return id;
    }

    public int getEnergy() {
        return energy;
    }

    public String getDate() {
        return date;
    }

    public int getLogin() {
        return login;
    }

    public int getPunchCard() {
        return punchCard;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setLogin(int login) {
        this.login = login;
    }

    public void setPunchCard(int punchCard) {
        this.punchCard = punchCard;
    }
}

package com.example.orderfood.Model;

public class User {
    private  String Name;
    private String Password;
    private String Address;
    private String Mail;
    private  String Phone;
    private  Integer Points;
    private Boolean Admin;

    public User(String name, String password,String address, String mail,String phone, Integer points) {
        Name = name;
        Password = password;
        Address = address;
        Mail = mail;
        Phone = phone;
        Points= points;
        Admin = false;
    }

    public User() {
    }

    public Boolean getAdmin() {
        return Admin;
    }

    public void setAdmin(Boolean admin) {
        Admin = admin;
    }

    public Integer getPoints() {
        return Points;
    }

    public void setPoints(Integer points) {
        Points = points;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}

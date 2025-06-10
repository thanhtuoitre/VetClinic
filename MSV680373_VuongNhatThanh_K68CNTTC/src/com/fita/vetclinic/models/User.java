package com.fita.vetclinic.models;

import java.util.Date;

public class User {
    private int userId;
    private String fullname;
    private String gender;
    private Date birthday;
    private String phone;
    private String email;
    private String imagePath;
    private String password;
    private String role;

    // Constructor mặc định
    public User() {
        this.role = "Khách hàng"; 
    }

    // Constructor có tham số
    public User(String fullname, String gender, Date birthday, String phone, String email, String imagePath, String password, String role) {
        this.fullname = fullname;
        this.gender = gender;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.imagePath = imagePath != null ? imagePath : "images/icons/user.png";
        this.password = password;
        this.role = role != null ? role : "khachhang";  // Nếu role là null, mặc định là "Khách hàng"
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

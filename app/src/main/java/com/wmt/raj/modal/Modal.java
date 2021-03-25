package com.wmt.raj.modal;

public class Modal {
    public String username;

    public Modal(String username, String email) {
        this.username = username;
        this.email = email;
    }
    public Modal(String username, String email,String img) {
        this.username = username;
        this.email = email;
        this.img = img;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImg() {
        return img;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String email;
    public String img;

}

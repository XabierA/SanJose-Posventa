package com.example.SanJose.Models;

import java.io.Serializable;

public class Email implements Serializable {
    //public String _id;
    public String email;

    public Email(String email) {
        //this._id = _id;
        this.email = email;
    }
    /*public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }*/

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

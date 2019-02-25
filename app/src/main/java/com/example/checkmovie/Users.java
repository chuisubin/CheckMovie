package com.example.checkmovie;

public class Users {
    private String mName;
    private String mEmail;
    private String mPassword;


    public Users(){
    };

    public Users(String name, String email, String password) {
        if (name.trim().equals("")) {
            name = "no name";
        }

        mName = name;
        mEmail=email;
        mPassword=password;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name)
    {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {

        return mPassword;
    }

    public void setPassword(String password) {

        mPassword = password;
    }

}
package com.example.checkmovie;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String mUserName;
    private String mDate;
    private String mKey;
    private String mInfo;


    public Upload(){

    };

    public Upload(String name, String imageUrl, String userName, String date,String info) {
        if (name.trim().equals("")) {
            name = "no name";

        }


        mName = name;
        mImageUrl = imageUrl;
        mUserName = userName;
        mDate = date;
        mInfo=info;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name)
    {
        mName = name;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getImageUrl() {

        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {

        mImageUrl = imageUrl;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {

        mDate = date;
    }
    public String getKey(){
        return mKey;
    }
    public void setKey(String key){
        mKey=key;
    }


    public String getInfo(){
        return mInfo;
    }
    public void setInfo(String info){
        mInfo=info;
    }
}

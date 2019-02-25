package com.example.checkmovie;

public class ReplyMessage {

        private String mReplyUser;
        private String mReplyDate;
        private String mReplyKey;
        private String mMessage;

        public ReplyMessage(){

        };

    public ReplyMessage(String replyUser, String replyDate, String message) {

        mReplyUser = replyUser;
        mReplyDate=replyDate;
        mMessage=message;
    }
    public String getReplyUser() {
        return mReplyUser;
    }

    public void setReplyUser(String replyUser)
    {

        mReplyUser = replyUser;
    }
    public String getReplyDate() {
        return mReplyDate;
    }

    public void setreplyDate(String replyDate)
    {

        mReplyDate = replyDate;
    }
    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {

        mMessage = message;
    }

    public String getReplyKey() {
        return mReplyKey;
    }

    public void setReplyKey(String replyKey)
    {

        mReplyKey = replyKey;
    }


}

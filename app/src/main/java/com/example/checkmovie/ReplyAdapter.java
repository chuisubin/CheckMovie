package com.example.checkmovie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ReplyAdapter extends BaseAdapter {
     Context mContext;

    private ImageAdapter.onItemClickListener mListener;
    private LayoutInflater layoutInflater;


    private List<ReplyMessage> mReply;

    public ReplyAdapter(Context context, List<ReplyMessage> reply) {
        mContext = context;
        mReply = reply;
        layoutInflater=LayoutInflater.from(context);
    }



    @Override
    public int getCount() {
        return mReply.size();
    }

    @Override
    public Object getItem(int position) {
        return mReply.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        if(row==null) {
            row =layoutInflater.inflate(R.layout.item_reply, null);
            TextView name = (TextView) row.findViewById(R.id.itemReply_user);
            TextView date = (TextView) row.findViewById(R.id.itemReply_date);
            TextView message = (TextView) row.findViewById(R.id.itemReply_message);
            ReplyMessage uploadCurrent = mReply.get(position);
            name.setText(uploadCurrent.getReplyUser());
            date.setText(uploadCurrent.getReplyDate());
            message.setText(uploadCurrent.getMessage());
        }

        return row;
    }
}



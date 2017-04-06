package com.example.fujinohiroki.homey.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fujinohiroki.homey.ChatMessage;
import com.example.fujinohiroki.homey.R;

import java.util.List;

/**
 * Created by fujinohiroki on 2017/04/02.
 */

public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {

    private static class ViewHolder {
        TextView message;
        ImageView icon;
        ImageView loveIcon;
    }

    private LayoutInflater layoutInflater_;

    public ChatMessageAdapter(Context context, int textViewResourceId, List<ChatMessage> objects) {
        super(context, textViewResourceId, objects);
        layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessageAdapter.ViewHolder viewHolder;

        int layoutResource = 0;
        /**
         * realmResultsがadapter
         * Dataに変更されている
         */
        ChatMessage chatMessage = getItem(position);
        boolean isbot = chatMessage.getIsBot();
        layoutResource = R.layout.chat_message;

        /**
         * convertViewはnullの場合は、contentViewを新規作成する
         * nullでない場合は, 使い回す
         * スクロールではみ出した場合に画面の外に出たcontentViewを使い回す
         */
        if(convertView == null) {
            convertView = layoutInflater_.inflate(layoutResource, parent, false);
            viewHolder = new ChatMessageAdapter.ViewHolder();
            viewHolder.message = (TextView) convertView.findViewById(R.id.chatMessage);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.loveIcon = (ImageView) convertView.findViewById(R.id.likeButton);
        } else {
            viewHolder = (ChatMessageAdapter.ViewHolder) convertView.getTag();
        }

        LinearLayout singleMessageContainer = (LinearLayout) convertView.findViewById(R.id.chatMessageContainer);
        convertView.setTag(viewHolder);

        /**
         * botのメッセージかどうかで左か右に振り分ける
         * botの場合はimageを挿入する
         */
        //singleMessageContainer.setGravity(Gravity.RIGHT);
        singleMessageContainer.setGravity(isbot? Gravity.LEFT : Gravity.RIGHT);
        viewHolder.message.setText(chatMessage.getMessage());
        //viewHolder.icon.setImageResource(0);
        viewHolder.icon.setImageResource(isbot? R.drawable.homeylogo : 0);
        //viewHolder.loveIcon.setTag(1, convertView);
        viewHolder.loveIcon.setTag(chatMessage.getChatId());
        if(isbot) {
            viewHolder.loveIcon.setImageResource(chatMessage.getLike()? R.drawable.heart_pink: R.drawable.heart_white);
        } else {
            viewHolder.loveIcon.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}

package com.example.fujinohiroki.homey;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by fujinohiroki on 2017/02/11.
 */

public class ChatMessageAdapter extends RealmBaseAdapter<ChatMessage> {

    private static class ViewHolder {
        TextView message;
        TextView sender;
    }

    public ChatMessageAdapter(Context context,
                              RealmResults<ChatMessage> realmResults) {
        super(context, realmResults);
    }

    /**
     * リストビューのセルが必要になるたびに呼び出されて、表示するビューを戻り値として取得する
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        int layoutResource = 0;
        ChatMessage chatMessage = adapterData.get(position);
        boolean isbot = chatMessage.getIsBot();
        if(isbot) {
            layoutResource = R.layout.bot_message;
        } else {
            layoutResource = R.layout.user_message;
        }
        /**
         * convertViewはnullの場合は、contentViewを新規作成する
         * nullでない場合は, 使い回す
         * スクロールではみ出した場合に画面の外に出たcontentViewを使い回す
         */
        if(convertView == null) {
            convertView = inflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder();
            if(isbot) {
                viewHolder.message = (TextView) convertView.findViewById(R.id.bot_msg);
            } else{
                viewHolder.message = (TextView) convertView.findViewById(R.id.user_msg);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        /**
         * realmResultsがadapterDataに変更されている
          */

        viewHolder.message.setText(chatMessage.getMessage());

        return convertView;
    }
}

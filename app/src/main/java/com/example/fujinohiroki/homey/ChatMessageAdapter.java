package com.example.fujinohiroki.homey;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        ImageView icon;
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
        /**
         * realmResultsがadapterDataに変更されている
         */
        ChatMessage chatMessage = adapterData.get(position);
        boolean isbot = chatMessage.getIsBot();
        layoutResource = R.layout.chat_message;

        /**
         * convertViewはnullの場合は、contentViewを新規作成する
         * nullでない場合は, 使い回す
         * スクロールではみ出した場合に画面の外に出たcontentViewを使い回す
         */
        if(convertView == null) {
            convertView = inflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.message = (TextView) convertView.findViewById(R.id.chatMessage);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        LinearLayout singleMessageContainer = (LinearLayout) convertView.findViewById(R.id.chatMessageContainer);
        convertView.setTag(viewHolder);

        /**
         * botのメッセージかどうかで左か右に振り分ける
         * botの場合はimageを挿入する
         */
        singleMessageContainer.setGravity(isbot? Gravity.LEFT : Gravity.RIGHT);
        viewHolder.message.setText(chatMessage.getMessage());
        viewHolder.icon.setImageResource(isbot? R.drawable.homeylogo : 0);

        return convertView;
    }
}



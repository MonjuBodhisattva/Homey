package com.example.fujinohiroki.homey;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
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

        /**
         * convertViewはnullの場合は、contentViewを新規作成する
         * nullでない場合は, 使い回す
         * スクロールではみ出した場合に画面の外に出たcontentViewを使い回す
         */
        if(convertView == null) {
            convertView = inflater.inflate(
                    android.R.layout.simple_list_item_2, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.sender = (TextView) convertView.findViewById(android.R.id.text1);
            viewHolder.message = (TextView) convertView.findViewById(android.R.id.text2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        /**
         * realmResultsがadapterDataに変更されている
          */
        ChatMessage chatMessage = adapterData.get(position);
        viewHolder.message.setText(chatMessage.getMessage());
        boolean isbot = chatMessage.getIsBot();
        // botかどうかで送信者を分ける
        if(isbot) {
            viewHolder.sender.setText("bot");
        } else {
            viewHolder.sender.setText("あなた");
        }

        return convertView;
    }
}

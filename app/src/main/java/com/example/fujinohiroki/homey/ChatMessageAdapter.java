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

        } else {

        }
        return null;
    }
}

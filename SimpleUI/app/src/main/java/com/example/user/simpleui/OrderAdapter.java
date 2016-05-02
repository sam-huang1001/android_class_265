package com.example.user.simpleui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by user on 2016/4/25.
 */
public class OrderAdapter extends BaseAdapter{ //最原始的適配器
    List<Order> orders;
    LayoutInflater inflater; //做layout轉換

    public  OrderAdapter(Context context, List<Order> orders){
        this.inflater = LayoutInflater.from(context);
        this.orders = orders;
    }

    @Override
    public int getCount() {
        return this.orders.size(); //顯示的清單數
    }

    @Override
    public Object getItem(int position) {
        return this.orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //指定view，重點
        Holder holder; //用來保持住UI元件的自訂Class，就不用每次都重新get，以增加效率

        //每一列list就是一個converView
        if(convertView == null){ //if還未指定converView
            convertView = inflater.inflate(R.layout.listview_item,null);

            holder = new Holder();

            holder.cupCount = (TextView)convertView.findViewById(R.id.cupCount);
            holder.note =  (TextView)convertView.findViewById(R.id.note);
            holder.storeInfo =  (TextView)convertView.findViewById(R.id.store);

            convertView.setTag(holder); //存到view的空間
        }else{
            holder = (Holder) convertView.getTag();
        }

        int total = 0;

        try {
            JSONArray mJsonArray = new JSONArray(orders.get(position).getMenuResults());
            for(int i=0; i < mJsonArray.length(); i++){
                JSONObject menu = mJsonArray.getJSONObject(i);

                total += menu.getInt("mCupCount");
                total += menu.getInt("lCupCount");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.cupCount.setText(String.valueOf(total));
        holder.note.setText(orders.get(position).getNote());
        holder.storeInfo.setText(orders.get(position).getStoreInfo());

        return convertView;
    }

    class Holder{
        TextView cupCount;
        TextView note;
        TextView storeInfo;
    }
}

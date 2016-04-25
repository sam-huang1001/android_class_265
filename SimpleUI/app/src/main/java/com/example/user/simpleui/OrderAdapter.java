package com.example.user.simpleui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by user on 2016/4/25.
 */
public class OrderAdapter extends BaseAdapter{ //最原始的適配器
    ArrayList<Order> orders;
    LayoutInflater inflater; //做layout轉換

    public  OrderAdapter(Context context, ArrayList<Order> orders){
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
    public View getView(int position, View convertView, ViewGroup parent) { //指定view
        //每一列list就是一個converView
        if(convertView == null){ //還未指定converView
            convertView = inflater.inflate(R.layout.listview_item,null);
        }

        TextView drinkName = (TextView)convertView.findViewById(R.id.drinkName);
        TextView note =  (TextView)convertView.findViewById(R.id.note);

        drinkName.setText(orders.get(position).drinkName);
        note.setText(orders.get(position).note);

        return convertView;
    }

}

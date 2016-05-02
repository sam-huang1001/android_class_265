package com.example.user.simpleui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DrinkMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);
        Log.d("debug", "Drink Menu Activity OnCreate");
    }

    public void add(View view){ //從自己的layout呼叫也算是外部
        Button mButton = (Button)view;
        String text = mButton.getText().toString();
        int count = Integer.parseInt(text);

        count++;

        mButton.setText(String.valueOf(count));
    }

    public void cancel(View view){
        finish(); //直接結束Activity
    }

    public void done(View view){
        JSONArray mjsonArray= getData();
    }

    private JSONArray getData(){
        LinearLayout rootLinearLayout = (LinearLayout) findViewById(R.id.root);
        JSONArray mJsonArray = new JSONArray();

        for(int i=1; i<4; i++){
            LinearLayout linearLayout = (LinearLayout) rootLinearLayout.getChildAt(i);
            TextView mTextView = (TextView) linearLayout.getChildAt(0);
            Button mButton1 = (Button) linearLayout.getChildAt(1);
            Button mButton2 = (Button) linearLayout.getChildAt(2);

            String drinkName = mTextView.getText().toString();
            int mCup = Integer.parseInt(mButton1.getText().toString());
            int lCup = Integer.parseInt(mButton2.getText().toString());

            JSONObject mJsonObject = new JSONObject();
            try {
                mJsonObject.put("drinkName", drinkName);
                mJsonObject.put("mCupCount", mCup);
                mJsonObject.put("lCupCountr", lCup);

                mJsonArray.put(mJsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return mJsonArray;
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d("debug", "Drink Menu Activity OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("debug", "Drink Menu Activity OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("debug", "Drink Menu Activity OnPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("debug", "Drink Menu Activity OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("debug", "Drink Menu Activity OnDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("debug", "Drink Menu Activity OnRestart");
    }
}

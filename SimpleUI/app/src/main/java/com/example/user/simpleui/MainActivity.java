package com.example.user.simpleui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    TextView mTextView;
    EditText mEditText;
    RadioGroup mRadioGroup;
    ArrayList<Order> orders;
    String drinkName;
    String note = "";
    CheckBox mCheckBox;
    ListView mListView;
    Spinner mSpinner;

    SharedPreferences sp; //只有read
    SharedPreferences.Editor editor; //這個才能write

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("debug", "Main Activity OnCreate");

        mTextView = (TextView) findViewById(R.id.textView);
        mEditText = (EditText) findViewById(R.id.editText);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mCheckBox = (CheckBox) findViewById(R.id.hideCheckBox);
        mListView = (ListView) findViewById(R.id.listView);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        orders = new ArrayList<>();

        sp = getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sp.edit();

        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        // Get a Realm instance for this thread
        realm = Realm.getInstance(realmConfig);

        mEditText.setText(sp.getString("editText", "")); //第二個參數是取不到值得default value

        //註冊實體鍵盤事件
        mEditText.setOnKeyListener(new View.OnKeyListener() { //參數為Interface
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String text = mEditText.getText().toString();
                editor.putString("editText", text);
                editor.apply();

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    click(v);
                    return true; //攔截，不繼續執行
                }
                return false;
            }
        });

        //註冊虛擬鍵盤事件
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    click(v);
                    return true;
                }
                return false;
            }
        });

        /*int checkedId = sp.getInt("radioGroup", R.id.blackTeaRadioButton);
        mRadioGroup.check(checkedId);

        RadioButton mRadioButton = (RadioButton) findViewById(checkedId);
        drinkName = mRadioButton.getText().toString();

        //註冊radio change事件
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                editor.putInt("radioGroup", checkedId);
                editor.apply();

                RadioButton mRadioButton = (RadioButton) findViewById(checkedId);
                drinkName = mRadioButton.getText().toString();
            }
        });*/

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //view = view 的layout
                Order order = (Order) parent.getAdapter().getItem(position); //因為get是回傳Obj所以要明確指定型態
                //Toast.makeText(MainActivity.this, order.note, Toast.LENGTH_SHORT).show(); //因為是在new的物件裡面，所以this不是指MainActivity
                Snackbar.make(view, order.getNote(), Snackbar.LENGTH_LONG).show(); //新UI設計，優點: 1.可以再click進去提供更多訊息(使用setAction()定義)  2.與其他UI元件是可互動的
            }
        });

        setupListView();
        setupSpinner();

        int selected = sp.getInt("spinnerSelected",0);
        mSpinner.setSelection(selected);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt("spinnerSelected", position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void setupListView(){
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orders); //obj, layout, list
        //mListView.setAdapter(adapter);
        RealmResults results = realm.allObjects(Order.class); //取出物件所有資料
        OrderAdapter adapter = new OrderAdapter(this, results.subList(0,results.size()));
        mListView.setAdapter(adapter);
    }

    void setupSpinner(){
        String[] data = getResources().getStringArray(R.array.storeInfo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, data);

        mSpinner.setAdapter(adapter);
    }

    public void click(View view){ //view為觸發的元素
        note = mEditText.getText().toString();
        String text = note;
        mTextView.setText(text);

        Order order = new Order(); //自訂物件，物件是繼承realmObject
        order.setDrinkName(drinkName);
        order.setNote(note);
        order.setStoreInfo((String) mSpinner.getSelectedItem());

        // Persist your data easily
        realm.beginTransaction();
        realm.copyToRealm(order);
        realm.commitTransaction();

        mEditText.setText("");

        setupListView();
    }

    public void goToMenu(View view){
        Intent intent = new Intent(); //activitty之間的媒介
        intent.setClass(this, DrinkMenuActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d("debug","Main Activity OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("debug", "Main Activity OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("debug", "Main Activity OnPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("debug", "Main Activity OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("debug", "Main Activity OnDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("debug", "Main Activity OnRestart");
    }
}
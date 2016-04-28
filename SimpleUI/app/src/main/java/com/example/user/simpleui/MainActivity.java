package com.example.user.simpleui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {

    TextView mTextView;
    EditText mEditText;
    RadioGroup mRadioGroup;
    ArrayList<Order> orders;
    String drinkName = "black tea";
    String note = "";
    CheckBox mCheckBox;
    ListView mListView;
    Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);
        mEditText = (EditText) findViewById(R.id.editText);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mCheckBox = (CheckBox) findViewById(R.id.hideCheckBox);
        mListView = (ListView) findViewById(R.id.listView);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        orders = new ArrayList<>();

        //註冊實體鍵盤事件
        mEditText.setOnKeyListener(new View.OnKeyListener() { //參數為Interface
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
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

        //註冊radio change事件
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton mRadioButton = (RadioButton) findViewById(checkedId);
                drinkName = mRadioButton.getText().toString();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //view = view 的layout
                Order order = (Order) parent.getAdapter().getItem(position); //因為get是回傳Obj所以要明確指定型態
                //Toast.makeText(MainActivity.this, order.note, Toast.LENGTH_SHORT).show(); //因為是在new的物件裡面，所以this不是指MainActivity
                Snackbar.make(view, order.note, Snackbar.LENGTH_LONG).show(); //新UI設計，優點: 1.可以再click進去提供更多訊息(使用setAction()定義)  2.與其他UI元件是可互動的
            }
        });

        setupListView();
        setupSpinner();
    }

    void setupListView(){
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orders); //obj, layout, list
        //mListView.setAdapter(adapter);

        OrderAdapter adapter = new OrderAdapter(this, orders);
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
        mEditText.setText("");

        Order order = new Order();
        order.drinkName = drinkName;
        order.note = note;
        order.storeInfo = (String) mSpinner.getSelectedItem();
        orders.add(order);

        setupListView();
    }
}
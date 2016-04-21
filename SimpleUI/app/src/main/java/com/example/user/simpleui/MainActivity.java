package com.example.user.simpleui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView mTextView;
    EditText mEditText;
    RadioGroup mRadioGroup;
    String sex = "Male";
    String selectedSex = "Male";
    String name = "";
    CheckBox mCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);
        mEditText = (EditText) findViewById(R.id.editText);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mCheckBox = (CheckBox) findViewById(R.id.hideCheckBox);

        //���U������L�ƥ�
        mEditText.setOnKeyListener(new View.OnKeyListener() { //�ѼƬ�Interface
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    click(v);
                    return true; //�d�I�A���~�����
                }
                return false;
            }
        });

        //���U������L�ƥ�
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    click(v);
                    return true;
                }
                return false;
            }
        });

        //���Uradio change�ƥ�
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.maleRadioButton) {
                    selectedSex = "Male";
                } else if (checkedId == R.id.femaleRadioButton) {
                    selectedSex = "Female";
                }
            }
        });

        //���Ucheckbox�Ŀ�ƥ�
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!name.equals("")) {
                    changeTextView();
                }
            }
        });
    }

    public void click(View view){ //view��Ĳ�o������
        name = mEditText.getText().toString();
        if(!name.equals("")) {
            sex = selectedSex;
            changeTextView();
            mEditText.setText("");
        }
    }

    public void changeTextView(){
        if(mCheckBox.isChecked()){
            String text = name;
            mTextView.setText(text);
        }else{
            String text = name + " sex: " + sex;
            mTextView.setText(text);
        }
    }
}
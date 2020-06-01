package com.example.sau22rane.sqlfunctionality;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class Edit_Budget_Popup extends AppCompatActivity {

    Button edit_save;
    String title,amount,date;
    EditText edit_title, edit_amount;
    TextView edit_date;
    Calendar calendar;
    private int year, month, day;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__budget__popup);
        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            Bundle b = intent.getExtras();
            if(b!=null) {
                title = b.getString("title");
                amount = b.getString("amount");
                date = b.getString("date");
            }
        }
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.8), (int)(height*0.5));
        edit_title = (EditText) findViewById(R.id.edit_budget_title);
        edit_amount = (EditText) findViewById(R.id.edit_budget_amount);
        edit_date = (TextView) findViewById(R.id.edit_budget_date);
        edit_date.setText(date);
        edit_amount.setText(amount);
        edit_title.setText(title);

        edit_save = (Button) findViewById(R.id.edit_save_budget);

        edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edit_title.getText().toString().contains("\'")){
                    edit_title.setError("Title must not contain semicolon!");
                }
                else if( TextUtils.isEmpty(edit_date.getText()) || TextUtils.isEmpty(edit_amount.getText()) || TextUtils.isEmpty(edit_title.getText())) {
                    if (TextUtils.isEmpty(edit_date.getText())) {
                        edit_date.setError("Date required!");
                    }
                    if (TextUtils.isEmpty(edit_amount.getText())) {
                        edit_amount.setError("Amount required!");
                    }
                    if (TextUtils.isEmpty(edit_title.getText())) {
                        edit_title.setError("Title required!");
                    }
                }

                else {
                    SQLite a = new SQLite(getApplicationContext(), "DEFAULT", "BUDGET", 1);
                    int x = Integer.parseInt(edit_amount.getText().toString()) - a.get_amount(title) + a.get_remaining(title);
                    a.update_remaining("BUDGET", title, x);
                    a.update("BUDGET", title, edit_title.getText().toString(), Integer.parseInt(edit_amount.getText().toString()), edit_date.getText().toString());
                    if (!title.equals(edit_title.getText().toString()))
                        a.rename_table(title, edit_title.getText().toString());
                    finish();
                }
            }
        });
        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(Edit_Budget_Popup.this, android.R.style.Theme_Material_Dialog_NoActionBar,  mDateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(41,65,67)));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d("TAG", "onDateSet: mm/dd/yyy: " + day + "/" + month + "/" + year);

                String str = day + "/" + month + "/" + year;
                edit_date.setText(str);
            }
        };

    }
}


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
import android.widget.Toast;

import java.util.Calendar;

public class Edit_Expenditure_Popup extends AppCompatActivity {

    Button edit_save;
    String title,amount,date,table, remaining;
    EditText edit_title, edit_amount;
    TextView edit_date;
    Calendar calendar;
    private int year, month, day;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__expenditure__popup);

        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            Bundle b = intent.getExtras();
            if(b!=null) {
                remaining = b.getString("remaining");
                table = b.getString("table");
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
        getWindow().setLayout((int)(width*0.6), (int)(height*0.3));
        edit_title = (EditText) findViewById(R.id.edit_expenditure_title);
        edit_amount = (EditText) findViewById(R.id.edit_expenditure_amount);
        edit_date = (TextView) findViewById(R.id.edit_expenditure_date);
        edit_date.setText(date);
        edit_amount.setText(amount);
        edit_title.setText(title);

        edit_save = (Button) findViewById(R.id.edit_save_expenditure);

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
                    Integer b = Integer.parseInt(remaining) + Integer.parseInt(amount) - Integer.parseInt(edit_amount.getText().toString());
                    if (b < 0)
                        Toast.makeText(getApplicationContext(), "Amount surpasses the remaining Budget", Toast.LENGTH_LONG).show();

                    else {
                        a.update(table, title, edit_title.getText().toString(), Integer.parseInt(edit_amount.getText().toString()), edit_date.getText().toString());
                        finish();
                    }
                }
            }
        });

        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(Edit_Expenditure_Popup.this, android.R.style.Theme_Material_Dialog_NoActionBar,  mDateSetListener, year,month,day);
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


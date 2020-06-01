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

public class AddExpenditurePopup extends AppCompatActivity {

    Button Add_Budget;
    String title,remaining;
    EditText Expen_title, amount;
    TextView date;
    Calendar calendar;
    private int year, month, day;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenditure_popup);
        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            Bundle b = intent.getExtras();
            if(b!=null) {
                remaining = b.getString("remaining");
                title = b.getString("title");
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
        Expen_title = (EditText) findViewById(R.id.expenditure_title);
        amount = (EditText) findViewById(R.id.expenditure_amount);
        date = (TextView) findViewById(R.id.expenditure_date);
        Add_Budget = (Button) findViewById(R.id.add_expenditure);
        Add_Budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Expen_title.getText().toString().contains("\'")){
                    Expen_title.setError("Title must not contain semicolon!");
                }
                else if( TextUtils.isEmpty(date.getText()) || TextUtils.isEmpty(amount.getText()) || TextUtils.isEmpty(Expen_title.getText())) {
                    if (TextUtils.isEmpty(date.getText())) {
                        date.setError("Date required!");
                    }
                    if (TextUtils.isEmpty(amount.getText())) {
                        amount.setError("Amount required!");
                    }
                    if (TextUtils.isEmpty(Expen_title.getText())) {
                        Expen_title.setError("Title required!");
                    }
                }
                else {
                    SQLite a = new SQLite(getApplicationContext(), "DEFAULT", "Budgets", 1);
                    a.create_expenditure_table(title);
                    if (Integer.parseInt(remaining) < Integer.parseInt(amount.getText().toString()))
                        amount.setError("Amount surpasses the remaining Budget!");
                    else {
                        boolean result = a.add_expenditure(title, Expen_title.getText().toString(), Integer.parseInt(amount.getText().toString()), date.getText().toString());
                        if (result) {
                            Toast.makeText(getApplicationContext(), "Data Added successfully", Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                }
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(AddExpenditurePopup.this, android.R.style.Theme_Material_Dialog_NoActionBar,  mDateSetListener, year,month,day);
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
                date.setText(str);
            }
        };
    }
}

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

public class AddBudgetPopup extends AppCompatActivity {
    Button Add_Budget;
    EditText title, amount;
    TextView date;
    Calendar calendar;
    private int year, month, day;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget_popup);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.8), (int)(height*0.5));
        title = (EditText) findViewById(R.id.budget_title);
        amount = (EditText) findViewById(R.id.budget_amount);
        date = (TextView) findViewById(R.id.budget_date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(AddBudgetPopup.this, android.R.style.Theme_Material_Dialog_NoActionBar,  mDateSetListener, year,month,day);
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


        Add_Budget = (Button) findViewById(R.id.add_budget);
        Add_Budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().contains("\'")){
                    title.setError("Title must not contain semicolon!");
                }
                else if( TextUtils.isEmpty(date.getText()) || TextUtils.isEmpty(amount.getText()) || TextUtils.isEmpty(title.getText())) {
                    if (TextUtils.isEmpty(date.getText())) {
                        date.setError("Date required!");
                    }
                    if (TextUtils.isEmpty(amount.getText())) {
                        amount.setError("Amount required!");
                    }
                    if (TextUtils.isEmpty(title.getText())) {
                        title.setError(" A Tittle is required");
                    }
                }
                else {
                    SQLite a = new SQLite(getApplicationContext(), "DEFAULT", "Budgets", 1);
                    a.create_budget_table();
                    a.create_expenditure_table(title.getText().toString());
                    boolean result = a.add_budget(title.getText().toString(), Integer.parseInt(amount.getText().toString()), date.getText().toString());
                    if (result) {
                        Toast.makeText(getApplicationContext(), "Successfully added Data", Toast.LENGTH_LONG).show();
                    }
                    finish();
                }
            }
        });
    }
}

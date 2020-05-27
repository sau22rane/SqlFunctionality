package com.example.sau22rane.sqlfunctionality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddExpenditurePopup extends AppCompatActivity {

    Button Add_Budget;
    String username,title,remaining;
    EditText Expen_title, amount, date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenditure_popup);
        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            Bundle b = intent.getExtras();
            if(b!=null) {
                remaining = b.getString("remaining");
                username = b.getString("username");
                title = b.getString("title");
            }
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.8), (int)(height*0.6));
        Expen_title = (EditText) findViewById(R.id.expenditure_title);
        amount = (EditText) findViewById(R.id.expenditure_amount);
        date = (EditText) findViewById(R.id.expenditure_date);
        Add_Budget = (Button) findViewById(R.id.add_expenditure);
        Add_Budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLite a = new SQLite(getApplicationContext(),username,"Budgets", 1);
                a.create_expenditure_table(title);
                if(Integer.parseInt(remaining)<Integer.parseInt(amount.getText().toString()))
                    Toast.makeText(getApplicationContext(),"Amount surpasses the remaining Budget "+remaining,Toast.LENGTH_LONG).show();
                else {
                    boolean result = a.add_expenditure(title, Expen_title.getText().toString(), Integer.parseInt(amount.getText().toString()), date.getText().toString());
                    if (result) {
                        Toast.makeText(getApplicationContext(), "Data Added successfully", Toast.LENGTH_LONG).show();
                    }
                    finish();
                }
            }
        });
    }
}

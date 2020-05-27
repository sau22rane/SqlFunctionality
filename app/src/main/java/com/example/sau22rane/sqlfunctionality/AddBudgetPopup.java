package com.example.sau22rane.sqlfunctionality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddBudgetPopup extends AppCompatActivity {
    Button Add_Budget;
    String username;
    EditText title, amount, date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget_popup);
        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            Bundle b = intent.getExtras();
            if(b!=null) {
                username = b.getString("username");
            }
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.8), (int)(height*0.6));
        title = (EditText) findViewById(R.id.budget_title);
        amount = (EditText) findViewById(R.id.budget_amount);
        date = (EditText) findViewById(R.id.budget_date);
        Add_Budget = (Button) findViewById(R.id.add_budget);
        Add_Budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLite a = new SQLite(getApplicationContext(),username,"Budgets", 1);
                a.create_budget_table();
                boolean result = a.add_budget(title.getText().toString(), Integer.parseInt(amount.getText().toString()),date.getText().toString());
                if(result){
                    Toast.makeText(getApplicationContext(),"Data Added successfully",Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });
    }
}

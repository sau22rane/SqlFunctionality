package com.example.sau22rane.sqlfunctionality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Edit_Expenditure_Popup extends AppCompatActivity {

    Button edit_save;
    String username,title,amount,date,table, remaining;
    EditText edit_title, edit_amount, edit_date;
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
                username = b.getString("username");
                title = b.getString("title");
                amount = b.getString("amount");
                date = b.getString("date");
            }
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.8), (int)(height*0.6));
        edit_title = (EditText) findViewById(R.id.edit_expenditure_title);
        edit_amount = (EditText) findViewById(R.id.edit_expenditure_amount);
        edit_date = (EditText) findViewById(R.id.edit_expenditure_date);
        edit_date.setText(date);
        edit_amount.setText(amount);
        edit_title.setText(title);

        edit_save = (Button) findViewById(R.id.edit_save_expenditure);

        edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLite a = new SQLite(getApplicationContext(),username,"BUDGET", 1);
                Integer b = Integer.parseInt(remaining)+Integer.parseInt(amount)-Integer.parseInt(edit_amount.getText().toString());
                if(b<0)
                    Toast.makeText(getApplicationContext(),"Amount surpasses the remaining Budget",Toast.LENGTH_LONG).show();
                //Log.d("TAG","The value is"+b+" "+username);
                else {
                    a.update(table, title, edit_title.getText().toString(), Integer.parseInt(edit_amount.getText().toString()), edit_date.getText().toString());
                    finish();
                }
            }
        });

    }
}


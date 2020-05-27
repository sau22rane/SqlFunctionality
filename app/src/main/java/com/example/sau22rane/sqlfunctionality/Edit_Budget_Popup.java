package com.example.sau22rane.sqlfunctionality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Edit_Budget_Popup extends AppCompatActivity {

    Button edit_save;
    String username,title,amount,date;
    EditText edit_title, edit_amount, edit_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__budget__popup);
        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            Bundle b = intent.getExtras();
            if(b!=null) {
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
        edit_title = (EditText) findViewById(R.id.edit_budget_title);
        edit_amount = (EditText) findViewById(R.id.edit_budget_amount);
        edit_date = (EditText) findViewById(R.id.edit_budget_date);
        edit_date.setText(date);
        edit_amount.setText(amount);
        edit_title.setText(title);

        edit_save = (Button) findViewById(R.id.edit_save_budget);

        edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLite a = new SQLite(getApplicationContext(),username,"BUDGET", 1);
                int x = Integer.parseInt(edit_amount.getText().toString()) - a.get_amount(title) + a.get_remaining(title);
                a.update_remaining("BUDGET", title, x);
                a.update("BUDGET",title,edit_title.getText().toString(),Integer.parseInt(edit_amount.getText().toString()),edit_date.getText().toString());
                if(!title.equals(edit_title.getText().toString()))
                    a.rename_table(title,edit_title.getText().toString());
                finish();
            }
        });

    }
}


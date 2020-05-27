package com.example.sau22rane.sqlfunctionality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OptionsBudgetPopup extends AppCompatActivity {

    Button view_expen,delete_budget;
    FloatingActionButton edit_budget_fab;
    String username,title,amount,date,remaining;
    TextView option_title, option_amount, option_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_budget_popup);

        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            Bundle b = intent.getExtras();
            if(b!=null) {
                remaining = b.getString("remaining");
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
        option_title = (TextView) findViewById(R.id.option_budget_title);
        option_amount = (TextView) findViewById(R.id.option_budget_amount);
        option_date = (TextView) findViewById(R.id.option_budget_date);
        option_date.setText(date);
        option_amount.setText(amount);
        option_title.setText(title);

        view_expen = (Button) findViewById(R.id.option_view_expenditure);
        delete_budget = (Button) findViewById(R.id.option_delete_budget);
        edit_budget_fab = (FloatingActionButton) findViewById(R.id.option_edit_budget_fab);
        view_expen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Expenditure_activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("remaining",remaining);
                bundle.putString("username",username);
                bundle.putString("title",title);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        });

        edit_budget_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Edit_Budget_Popup.class);
                Bundle bundle = new Bundle();
                bundle.putString("remaining",remaining);
                bundle.putString("username",username);
                bundle.putString("title",title);
                bundle.putString("amount",amount);
                bundle.putString("date",date);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        });

        delete_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLite a = new SQLite(getApplicationContext(),username,"BUDGET", 1);
                a.delete_row("BUDGET",title);
                a.delete_table(title);
                finish();
            }
        });

    }
}


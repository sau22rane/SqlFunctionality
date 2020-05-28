package com.example.sau22rane.sqlfunctionality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OptionExpenditurePopup extends AppCompatActivity {

    Button delete_expen;
    FloatingActionButton edit_expen_fab;
    String title,amount,date,table, remaining;
    TextView option_title, option_amount, option_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_expenditure_popup);

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
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.6), (int)(height*0.3));
        option_title = (TextView) findViewById(R.id.option_expenditure_title);
        option_amount = (TextView) findViewById(R.id.option_expenditure_amount);
        option_date = (TextView) findViewById(R.id.option_expenditure_date);
        option_date.setText(date);
        option_amount.setText(amount);
        option_title.setText(title);

        delete_expen = (Button) findViewById(R.id.option_delete_expenditure);
        edit_expen_fab = (FloatingActionButton) findViewById(R.id.option_edit_expenditure_fab);


        edit_expen_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Edit_Expenditure_Popup.class);
                Bundle bundle = new Bundle();
                bundle.putString("table",table);
                bundle.putString("remaining",remaining);
                bundle.putString("title",title);
                bundle.putString("amount",amount);
                bundle.putString("date",date);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        });

        delete_expen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLite a = new SQLite(getApplicationContext(),"DEFAULT","BUDGET", 1);
                a.delete_row(table,title);
                finish();
            }
        });

    }
}


package com.example.sau22rane.sqlfunctionality;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Expenditure_activity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClicked {

    boolean isEmpty = false;
    RecyclerView Recycle;
    ArrayList<String> Expenditure;
    ArrayList<String> Expenditures_title, Expenditures_date, Expenditures_amount;
    String title, remaining;
    SQLite a;
    int used_amount;
    TextView remaining_amount;
    RecyclerViewAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure_activity);
        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            Bundle b = intent.getExtras();
            if(b!=null) {
                remaining = b.getString("remaining");
                title = b.getString("title");
            }

        }
        FloatingActionButton fab = findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AddExpenditurePopup.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("title",title);
                mBundle.putString("remaining",remaining);
                i.putExtras(mBundle);
                startActivityForResult(i, 3);
            }
        });
        remaining_amount = (TextView) findViewById(R.id.remaining_amount);
        remaining_amount.setText(remaining);
        Recycle = (RecyclerView) findViewById(R.id.Recycle_expen);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        Recycle.setLayoutManager(linearLayoutManager);
        Expenditure = new ArrayList<String>();
        Expenditures_title = new ArrayList<String>();
        Expenditures_amount = new ArrayList<String>();
        Expenditures_date = new ArrayList<String>();
        customAdapter = new RecyclerViewAdapter(getApplicationContext(), Expenditure);

        a = new SQLite(getApplicationContext(),"DEFAULT",title, 1);
        getData();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==3)
        {
            getData();
        }
    }


    private void getData(){
        Cursor res = a.get_data(title);

        Expenditure.clear();
        Expenditures_title.clear();
        Expenditures_amount.clear();
        Expenditures_date.clear();
        used_amount = 0;
        if(res.getCount() == 0){
            Expenditure.add("No Expenditures Available. ADD new!");
            isEmpty = true;
        }
        else{
            isEmpty = false;
            while(res.moveToNext()){
                StringBuffer buffer = new StringBuffer();
                buffer.append("Title : "+res.getString(1));
                buffer.append("\nAmount : "+res.getString(2));
                buffer.append("\nDate : "+res.getString(3));
                Expenditure.add(String.valueOf(buffer));
                Expenditures_title.add(res.getString(1));
                Expenditures_amount.add(res.getString(2));
                Expenditures_date.add(res.getString(3));
                used_amount += Integer.parseInt(res.getString(2));
            }
        }
        customAdapter.setOnClick((RecyclerViewAdapter.OnItemClicked) Expenditure_activity.this);
        Recycle.setAdapter(customAdapter);
        int total_amt = a.get_amount(title);
        a.update_remaining("BUDGET",title,total_amt-used_amount);
        remaining = String.valueOf(total_amt-used_amount);
        remaining_amount.setText("Remaining amount: "+remaining);
    }

    @Override
    public void onItemClick(int position) {
        if(!isEmpty) {
            Intent i = new Intent(getApplicationContext(), OptionExpenditurePopup.class);
            Bundle bundle = new Bundle();
            bundle.putString("table", title);
            bundle.putString("remaining", remaining);
            bundle.putString("title", Expenditures_title.get(position));
            bundle.putString("amount", Expenditures_amount.get(position));
            bundle.putString("date", Expenditures_date.get(position));
            i.putExtras(bundle);
            startActivityForResult(i, 3);
        }
    }
}

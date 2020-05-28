package com.example.sau22rane.sqlfunctionality;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Budget_activity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClicked  {

    boolean isEmpty = false;
    RecyclerView Recycle;
    ArrayList<String> Budgets;
    ArrayList<String> Budgets_name, Budgets_date;
    ArrayList<Integer> Budgets_amount, Budgets_remaining;
    SQLite a;
    RecyclerViewAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_activity);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AddBudgetPopup.class);
                startActivityForResult(i, 1);
            }
        });

        Recycle = (RecyclerView) findViewById(R.id.Recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        Recycle.setLayoutManager(linearLayoutManager);
        Budgets = new ArrayList<String>();
        Budgets_name = new ArrayList<String>();
        Budgets_date = new ArrayList<String>();
        Budgets_amount = new ArrayList<Integer>();
        Budgets_remaining = new ArrayList<Integer>();
        customAdapter = new RecyclerViewAdapter(getApplicationContext(), Budgets);

        a = new SQLite(getApplicationContext(),"DEFAULT","Budgets", 1);
        a.create_budget_table();
        getData();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==1)
        {
            getData();
        }
        else if(requestCode==2)
        {
            getData();
        }
    }


    private void getData(){
        Cursor res = a.get_data("BUDGET");

        Budgets.clear();
        Budgets_name.clear();
        Budgets_date.clear();
        Budgets_amount.clear();
        Budgets_remaining.clear();
        if(res.getCount() == 0){
            Budgets.add("No Budgets Available. ADD new!");
            isEmpty = true;
        }
        else{
            isEmpty = false;
            while(res.moveToNext()){
                StringBuffer buffer = new StringBuffer();
                buffer.append("Title : "+res.getString(1));
                buffer.append("\nAmount : "+res.getString(2));
                buffer.append("\nRemaining amount : "+res.getString(3));
                buffer.append("\nDate : "+res.getString(4));
                Budgets_name.add(res.getString(1));
                Budgets_amount.add(Integer.parseInt(res.getString(2)));
                Budgets_remaining.add(Integer.parseInt(res.getString(3)));
                Budgets_date.add(res.getString(4));
                Budgets.add(String.valueOf(buffer));
            }

        }
        customAdapter.setOnClick((RecyclerViewAdapter.OnItemClicked) Budget_activity.this);
        Recycle.setAdapter(customAdapter);
    }

    @Override
    public void onItemClick(int position) {
        if(!isEmpty) {
            Intent i = new Intent(getApplicationContext(), OptionsBudgetPopup.class);
            Bundle bundle = new Bundle();
            bundle.putString("title", Budgets_name.get(position));
            bundle.putString("remaining", Budgets_remaining.get(position).toString());
            bundle.putString("amount", Budgets_amount.get(position).toString());
            bundle.putString("date", Budgets_date.get(position));
            i.putExtras(bundle);
            startActivityForResult(i, 2);
        }
    }
}

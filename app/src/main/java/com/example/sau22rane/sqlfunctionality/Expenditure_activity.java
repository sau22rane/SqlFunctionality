package com.example.sau22rane.sqlfunctionality;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Expenditure_activity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClicked, RecyclerViewAdapter.OnDeleteClicked, RecyclerViewAdapter.OnEditClicked{

    boolean isEmpty = false;
    RecyclerView Recycle;
    ArrayList<String> Expenditure;
    ArrayList<String> Expenditures_title, Expenditures_date, Expenditures_amount;
    String title, remaining;
    SQLite a;
    int previous = -1;
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
                previous = -1;
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
                buffer.append(",Amount : "+res.getString(2));
                buffer.append(",Date : "+res.getString(3));
                Expenditure.add(String.valueOf(buffer));
                Expenditures_title.add(res.getString(1));
                Expenditures_amount.add(res.getString(2));
                Expenditures_date.add(res.getString(3));
                used_amount += Integer.parseInt(res.getString(2));
            }
        }
        customAdapter.setOnClick((RecyclerViewAdapter.OnItemClicked) Expenditure_activity.this);
        customAdapter.setOnDelete((RecyclerViewAdapter.OnDeleteClicked) Expenditure_activity.this);
        customAdapter.setOnEdit((RecyclerViewAdapter.OnEditClicked) Expenditure_activity.this);
        Recycle.setAdapter(customAdapter);
        int total_amt = a.get_amount(title);
        a.update_remaining("BUDGET",title,total_amt-used_amount);
        remaining = String.valueOf(total_amt-used_amount);
        remaining_amount.setText("Remaining amount: "+remaining);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onItemClick(int position) {
        if(!isEmpty) {
            if(previous == -1) {
                previous = position;
                FloatingActionButton edit;
                CoordinatorLayout coolo;
                Button view;
                view = (Button) Recycle.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.view);
                view.setVisibility(View.GONE);
                coolo = (CoordinatorLayout) Recycle.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.expandable);
                edit = (FloatingActionButton) Recycle.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.edit);
                if (edit.getVisibility() == View.GONE) {
                    edit.setVisibility(View.VISIBLE);
                } else {
                    edit.setVisibility(View.GONE);
                }
                TransitionManager.beginDelayedTransition(coolo, new AutoTransition());
                if (coolo.getVisibility() == View.GONE)
                    coolo.setVisibility(View.VISIBLE);
                else
                    coolo.setVisibility(View.GONE);
            }
            else if(previous != position){
                FloatingActionButton edit;
                CoordinatorLayout coolo;
                FloatingActionButton editp;
                CoordinatorLayout coolop;
                Button view;
                view = (Button) Recycle.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.view);
                view.setVisibility(View.GONE);
                coolop = (CoordinatorLayout) Recycle.findViewHolderForAdapterPosition(previous).itemView.findViewById(R.id.expandable);
                editp = (FloatingActionButton) Recycle.findViewHolderForAdapterPosition(previous).itemView.findViewById(R.id.edit);
                coolo = (CoordinatorLayout) Recycle.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.expandable);
                edit = (FloatingActionButton) Recycle.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.edit);
                if (edit.getVisibility() == View.GONE) {
                    edit.setVisibility(View.VISIBLE);
                }
                if (editp.getVisibility() == View.VISIBLE) {
                    editp.setVisibility(View.GONE);
                }
                TransitionManager.beginDelayedTransition(coolo, new AutoTransition());
                if (coolo.getVisibility() == View.GONE)
                    coolo.setVisibility(View.VISIBLE);
                if(coolop.getVisibility() == View.VISIBLE)
                    coolop.setVisibility(View.GONE);
                previous = position;
            }
        }
    }

    @Override
    public void onDelete(int position) {
        SQLite a = new SQLite(getApplicationContext(),"DEFAULT","BUDGET", 1);
        a.delete_row(title,Expenditures_title.get(position));
        getData();
        previous = -1;
    }


    @Override
    public void onEdit(int position) {
        Intent i = new Intent(getApplicationContext(), Edit_Expenditure_Popup.class);
        Bundle bundle = new Bundle();
        bundle.putString("table",title);
        bundle.putString("remaining",remaining);
        bundle.putString("title",Expenditures_title.get(position));
        bundle.putString("amount",Expenditures_amount.get(position));
        bundle.putString("date",Expenditures_date.get(position));
        i.putExtras(bundle);
        startActivityForResult(i,3);
        previous = -1;
    }
}

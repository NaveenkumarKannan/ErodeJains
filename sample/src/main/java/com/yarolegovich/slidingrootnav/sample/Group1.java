package com.yarolegovich.slidingrootnav.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Group1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group1);

        LinearLayout l1= (LinearLayout) findViewById(R.id.l1);
        LinearLayout l2= (LinearLayout) findViewById(R.id.l2);
        LinearLayout l3= (LinearLayout) findViewById(R.id.l3);

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Group1.this,TicketBooking.class);
                startActivity(i);
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Group1.this,vijay.class);
                startActivity(i);
            }
        });
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Group1.this,ajith.class);
                startActivity(i);
            }
        });
    }
}

package com.example.mytodo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    public static String p="", t="", n="", c="";
    TextView t1, t2, t3, t4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().hide();

        t1 = findViewById(R.id.plan);
        t2 = findViewById(R.id.time);
        t3 = findViewById(R.id.note);
        t4 = findViewById(R.id.category);
        t1.setText(p);
        t2.setText(t);
        t3.setText(n);
        t4.setText(c);
    }
}
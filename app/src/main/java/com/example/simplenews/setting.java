package com.example.simplenews;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import  com.example.simplenews.R;
import androidx.appcompat.app.AppCompatActivity;

public class setting extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Button  set_color=findViewById(R.id.set_color);
        set_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheme(R.style.AppThemeGreen);
                recreate();
            }
        });
    }
}

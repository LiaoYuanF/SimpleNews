package com.example.simplenews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WelcomeActivity extends Activity {
    private Button skipWelcome;
    private final int WELCOME_TIME = 5;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcom);
        skipWelcome = findViewById(R.id.skip_welcome);
        thread.start();
        skipWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = Message.obtain(handler);
                message.what = 0;
                handler.sendMessage(message);
                finish();
            }
        });
    }

    final Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try{
                for(int i = WELCOME_TIME; i >= 0; i--) {
                    if(thread.isInterrupted()) break;
                    Thread.sleep(1000);
                    Message message = Message.obtain(handler);
                    message.what = i;
                    handler.sendMessage(message);
                }
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    });

    final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what != 0) {
                skipWelcome.setText("跳过 " + msg.what);
            }
            else {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                thread.interrupt();
                finish();
            }
        }
    };
}

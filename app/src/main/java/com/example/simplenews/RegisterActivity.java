package com.example.simplenews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {
    EditText usernameView;
    EditText passwordView;
    EditText phoneView;
    Button register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameView = findViewById(R.id.register_username);
        passwordView = findViewById(R.id.register_password);
        phoneView = findViewById(R.id.register_phone);
        register_btn=findViewById(R.id.register_button);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = usernameView.getText().toString();
                String psw = passwordView.getText().toString();
                String phone = phoneView.getText().toString();
                RegisterTask myTask = new RegisterTask(uname, psw, phone);
                myTask.execute();
            }
        });
    }

    private class RegisterTask extends AsyncTask<Void, Void, Boolean>{
        String uname;
        String psw;
        String phone;

        public RegisterTask(String uname, String psw, String phone){
            this.uname = uname;
            this.psw = psw;
            this.phone = phone;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String uri = QueryUtils.makeUri("register", uname, psw, phone);
            String result = "fail";
            try {
                String response = QueryUtils.makeHTTPRequest(new URL(uri));
                if(TextUtils.isEmpty(response)) {
                    Log.e("RegisterActivity", "empty response");
                    return false;
                }
                JSONObject jsonObject = new JSONObject(response);
                result = jsonObject.getString("result");
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return result.equals("success");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                Toast toast = Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 100);
                toast.show();
                finish();
            }
            else{
                Toast toast = Toast.makeText(RegisterActivity.this, "注册失败！", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 100);
                toast.show();
                return;
            }
            updateUI();
        }
    }

    private void updateUI() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}

package com.example.simplenews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private UserLoginTask mAuthTask = null;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private EditText username, password;
    private CheckBox rememberPass; //记住密码
    private CheckBox autoLogin;    //自动登录
    private Button login_btn, register_btn;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        rememberPass = findViewById(R.id.remember_password);
        autoLogin = findViewById(R.id.auto_login);
        login_btn = findViewById(R.id.login_button);
        register_btn = findViewById(R.id.login_register_button);

        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL){
                    attempLogin();
                    return true;
                }
                return false;
            }
        });

        //记住密码与自动登录
        boolean isRemember = preferences.getBoolean("remember_password",false);
        boolean isAutoLogin = preferences.getBoolean("auto_Login",false);
        if(isRemember){
            String uname = preferences.getString("username","");
            String pwd = preferences.getString("password","");
            username.setText(uname);
            password.setText(pwd);
            rememberPass.setChecked(true);
            if(isAutoLogin){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);
                Toast.makeText(getApplicationContext(),"自动登录",Toast.LENGTH_SHORT).show();
            }
        }

        //登录按钮
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempLogin();
            }
        });

        //注册按钮
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void attempLogin() {
        if(mAuthTask != null){
            return;
        }

        username.setError(null);
        password.setError(null);

        //在登录时尝试存储值
        String uname = username.getText().toString();
        String psw = password.getText().toString();
        editor = preferences.edit();
        if(autoLogin.isChecked()){
            editor.putBoolean("auto_Login",true);
        }
        if(rememberPass.isChecked()){
            editor.putBoolean("remember_password",true);
            editor.putString("username",uname);
            editor.putString("password",psw);
        } else {
            editor.clear();
        }
        editor.apply();

        boolean cancel = false;
        View focusView = null;

        //检查用户输入的密码是否合法
        if(!TextUtils.isEmpty(psw) && !isPasswordValid(psw)){
            password.setError(getString(R.string.error_invalid_password));
            focusView = password;
            cancel = true;
        }

        if(!TextUtils.isEmpty(uname) && !isUsernameValid(uname)){
            username.setError(getString(R.string.error_invalid_username));
            focusView = username;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
        } else {
            showProgress(true);
            mAuthTask = new UserLoginTask(uname,psw);
            mAuthTask.execute((Void)null);
        }
    }

    private boolean isUsernameValid(String uname) {
        return uname.length() <= 20;
    }

    private boolean isPasswordValid(String psw) {
        return psw.length() >= 6;
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean>{

        private final String uname;
        private final String psw;

        public UserLoginTask(String uname, String psw) {
            this.uname = uname;
            this.psw = psw;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String uri = QueryUtils.makeUri("login", uname, psw, null);
            String result = "fail";
            try {
                String response = QueryUtils.makeHTTPRequest(new URL(uri));
                if(TextUtils.isEmpty(response)) {
                    Log.e("LoginActivity", "empty response");
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
        protected void onPostExecute(Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if(success){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast toast = Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 100);
                toast.show();
                return;
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    //显示进度UI并隐藏登陆表单
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
        else {
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}

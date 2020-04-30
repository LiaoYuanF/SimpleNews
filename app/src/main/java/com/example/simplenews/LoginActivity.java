package com.example.simplenews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {
    private UserLoginTask mAuthTask = null;

    private static final int REQUEST_READ_CONTACTS = 0;

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
        register_btn = findViewById(R.id.register_button);

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

    public void LoginRequest(final String uname, final String psw){
        String url = "http://http://47.96.142.235:8080/SimpleNews/LoginServlet?username="+uname+"&password="+psw;
        String tag = "Login";

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                    String result = jsonObject.getString("result");
                    if (result.equals("success")) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "欢迎登录SimpleNews", Toast.LENGTH_LONG).show();
                    } else {
                        if (result.equals("fail")) {
                            Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    Log.e("TAG", e.getMessage(), e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", uname);
                params.put("password", psw);
                return params;
            }
        };

        request.setTag(tag);
        requestQueue.add(request);
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean>{

        private final String uname;
        private final String psw;

        public UserLoginTask(String uname, String psw) {
            this.uname =uname;
            this.psw =psw;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            //通过网络请求服务器验证用户信息

            try{
                LoginRequest(uname, psw);
                Thread.sleep(3000);
            } catch (InterruptedException e){
                return false;
            }

            return true;
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
                password.setError(getString(R.string.error_incorrect_password));
                password.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

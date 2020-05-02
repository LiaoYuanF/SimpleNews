package com.example.simplenews;

import android.net.Uri;
import android.renderscript.ScriptGroup;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class QueryUtils {
    private static final String LOGIN_REQUEST_URL = "http://47.96.142.235:8080/SimpleNews/LoginServlet?";
    private static final String REGISTER_REQUEST_URL = "http://47.96.142.235:8080/SimpleNews/RegisterServlet?";

    private QueryUtils(){}

    public static String makeUri(String type, String uname, String psw, String phone){
        Uri baseUri;
        if(type.equals("login")) {
            baseUri = Uri.parse(LOGIN_REQUEST_URL);
        }
        else {
            baseUri = Uri.parse(REGISTER_REQUEST_URL);
        }
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("username", uname);
        uriBuilder.appendQueryParameter("password", psw);
        if(type.equals("register")) uriBuilder.appendQueryParameter("phone", phone);

        return uriBuilder.toString();
    }

    public static String makeHTTPRequest(URL url) throws IOException{
        String JSONResponse = "";
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        if(url == null) return JSONResponse;
        try{
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if(responseCode == 200){
                inputStream = connection.getInputStream();
                JSONResponse = readInputStream(inputStream);
            }
            else{
                Log.e("QueryUtils", "Response Code" + responseCode);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if(connection != null){
                connection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return JSONResponse;
    }

    private static String readInputStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = reader.readLine();
            while(line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}

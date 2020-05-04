package com.example.simplenews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        List<String> list = new ArrayList<>();

        System.out.println("查看收藏");
        MySQLiteOpenHelper dbHelper_query = new MySQLiteOpenHelper(FavoriteActivity.this,"favorite",2);
        SQLiteDatabase sqliteDatabase_query = dbHelper_query.getReadableDatabase();
        Cursor cursor = sqliteDatabase_query.rawQuery("select * from favorite",null);
        String id = null;
        String newstitle = null;
        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex("id"));
            newstitle = cursor.getString(cursor.getColumnIndex("newstitle"));
            list.add("收藏的新闻:"+"id: "+id+"  "+"title: "+newstitle);
            System.out.println("收藏的新闻:"+"id: "+id+"  "+"title: "+newstitle);
        }
        sqliteDatabase_query.close();

        ArrayAdapter<String> favoriteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        ListView listView = findViewById(R.id.favorite_listView);
        listView.setAdapter(favoriteAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(FavoriteActivity.this, "你点你吗呢", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(FavoriteActivity.this, NewsDetailActivity.class);
//                startActivity(intent);
            }
        });
    }



}

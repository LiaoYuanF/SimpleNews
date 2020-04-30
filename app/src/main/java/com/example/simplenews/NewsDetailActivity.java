package com.example.simplenews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewsDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        final int i=1;
        String s="一条历史记录";
        add_history(i,s);
        Button favorite_btn = findViewById(R.id.favorite);
        Button favorite_cancel = findViewById(R.id.cancel);
        Button favorite_add = findViewById(R.id.add);

        favorite_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(NewsDetailActivity.this, FavoriteActivity.class);
                startActivity(intent);
            }
        });

        favorite_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("删除数据");
                MySQLiteOpenHelper dbHelper_cancel = new MySQLiteOpenHelper(NewsDetailActivity.this,"favorite",2);
                SQLiteDatabase sqliteDatabase_cancel = dbHelper_cancel.getWritableDatabase();
                String sql = "delete from favorite where id="+i;
                sqliteDatabase_cancel.execSQL(sql);
                sqliteDatabase_cancel.close();
            }
        });

        favorite_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("插入数据");
                MySQLiteOpenHelper dbHelper_add = new MySQLiteOpenHelper(NewsDetailActivity.this,"favorite",2);
                SQLiteDatabase  sqliteDatabase_add = dbHelper_add.getWritableDatabase();
                ContentValues values1 = new ContentValues();
                values1.put("id", i);
                values1.put("newstitle", "carson");
                sqliteDatabase_add.insert("favorite", null, values1);
                sqliteDatabase_add.close();
            }
        });
    }
    public void add_history(int i,String s){
        System.out.println("添加浏览记录");
        try {
            MySQLiteOpenHelper dbHelper_add = new MySQLiteOpenHelper(NewsDetailActivity.this, "history", 2);
            SQLiteDatabase sqliteDatabase_add = dbHelper_add.getWritableDatabase();
            ContentValues values1 = new ContentValues();
            values1.put("id", i);
            values1.put("newstitle", s);
            sqliteDatabase_add.insert("history", null, values1);
            sqliteDatabase_add.close();
        } catch (Exception e){
            System.out.println("已经浏览过了");
        }


    }
}

package com.example.simplenews;

import androidx.appcompat.app.AppCompatActivity;

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

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        List<String> list = new ArrayList<String>();
        System.out.println("查看历史");

        MySQLiteOpenHelper dbHelper_query = new MySQLiteOpenHelper(HistoryActivity.this,"history",2);
        SQLiteDatabase sqliteDatabase_query = dbHelper_query.getReadableDatabase();
        Cursor cursor = sqliteDatabase_query.rawQuery("select * from history",null);
        String id = null;
        String newstitle = null;
        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex("id"));
            newstitle = cursor.getString(cursor.getColumnIndex("newstitle"));
            list.add("浏览历史:"+"id: "+id+"  "+"newstitle: "+newstitle);
            System.out.println("浏览历史:"+"id: "+id+"  "+"newstitle: "+newstitle);
        }
        sqliteDatabase_query.close();

        ArrayAdapter<String> favoriteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        ListView listView = (ListView) findViewById(R.id.history_listView);
        listView.setAdapter(favoriteAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HistoryActivity.this, "你点你吗呢", Toast.LENGTH_SHORT).show(); }
        });
    }
}

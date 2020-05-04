package com.example.simplenews;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {


    //数据库版本号
    private static Integer Version = 1;

    //在SQLiteOpenHelper的子类当中，必须有该构造函数
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                              int version) {
        //必须通过super调用父类当中的构造函数
        super(context, name, factory, version);
    }
    //参数说明
    //context:上下文对象
    //name:数据库名称
    //param:factory
    //version:当前数据库的版本，值必须是整数并且是递增的状态

    public MySQLiteOpenHelper(Context context,String name,int version)
    {
        this(context,name,null,version);
    }

    public MySQLiteOpenHelper(Context context,String name)
    {
        this(context, name, Version);
    }

    //当数据库创建的时候被调用
    @Override
    public void onCreate(SQLiteDatabase db) {

        System.out.println("创建数据库和表");

        String sql = "create table favorite(id int primary key,newstitle varchar(200))";
        db.execSQL(sql);

        sql = "create table history(id int primary key,newstitle varchar(200))";
        db.execSQL(sql);
    }

    //数据库升级时调用
    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade（）方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("更新数据库版本为:"+newVersion);
    }
}



package cn.suiseiseki.www.suiseiseeker.model;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shuikeyi on 2016/7/18.
 * address:shuikeyi92@gmail.com
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "suiseiseki_wordpress.db";
    private final static int VERSION = 1;

    private final static String TAG = MyDatabaseHelper.class.getSimpleName();

    public MyDatabaseHelper(Context context)
    {
        super(context,DB_NAME,null,VERSION);
    }

    /**
     * Create a database
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String sql = "CREATE TABLE if not exist POST_TABLE (post_id int primary key,post_title char(100),post_author char(31),post_date char(50),post_content longtext);";
        sqLiteDatabase.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int oldversion,int newversion)
    {

    }

}

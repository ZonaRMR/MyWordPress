package cn.suiseiseki.www.suiseiseeker.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Suiseiseki/shuikeyi on 2016/5/5.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "post_provider.db";
    public static final String POST_TABLE_NAME = "post";
    private static final String TAG = DbOpenHelper.class.getSimpleName();

    private static final int DB_VERSION = 92;

    private String CREATE_POST_TABLE = "CREATE TABLE IF NOT EXISTS " + POST_TABLE_NAME + "(_id INTEGER PRIMARY KEY," + "post TEXT)";

    public DbOpenHelper(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_POST_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {

    }

}

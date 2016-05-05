package cn.suiseiseki.www.suiseiseeker.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import cn.suiseiseki.www.suiseiseeker.tools.DbOpenHelper;

/**
 * Created by Suiseiseki/shuikeyi on 2016/5/5.
 */
public class PostProvider extends ContentProvider {

    private static final String TAG = "PostProvider";

    public static final String AUTHORITY = "cn.suiseiseki.www.suiseiseeker.postprovider";
    public static final Uri POST_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/post");

    public static final int POST_URI_CODE = 0;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private Context mContext;
    private SQLiteDatabase mSQLiteDatabase;

    static {
      sUriMatcher.addURI(AUTHORITY,"post",POST_URI_CODE);
    }

    private String getTableName(Uri uri)
    {
        String tableName = null;
        switch (sUriMatcher.match(uri))
        {
            case POST_URI_CODE:
                tableName = DbOpenHelper.POST_TABLE_NAME;
                break;
            default:break;
        }
        return tableName;
    }

    /**
     * onCreate:do some preparation work
     */
    @Override
    public boolean onCreate()
    {
        Log.d(TAG,"onCreate, current thread:" + Thread.currentThread().getName() );
        mContext = getContext();
        initProviderData();
        return true;
    }

    private void initProviderData()
    {
        mSQLiteDatabase = new DbOpenHelper(mContext).getWritableDatabase();
    }

    /**
     * CRUD method
     */
    @Override
    public Cursor query(Uri uri,String[] projection,String selection,String[] selectionArgs,String sortOrder)
    {
        Log.d(TAG,"query,current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if(table == null)
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        return mSQLiteDatabase.query(table,projection,selection,selectionArgs,null,null,sortOrder,null);
    }
    @Override
    public Uri insert(Uri uri,ContentValues contentValues)
    {
        Log.d(TAG,"Insert");
        String table = getTableName(uri);
        if(table == null)
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        mSQLiteDatabase.insert(table,null,contentValues);
        mContext.getContentResolver().notifyChange(uri,null);
        return uri;
    }
    @Override
    public int delete(Uri uri,String selection,String[] selectionArgs)
    {
        Log.d(TAG,"delete");
        String table = getTableName(uri);
        if(table == null)
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        int count = mSQLiteDatabase.delete(table,selection,selectionArgs);
        if(count > 0)
            mContext.getContentResolver().notifyChange(uri,null);
        return count;
    }
    @Override
    public int update(Uri uri,ContentValues contentValues,String selection,String[] selectionArgs)
    {
        Log.d(TAG,"update");
        String table = getTableName(uri);
        if(table == null)
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        int row = mSQLiteDatabase.update(table,contentValues,selection,selectionArgs);
        if(row > 0)
            mContext.getContentResolver().notifyChange(uri,null);
        return row;
    }
    /**
     * getType : return a MIME responding to an Uri request
     */
    public String getType(Uri uri)
    {
        Log.d(TAG,"getType");
        return "*/*";
    }
}

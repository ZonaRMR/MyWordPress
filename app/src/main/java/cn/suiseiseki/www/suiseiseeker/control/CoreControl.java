package cn.suiseiseki.www.suiseiseeker.control;

import android.app.Application;
import android.app.DownloadManager;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.kymjs.core.bitmap.BitmapMemoryCache;

import cn.suiseiseki.www.suiseiseeker.tools.LruBitmapCache;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/16.
 * We may deliver much data in Activities in the future,It's wise to use a Application
 */
public class CoreControl extends Application{
    /**
     * For one Activity,we need just one Queue
     * Of course,we just need singleInstance of CoreControl
     * initialize when created
     */
    private static final String TAG = "CoreControl";
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static CoreControl mCoreControl;
    @Override
    public void onCreate()
    {
        mCoreControl = this;
    }
    public synchronized static CoreControl getInstance()
    {
        return mCoreControl;
    }
    /**
     * get the Queue in CoreControl,initialize when necessary
     * Use getApplicationContext() to make it a full lifetime :)
     */
    public RequestQueue getRequestQueue()
    {
        if(mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        return  mRequestQueue;
    }
    public ImageLoader getImageLoader()
    {
        // avoid a null Queue,As ImageLoader always need a RequestQueue;
        getRequestQueue();
        if(mImageLoader == null)
            mImageLoader =new ImageLoader(mRequestQueue,new LruBitmapCache());
        return  mImageLoader;
    }
    /**
     * Manage the Queue
     */
    public <T> void addToRequestQueue(Request<T> request)
    {
        // set default TAG
        request.setTag(TAG);
        mRequestQueue.add(request);
    }
    public <T> void addToRequestQueue(Request<T> request,String tag)
    {
        if(TextUtils.isEmpty(tag))
            request.setTag(TAG);
        else
            request.setTag(tag);
        mRequestQueue.add(request);
    }
    public void cancelRequestByTag(Object tag )
    {
        if(mRequestQueue != null)
            mRequestQueue.cancelAll(tag);
    }




}

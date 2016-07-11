package cn.suiseiseki.www.suiseiseeker.control;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import cn.suiseiseki.www.suiseiseeker.tools.LruBitmapCache;
import cn.suiseiseki.www.suiseiseeker.tools.MyJSONParser;
import cn.suiseiseki.www.suiseiseeker.tools.Settings;

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
    public static int state = 0;
    private static final String TAG = CoreControl.class.getName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static CoreControl mCoreControl;
    @Override
    public void onCreate()
    {
        super.onCreate();
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
        getRequestQueue().add(request);
    }
    public <T> void addToRequestQueue(Request<T> request,String tag)
    {
        if(TextUtils.isEmpty(tag))
            request.setTag(TAG);
        else
            request.setTag(tag);
        getRequestQueue().add(request);
    }
    public void cancelRequestByTag(Object tag )
    {
        if(mRequestQueue != null)
            getRequestQueue().cancelAll(tag);
    }
    /**
     * Set the Nonce
     */
    public void setNonce(String nonce)
    {
        Settings.Nonce = nonce;
    }
    synchronized public static void getNonce(int mode)
    {
        /*********************** The Json request for Nonce *****************/
        switch(mode) {
            case 1: //create_post
            {
                JsonObjectRequest request_nonce_json = new JsonObjectRequest(Request.Method.GET, Settings.NONCE_URL,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                CoreControl.getInstance().setNonce(MyJSONParser.ParseNonce(response));
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("JSON", "VolleyError in getting Nonce");
                            }
                        });
                CoreControl.getInstance().addToRequestQueue(request_nonce_json);
                break;
            }
            case 2: //delete_post
            {
                JsonObjectRequest request_nonce_json = new JsonObjectRequest(Request.Method.GET, Settings.NONCE_URL_DELETE,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Settings.Nonce_delete = MyJSONParser.ParseNonce(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("JSON", "VolleyError in getting Nonce");
                            }
                        });
                CoreControl.getInstance().addToRequestQueue(request_nonce_json);
                break;
            }
            default:

        }
        /*********************** The Json request for Nonce *****************/
    }




}

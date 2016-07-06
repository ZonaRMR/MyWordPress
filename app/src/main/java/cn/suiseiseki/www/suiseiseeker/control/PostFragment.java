package cn.suiseiseki.www.suiseiseeker.control;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import cn.suiseiseki.www.suiseiseeker.R;
import cn.suiseiseki.www.suiseiseeker.model.Post;
import cn.suiseiseki.www.suiseiseeker.model.PostProvider;
import cn.suiseiseki.www.suiseiseeker.tools.MyImageLoader.ImageResizer;
import cn.suiseiseki.www.suiseiseeker.tools.MyJSONParser;
import cn.suiseiseki.www.suiseiseeker.tools.Settings;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/23.
 */
public class PostFragment extends Fragment {

    public final static String TAG = "PostFragment";
    public final static String DELETE_FLAG = "delete_me";
    MyBroadCastReceiver delete_receiver;


    /* The View */
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private NestedScrollView mNestedScrollView;
    private AppBarLayout mAppBarLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private ImageView featuredImageView;
    private WebView mWebView;
    /* The Label */
    private int id;
    private String mTitle;
    private String mContent;
    private String mUrl;
    private String mFeaturedImageurl;
    /* The Extra / argument */
    final static String POST = "postfragment.post";
    final static String ARG_ID = "postfragment.id";
    final static String ARG_TITLE = "postfragment.title";
    final static String ARG_URL = "postfragment.url";
    final static String ARG_CONTENT = "postfragment.content";
    final static String ARG_IMAGEURL = "postfragment.imageurl";
    final static String ARG_DATE = "postfragment.date";
    final static String ARG_AUTHOR = "postfragment.authorname";


    /**
     * Define a callback
     */
    public interface onPostListener
    {
        void onHomePressed();
        void onDeletePressed();
    }
    private onPostListener mCallback;
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mCallback = (onPostListener)activity;
    }
    public void onDetach()
    {
        super.onDetach();
        mCallback = null;
    }
    public void onDestroy()
    {
        super.onDestroy();
        getActivity().unregisterReceiver(delete_receiver);
    }
    /**
     * The onCreate Method
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        CoreControl.getNonce(2);
    }
    /**
     * The onCreateView() Method,initialize the layout
     */
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_post,parent,false);
        /* Find Views */
        mToolbar = (Toolbar)v.findViewById(R.id.post_toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout)v.findViewById(R.id.post_collapsingtoorbarlayout);
        mCollapsingToolbarLayout.setTitle(getString(R.string.app_name));
        mNestedScrollView = (NestedScrollView)v.findViewById(R.id.post_nestedScrollView);
        mAppBarLayout = (AppBarLayout)v.findViewById(R.id.post_AppBarLayout);
        mCoordinatorLayout = (CoordinatorLayout)v.findViewById(R.id.post_coordinatorLayout);
        featuredImageView = (ImageView)v.findViewById(R.id.featuredImage);
        mWebView = (WebView)v.findViewById(R.id.webview);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        FontHelper.applyFont(getActivity(),v,"fonts/myfont.ttf");
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(TAG);
        delete_receiver = new MyBroadCastReceiver();
        getActivity().registerReceiver(delete_receiver,mIntentFilter);
        return v;
    }
    /**
     *  Cannot reach bundle when fragment was already existed,we have to do it in UI thread.
     */
    public void setUIArguments(final Bundle args)
    {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                // clear the content by load empty html
                mWebView.loadData("", "text/html; charset=UTF-8", null);
                featuredImageView.setImageBitmap(null);
                /** get data from args(old way) */
 //                id = args.getInt(ARG_ID);
//                mTitle = args.getString(ARG_TITLE);
//                String date = args.getString(ARG_DATE);
//                String author = args.getString(ARG_AUTHOR);
//                mContent = args.getString(ARG_CONTENT);
//                mUrl = args.getString(ARG_URL);
//                mFeaturedImageurl = args.getString(ARG_IMAGEURL);
                /** get data by Parcelable Object */
                final Post temp = args.getParcelable(POST);
                queryFromPostProvider(temp);
                id=temp.getId();
                mTitle = temp.getTitle();
                String date = temp.getDate();
                String author = temp.getAuthor();
                mContent = temp.getContent();
                mUrl = temp.getUrl();
                mFeaturedImageurl = temp.getFeaturedImageUrl();

                // download featured image
                Glide.with(PostFragment.this)
                        .load(mFeaturedImageurl)
                        .centerCrop()
                        .into(featuredImageView);
                // Build HTML content
                // CSS
                String html = "<style>img{max-width:100%;height:auto;} " +
                        "iframe{width:100%;}</style> ";
                // Article Title
                html += "<h2>" + mTitle + "</h2> ";
                // Date & author
                html += "<h4>" + date + " " + author + "</h4>";
                // The actual content
                html += mContent;

                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.setWebChromeClient(new WebChromeClient());
                // Loading html data
                mWebView.loadDataWithBaseURL("",html, "text/html", "utf-8", null);

                //Reset Action Bar and expand Toolbar
                ((MainActivity) getActivity()).setSupportActionBar(mToolbar);
                ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                expandToolbar();

                // delay some time to wait html loading
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mNestedScrollView.smoothScrollTo(0, 0);
                    }
                }, 666);
                // save post to database
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        savePost(temp);
//                    }
//                });
//                thread.start();
            }
        }; // The define of Runnable ends
        getActivity().runOnUiThread(myRunnable);
        setNotify2();

    }

    /**
     * Expand the collapsed toolbar
     */
    private void expandToolbar()
    {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)mAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior)layoutParams.getBehavior();
        // set the behavior of AppBar
        if(behavior != null)
        {
            behavior.setTopAndBottomOffset(0);
            behavior.onNestedPreScroll(mCoordinatorLayout, mAppBarLayout, null, 0, 1, new int[2]);
        }
    }
    /**
     * Set the menu of fragment
     */
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater)
    {
        inflater.inflate(R.menu.post_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId()){
            case android.R.id.home: mCallback.onHomePressed();return true;
            case R.id.share_post_item:
                return true;
            case R.id.delete_post_item:
                    mCallback.onDeletePressed();
                    return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
    /**
     * Query post data from PostProvider,May use when off-line
     */
    private void queryFromPostProvider(Post post)
    {
        Uri uri = PostProvider.POST_CONTENT_URI;
        getActivity().getContentResolver().query(uri,new String[]{"_id"},null,null,null);
    }
    /**
     * Save Post data to Database
     */
    public void savePost(Post post)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectoutput = new ObjectOutputStream(byteArrayOutputStream);
            objectoutput.writeObject(post);
            objectoutput.flush();
            byte[] data = byteArrayOutputStream.toByteArray();
            objectoutput.close();
            byteArrayOutputStream.close();
            Uri postUri = PostProvider.POST_CONTENT_URI;
            ContentValues values = new ContentValues();
            values.put("_id", post.getId());
            values.put("post", data);
            getActivity().getContentResolver().insert(postUri, values);
            Log.d(TAG,"post s" +
                    "aved to database");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    /**
     * set a notification
     */
    public void setNotify2()
    {
        Bitmap bitmap = new ImageResizer().decodeSampledBitmapFromResource(getResources(),R.drawable.cute_purple,200,200);
        Notification notification = new Notification.Builder(getActivity()).setAutoCancel(false).setContentTitle(getString(R.string.app_name))
                .setContentText(mTitle).setSmallIcon(R.mipmap.su).setLargeIcon(bitmap).setWhen(System.currentTimeMillis()).build();
        notification.contentIntent =  PendingIntent.getActivity(getActivity(), 0, new Intent(getActivity(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(2,notification);
    }

    /**
     * Delete the Post
     */
    private void deleteThisPost()
    {
        StringBuilder mStringBuilder =new StringBuilder();
        mStringBuilder.append(Settings.DELETE_POST_URL);
        mStringBuilder.append("?nonce=").append(Settings.Nonce_delete).append("&post_id=").append(id);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, mStringBuilder.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(),getString(R.string.success),Toast.LENGTH_SHORT);
                mCallback.onHomePressed();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"Volley Error while deleting post");
            }
        });
        CoreControl.getInstance().addToRequestQueue(mStringRequest);
    }

    class MyBroadCastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context,Intent intent)
        {
           int i = intent.getIntExtra(DELETE_FLAG,0);
            if(i == 1)
                deleteThisPost();
        }
    }
}

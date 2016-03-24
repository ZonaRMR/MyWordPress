package cn.suiseiseki.www.suiseiseeker.control;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import cn.suiseiseki.www.suiseiseeker.R;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/23.
 */
public class PostFragment extends Fragment {

    private final static String TAG = "PostFragment";

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

    /**
     * Define a callback
     */
    public interface onPostListener
    {
        void onHomePressed();
    }
    private onPostListener mCallback;
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mCallback = (onPostListener)activity;
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
        return v;
    }
    /**
     *
     */
    public void setUIArguments(final Bundle args)
    {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                // clear the content
                mWebView.loadData("", "text/html; charset=UTF-8", null);
            }
        };
    }


}

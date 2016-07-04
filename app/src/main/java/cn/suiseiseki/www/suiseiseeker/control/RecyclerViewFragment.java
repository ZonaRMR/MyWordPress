package cn.suiseiseki.www.suiseiseeker.control;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

import cn.suiseiseki.www.suiseiseeker.R;
import cn.suiseiseki.www.suiseiseeker.model.CategoryAdapter;
import cn.suiseiseki.www.suiseiseeker.model.MyRecyclerViewAdapter;
import cn.suiseiseki.www.suiseiseeker.model.Post;
import cn.suiseiseki.www.suiseiseeker.tools.MyJSONBuilder;
import cn.suiseiseki.www.suiseiseeker.tools.MyJSONParser;
import cn.suiseiseki.www.suiseiseeker.tools.Settings;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/17.
 * As this Fragment its self need to be refreshed,must implements a listener
 */
public class RecyclerViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "RecyclerViewFragment";
    public static final String CATEGORY_ID = "cn.suiseiseki.www.suiseiseeker.category_id";
    public static final String QUERY = "query";

    private String mSearchQuery = ""; // Query string used for search result

    /* The Model and the State*/
    private int mCategoryID;
    private int mPage = 1; //Current Page number in the Recycler view
    private  ArrayList<Post> mPostArrayList = new ArrayList<>();
    private boolean isLoading = false;
    private boolean isSearch = false;

    /* The View */
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private TextView mLoadingTextView;
    private LinearLayoutManager mLayoutManager;

    /* The Adapter */
    private MyRecyclerViewAdapter mMyRecyclerViewAdapter;

    /* Callback */
    private PostListListener mCallback;

    /* Counter of the List item */
    private int mPastVisibleItems;
    private int mVisibleItemCount;
    private int mPreviousPostNum = 0;
    private int mPostNum; // The Number of Post Received

    public interface PostListListener {
        void onPostSelected(Post post, boolean isSearch);
    }
    /**
     * Attach Callback Activity
     */
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mCallback = (PostListListener)activity;
    }
    @Override
    public void onDetach()
    {
        mCallback = null;
        super.onDetach();
    }
    /**
     *  Use Argument to transfer category_id to Fragment / or query string
     * @param category_id:the id of category
     * @return a instance of RecyclerViewFragment
     */
    public static RecyclerViewFragment newInstance(int category_id)
    {
        Bundle arguments = new Bundle();
        arguments.putInt(CATEGORY_ID,category_id);
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        fragment.setArguments(arguments);
        return fragment;
    }
    public static RecyclerViewFragment newInstance(String query)
    {
        Bundle arguments = new Bundle();
        arguments.putString(QUERY, query);
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        fragment.setArguments(arguments);
        return  fragment;
    }
    /**
     * The onCreate() of this Fragment
     * initialize the Category
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
        {
            mCategoryID = getArguments().getInt(CATEGORY_ID,-1);
            mSearchQuery = getArguments().getString(QUERY,"");
        }
    }
    /**
     * The onCreateView() of this Fragment,may draw the basic layout
     */
    @Override
    public void onRefresh() {
        mPostArrayList.clear();
        mMyRecyclerViewAdapter.notifyDataSetChanged();
        loadFirstPage();
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_recycler_view_layout,null);
        mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipeRefreshLayout_recycler);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.recylerView_recycler);
        mLoadingTextView = (TextView) v.findViewById(R.id.textview_recyler_dialog);
        mLayoutManager = new LinearLayoutManager(getActivity());
        //pull to refresh listener
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //Initialize the adapter
        mMyRecyclerViewAdapter = new MyRecyclerViewAdapter(mPostArrayList, new MyRecyclerViewAdapter.onItemClickCallback() {
            @Override
            public void onItemClick(Post post) {
                mCallback.onPostSelected(post,isSearch);
            }
        });
        /**
         * Every row in the list has the same size
         * setLayoutManager and Adapter
         */
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mMyRecyclerViewAdapter);
        // set a listener to scroll
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // The Count of items on Screen
                mVisibleItemCount = mLayoutManager.getChildCount();
                // The Count of items past
                mPastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                int totalCount = mLayoutManager.getItemCount();
                if (mPostNum > mPreviousPostNum)
                    if (!mPostArrayList.isEmpty())
                        if (mVisibleItemCount != 0)
                            if (totalCount > mVisibleItemCount)
                                if (!isLoading)
                                    if ((mVisibleItemCount + mPastVisibleItems) >= totalCount)
                                    {
                                        loadNextPage();
                                        mPreviousPostNum = mPostNum;}
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        return  v;
    }
    /**
     * When the Activity is Created,Loading posts
     */
    @Override
    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);
        loadFirstPage();
    }
    /**
     * Methods to control the loadingView
     */
    private void showLoadingView()
    {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingTextView.setVisibility(View.VISIBLE);
    }
    private void hideLoadingView()
    {
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoadingTextView.setVisibility(View.INVISIBLE);
    }
    /**
     * Some method to control the page of Categories
     */
    public void loadFirstPage()
    {
        mPage = 1;
        if(mPostArrayList.isEmpty())
        {
            showLoadingView();
            mPreviousPostNum = 0;
            loadPost(mPage,false);
        }
        else
            hideLoadingView();
    }
    public void loadNextPage()
    {
        mPage++;
        loadPost(mPage,true);
    }
    /**
     * Loading Posts that belong to the page
     * @param page :the page that loading
     * @param showLoadingMessage :whether make toast message to the user
     */
    protected void loadPost(int page,boolean showLoadingMessage)
    {
        Log.d(TAG,"-----------Start Loading Posts-----------------");
        isLoading = true;
        if(showLoadingMessage)
        {
            Toast.makeText(getActivity(),getString(R.string.seek_posts),Toast.LENGTH_LONG).show();
        }
        /**
         * Build the correct Url API
         */
        //It's a search request
         String url;
         if(!mSearchQuery.equals(""))
         {
             isSearch = true;
             url = Settings.MAIN_URL + "?json=get_search_results&search=" + mSearchQuery +
                     "&page=" + String.valueOf(page);
         }
        else{
             isSearch = false;
             if(mCategoryID == 0) // The recent Posts
             {
                 url = Settings.MAIN_URL + "?json=get_recent_posts";
             }
             else
             {
                 url = Settings.MAIN_URL + "?json=get_category_posts&category_id="+String.valueOf(mCategoryID)+ "&page=" + String.valueOf(page);
             }

         }
        Log.d(TAG, "url:"+url);
        /**
         * The Response Listener and Error Listener
         * The JsonObject Request for Posts
         */
        Response.Listener responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mSwipeRefreshLayout.setRefreshing(false);
                mPostArrayList.addAll(MyJSONParser.ParsePosts(response));
                // avoid same Posts
                HashSet<Post> postSet = new LinkedHashSet<>(mPostArrayList);
                mPostArrayList.clear();
                mPostArrayList.addAll(new ArrayList<>(postSet));
                mPostNum = mPostArrayList.size();
                Log.d(TAG, "Receive Posts count:" + mPostNum);
                mMyRecyclerViewAdapter.notifyDataSetChanged();
                // Move the article list up by one row
                if(mPage !=1)
                    mLayoutManager.scrollToPosition(mPastVisibleItems + mVisibleItemCount);
                isLoading = false;
                hideLoadingView();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoading = false;
                hideLoadingView();
                mSwipeRefreshLayout.setRefreshing(false);
                error.printStackTrace();
                Log.d(TAG,"Volley Error" + error.getMessage());
                //Use SnackBar instead of ActionBar
                Snackbar.make(mRecyclerView,getString(R.string.failed_load_posts),Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.retry), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadPost(mPage,true);
                            }
                        }).show();
            }
        };
        JsonObjectRequest request = new JsonObjectRequest(url,null,responseListener,errorListener);
        // 10s retry
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CoreControl.getInstance().addToRequestQueue(request,TAG);
    }




}

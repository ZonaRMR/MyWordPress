package cn.suiseiseki.www.suiseiseeker.control;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.suiseiseki.www.suiseiseeker.R;
import cn.suiseiseki.www.suiseiseeker.model.CategoryAdapter;
import cn.suiseiseki.www.suiseiseeker.model.MyRecyclerViewAdapter;
import cn.suiseiseki.www.suiseiseeker.model.Post;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/17.
 * As this Fragment its self need to be refreshed,must implements a listener
 */
public class RecyclerViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "RecyclerViewFragment";
    public static final String CATEGORY_ID = "cn.suiseiseki.www.suiseiseeker.category_id";
    /* The Model and the State*/
    private int mCategoryID;
    private ArrayList<Post> mPostArrayList = new ArrayList<>();
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
    private int mPostNum;

    public interface PostListListener {
        void onPostSelected(Post post, boolean isSearch);
    }
    /**
     *  Use Argument to transfer category_id to Fragment
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
            mCategoryID = getArguments().getInt(CATEGORY_ID);
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
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                // The Count of items on Screen
                  mVisibleItemCount = mLayoutManager.getChildCount();
                // The Count of items past
                 mPastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                int totalCount = mLayoutManager.getItemCount();
                if(mPostNum > mPreviousPostNum)
                    if(!mPostArrayList.isEmpty())
                        if(mVisibleItemCount > 0)
                            if(totalCount > mVisibleItemCount)
                                if(!isLoading)
                                    if((mVisibleItemCount + mPastVisibleItems)>=totalCount)
                                        loadNextPage();
                mPreviousPostNum = mPostNum;
            }
        });
        return  v;
    }


}

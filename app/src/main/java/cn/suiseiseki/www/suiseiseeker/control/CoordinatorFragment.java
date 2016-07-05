package cn.suiseiseki.www.suiseiseeker.control;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SearchView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;

import cn.suiseiseki.www.suiseiseeker.R;
import cn.suiseiseki.www.suiseiseeker.control.postedit.PostEditActivity;
import cn.suiseiseki.www.suiseiseeker.model.Category;
import cn.suiseiseki.www.suiseiseeker.model.CategoryAdapter;
import cn.suiseiseki.www.suiseiseeker.tools.MyJSONParser;
import cn.suiseiseki.www.suiseiseeker.tools.Settings;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/16.
 */
public class CoordinatorFragment extends Fragment implements SearchView.OnQueryTextListener{
    private final static String TAG = "CoordinatorFragment";
    /* The View */
    private ProgressDialog mProgressDialog;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SearchView mSearchView;
    private MenuItem mSearchMenuItem;


    /* The Model */
    public static ArrayList<Category> mCategories = null;
    /* CallBack to Activity,If need a Search */
    Callback mCallback;
    public interface Callback
    {
        void onSearchSubmit(String searchText);
    }
    /**
     * Attach to Callback Activity
     */
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mCallback = (Callback)activity;
    }
    @Override
    public void onDetach()
    {
        mCallback = null;
        super.onDetach();
    }
    /**
     * The Work of onCreate() and onDestroy(),do some preparation work
     * Retain Instance and has optionsMenu
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    /**
     * Inflate the layout of this Fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_coordinator_layout,null,false);
        mToolbar = (Toolbar)v.findViewById(R.id.toolbar_coordinator);
        Log.d(TAG, "Loading ActionBar");
       ((MainActivity)getActivity()).setSupportActionBar(mToolbar);
        Log.d(TAG,"Loading TabLayout....");
        mTabLayout = (TabLayout)v.findViewById(R.id.tablayout_coordinator);
        mViewPager = (ViewPager)v.findViewById(R.id.viewPager_coordinator);
        // Pre-loading one fragment
        mViewPager.setOffscreenPageLimit(1);
        FontHelper.applyFont(getActivity(),v,"fonts/myfont.ttf");
        return v;
    }
    /**
     * The Menu and SearchView
     */
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater)
    {
        super.onCreateOptionsMenu(menu,menuInflater);
        menuInflater.inflate(R.menu.search_menu,menu);
        //Create a searchView
        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        mSearchMenuItem = menu.findItem(R.id.search_menu_item_search_action);
        // The Action View was already defined in the xml
        mSearchView = (SearchView)mSearchMenuItem.getActionView();
        Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.searchview);
        mSearchView.startAnimation(animation);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setQueryHint(getString(R.string.search_hint));
        /* The fragment should response to the searchview */
        mSearchView.setOnQueryTextListener(this);
    }
    @Override
    public boolean onQueryTextSubmit(String query)
    {
        mSearchView.clearFocus();
        // Let Activity to handle this
        mCallback.onSearchSubmit(query);
        return false;
    }
    @Override
    public boolean onQueryTextChange(String queryText){
        return  false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.search_menu_item_search_action:
            {
                mSearchView.requestFocus();
                break;
            }
            case R.id.search_menu_item_add_action:
            {
                Intent intent = new Intent(getActivity(),PostEditActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in_slow,R.anim.fade_out_slow);
                break;
            }
        }
        return true;
    }
    // reset actionbar when fragment changes

    public void resetActionBar() {
        ((MainActivity)getActivity()).setSupportActionBar(mToolbar);
        mSearchMenuItem.collapseActionView();
    }
    /**
     * When the activity onCreate() is applied
     * Load Categories
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        loadCategories();
    }

    private void loadCategories()
    {
        // Use a ProgressDialog
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.seek_categories));
        // Please hold your patience
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        // Parse JSON for Categories
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mProgressDialog.dismiss();
                mCategories = MyJSONParser.ParseCategories(response);
                CategoryAdapter adaptor = new
                        CategoryAdapter(getChildFragmentManager(),mCategories);
                mViewPager.setAdapter(adaptor);
                mTabLayout.setupWithViewPager(mViewPager);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"Error: Requesting JSON for Categories" );
                mProgressDialog.dismiss();
                //may use Snackbar instead of Toast
                Snackbar.make(mTabLayout, R.string.error_load_categories,
                        Snackbar.LENGTH_INDEFINITE).setAction(R.string.retry,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadCategories();
                            }
                        }).show();

            }
        };
        JsonObjectRequest jsonCategoryRequest = new JsonObjectRequest(Settings.CATEGORY_INDEX_URL,listener,errorListener);
        //Send Request to CoreControl to Handle
        CoreControl.getInstance().addToRequestQueue(jsonCategoryRequest);
    }




}

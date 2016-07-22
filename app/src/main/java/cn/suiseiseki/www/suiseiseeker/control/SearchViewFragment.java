package cn.suiseiseki.www.suiseiseeker.control;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import cn.suiseiseki.www.suiseiseeker.R;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/20.
 */
public class SearchViewFragment  extends Fragment {
    private final static String TAG = SearchViewFragment.class.getSimpleName();

    private String mQuery;
    private static final String QUERY = "cn.suiseiseki.www.query";

    /**
     * A callback to Actvity
     */
    public interface onHomePressed
    {
        void onHomePressed();
    }
    private onHomePressed mCallback;
    @Override
    public void onAttach(Activity activity)
    {
        mCallback = (onHomePressed) activity;
        super.onAttach(activity);
    }

    /**
     * Deliver Messages by Argument
     */
    public static SearchViewFragment newInstance(String query)
    {
        SearchViewFragment fragment = new SearchViewFragment();
        Bundle argument = new Bundle();
        argument.putString(QUERY, query);
        fragment.setArguments(argument);
        return fragment;
    }
    public SearchViewFragment(){};
    /**
     * Receive argument
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(getArguments() != null)
            mQuery = getArguments().getString(QUERY,"");
    }
    /**
     * Loading the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_search_result_layout,parent,false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.search_result_toorbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar()
                .setTitle(getString(R.string.search_result) + " \"" + mQuery + "\"");
        // add a recyclerview to show result
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        RecyclerViewFragment recyclerViewFragment = RecyclerViewFragment.newInstance(mQuery);
        transaction.add(R.id.search_result_container, recyclerViewFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        return v;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            mCallback.onHomePressed();
        }
        return true;
    }


}

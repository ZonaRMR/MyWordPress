package cn.suiseiseki.www.suiseiseeker.control;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import cn.suiseiseki.www.suiseiseeker.R;
import cn.suiseiseki.www.suiseiseeker.model.Post;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/15.
 * Must extends AppCompatActivity
 */
public class MainActivity extends AppCompatActivity implements RecyclerViewFragment.PostListListener,CoordinatorFragment.Callback,SearchViewFragment.onHomePressed,PostFragment.onPostListener{

    private FragmentManager mFragmentManager;

    private final static String TAG = MainActivity.class.getSimpleName();
    private final static String COR_LAYOUT_TAG = "CoordinatorFragment";

    /* The Fragments that control */
    private CoordinatorFragment mCoordinatorFragment;
    private SearchViewFragment mSearchViewFragment;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mFragmentManager = getSupportFragmentManager();
        mCoordinatorFragment = new CoordinatorFragment();
        mFragmentManager.beginTransaction().add(R.id.container,mCoordinatorFragment).commit();
    }
    /**
     * When a post in the List is selected
     */
    @Override
    public void onPostSelected(Post post, boolean isSearch) {};
    /**
     * Callback from CoordinatorFragment
     * Handle Search Event
     */
    @Override
    public void onSearchSubmit(String searchText)
    {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        /* Set Animation */
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        mSearchViewFragment = SearchViewFragment.newInstance(searchText);
        fragmentTransaction.add(android.R.id.content,mSearchViewFragment);
        fragmentTransaction.hide(mCoordinatorFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    /**
     * Simulate a back button press when home is selected
     */
    @Override
    public void onHomePressed() {
        resetActionBarIfApplicable();
        mFragmentManager.popBackStack();
    }
    /**
     * Reset CoordinatorLayoutFragment's ActionBar if necessary
     */
    private void resetActionBarIfApplicable() {
        if (mSearchViewFragment.isVisible()) {
           mCoordinatorFragment.resetActionBar();
        }
    }
}

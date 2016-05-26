package cn.suiseiseki.www.suiseiseeker.control;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.json.JSONObject;

import java.io.Serializable;

import cn.suiseiseki.www.suiseiseeker.R;
import cn.suiseiseki.www.suiseiseeker.model.Post;


/**
 * Created by Suiseiseki/shuikeyi on 2016/3/15.
 * Must extends AppCompatActivity
 */
public class MainActivity extends AppCompatActivity implements RecyclerViewFragment.PostListListener,CoordinatorFragment.Callback,SearchViewFragment.onHomePressed,PostFragment.onPostListener{

    private static final long serialVersionUID = 511124199211281219L;

    private FragmentManager mFragmentManager;

    private final static String TAG = MainActivity.class.getSimpleName();
    private final static String COR_TAG = "CoordinatorFragment";
    private final static String POST_TAG = "PostFragment";
    String d;

    /* The Fragments that control */
    private CoordinatorFragment mCoordinatorFragment;
    private SearchViewFragment mSearchViewFragment = new SearchViewFragment();
    private PostFragment mPostFragment;
    JSONObject temp;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mFragmentManager = getSupportFragmentManager();
        mCoordinatorFragment = new CoordinatorFragment();
        mPostFragment = new PostFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        // add Fragment by TAG, instead of by ID;
        fragmentTransaction.add(android.R.id.content, mCoordinatorFragment, COR_TAG);
        fragmentTransaction.add(android.R.id.content, mPostFragment, POST_TAG);
        fragmentTransaction.hide(mPostFragment);
        fragmentTransaction.commit();
    }

    /**
     * When a post in the List is selected
     */
    @Override
    public void onPostSelected(Post post, boolean isSearch)
    {
        // find the post Fragment
        mPostFragment = (PostFragment)getSupportFragmentManager().findFragmentByTag(POST_TAG);
        // build an arguments
        Bundle args = new Bundle();
        args.putParcelable(PostFragment.POST,post);
        /** The Old way */
//        args.putInt(PostFragment.ARG_ID, post.getId());
//        args.putString(PostFragment.ARG_AUTHOR, post.getAuthor());
//        args.putString(PostFragment.ARG_IMAGEURL,post.getFeaturedImageUrl());
//        args.putString(PostFragment.ARG_URL, post.getUrl());
//        args.putString(PostFragment.ARG_CONTENT, post.getContent());
//        args.putString(PostFragment.ARG_DATE, post.getDate());
//        args.putString(PostFragment.ARG_TITLE, post.getTitle());

        // Set the "UI" arguments
        mPostFragment.setUIArguments(args);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
//        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.enter_post);
//        Animation animation2 = AnimationUtils.loadAnimation(this,R.anim.enter_post2);
        fragmentTransaction.setCustomAnimations(R.anim.enter_post,R.anim.enter_post2,R.anim.enter_post,R.anim.enter_post2);
        if(isSearch)
        {
            fragmentTransaction.hide(mSearchViewFragment);
        }
        else
        {
            fragmentTransaction.hide(mCoordinatorFragment);
        }
        fragmentTransaction.show(mPostFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    };
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
    @Override
    public void onBackPressed() {
        resetActionBarIfApplicable();
        super.onBackPressed();
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

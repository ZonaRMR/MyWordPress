package cn.suiseiseki.www.suiseiseeker.control;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import cn.suiseiseki.www.suiseiseeker.R;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/25.
 */
public class PostEditActivity extends AppCompatActivity {

    /**
     * The onCreate() Method
     */
    private Fragment createFragment()
    {
        return  new PostEditFragment();
    }
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_edit_activity);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = createFragment();
        fm.beginTransaction().add(R.id.post_edit_fragment_container,fragment).commit();
    }
}

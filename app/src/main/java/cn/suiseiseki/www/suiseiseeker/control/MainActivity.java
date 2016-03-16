package cn.suiseiseki.www.suiseiseeker.control;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import cn.suiseiseki.www.suiseiseeker.R;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/15.
 * Must extends AppCompatActivity
 */
public class MainActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;

    private Fragment mCoordinatorFragment;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mFragmentManager = getSupportFragmentManager();
        mCoordinatorFragment = new CoordinatorFragment();
        mFragmentManager.beginTransaction().add(R.id.container,mCoordinatorFragment).commit();
    }

}

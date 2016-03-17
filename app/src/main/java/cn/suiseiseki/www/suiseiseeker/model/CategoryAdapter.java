package cn.suiseiseki.www.suiseiseeker.model;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/17.
 * Adapter for PagerView
 * Use FragmentPagerAdapter instead of FragmentStatePagerAdapter to secure safety,as there are not so many categories
 */
public class CategoryAdapter extends FragmentPagerAdapter {
    private ArrayList<Category> mCategories;
    public CategoryAdapter(FragmentManager fm,ArrayList<Category> categories)
    {
        super(fm);
        mCategories = categories;
    }
    public int getCount()
    {
        return mCategories.size();
    }
    public Fragment getItem(int pos)
    {
        return null;
    }
    /**
     * Set the Title of Page
     */
    @Override
    public CharSequence getPageTitle(int position)
    {
        return mCategories.get(position).getName();
    }


}

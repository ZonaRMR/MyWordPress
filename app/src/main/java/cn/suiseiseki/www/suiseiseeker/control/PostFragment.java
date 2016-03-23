package cn.suiseiseki.www.suiseiseeker.control;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.suiseiseki.www.suiseiseeker.R;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/23.
 */
public class PostFragment extends Fragment {

    private final static String TAG = "PostFragment";

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

    }



}

package cn.suiseiseki.www.suiseiseeker.control.postedit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.util.zip.Inflater;

import cn.suiseiseki.www.suiseiseeker.R;
import cn.suiseiseki.www.suiseiseeker.control.PostFragment;

/**
 * Created by Shuikeyi on 2016/7/5.
 * address:shuikeyi92@gmail.com
 */
public class DeleteDiagFragment extends AllClearDiagFragment {
    TextView mTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle savedInstanceState)
    {
        View v = super.onCreateView(inflater,root,savedInstanceState);
        mTextView= (TextView)v.findViewById(R.id.all_clear_fragment_textview);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        mTextView.setText(getString(R.string.delete_post_confirm));
        return v;
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.clearall_yes_button:
                Intent mIntent = new Intent();
                mIntent.setAction(PostFragment.TAG);
                mIntent.putExtra(PostFragment.DELETE_FLAG,1);
                getActivity().sendBroadcast(mIntent);
                Back();
                break;
            case R.id.clearall_no_button:
                Back();
                break;
            default:
                Back();
        }
    }
}

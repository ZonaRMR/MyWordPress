package cn.suiseiseki.www.suiseiseeker.control.postedit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.suiseiseki.www.suiseiseeker.R;
import cn.suiseiseki.www.suiseiseeker.control.FontHelper;
import cn.suiseiseki.www.suiseiseeker.control.PostFragment;

/**
 * Created by Shuikeyi on 2016/7/4.
 * address:shuikeyi92@gmail.com
 */
public class AllClearDiagFragment extends DialogFragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.all_clear_fragment_layout,root,false);
        Button yes = (Button)v.findViewById(R.id.clearall_yes_button);
        Button no = (Button)v.findViewById(R.id.clearall_no_button);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        FontHelper.applyFont(getActivity(),v,"fonts/myfont.ttf");
        return v;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.clearall_yes_button:
                Intent mIntent = new Intent();
                mIntent.setAction(PostEditFragment.TAG);
                mIntent.putExtra(PostEditFragment.ALL_CLEAR,1);
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

    protected void Back()
    {
        FragmentManager FM = getActivity().getSupportFragmentManager();
        FM.beginTransaction().remove(this).commit();
    }
}

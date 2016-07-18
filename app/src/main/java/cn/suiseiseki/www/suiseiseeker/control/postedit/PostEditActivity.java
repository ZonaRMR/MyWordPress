package cn.suiseiseki.www.suiseiseeker.control.postedit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import cn.suiseiseki.www.suiseiseeker.R;
import cn.suiseiseki.www.suiseiseeker.tools.FontHelper;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/25.
 */
public class PostEditActivity extends AppCompatActivity implements PostEditFragment.Callback{

    /**
     * The onCreate() Method
     */
    FragmentManager fm;
    private Fragment createFragment()
    {
        return  new PostEditFragment();
    }
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_edit_activity);
        fm = getSupportFragmentManager();
        Fragment fragment = createFragment();
        FragmentTransaction mBeginTransaction = fm.beginTransaction();
        mBeginTransaction.setCustomAnimations(R.anim.enter_post,R.anim.enter_post2,R.anim.enter_post,R.anim.enter_post2);
        mBeginTransaction.add(R.id.post_edit_fragment_container,fragment).commit();
        FontHelper.applyFont(this,findViewById(R.id.post_edit_fragment_container),"fonts/myfont.ttf");
    }

    @Override
    public void onAllClearClicked() {
        AllClearDiagFragment allclearFragment = new AllClearDiagFragment();
        allclearFragment.show(fm,getString(R.string.all_clear));

    }
}

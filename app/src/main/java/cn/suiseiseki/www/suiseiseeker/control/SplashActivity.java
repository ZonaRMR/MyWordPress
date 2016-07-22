package cn.suiseiseki.www.suiseiseeker.control;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import cn.suiseiseki.www.suiseiseeker.R;

/**
 * Created by Shuikeyi on 2016/7/11.
 * address:shuikeyi92@gmail.com
 */
public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private final int TIME_LENGTH = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this,R.layout.splash_layout,null);
        setContentView(view);
        if(CoreControl.state == 0)
        {
            AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
            aa.setDuration(TIME_LENGTH);
            view.startAnimation(aa);
            aa.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation arg0) {
                    CoreControl.state = 1;
                    directTo();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }

            });
        }
        else
        {
            directTo();
        }
    }

    private void directTo()
    {
        Intent mIntent = new Intent(SplashActivity.this, MainActivity.class);
        overridePendingTransition(R.anim.fade_in_slow, R.anim.fade_out_slow);
        startActivity(mIntent);
        finish();
    }

}

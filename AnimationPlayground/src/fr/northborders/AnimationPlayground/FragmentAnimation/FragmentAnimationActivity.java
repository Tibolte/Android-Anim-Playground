package fr.northborders.AnimationPlayground.FragmentAnimation;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import fr.northborders.AnimationPlayground.FragmentAnimation.Fragments.BaseFragment;
import fr.northborders.AnimationPlayground.FragmentAnimation.Fragments.Fragment1;
import fr.northborders.AnimationPlayground.FragmentAnimation.Fragments.Fragment2;
import fr.northborders.AnimationPlayground.FragmentAnimation.Listeners.Fragment1TouchListener;
import fr.northborders.AnimationPlayground.FragmentAnimation.Listeners.Fragment2TouchListener;
import fr.northborders.AnimationPlayground.R;

/**
 * Created by thibaultguegan on 12/07/2014.
 */
public class FragmentAnimationActivity extends FragmentActivity implements Fragment1TouchListener, Fragment2TouchListener{
    private static final String TAG = FragmentAnimationActivity.class.getName();

    private final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
    private final int ANIMATION_DURATION = 250;

    private FrameLayout mMainContainer;

    private BaseFragment baseFragment;
    private Fragment1 fragment1;
    private Fragment2 fragment2;

    private boolean fragment1Visible;
    private boolean fragment2Visible;

    private final Rect mTmpRect = new Rect();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_animation_activity);

        mMainContainer = (FrameLayout) findViewById(R.id.main_container);

        FragmentManager fragmentManager = getSupportFragmentManager();
        baseFragment = new BaseFragment();
        baseFragment.setFragment1TouchListener(this);
        baseFragment.setFragment2TouchListener(this);
        fragmentManager.beginTransaction().replace(R.id.base_fragment, baseFragment).commit();

        fragment1 = new Fragment1();
        fragmentManager.beginTransaction().replace(R.id.fragment1, fragment1).commit();
        fragment2 = new Fragment2();
        fragmentManager.beginTransaction().replace(R.id.fragment2, fragment2).commit();

    }

    @Override
    public void onFragment1Touch(View v, View movableView, boolean animated) {
        FrameLayout frame1 = (FrameLayout) findViewById(R.id.fragment1);
        TextView txtFragment1 = (TextView) findViewById(R.id.txtSelectFragment1);

        if(!fragment1Visible)
        {
            fragment1Visible = true;

            txtFragment1.setText("Dismiss fragment 1");

            v.getDrawingRect(mTmpRect);

            ObjectAnimator anim = ObjectAnimator.ofFloat(v, "translationY", - ( movableView.getTop()));
            anim.setInterpolator(ANIMATION_INTERPOLATOR);
            anim.setDuration(ANIMATION_DURATION);
            anim.start();

            slideInToTop(v, movableView, frame1);
        }
        else
        {
            fragment1Visible = false;

            txtFragment1.setText("Show fragment 1");

            v.getDrawingRect(mTmpRect);

            Log.d(TAG, "Movable view top: " + String.valueOf(movableView.getTop()));
            Log.d(TAG, "Container view top: " + String.valueOf(v.getTop()));

            ObjectAnimator anim = ObjectAnimator.ofFloat(v, "translationY", 0);
            anim.setInterpolator(ANIMATION_INTERPOLATOR);
            anim.setDuration(ANIMATION_DURATION);
            anim.start();

            slideInToBottom(v, frame1);
        }
    }

    @Override
    public void onFragment2Touch(View v, View movableView, boolean animated) {
        FrameLayout frame2 = (FrameLayout) findViewById(R.id.fragment2);
        TextView txtFragment2 = (TextView) findViewById(R.id.txtSelectFragment2);

        if(!fragment2Visible)
        {
            fragment2Visible = true;

            txtFragment2.setText("Dismiss fragment 2");

            v.getDrawingRect(mTmpRect);

            ObjectAnimator anim = ObjectAnimator.ofFloat(v, "translationY", - ( movableView.getTop()));
            anim.setInterpolator(ANIMATION_INTERPOLATOR);
            anim.setDuration(ANIMATION_DURATION);
            anim.start();

            slideInToTop(v, movableView, frame2);
        }
        else
        {
            fragment2Visible = false;

            txtFragment2.setText("Show fragment 2");

            v.getDrawingRect(mTmpRect);

            Log.d(TAG, "Movable view top: " + String.valueOf(movableView.getTop()));
            Log.d(TAG, "Container view top: " + String.valueOf(v.getTop()));

            ObjectAnimator anim = ObjectAnimator.ofFloat(v, "translationY", 0);
            anim.setInterpolator(ANIMATION_INTERPOLATOR);
            anim.setDuration(ANIMATION_DURATION);
            anim.start();

            slideInToBottom(v, frame2);
        }
    }

    private void slideInToTop(View editContainer, View movableView, View v)
    {
        Log.d(TAG, "container view height :" + String.valueOf(v.getHeight()));

        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "translationY", mMainContainer.getHeight());
        anim.setDuration(0);
        anim.start();

        v.setVisibility(View.VISIBLE);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(v, "translationY", editContainer.getTop()+movableView.getHeight()),
                ObjectAnimator.ofFloat(v, "alpha", 0, 1)
        );
        set.setDuration(ANIMATION_DURATION);
        set.setInterpolator(ANIMATION_INTERPOLATOR);
        set.start();
    }

    private void slideInToBottom(View editContainer, View v)
    {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(v, "translationY", mMainContainer.getHeight()),
                ObjectAnimator.ofFloat(v, "alpha", 1, 0)
        );
        set.setDuration(ANIMATION_DURATION);
        set.setInterpolator(ANIMATION_INTERPOLATOR);
        set.start();

    }
}
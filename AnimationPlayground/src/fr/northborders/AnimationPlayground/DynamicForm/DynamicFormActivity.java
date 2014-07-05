package fr.northborders.AnimationPlayground.DynamicForm;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import fr.northborders.AnimationPlayground.DynamicForm.Fragments.EditContentFragment;
import fr.northborders.AnimationPlayground.DynamicForm.Fragments.EditFieldFragment;
import fr.northborders.AnimationPlayground.DynamicForm.Listeners.EditTextFocusListener;
import fr.northborders.AnimationPlayground.R;

/**
 * Created by thibaultguegan on 30/05/2014.
 */
public class DynamicFormActivity extends FragmentActivity implements EditTextFocusListener{
    private static final String TAG = DynamicFormActivity.class.getName();

    private final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
    private final int ANIMATION_DURATION = 250;
    private int modeContainerMargin;

    private FrameLayout mMainContainer;

    private EditContentFragment editContentFragment;
    private EditFieldFragment editFieldFragment;

    private final Rect mTmpRect = new Rect();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dynamic_form);

        mMainContainer = (FrameLayout) findViewById(R.id.main_container);

        FragmentManager fragmentManager = getSupportFragmentManager();

        //replace form container
        editFieldFragment = new EditFieldFragment();
        editFieldFragment.setListener(this);
        fragmentManager.beginTransaction().replace(R.id.form_container, editFieldFragment).commit();

        //replace edit mode container
        editContentFragment = new EditContentFragment();
        fragmentManager.beginTransaction().replace(R.id.edit_mode_container, editContentFragment).commit();

        //margin for edit mode container
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        modeContainerMargin = getResources().getDimensionPixelSize(R.dimen.margin_mode_container);
    }

    @Override
    public void focusOn(View v, View movableView, boolean animated) {
        v.getDrawingRect(mTmpRect);
        //mMainContainer.offsetDescendantRectToMyCoords(v, mTmpRect);

        //Log.d(TAG, "Movable view top: " + String.valueOf(movableView.getTop()));
        //Log.d(TAG, "Container view top: " + String.valueOf(v.getTop()));


        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "translationY", - ( movableView.getTop()));
        anim.setInterpolator(ANIMATION_INTERPOLATOR);
        anim.setDuration(ANIMATION_DURATION);
        anim.start();

        slideInToTop(v, movableView);

    }

    @Override
    public void focusOff(View v, View movableView, boolean animated) {
        v.getDrawingRect(mTmpRect);

        Log.d(TAG, "Movable view top: " + String.valueOf(movableView.getTop()));
        Log.d(TAG, "Container view top: " + String.valueOf(v.getTop()));

        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "translationY", 0);
        anim.setInterpolator(ANIMATION_INTERPOLATOR);
        anim.setDuration(ANIMATION_DURATION);
        anim.start();

        slideInToBottom(v);
    }

    private void slideInToTop(View editContainer, View movableView)
    {
        View v = findViewById(R.id.edit_mode_container);

        //put edit at the bottom
        Log.d(TAG, "container view height :" + String.valueOf(v.getHeight()));

        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "translationY", mMainContainer.getHeight());
        anim.setDuration(0);
        anim.start();

        v.setVisibility(View.VISIBLE);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(v, "translationY", editContainer.getTop()+movableView.getHeight()+modeContainerMargin),
                ObjectAnimator.ofFloat(v, "alpha", 0, 1)
        );
        set.setDuration(ANIMATION_DURATION);
        set.setInterpolator(ANIMATION_INTERPOLATOR);
        set.start();

    }

    private void slideInToBottom(View editContainer)
    {
        View v = findViewById(R.id.edit_mode_container);

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

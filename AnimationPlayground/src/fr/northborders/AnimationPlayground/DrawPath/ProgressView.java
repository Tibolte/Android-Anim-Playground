package fr.northborders.AnimationPlayground.DrawPath;

import com.nineoldandroids.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import fr.northborders.AnimationPlayground.R;
import fr.northborders.AnimationPlayground.Utils.SvgHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thibaultguegan on 29/05/2014.
 */
public class ProgressView extends View {

    private static final String LOG_TAG = "StateView";

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final Object mSvgLock = new Object();
    private List<SvgHelper.SvgPath> mPaths = new ArrayList<SvgHelper.SvgPath>(0);
    private Thread mLoader;

    private SvgHelper.SvgPath mWaitPath;
    private SvgHelper.SvgPath mDragPath;
    private Paint mArrowPaint;
    private int mArrowLength;
    private int mArrowHeight;

    private float mPhase;
    private float mWait;
    private float mDrag;
    private float mFadeFactor;
    private int mDuration;

    private int mRadius;
    private ObjectAnimator mWaitAnimator;


    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mPaint.setStyle(Paint.Style.STROKE);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SvgView, defStyle, 0);
        try {
            if (a != null) {
                mPaint.setStrokeWidth(a.getFloat(R.styleable.SvgView_strokeWidth, 1.0f));
                mPaint.setColor(a.getColor(R.styleable.SvgView_strokeColor, 0xff000000));
                mPhase = a.getFloat(R.styleable.SvgView_phase, 0.0f);
                mDuration = a.getInt(R.styleable.SvgView_duration, 4000);
                mFadeFactor = a.getFloat(R.styleable.SvgView_fadeFactor, 10.0f);
                mRadius = a.getDimensionPixelSize(R.styleable.SvgView_waitRadius, 50);
                mArrowLength = a.getDimensionPixelSize(R.styleable.SvgView_arrowLength, 32);
                mArrowHeight = a.getDimensionPixelSize(R.styleable.SvgView_arrowHeight, 18);
            }
        } finally {
            if (a != null) a.recycle();
        }

        init();
    }

    private void init() {
        mPaint.setStyle(Paint.Style.STROKE);

        createWaitPath();

        mWaitAnimator = ObjectAnimator.ofFloat(this, "wait", 1.0f, 0.0f).setDuration(mDuration);
        mWaitAnimator.setRepeatMode(ObjectAnimator.RESTART);
        mWaitAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        mWaitAnimator.setInterpolator(new LinearInterpolator());
        mWaitAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(0.0f, getHeight() - getPaddingBottom() - mRadius * 3.0f);
        if (mWaitPath.paint.getAlpha() > 0) {
            canvas.translate(getWidth() / 2.0f - mRadius * 3.0f, mRadius);
            canvas.drawPath(mWaitPath.path, mWaitPath.paint);
        } else {
            canvas.translate((getWidth() - mDragPath.bounds.width()) / 2.0f, 0.0f);
            canvas.drawPath(mDragPath.path, mDragPath.paint);
            canvas.drawPath(mDragPath.path, mArrowPaint);
        }
        canvas.restore();
    }

    private void createWaitPath() {
        Paint paint = new Paint(mPaint);
        paint.setStrokeWidth(paint.getStrokeWidth() * 4.0f);

        Path p = new Path();
        p.moveTo(0.0f, 0.0f);
        p.lineTo(mRadius * 6.0f, 0.0f);

        mWaitPath = new SvgHelper.SvgPath(p, paint);
        mArrowPaint = new Paint(mWaitPath.paint);

        paint = new Paint(mWaitPath.paint);
        mDragPath = new SvgHelper.SvgPath(makeDragPath(mRadius), paint);
    }

    private void updatePathsPhaseLocked() {
        final int count = mPaths.size();
        for (int i = 0; i < count; i++) {
            SvgHelper.SvgPath svgPath = mPaths.get(i);
            svgPath.paint.setPathEffect(createPathEffect(svgPath.length, mPhase, 0.0f));
        }
    }

    public float getPhase() {
        return mPhase;
    }

    public void setPhase(float phase) {
        mPhase = phase;
        synchronized (mSvgLock) {
            updatePathsPhaseLocked();
        }
        invalidate();
    }

    public float getWait() {
        return mWait;
    }

    public void setWait(float wait) {
        mWait = wait;
        mWaitPath.paint.setPathEffect(createConcaveArrowPathEffect(mWaitPath.length, mWait, 32.0f));

        invalidate();
    }

    public float getDrag() {
        return mDrag;
    }

    public void setDrag(float drag) {
        mDrag = drag;

        mDragPath.paint.setPathEffect(createPathEffect(mDragPath.length, mDrag, mArrowLength));
        mArrowPaint.setPathEffect(createArrowPathEffect(mDragPath.length, mDrag, mArrowLength));

        int alpha = (int) (Math.min((1.0f - mDrag) * mFadeFactor, 1.0f) * 255.0f);
        mDragPath.paint.setAlpha(alpha);
        mArrowPaint.setAlpha(alpha);

        invalidate();
    }

    private static PathEffect createPathEffect(float pathLength, float phase, float offset) {
        return new DashPathEffect(new float[] { pathLength, pathLength },
                Math.max(phase * pathLength, offset));
    }

    private PathEffect createArrowPathEffect(float pathLength, float phase, float offset) {
        return new PathDashPathEffect(makeArrow(mArrowLength, mArrowHeight), pathLength,
                Math.max(phase * pathLength, offset), PathDashPathEffect.Style.ROTATE);
    }

    private PathEffect createConcaveArrowPathEffect(float pathLength, float phase, float offset) {
        return new PathDashPathEffect(makeConcaveArrow(mArrowLength, mArrowHeight), mArrowLength * 1.2f,
                Math.max(phase * pathLength, offset), PathDashPathEffect.Style.ROTATE);
    }

    private static Path makeDragPath(int radius) {
        Path p = new Path();
        RectF oval = new RectF(0.0f, 0.0f, radius * 2.0f, radius * 2.0f);

        float cx = oval.centerX();
        float cy = oval.centerY();
        float rx = oval.width() / 2.0f;
        float ry = oval.height() / 2.0f;

        final float TAN_PI_OVER_8 = 0.414213562f;
        final float ROOT_2_OVER_2 = 0.707106781f;

        float sx = rx * TAN_PI_OVER_8;
        float sy = ry * TAN_PI_OVER_8;
        float mx = rx * ROOT_2_OVER_2;
        float my = ry * ROOT_2_OVER_2;

        float L = oval.left;
        float T = oval.top;
        float R = oval.right;
        float B = oval.bottom;

        p.moveTo(R, cy);
        p.quadTo(      R, cy + sy, cx + mx, cy + my);
        p.quadTo(cx + sx, B, cx, B);
        p.quadTo(cx - sx,       B, cx - mx, cy + my);
        p.quadTo(L, cy + sy, L, cy);
        p.quadTo(      L, cy - sy, cx - mx, cy - my);
        p.quadTo(cx - sx, T, cx, T);
        p.lineTo(cx, T - oval.height() * 1.3f);

        return p;
    }

    private static Path makeArrow(float length, float height) {
        Path p = new Path();
        p.moveTo(-2.0f, -height / 2.0f);
        p.lineTo(length, 0.0f);
        p.lineTo(-2.0f, height / 2.0f);
        p.close();
        return p;
    }

    private static Path makeConcaveArrow(float length, float height) {
        Path p = new Path();
        p.moveTo(-2.0f, -height / 2.0f);
        p.lineTo(length - height / 4.0f, -height / 2.0f);
        p.lineTo(length, 0.0f);
        p.lineTo(length - height / 4.0f, height / 2.0f);
        p.lineTo(-2.0f, height / 2.0f);
        p.lineTo(-2.0f + height / 4.0f, 0.0f);
        p.close();
        return p;
    }

}

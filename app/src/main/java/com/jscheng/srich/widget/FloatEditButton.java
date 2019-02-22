package com.jscheng.srich.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.jscheng.srich.R;

/**
 * Created By Chengjunsen on 2019/2/22
 */
public class FloatEditButton extends android.support.v7.widget.AppCompatImageView implements View.OnClickListener{
    private static final String NameSpace = "http://schemas.android.com/apk/res/android";
    private OnClickListener mClickListener = null;
    private Animation mAnimation = null;

    public FloatEditButton(Context context) {
        this(context, null);
    }

    public FloatEditButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatEditButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int srcResource = attrs.getAttributeResourceValue(NameSpace, "src", 0);
        init(context, srcResource);
    }

    private void init(Context context, int src) {
        this.setClickable(true);
        super.setOnClickListener(this);
        if (src == 0) {
            Drawable defaultDrawable =context.getResources().getDrawable(R.mipmap.ic_compose, null);
            this.setScaleType(ScaleType.CENTER_CROP);
            this.setImageDrawable(defaultDrawable);
        }
        this.mAnimation = new RotateAnimation(0, 90,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        this.mAnimation.setDuration(500);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener listener) {
        this.mClickListener = listener;
    }

    @Override
    public void onClick(final View v) {
        this.startAnimation(mAnimation);
        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mClickListener != null) {
                    mClickListener.onClick(v);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }
}

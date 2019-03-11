package com.jscheng.srich.edit_note;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import com.jscheng.srich.R;

/**
 * Created By Chengjunsen on 2019/2/22
 */
public class FloatEditButton extends android.support.v7.widget.AppCompatImageView implements View.OnClickListener{
    private static final String NameSpace = "http://schemas.android.com/apk/res/android";
    private EditNotePresenter mPresenter;

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
            Drawable defaultDrawable =context.getResources().getDrawable(R.mipmap.ic_note_edit_edit, null);
            this.setScaleType(ScaleType.CENTER_CROP);
            this.setImageDrawable(defaultDrawable);
        }
    }

    public void setPresenter(EditNotePresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void onClick(final View v) {
        checkPresenter();
        AnimationSet animationSet = getHideAniamtion();
        this.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(GONE);
                mPresenter.tapEdit();
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

    public void show() {
        AnimationSet animationSet = getShowAniamtion();
        this.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

    private AnimationSet getHideAniamtion() {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new TranslateAnimation(0, 0, 0, getMeasuredHeight()));
        animationSet.addAnimation(new AlphaAnimation(getAlpha(), getAlpha()/2));
        animationSet.setDuration(500);
        return animationSet;
    }

    private AnimationSet getShowAniamtion() {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new TranslateAnimation(0, 0, getMeasuredHeight(), 0));
        animationSet.addAnimation(new AlphaAnimation(getAlpha()/2, getAlpha()));
        animationSet.setDuration(300);
        return animationSet;
    }

    private void checkPresenter() {
        if (mPresenter == null) {
            throw new RuntimeException("you need to call setPresenter() at first");
        }
    }
}

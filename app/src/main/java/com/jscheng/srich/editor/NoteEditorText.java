package com.jscheng.srich.editor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class NoteEditorText extends AppCompatEditText {
    private static final String TAG = "NoteEditorText";
    public NoteEditorText(Context context) {
        this(context, null);
    }

    public NoteEditorText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoteEditorText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setFocusableInTouchMode(true); // 触摸获得焦点
        this.setBackground(null);
        this.setInputType(EditorInfo.TYPE_CLASS_TEXT
                | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE
                | EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        this.setHighlightColor(Color.GREEN);
        this.setCursorColor(Color.GREEN);
        this.setHandlerColor(Color.GREEN);
    }

    private void setHandlerColor(int color) {
        try {
            // Get the cursor resource id
            Field fieldLeftRes = TextView.class.getDeclaredField("mTextSelectHandleLeftRes");
            fieldLeftRes.setAccessible(true);
            int leftDrawableResId = fieldLeftRes.getInt(this);
            Field fieldRightRes = TextView.class.getDeclaredField("mTextSelectHandleRightRes");
            fieldRightRes.setAccessible(true);
            int rightDrawableResId = fieldRightRes.getInt(this);
            Field fieldCenterRes = TextView.class.getDeclaredField("mTextSelectHandleRes");
            fieldCenterRes.setAccessible(true);
            int centerDrawableResId = fieldCenterRes.getInt(this);

            // Get the drawable and set a color filter
            Drawable drawableLeft = ContextCompat.getDrawable(this.getContext(), leftDrawableResId);
            drawableLeft.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            Drawable drawableRight = ContextCompat.getDrawable(this.getContext(), rightDrawableResId);
            drawableRight.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            Drawable drawableCenter = ContextCompat.getDrawable(this.getContext(), centerDrawableResId);
            drawableCenter.setColorFilter(color, PorterDuff.Mode.SRC_IN);

            if (Build.VERSION.SDK_INT == 15) {
                Class<?> drawableFieldClass = TextView.class;
                Field fieldLeft = drawableFieldClass.getDeclaredField("mSelectHandleLeft");
                fieldLeft.setAccessible(true);
                fieldLeft.set(this, drawableLeft);
                Field fieldRight = drawableFieldClass.getDeclaredField("mSelectHandleRight");
                fieldRight.setAccessible(true);
                fieldRight.set(this, drawableRight);
                Field fieldCenter = drawableFieldClass.getDeclaredField("mSelectHandleCenter");
                fieldCenter.setAccessible(true);
                fieldCenter.set(this, drawableCenter);
            } else {
                Field fieldEditor = TextView.class.getDeclaredField("mEditor");
                fieldEditor.setAccessible(true);
                Object editor = fieldEditor.get(this);
                Field fieldLeft = editor.getClass().getDeclaredField("mSelectHandleLeft");
                fieldLeft.setAccessible(true);
                fieldLeft.set(editor, drawableLeft);
                Field fieldRight = editor.getClass().getDeclaredField("mSelectHandleRight");
                fieldRight.setAccessible(true);
                fieldRight.set(editor, drawableRight);
                Field fieldCenter = editor.getClass().getDeclaredField("mSelectHandleCenter");
                fieldCenter.setAccessible(true);
                fieldCenter.set(editor, drawableCenter);
            }
        } catch (Exception e) {
            Log.e(TAG, "->", e );
        }
    }

    public void setCursorColor(int color) {
        try {
            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            int drawableResId = field.getInt(this);
            field = TextView.class.getDeclaredField("mEditor");
            field.setAccessible(true);
            Object editor = field.get(this);
            Drawable drawable = ContextCompat.getDrawable(getContext(), drawableResId);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);

            if(Build.VERSION.SDK_INT >= 28){
                field = editor.getClass().getDeclaredField("mDrawableForCursor");
                field.setAccessible(true);
                field.set(editor, drawable);
            }else {
                Drawable[] drawables = {drawable, drawable};
                field = editor.getClass().getDeclaredField("mCursorDrawable");
                field.setAccessible(true);
                field.set(editor, drawables);
            }
        } catch (Exception e) {
            Log.e(TAG, "->", e );
        }
    }
}

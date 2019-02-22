package com.jscheng.srich.utils;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created By Chengjunsen on 2019/2/22
 */
public class EditTextUtil {
    private static final String TAG = "EditTextUtil";


    public static void setEditTextCursorColor(EditText editText, @ColorInt int color) {
        try {
            // Get the cursor resource id
            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            int drawableResId = field.getInt(editText);

            // Get the drawable and set a color filter
            Drawable drawable = ContextCompat.getDrawable(editText.getContext(), drawableResId);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            Drawable[] drawables = {drawable, drawable};

            if (Build.VERSION.SDK_INT == 15) {
                // Get the editor
                Class<?> drawableFieldClass = TextView.class;
                // Set the drawables
                field = drawableFieldClass.getDeclaredField("mCursorDrawable");
                field.setAccessible(true);
                field.set(editText, drawables);

            } else if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT <= 27) {
                // Get the editor
                field = TextView.class.getDeclaredField("mEditor");
                field.setAccessible(true);
                Object editor = field.get(editText);
                // Set the drawables
                field = editor.getClass().getDeclaredField("mCursorDrawable");
                field.setAccessible(true);
                field.set(editor, drawables);

            } else if (Build.VERSION.SDK_INT >= 28) {
                // TODO: -> Not working for 28
                // Get the editor
                field = TextView.class.getDeclaredField("mEditor");
                field.setAccessible(true);
                Object editor = field.get(editText);
                // Set the drawables
                field = editor.getClass().getDeclaredField("mDrawableForCursor");
                field.setAccessible(true);
                field.set(editor, drawables[0]);
            }

        } catch (Exception e) {
            Log.e(TAG, "-> ", e);
        }
    }

    public static void setEditTextHandleColor(EditText editText, @ColorInt int color) {
        try {
            // Get the cursor resource id
            Field fieldLeftRes = TextView.class.getDeclaredField("mTextSelectHandleLeftRes");
            fieldLeftRes.setAccessible(true);
            int leftDrawableResId = fieldLeftRes.getInt(editText);

            Field fieldRightRes = TextView.class.getDeclaredField("mTextSelectHandleRightRes");
            fieldRightRes.setAccessible(true);
            int rightDrawableResId = fieldRightRes.getInt(editText);

            Field fieldCenterRes = TextView.class.getDeclaredField("mTextSelectHandleRes");
            fieldCenterRes.setAccessible(true);
            int centerDrawableResId = fieldCenterRes.getInt(editText);

            // Get the drawable and set a color filter
            Drawable drawableLeft = ContextCompat.getDrawable(editText.getContext(), leftDrawableResId);
            drawableLeft.setColorFilter(color, PorterDuff.Mode.SRC_IN);

            Drawable drawableRight = ContextCompat.getDrawable(editText.getContext(), rightDrawableResId);
            drawableRight.setColorFilter(color, PorterDuff.Mode.SRC_IN);

            Drawable drawableCenter = ContextCompat.getDrawable(editText.getContext(), centerDrawableResId);
            drawableCenter.setColorFilter(color, PorterDuff.Mode.SRC_IN);

            if (Build.VERSION.SDK_INT == 15) {
                // Get the editor
                Class<?> drawableFieldClass = TextView.class;

                // Set the drawables
                Field fieldLeft = drawableFieldClass.getDeclaredField("mSelectHandleLeft");
                fieldLeft.setAccessible(true);
                fieldLeft.set(editText, drawableLeft);

                Field fieldRight = drawableFieldClass.getDeclaredField("mSelectHandleRight");
                fieldRight.setAccessible(true);
                fieldRight.set(editText, drawableRight);

                Field fieldCenter = drawableFieldClass.getDeclaredField("mSelectHandleCenter");
                fieldCenter.setAccessible(true);
                fieldCenter.set(editText, drawableCenter);

            } else {
                // Get the editor
                Field fieldEditor = TextView.class.getDeclaredField("mEditor");
                fieldEditor.setAccessible(true);
                Object editor = fieldEditor.get(editText);

                // Set the drawables
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
            Log.e(TAG, "-> ", e);
        }
    }

    @SuppressLint("ResourceType")
    public static void setEditTextHandleColor(EditText editText, int leftResId, int centerResId, int rightResId) {
        try {
            Field fieldLeftRes = TextView.class.getDeclaredField("mTextSelectHandleLeftRes");
            fieldLeftRes.setAccessible(true);
            Field fieldRightRes = TextView.class.getDeclaredField("mTextSelectHandleRightRes");
            fieldRightRes.setAccessible(true);
            Field fieldCenterRes = TextView.class.getDeclaredField("mTextSelectHandleRes");
            fieldCenterRes.setAccessible(true);

            Drawable drawableLeft = ContextCompat.getDrawable(editText.getContext(), leftResId);
            Drawable drawableRight = ContextCompat.getDrawable(editText.getContext(), rightResId);
            Drawable drawableCenter = ContextCompat.getDrawable(editText.getContext(), centerResId);

            if (Build.VERSION.SDK_INT == 15) {
                Class<?> drawableFieldClass = TextView.class;
                Field fieldLeft = drawableFieldClass.getDeclaredField("mSelectHandleLeft");
                fieldLeft.setAccessible(true);
                fieldLeft.set(editText, drawableLeft);
                Field fieldRight = drawableFieldClass.getDeclaredField("mSelectHandleRight");
                fieldRight.setAccessible(true);
                fieldRight.set(editText, drawableRight);
                Field fieldCenter = drawableFieldClass.getDeclaredField("mSelectHandleCenter");
                fieldCenter.setAccessible(true);
                fieldCenter.set(editText, drawableCenter);
            } else {
                Field fieldEditor = TextView.class.getDeclaredField("mEditor");
                fieldEditor.setAccessible(true);
                Object editor = fieldEditor.get(editText);
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
}

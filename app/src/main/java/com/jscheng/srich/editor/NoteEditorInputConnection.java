package com.jscheng.srich.editor;

import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnectionWrapper;

/**
 * Created By Chengjunsen on 2019/3/1
 */
public class NoteEditorInputConnection extends InputConnectionWrapper {
    private static final String TAG = "InputConnection";
    private INoteEditorManager mStyleManager;

    public NoteEditorInputConnection(INoteEditorManager mStyleManager) {
        super(null, true);
        this.mStyleManager = mStyleManager;
    }

    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        Log.d(TAG, "commitText: " + newCursorPosition + " count: " + text.length() + " -> " + text);
        if (text.length() == 0) {
            mStyleManager.commandDelete(true);
        } else {
            mStyleManager.commandInput(text, true);
        }
        return true;
    }

    /**
     * 当软键盘删除文本之前，会调用这个方法通知输入框，我们可以重写这个方法并判断是否要拦截这个删除事件。
     * 在谷歌输入法上，点击退格键的时候不会调用{@link #sendKeyEvent(KeyEvent event)}，
     * 而是直接回调这个方法，所以也要在这个方法上做拦截；
     **/
    @Override
    public boolean deleteSurroundingText(int beforeLength, int afterLength) {
        mStyleManager.commandDelete(beforeLength - afterLength, true);
        //super.deleteSurroundingText(beforeLength, afterLength);
        return true;
    }

    @Override
    public boolean deleteSurroundingTextInCodePoints(int beforeLength, int afterLength) {
        Log.e(TAG, "deleteSurroundingTextInCodePoints: " + (afterLength - beforeLength));
        return super.deleteSurroundingTextInCodePoints(beforeLength, afterLength);
    }

    /**
     * 当在软件盘上点击某些按钮（比如退格键，数字键，回车键等），该方法可能会被触发（取决于输入法的开发者），
     * 所以也可以重写该方法并拦截这些事件，这些事件就不会被分发到输入框了
     **/
    @Override
    public boolean sendKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DEL:
                    mStyleManager.commandDelete(true);
                    return true;
                case KeyEvent.KEYCODE_ENTER:
                    mStyleManager.commandEnter(true);
                    return true;
                default:
                    Log.e(TAG, "sendKeyEvent: " + event.getKeyCode());
                    return super.sendKeyEvent(event);
            }
        }
        return super.sendKeyEvent(event);
    }
}

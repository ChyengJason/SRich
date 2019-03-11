package com.jscheng.srich.editor;
import android.net.Uri;
import com.jscheng.srich.model.Note;

/**
 * Created By Chengjunsen on 2019/2/27
 */
public class NoteEditorManager implements INoteEditorManager{
    private NoteEditorManagerImpl mManagerImpl;

    public NoteEditorManager(NoteEditorText editorText) {
        mManagerImpl = new NoteEditorManagerImpl(editorText);
    }

    public void reset(Note note) {
        mManagerImpl.reset(note);
    }

    @Override
    public void addSelectionChangeListener(NoteEditorSelectionListener listener) {
        mManagerImpl.addSelectionChangeListener(listener);
    }

    @Override
    public void requestDraw() {
        mManagerImpl.requestDraw();
    }

    @Override
    public void commandImage(Uri uri, boolean draw) {
        String url = uri.toString();
        mManagerImpl.inputImage(url);
        if (draw) { requestDraw(); }
    }

    @Override
    public void commandImage(String url, boolean draw) {
        mManagerImpl.inputImage(url);
        if (draw) { requestDraw(); }
    }

    @Override
    public void commandColor(boolean isSelected, boolean draw) {
        mManagerImpl.inputColor(isSelected);
        if (draw) { requestDraw(); }
    }

    @Override
    public void commandUnderline(boolean isSelected, boolean draw) {
        mManagerImpl.inputUnderline(isSelected);
        if (draw) { requestDraw(); }
    }

    @Override
    public void commandItalic(boolean isSelected, boolean draw) {
        mManagerImpl.inputItalic(isSelected);
        if (draw) { requestDraw(); }
    }

    @Override
    public void commandBold(boolean isSelected, boolean draw) {
        mManagerImpl.inputBold(isSelected);
        if (draw) { requestDraw(); }
    }

    @Override
    public void commandSuperscript(boolean isSelected, boolean draw) {
        mManagerImpl.inputSuperscript(isSelected);
        if (draw) { requestDraw(); }

    }

    @Override
    public void commandSubscript(boolean isSelected, boolean draw) {
        mManagerImpl.inputSubscript(isSelected);
        if (draw) { requestDraw(); }
    }

    @Override
    public void commandStrikeThrough(boolean isSelected, boolean draw) {
        mManagerImpl.inputStrikeThrough(isSelected);
        if (draw) { requestDraw(); }
    }

    @Override
    public void commandDividingLine(boolean draw) {
        mManagerImpl.inputDividingLine();
        if (draw) { requestDraw(); }
    }

    @Override
    public void commandBulletList(boolean isSelected, boolean draw) {
        mManagerImpl.inputBulletList(isSelected);
        if (draw) { requestDraw(); }
    }

    @Override
    public void commandNumList(boolean isSelected, boolean draw) {
        mManagerImpl.inputNumList(isSelected);
        if (draw) { requestDraw(); }
    }

    @Override
    public void commandCheckBox(boolean isSelected, boolean draw) {
        mManagerImpl.inputUnCheckBox(isSelected);
        if (draw) { requestDraw(); }
    }

    @Override
    public void commandIndentation(boolean draw) {
        mManagerImpl.inputIndentation();
        if (draw) { requestDraw(); }
    }

    @Override
    public void commandReduceIndentation(boolean draw) {
        mManagerImpl.inputReduceIndentation();
        if (draw) { requestDraw(); }
    }

    @Override
    public void commandDeleteSelection(boolean draw) {
        mManagerImpl.deleteSelection();
        if (draw) { requestDraw(); }
    }

    @Override
    public void commandDelete(boolean draw) {
        mManagerImpl.deleteSelection();
        if (draw) { requestDraw(); }
    }

    @Override
    public void commandDelete(int num, boolean draw) {
        mManagerImpl.deleteSelection(num);
        if (draw) { requestDraw(); }
    }

    @Override
    public void commandPaste(String content, boolean draw) {
        commandInput(content, draw);
    }

    @Override
    public void commandEnter(boolean draw) {
        mManagerImpl.inputEnter();
        if (draw) { requestDraw(); }
    }

    @Override
    public void commandInput(CharSequence content, boolean draw) {
        if (content.length() > 0) {
            StringBuilder contentNoEnter = new StringBuilder();
            for (int i = 0; i < content.length(); i++) {
                char c = content.charAt(i);
                if (c == NoteEditorRender.EndCodeChar) {
                    mManagerImpl.inputParagraph(contentNoEnter.toString());
                    mManagerImpl.inputEnter();
                    contentNoEnter.delete(0, contentNoEnter.length());
                } else {
                    contentNoEnter.append(c);
                }
            }
            if (contentNoEnter.length() > 0) {
                mManagerImpl.inputParagraph(contentNoEnter.toString());
            }
        }
        if (draw) { requestDraw(); }
    }

    public String getSelectionText() {
        return mManagerImpl.getSelectionText();
    }

    public void onSelectionChanged(int selStart, int selEnd) {
        mManagerImpl.onSelectionChanged(selStart, selEnd);
    }

    public boolean onSpanTouchUp(int off, long interval) {
        return mManagerImpl.onSpanTouchUp(off, interval);
    }

    public boolean onSpanTouchDown(int off) {
        return mManagerImpl.onSpanTouchDown(off);
    }
}

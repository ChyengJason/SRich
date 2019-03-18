package com.jscheng.srich.revoke;

import com.jscheng.srich.model.Note;
import com.jscheng.srich.model.NoteSnap;
import com.jscheng.srich.model.NoteSnapBuilder;

import java.util.LinkedList;

/**
 * Created By Chengjunsen on 2019/3/18
 */
public class NoteRevocationManager {

    /**
     * 最大撤销步数
     */
    private final static int MAX_REVOCATION_STEP_COUNT = 10;

    /**
     * 最大间隔时间，可当做是同一动作
     */
    private final static int MAX_INTERVER_MILLTIME = 1000;

    /**
     * 撤销栈
     */
    private LinkedList<NoteSnap> mRevocationList;

    /**
     * 恢复栈
     */
    private LinkedList<NoteSnap> mRecoveryList;

    /**
     * 可连续的动作
     */
    private boolean isContinuousAction;

    private NoteSnap currentSnap;

    private boolean isRunning;

    public NoteRevocationManager() {
        this.mRecoveryList = new LinkedList<>();
        this.mRevocationList = new LinkedList<>();
        this.isRunning = false;
        this.isContinuousAction = false;
        this.currentSnap = null;
    }

    /**
     * 恢复
     */
    public NoteSnap recover(Note note, int selectionBegin, int selectionEnd) {
        if (mRecoveryList.isEmpty()){
            return null;
        }

        NoteSnap currentSnap = new NoteSnapBuilder(note)
                .continuous(isContinuousAction)
                .selection(selectionBegin, selectionEnd)
                .build();
        mRevocationList.push(currentSnap);
        return mRecoveryList.pop();
    }

    /**
     * 撤销
     */
    public NoteSnap revoke(Note note, int selectionBegin, int selectionEnd) {
        if (mRevocationList.isEmpty()) {
            return null;
        }
        NoteSnap currentSnap = new NoteSnapBuilder(note)
                .continuous(isContinuousAction)
                .selection(selectionBegin, selectionEnd)
                .build();
        mRecoveryList.push(currentSnap);
        return mRevocationList.pop();
    }

    /**
     * 是否可以恢复
     */
    public boolean isCanRecover() {
        return mRecoveryList.size() > 0;
    }

    /**
     * 是否可以撤销
     */
    public boolean isCanRevoke() {
        return mRevocationList.size() > 0;
    }

    /**
     * 开始动作
     */
    public void beginAction(Note note, int selectionBegin, int selectionEnd, boolean isContinuous) {
        if (isRunning) {
            throw new RuntimeException("you should end action before");
        }
        isRunning = true;
        isContinuousAction = isContinuous;

        currentSnap = new NoteSnapBuilder(note)
                .continuous(isContinuousAction)
                .selection(selectionBegin, selectionEnd)
                .build();
    }

    /**
     * 结束动作
     */
    public void endAction() {
        if (!isRunning) {
            throw new RuntimeException("you should begin action before");
        }
        isRunning = false;
        mRecoveryList.clear();

        if (!mRevocationList.isEmpty()) {
            NoteSnap lastSnap = mRevocationList.getLast();
            if (lastSnap.isContinuousAction() && isContinuousAction &&
                    Math.abs(currentSnap.getTime() - lastSnap.getTime()) < MAX_INTERVER_MILLTIME) {
                return;
            }
        }

        mRevocationList.push(currentSnap);
        checkMaxActions();
    }

    private void checkMaxActions() {
        while (mRevocationList.size() > MAX_REVOCATION_STEP_COUNT) {
            mRevocationList.removeLast();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

}

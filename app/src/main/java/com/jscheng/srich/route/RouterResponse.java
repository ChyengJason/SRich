package com.jscheng.srich.route;

import android.content.Intent;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class RouterResponse {

    private boolean isDone;

    private String msg;

    private Intent intent;

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}

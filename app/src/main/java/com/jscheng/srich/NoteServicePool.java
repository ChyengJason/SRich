package com.jscheng.srich;

import android.support.annotation.NonNull;

import com.jscheng.srich.model.Note;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created By Chengjunsen on 2019/3/7
 */
public class NoteServicePool {

    private ThreadPoolExecutor mExecutors;
    private int corePoolSize = 2;
    private int maximumPoolSize = 5;
    private int keepLive = 10;

    public static NoteServicePool mInstance;

    public static NoteServicePool getInstance() {
        if (mInstance == null) {
            synchronized (NoteServicePool.class) {
                if (mInstance == null) {
                    mInstance = new NoteServicePool();
                }
            }
        }
        return mInstance;
    }

    public NoteServicePool() {
        mExecutors = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepLive,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(50), new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("NoteService " + thread.getId());
                return thread;
            }
        });
    }

    public void updateNote(Note mNote) {
        mExecutors.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}

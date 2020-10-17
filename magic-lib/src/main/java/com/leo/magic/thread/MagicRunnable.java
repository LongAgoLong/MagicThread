package com.leo.magic.thread;

import androidx.annotation.CallSuper;

public abstract class MagicRunnable implements Runnable {
    private Thread mThread;

    public void stop() {
        if (mThread != null && !mThread.isInterrupted()) {
            mThread.interrupt();
        }
    }

    @CallSuper
    @Override
    public void run() {
        mThread = Thread.currentThread();
    }
}

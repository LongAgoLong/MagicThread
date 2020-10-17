package com.leo.magic.thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class ThreadController {
    private static final HandlerThread mHandlerThread = new HandlerThread("Thread BackGround");
    private static final Handler mBackHandler;
    private static final Handler mUIHandler;

    static {
        mHandlerThread.start();
        mBackHandler = new Handler(mHandlerThread.getLooper());
        mUIHandler = new Handler(Looper.getMainLooper());
    }

    public static void runOnUIThread(MagicRunnable runnable, int delayMillis) {
        if (delayMillis > 0) {
            mUIHandler.postDelayed(runnable, delayMillis);
        } else {
            mUIHandler.post(runnable);
        }
    }

    public static void runOnIOThread(final MagicRunnable runnable, int delayMillis) {
        if (delayMillis == 0) {
            IOThreadPool.execute(runnable);
        } else {
            mUIHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    IOThreadPool.execute(runnable);
                }
            }, delayMillis);
        }
    }

    public static void runOnCalcThread(final MagicRunnable runnable, int delayMillis) {
        if (delayMillis == 0) {
            CalcThreadPool.execute(runnable);
        } else {
            mUIHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    CalcThreadPool.execute(runnable);
                }
            }, delayMillis);
        }
    }

    public static void runOnBackThread(MagicRunnable runnable, int delayMillis) {
        if (delayMillis > 0) {
            mBackHandler.postDelayed(runnable, delayMillis);
        } else {
            mBackHandler.post(runnable);
        }
    }

    public static void removeTask(MagicRunnable runnable) {
        mUIHandler.removeCallbacks(runnable);
        mBackHandler.removeCallbacks(runnable);
        IOThreadPool.cancel(runnable);
        CalcThreadPool.cancel(runnable);
        runnable.stop();
    }
}

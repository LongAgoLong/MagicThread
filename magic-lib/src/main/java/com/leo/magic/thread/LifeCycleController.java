package com.leo.magic.thread;


import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class LifeCycleController implements LifecycleObserver {


    private static ConcurrentHashMap<LifecycleOwner, List<MagicRunnable>> mTasks = new ConcurrentHashMap<>();

    public synchronized void subscribe(LifecycleOwner lifecycleOwner, MagicRunnable runnable) {
        lifecycleOwner.getLifecycle().addObserver(this);
        if (!mTasks.containsKey(lifecycleOwner)) {
            List<MagicRunnable> runnableList = Collections.synchronizedList(new ArrayList<MagicRunnable>());
            runnableList.add(runnable);
            mTasks.put(lifecycleOwner, runnableList);
        } else {
            List<MagicRunnable> runnableList = mTasks.get(lifecycleOwner);
            runnableList.add(runnable);
        }
    }

    public synchronized void unSubscribe(LifecycleOwner lifecycleOwner, MagicRunnable runnable) {
        if (mTasks.containsKey(lifecycleOwner)) {
            List<MagicRunnable> runnableList = mTasks.get(lifecycleOwner);
            runnableList.remove(runnable);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public synchronized void onDestroy(LifecycleOwner source) {
        List<MagicRunnable> runnableList = mTasks.get(source);
        for (MagicRunnable runnable : runnableList) {
            ThreadController.removeTask(runnable);
        }
        mTasks.remove(source);
    }

}

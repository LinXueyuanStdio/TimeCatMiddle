package com.timecat.middle.block.page;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/2
 * @description null
 * @usage null
 */
public class PageLifecycleDispatcher {
    private final LifecycleRegistry mRegistry;
    private final Handler mHandler;
    private DispatchRunnable mLastDispatchRunnable;

    /**
     * @param provider {@link LifecycleOwner} for a service, usually it is a service itself
     */
    public PageLifecycleDispatcher(@NonNull LifecycleOwner provider) {
        mRegistry = new LifecycleRegistry(provider);
        mHandler = new Handler();
    }

    private void postDispatchRunnable(Lifecycle.Event event) {
        if (mLastDispatchRunnable != null) {
            mLastDispatchRunnable.run();
        }
        mLastDispatchRunnable = new DispatchRunnable(mRegistry, event);
        mHandler.postAtFrontOfQueue(mLastDispatchRunnable);
    }

    public void onPagePreSuperOnCreate() {
        postDispatchRunnable(Lifecycle.Event.ON_CREATE);
    }

    public void onPagePreSuperOnPause() {
        postDispatchRunnable(Lifecycle.Event.ON_PAUSE);
    }

    public void onPagePreSuperOnResume() {
        postDispatchRunnable(Lifecycle.Event.ON_RESUME);
    }

    public void onPagePreSuperOnStart() {
        postDispatchRunnable(Lifecycle.Event.ON_START);
    }

    public void onPagePreSuperOnDestroy() {
        postDispatchRunnable(Lifecycle.Event.ON_STOP);
        postDispatchRunnable(Lifecycle.Event.ON_DESTROY);
    }

    /**
     * @return {@link Lifecycle} for the given {@link LifecycleOwner}
     */
    @NonNull
    public Lifecycle getLifecycle() {
        return mRegistry;
    }

    static class DispatchRunnable implements Runnable {
        private final LifecycleRegistry mRegistry;
        final Lifecycle.Event mEvent;
        private boolean mWasExecuted = false;

        DispatchRunnable(@NonNull LifecycleRegistry registry, Lifecycle.Event event) {
            mRegistry = registry;
            mEvent = event;
        }

        @Override
        public void run() {
            if (!mWasExecuted) {
                mRegistry.handleLifecycleEvent(mEvent);
                mWasExecuted = true;
            }
        }
    }
}

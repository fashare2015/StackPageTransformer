package com.fashare.stackpagetransform;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务的 ViewPager
 */
public class TimerViewPager extends ViewPager {
    protected final String TAG = this.getClass().getSimpleName();

    private ScheduledExecutorService mTimer;    // 定时器
    private OnTimerSchedule mOnTimerSchedule = new OnTimerSchedule() {
        @Override
        public void runOnUiThread() {
            ViewPager vp = TimerViewPager.this;
            int nextPageOffset = (vp.getCurrentItem()+1) % vp.getAdapter().getCount();
            vp.setCurrentItem(nextPageOffset);
        }
    };
    private boolean mIsTouching;    // 触摸状态

    public void setOnTimerSchedule(OnTimerSchedule onTimerSchedule) {
        mOnTimerSchedule = onTimerSchedule;
    }

    public TimerViewPager(Context context) {
        super(context);
    }

    public TimerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * <p>
     * 触摸暂停, 重写以下方法皆可:
     * <li/> dispatchTouchEvent
     * <li/> onInterceptTouchEvent
     * <li/> onTouchEvent
     * <p/>
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "DOWN");
                mIsTouching = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "UP");
                mIsTouching = false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Log.d(TAG, "onWindowFocusChanged: hasWindowFocus = " + hasWindowFocus);
        if(hasWindowFocus)
            start();
        else
            stop();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, "onAttachedToWindow");
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "onDetachedFromWindow");
        stop();
    }

    public void start() {
        Log.i(TAG, "start");
        clearTimer();
        mTimer = Executors.newSingleThreadScheduledExecutor();
        mTimer.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!mIsTouching && getAdapter()!= null
                        && getChildCount()>1 && mOnTimerSchedule != null)
                    TimerViewPager.this.post(new Runnable(){
                        @Override
                        public void run() {
                            mOnTimerSchedule.runOnUiThread();
                        }
                    });  // mTimer 在子线程, 应当 post 到 MainThread
            }
        }, 1000 * 2, 1000 * 2, TimeUnit.MILLISECONDS);
    }

    public void pause() {
        clearTimer();
    }

    public void stop(){
        Log.i(TAG, "stop");
        clearTimer();
    }

    private void clearTimer() {
        if (mTimer != null && !mTimer.isShutdown()) {
            mTimer.shutdown();
        }
        mTimer = null;
    }

    public interface OnTimerSchedule{
        void runOnUiThread();
    }
}

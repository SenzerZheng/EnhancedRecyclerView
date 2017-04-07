package com.senzer.enhancedrecyclerview.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ProjectName: SlidingRecyclerView
 * Description: 可设置是否禁止，滑动RecyclerView，主要解决在5.1系统中嵌套在ScrollView中的惯性问题
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 2016/8/22 21:10
 */
public class SlidingRecyclerView extends RecyclerView {
    private boolean isCanSliding = true;

    public SlidingRecyclerView(Context context) {
        super(context);
    }

    public SlidingRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isCanSliding && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isCanSliding && super.onInterceptTouchEvent(event);
    }

    /**
     * whether can scroll left and right
     *
     * @param canSliding true: scrolling, false: do not scroll
     */
    public void setCanSliding(boolean canSliding) {
        this.isCanSliding = canSliding;
    }
}

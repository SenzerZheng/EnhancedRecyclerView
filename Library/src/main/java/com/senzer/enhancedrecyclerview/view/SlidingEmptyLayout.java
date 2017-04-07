package com.senzer.enhancedrecyclerview.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * ProjectName: SlidingEmptyLayout
 * Description: 可设置是否{@link SwipeRefreshLayout}滑动而滑动的EmptyLayout
 * 即：在SlidingEmptyLayout显示时，是否需要下拉刷新，更新数据功能。
 * <p>
 * review by chenpan, wangkang, wangdong 2017/3/30
 * edit by JeyZheng 2017/3/30
 * author: JeyZheng
 * version: 4.0
 * created at: 2017/3/30 11:41
 */
public class SlidingEmptyLayout extends RelativeLayout {

    private boolean isCanSliding = true;

    public SlidingEmptyLayout(Context context) {
        super(context);
    }

    public SlidingEmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingEmptyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SlidingEmptyLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return isCanSliding || super.onTouchEvent(event);
    }

    /**
     * @param canSliding true: can pull down to refresh, false: can not pull down
     */
    public void setCanSliding(boolean canSliding) {
        isCanSliding = canSliding;
    }
}

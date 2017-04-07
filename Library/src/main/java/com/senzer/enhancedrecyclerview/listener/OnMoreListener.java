package com.senzer.enhancedrecyclerview.listener;

/**
 * ProjectName: OnMoreListener
 * Description: Classes that wish to be notified when the swipe gesture correctly
 * triggers a load more should implement this interface.
 * <p>
 * review by chenpan, wangkang, wangdong 2017/4/7
 * edit by JeyZheng 2017/4/7
 * author: JeyZheng
 * version: 4.0
 * created at: 2017/4/7 13:25
 */
public interface OnMoreListener {
    /**
     * @param overallItemsCount
     * @param itemsBeforeMore
     * @param maxLastVisiblePosition for staggered grid this is max of all spans
     */
    void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition);
}

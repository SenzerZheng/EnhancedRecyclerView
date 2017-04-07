package com.senzer.enhancedrecyclerview.listener;

/**
 * ProjectName: OnRefreshListener
 * Description: Classes that wish to be notified when the swipe gesture correctly
 * triggers a refresh should implement this interface.
 * <p>
 * review by chenpan, wangkang, wangdong 2017/4/7
 * edit by JeyZheng 2017/4/7
 * author: JeyZheng
 * version: 4.0
 * created at: 2017/4/7 11:35
 */
public interface OnRefreshListener {
    /**
     * refresh action
     */
    void onRefresh();
}
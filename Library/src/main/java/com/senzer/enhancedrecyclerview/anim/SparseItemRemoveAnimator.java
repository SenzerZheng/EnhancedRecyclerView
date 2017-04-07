package com.senzer.enhancedrecyclerview.anim;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

/**
 * ProjectName: SparseItemRemoveAnimator
 * Description: remove animation
 *
 * review by chenpan, wangkang, wangdong 2017/4/7
 * edit by JeyZheng 2017/4/7
 * author: JeyZheng
 * version: 4.0
 * created at: 2017/4/7 13:41
 */
public class SparseItemRemoveAnimator extends DefaultItemAnimator {

    private boolean skipNext = false;

    public void setSkipNext(boolean skipNext) {
        this.skipNext = skipNext;
    }

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        if (!skipNext) {
            return super.animateRemove(holder);
        } else {
            dispatchRemoveFinished(holder);
            skipNext = false;
            return false;
        }
    }
}

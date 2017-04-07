package com.senzer.enhancedrecyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.senzer.enhancedrecyclerview.library.R;
import com.senzer.enhancedrecyclerview.listener.OnMoreListener;
import com.senzer.enhancedrecyclerview.listener.OnRefreshListener;
import com.senzer.enhancedrecyclerview.listener.SwipeDismissRVTouchListener;
import com.senzer.enhancedrecyclerview.util.FloatUtil;
import com.senzer.enhancedrecyclerview.view.SwipeRefreshLayout;

/**
 * ProjectName: EnhancedRecyclerView
 * Description:
 * <p>
 * 1.1. replace {@link com.malinskiy.superrecyclerview.SuperRecyclerView} with EnhancedRecyclerView.
 * 1.2. replace {@link android.support.v4.widget.SwipeRefreshLayout} with EnhancedSwipeRefreshLayout
 * 2. the refresh view and the more progress
 * 1). the refresh view: setHeaderView inside EnhancedSwipeRefreshLayout
 * 2). the more progress: mMoreProgress on the current page
 * 3. it is a manager to control the view which is from layout.xml
 * <p>
 * review by chenpan, wangkang, wangdong 2016/11/11
 * edit by JeyZheng 2016/11/11
 * author: JeyZheng
 * version: 4.0
 * created at: 2016/11/7 19:02
 */
@SuppressWarnings("ALL")
public class EnhancedRecyclerView extends FrameLayout implements SwipeRefreshLayout.OnPullRefreshListener {

    protected int ITEM_LEFT_TO_LOAD_MORE = 10;

    protected RecyclerView mRecycler;
    // 当ViewStub被调用inflate之后，这个ViewStub在布局中将被使用指定的View替换，
    // 所以inflate过一遍的ViewStub，如果被隐藏之后再次想要显示，将不能使用inflate()方法，
    // 但是可以再次使用setVisibility(int)方法设置为可见，这就是这两个方法的区别。
    protected ViewStub mProgress;
    protected ViewStub mMoreProgress;
    protected ViewStub mEmpty;
    @SuppressLint("unused")
    protected View mProgressView;
    protected View mMoreProgressView;
    protected View mEmptyView;

    protected boolean mClipToPadding;
    protected int mPadding;
    protected int mPaddingTop;
    protected int mPaddingBottom;
    protected int mPaddingLeft;
    protected int mPaddingRight;
    protected int mScrollbarStyle;
    protected int mEmptyId;             // empty view
    protected int mRefreshId;           // refresh view
    protected int mMoreProgressId;      // more progress

    protected LAYOUT_MANAGER_TYPE layoutManagerType;

    protected RecyclerView.OnScrollListener mInternalOnScrollListener;
    private RecyclerView.OnScrollListener mSwipeDismissScrollListener;
    protected RecyclerView.OnScrollListener mExternalOnScrollListener;

    protected OnRefreshListener mOnRefreshListener;
    protected OnMoreListener mOnMoreListener;
    protected boolean isLoadingMore;
    protected SwipeRefreshLayout mPtrLayout;
    // ------------- header view inside the EnhancedSwipeRefreshLayout --------------
    protected ProgressBar mProgressBar;
    private TextView tvTips;
    private ImageView ivArrow;
    // ------------- header view end --------------

    // ------------- footer view inside the EnhancedSwipeRefreshLayout --------------
    // ...
    // ------------- footer view end --------------

    protected int mEnhancedRecyclerViewMainLayout;
    private int mProgressId;

    private int[] lastScrollPositions;

    public SwipeRefreshLayout getSwipeToRefresh() {
        return mPtrLayout;
    }

    public RecyclerView getRecyclerView() {
        return mRecycler;
    }

    public EnhancedRecyclerView(Context context) {
        super(context);
        initView();
    }

    public EnhancedRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initView();
    }

    public EnhancedRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(attrs);
        initView();
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.EnhancedRV);
        try {
            mEnhancedRecyclerViewMainLayout = a.getResourceId(
                    R.styleable.EnhancedRV_mainLayoutId, R.layout.layout_progress_recyclerview);
            mClipToPadding = a.getBoolean(R.styleable.EnhancedRV_recyclerClipToPadding, false);
            mPadding = (int) a.getDimension(R.styleable.EnhancedRV_recyclerPadding, -1.0f);
            mPaddingTop = (int) a.getDimension(R.styleable.EnhancedRV_recyclerPaddingTop, 0.0f);
            mPaddingBottom = (int) a.getDimension(R.styleable.EnhancedRV_recyclerPaddingBottom, 0.0f);
            mPaddingLeft = (int) a.getDimension(R.styleable.EnhancedRV_recyclerPaddingLeft, 0.0f);
            mPaddingRight = (int) a.getDimension(R.styleable.EnhancedRV_recyclerPaddingRight, 0.0f);
            mScrollbarStyle = a.getInt(R.styleable.EnhancedRV_scrollbarStyle, -1);

            mEmptyId = a.getResourceId(R.styleable.EnhancedRV_layout_empty, 0);
            mMoreProgressId = a.getResourceId(
                    R.styleable.EnhancedRV_layout_moreProgress, R.layout.layout_more_progress);
            mProgressId = a.getResourceId(
                    R.styleable.EnhancedRV_layout_progress, R.layout.layout_progress);
        } finally {
            a.recycle();
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.EnhancedRecyclerView);
        try {
            mRefreshId = typedArray.getResourceId(
                    R.styleable.EnhancedRecyclerView_layout_refresh, R.layout.layout_header);
        } finally {
            typedArray.recycle();
        }
    }

    private void initView() {
        if (isInEditMode()) {
            return;
        }

        View v = LayoutInflater.from(getContext()).inflate(mEnhancedRecyclerViewMainLayout, this);
        mPtrLayout = (SwipeRefreshLayout) v.findViewById(R.id.ptr_layout);
        mPtrLayout.setEnabled(false);
        if (mRefreshId == R.layout.layout_header) {                 // default header view
            /** STEP ONE: set OnPullRefreshListener */
//            mPtrLayout.setOnPullRefreshListener(this);
            setRefreshListener(this);
            /** STEP TWO: set the header view */
            setHeaderView(createHeaderView());
        }

        mProgress = (ViewStub) v.findViewById(android.R.id.progress);
        mProgress.setLayoutResource(mProgressId);
        mProgressView = mProgress.inflate();

        mMoreProgress = (ViewStub) v.findViewById(R.id.more_progress);
        mMoreProgress.setLayoutResource(mMoreProgressId);
        if (mMoreProgressId != 0) {
            mMoreProgressView = mMoreProgress.inflate();
        }
        mMoreProgress.setVisibility(View.GONE);

        mEmpty = (ViewStub) v.findViewById(R.id.empty);
        mEmpty.setLayoutResource(mEmptyId);
        if (mEmptyId != 0) {
            mEmptyView = mEmpty.inflate();
        }
        mEmpty.setVisibility(View.GONE);

        initRecyclerView(v);
    }

    /**
     * create the header view inside the EnhancedSwipeRefreshLayout
     *
     * @return
     */
    private View createHeaderView() {
        View headerView = LayoutInflater.from(getContext()).inflate(mRefreshId, null);
        mProgressBar = (ProgressBar) headerView.findViewById(R.id.pb_loading);
        tvTips = (TextView) headerView.findViewById(R.id.tv_tips);
        ivArrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
        return headerView;
    }

    /**
     * Implement this method to customize the AbsListView
     */
    protected void initRecyclerView(View view) {
        View recyclerView = view.findViewById(android.R.id.list);

        if (recyclerView instanceof RecyclerView)
            mRecycler = (RecyclerView) recyclerView;
        else
            throw new IllegalArgumentException("EnhancedRecyclerView works with a RecyclerView!");


        mRecycler.setClipToPadding(mClipToPadding);
        mInternalOnScrollListener = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                processOnMore();

                if (mExternalOnScrollListener != null)
                    mExternalOnScrollListener.onScrolled(recyclerView, dx, dy);
                if (mSwipeDismissScrollListener != null)
                    mSwipeDismissScrollListener.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mExternalOnScrollListener != null)
                    mExternalOnScrollListener.onScrollStateChanged(recyclerView, newState);
                if (mSwipeDismissScrollListener != null)
                    mSwipeDismissScrollListener.onScrollStateChanged(recyclerView, newState);
            }
        };
        mRecycler.addOnScrollListener(mInternalOnScrollListener);

        if (!FloatUtil.compareFloats(mPadding, -1.0f)) {
            mRecycler.setPadding(mPadding, mPadding, mPadding, mPadding);
        } else {
            mRecycler.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
        }

        if (mScrollbarStyle != -1) {
            mRecycler.setScrollBarStyle(mScrollbarStyle);
        }
    }

    private void processOnMore() {
        RecyclerView.LayoutManager layoutManager = mRecycler.getLayoutManager();
        int lastVisibleItemPosition = getLastVisibleItemPosition(layoutManager);
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();

        if (((totalItemCount - lastVisibleItemPosition) <= ITEM_LEFT_TO_LOAD_MORE ||
                (totalItemCount - lastVisibleItemPosition) == 0 && totalItemCount > visibleItemCount)
                && !isLoadingMore) {

            isLoadingMore = true;
            if (mOnMoreListener != null) {
                mMoreProgress.setVisibility(View.VISIBLE);
                mOnMoreListener.onMoreAsked(mRecycler.getAdapter().getItemCount(), ITEM_LEFT_TO_LOAD_MORE, lastVisibleItemPosition);
            }
        }
    }

    private int getLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        int lastVisibleItemPosition = -1;
        if (layoutManagerType == null) {
            if (layoutManager instanceof GridLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.GRID;
            } else if (layoutManager instanceof LinearLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
            } else {
                throw new RuntimeException("Unsupported LayoutManager used. " +
                        "Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }

        switch (layoutManagerType) {
            case LINEAR:
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case GRID:
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case STAGGERED_GRID:
                lastVisibleItemPosition = caseStaggeredGrid(layoutManager);
                break;
        }
        return lastVisibleItemPosition;
    }

    private int caseStaggeredGrid(RecyclerView.LayoutManager layoutManager) {
        StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
        if (lastScrollPositions == null)
            lastScrollPositions = new int[staggeredGridLayoutManager.getSpanCount()];

        staggeredGridLayoutManager.findLastVisibleItemPositions(lastScrollPositions);
        return findMax(lastScrollPositions);
    }


    private int findMax(int[] lastPositions) {
        int max = Integer.MIN_VALUE;
        for (int value : lastPositions) {
            if (value > max)
                max = value;
        }
        return max;
    }

    /**
     * @param adapter                       The new adapter to set, or null to set no adapter
     * @param compatibleWithPrevious        Should be set to true if new adapter uses the same {@android.support.v7.widget.RecyclerView.ViewHolder}
     *                                      as previous one
     * @param removeAndRecycleExistingViews If set to true, RecyclerView will recycle all existing Views. If adapters
     *                                      have stable ids and/or you want to animate the disappearing views, you may
     *                                      prefer to set this to false
     */
    private void setAdapterInternal(RecyclerView.Adapter adapter, boolean compatibleWithPrevious,
                                    boolean removeAndRecycleExistingViews) {
        if (compatibleWithPrevious) {
            mRecycler.swapAdapter(adapter, removeAndRecycleExistingViews);
        } else {
            mRecycler.setAdapter(adapter);
        }

        mProgress.setVisibility(View.GONE);
        mRecycler.setVisibility(View.VISIBLE);
        mPtrLayout.setRefreshing(false);
        if (null != adapter)
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    super.onItemRangeChanged(positionStart, itemCount);
                    update();
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    update();
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    super.onItemRangeRemoved(positionStart, itemCount);
                    update();
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                    update();
                }

                @Override
                public void onChanged() {
                    super.onChanged();
                    update();
                }

                private void update() {
                    mProgress.setVisibility(View.GONE);
                    mMoreProgress.setVisibility(View.GONE);
                    isLoadingMore = false;
                    mPtrLayout.setRefreshing(false);
                    if (mRecycler.getAdapter().getItemCount() == 0 && mEmptyId != 0) {

                        mEmpty.setVisibility(View.VISIBLE);
                    } else if (mEmptyId != 0) {

                        mEmpty.setVisibility(View.GONE);
                    }
                }
            });

        if (mEmptyId != 0) {

            boolean isEmptyInvisible = null != adapter && adapter.getItemCount() > 0;
            mEmpty.setVisibility(isEmptyInvisible ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Set the layout manager to the recycler
     */
    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        mRecycler.setLayoutManager(manager);
    }

    /**
     * Set the adapter to the recycler
     * Automatically hide the progressbar
     * Set the refresh to false
     * If adapter is empty, then the emptyview is shown
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        setAdapterInternal(adapter, false, true);
    }

    /**
     * @param adapter                       The new adapter to , or null to set no adapter.
     * @param removeAndRecycleExistingViews If set to true, RecyclerView will recycle all existing Views. If adapters
     *                                      have stable ids and/or you want to animate the disappearing views, you may
     *                                      prefer to set this to false.
     */
    public void swapAdapter(RecyclerView.Adapter adapter, boolean removeAndRecycleExistingViews) {
        setAdapterInternal(adapter, true, removeAndRecycleExistingViews);
    }

    public void setupSwipeToDismiss(final SwipeDismissRVTouchListener.DismissCallbacks listener) {
        SwipeDismissRVTouchListener touchListener =
                new SwipeDismissRVTouchListener(mRecycler, new SwipeDismissRVTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return listener.canDismiss(position);
                    }

                    @Override
                    public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {
                        listener.onDismiss(recyclerView, reverseSortedPositions);
                    }
                });
        mSwipeDismissScrollListener = touchListener.makeScrollListener();
        mRecycler.setOnTouchListener(touchListener);
    }

    /**
     * Remove the adapter from the recycler
     */
    public void clear() {
        mRecycler.setAdapter(null);
    }

    /**
     * Show the progressbar
     */
    public void showProgress() {
        hideRecycler();
        if (mEmptyId != 0) mEmpty.setVisibility(View.INVISIBLE);
        mProgress.setVisibility(View.VISIBLE);
    }

    /**
     * Hide the progressbar and show the recycler
     */
    public void showRecycler() {
        hideProgress();
        if (mRecycler.getAdapter().getItemCount() == 0 && mEmptyId != 0) {
            mEmpty.setVisibility(View.VISIBLE);
        } else if (mEmptyId != 0) {
            mEmpty.setVisibility(View.GONE);
        }
        mRecycler.setVisibility(View.VISIBLE);
    }

    public void showMoreProgress() {
        mMoreProgress.setVisibility(View.VISIBLE);
    }

    public void hideMoreProgress() {
        mMoreProgress.setVisibility(View.GONE);
    }

    /**
     * Hide the progressbar
     */
    public void hideProgress() {
        mProgress.setVisibility(View.GONE);
    }

    /**
     * Hide the recycler
     */
    public void hideRecycler() {
        mRecycler.setVisibility(View.GONE);
    }

    /**
     * Set the scroll listener for the recycler
     */
    public void setOnScrollListener(RecyclerView.OnScrollListener listener) {
        mExternalOnScrollListener = listener;
    }

    /**
     * Add the onItemTouchListener for the recycler
     */
    public void addOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        mRecycler.addOnItemTouchListener(listener);
    }

    /**
     * Remove the onItemTouchListener for the recycler
     */
    public void removeOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        mRecycler.removeOnItemTouchListener(listener);
    }

    /**
     * @return the recycler adapter
     */
    public RecyclerView.Adapter getAdapter() {
        return mRecycler.getAdapter();
    }

    /**
     * Sets the More listener
     *
     * @param max Number of items before loading more
     */
    public void setupMoreListener(OnMoreListener onMoreListener, int max) {
        mOnMoreListener = onMoreListener;
        ITEM_LEFT_TO_LOAD_MORE = max;
    }

    /**
     * Set the listener to be notified when a refresh is triggered via the swipe gesture.
     */
    public void setRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    public void setOnMoreListener(OnMoreListener onMoreListener) {
        mOnMoreListener = onMoreListener;
    }

    public void setNumberBeforeMoreIsCalled(int max) {
        ITEM_LEFT_TO_LOAD_MORE = max;
    }

    public boolean isLoadingMore() {
        return isLoadingMore;
    }

    /**
     * Enable/Disable the More event
     */
    public void setLoadingMore(boolean isLoadingMore) {
        this.isLoadingMore = isLoadingMore;
    }

    /**
     * Remove the moreListener
     */
    public void removeMoreListener() {
        mOnMoreListener = null;
    }

    public void setOnTouchListener(OnTouchListener listener) {
        mRecycler.setOnTouchListener(listener);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecycler.addItemDecoration(itemDecoration);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int index) {
        mRecycler.addItemDecoration(itemDecoration, index);
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecycler.removeItemDecoration(itemDecoration);
    }

    /**
     * @return inflated progress view or null
     */
    public View getProgressView() {
        return mProgressView;
    }

    /**
     * @return inflated more progress view or null
     */
    public View getMoreProgressView() {
        return mMoreProgressView;
    }

    /**
     * @return inflated empty view or null
     */
    public View getEmptyView() {
        return mEmptyView;
    }

    /**
     * Animate a scroll by the given amount of pixels along either axis.
     *
     * @param dx Pixels to scroll horizontally
     * @param dy Pixels to scroll vertically
     */
    public void smoothScrollBy(int dx, int dy) {
        mRecycler.smoothScrollBy(dx, dy);
    }

    public enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

    // ------ EnhancedSwipeRefreshLayout - methods START -------

    /**
     * Set whether can refresh
     *
     * @param refreshing false: complete and cancel refresh; true: can refresh
     */
    public void setRefreshing(boolean refreshing) {
        /**
         * STEP FIVE: refresh complete
         */
        mPtrLayout.setRefreshing(refreshing);
        // hide the progress bar
        if (null != mProgressBar) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Set the listener when refresh is triggered and enable the SwipeRefreshLayout
     */
    public void setRefreshListener(SwipeRefreshLayout.OnPullRefreshListener listener) {
        mPtrLayout.setEnabled(true);
        mPtrLayout.setOnPullRefreshListener(listener);
    }

    /**
     * Set the header view by custom
     */
    public void setHeaderView(View child) {
        mPtrLayout.setHeaderView(child);
    }

    /**
     * 设置是否禁用下拉刷新功能
     *
     * @param forbidPull true: can pull down; false: cannot pull down
     */
    public void setForbidPull(boolean forbidPull) {
        mPtrLayout.setForbidPull(forbidPull);
    }
    // ------ EnhancedSwipeRefreshLayout - methods END -------

    // ------ EnhancedSwipeRefreshLayout.OnPullRefreshListener - onRefresh -------
    @Override
    public void onRefresh() {
        /**
         * STEP FOUR: onRefresh - refreshing
         */
        tvTips.setText(R.string.pull_down_refreshing);
        ivArrow.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        mOnRefreshListener.onRefresh();
    }

    @Override
    public void onPullDistance(int distance) {
        // pull distance
    }

    @Override
    public void onPullEnable(boolean enable) {
        /**
         * STEP THREE: change the view by onTouchEvent
         */

        final int DEGREE_DEFAULT = 0;
        final int DEGREE_REVERSE = 180;

        // default: pull_down_refresh; pull down: pull_down_release
        tvTips.setText(enable ? R.string.pull_down_release : R.string.pull_down_refresh);
        ivArrow.setVisibility(View.VISIBLE);
        ivArrow.setRotation(enable ? DEGREE_REVERSE : DEGREE_DEFAULT);
    }
}

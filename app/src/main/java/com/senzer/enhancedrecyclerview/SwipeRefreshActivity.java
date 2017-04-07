package com.senzer.enhancedrecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.senzer.enhancedrecyclerview.adapter.RecycleDemoAdapter;
import com.senzer.enhancedrecyclerview.entity.User;
import com.senzer.enhancedrecyclerview.view.SlidingRecyclerView;
import com.senzer.enhancedrecyclerview.view.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class SwipeRefreshActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnPullRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private SlidingRecyclerView rvTop;
    private SlidingRecyclerView rvBottom;

    // ------------- header view --------------
    private ProgressBar mProgressBar;
    private TextView tvTips;
    private ImageView ivArrow;

    private boolean isRefreshing;                   // whether is showing the progressDlg

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_refresh);

        initView();
        initRefreshView();
        initData();
    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        rvTop = (SlidingRecyclerView) findViewById(R.id.rv_top);
        rvBottom = (SlidingRecyclerView) findViewById(R.id.rv_bottom);

        rvTop.setCanSliding(false);
        rvBottom.setCanSliding(false);

        GridLayoutManager manager = new GridLayoutManager(this, 5);
        rvTop.setLayoutManager(manager);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayout.VERTICAL);
        rvBottom.setLayoutManager(llm);
    }

    private void initRefreshView() {
        /**
         * STEP ONE: set OnPullRefreshListener
         */
        swipeRefreshLayout.setOnPullRefreshListener(this);
        /**
         * STEP TWO: set headerView
         */
        swipeRefreshLayout.setHeaderView(createHeaderView());
    }

    private View createHeaderView() {
        View headerView = LayoutInflater.from(this).inflate(R.layout.layout_header, null);
        mProgressBar = (ProgressBar) headerView.findViewById(R.id.pb_loading);
        tvTips = (TextView) headerView.findViewById(R.id.tv_tips);
        ivArrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
        return headerView;
    }

    private void initData() {

        List<User> users = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setName("name: " + i);
            users.add(user);
        }
        RecycleDemoAdapter adapter = new RecycleDemoAdapter(this, users);
        rvTop.setAdapter(adapter);
        rvBottom.setAdapter(adapter);
    }

    // ---------------- pullDown and PullUp Start ----------------
    protected RecyclerView.LayoutManager getLayoutManager() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayout.VERTICAL);
        return llm;
    }

    // ------ SpiderSwipeRefreshLayout.OnPullRefreshListener - onRefresh -------
    @Override
    public void onRefresh() {
        /**
         * STEP FOUR: onRefresh - refreshing
         */

        tvTips.setText(R.string.pull_down_refreshing);
        ivArrow.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        isRefreshing = true;

        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isRefreshing) {
                    isRefreshing = false;
                    // stop refreshing
                    swipeRefreshLayout.setRefreshing(false);
                }

                // hide the progress bar
                if (null != mProgressBar) {
                    mProgressBar.setVisibility(View.GONE);
                }

                Toast.makeText(SwipeRefreshActivity.this, "刷新成功！", Toast.LENGTH_LONG);
            }
        }, 2000);
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

        tvTips.setText(enable ? R.string.pull_down_release : R.string.pull_down_refresh);
        ivArrow.setVisibility(View.VISIBLE);
        ivArrow.setRotation(enable ? DEGREE_REVERSE : DEGREE_DEFAULT);
    }
    // ---------------- pullDown and PullUp End ----------------
}

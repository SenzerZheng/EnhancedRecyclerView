package com.senzer.enhancedrecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.senzer.enhancedrecyclerview.adapter.RecycleDemoAdapter;
import com.senzer.enhancedrecyclerview.entity.User;
import com.senzer.enhancedrecyclerview.listener.OnMoreListener;
import com.senzer.enhancedrecyclerview.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class RecycleDemoActivity extends AppCompatActivity implements
        OnRefreshListener,
        OnMoreListener {

    private EnhancedRecyclerView enhancedRecyclerView;
    private RecycleDemoAdapter adapter;
    private List<String> mDatasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_demo);

        enhancedRecyclerView = (EnhancedRecyclerView) findViewById(R.id.enhanced_recycler);
        enhancedRecyclerView.setLayoutManager(getLayoutManager());
        enhancedRecyclerView.setRefreshListener(this);
        enhancedRecyclerView.setupMoreListener(this, 1);

        mDatasource = new ArrayList<>();
        adapter = new RecycleDemoAdapter(this, mDatasource);
        enhancedRecyclerView.setAdapter(adapter);
    }

    // ---------------- pullDown and PullUp Start ----------------
    protected RecyclerView.LayoutManager getLayoutManager() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayout.VERTICAL);
        return llm;
    }

    @Override
    public void onRefresh() {
        enhancedRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<User> users = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    User user = new User();
                    user.setName("name: " + i);
                    users.add(user);
                }
                adapter.setDataSource(users, false);

                Toast.makeText(RecycleDemoActivity.this, "刷新成功！", Toast.LENGTH_LONG);
                refreshComplete();
            }
        }, 2000);
    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
        enhancedRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<User> users = new ArrayList<>();
                for (int i = 20; i < 40; i++) {
                    User user = new User();
                    user.setName("name: " + i);
                    users.add(user);
                }
                adapter.setDataSource(users, true);

                Toast.makeText(RecycleDemoActivity.this, "加载更多成功！", Toast.LENGTH_LONG);
                refreshComplete();
            }
        }, 2000);
    }

    /**
     * stop refreshing
     */
    private void refreshComplete() {
        enhancedRecyclerView.setRefreshing(false);
    }
    // ---------------- pullDown and PullUp End ----------------
}

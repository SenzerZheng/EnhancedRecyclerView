package com.senzer.enhancedrecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_recycle_demo:
                startActivity(new Intent(this, RecycleDemoActivity.class));
                break;

            case R.id.tv_swipe_refresh:
                startActivity(new Intent(this, SwipeRefreshActivity.class));
                break;

            default:
                break;
        }
    }
}

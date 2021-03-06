package com.hsy.refresh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import com.hsy.refresh.ui.GoogleRefreshActivity;
import com.hsy.refresh.ui.MySwipeRefreshActivity;
import com.hsy.refresh.ui.RecycleViewRefreshActivity;
import com.hsy.refresh.ui.ViewPagerRefreshActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.button3)
    Button button3;
    @BindView(R.id.button4)
    Button button4;
    @BindView(R.id.activity_main)
    ScrollView activityMain;

    /*主界面*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                /*Google原生的SwipeRefreshLayout---滚动监听上拉刷新*/
                startActivity(new Intent(MainActivity.this, GoogleRefreshActivity.class));
                break;
            case R.id.button2:
                /**
                 * 支持自定义上拉刷新界面
                 */
                startActivity(new Intent(MainActivity.this, MySwipeRefreshActivity.class));
                break;
            case R.id.button3:
                /**
                 * 支持自定义Viewpage下拉刷新界面
                 */
                startActivity(new Intent(MainActivity.this, ViewPagerRefreshActivity.class));
                break;
            case R.id.button4:
                /**
                 * RecyclerView+SwpieRefreshLayout实现下拉刷新
                 */
                startActivity(new Intent(MainActivity.this, RecycleViewRefreshActivity.class));
//                Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
                break;
        }
    }


}

package com.hsy.refresh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hsy.refresh.ui.MyRefreshActivity;
import com.hsy.refresh.ui.GoogleRefreshActivity;
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

    /*主界面*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3})
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
                startActivity(new Intent(MainActivity.this, MyRefreshActivity.class));
                break;
            case R.id.button3:
                /**
                 * 支持自定义Viewpage下拉刷新界面
                 */
                startActivity(new Intent(MainActivity.this, ViewPagerRefreshActivity.class));
                break;
        }
    }
}

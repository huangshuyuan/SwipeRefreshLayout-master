# SwipeRefresh 
基于原生的SwipeRefreshLayout 做了封装处理

###1.原生SwipeRefreshLayout

除了OnRefreshListener接口外，SwipRefreshLayout中还有一些其他重要的方法，具体如下：

```

         1、setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener):设置手势滑动监听器。
         
         2、setProgressBackgroundColor(int colorRes):设置进度圈的背景色。
         
         3、setColorSchemeResources(int… colorResIds):设置进度动画的颜色。
         
         4、setRefreshing(Boolean refreshing):设置组件的刷洗状态。
         
         5、setSize(int size):设置进度圈的大小，只有两个值：DEFAULT、LARGE
         
 ```
 
 布局，具体内容如下：
 ```
<?xml version="1.0" encoding="utf-8"?>
<android.support.v4.widget.SwipeRefreshLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:id="@+id/swipeLayout" >
     
    <ListView 
        android:id="@+id/mylist"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
    
</android.support.v4.widget.SwipeRefreshLayout>
```
Activity核心代码如下：
```
swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeLayout);
  /*加载的渐变动画*/
        swipeRefreshLayout.setColorSchemeResources(R.color.swipe_color_1,
                R.color.swipe_color_2,
                R.color.swipe_color_3,
                R.color.swipe_color_4);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);;
        swipeRefreshLayout.setProgressBackgroundColor(R.color.swipe_background_color);
        //swipeRefreshLayout.setPadding(20, 20, 20, 20);
        //swipeRefreshLayout.setProgressViewOffset(true, 100, 200);
        //swipeRefreshLayout.setDistanceToTriggerSync(50);
        swipeRefreshLayout.setProgressViewEndTarget(true, 100);
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        data.clear();
                        for(int i=0;i<20;i++){
                            data.add("SwipeRefreshLayout下拉刷新"+i);
                        }
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });
    //handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case 1:
                
                swipeRefreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
                //swipeRefreshLayout.setEnabled(false);
                break;
            default:
                break;
            }
        }
    };

```

###2.自定义支持上拉刷新的组件



###3.自定义支持ViewPage的刷新组件VPSwipeRefreshLayout


* 原生SwipeRefreshLayout会存在以下问题：

1、 SwipeRefreshLayout会吃掉ViewPager的滑动事件。 

2、 SwipeRefreshLayout需要套在ScrollView和ListView上的时候才表现的比较友好，在其他ViewGroup上有点问题



* 重写后的SwipeRefreshLayout，直接复制到项目就可以使用了。
```
public class VpSwipeRefreshLayout extends SwipeRefreshLayout {

private float startY;
private float startX;
// 记录viewPager是否拖拽的标记
private boolean mIsVpDragger;
private final int mTouchSlop;

public VpSwipeRefreshLayout(Context context, AttributeSet attrs) {
super(context, attrs);
mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
}

@Override
public boolean onInterceptTouchEvent(MotionEvent ev) {
int action = ev.getAction();
switch (action) {
case MotionEvent.ACTION_DOWN:
// 记录手指按下的位置
startY = ev.getY();
startX = ev.getX();
// 初始化标记
mIsVpDragger = false;
break;
case MotionEvent.ACTION_MOVE:
// 如果viewpager正在拖拽中，那么不拦截它的事件，直接return false；
if(mIsVpDragger) {
return false;
}

// 获取当前手指位置
float endY = ev.getY();
float endX = ev.getX();
float distanceX = Math.abs(endX - startX);
float distanceY = Math.abs(endY - startY);
// 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
if(distanceX > mTouchSlop && distanceX > distanceY) {
mIsVpDragger = true;
return false;
}
break;
case MotionEvent.ACTION_UP:
case MotionEvent.ACTION_CANCEL:
// 初始化标记
mIsVpDragger = false;
break;
}
// 如果是Y轴位移大于X轴，事件交给swipeRefreshLayout处理。
return super.onInterceptTouchEvent(ev);
}
}

```

***

上面是自己研究的，最近发现一个大神已经封装好了使用，可以参照大神的

大神的源码地址：https://github.com/yanzhenjie/SwipeRecyclerView

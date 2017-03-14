# SwipeRefresh 
基于原生的SwipeRefreshLayout 做了封装处理

##此项目中包括三种：
* 1.原生SwipeRefreshLayout（上拉可通过滚动监听实现）

* 2.自定义支持上拉刷新的组件

* 3.自定义支持ViewPage的刷新组件VPSwipeRefreshLayout

***
###1.原生SwipeRefreshLayout（上拉可通过滚动监听实现）
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
* 原生实现上拉效果

通过监听滚动事件，对listview添加底部的组件实现上拉
```
 implements AbsListView.OnScrollListener {···
 
 
 ···
  /**
         * 对listview添加底部的组件实现上拉
         */
        footerView = getLayoutInflater().inflate(R.layout.refresh_footview_layout, null);
        lv.addFooterView(footerView);
  /**
         * 设置滚动监听
         */
   lv.setOnScrollListener(this);
   
    /**
     * 重写滚动方法
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (adapter.getCount() == visibleLastIndex && scrollState == SCROLL_STATE_IDLE) {
            Toast.makeText(this, "加载更多完成", Toast.LENGTH_SHORT).show();
            footerView.setVisibility(View.GONE);
            /*在此处加载更多数据*/
//            new LoadDataThread().start();
        }else {
            footerView.setVisibility(View.VISIBLE);
//            Toast.makeText(this, "加载更多...", Toast.LENGTH_SHORT).show();
        }

    }
   
```
***
###2.自定义支持上拉刷新的组件

实现下拉和上拉监听
```
···AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        RefreshLayout.OnLoadListener {
        ···
```
下拉和原先一样用法：
```
         //下拉监听
        swipeLayout.setOnRefreshListener(this);
        //上拉监听
        swipeLayout.setOnLoadListener(this);
        
         /**
     * 上拉刷新
     */
    @Override
    public void onRefresh() {
        swipeLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 更新数据  更新完后调用该方法结束刷新
                list.clear();
                for (int i = 0; i < 8; i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("itemImage", i + "刷新");
                    map.put("itemText", i + "刷新");
                    list.add(map);
                }
                adapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
            }
        }, 2000);

    }

    /**
     * 加载更多
     */
    @Override
    public void onLoad() {
        swipeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 更新数据  更新完后调用该方法结束刷新
                swipeLayout.setLoading(false);
                for (int i = 1; i < 10; i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("itemImage", i + "更多");
                    map.put("itemText", i + "更多");
                    list.add(map);
                }
                adapter.notifyDataSetChanged();
            }
        }, 2000);
    }

        
```
   
* 自定义组件如下：
```
/**
 * Created by huangshuyuan on 2017/3/9.
 * email:hshuuyuan@foxmail.com
 * version:v1.0
 * <p>
 * 自定义view
 */

/**
 * 继承自SwipeRefreshLayout,从而实现滑动到底部时上拉加载更多的功能.
 */
public class RefreshLayout extends SwipeRefreshLayout implements
        OnScrollListener {

    /**
     * 滑动到最下面时的上拉操作
     */

    private int mTouchSlop;
    /**
     * listview实例
     */
    private ListView mListView;

    /**
     * 上拉监听器, 到了最底部的上拉加载操作
     */
    private OnLoadListener mOnLoadListener;

    /**
     * ListView的加载中footer
     */
    private View mListViewFooter;

    /**
     * 按下时的y坐标
     */
    private int mYDown;
    /**
     * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
     */
    private int mLastY;
    /**
     * 是否在加载中 ( 上拉加载更多 )
     */
    private boolean isLoading = false;

    /**
     * @param context
     */
    public RefreshLayout(Context context) {
        this(context, null);
    }

    @SuppressLint("InflateParams")
    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mListViewFooter = LayoutInflater.from(context).inflate(
                R.layout.listview_footer, null, false);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 初始化ListView对象
        if (mListView == null) {
            getListView();
        }
    }

    /**
     * 获取ListView对象
     */
    private void getListView() {
        int childs = getChildCount();
        if (childs > 0) {
            View childView = getChildAt(0);
            if (childView instanceof ListView) {
                mListView = (ListView) childView;
                // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
                mListView.setOnScrollListener(this);
                Log.d(VIEW_LOG_TAG, "### 找到listview");
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mYDown = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                // 移动
                mLastY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                // 抬起
                if (canLoad()) {
                    loadData();
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    /**
     * 是否可以加载更多, 条件是到了最底部, listview不在加载中, 且为上拉操作.
     *
     * @return
     */
    private boolean canLoad() {
        return isBottom() && !isLoading && isPullUp();
    }

    /**
     * 判断是否到了最底部
     */
    private boolean isBottom() {

        if (mListView != null && mListView.getAdapter() != null) {
            return mListView.getLastVisiblePosition() == (mListView
                    .getAdapter().getCount() - 1);
        }
        return false;
    }

    /**
     * 是否是上拉操作
     *
     * @return
     */
    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }

    /**
     * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
     */
    private void loadData() {
        if (mOnLoadListener != null) {
            // 设置状态
            setLoading(true);
            //
            mOnLoadListener.onLoad();
        }
    }

    /**
     * @param loading
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
        if (isLoading) {
            mListView.addFooterView(mListViewFooter);
        } else {
            mListView.removeFooterView(mListViewFooter);
            mYDown = 0;
            mLastY = 0;
        }
    }

    /**
     * @param loadListener
     */
    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // 滚动时到了最底部也可以加载更多
        if (canLoad()) {
            loadData();
        }
    }

    /**
     * 设置刷新
     */
    public static void setRefreshing(SwipeRefreshLayout refreshLayout,
                                     boolean refreshing, boolean notify) {
        Class<? extends SwipeRefreshLayout> refreshLayoutClass = refreshLayout
                .getClass();
        if (refreshLayoutClass != null) {

            try {
                Method setRefreshing = refreshLayoutClass.getDeclaredMethod(
                        "setRefreshing", boolean.class, boolean.class);
                setRefreshing.setAccessible(true);
                setRefreshing.invoke(refreshLayout, refreshing, notify);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载更多的监听器
     */
    public static interface OnLoadListener {
        public void onLoad();
    }
}
```
***
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

package com.liang.tind.dateviewdemo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.liang.tind.dateviewdemo.adapter.Adapter;
import com.liang.tind.dateviewdemo.model.TestBean;
import com.liang.tind.dateviewdemo.view.DateView;
import com.liang.tind.dateviewdemo.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import static android.animation.ObjectAnimator.ofFloat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private com.liang.tind.dateviewdemo.view.DateView dateview;
    private android.widget.TextView mTvMonth;
    private android.support.v7.widget.RecyclerView rv;
    /**
     * 动画持续时间，经我测试，=50时表现效果较好
     */
    public static final int ANIMATION_DURATION = 50;
    /**
     * 测试数据集合
     */
    private List<TestBean> mList;
    /**
     * 向上弹出动画
     */
    private ObjectAnimator mUpAnimOut;
    /**
     * 向上弹进动画
     */
    private ObjectAnimator mUpAnimIn;
    /**
     * 向下弹出动画
     */
    private ObjectAnimator mDownAnimOut;
    /**
     * recyclerview item的高度
     */
    private float mRvItemHeight;
    /**
     * recyclerview 滑动的状态，主要关注静止状态SCROLL_STATE_IDLE
     */
    private int scrollState = 1;
    /**
     * recyclerview 可见的第一条数据的month值
     */
    private int mCurrentMonth = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.rv = (RecyclerView) findViewById(R.id.rv);
        this.mTvMonth = (TextView) findViewById(R.id.tv_date);
        this.dateview = (DateView) findViewById(R.id.date_view);

        initData();
    }

    private void initData() {
        //添加测试数据
        mList = new ArrayList<>();
        for (int i = 100; i < 120; i++) {
            mList.add(new TestBean(i));
        }

        //post 一个runnable 待 view layout 完毕后测量 rcyclerview item的高度
        rv.post(new Runnable() {
            @Override
            public void run() {
                View childAt = rv.getLayoutManager().findViewByPosition(0);
                if (childAt != null) {
                    mRvItemHeight = (float) childAt.getHeight();
                    initAnimation();
                }
            }
        });

        rv.setLayoutManager(new LinearLayoutManager(this));
        //添加item间的分割线
        rv.addItemDecoration(new DividerItemDecoration(this));

        RecyclerView.Adapter adapter = new Adapter(this, mList);
        rv.setAdapter(adapter);
        //为了和dateview 完成联动，添加滑动监听
        rv.addOnScrollListener(new MyScrollListener());
    }

    private void initAnimation() {
        // Y轴方向上的坐标
        float translationY = mTvMonth.getTranslationY();
        float tvMonthHeight = mTvMonth.getHeight();
        //向上弹出动画
        //第一个参数是要执行动画的控件，第二个参数是更改的属性字段（需带有setter方法），
        //第三个参数是 动画开始时 要更改的属性字段的起始值，第四个是结束时的值（translationY - tvMonthHeight 相当于滑出边界不可见了。）
        //这里指mTvMonth执行Y轴上的坐标 更改（Y轴位移动画)
        mUpAnimOut = ObjectAnimator.
                ofFloat(mTvMonth, "translationY", translationY, translationY - tvMonthHeight);
        //向上弹进动画
        mUpAnimIn =
                ofFloat(mTvMonth, "translationY", translationY + tvMonthHeight, translationY);
        mUpAnimOut.setDuration(ANIMATION_DURATION);
        mUpAnimIn.setDuration(ANIMATION_DURATION);
        //添加动画执行监听
        addUpAnimListener(mUpAnimIn);

        //向下弹出动画
        mDownAnimOut =
                ofFloat(mTvMonth, "translationY", translationY, translationY + tvMonthHeight);
        //向下弹进动画
        ObjectAnimator downAnimIn =
                ofFloat(mTvMonth, "translationY", translationY - tvMonthHeight, translationY);

        mDownAnimOut.setDuration(ANIMATION_DURATION);
        downAnimIn.setDuration(ANIMATION_DURATION);
        //添加动画执行监听
        addDownAnimListener(downAnimIn);


    }

    private void addDownAnimListener(final ObjectAnimator downAnimIn) {
        downAnimIn.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mTvMonth.setText(String.valueOf(mCurrentMonth));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //当recycler滑动速度非常快的时候，当前的动画还未执行，已经滑动到下条数据要执行下一个动画时，
                //因为我们判断了!upAnimIn.isStarted() ,所以下个动画不会执行，这时候就需要以下判断当RecyclerView
                //滑动停止，当前动画结束时将正确的（下一条的数据）设置给mTvMonth，避免数据错乱.
                if (scrollState == RecyclerView.SCROLL_STATE_IDLE && mCurrentMonth != Integer.parseInt(mTvMonth.getText().toString())) {
                    mTvMonth.setText(String.valueOf(mCurrentMonth));
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        mDownAnimOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!downAnimIn.isStarted()) {
                    downAnimIn.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void addUpAnimListener(final ObjectAnimator upAnimIn) {
        mUpAnimOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!upAnimIn.isStarted()) {
                    upAnimIn.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        upAnimIn.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mTvMonth.setText(String.valueOf(mCurrentMonth));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //当recycler滑动速度非常快的时候，当前的动画还未执行，已经滑动到下条数据要执行下一个动画时，
                //因为我们判断了!upAnimIn.isStarted() ,所以下个动画不会执行，这时候就需要以下判断当RecyclerView
                //滑动停止，当前动画结束时将正确的（下一条的数据）设置给mTvMonth，避免数据错乱.
                if (scrollState == RecyclerView.SCROLL_STATE_IDLE && mCurrentMonth != Integer.parseInt(mTvMonth.getText().toString())) {
                    mTvMonth.setText(String.valueOf(mCurrentMonth));
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 开始向上滑出的动画
     */
    private void startUpAnim(  ) {
        if (!mUpAnimOut.isStarted()) {
            mUpAnimOut.start();
        }
    }

    /**
     * 开始向下滑出的动画
     */
    private void startDownAnim() {
        if (!mDownAnimOut.isStarted()) {
            mDownAnimOut.start();
        }
    }

    /**
     * RecyclerView 为了和dateView 完成交互 需重写 RecyclerView .OnScrollListener
     */
    private class MyScrollListener extends RecyclerView.OnScrollListener {
        private TestBean mBean;
        float y = 0;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (mBean != null) {
                scrollState = newState;
                //当非常快速滑动的时候 在滑动的最后判断数据是否准确，将正确的数据返回。
                if (scrollState == RecyclerView.SCROLL_STATE_IDLE && mCurrentMonth != Integer.parseInt(mTvMonth.getText().toString())) {
                    mCurrentMonth = mBean.getMonth();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if ( mRvItemHeight != 0 ) {
                y += dy;
                //将累计的滑动距离 跟一个item的高度 比较，判断滑动了相当于几个item的距离。
                float position = y / mRvItemHeight;
                dateview.setProcess(position);
                mBean = mList.get((int) position);
                if (mBean.getMonth()!= Integer.parseInt(mTvMonth.getText().toString())) {
                    mCurrentMonth = mBean.getMonth();
                    if (dy > 0) {
                        startUpAnim( );
                    } else {
                        startDownAnim();
                    }

                }
            }
        }
    }


}

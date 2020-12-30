package com.timecat.middle.block.im;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.timecat.middle.block.R;
import com.vmloft.develop.library.tools.utils.VMDimen;
import com.vmloft.develop.library.tools.utils.VMTheme;

import java.util.List;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-04
 * @description null
 * @usage null
 * IM Activity 基类
 */
public abstract class IMBaseActivity extends VMBActivity {

    // 统一的 TopBar
    protected Toolbar mTopBar;
    protected View mTopSpaceView;

    @Override
    protected void initUI() {
        // 设置深色状态栏
        VMTheme.setDarkStatusBar(mActivity, true);

        setupTopBar();
    }

    /**
     * 装载 TopBar
     */
    protected void setupTopBar() {

        if (mTopSpaceView != null) {
            // 设置状态栏透明主题时，布局整体会上移，所以给头部 View 设置 StatusBar 的高度
            mTopSpaceView.getLayoutParams().height = VMDimen.getStatusBarHeight();
        }
        if (mTopBar != null) {
            mTopBar.setNavigationIcon(R.drawable.ic_arrow_back);
            mTopBar.setNavigationOnClickListener((View v) -> {onBackPressed();});
        }
    }

    /**
     * 通用的获取 TopBar 方法
     */
    protected Toolbar getTopBar() {
        return mTopBar;
    }

    /**
     * 设置标题
     */
    protected void setTopTitle(int resId) {
        if (mTopBar == null) {
            return;
        }
        mTopBar.setTitle(resId);
    }

    /**
     * 设置标题
     */
    protected void setTopTitle(String title) {
        if (mTopBar == null) {
            return;
        }
        mTopBar.setTitle(title);
    }

    /**
     * 解决Fragment中的onActivityResult()方法无响应问题。
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 1.使用 getSupportFragmentManager().getFragments() 获取到当前 Activity 中添加的 Fragment 集合
         * 2.遍历 Fragment 集合，手动调用在当前 Activity 中的 Fragment 中的 onActivityResult() 方法。
         */
        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment mFragment : fragments) {
                mFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}


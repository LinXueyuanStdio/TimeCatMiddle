package com.timecat.middle.block.page;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.timecat.page.base.friend.toolbar.BaseToolbarActivity;
import com.timecat.middle.block.R;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description CollapsingToolbarLayout
 * @usage null
 */
public abstract class BaseCollapseToolbarActivity extends BaseToolbarActivity {
    protected CollapsingToolbarLayout collapseContainer;
    protected AppBarLayout appBarLayout;
    protected CollapsingToolbarLayoutState state;

    protected enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }
    @Override
    protected boolean canBack() {
        return true;
    }

    @Override
    protected int layout() {
        return R.layout.base_detail_collapse;
    }

    @Override
    protected void initView() {
        collapseContainer = findViewById(R.id.collapseContainer);
        appBarLayout = findViewById(R.id.appBarLayout);
    }
}

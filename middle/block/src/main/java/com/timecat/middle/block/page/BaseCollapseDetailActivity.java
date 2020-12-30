package com.timecat.middle.block.page;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.timecat.middle.block.R;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description CollapsingToolbarLayout + ViewPager + TabLayout
 * @usage null
 */
public abstract class BaseCollapseDetailActivity extends BaseCollapseToolbarActivity {
    public ViewPager viewPager;
    public TabLayout tabs;

    @Override
    protected int layout() {
        return R.layout.base_detail_collapse_viewpager;
    }

    @Override
    protected void initView() {
        super.initView();
        viewPager = findViewById(R.id.viewpager);
        tabs = findViewById(R.id.tabs);
    }
}

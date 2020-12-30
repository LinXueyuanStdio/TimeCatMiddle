package com.timecat.middle.block.im;

import android.os.Bundle;

import com.vmloft.develop.library.tools.base.VMActivity;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-04
 * @description null
 * @usage null
 */
public abstract class VMBActivity extends VMActivity {
    public VMBActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(this.layoutId());
        this.initUI();
        this.initData();
    }

    protected abstract int layoutId();

    protected abstract void initUI();

    protected abstract void initData();
}

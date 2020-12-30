package com.timecat.middle.block.view;

import androidx.annotation.IntDef;

import com.timecat.middle.block.temp.Def;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/1/25
 * @description null
 * @usage null
 */
@IntDef({Def.STYLE_WHITE, Def.STYLE_BLACK})
@Retention(RetentionPolicy.SOURCE)
public @interface Style {

}
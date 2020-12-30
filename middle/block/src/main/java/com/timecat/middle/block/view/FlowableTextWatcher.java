package com.timecat.middle.block.view;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

import io.reactivex.FlowableEmitter;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/3/28
 * @description null
 * @usage null
 */
public class FlowableTextWatcher implements TextWatcher {
    @NonNull
    FlowableEmitter<String> emitter;

    public FlowableTextWatcher(@NonNull FlowableEmitter<String> emitter) {
        this.emitter = emitter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        emitter.onNext(s.toString());
    }
}

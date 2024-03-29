package com.timecat.middle.block.view;

import android.text.Editable;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import io.reactivex.FlowableEmitter;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/3/28
 * @description null
 * @usage null
 */
public class IndentTextWatcher extends FlowableTextWatcher {
    public IndentTextWatcher(@NonNull FlowableEmitter<String> emitter) {
        super(emitter);
    }

    int markStart = -1;
    int changeCount = -1;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        super.beforeTextChanged(s, start, count, after);
        this.markStart = -1;
        this.changeCount = -1;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        super.onTextChanged(s, start, before, count);
        this.markStart = start;
        this.changeCount = count;
    }

    @Override
    public void afterTextChanged(Editable s) {

        int start = this.markStart;
        int count = this.changeCount;

        if (count == 1 && s.charAt(start) == '\n') {
            indent(s, start);
        }

        this.markStart = -1;
        this.changeCount = -1;
        super.afterTextChanged(s);
    }

    private static void indent(Editable s, int start) {
        CharSequence value = getIndent(s, start);
        if (!TextUtils.isEmpty(value)) {
            s.insert(start + 1, value);
        }
    }

    static CharSequence getIndent(Editable s, int start) {

        int begin = lastIndexOf(s, '\n', start - 1);
        begin = (begin < 0) ? 0 : (begin + 1);
        int end = begin;
        for (int i = begin, length = start + 1; i < length; i++) {
            char c = s.charAt(i);
            if (!isWhitespace(c)) {
                end = i;
                break;
            }
        }

        if (end > begin) {
            return s.subSequence(begin, end).toString();
        }

        return null;
    }


    private static boolean isWhitespace(char c) {
        return (c == ' ')
                || (c == '\t')
                || (c == '\u3000');
    }

    private static int lastIndexOf(Editable s, char ch, int fromIndex) {
        if (ch < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
            // handle most cases here (ch is a BMP code point or a
            // negative value (invalid code point))
            int i = Math.min(fromIndex, s.length() - 1);
            for (; i >= 0; i--) {
                if (s.charAt(i) == ch) {
                    return i;
                }
            }
            return -1;
        } else {
            return lastIndexOfSupplementary(s, ch, fromIndex);
        }
    }

    private static int lastIndexOfSupplementary(Editable s, int ch, int fromIndex) {
        if (Character.isValidCodePoint(ch)) {
            char hi = Character.highSurrogate(ch);
            char lo = Character.lowSurrogate(ch);
            int i = Math.min(fromIndex, s.length() - 2);
            for (; i >= 0; i--) {
                if (s.charAt(i) == hi && s.charAt(i + 1) == lo) {
                    return i;
                }
            }
        }
        return -1;
    }
}

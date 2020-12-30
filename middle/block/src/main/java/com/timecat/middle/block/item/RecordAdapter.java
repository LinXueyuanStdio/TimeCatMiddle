package com.timecat.middle.block.item;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/15
 * @description null
 * @usage null
 */
public class RecordAdapter extends FlexibleAdapter<BaseRecordItem<?>> {

    public RecordAdapter(@Nullable List<BaseRecordItem<?>> items) {
        super(items);
    }

    public RecordAdapter(@Nullable List<BaseRecordItem<?>> items, @Nullable Object listeners) {
        super(items, listeners);
    }

    public RecordAdapter(@Nullable List<BaseRecordItem<?>> items, @Nullable Object listeners, boolean stableIds) {
        super(items, listeners, stableIds);
    }

    @Override
    public void updateDataSet(List<BaseRecordItem<?>> items, boolean animate) {
        super.updateDataSet(items, animate);
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public String onCreateBubbleText(int position) {
        if (position < getScrollableHeaders().size()) {
            return "Top";
        } else if (position >= getItemCount() - getScrollableFooters().size()) {
            return "Bottom";
        } else {
            position -= getScrollableHeaders().size() + 1;
        }
        return super.onCreateBubbleText(position);
    }

    public void updateById(@NonNull String id) {
        BaseRecordItem<?> baseItem = getById(id);
        if (baseItem != null) {
            updateItem(baseItem);
        }
    }

    @Nullable
    public BaseRecordItem<?> getById(@NonNull String id) {
        List<BaseRecordItem<?>> items = getCurrentItems();
        for (BaseRecordItem<?> item : items) {
            if (id.equals(item.id)) {
                return item;
            }
        }
        return null;
    }
}
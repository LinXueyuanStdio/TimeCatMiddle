package com.timecat.middle.block.item;

import com.timecat.layout.ui.entity.BaseItem;
import com.timecat.data.room.record.RoomRecord;
import com.timecat.middle.block.ext.TitleGetter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/15
 * @description null
 * @usage null
 */
public abstract class BaseRecordItem<T extends FlexibleViewHolder>
        extends BaseItem<T> implements IFilterable<String> {
    public enum TaskState {
        FINISH, WAITING, DOING, DELAY, PAUSE
    }

    public RoomRecord record;
    public TaskState state = TaskState.FINISH; //0 完成，1 等待，2 进行中，3 延期

    public BaseRecordItem(RoomRecord record) {
        super(record.getUuid());
        this.record = record;
        setTitle(TitleGetter.getPrettyTitle(record));
        setDraggable(false);
        setSwipeable(false);
    }

    @Override
    public boolean filter(String constraint) {
        return getTitle() != null && getTitle().toLowerCase().trim().contains(constraint) ||
                getSubtitle() != null && getSubtitle().toLowerCase().trim().contains(constraint);
    }

    @NotNull
    @Override
    public String toString() {
        return record.toString();
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, T holder, int position,
                               List<Object> payloads) {}
}
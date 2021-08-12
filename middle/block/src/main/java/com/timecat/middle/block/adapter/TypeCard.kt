package com.timecat.middle.block.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.timecat.element.alert.ToastUtil
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.middle.block.R
import com.timecat.middle.block.adapter.vh.BaseRecordCardVH
import com.timecat.module.master.adapter.item.virtual.TypeItem
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IExpandable
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/21
 * @description null
 * @usage null
 */
class TypeCard(
    val item: TypeItem
) : BaseItem<TypeCard.TypeCardVH>(item.title), IExpandable<TypeCard.TypeCardVH, BaseItem<*>> {
    class TypeCardVH(v: View, adapter: FlexibleAdapter<*>) : BaseRecordCardVH(v, adapter) {
        val title: TextView = v.findViewById(R.id.title)

        init {
            frontView.setOnClickListener(this)
        }

        /**
         * Allows to expand or collapse child views of this itemView when [View.OnClickListener]
         * event occurs on the entire view.
         *
         * This method returns always true; Extend with "return false" to Not expand or collapse
         * this ItemView onClick events.
         *
         * @return always true, if not overridden
         * @since 5.0.0-b1
         */
        override fun isViewExpandableOnClick(): Boolean {
            return true //default=true
        }

        /**
         * Allows to collapse child views of this ItemView when [View.OnClickListener]
         * event occurs on the entire view.
         *
         * This method returns always true; Extend with "return false" to Not collapse this
         * ItemView onClick events.
         *
         * @return always true, if not overridden
         * @since 5.0.4
         */
        override fun isViewCollapsibleOnClick(): Boolean {
            return true //default=true
        }

        /**
         * Allows to collapse child views of this ItemView when [View.OnLongClickListener]
         * event occurs on the entire view.
         *
         * This method returns always true; Extend with "return false" to Not collapse this
         * ItemView onLongClick events.
         *
         * @return always true, if not overridden
         * @since 5.0.0-b1
         */
        override fun isViewCollapsibleOnLongClick(): Boolean {
            return true //default=true
        }

        /**
         * Allows to notify change and rebound this itemView on expanding and collapsing events,
         * in order to update the content (so, user can decide to display the current expanding status).
         *
         * This method returns always false; Override with `"return true"` to trigger the
         * notification.
         *
         * @return true to rebound the content of this itemView on expanding and collapsing events,
         * false to ignore the events
         * @see .expandView
         * @see .collapseView
         * @since 5.0.0-rc1
         */
        override fun shouldNotifyParentOnClick(): Boolean {
            return true //default=false
        }

        /**
         * Expands or Collapses based on the current state.
         *
         * @see .shouldNotifyParentOnClick
         * @see .expandView
         * @see .collapseView
         * @since 5.0.0-b1
         */
        override fun toggleExpansion() {
            super.toggleExpansion() //If overridden, you must call the super method
        }

        /**
         * Triggers expansion of this itemView.
         *
         * If [.shouldNotifyParentOnClick] returns `true`, this view is rebound
         * with payload [Payload.EXPANDED].
         *
         * @see .shouldNotifyParentOnClick
         * @since 5.0.0-b1
         */
        override fun expandView(position: Int) {
            super.expandView(position) //If overridden, you must call the super method
            // Let's notify the item has been expanded. Note: from 5.0.0-rc1 the next line becomes
            // obsolete, override the new method shouldNotifyParentOnClick() as showcased here
            //if (mAdapter.isExpanded(position)) mAdapter.notifyItemChanged(position, true);
        }

        /**
         * Triggers collapse of this itemView.
         *
         * If [.shouldNotifyParentOnClick] returns `true`, this view is rebound
         * with payload [Payload.COLLAPSED].
         *
         * @see .shouldNotifyParentOnClick
         * @since 5.0.0-b1
         */
        override fun collapseView(position: Int) {
            super.collapseView(position) //If overridden, you must call the super method
            // Let's notify the item has been collapsed. Note: from 5.0.0-rc1 the next line becomes
            // obsolete, override the new method shouldNotifyParentOnClick() as showcased here
            //if (!mAdapter.isExpanded(position)) mAdapter.notifyItemChanged(position, true);
        }
    }

    override fun getLayoutRes(): Int = R.layout.card_virtual_item

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): TypeCardVH {
        return TypeCardVH(view, adapter)
    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: TypeCardVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        holder.title.text = item.title
        holder.title.setShakelessClickListener {
            ToastUtil.i_long(item.desc)
        }
//        holder.frontView.setShakelessClickListener {
//            if (isExpanded) {
//                adapter.collapse(adapter.getGlobalPositionOf(this))
//            } else {
//                adapter.expand(this)
//            }
//            isExpanded = !isExpanded
//        }
    }

    //region IExpandable
    private var mSubItems: MutableList<BaseItem<*>> = mutableListOf()
    override fun getExpansionLevel(): Int = 0

    override fun isExpanded(): Boolean = item.expand
    override fun setExpanded(expanded: Boolean) {
        item.expand = expanded
    }

    override fun getSubItems(): MutableList<BaseItem<*>> = mSubItems

    fun hasSubItems(): Boolean {
        return mSubItems.size > 0
    }

    fun removeSubItem(item: BaseItem<*>): Boolean {
        return mSubItems.remove(item)
    }

    fun removeSubItem(position: Int): Boolean {
        if (position >= 0 && position < mSubItems.size) {
            mSubItems.removeAt(position)
            return true
        }
        return false
    }

    fun addSubItem(subItem: BaseItem<*>) {
        mSubItems.add(subItem)
    }

    fun addSubItem(position: Int, subItem: BaseItem<*>) {
        if (position >= 0 && position < mSubItems.size) {
            mSubItems.add(position, subItem)
        } else addSubItem(subItem)
    }
    //endregion

}
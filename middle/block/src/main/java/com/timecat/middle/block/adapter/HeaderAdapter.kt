package com.timecat.middle.block.adapter

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.timecat.middle.block.R
import de.hdodenhof.circleimageview.CircleImageView

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-09
 * @description null
 * @usage null
 */
abstract class HeaderAdapter<T, K : BaseViewHolder>(data: MutableList<T>) :
    BaseQuickAdapter<T, K>(R.layout.view_item_comment, data) {

    protected fun setHeadText(
        helper: BaseViewHolder, @IdRes id: Int,
        item: String,
        listener: View.OnClickListener?
    ) {
        val mTextView = helper.getView<TextView>(id)
        mTextView.text = item
        mTextView.setOnClickListener(listener)
    }

    abstract fun image(url: String, v: ImageView)
    abstract fun onClickAvatar(v: View, id: String)

    protected fun setHeadImage(
        helper: BaseViewHolder, @IdRes id: Int,
        avatar: String, objId: String
    ) {
        val iv = helper.getView<CircleImageView>(id)
        image(avatar, iv)
        iv.setOnClickListener {
            onClickAvatar(it, objId)
        }
    }

    //设置超链接文字
    protected fun getClickableSpan(
        he: String,
        it: String,
        doWhatEnd: String,
        listener: View.OnClickListener
    ): SpannableString {
        val itStrStart = he.length + 1
        val itStrEnd = itStrStart + it.length
        val spanStr = SpannableString("$he $it $doWhatEnd")
        //设置下划线文字
        //            spanStr.setSpan(new BackgroundColorSpan(Color.GRAY), itStrStart, itStrEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的单击事件
        spanStr.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                listener.onClick(widget)
            }
        }, itStrStart, itStrEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        //设置文字的前景色
        spanStr.setSpan(
            ForegroundColorSpan(Color.BLUE),
            itStrStart,
            itStrEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spanStr
    }

}
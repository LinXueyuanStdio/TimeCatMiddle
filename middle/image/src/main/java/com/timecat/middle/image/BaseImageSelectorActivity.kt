package com.timecat.middle.image

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.broadcast.BroadcastAction
import com.luck.picture.lib.broadcast.BroadcastManager
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.decoration.GridSpacingItemDecoration
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.permissions.PermissionChecker
import com.luck.picture.lib.tools.PictureFileUtils
import com.luck.picture.lib.tools.ScreenUtils
import com.timecat.page.base.friend.toolbar.BaseToolbarActivity
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.element.alert.ToastUtil
import com.timecat.extend.image.GlideEngine
import com.timecat.extend.image.IMG
import com.timecat.extend.image.savablePath
import com.timecat.extend.image.selectForResult
import com.timecat.middle.image.adapter.FullyGridLayoutManager
import com.timecat.middle.image.adapter.GridImageAdapter
import com.timecat.middle.image.listener.DragListener
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/29
 * @description 九宫格编辑
 * @usage 继承本类，按下面要求提供 view 对象，重写 initView() 即可。
 * 需要
 * - RecyclerView : 默认 -- layout() 里必须有 R.id.rv
 */
abstract class BaseImageSelectorActivity : BaseToolbarActivity() {
    protected lateinit var imageAdapter: GridImageAdapter
    private lateinit var tvDeleteText: TextView
    private var mItemTouchHelper: ItemTouchHelper? = null
    private var mDragListener: DragListener? = null
    private var isUpward = false
    private var needScaleBig = true
    private var needScaleSmall = true

    protected open fun imageSelectMax(): Int = 9

    protected open fun imageItemDecoration(): RecyclerView.ItemDecoration =
        GridSpacingItemDecoration(4, ScreenUtils.dip2px(this, 8f), false)

    protected open fun imageLayoutManager(): GridLayoutManager =
        FullyGridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false)

    /**
     * 图片选好后放在这个 RecyclerView 里
     * 图片信息可以从 imageAdapter.data 里拿
     * 如果不是在 layout() 里定义的，可以重写这个方法，自己提供个 RecyclerView 对象
     */
    protected open fun imageRecyclerView(): RecyclerView {
        return findViewById(R.id.rv)
    }

    override fun layout(): Int = R.layout.middle_activity_image

    override fun initView() {
        tvDeleteText = findViewById(R.id.tv_delete_text)
        val mRecyclerView = imageRecyclerView()
        mRecyclerView.layoutManager = imageLayoutManager()
        mRecyclerView.addItemDecoration(imageItemDecoration())
        imageAdapter = GridImageAdapter(this, onAddPicClickListener)
        imageAdapter.setSelectMax(imageSelectMax())
        mRecyclerView.adapter = imageAdapter
        imageAdapter.setOnItemClickListener { _, position ->
            val selectList = imageAdapter.data
            if (selectList.size > 0) {
                val media = selectList[position]
                val mimeType = media.mimeType
                when (PictureMimeType.getMimeType(mimeType)) {
                    PictureConfig.TYPE_VIDEO ->
                        // 预览视频
                        PictureSelector.create(this)
                            .themeStyle(R.style.picture_default_style)
                            .externalPictureVideo(media.savablePath())
                    PictureConfig.TYPE_AUDIO ->
                        // 预览音频
                        PictureSelector.create(this)
                            .externalPictureAudio(media.savablePath())
                    else ->
                        // 预览图片 可自定长按保存路径
                        PictureSelector.create(this)
                            .themeStyle(R.style.picture_default_style) // xml设置主题
                            .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) // 设置相册Activity方向，不设置默认使用系统
                            // 预览图片长按是否可以下载
                            .isNotPreviewDownload(true)
                            // 外部传入图片加载引擎，必传项
                            .imageEngine(GlideEngine.createGlideEngine())
                            .openExternalPreview(position, selectList)
                }
            }
        }
        imageAdapter.setItemLongClickListener { holder, _, _ ->
            //如果item不是最后一个，则执行拖拽
            needScaleBig = true
            needScaleSmall = true
            val size: Int = imageAdapter.data.size
            if (size != imageSelectMax()) {
                mItemTouchHelper!!.startDrag(holder)
                return@setItemLongClickListener
            }
            if (holder.layoutPosition != size - 1) {
                mItemTouchHelper!!.startDrag(holder)
            }
        }
        mDragListener = object : DragListener {
            override fun deleteState(isDelete: Boolean) {
                if (isDelete) {
                    tvDeleteText.text = getString(R.string.app_let_go_drag_delete)
                    tvDeleteText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        R.drawable.ic_let_go_delete,
                        0,
                        0
                    )
                } else {
                    tvDeleteText.text = getString(R.string.app_drag_delete)
                    tvDeleteText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        R.drawable.picture_icon_delete,
                        0,
                        0
                    )
                }
            }

            override fun dragState(isStart: Boolean) {
                val visibility: Int = tvDeleteText.visibility
                if (isStart) {
                    if (visibility == View.GONE) {
                        tvDeleteText.animate()
                            .alpha(1f)
                            .setDuration(300)
                            .interpolator = AccelerateInterpolator()
                        tvDeleteText.visibility = View.VISIBLE
                    }
                } else {
                    if (visibility == View.VISIBLE) {
                        tvDeleteText.animate()
                            .alpha(0f)
                            .setDuration(300)
                            .interpolator = AccelerateInterpolator()
                        tvDeleteText.visibility = View.GONE
                    }
                }
            }
        }
        mItemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            val rect = Rect()
            override fun isLongPressDragEnabled(): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val itemViewType = viewHolder.itemViewType
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    viewHolder.itemView.alpha = 0.7f
                }
                return makeMovementFlags(
                    ItemTouchHelper.DOWN or ItemTouchHelper.UP
                            or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                    0
                )
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                try {
                    //得到item原来的position
                    val fromPosition = viewHolder.adapterPosition
                    //得到目标position
                    val toPosition = target.adapterPosition
                    val itemViewType = target.itemViewType
                    if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                        if (fromPosition < toPosition) {
                            for (i in fromPosition until toPosition) {
                                Collections.swap(imageAdapter.data, i, i + 1)
                            }
                        } else {
                            for (i in fromPosition downTo toPosition + 1) {
                                Collections.swap(imageAdapter.data, i, i - 1)
                            }
                        }
                        imageAdapter.notifyItemMoved(fromPosition, toPosition)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return true
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemViewType = viewHolder.itemViewType
                if (itemViewType == GridImageAdapter.TYPE_CAMERA) return
                if (null == mDragListener) return

                if (needScaleBig) {
                    //执行放大动画之前,标记改掉，防止重复触发放大
                    needScaleBig = false
                    //默认不需要执行缩小动画，当执行完成放大 并且松手后才允许执行
                    needScaleSmall = false
                    //如果需要执行放大动画
                    viewHolder.itemView.animate()
                        .scaleXBy(0.1f)
                        .scaleYBy(0.1f)
                        .duration = 100
                }
                recyclerView.getGlobalVisibleRect(rect)
                val sh: Float = rect.exactCenterY()
                val ry: Float = tvDeleteText.y - sh
                if (dY >= ry) {
                    //拖到删除处
                    mDragListener!!.deleteState(true)
                    if (isUpward) {
                        //在删除处放手，则删除item
                        viewHolder.itemView.visibility = View.INVISIBLE
                        imageAdapter.delete(viewHolder.adapterPosition)
                        resetState()
                        return
                    }
                } else {
                    //没有到删除处
                    if (View.INVISIBLE == viewHolder.itemView.visibility) {
                        //如果viewHolder不可见，则表示用户放手，重置删除区域状态
                        mDragListener!!.dragState(false)
                    }
                    if (needScaleSmall) {
                        //需要松手后才能执行
                        viewHolder.itemView.animate()
                            .scaleXBy(1f)
                            .scaleYBy(1f)
                            .duration = 100
                    }
                    mDragListener!!.deleteState(false)
                }
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            override fun onSelectedChanged(
                viewHolder: RecyclerView.ViewHolder?,
                actionState: Int
            ) {
                val itemViewType =
                    viewHolder?.itemViewType ?: GridImageAdapter.TYPE_CAMERA
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    if (ItemTouchHelper.ACTION_STATE_DRAG == actionState && mDragListener != null) {
                        mDragListener!!.dragState(true)
                    }
                    super.onSelectedChanged(viewHolder, actionState)
                }
            }

            override fun getAnimationDuration(
                recyclerView: RecyclerView,
                animationType: Int,
                animateDx: Float,
                animateDy: Float
            ): Long {
                needScaleSmall = true
                isUpward = true
                return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy)
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                val itemViewType = viewHolder.itemViewType
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    viewHolder.itemView.alpha = 1.0f
                    super.clearView(recyclerView, viewHolder)
                    imageAdapter.notifyDataSetChanged()
                    resetState()
                }
            }
        })

        // 绑定拖拽事件
        mItemTouchHelper!!.attachToRecyclerView(mRecyclerView)

        // 注册广播
        BroadcastManager.getInstance(context).registerReceiver(
            broadcastReceiver,
            BroadcastAction.ACTION_DELETE_PREVIEW_POSITION
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let { bundle ->
            bundle.getParcelableArrayList<LocalMedia>("selectorList")?.let {
                imageAdapter.setList(it)
            }
        } ?: clearCache()
    }

    /**
     * 重置
     */
    private fun resetState() {
        if (mDragListener != null) {
            mDragListener!!.deleteState(false)
            mDragListener!!.dragState(false)
        }
        isUpward = false
    }

    /**
     * 清空缓存包括裁剪、压缩、AndroidQToPath所生成的文件，注意调用时机必须是处理完本身的业务逻辑后调用；非强制性
     */
    private fun clearCache() {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        if (PermissionChecker.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            //PictureFileUtils.deleteCacheDirFile(this, PictureMimeType.ofImage());
            PictureFileUtils.deleteAllCacheDirFile(this)
        } else {
            PermissionChecker.requestPermissions(
                this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            // 存储权限
            PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE -> {
                for (i in grantResults) {
                    if (i == PackageManager.PERMISSION_GRANTED) {
                        PictureFileUtils.deleteCacheDirFile(context, PictureMimeType.ofImage())
                    } else {
                        ToastUtil.e(R.string.picture_jurisdiction)
                    }
                }
            }
        }
    }

    protected open fun onAddImage() {
        IMG.select(PictureSelector.create(this).openGallery(PictureMimeType.ofAll()))
            .selectionData(imageAdapter.data)
            .selectForResult {
                imageAdapter.setList(it)
                imageAdapter.notifyDataSetChanged()
            }
    }

    private val onAddPicClickListener = GridImageAdapter.onAddPicClickListener {
        onAddImage()
        LogUtil.se("onAddImage")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (imageAdapter.data != null && imageAdapter.data.size > 0) {
            outState.putParcelableArrayList(
                "selectorList",
                imageAdapter.data as ArrayList<out Parcelable?>
            )
        }
    }

    private val broadcastReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (TextUtils.isEmpty(action)) {
                return
            }
            if (BroadcastAction.ACTION_DELETE_PREVIEW_POSITION == action) {
                // 外部预览删除按钮回调
                intent.extras?.let {
                    val position = it.getInt(PictureConfig.EXTRA_PREVIEW_DELETE_POSITION)
                    imageAdapter.remove(position)
                    imageAdapter.notifyItemRemoved(position)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (broadcastReceiver != null) {
            BroadcastManager.getInstance(context).unregisterReceiver(
                broadcastReceiver,
                BroadcastAction.ACTION_DELETE_PREVIEW_POSITION
            )
        }
    }

}
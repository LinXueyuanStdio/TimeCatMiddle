package com.timecat.module.master.fragment.home.type

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.hasActionButtons
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.internal.list.DialogRecyclerView
import com.afollestad.materialdialogs.list.getItemSelector
import com.afollestad.materialdialogs.utils.MDUtil
import com.afollestad.materialdialogs.utils.MDUtil.isColorDark
import com.afollestad.materialdialogs.utils.MDUtil.maybeSetTextColor
import com.timecat.component.commonsdk.extension.beVisibleIf
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.type.BLOCK_CONTAINER
import com.timecat.middle.block.R
import com.timecat.middle.block.ext.prettyTitle
import com.timecat.middle.block.service.IDatabase
import kotlinx.coroutines.*
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/6/20
 * @description null
 * @usage null
 */

typealias ChooseRecordFilter = ((RoomRecord) -> Boolean)?
typealias ChooseRecordCallback = ((dialog: MaterialDialog, file: RoomRecord?) -> Unit)?

val RoomRecord.isDirectory: Boolean
    get() = type == BLOCK_CONTAINER

internal fun RoomRecord.hasParent() = true

internal suspend fun RoomRecord.betterParent(
    getParent: suspend (String) -> RoomRecord?
): RoomRecord? {
    return getParent(parent)
}

internal class FileChooserViewHolder(
    itemView: View,
    private val adapter: FileChooserAdapter
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.setOnClickListener(this)
    }

    val iconView: ImageView = itemView.findViewById(R.id.icon)
    val nameView: TextView = itemView.findViewById(R.id.name)

    override fun onClick(view: View) = adapter.itemClicked(adapterPosition)
}

/** @author Aidan Follestad (afollestad */
internal class FileChooserAdapter(
    private val dialog: MaterialDialog,
    initialFolder: RoomRecord?,
    private val waitForPositiveButton: Boolean,
    private val emptyView: TextView,
    private val onlyFolders: Boolean,
    private val filter: ChooseRecordFilter,
    private val loadFor: suspend (String) -> List<RoomRecord>,
    private val getParent: suspend (String) -> RoomRecord?,
    private val callback: ChooseRecordCallback
) : RecyclerView.Adapter<FileChooserViewHolder>() {

    var selectedFile: RoomRecord? = null

    private var currentFolder = initialFolder
    private var listingJob: Job? = null
    private var contents: List<RoomRecord>? = null

    private val isLightTheme =
        MDUtil.resolveColor(dialog.windowContext, attr = android.R.attr.textColorPrimary).isColorDark()

    init {
        dialog.onDismiss { listingJob?.cancel() }
        switchDirectory(initialFolder)
    }

    fun itemClicked(index: Int) {
        if (currentFolder != null && index == goUpIndex()) {
            // go up
            GlobalScope.launch(Dispatchers.IO) {
                val parent = currentFolder?.betterParent(getParent)
                switchDirectory(parent)
            }
            return
        }

        val actualIndex = actualIndex(index)
        val selected = contents!![actualIndex]

        if (selected.isDirectory) {
            switchDirectory(selected)
        } else {
            val previousSelectedIndex = getSelectedIndex()
            this.selectedFile = selected
            val actualWaitForPositive = waitForPositiveButton && dialog.hasActionButtons()

            if (actualWaitForPositive) {
                dialog.setActionButtonEnabled(WhichButton.POSITIVE, true)
                notifyItemChanged(index)
                notifyItemChanged(previousSelectedIndex)
            } else {
                callback?.invoke(dialog, selected)
                dialog.dismiss()
            }
        }
    }

    private fun switchDirectory(directory: RoomRecord?) {
        listingJob?.cancel()
        listingJob = GlobalScope.launch(Dispatchers.Main) {
            if (onlyFolders) {
                selectedFile = directory
                dialog.setActionButtonEnabled(WhichButton.POSITIVE, true)
            }

            currentFolder = directory
            dialog.title(text = directory?.prettyTitle ?: "时光猫")

            val result = withContext(Dispatchers.IO) {
                val rawContents = loadFor(directory?.uuid ?: "")
                if (onlyFolders) {
                    rawContents
                        .filter { it.isDirectory && filter?.invoke(it) ?: true }
                        .sortedBy { it.name.toLowerCase(Locale.getDefault()) }
                } else {
                    rawContents
                        .filter { filter?.invoke(it) ?: true }
                        .sortedWith(compareBy({ !it.isDirectory }, {
                            it.prettyTitle.toLowerCase(Locale.getDefault())
                        }))
                }
            }

            contents = result.apply {
                emptyView.beVisibleIf(isEmpty())
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        var count = contents?.size ?: 0
        if (currentFolder?.hasParent() == true) {
            count += 1
        }
        return count
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FileChooserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.md_file_chooser_item, parent, false)
        view.background = dialog.getItemSelector()

        val viewHolder = FileChooserViewHolder(view, this)
        viewHolder.nameView.maybeSetTextColor(dialog.windowContext, R.attr.md_color_content)
        return viewHolder
    }

    override fun onBindViewHolder(holder: FileChooserViewHolder, position: Int) {
        if (position == goUpIndex()) {
            if (currentFolder?.hasParent() == true) {
                GlobalScope.launch(Dispatchers.IO) {
                    val currentParent = currentFolder?.betterParent(getParent)
                    withContext(Dispatchers.Main) {
                        // Go up
                        holder.iconView.setImageResource(
                            if (isLightTheme) R.drawable.icon_return_dark
                            else R.drawable.icon_return_light
                        )
                        holder.nameView.text = currentParent?.prettyTitle ?: "时光猫"
                        holder.itemView.isActivated = false
                    }
                }
                return
            }
        }

        val actualIndex = actualIndex(position)
        val item = contents!![actualIndex]
        holder.iconView.setImageResource(item.iconRes())
        holder.nameView.text = item.prettyTitle
        holder.itemView.isActivated = selectedFile?.uuid == item.uuid
    }

    private fun goUpIndex() = if (currentFolder?.hasParent() == true) 0 else -1

    private fun newFolderIndex() = if (currentFolder?.hasParent() == true) 1 else 0

    private fun actualIndex(position: Int): Int {
        var actualIndex = position
        if (currentFolder?.hasParent() == true) {
            actualIndex -= 1
        }
        return actualIndex
    }

    private fun RoomRecord.iconRes(): Int {
        return if (isLightTheme) {
            if (this.isDirectory) R.drawable.icon_folder_dark
            else R.drawable.icon_file_dark
        } else {
            if (this.isDirectory) R.drawable.icon_folder_light
            else R.drawable.icon_file_light
        }
    }

    private fun getSelectedIndex(): Int {
        if (selectedFile == null) return -1
        else if (contents?.isEmpty() == true) return -1
        val index = contents?.indexOfFirst { it.uuid == selectedFile?.uuid } ?: -1
        return if (index > -1 && currentFolder?.hasParent() == true) index + 1 else index
    }
}


fun MaterialDialog.containerChooser(
    initialDirectory: RoomRecord? = null,
    filter: ChooseRecordFilter = null,
    waitForPositiveButton: Boolean = true,
    emptyTextRes: Int = R.string.files_default_empty_text,
    db: IDatabase,
    selection: ChooseRecordCallback = null
): MaterialDialog {
    var actualFilter: ChooseRecordFilter = filter

    if (filter == null) {
        actualFilter = { true }
    }

    customView(R.layout.md_file_chooser_base, noVerticalPadding = true)
    setActionButtonEnabled(WhichButton.POSITIVE, false)

    val customView = getCustomView()
    val list: DialogRecyclerView = customView.findViewById(R.id.list)
    val emptyText: TextView = customView.findViewById(R.id.empty_text)
    emptyText.setText(emptyTextRes)
    emptyText.maybeSetTextColor(windowContext, R.attr.md_color_content)

    list.attach(this)
    list.layoutManager = LinearLayoutManager(windowContext)
    val adapter = FileChooserAdapter(
        dialog = this,
        initialFolder = initialDirectory,
        waitForPositiveButton = waitForPositiveButton,
        emptyView = emptyText,
        onlyFolders = true,
        loadFor = { uuid ->
//            SortType.TYPE
            db.getAllLiveChildren(uuid, 4, true, 0, 512)
        },
        getParent = { uuid ->
            db.getByUuid(uuid)
        },
        filter = actualFilter,
        callback = selection
    )
    list.adapter = adapter

    if (waitForPositiveButton && selection != null) {
        setActionButtonEnabled(WhichButton.POSITIVE, false)
        positiveButton {
            val selectedFile = adapter.selectedFile
            if (selectedFile != null) {
                selection.invoke(this, selectedFile)
            }
        }
    }

    return this
}

fun MaterialDialog.recordChooser(
    initialDirectory: RoomRecord? = null,
    filter: ChooseRecordFilter = null,
    waitForPositiveButton: Boolean = true,
    emptyTextRes: Int = R.string.files_default_empty_text,
    db: IDatabase,
    selection: ChooseRecordCallback = null
): MaterialDialog {
    var actualFilter: ChooseRecordFilter = filter

    if (filter == null) {
        actualFilter = { true }
    }

    customView(R.layout.md_file_chooser_base, noVerticalPadding = true)
    setActionButtonEnabled(WhichButton.POSITIVE, false)

    val customView = getCustomView()
    val list: DialogRecyclerView = customView.findViewById(R.id.list)
    val emptyText: TextView = customView.findViewById(R.id.empty_text)
    emptyText.setText(emptyTextRes)
    emptyText.maybeSetTextColor(windowContext, R.attr.md_color_content)

    list.attach(this)
    list.layoutManager = LinearLayoutManager(windowContext)
    val adapter = FileChooserAdapter(
        dialog = this,
        initialFolder = initialDirectory,
        waitForPositiveButton = waitForPositiveButton,
        emptyView = emptyText,
        onlyFolders = false,
        loadFor = { uuid ->
//            SortType.TYPE
            db.getAllLiveChildren(uuid, 4, true, 0, 512)
        },
        getParent = { uuid ->
            db.getByUuid(uuid)
        },
        filter = actualFilter,
        callback = selection
    )
    list.adapter = adapter

    if (waitForPositiveButton && selection != null) {
        setActionButtonEnabled(WhichButton.POSITIVE, false)
        positiveButton {
            val selectedFile = adapter.selectedFile
            if (selectedFile != null) {
                selection.invoke(this, selectedFile)
            }
        }
    }

    return this
}
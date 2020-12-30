package com.timecat.middle.block.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputFilter
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.timecat.component.identity.Attr
import com.timecat.data.room.TimeCatRoomDatabase.Companion.forFile
import com.timecat.data.room.habit.Habit
import com.timecat.data.room.habit.HabitRecord
import com.timecat.data.room.habit.HabitReminder
import com.timecat.data.room.record.RecordDao
import com.timecat.data.room.record.RoomRecord
import com.timecat.data.room.reminder.Reminder
import com.timecat.identity.data.base.GOAL
import com.timecat.identity.data.base.HABIT
import com.timecat.identity.data.base.REMINDER
import com.timecat.identity.data.base.Type
import com.timecat.middle.block.R
import com.timecat.middle.block.adapter.RecurrencePickerAdapter
import com.timecat.middle.block.adapter.TimeOfDayRecAdapter
import com.timecat.middle.block.popup.DateTimePicker
import com.timecat.middle.block.support.ReminderHabitParams
import com.timecat.middle.block.temp.Def
import com.timecat.middle.block.util.DateTimeUtil
import com.timecat.middle.block.util.DisplayUtil
import com.timecat.middle.block.util.KeyboardUtil
import com.timecat.middle.block.util.LocaleUtil
import com.timecat.middle.block.view.InputLayout
import org.joda.time.DateTime
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-12-26
 * @description null
 * @usage null
 */
class AddTimerPopup(
    var mRoomRecord: RoomRecord,
    var rhParams: ReminderHabitParams,
    var parent: View,
    var listener: Listener
) {
    interface Listener {
        fun onChangeReminder(rhParams: ReminderHabitParams, desc: String)
    }

    private val view: View = LayoutInflater.from(parent.context).inflate(
        R.layout.view_popup_add_timer, null, false
    )
    private val popupWindow: PopupWindow
    private val context: Context = parent.context
    private val NO_PROBLEM: String = "no problem"

    private var mAccentColor: Int = Attr.getAccentColor(context)
    private var black_54p: Int = Attr.getPrimaryTextColor(context)
    private var black_26p: Int = Attr.getSecondaryTextColor(context)

    //region view
    private val mVpDateTime: ViewFlipper
    // at
    private val mTimeTypes: IntArray = intArrayOf(
        Calendar.YEAR,
        Calendar.MONTH,
        Calendar.DATE,
        Calendar.HOUR_OF_DAY,
        Calendar.MINUTE
    )
    private lateinit var mTvSummaryAt: TextView
    private lateinit var mTvsAt: Array<TextView>
    private lateinit var mEtsAt: Array<EditText>
    private lateinit var mIlsAt: Array<InputLayout>

    // after
    private lateinit var mEtTimeAfter: EditText
    private lateinit var mTvTimeAsBtAfter: TextView
    private lateinit var mDtpAfter: DateTimePicker
    private lateinit var mTvErrorAfter: TextView

    // recurrence
    private lateinit var mTvTimesLRec: TextView
    private lateinit var mTvTimesRRec: TextView
    private lateinit var mTvTimeAsBtRec: TextView
    private lateinit var mTvSummaryRec: TextView
    private lateinit var mDtpRec: DateTimePicker
    private lateinit var mIvPickAllAsBtRec: ImageView

    private lateinit var mRvTimeOfDay: RecyclerView
    private lateinit var mAdapterTimeOfDay: TimeOfDayRecAdapter
    private lateinit var mLlmTimeOfDay: LinearLayoutManager

    private lateinit var mRlWmy: RelativeLayout// wmy -> Week Month Year
    private lateinit var mFlDayYear: FrameLayout
    private lateinit var mIlDayYear: InputLayout
    private lateinit var mIlHourWmy: InputLayout
    private lateinit var mIlMinuteWmy: InputLayout
    private lateinit var mRvWmy: RecyclerView
    private lateinit var mGlmDayOfWeek: GridLayoutManager
    private lateinit var mGlmDayOfMonth: GridLayoutManager
    private lateinit var mGlmMonthOfYear: GridLayoutManager
    private lateinit var mAdapterDayOfWeek: RecurrencePickerAdapter
    private lateinit var mAdapterDayOfMonth: RecurrencePickerAdapter
    private lateinit var mAdapterMonthOfYear: RecurrencePickerAdapter

    // footer
    private lateinit var mTvConfirmAsBt: TextView
    private lateinit var mTvCancelAsBt: TextView
    //endregion

    fun recordDao(): RecordDao = forFile(context).recordDao()

    fun <T : View> f(layout: Int): T = f(view, layout)

    fun <T : View> f(view: View, layout: Int): T = view.findViewById(layout)

    fun getHabitById(id: Long): Habit? {
        val habit = forFile(context).habitDao().getByID(id) ?: return null
        habit.habitReminders = getHabitRemindersByHabitId(id)
        habit.habitRecords = getHabitRecordsByHabitId(id)
        return habit
    }

    private fun getHabitRemindersByHabitId(habitId: Long): List<HabitReminder> {
        return forFile(context).habitReminderDao().getByHabit(habitId)
    }

    private fun getHabitRecordsByHabitId(habitId: Long): List<HabitRecord> {
        return forFile(context).habitRecordDao().getByHabit(habitId)
    }


    private fun initAll(page: Int) {
        mVpDateTime.displayedChild = page;
        val list = listOf(
            (f(R.id.tv0) as TextView),
            (f(R.id.tv1) as TextView),
            (f(R.id.tv2) as TextView)
        )
        for (i in list) {
            i.setTextColor(Attr.getTertiaryTextColor(context))
        }
        list[page].setTextColor(Attr.getPrimaryTextColor(context))
        if (page == 0) {
            findViewsAt()
            initUIAt()
            setEventsAt()
        } else if (page == 1) {
            findViewsAfter()
            initUIAfter()
            setEventsAfter()
        } else if (page == 2) {
            findViewsRec()
            initUIRec()
            setEventsRec()
            if (mRoomRecord.subType == HABIT || rhParams.habitDetail != null) {
                val type: Int = if (rhParams.habitDetail != null) {
                    rhParams.habitType
                } else {
                    val habit: Habit = getHabitById(mRoomRecord.id)!!
                    habit.type
                }
                when (type) {
                    Calendar.DATE -> updateUIRecDay()
                    Calendar.WEEK_OF_YEAR -> updateUIRecWeek()
                    Calendar.MONTH -> updateUIRecMonth()
                    Calendar.YEAR -> updateUIRecYear()
                }
            } else {
                updateUIRecDay()
            }
        } else {
            initAll(0)
            return
        }
    }

    private fun findViews() {
        mTvConfirmAsBt = f(R.id.tv_confirm_as_bt)
        mTvCancelAsBt = f(R.id.tv_cancel_as_bt)

        val tv0: TextView = f(R.id.tv0)
        val tv1: TextView = f(R.id.tv1)
        val tv2: TextView = f(R.id.tv2)
        tv0.setOnClickListener { initAll(0) }
        tv1.setOnClickListener { initAll(1) }
        tv2.setOnClickListener { initAll(2) }
    }

    private fun findViewsAt() {
        val tab0: View = f(mVpDateTime, R.id.tab0)
        mTvsAt = arrayOf(
            f(tab0, R.id.tv_year_at),
            f(tab0, R.id.tv_month_at),
            f(tab0, R.id.tv_day_at),
            f(tab0, R.id.tv_hour_at),
            f(tab0, R.id.tv_minute_at)
        )
        mEtsAt = arrayOf(
            f(tab0, R.id.et_year_at),
            f(tab0, R.id.et_month_at),
            f(tab0, R.id.et_day_at),
            f(tab0, R.id.et_hour_at),
            f(tab0, R.id.et_minute_at)
        )
        mIlsAt = arrayOf(
            InputLayout(context, mTvsAt[0], mEtsAt[0], mAccentColor),
            InputLayout(context, mTvsAt[1], mEtsAt[1], mAccentColor),
            InputLayout(context, mTvsAt[2], mEtsAt[2], mAccentColor),
            InputLayout(context, mTvsAt[3], mEtsAt[3], mAccentColor),
            InputLayout(context, mTvsAt[4], mEtsAt[4], mAccentColor)
        )

        mTvSummaryAt = f(tab0, R.id.tv_summary_at)
    }

    private fun findViewsAfter() {
        val tab1: View = f(mVpDateTime, R.id.tab1)
        mEtTimeAfter = f(tab1, R.id.et_time_after)
        mTvTimeAsBtAfter = f(tab1, R.id.tv_time_as_bt_after)
        mDtpAfter = DateTimePicker(
            context, parent,
            Def.PickerType.TIME_TYPE_HAVE_HOUR_MINUTE, mAccentColor
        )
        mTvErrorAfter = f(tab1, R.id.tv_error_after)
    }

    private fun findViewsRec() {
        val tab2: View = f(mVpDateTime, R.id.tab2)
        mTvTimesLRec = f(tab2, R.id.tv_times_l_recurrence)
        mTvTimesRRec = f(tab2, R.id.tv_times_r_recurrence)
        mTvTimeAsBtRec = f(tab2, R.id.tv_time_as_bt_recurrence)
        mDtpRec = DateTimePicker(
            context, parent,
            Def.PickerType.TIME_TYPE_NO_HOUR_MINUTE, mAccentColor
        )
        mIvPickAllAsBtRec = f(tab2, R.id.iv_pick_all_as_bt_rec)
        mTvSummaryRec = f(tab2, R.id.tv_summary_rec)
        // day
        mRvTimeOfDay = f(tab2, R.id.rv_time_of_day)
        mAdapterTimeOfDay = TimeOfDayRecAdapter(context, mAccentColor)
        val items: ArrayList<Int> = ArrayList()
        items.add(-1)
        items.add(-1)
        mAdapterTimeOfDay.setItems(items)
        mLlmTimeOfDay = LinearLayoutManager(context)
        // wmy
        mRlWmy = f(tab2, R.id.rl_rec_wmy)
        mRvWmy = f(tab2, R.id.rv_rec_wmy)
        mFlDayYear = f(tab2, R.id.fl_day_rec_wmy)
        mIlDayYear = InputLayout(
            context,
            f<View>(tab2, R.id.tv_day_rec_wmy) as TextView,
            f<View>(tab2, R.id.et_day_rec_wmy) as EditText,
            mAccentColor
        )
        mIlHourWmy = InputLayout(
            context,
            f<View>(tab2, R.id.tv_hour_rec_wmy) as TextView,
            f<View>(tab2, R.id.et_hour_rec_wmy) as EditText,
            mAccentColor
        )
        mIlMinuteWmy = InputLayout(
            context,
            f<View>(tab2, R.id.tv_minute_rec_wmy) as TextView,
            f<View>(tab2, R.id.et_minute_rec_wmy) as EditText,
            mAccentColor
        )
        // week
        mGlmDayOfWeek = GridLayoutManager(context, 4)
        mAdapterDayOfWeek =
            RecurrencePickerAdapter(context, Def.PickerType.DAY_OF_WEEK, mAccentColor)
        // month
        mGlmDayOfMonth = GridLayoutManager(context, 6)
        mGlmDayOfMonth.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 27) 3 else 1
            }
        }
        mAdapterDayOfMonth =
            RecurrencePickerAdapter(context, Def.PickerType.DAY_OF_MONTH, mAccentColor)
        // year
        mGlmMonthOfYear = GridLayoutManager(context, 4)
        mAdapterMonthOfYear =
            RecurrencePickerAdapter(context, Def.PickerType.MONTH_OF_YEAR, mAccentColor)
    }

    private fun initUI() {
        mTvConfirmAsBt.setTextColor(mAccentColor)
    }

    fun isReminderType(@Type type: Int): Boolean {
        return type == REMINDER || type == GOAL
    }

    private fun initUIAt() {
        var dt = DateTime()
        val reminderInMillis: Long = rhParams.reminderInMillis
        val reminderAfterTime: IntArray? = rhParams.reminderAfterTime
        dt = when {
            reminderInMillis != -1L -> {
                dt.withMillis(reminderInMillis)
            }
            reminderAfterTime != null -> {
                dt.withMillis(DateTimeUtil.getActualTimeAfterSomeTime(reminderAfterTime))
            }
            isReminderType(mRoomRecord.subType) -> {
                val reminder: Reminder = forFile(context).reminderDao().getByID(mRoomRecord.id)
                dt.withMillis(reminder.getNotifyTime())
            }
            else -> {
                dt.plusMinutes(1)
            }
        }
        val times = IntArray(5)
        for (i in times.indices) {
            times[i] = dt.get(DateTimeUtil.getJodaType(mTimeTypes[i]))
            mEtsAt[i].setText("${times[i]}")
            mEtsAt[i].setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    KeyboardUtil.showKeyboard(v)
                }
            }
            mIlsAt[i].raiseLabel(false)
        }
        formatMinuteAt()
        updateSummaryAt(times[0], times[1], times[2], times[3])
    }

    private fun initUIAfter() {
        DisplayUtil.tintView(mEtTimeAfter, black_26p)
        DisplayUtil.setSelectionHandlersColor(mEtTimeAfter, mAccentColor)
        mEtTimeAfter.setTextColor(black_54p)
        mDtpAfter.setAnchor(mTvTimeAsBtAfter)
        mDtpAfter.pickForUI(0)
        improveComplex()
    }

    private fun initUIRec() {
        mDtpRec.setAnchor(mTvTimeAsBtRec)
        (mRvWmy.itemAnimator as SimpleItemAnimator?)?.supportsChangeAnimations = false
    }

    private fun getHabitDetail(): String? {
        var habitDetail: String? = rhParams.habitDetail
        if (habitDetail == null && mRoomRecord.subType == HABIT) {
            habitDetail = getHabitById(mRoomRecord.id)!!.detail
        }
        return habitDetail
    }

    private fun getHabitType(): Int {
        var habitType: Int = rhParams.habitType
        if (habitType == -1 && mRoomRecord.subType == HABIT) {
            habitType = getHabitById(mRoomRecord.id)!!.type
        }
        return habitType
    }

    private fun updateUIRecDay() {
        mDtpRec.pickForUI(0)
        mRvTimeOfDay.visibility = View.VISIBLE
        mRlWmy.visibility = View.GONE
        mIvPickAllAsBtRec.visibility = View.GONE
        if (getHabitType() == Calendar.DATE) {
            val habitDetail: String? = getHabitDetail()
            if (habitDetail != null) {
                mAdapterTimeOfDay.setItems(Habit.getDayTimeListFromDetail(habitDetail))
            }
        } else {
            val dt = DateTime()
            val items: MutableList<Int> = ArrayList()
            items.add(dt.hourOfDay)
            items.add(dt.minuteOfHour)
            mAdapterTimeOfDay.setItems(items)
        }
        mRvTimeOfDay.adapter = mAdapterTimeOfDay
        mRvTimeOfDay.layoutManager = mLlmTimeOfDay
        updateHeightsTimeOfDay()
        updatePickedTimesRec()
        updateTimePeriodRec()
    }

    private fun updateUIRecWeek() {
        mDtpRec.pickForUI(1)
        updateRvHeightRec(1)
        mRlWmy.visibility = View.VISIBLE
        mRvTimeOfDay.visibility = View.GONE
        mIvPickAllAsBtRec.visibility = View.VISIBLE
        updatePickAllButton(mAdapterDayOfWeek)
        mFlDayYear.visibility = View.GONE
        if (getHabitType() == Calendar.WEEK_OF_YEAR) {
            val habitDetail: String? = getHabitDetail()
            if (habitDetail != null) {
                mAdapterDayOfWeek.pick(Habit.getDayOrMonthListFromDetail(habitDetail))
                val times: Array<String> = Habit.getTimeFromDetailWeekMonth(habitDetail)
                mIlHourWmy.setTextForEditText(times[0])
                mIlMinuteWmy.setTextForEditText(times[1])
            }
        } else {
            val dt = DateTime()
            var week: Int = dt.dayOfWeek
            week = if (week == 7) 0 else week
            mAdapterDayOfWeek.pick(week)
            mIlHourWmy.setTextForEditText("" + dt.hourOfDay)
            var minute: String = "" + dt.minuteOfHour
            minute = if (minute.length == 1) "0$minute" else minute
            mIlMinuteWmy.setTextForEditText(minute)
        }
        mRvWmy.adapter = mAdapterDayOfWeek
        mRvWmy.layoutManager = mGlmDayOfWeek
        updatePickedTimesRec()
        updateTimePeriodRec()
    }

    private fun updateUIRecMonth() {
        mDtpRec.pickForUI(2)
        updateRvHeightRec(2)
        mRlWmy.visibility = View.VISIBLE
        mRvTimeOfDay.visibility = View.GONE
        mIvPickAllAsBtRec.visibility = View.VISIBLE
        updatePickAllButton(mAdapterDayOfMonth)
        mFlDayYear.visibility = View.GONE
        if (getHabitType() == Calendar.MONTH) {
            val habitDetail: String? = getHabitDetail()
            if (habitDetail != null) {
                val days: List<Int> = Habit.getDayOrMonthListFromDetail(habitDetail)
                mAdapterDayOfMonth.pick(days)
                if (days[days.size - 1] == 27) {
                    mAdapterDayOfMonth.pick(27)
                    mAdapterDayOfMonth.pick(mAdapterDayOfMonth.itemCount - 1)
                }
                val times: Array<String> = Habit.getTimeFromDetailWeekMonth(habitDetail)
                mIlHourWmy.setTextForEditText(times[0])
                mIlMinuteWmy.setTextForEditText(times[1])
            }
        } else {
            val dt = DateTime()
            var day: Int = dt.dayOfMonth
            day = if (day >= 28) 27 else day - 1
            mAdapterDayOfMonth.pick(day)
            mIlHourWmy.setTextForEditText("${dt.hourOfDay}")
            var minute = "${dt.minuteOfHour}"
            minute = if (minute.length == 1) "0$minute" else minute
            mIlMinuteWmy.setTextForEditText(minute)
        }
        mRvWmy.adapter = mAdapterDayOfMonth
        mRvWmy.layoutManager = mGlmDayOfMonth
        updatePickedTimesRec()
        updateTimePeriodRec()
    }

    private fun updateUIRecYear() {
        mDtpRec.pickForUI(3)
        updateRvHeightRec(3)
        mRlWmy.visibility = View.VISIBLE
        mRvTimeOfDay.visibility = View.GONE
        mIvPickAllAsBtRec.visibility = View.VISIBLE
        updatePickAllButton(mAdapterMonthOfYear)
        mFlDayYear.visibility = View.VISIBLE
        if (getHabitType() == Calendar.YEAR) {
            val habitDetail: String? = getHabitDetail()
            if (habitDetail != null) {
                val months: List<Int> = Habit.getDayOrMonthListFromDetail(habitDetail)
                mAdapterMonthOfYear.pick(months)
                val dayTimes: Array<String> = Habit.getTimeFromDetailYear(habitDetail)
                if ((dayTimes[0] == "28")) {
                    val et: EditText = mIlDayYear.editText
                    et.inputType = EditorInfo.TYPE_CLASS_TEXT
                    et.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(12))
                    mIlDayYear.setTextForEditText(context.getString(R.string.end_of_month))
                } else {
                    mIlDayYear.setTextForEditText(dayTimes[0])
                }
                mIlHourWmy.setTextForEditText(dayTimes[1])
                mIlMinuteWmy.setTextForEditText(dayTimes[2])
            }
        } else {
            val dt = DateTime()
            val month: Int = dt.monthOfYear - 1
            mAdapterMonthOfYear.pick(month)
            val day: Int = dt.dayOfMonth
            if (day >= 28) {
                val et: EditText = mIlDayYear.editText
                et.inputType = EditorInfo.TYPE_CLASS_TEXT
                et.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(12))
                mIlDayYear.setTextForEditText(context.getString(R.string.end_of_month))
            } else {
                mIlDayYear.setTextForEditText("" + day)
            }
            mIlHourWmy.setTextForEditText("" + dt.hourOfDay)
            var minute: String = "" + dt.minuteOfHour
            minute = if (minute.length == 1) "0$minute" else minute
            mIlMinuteWmy.setTextForEditText(minute)
        }
        mRvWmy.adapter = mAdapterMonthOfYear
        mRvWmy.layoutManager = mGlmMonthOfYear
        updatePickedTimesRec()
        updateTimePeriodRec()
    }

    private fun updateRvHeightRec(index: Int) {
        if (index == 0) {
            val params: RelativeLayout.LayoutParams =
                mRvTimeOfDay.layoutParams as RelativeLayout.LayoutParams
            val count: Int = mAdapterTimeOfDay.itemCount
            params.height = (count * 48 * DisplayUtil.getScreenDensity(context)).toInt()
            mRvTimeOfDay.requestLayout()
        } else {
            val sd: Float = DisplayUtil.getScreenDensity(context)
            val heights: FloatArray = floatArrayOf(sd * 122, sd * 240, sd * 184)
            val params: RelativeLayout.LayoutParams =
                mRvWmy.layoutParams as RelativeLayout.LayoutParams
            params.height = heights[index - 1].toInt()
            mRvWmy.requestLayout()
        }
    }

    private fun updatePickAllButton(adapter: RecurrencePickerAdapter) {
        if (adapter.pickedCount == adapter.itemCount) {
            mIvPickAllAsBtRec.setImageResource(R.drawable.every_act_deselect_all)
        } else {
            mIvPickAllAsBtRec.setImageResource(R.drawable.every_act_select_all)
        }
    }

    private fun setEvents() {
        parent.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    KeyboardUtil.hideKeyboard(v)
                    return true
                }
                return false
            }
        })
        popupWindow.setOnDismissListener {
            KeyboardUtil.hideKeyboard(mVpDateTime)
        }
        setButtonEvents()
    }

    private fun setButtonEvents() {
        mTvConfirmAsBt.setOnClickListener { endSettingTime() }
        mTvCancelAsBt.setOnClickListener { popupWindow.dismiss() }
    }

    private fun setEventsAt() {
        mEtsAt[4].setOnEditorActionListener {_,actionId,_->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                endSettingTime()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        for (i in mIlsAt.indices) {
            mIlsAt[i].setOnFocusChangeListenerForEditText(object : View.OnFocusChangeListener {
                override fun onFocusChange(v: View, hasFocus: Boolean) {
                    val times = IntArray(5)
                    var temp: String
                    for (i1 in times.indices) {
                        temp = mEtsAt[i1].text.toString()
                        if (temp.isEmpty()) {
                            times[i1] = -1
                        } else {
                            try {
                                times[i1] = temp.toInt()
                            } catch (e: Throwable) {
                                e.printStackTrace()
                                return
                            }
                        }
                        if ((times[i1] == 0) && (i1 != 0) && (i1 != 3) && (i1 != 4)) {
                            mEtsAt[i1].setText("1")
                            times[i1] = 1
                        }
                        if (times[0] != -1 && times[1] != -1) {
                            val limit: Int =
                                DateTimeUtil.getTimeTypeLimit(times[0], times[1], i1)
                            if (times[i1] > limit) {
                                times[i1] = limit
                                mEtsAt[i1].setText("$limit")
                            }
                        }
                    }
                    if (!hasFocus) {
                        if (mEtsAt[i].text.toString().isNotEmpty()) {
                            formatMinuteAt()
                        }
                        updateSummaryAt(times[0], times[1], times[2], times[3])
                    }
                }
            })
        }
    }

    private fun setEventsAfter() {
        mTvTimeAsBtAfter.setOnClickListener {
            improveComplex()
            mDtpAfter.show()
            KeyboardUtil.hideKeyboard(mEtTimeAfter)
        }
        mDtpAfter.setPickedListener { improveComplex() }
        mEtTimeAfter.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                DisplayUtil.tintView(v, mAccentColor)
                (v as EditText).setTextColor(mAccentColor)
                v.highlightColor = DisplayUtil.getLightColor(mAccentColor, context)
            } else {
                improveComplex()
                DisplayUtil.tintView(v, black_26p)
                (v as EditText).setTextColor(black_54p)
            }
        }
        mEtTimeAfter.setOnEditorActionListener{_,actionId,_->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                improveComplex()
                KeyboardUtil.hideKeyboard(mEtTimeAfter)
                mDtpAfter.show()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setEventsRec() {
        mTvTimeAsBtRec.setOnClickListener {
            improveComplex()
            mDtpRec.show()
        }
        mDtpRec.setPickedListener {
            val pickedIndex: Int = mDtpRec.pickedIndex
            mIvPickAllAsBtRec.visibility = View.VISIBLE
            mRvTimeOfDay.visibility = View.GONE
            mRlWmy.visibility = View.GONE
            mTvSummaryRec.text = ""
            when (pickedIndex) {
                0 -> updateUIRecDay()
                1 -> updateUIRecWeek()
                2 -> updateUIRecMonth()
                else -> updateUIRecYear()
            }
        }
        mIvPickAllAsBtRec.setOnClickListener { pickOrUnpickAll(mDtpRec.pickedIndex) }
        setEventsRecDay()
        setEventsRecWmy()
        setEventsRecWeek()
        setEventsRecMonth()
        setEventsRecYear()
    }

    private fun updateHeightsTimeOfDay() {
        updateRvHeightRec(0)
    }

    private fun pickOrUnpickAll(index: Int) {
        when (index) {
            1 -> {
                if (mAdapterDayOfWeek.pickedCount == mAdapterDayOfWeek.itemCount) {
                    mAdapterDayOfWeek.unpickAll()
                } else {
                    mAdapterDayOfWeek.pickAll()
                }
                mAdapterDayOfWeek.notifyDataSetChanged()
                mTvTimesLRec.text = "${mAdapterDayOfWeek.pickedCount}"
                updatePickAllButton(mAdapterDayOfWeek)
            }
            2 -> {
                if (mAdapterDayOfMonth.pickedCount == mAdapterDayOfMonth.itemCount) {
                    mAdapterDayOfMonth.unpickAll()
                } else {
                    mAdapterDayOfMonth.pickAll()
                }
                mAdapterDayOfMonth.notifyDataSetChanged()
                mTvTimesLRec.text = "${mAdapterDayOfMonth.pickedCount}"
                updatePickAllButton(mAdapterDayOfMonth)
            }
            3 -> {
                if (mAdapterMonthOfYear.pickedCount == mAdapterMonthOfYear.itemCount) {
                    mAdapterMonthOfYear.unpickAll()
                } else {
                    mAdapterMonthOfYear.pickAll()
                }
                mAdapterMonthOfYear.notifyDataSetChanged()
                mTvTimesLRec.text = "${mAdapterMonthOfYear.pickedCount}"
                updatePickAllButton(mAdapterMonthOfYear)
            }
        }
        improveComplex()
    }

    private fun setEventsRecDay() {
        mAdapterTimeOfDay.setOnItemChangeCallback(object :
            TimeOfDayRecAdapter.OnItemChangeCallback {
            override fun onItemInserted() {
                updatePickedTimesRec()
                updateHeightsTimeOfDay()
            }

            override fun onItemRemoved() {
                updatePickedTimesRec()
                updateHeightsTimeOfDay()
            }
        })
    }

    private fun setEventsRecWmy() {
        mIlHourWmy.setOnFocusChangeListenerForEditText(object :
            View.OnFocusChangeListener {
            override fun onFocusChange(v: View, hasFocus: Boolean) {
                if (!hasFocus) {
                    DateTimeUtil.limitHourForEditText(v as EditText?)
                    val hourStr: String = mIlHourWmy.textFromEditText
                    if (hourStr.isEmpty()) {
                        mTvSummaryRec.text = ""
                        return
                    }
                    val hour: Int = hourStr.toInt()
                    if (hour >= 24) {
                        mIlHourWmy.setTextForEditText("23")
                    }
                    updateTimePeriodRec()
                }
            }
        })
        mIlMinuteWmy.setOnFocusChangeListenerForEditText { v, hasFocus ->
            if (!hasFocus) {
                DateTimeUtil.formatLimitMinuteForEditText(v as EditText?)
            }
        }
    }

    private fun setEventsRecWeek() {
        mAdapterDayOfWeek.setOnPickListener(RecAdapterPickedListener(mAdapterDayOfWeek))
    }

    private fun setEventsRecMonth() {
        mAdapterDayOfMonth.setOnPickListener(RecAdapterPickedListener(mAdapterDayOfMonth))
    }

    private fun setEventsRecYear() {
        mIlDayYear.setOnFocusChangeListenerForEditText(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View, hasFocus: Boolean) {
                val et: EditText = v as EditText
                if (!hasFocus) {
                    val dayStr: String = et.text.toString()
                    if (dayStr.isEmpty()) return
                    try {
                        val day: Int = dayStr.toInt()
                        if (day == 0) {
                            et.setText("1")
                        } else if (day >= 28) {
                            et.inputType = EditorInfo.TYPE_CLASS_TEXT
                            et.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(12))
                            mIlDayYear.setTextForEditText(context.getString(R.string.end_of_month))
                        }
                    } catch (e: NumberFormatException) {
                        et.inputType = EditorInfo.TYPE_CLASS_TEXT
                        et.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2))
                        e.printStackTrace()
                    }
                } else {
                    et.inputType = EditorInfo.TYPE_CLASS_NUMBER
                    et.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2))
                }
            }
        })
        mAdapterMonthOfYear.setOnPickListener(RecAdapterPickedListener(mAdapterMonthOfYear))
    }

    inner class RecAdapterPickedListener(
        val mAdapter: RecurrencePickerAdapter
    ) : View.OnClickListener {
        override fun onClick(v: View) {
            mTvTimesLRec.text = "${mAdapter.pickedCount}"
            improveComplex()
            updatePickAllButton(mAdapter)
        }
    }

    private fun updateTimePeriodRec() {
        if (mDtpRec.pickedIndex == 0) {
            mTvSummaryRec.text = ""
            return
        }
        val hourStr: String = mIlHourWmy.textFromEditText
        if (hourStr.isEmpty()) {
            mTvSummaryRec.text = ""
            return
        }
        val hour: Int = hourStr.toInt()
        mTvSummaryRec.setTextColor(black_54p)
        mTvSummaryRec.text = DateTimeUtil.getTimePeriodStr(hour, context.resources)
    }


    private fun updatePickedTimesRec() {
        val count: Int
        val type: Int = mDtpRec.pickedIndex
        count = when (type) {
            0 -> mAdapterTimeOfDay.timeCount
            1 -> mAdapterDayOfWeek.pickedCount
            2 -> mAdapterDayOfMonth.pickedCount
            else -> mAdapterMonthOfYear.pickedCount
        }
        mTvTimesLRec.text = "$count"
        improveComplex()
    }

    @SuppressLint("SetTextI18n")
    private fun improveComplex() {
        if (LocaleUtil.isChinese(context)) return
        val page: Int = mVpDateTime.displayedChild
        if (page == 1) {
            val timeStr: String = mEtTimeAfter.text.toString()
            if (timeStr.isEmpty()) return
            val strs: Array<String> =
                mTvTimeAsBtAfter.text.toString().split(" ").toTypedArray()
            val time: Int
            try {
                time = timeStr.toInt()
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                return
            }
            val length: Int = strs[0].length
            if (time > 1 && strs[0][length - 1] != 's') {
                mTvTimeAsBtAfter.text = "${strs[0]}s ${strs[1]}"
            } else if (time <= 1 && strs[0][length - 1] == 's') {
                mTvTimeAsBtAfter.text = "${strs[0].substring(0, length - 1)} ${strs[1]}"
            }
        } else if (page == 2) {
            val timesStr: String = mTvTimesLRec.text.toString()
            improveComplex(timesStr.toInt(), mTvTimesRRec)
        }
    }

    private fun improveComplex(num: Int, tv: TextView?) {
        if (tv == null) {
            return
        }
        val str: String = tv.text.toString()
        val length: Int = str.length
        if (num > 1 && str[length - 1] != 's') {
            tv.append("s")
        } else if (num <= 1 && str[length - 1] == 's') {
            tv.text = str.substring(0, length - 1)
        }
    }

    private fun endSettingTime() {
        KeyboardUtil.hideKeyboard(mVpDateTime)
        when (mVpDateTime.displayedChild) {
            0 -> endSettingTimeAt()
            1 -> endSettingTimeAfter()
            else -> endSettingTimeRec()
        }
    }

    private fun endSettingTimeAt() {
        val yearStr: String = mEtsAt[0].text.toString()
        try {
            val year: Int = yearStr.toInt()
            if (year > 4600000) {
                setErrorAt(R.string.error_too_late)
                return
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            setErrorAt(R.string.error_too_late)
            return
        }
        val times = IntArray(5)
        var temp: String
        var mayCanConfirm = true
        for (i in times.indices) {
            temp = mEtsAt[i].text.toString()
            if (temp.isEmpty()) {
                times[i] = -1
                mayCanConfirm = false
                break
            } else {
                times[i] = temp.toInt()
            }
        }
        if (mayCanConfirm) {
            val dt = DateTime(times[0], times[1], times[2], times[3], times[4])
            val cur = DateTime()
            if (dt <= cur) {
                setErrorAt(R.string.error_later)
            } else {
                rhParams.reset()
                rhParams.reminderInMillis = dt.millis
                listener.onChangeReminder(
                    rhParams,
                    DateTimeUtil.getDateTimeStrAt(dt, context, false)
                )
                popupWindow.dismiss()
            }
        } else {
            setErrorAt(R.string.error_complete_time)
        }
    }

    private fun setErrorAt(@StringRes textRes: Int) {
        mTvSummaryAt.setTextColor(ContextCompat.getColor(context, R.color.colorError))
        mTvSummaryAt.text = context.getString(textRes)
    }

    private fun endSettingTimeAfter() {
        val timeStr: String = mEtTimeAfter.text.toString()
        if (timeStr.isEmpty()) {
            mTvErrorAfter.setText(R.string.error_complete_time)
        } else {
            val time: Int
            try {
                time = timeStr.toInt()
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                mTvErrorAfter.setText(R.string.error_number_too_big)
                return
            }
            if (time == 0) {
                mTvErrorAfter.setText(R.string.error_later)
            } else {
                val type: Int = mDtpAfter.pickedTimeType
                if (((time > 4600000 && type == Calendar.YEAR) ||
                            (time > 4600000 * 12 && type == Calendar.MONTH) ||
                            (time > 4600000 * 53 && type == Calendar.WEEK_OF_YEAR) ||
                            (time > 4600000 * 365 && type == Calendar.DATE))
                ) {
                    mTvErrorAfter.setText(R.string.error_too_late)
                    return
                }
                rhParams.reset()
                rhParams.reminderAfterTime = intArrayOf(type, time)
                listener.onChangeReminder(
                    rhParams,
                    DateTimeUtil.getDateTimeStrAfter(type, time, context)
                )
                popupWindow.dismiss()
            }
        }
    }

    private fun endSettingTimeRec() {
        val canConfirm: String = checkCanConfirmRec()
        if ((NO_PROBLEM == canConfirm)) {
            val type: Int = mDtpRec.pickedTimeType
            var detail: String? = ""
            if (type == Calendar.DATE) {
                detail =
                    Habit.generateDetailTimeOfDay(mAdapterTimeOfDay.finalItems)
            } else {
                val hour: Int = mIlHourWmy.textFromEditText.toInt()
                val minute: Int = mIlMinuteWmy.textFromEditText.toInt()
                when (type) {
                    Calendar.WEEK_OF_YEAR -> {
                        val days: List<Int> = mAdapterDayOfWeek.pickedIndexes
                        detail = Habit.generateDetailDayOf(days, hour, minute)
                    }
                    Calendar.MONTH -> {
                        val days: List<Int> = mAdapterDayOfMonth.pickedIndexes
                        detail = Habit.generateDetailDayOf(days, hour, minute)
                    }
                    Calendar.YEAR -> {
                        val months: List<Int> = mAdapterMonthOfYear.pickedIndexes
                        val day: Int = try {
                            mIlDayYear.textFromEditText.toInt()
                        } catch (e: NumberFormatException) {
                            28
                        }
                        detail = Habit.generateDetailMonthOfYear(months, day, hour, minute)
                    }
                }
            }
            rhParams.reset()
            rhParams.habitType = type
            rhParams.habitDetail = detail
            listener.onChangeReminder(
                rhParams,
                DateTimeUtil.getDateTimeStrRec(context, type, detail)
            )
            popupWindow.dismiss()
        } else {
            mTvSummaryRec.setTextColor(ContextCompat.getColor(context, R.color.colorError))
            mTvSummaryRec.text = canConfirm
        }
    }

    private fun formatMinuteAt() {
        val temp: String = mEtsAt[4].text.toString()
        if (temp.length == 1) {
            mEtsAt[4].setText("0$temp")
        }
    }

    private fun updateSummaryAt(year: Int, month: Int, day: Int, hour: Int) {
        if (year > 4600000) {
            setErrorAt(R.string.error_too_late)
            return
        }
        mTvSummaryAt.setTextColor(black_54p)
        val sb: StringBuilder = StringBuilder()
        if ((year != -1) && (month != -1) && (day != -1)) {
            val dt: DateTime =
                DateTime().withYear(year).withMonthOfYear(month).withDayOfMonth(day)
            var dayOfWeek: Int = dt.dayOfWeek
            dayOfWeek = if (dayOfWeek == 7) 1 else dayOfWeek + 1
            sb.append(context.resources.getStringArray(R.array.day_of_week)[dayOfWeek - 1])
            if (hour != -1) {
                sb.append(", ")
            }
        }
        if (hour != -1) {
            var period: String = DateTimeUtil.getTimePeriodStr(hour, context.resources)
            if (((year == -1) || (month == -1) || (day == -1)) && !LocaleUtil.isChinese(context)) {
                val temp: String = period.substring(0, 1).toUpperCase()
                period = temp + period.substring(1, period.length)
            }
            sb.append(period)
        }
        mTvSummaryAt.text = sb.toString()
    }

    private fun checkCanConfirmRec(): String {
        return when (mDtpRec.pickedIndex) {
            0 -> checkCanConfirmRecDay()
            1 -> checkCanConfirmRecWeek()
            2 -> checkCanConfirmRecMonth()
            else -> checkCanConfirmRecYear()
        }
    }

    private fun checkCanConfirmRecDay(): String {
        val times: List<Int> = mAdapterTimeOfDay.finalItems
        if (times.isEmpty()) {
            return context.getString(R.string.error_complete_time)
        }
        if (times.contains(-1)) {
            return context.getString(R.string.error_complete_time)
        } else {
            val set: HashSet<String> = HashSet()
            var i = 0
            while (i < times.size) {
                val time: String = times[i].toString() + ":" + times[i + 1]
                if (!set.add(time)) {
                    return context.getString(R.string.error_different)
                }
                i += 2
            }
        }
        return NO_PROBLEM
    }

    private fun isHourMinuteWmyOK(): Boolean {
        return mIlHourWmy.textFromEditText.isNotEmpty()
                && mIlMinuteWmy.textFromEditText.isNotEmpty()
    }

    private fun checkCanConfirmRecWeek(): String {
        if (!isHourMinuteWmyOK() || mAdapterDayOfWeek.pickedCount == 0) {
            return context.getString(R.string.error_complete_time)
        }
        return NO_PROBLEM
    }

    private fun checkCanConfirmRecMonth(): String {
        if (!isHourMinuteWmyOK() || mAdapterDayOfMonth.pickedCount == 0) {
            return context.getString(R.string.error_complete_time)
        }
        return NO_PROBLEM
    }

    private fun checkCanConfirmRecYear(): String {
        if (!isHourMinuteWmyOK()
            || mIlDayYear.textFromEditText.isEmpty()
            || (mAdapterMonthOfYear.pickedCount == 0)
        ) {
            return context.getString(R.string.error_complete_time)
        }
        return NO_PROBLEM
    }

    init {
        popupWindow = PopupWindow(
            view,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        )
        popupWindow.animationStyle = R.style.popup_window_animation
        popupWindow.showAtLocation(parent, 17, 0, 0)
        popupWindow.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        val root: View = view.findViewById(R.id.root)
        root.setOnClickListener { popupWindow.dismiss() }
        mVpDateTime = f(root, R.id.vp_date_time)
        val card: View = f(root, R.id.card)
        card.setOnClickListener { }

        findViews()
        initUI()
        setEvents()
        if (rhParams.habitDetail != null) {
            initAll(2)
        } else {
            initAll(0)
        }
    }

}
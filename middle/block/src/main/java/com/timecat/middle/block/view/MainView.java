package com.timecat.middle.block.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.timecat.extend.arms.BaseApplication;
import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.component.identity.Attr;
import com.timecat.middle.block.R;
import com.timecat.middle.block.aggreement.CheckListAdapter;
import com.timecat.middle.block.aggreement.ContentAgreement;
import com.timecat.middle.block.temp.Def;
import com.timecat.middle.block.temp.FrequentSettings;
import com.timecat.middle.block.util.DeviceUtil;
import com.timecat.middle.block.util.DisplayUtil;
import com.timecat.middle.block.util.KeyboardUtil;
import com.timecat.middle.block.util.LineSpacingHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/9/1
 * @description null
 * @usage null
 */
public class MainView extends LinearLayout {

    public MainView(Context context) {
        super(context);
        init(context);
    }

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MainView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private EditText mEtContent;
    private RecyclerView mRvCheckList;
    private LinearLayout mLlMoveChecklist;
    private TextView mTvMoveChecklistAsBt;

    private CheckListAdapter mCheckListAdapter;
    private CannotScrollLinearLayoutManager mLlmCheckList;
    private ItemTouchHelper mChecklistTouchHelper;

    private boolean mEditable = true;
    private boolean mShouldAutoLink = true;
    private boolean shouldAddToActionList = true;

    private static float density;

    private static int appAccent;
    private static int cursorWidth;
    private static int normalLineCursorHeightVary;
    private static int lastLineCursorHeightVary;

    static {
        Context context = BaseApplication.getContext();
        density = DisplayUtil.getScreenDensity(context);

        appAccent = Attr.getAccentColor(context);
        cursorWidth = (int) (3 * density);
        normalLineCursorHeightVary = (int) (0 * density);
        if (DeviceUtil.hasLollipopApi()) {
            lastLineCursorHeightVary = (int) (0 * density);
        } else {
            lastLineCursorHeightVary = normalLineCursorHeightVary;
        }
    }

    public static final int CREATE = 0;
    public static final int UPDATE = 1;
    private int mType = UPDATE;

    private TextWatcher tw;
    private Listener listener;

    public interface Listener {
        void onClickUrl(String url);

        void addAction(@NonNull ThingAction action);

        void hindKeyboard();

        void clearFocus();

        void toggleModeButton(boolean isCheckList);

        void requestSaveModel(@NonNull String content);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void init(Context context) {
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        inflate(context, R.layout.view_main_content, this);
        //region init members
        initAutoLink();
        //endregion

        //region ui
        mEtContent = findViewById(R.id.et_content);
        mRvCheckList = findViewById(R.id.rv_check_list);
        mRvCheckList.setItemAnimator(null);
        mRvCheckList.setNestedScrollingEnabled(false);
        mLlMoveChecklist = findViewById(R.id.ll_move_checklist);
        mTvMoveChecklistAsBt = findViewById(R.id.tv_move_checklist_as_bt);
        //endregion

        if (mShouldAutoLink) {
            mEtContent.setOnTouchListener(mSpannableTouchListener);
            mEtContent.setOnClickListener(mEtContentClickListener);
            mEtContent.setOnLongClickListener(mEtContentLongClickListener);
        }

        if (mEditable) {
            LineSpacingHelper.helpCorrectSpacingForNewLine(mEtContent);
            Flowable.create(new FlowableOnSubscribe<String>() {
                @Override
                public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                    tw = new ActionTextWatcher(emitter, ThingAction.UPDATE_CONTENT);
                    mEtContent.addTextChangedListener(tw);
                }
            }, BackpressureStrategy.LATEST)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            if (listener != null) {
                                listener.requestSaveModel(getRealContent());
                            }
                        }
                    });
        }
    }

    public void setAppAccentColor(@ColorInt int appAccent) {
        if (mEditable) {
            DisplayUtil.setSelectionHandlersColor(mEtContent, appAccent);

            if (!DeviceUtil.isFlyme()) {
                LineSpacingHelper.setTextCursorDrawable(
                        mEtContent, appAccent, cursorWidth,
                        normalLineCursorHeightVary, lastLineCursorHeightVary);
            }
        }
        CheckListAdapter.setAppAccent(appAccent);
    }

    public void setEditable(boolean editable) {
        mEditable = editable;
        shouldAddToActionList = mEditable;

        if (!mEditable) {
            mEtContent.setKeyListener(null);
        }
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public EditText getmEtContent() {
        return mEtContent;
    }

    public void setContent(Context context, String content) {
        setContent(context, content, false);
    }

    public void setContent(Context context, String content, boolean isFinish) {
        if (mShouldAutoLink) {
            mEtContent.setAutoLinkMask(Linkify.ALL);
        } else {
            mEtContent.setAutoLinkMask(0);
        }
        if (mType == CREATE) {
            mEtContent.requestFocus();
            mEtContent.removeTextChangedListener(tw);
            mEtContent.setText(content);
            mEtContent.addTextChangedListener(tw);
            mEtContent.setSelection(content.length());
            if (mCheckListAdapter != null) {
                mCheckListAdapter.setItems(new ArrayList<>());
            }
        } else {
            if (ContentAgreement.isCheckListStr(content)) {
                mEtContent.setVisibility(View.GONE);
                mRvCheckList.setVisibility(View.VISIBLE);
                if (mEditable) {
                    mLlMoveChecklist.setVisibility(View.VISIBLE);
                }

                List<String> items = ContentAgreement.toCheckListItems(content, false);
                if (!mEditable) {
                    items.remove("2");
                    if (isFinish) {
                        items.remove("3");
                        items.remove("4");
                    } else if (items.get(0).equals("2")) {
                        items.remove("3");
                        items.remove("4");
                    }
                    mCheckListAdapter = new CheckListAdapter(context, CheckListAdapter.EDITTEXT_UNEDITABLE, items);
                } else {
                    initEditableCheckList(items);
                }
                mCheckListAdapter.setShouldAutoLink(mShouldAutoLink);
                setChecklistExpandShrinkEvent();

                setMoveChecklistEvent();

                mLlmCheckList = new CannotScrollLinearLayoutManager(context);
                mRvCheckList.setAdapter(mCheckListAdapter);
                mRvCheckList.setLayoutManager(mLlmCheckList);
                if (mEditable) {
                    mCheckListAdapter.setExpanded(false);
                    mRvCheckList.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            expandOrShrinkChecklistFinishedItems(
                                    false, mCheckListAdapter.getItems(), false);
                            ViewTreeObserver observer = mRvCheckList.getViewTreeObserver();
                            if (observer.isAlive()) {
                                observer.removeOnPreDrawListener(this);
                            }
                            return true;
                        }
                    });
                }
            } else {
                if (mCheckListAdapter != null) {
                    mCheckListAdapter.setItems(new ArrayList<>());
                }
                mEtContent.setVisibility(View.VISIBLE);
                mEtContent.removeTextChangedListener(tw);
                mEtContent.setText(content);
                mEtContent.addTextChangedListener(tw);
            }
        }
    }

    private void setChecklistExpandShrinkEvent() {
        mCheckListAdapter.setExpandShrinkCallback(new CheckListAdapter.ExpandShrinkCallback() {
            @Override
            public void updateChecklistHeight(
                    boolean expand, List<String> items, boolean isClickingExpandOrShrink) {
                expandOrShrinkChecklistFinishedItems(expand, items, isClickingExpandOrShrink);
            }
        });
    }

    private void expandOrShrinkChecklistFinishedItems(
            boolean expand, List<String> items, boolean isClickingExpandOrShrink) {
        if (isClickingExpandOrShrink) {
            if (listener != null) {
                listener.clearFocus();
            }
        }
        ViewGroup.LayoutParams vlp = mRvCheckList.getLayoutParams();
        vlp.height = getChecklistItemsHeight(expand, items);
        mRvCheckList.requestLayout();
    }

    /**
     * 这里的计算不正确，会导致少一行
     *
     * @param expand
     * @param items
     * @return
     */
    private int getChecklistItemsHeight(boolean expand, List<String> items) {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
//        if (expand) {
//            return ViewGroup.LayoutParams.WRAP_CONTENT;
//        }
//        int unfinishedHeight = 0;
//        for (int i = 0; i < items.size(); i++) {
//            String item = items.get(i);
//            char state = item.charAt(0);
//            if (state != '1') {
//                RecyclerView.ViewHolder holder = mRvCheckList.findViewHolderForAdapterPosition(i);
//                if (holder != null) {
//                    unfinishedHeight += holder.itemView.getHeight();
//                }
//                /*
//                    if user inserts a new item and if the checklist finished items are shrank,
//                    the "finished" item will disappear temporarily, thus the holder will be
//                    null. But we still need the height of that item, so we recorded its height
//                    at the beginning and add it now because its height never change.
//                 */
//                else if (state == '3') {
//                    unfinishedHeight += 48 * screenDensity;
//                } else if (state == '4') {
//                    unfinishedHeight += 36 * screenDensity;
//                }
//            } else break;
//        }
//        return unfinishedHeight;
    }

    private void setMoveChecklistEvent() {
        if (!mEditable) return;

        if (mChecklistTouchHelper == null) {
            mChecklistTouchHelper = new ItemTouchHelper(new CheckListTouchCallback());
        }

        mTvMoveChecklistAsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDragging = mCheckListAdapter.isDragging();
                if (!isDragging) {
                    mTvMoveChecklistAsBt.setText(R.string.act_back_from_move_checklist);
                    if (DeviceUtil.hasJellyBeanMR1Api()) {
                        mTvMoveChecklistAsBt.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                R.drawable.act_back_from_move_checklist, 0, 0, 0);
                    } else {
                        mTvMoveChecklistAsBt.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.act_back_from_move_checklist, 0, 0, 0);
                    }
                    mCheckListAdapter.setDragging(true);
                    mChecklistTouchHelper.attachToRecyclerView(mRvCheckList);
                } else {
                    mTvMoveChecklistAsBt.setText(R.string.act_move_check_list);
                    if (DeviceUtil.hasJellyBeanMR1Api()) {
                        mTvMoveChecklistAsBt.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                R.drawable.act_move_checklist, 0, 0, 0);
                    } else {
                        mTvMoveChecklistAsBt.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.act_move_checklist, 0, 0, 0);
                    }
                    mCheckListAdapter.setDragging(false);
                    mChecklistTouchHelper.attachToRecyclerView(null);
                }
                mCheckListAdapter.notifyDataSetChanged();
            }
        });

        mCheckListAdapter.setIvStateTouchCallback(new CheckListAdapter.IvStateTouchCallback() {
            @Override
            public void onTouch(int pos) {
                mChecklistTouchHelper.startDrag(mRvCheckList.findViewHolderForAdapterPosition(pos));
            }
        });
    }

    public String finishCheckList(String content) {
        return content.replaceAll(ContentAgreement.SIGNAL + "1",
                ContentAgreement.SIGNAL + "0");
    }

    public boolean isEmpty() {
        boolean canShare = true;
        if (mEtContent.getVisibility() == View.VISIBLE) {
            if (mEtContent.getText().toString().isEmpty()) {
                canShare = false;
            }
        } else if (mRvCheckList.getVisibility() == View.VISIBLE) {
            if (mEditable && mCheckListAdapter.getItemCount() == 1) {
                canShare = false;
            }
        }
        return canShare;
    }

    /**
     * 初始化可编辑的清单 adapter
     *
     * @param items 初始化用到的内容 items
     */
    public void initEditableCheckList(List<String> items) {
        mCheckListAdapter = new CheckListAdapter(getContext(), CheckListAdapter.EDITTEXT_EDITABLE, items);
        mCheckListAdapter.setRequestSave(new CheckListAdapter.RequestSave() {
            @Override
            public void requestSave() {
                if (listener != null) {
                    listener.requestSaveModel(getRealContent());
                }
            }
        });
        if (mShouldAutoLink) {
            mCheckListAdapter.setEtTouchListener(mSpannableTouchListener);
            mCheckListAdapter.setEtClickListener(mEtContentClickListener);
            mCheckListAdapter.setEtLongClickListener(mEtContentLongClickListener);
        }
        mCheckListAdapter.setItemsChangeCallback(new CheckListItemsChangeCallback());
        mCheckListAdapter.setActionCallback(new CheckListActionCallback());
    }

    /**
     * @return 复制分享用的 content，已美化
     */
    public String copyContent() {
        String content = "";
        if (mEtContent.getVisibility() == View.VISIBLE) {
            content = mEtContent.getText().toString();
        } else if (mRvCheckList != null && mRvCheckList.getVisibility() == View.VISIBLE
                && mCheckListAdapter != null) {
            content = ContentAgreement.toContentStr(
                    ContentAgreement.toCheckListStr(mCheckListAdapter.getItems()), "X  ", "√  ");
        }
        return content;
    }

    /**
     * @return 原始的content
     */
    public String getRealContent() {
        String content = "";
        if (mEtContent.getVisibility() == View.VISIBLE) {
            content = mEtContent.getText().toString();
        } else if (mRvCheckList != null
                && mRvCheckList.getVisibility() == View.VISIBLE
                && mCheckListAdapter != null) {
            content = ContentAgreement.toCheckListStr(mCheckListAdapter.getItems());
        }
        return content;
    }

    public void undoOrRedoContent(String to, int cursorPos) {
        shouldAddToActionList = false;
        mEtContent.setText(to);
        mEtContent.setSelection(cursorPos);
        shouldAddToActionList = false;
    }

    public void undoOrRedoToggleCheckList(String from, boolean undo) {
        shouldAddToActionList = false;
        toggleCheckListMode();
        if (ContentAgreement.isCheckListStr(from) && undo) {
            mCheckListAdapter.setItems(ContentAgreement.toCheckListItems(from, false));
        }
        shouldAddToActionList = true;
    }

    public void undoOrRedoMoveCheckList(int from, int to) {
        shouldAddToActionList = false;
        moveChecklist(from, to);
        shouldAddToActionList = true;
    }

    public void undoOrRedoUpdateCheckList(String from) {
        shouldAddToActionList = false;
        mCheckListAdapter.setItems(ContentAgreement.toCheckListItems(from, false));
        if (!mCheckListAdapter.isExpanded()) {
            getmEtContent().post(new Runnable() {
                @Override
                public void run() {
                    expandOrShrinkChecklistFinishedItems(
                            false, mCheckListAdapter.getItems(), false
                    );
                }
            });
        }
        shouldAddToActionList = true;
    }

    public boolean isCheckListMode() {
        return mRvCheckList.getVisibility() == View.VISIBLE;
    }

    /**
     * 文本编辑模式
     */
    public void onEditContentMode() {
        toggleModeButton(false);
        mEtContent.setVisibility(View.VISIBLE);
        mRvCheckList.setVisibility(View.GONE);
        if (mLlMoveChecklist != null) {
            // don't know why this is possible but some user's log showed that this can happen
            mLlMoveChecklist.setVisibility(View.GONE);
        }
        mChecklistTouchHelper.attachToRecyclerView(null);

        String contentStr = ContentAgreement.toContentStr(mCheckListAdapter.getItems());
        boolean temp = shouldAddToActionList;
        shouldAddToActionList = false;
        mEtContent.setText(contentStr);
        shouldAddToActionList = temp;

        if (contentStr.isEmpty()) {
            KeyboardUtil.showKeyboard(mEtContent);
        } else {
            if (listener != null) {
                listener.hindKeyboard();
            }
        }
    }

    /**
     * 清单模式
     */
    public void onCheckListMode() {
        toggleModeButton(true);
        mEtContent.setVisibility(View.GONE);
        mRvCheckList.setVisibility(View.VISIBLE);
        if (mLlmCheckList != null) {
            mLlMoveChecklist.setVisibility(View.VISIBLE);
        }

        String content = mEtContent.getText().toString();
        List<String> items = ContentAgreement.toCheckListItems(content, true);
        boolean focusFirst = false;
        if (items.size() == 2 && items.get(0).equals("0")) {
            focusFirst = true;
        }

        if (mCheckListAdapter == null) {
            initEditableCheckList(items);
            mLlmCheckList = new CannotScrollLinearLayoutManager(getContext());
        } else {
            mCheckListAdapter.setItems(items);
        }
        mCheckListAdapter.setShouldAutoLink(mShouldAutoLink);
        setChecklistExpandShrinkEvent();
        mRvCheckList.setAdapter(mCheckListAdapter);
        mRvCheckList.setLayoutManager(mLlmCheckList);

        setMoveChecklistEvent();

        if (focusFirst) {
            mRvCheckList.post(() -> {
                CheckListAdapter.EditTextHolder holder = (CheckListAdapter.EditTextHolder)
                        mRvCheckList.findViewHolderForAdapterPosition(0);
                if (holder == null) return;
                KeyboardUtil.showKeyboard(holder.et);
            });
        } else {
            if (listener != null) {
                listener.hindKeyboard();
            }
        }
    }

    public String toggleCheckListMode() {
        shouldAddToActionList = false;
        String before;
        if (isCheckListMode()) {
            before = ContentAgreement.toCheckListStr(mCheckListAdapter.getItems());
            onEditContentMode();
        } else {
            before = mEtContent.getText().toString();
            onCheckListMode();
        }
        shouldAddToActionList = true;
        return before;
    }

    public void toggleCheckList() {
        LogUtil.se("shouldAddToActionList = " + shouldAddToActionList);
        String before = toggleCheckListMode();
        LogUtil.se("shouldAddToActionList = " + shouldAddToActionList);
        if (shouldAddToActionList) {
            if (listener != null) {
                listener.addAction(new ThingAction(ThingAction.TOGGLE_CHECKLIST, before, null));
            }
        }
        LogUtil.se("shouldAddToActionList = " + shouldAddToActionList);
        shouldAddToActionList= true;
    }

    private void toggleModeButton(boolean isCheckList) {
        if (listener != null) {
            listener.toggleModeButton(isCheckList);
        }
    }

    //region auto link
    /**
     * This {@link View.OnTouchListener} will listen to click events that should
     * be handled by link/phoneNum/email/maps in mEtContent and other {@link EditText}s
     * so that we can handle them with different intents and not lose ability to edit them.
     */
    View.OnTouchListener mSpannableTouchListener;
    private View.OnClickListener mEtContentClickListener;
    private View.OnLongClickListener mEtContentLongClickListener;

    private HashMap<View, Integer> mTouchMovedCountMap;
    private HashMap<View, Boolean> mOnLongClickedMap;

    private void initAutoLink() {
        mShouldAutoLink = FrequentSettings.getBoolean(Def.Meta.KEY_AUTO_LINK);
        if (mShouldAutoLink) {
            mTouchMovedCountMap = new HashMap<>();
            mOnLongClickedMap = new HashMap<>();
            mSpannableTouchListener = new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    Boolean onLongClicked = mOnLongClickedMap.get(v);
                    if (onLongClicked != null && onLongClicked) {
                        return false;
                    }

                    if (action == MotionEvent.ACTION_UP) {
                        Integer touchMovedCount = mTouchMovedCountMap.get(v);
                        if (touchMovedCount != null && touchMovedCount >= 3) {
                            mTouchMovedCountMap.put(v, 0);
                            return false;
                        }

                        final EditText et = (EditText) v;
                        final Spannable sContent = Spannable.Factory.getInstance()
                                .newSpannable(et.getText());

                        int x = (int) event.getX();
                        int y = (int) event.getY();

                        x -= et.getTotalPaddingLeft();
                        y -= et.getTotalPaddingTop();
                        x += et.getScrollX();
                        y += et.getScrollY();

                        Layout layout = et.getLayout();
                        int line = layout.getLineForVertical(y);
                        int offset = layout.getOffsetForHorizontal(line, x);

                        // place cursor of EditText to correct position.
                        et.requestFocus();
                        if (offset > 0) {
                            if (x > layout.getLineMax(line)) {
                                et.setSelection(offset);
                            } else et.setSelection(offset - 1);
                        }

                        ClickableSpan[] link = sContent.getSpans(offset, offset, ClickableSpan.class);
                        if (link.length != 0) {
                            final URLSpan urlSpan = (URLSpan) link[0];

                            if (!mEditable) {
                                urlSpan.onClick(et);
                                return true;
                            }

                            String url = urlSpan.getURL();
                            if (listener != null) {
                                listener.onClickUrl(url);
                            }
                            return true;
                        }
                    } else if (action == MotionEvent.ACTION_MOVE) {
                        Integer touchMovedCount = mTouchMovedCountMap.get(v);
                        mTouchMovedCountMap.put(v, touchMovedCount == null ? 1 : touchMovedCount + 1);
                    }
                    return false;
                }
            };
            mEtContentClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTouchMovedCountMap.put(v, 0);
                    mOnLongClickedMap.put(v, false);
                }
            };
            mEtContentLongClickListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mTouchMovedCountMap.put(v, 0);
                    mOnLongClickedMap.put(v, true);
                    return false;
                }
            };
        }
    }
    //endregion

    //region inner class
    private class CannotScrollLinearLayoutManager extends LinearLayoutManager {

        CannotScrollLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }

    private class ActionTextWatcher extends FlowableTextWatcher {

        private int mActionType;

        private String mBefore;
        private int mCursorPosBefore;

        private EditText mEditText;

        ActionTextWatcher(@NonNull FlowableEmitter<String> emitter, int actionType) {
            super(emitter);
            mActionType = actionType;
            mEditText = mEtContent;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            mBefore = s.toString();
            mCursorPosBefore = mEditText.getSelectionEnd();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            emitter.onNext(s.toString());
            if (shouldAddToActionList) {
                ThingAction action = new ThingAction(mActionType, mBefore, s.toString());
                action.getExtras().putInt(ThingAction.KEY_CURSOR_POS_BEFORE, mCursorPosBefore);
                action.getExtras().putInt(ThingAction.KEY_CURSOR_POS_AFTER, mEditText.getSelectionStart());
                if (listener != null) {
                    listener.addAction(action);
                }
            }
        }
    }

    private class CheckListActionCallback implements CheckListAdapter.ActionCallback {

        @Override
        public void onAction(String before, String after) {
            if (shouldAddToActionList) {
                ThingAction action = new ThingAction(ThingAction.UPDATE_CHECKLIST, before, after);
                if (listener != null) {
                    listener.addAction(action);
                }
            }
        }
    }

    private class CheckListItemsChangeCallback implements CheckListAdapter.ItemsChangeCallback {

        @Override
        public void onInsert(int position) {
            CheckListAdapter.EditTextHolder holder = (CheckListAdapter.EditTextHolder)
                    mRvCheckList.findViewHolderForAdapterPosition(position);
            if (holder != null) {
                KeyboardUtil.showKeyboard(holder.et);
            }
        }

        @Override
        public void onRemove(int position, String item, int cursorPos) {
            if (item == null) {
                CheckListAdapter.EditTextHolder holder = (CheckListAdapter.EditTextHolder)
                        mRvCheckList.findViewHolderForAdapterPosition(position);
                if (holder == null) return;
                if (position != -1) {
                    holder.et.requestFocus();
                    holder.et.setSelection(cursorPos);
                } else {
                    if (listener != null) {
                        listener.hindKeyboard();
                    }
                }
            } else {
                if (listener != null) {
                    listener.hindKeyboard();
                }
            }
        }
    }

    private boolean moveChecklist(int from, int to) {
        List<String> items = mCheckListAdapter.getItems();
        int pos2 = items.indexOf("2");
        int fromPos2 = from - pos2;
        int toPos2 = to - pos2;
        if (fromPos2 * toPos2 <= 0) {
            return false;
        }

        int pos3 = items.indexOf("3");
        if (pos3 != -1) { // there are finished items
            int pos4 = pos3 + 1;
            if ((from <= pos3 && to >= pos3) || (from >= pos3 && to <= pos3)) {
                return false;
            }
            if ((from <= pos4 && to >= pos4) || (from >= pos4 && to <= pos4)) {
                return false;
            }
        }

        String item = items.remove(from);
        items.add(to, item);
        mCheckListAdapter.notifyItemMoved(from, to);

        if (shouldAddToActionList) {
            ThingAction action = new ThingAction(ThingAction.MOVE_CHECKLIST, from, to);
            if (listener != null) {
                listener.addAction(action);
            }
        }

        return true;
    }

    private class CheckListTouchCallback extends ItemTouchHelper.Callback {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            return makeMovementFlags(dragFlags, 0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            final int from = viewHolder.getAdapterPosition();
            final int to = target.getAdapterPosition();
            return moveChecklist(from, to);
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }
    }
    //endregion
}

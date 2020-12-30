package com.timecat.middle.block.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.timecat.middle.block.R;
import com.timecat.middle.block.base.BaseViewHolder;

import java.util.List;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/1/25
 * @description null
 * @usage null
 */
public class AttachmentInfoDialogFragment extends BaseDialogFragment {

    public static final String TAG = "AttachmentInfoDialogFragment";

    private int mAccentColor;
    private List<Pair<String, String>> mItems;

    private LayoutInflater mInflater;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_attachment_info;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Activity activity = getActivity();
        mInflater = LayoutInflater.from(activity);

        TextView title = f(R.id.tv_title_attachment_info);
        title.setTextColor(mAccentColor);
        TextView confirm = f(R.id.tv_confirm_as_bt_attachment_info);
        confirm.setTextColor(mAccentColor);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        RecyclerView recyclerView = f(R.id.rv_attachment_info);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(new Adapter());

        return mContentView;
    }

    public void setAccentColor(int accentColor) {
        mAccentColor = accentColor;
    }

    public void setItems(List<Pair<String, String>> items) {
        mItems = items;
    }

    class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(mInflater.inflate(R.layout.rv_attachment_info, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Pair<String, String> item = mItems.get(position);
            holder.tvTitle.setText(item.first);
            holder.tvContent.setText(item.second);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        class Holder extends BaseViewHolder {

            final TextView tvTitle;
            final TextView tvContent;

            Holder(View itemView) {
                super(itemView);

                tvTitle = f(R.id.tv_rv_attachment_info_title);
                tvContent = f(R.id.tv_rv_attachment_info_content);
            }
        }

    }
}

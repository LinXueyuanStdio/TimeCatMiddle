package com.timecat.middle.block.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;

/**
 * Created by ywwynm on 2015/9/29. A subclass of {@link DialogFragment} without dialog title
 */
public abstract class BaseDialogFragment extends DialogFragment {

    protected View mContentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(),
                getActivity().getTheme());
        LayoutInflater themeAwareInflater = inflater.cloneInContext(contextThemeWrapper);
        mContentView = themeAwareInflater.inflate(getLayoutResource(), container, false);
        return mContentView;
    }

    protected abstract @LayoutRes
    int getLayoutResource();

    @SuppressWarnings("unchecked")
    protected final <T extends View> T f(View view, @IdRes int id) {
        return (T) view.findViewById(id);
    }

    protected final <T extends View> T f(@IdRes int id) {
        return f(mContentView, id);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!isAdded()) {
            try {
                super.show(manager, tag);
            } catch (IllegalStateException ignored) {
                // ignore this
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismissAllowingStateLoss();
    }
}

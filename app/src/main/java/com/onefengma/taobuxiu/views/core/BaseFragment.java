package com.onefengma.taobuxiu.views.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.onefengma.taobuxiu.model.EventBusHelper;

/**
 * Created by chufengma on 16/8/7.
 */
public class BaseFragment extends Fragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyToolbarShadow(view);
    }

    private void applyToolbarShadow(View view) {
//        if (view.findViewById(R.id.toolbar) != null) {
//            try {
//                View shadow = new View(getContext());
//                shadow.setBackgroundResource(R.drawable.tool_bar_shadow);
//                ViewGroup.LayoutParams layoutParams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewUtils.dipToPixels(4));
//                shadow.
//            } catch (Exception e) {
//                // do nothing
//            }
//        }
    }

}

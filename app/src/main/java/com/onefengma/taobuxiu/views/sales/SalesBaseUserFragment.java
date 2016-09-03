package com.onefengma.taobuxiu.views.sales;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.views.core.BaseFragment;
import com.onefengma.taobuxiu.views.widgets.listview.XListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yfchu
 * @date 2016/9/2
 */
public abstract class SalesBaseUserFragment extends BaseFragment implements TextWatcher {

    @BindView(R.id.search_bar)
    EditText searchBar;
    @BindView(R.id.list)
    XListView list;
    @BindView(R.id.emptyView)
    TextView emptyView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slaes_normal_user, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list.setAdapter(getAdapter());
        list.enablePullRefresh(false);
        list.setEmptyView(emptyView);
        searchBar.addTextChangedListener(this);
    }

    @OnClick(R.id.emptyView)
    public void onEmptyViewClick() {
        searchBar.setText("");
        doSearch("");
    }

    public abstract void doSearch(String words);

    public abstract ListAdapter getAdapter();

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }


    @Override
    public void onTextChanged(final CharSequence s, int start, int before, int count) {
        final String words = s.toString();
        searchBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (StringUtils.equals(searchBar.getText().toString(), words)) {
                    doSearch(words);
                }
            }
        }, 500);
    }


    @Override
    public void afterTextChanged(Editable s) {

    }


}

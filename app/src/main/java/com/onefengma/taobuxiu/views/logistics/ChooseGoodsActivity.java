package com.onefengma.taobuxiu.views.logistics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.GoodsCategory;
import com.onefengma.taobuxiu.model.entities.Contact;
import com.onefengma.taobuxiu.model.events.logistics.GoodsChooseEvent;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseGoodsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar toolbar;
    @BindView(R.id.title_list)
    RecyclerView titleList;
    @BindView(R.id.content_list)
    RecyclerView contentList;

    TitleAdapter titleAdapter;
    ContentAdapter contentAdapter;

    public static void start(Activity activity, String requestId) {
        Intent intent = new Intent(activity, ChooseGoodsActivity.class);
        intent.putExtra("requestId", requestId);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_goods);
        ButterKnife.bind(this);

        titleAdapter = new TitleAdapter(GoodsCategory.getTitle());
        contentAdapter = new ContentAdapter(GoodsCategory.getContent(0));

        titleList.setLayoutManager(new LinearLayoutManager(this));
        titleList.setAdapter(titleAdapter);

        contentList.setLayoutManager(new LinearLayoutManager(this));
        contentList.setAdapter(contentAdapter);
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public TextViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
        }

    }

    public class TitleAdapter extends RecyclerView.Adapter<TextViewHolder> {

        List<String> titles = new ArrayList<>();
        int currentSelectedIndex = 0;

        public TitleAdapter(List<String> titles) {
            this.titles.addAll(titles);
        }

        @Override
        public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_title, parent, false));
        }

        @Override
        public void onBindViewHolder(final TextViewHolder holder, final int position) {
            holder.name.setText(titles.get(position));
            holder.itemView.setSelected(position == currentSelectedIndex);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentSelectedIndex = position;
                    notifyDataSetChanged();
                    contentAdapter.setTitles(GoodsCategory.getContent(titles.get(position)));
                }
            });
        }

        @Override
        public int getItemCount() {
            return titles.size();
        }

        public String getCurrentTitle() {
            return titles.get(currentSelectedIndex);
        }

    }

    public class ContentAdapter extends RecyclerView.Adapter<TextViewHolder> {

        List<String> titles = new ArrayList<>();

        public ContentAdapter(List<String> titles) {
            this.titles.addAll(titles);
        }

        public void setTitles(List<String> titles) {
            this.titles = titles;
            notifyDataSetChanged();
        }

        @Override
        public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_content, parent, false));
        }

        @Override
        public void onBindViewHolder(final TextViewHolder holder, final int position) {
            holder.name.setText(titles.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBusHelper.post(new GoodsChooseEvent(getIntent().getStringExtra("requestId"), titleAdapter.getCurrentTitle(), titles.get(position)));
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return titles.size();
        }
    }

}

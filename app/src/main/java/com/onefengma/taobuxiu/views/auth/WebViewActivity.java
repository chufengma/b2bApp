package com.onefengma.taobuxiu.views.auth;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.toolbar)
    ToolBar toolbar;

    public static void start(BaseActivity activity, String url, String title) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);
        ButterKnife.bind(this);

        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        webview.loadUrl(url);
        toolbar.setTitle(title);
    }
}

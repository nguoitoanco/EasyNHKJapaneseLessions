package muscular.man.tools.learning.easyjapaneselessions.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.startapp.android.publish.StartAppSDK;

import muscular.man.tools.learning.easyjapaneselessions.R;
import muscular.man.tools.learning.easyjapaneselessions.utils.NetworkUtil;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private WebView mWebView;
    private String mCurrentUrl;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppSDK.init(this, "209582531", true);
        setContentView(R.layout.activity_main);

        mWebView = (WebView) findViewById(R.id.webview);

        mWebView.setWebViewClient(new MyBrowser());
        mWebView.getSettings().setAllowFileAccess( true );
        mWebView.getSettings().setAppCacheEnabled( true );
        mWebView.getSettings().setJavaScriptEnabled( true );

        mCurrentUrl = getString(R.string.english_lession_list);
        loadUrl(false);
        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadUrl(true);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    private void loadUrl(boolean isReload) {
        if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            // Load offline.
            mWebView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );
            showUnableNetworkStatusBar();
        } else {
            // Load online.
            mWebView.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT);
        }

        if (isReload) {
            mWebView.reload();
        } else {
            mWebView.loadUrl(mCurrentUrl);
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            mCurrentUrl = url;
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            view.findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.findViewById(R.id.loading_panel).setVisibility(View.GONE);
        }
    }

    protected void showUnableNetworkStatusBar() {
        if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            Snackbar.make(mWebView, "Network is unavailable!", Snackbar.LENGTH_LONG)
                    .setAction("Setting", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        }
                    }).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                        mCurrentUrl = mWebView.getUrl();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}

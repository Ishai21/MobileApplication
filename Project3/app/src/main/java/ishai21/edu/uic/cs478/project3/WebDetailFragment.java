package ishai21.edu.uic.cs478.project3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WebDetailFragment extends Fragment {

    private static final String ARG_INITIAL_URL = "initial_url";
    private static final String STATE_WEBVIEW = "state_webview";

    private WebView webView;
    private String pendingUrl;

    public static WebDetailFragment newInstance(String url) {
        WebDetailFragment f = new WebDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_INITIAL_URL, url);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pendingUrl = getArguments().getString(ARG_INITIAL_URL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = view.findViewById(R.id.place_webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        webView.setWebViewClient(
                new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(
                            WebView view, WebResourceRequest request) {
                        return false;
                    }
                });

        if (savedInstanceState != null) {
            Bundle wvState = savedInstanceState.getBundle(STATE_WEBVIEW);
            if (wvState != null) {
                webView.restoreState(wvState);
                return;
            }
        }
        if (pendingUrl != null) {
            webView.loadUrl(pendingUrl);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (webView != null) {
            Bundle wv = new Bundle();
            webView.saveState(wv);
            outState.putBundle(STATE_WEBVIEW, wv);
        }
    }

    public void loadUrl(String url) {
        pendingUrl = url;
        if (webView != null) {
            webView.loadUrl(url);
        }
    }

    @Override
    public void onDestroyView() {
        if (webView != null) {
            webView.stopLoading();
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
            webView.destroy();
            webView = null;
        }
        super.onDestroyView();
    }
}

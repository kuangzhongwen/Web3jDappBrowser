package io.web3j.browser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Web3 browser
 *
 * @author kuang on 2018/01/08.
 */
public final class Web3Browser extends WebView {

    private static final String JS_PROTOCOL_ON_SUCCESSFUL = "onSignSuccessful(%1$s, \"%2$s\")";
    private static final String JS_PROTOCOL_ON_FAILURE = "onSignError(%1$s, \"%2$s\")";
    private static final String JS_PROTOCOL_CANCELLED = "cancelled";

    @Nullable
    private OnSignTransactionListener onSignTransactionListener;
    @Nullable
    private OnSignMessageListener onSignMessageListener;
    @Nullable
    private OnSignPersonalMessageListener onSignPersonalMessageListener;
    @Nullable
    private OnSignTypedMessageListener onSignTypedMessageListener;

    private JsInjectorClient jsInjectorClient;
    private Web3BrowserClient web3BrowserClient;

    public Web3Browser(@NonNull Context context) {
        this(context, null);
    }

    public Web3Browser(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, Resources.getSystem().getIdentifier("webViewStyle", "attr", "android"));
    }

    public Web3Browser(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        super.setWebChromeClient(client);
    }

    public void setWeb3BrowserClient(WebViewClient client) {
        super.setWebViewClient(new WrapWebViewClient(web3BrowserClient, client, jsInjectorClient));
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        jsInjectorClient = new JsInjectorClient(getContext());
        web3BrowserClient = new Web3BrowserClient(jsInjectorClient, new UrlHandlerManager());
        WebSettings webSettings = super.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        addJavascriptInterface(new SignCallbackJSInterface(
            this,
            innerOnSignTransactionListener,
            innerOnSignMessageListener,
            innerOnSignPersonalMessageListener,
            innerOnSignTypedMessageListener), "ctxcWeb3Browser");
        super.setWebViewClient(web3BrowserClient);
    }

    @Override
    public WebSettings getSettings() {
        return new WrapWebSettings(super.getSettings());
    }

    public void setWalletAddress(@NonNull Address address) {
        jsInjectorClient.setWalletAddress(address);
    }

    @Nullable
    public Address getWalletAddress() {
        return jsInjectorClient.getWalletAddress();
    }

    public void setChainId(int chainId) {
        jsInjectorClient.setChainId(chainId);
    }

    public int getChainId() {
        return jsInjectorClient.getChainId();
    }

    public void setRpcUrl(@NonNull String rpcUrl) {
        jsInjectorClient.setRpcUrl(rpcUrl);
    }

    @Nullable
    public String getRpcUrl() {
        return jsInjectorClient.getRpcUrl();
    }

    public void addUrlHandler(@NonNull UrlHandler urlHandler) {
        web3BrowserClient.addUrlHandler(urlHandler);
    }

    public void removeUrlHandler(@NonNull UrlHandler urlHandler) {
        web3BrowserClient.removeUrlHandler(urlHandler);
    }

    public void setOnSignTransactionListener(@Nullable OnSignTransactionListener onSignTransactionListener) {
        this.onSignTransactionListener = onSignTransactionListener;
    }

    public void setOnSignMessageListener(@Nullable OnSignMessageListener onSignMessageListener) {
        this.onSignMessageListener = onSignMessageListener;
    }

    public void setOnSignPersonalMessageListener(@Nullable OnSignPersonalMessageListener onSignPersonalMessageListener) {
        this.onSignPersonalMessageListener = onSignPersonalMessageListener;
    }

    public void setOnSignTypedMessageListener(@Nullable OnSignTypedMessageListener onSignTypedMessageListener) {
        this.onSignTypedMessageListener = onSignTypedMessageListener;
    }

    public void onSignTransactionSuccessful(Transaction transaction, String signHex) {
        callbackToJS(transaction.leafPosition, JS_PROTOCOL_ON_SUCCESSFUL, signHex);
    }

    public void onSignMessageSuccessful(Message message, String signHex) {
        callbackToJS(message.leafPosition, JS_PROTOCOL_ON_SUCCESSFUL, signHex);
    }

    public void onSignPersonalMessageSuccessful(Message message, String signHex) {
        callbackToJS(message.leafPosition, JS_PROTOCOL_ON_SUCCESSFUL, signHex);
    }

    public void onSignError(Transaction transaction, String error) {
        callbackToJS(transaction.leafPosition, JS_PROTOCOL_ON_FAILURE, error);
    }

    public void onSignError(Message message, String error) {
        callbackToJS(message.leafPosition, JS_PROTOCOL_ON_FAILURE, error);
    }

    public void onSignCancel(Transaction transaction) {
        callbackToJS(transaction.leafPosition, JS_PROTOCOL_ON_FAILURE, JS_PROTOCOL_CANCELLED);
    }

    public void onSignCancel(Message message) {
        callbackToJS(message.leafPosition, JS_PROTOCOL_ON_FAILURE, JS_PROTOCOL_CANCELLED);
    }

    private void callbackToJS(long callbackId, String function, String param) {
        final String callback = String.format(function, callbackId, param);
        post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    evaluateJavascript(callback, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Log.i("WEB_VIEW", value);
                        }
                    });
                } else {
                    loadUrl(callback);
                }
            }
        });
    }

    private final OnSignTransactionListener innerOnSignTransactionListener = new OnSignTransactionListener() {
        @Override
        public void onSignTransaction(Transaction transaction) {
            if (onSignTransactionListener != null) {
                onSignTransactionListener.onSignTransaction(transaction);
            }
        }
    };

    private final OnSignMessageListener innerOnSignMessageListener = new OnSignMessageListener() {
        @Override
        public void onSignMessage(Message message) {
            if (onSignMessageListener != null) {
                onSignMessageListener.onSignMessage(message);
            }
        }
    };

    private final OnSignPersonalMessageListener innerOnSignPersonalMessageListener = new OnSignPersonalMessageListener() {
        @Override
        public void onSignPersonalMessage(Message message) {
            onSignPersonalMessageListener.onSignPersonalMessage(message);
        }
    };

    private final OnSignTypedMessageListener innerOnSignTypedMessageListener = new OnSignTypedMessageListener() {
        @Override
        public void onSignTypedMessage(Message<TypedData[]> message) {
            onSignTypedMessageListener.onSignTypedMessage(message);
        }
    };

    private class WrapWebViewClient extends WebViewClient {

        private final Web3BrowserClient internalClient;
        private final WebViewClient externalClient;
        private final JsInjectorClient jsInjectorClient;

        WrapWebViewClient(Web3BrowserClient internalClient, WebViewClient externalClient, JsInjectorClient jsInjectorClient) {
            this.internalClient = internalClient;
            this.externalClient = externalClient;
            this.jsInjectorClient = jsInjectorClient;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return externalClient.shouldOverrideUrlLoading(view, url) || internalClient.shouldOverrideUrlLoading(view, url);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            WebResourceResponse response = externalClient.shouldInterceptRequest(view, request);
            if (response != null) {
                try {
                    InputStream in = response.getData();
                    int len = in.available();
                    byte[] data = new byte[len];
                    int readLen = in.read(data);
                    if (readLen == 0) {
                        throw new IOException("Nothing is read.");
                    }
                    String injectedHtml = jsInjectorClient.injectJS(new String(data));
                    response.setData(new ByteArrayInputStream(injectedHtml.getBytes()));
                } catch (IOException ex) {
                    Log.e("INJECT AFTER_EXTRNAL", "", ex);
                }
            } else {
                response = internalClient.shouldInterceptRequest(view, request);
            }
            return response;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            externalClient.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            externalClient.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedSslError(final WebView view, final SslErrorHandler handler, SslError error) {
            externalClient.onReceivedSslError(view, handler, error);
        }
    }
}

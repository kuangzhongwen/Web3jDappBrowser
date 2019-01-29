package io.web3j.browser;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import com.google.gson.Gson;
import java.math.BigInteger;

/**
 * Sign callback js interface
 *
 * @author kuang on 2018/01/08.
 */
public class SignCallbackJSInterface {

    private final WebView webView;

    @NonNull
    private final OnSignTransactionListener onSignTransactionListener;
    @NonNull
    private final OnSignMessageListener onSignMessageListener;
    @NonNull
    private final OnSignPersonalMessageListener onSignPersonalMessageListener;
    @NonNull
    private final OnSignTypedMessageListener onSignTypedMessageListener;

    SignCallbackJSInterface(
        WebView webView,
        @NonNull OnSignTransactionListener onSignTransactionListener,
        @NonNull OnSignMessageListener onSignMessageListener,
        @NonNull OnSignPersonalMessageListener onSignPersonalMessageListener,
        @NonNull OnSignTypedMessageListener onSignTypedMessageListener) {
        this.webView = webView;
        this.onSignTransactionListener = onSignTransactionListener;
        this.onSignMessageListener = onSignMessageListener;
        this.onSignPersonalMessageListener = onSignPersonalMessageListener;
        this.onSignTypedMessageListener = onSignTypedMessageListener;
    }

    @JavascriptInterface
    public void signTransaction(
        int callbackId,
        String recipient,
        String value,
        String nonce,
        String gasLimit,
        String gasPrice,
        String payload) {
        long gasL;
        try {
            gasL = Long.parseLong(gasLimit);
        } catch (NumberFormatException e) {
            gasL = 0;
        }
        Transaction transaction = new Transaction(
            TextUtils.isEmpty(recipient) ? Address.EMPTY : new Address(recipient),
            null,
            Hex.hexToBigInteger(value, BigInteger.ZERO),
            Hex.hexToBigInteger(gasPrice, BigInteger.ZERO),
            gasL,
            Hex.hexToLong(nonce, -1),
            payload,
            callbackId);
        onSignTransactionListener.onSignTransaction(transaction);
    }

    @JavascriptInterface
    public void signMessage(final int callbackId, final String data) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                onSignMessageListener.onSignMessage(new Message<>(data, getUrl(), callbackId));
            }
        });
    }

    @JavascriptInterface
    public void signPersonalMessage(final int callbackId, final String data) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                onSignPersonalMessageListener.onSignPersonalMessage(new Message<>(data, getUrl(), callbackId));
            }
        });
    }

    @JavascriptInterface
    public void signTypedMessage(final int callbackId, final String data) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                TrustProviderTypedData[] rawData = new Gson().fromJson(data, TrustProviderTypedData[].class);
                int len = rawData.length;
                TypedData[] typedData = new TypedData[len];
                for (int i = 0; i < len; i++) {
                    typedData[i] = new TypedData(rawData[i].name, rawData[i].type, rawData[i].value);
                }
                onSignTypedMessageListener.onSignTypedMessage(new Message<>(typedData, getUrl(), callbackId));
            }
        });
    }

    private String getUrl() {
        return webView == null ? "" : webView.getUrl();
    }

    private static class TrustProviderTypedData {

        public String name;
        public String type;
        public Object value;
    }
}

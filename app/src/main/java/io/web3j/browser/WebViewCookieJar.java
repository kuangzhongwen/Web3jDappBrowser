package io.web3j.browser;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * WebView cookie jar
 *
 * @author kuang on 2018/01/08.
 */
final class WebViewCookieJar implements CookieJar {

    private CookieManager cookieManager;

    WebViewCookieJar() {
        try {
            cookieManager = CookieManager.getInstance();
        } catch (Exception ex) {
            // Caused by android.content.pm.PackageManager$NameNotFoundException com.google.android.webview
            Log.e("WebViewCookieJar", ex.getMessage());
        }
    }

    @Override
    public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
        if (cookieManager != null) {
            String urlString = url.toString();
            for (Cookie cookie : cookies) {
                cookieManager.setCookie(urlString, cookie.toString());
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        if (cookieManager != null) {
            String cookieStr = cookieManager.getCookie(url.toString());
            if (!TextUtils.isEmpty(cookieStr)) {
                String[] cookieHeaders = Utils.fastSplit(cookieStr, ';');
                if (cookieHeaders != null) {
                    List<Cookie> cookies = new ArrayList<>();
                    for (String header : cookieHeaders) {
                        cookies.add(Cookie.parse(url, header));
                    }
                    return cookies;
                }
            }
        }
        return Collections.emptyList();
    }
}

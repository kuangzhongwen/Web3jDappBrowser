package io.web3j.browser;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import java.util.Collections;
import java.util.Map;

/**
 * Url handler manager
 *
 * @author kuang on 2018/01/08.
 */
final class UrlHandlerManager {

    private final Map<String, UrlHandler> handlers = Collections.emptyMap();

    UrlHandlerManager(@NonNull UrlHandler... handlers) {
        for (UrlHandler handler : handlers) {
            this.handlers.put(handler.getScheme(), handler);
        }
    }

    public void add(@NonNull UrlHandler handler) {
        handlers.put(handler.getScheme(), handler);
    }

    public void remove(@NonNull UrlHandler handler) {
        handlers.remove(handler.getScheme());
    }

    String handle(String url) {
        if (TextUtils.isEmpty(url))
            return null;
        return handle(Uri.parse(url));
    }

    private String handle(Uri uri) {
        if (uri == null)
            return null;
        if (!handlers.containsKey(uri.getScheme()))
            return uri.toString();
        return handlers.get(uri.getScheme()).handle(uri);
    }
}

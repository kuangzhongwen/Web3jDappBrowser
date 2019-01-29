package io.web3j.browser;

import android.net.Uri;

/**
 * Url handler interface
 *
 * @author kuang on 2018/01/08.
 */
interface UrlHandler {

    String getScheme();

    String handle(Uri url);
}

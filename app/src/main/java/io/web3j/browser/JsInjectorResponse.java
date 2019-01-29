package io.web3j.browser;

/**
 * Js injector response entity
 *
 * @author kuang on 2018/01/08.
 */
final class JsInjectorResponse {

    final String data;
    final String url;
    final String mime;
    final String charset;
    final boolean isRedirect;

    JsInjectorResponse(String data, int code, String url, String mime, String charset, boolean isRedirect) {
        this.data = data;
        this.url = url;
        this.mime = mime;
        this.charset = charset;
        this.isRedirect = isRedirect;
    }
}

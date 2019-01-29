package io.web3j.browser;

/**
 * Sign typed message listener.
 *
 * @author kuang on 2018/01/08.
 */
public interface OnSignTypedMessageListener {

    void onSignTypedMessage(Message<TypedData[]> message);
}

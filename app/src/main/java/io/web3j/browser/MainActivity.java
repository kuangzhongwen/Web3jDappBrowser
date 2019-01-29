package io.web3j.browser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebSettings;

/**
 * Main page.
 *
 * @author kuang on 2018/01/08.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.webView_container);
        final Web3Browser web3Browser = new Web3Browser(this);
        web3Browser.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));
        viewGroup.removeAllViews();
        viewGroup.addView(web3Browser);
        WebSettings settings = web3Browser.getSettings();
        settings.setSupportZoom(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web3Browser.removeJavascriptInterface("searchBoxJavaBridge_");
        web3Browser.removeJavascriptInterface("accessibilityTraversal");
        web3Browser.removeJavascriptInterface("accessibility");
        web3Browser.setVerticalScrollBarEnabled(false);
        web3Browser.setHorizontalScrollBarEnabled(false);
        web3Browser.setOnSignMessageListener(new OnSignMessageListener() {
            @Override
            public void onSignMessage(Message<String> message) {
                web3Browser.onSignCancel(message);
            }
        });
        web3Browser.setOnSignPersonalMessageListener(new OnSignPersonalMessageListener() {
            @Override
            public void onSignPersonalMessage(Message<String> message) {
                web3Browser.onSignCancel(message);
            }
        });
        web3Browser.setOnSignTransactionListener(new OnSignTransactionListener() {
            @Override
            public void onSignTransaction(final Transaction transaction) {
                Log.i(TAG, "onSignTransaction 处理交易签名，转账");
            }
        });
        web3Browser.setChainId(1);
        web3Browser.setRpcUrl("https://cerebro.cortexlabs.ai/wallet");
        web3Browser.setWalletAddress(new Address("0x9337d28ae702616abca62f01b7b90fd278bec830"));
        web3Browser.loadUrl("https://digitalclash.github.io");
    }
}

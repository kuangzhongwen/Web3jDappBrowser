# Web3jDappBrowser

通过注入 js 拦截 web3j 请求：注入钱包地址，拦截交易交付给客户端做签名转账，无需 dapp 项目方做太多适配。

主要注意点，客户端的raw文件夹下的init.js中：
```
window.ctxWeb3 = new Web3(window.web3.currentProvider);
window.ctxWeb3.eth.defaultAccount = window.web3.eth.defaultAccount;
```
这两句是和dapp源代码中需要的web3j对象命名一致。

调用：
```
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
```

<img src="https://m.qpic.cn/psb?/V149vWW31mgVIF/nP6PweDjv68blB41vNXx6V5vC3wERBVfhUssz04esHI!/b/dPQAAAAAAAAA&bo=OASABwAAAAARB4s!&rf=viewer_4" width="540" hegiht="690" align=center />

<img src="http://m.qpic.cn/psb?/V149vWW31mgVIF/mnfveEzulOoRrV9G.T30oPAsOdtyGkRuLMm3DrUKoqk!/b/dIsBAAAAAAAA&bo=OASABwAAAAARF5s!&rf=viewer_4" width="540" hegiht="690" align=center />

<img src="http://m.qpic.cn/psb?/V149vWW31mgVIF/IRKYgL*LbJ0w.ykvSWSpv8W7XnBwtYapamy4nEj9hno!/b/dA4AAAAAAAAA&bo=OASABwAAAAARF5s!&rf=viewer_4" width="540" hegiht="690" align=center />

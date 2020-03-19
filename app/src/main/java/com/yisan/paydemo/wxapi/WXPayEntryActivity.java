package com.yisan.paydemo.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yisan.paydemo.constants.Constants;

/**
 * @author：wzh
 * @description: <p>
 * 这个页面是在你调起微信支付完成支付（或取消或失败）后，再回到你的App时会调用的一个页面。
 * 请务必保证在你项目下他的结果目录为：
 * 开放平台绑定的商户应用包名 + wxapi + WXPayEntryActivity
 *
 *
 * </p>
 * @packageName: com.yisan.paydemo.wxapi
 * @date：2020/3/19 0019 下午 2:21
 */
public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    /**
     * 支付成功
     */
    private static final int RES_PAY_CODE_SUCCESS = 0;
    /**
     * 支付取消
     */
    private static final int RES_PAY_CODE_CANCLE = -2;
    /**
     * 支付失败
     */
    private static final int RES_PAY_CODE_FAILURE = -1;


    private IWXAPI wxapi;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            //支付可能发生错误、要捕获异常
            wxapi = WXAPIFactory.createWXAPI(this, Constants.WX_PAY_APP_ID);
            wxapi.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxapi.handleIntent(intent, this);

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {

        if (resp.errCode == ConstantsAPI.COMMAND_PAY_BY_WX) {

            if (resp.errCode == RES_PAY_CODE_SUCCESS) {
                Toast.makeText(this, "支付成功", Toast.LENGTH_LONG).show();
            } else if (resp.errCode == RES_PAY_CODE_CANCLE) {
                Toast.makeText(this, "取消支付", Toast.LENGTH_LONG).show();
            } else if (resp.errCode == RES_PAY_CODE_FAILURE){
                Toast.makeText(this, "支付失败", Toast.LENGTH_LONG).show();
            }

            /**
             * <p>
             *     我们在支付完成以后，一般是希望直接回到我们自己的应用里面。
             *     这个时候我们就需要让上面的WXPayEntryActivity不显示,否则就会闪一下或是停留在这个黑黑页面。
             *     不显示的话要注意2点，一个是不要有布局，另一个就是要及时的finish掉这个Activity
             * </p>
             */
            sendPayNotice();
            finish();
        }
    }

    /**
     * 发送支付结果通知
     */
    private void sendPayNotice(){

    }
}

package com.yisan.paydemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yisan.paydemo.constants.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


/**
 * 集成市面上常用的支付Demo：微信支付、支付宝支付、银联支付
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private static final int SDK_ALI_PAY_FLAG = 0x01;

    /**
     * 微信支付
     */
    private Button btnWx;
    /**
     * 支付宝
     */
    private Button btnAli;
    /**
     * 银联
     */
    private Button btnUnion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnWx = findViewById(R.id.btn_wx_pay);
        btnAli = findViewById(R.id.btn_ali_pay);
        btnUnion = findViewById(R.id.btn_union_pay);

        btnWx.setOnClickListener(this);
        btnAli.setOnClickListener(this);
        btnUnion.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_wx_pay) {
            callWxPay();
        } else if (v.getId() == R.id.btn_ali_pay) {

        } else if (v.getId() == R.id.btn_union_pay) {

        }
    }


    /**
     * 调用微信支付
     */
    private void callWxPay() {
        try {
            //1、调用接口从后台获取订单信息
            String content = "";
            //2、需要一个注册微信支付的AppId
            IWXAPI api = WXAPIFactory.createWXAPI(MainActivity.this, Constants.WX_PAY_APP_ID);
            //3、解析订单信息
            JSONObject json = new JSONObject(content);
            PayReq req = new PayReq();
            req.appId = json.getString("appid");
            req.partnerId = json.getString("partnerid");
            req.prepayId = json.getString("prepayid");
            req.nonceStr = json.getString("noncestr");
            req.timeStamp = json.getString("timestamp");
            req.packageValue = json.getString("package");
            req.sign = json.getString("sign");
            //4、发起调用微信支付
            api.sendReq(req);
        } catch (JSONException e) {
            Log.e(TAG, "调用微信支付失败 ");
            e.printStackTrace();
        }
    }


    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_ALI_PAY_FLAG: {
                    //这里接收支付宝的回调信息
                    //需要注意的是，支付结果一定要调用自己的服务端来确定，不能通过支付宝的回调结果来判断
                    break;
                }
                default:
                    break;
            }
        };
    };


    Runnable payRunnable = new Runnable() {

        @Override
        public void run() {

            String orderInfo = null;
            PayTask alipay = new PayTask(MainActivity.this);
            Map<String, String> result = alipay.payV2(orderInfo, true);
            Log.i(TAG, result.toString());

            Message msg = new Message();
            msg.what = SDK_ALI_PAY_FLAG;
            msg.obj = result;

        }
    };
}

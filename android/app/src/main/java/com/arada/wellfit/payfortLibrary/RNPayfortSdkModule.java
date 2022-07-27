package com.RNpayfort.payfortLibrary;

import android.content.Intent;

import android.app.Activity;
import android.content.Intent;

import com.RNpayfort.payfortLibrary.LIFortPayment;
import com.RNpayfort.payfortLibrary.RequestParameter;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager;
import com.payfort.fortpaymentsdk.callbacks.FortCallBackManager;

//import com.payfort.sdk.android.dependancies.base.FortInterfaces;
import com.payfort.fortpaymentsdk.callbacks.FortInterfaces;

//import com.payfort.sdk.android.dependancies.models.FortRequest;
import com.payfort.fortpaymentsdk.domain.model.FortRequest;
import com.payfort.fortpaymentsdk.FortSdk;
import com.payfort.fortpaymentsdk.callbacks.FortCallback;


import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.annotations.NonNull;

import static com.RNpayfort.payfortLibrary.LIFortPayment.random;

public class RNPayfortSdkModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    private static ReactApplicationContext reactContext;
    FortCallBackManager fortCallback = FortCallback.Factory.create();

    public RNPayfortSdkModule(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.reactContext.addActivityEventListener(this);
    }

    public void handleCallBack(int requestCode, int resultCode, Intent data){
        fortCallback.onActivityResult(requestCode, resultCode, data);
    }

    @ReactMethod
    public void getDeviceId(Callback successCallback){
        successCallback.invoke(FortSdk.getDeviceId(getCurrentActivity()));
    }

    @ReactMethod
    public void Pay(String parameters, Callback successCallback, Callback errorCallback){
//        Toast.makeText(getReactApplicationContext(),"PayMent",Toast.LENGTH_LONG);

        Gson gson=new Gson();
        RequestParameter requestParameterBean =gson.fromJson(parameters, RequestParameter.class);

        try{
            LIFortPayment liFortPayment = new LIFortPayment.LiFortpaymentBuilder()
                    .setContext(getCurrentActivity())
                    .setReactContext(reactContext)
                    .setCommand(requestParameterBean.getCommand())
                    .setAccess_code(requestParameterBean.getAccessCode())
                    .setMerchant_identifier(requestParameterBean.getMerchantIdentifier())
                    .setSha_Request_Phrase(requestParameterBean.getShaRequestPhrase())
                    .setFortCallback(fortCallback)
                    .setTesting(requestParameterBean.getTesting())
                    .setAmount(requestParameterBean.getAmount())
                    .setCurrency_type(requestParameterBean.getCurrencyType())
                    .setEmail(requestParameterBean.getEmail())
                    .setLanguage(requestParameterBean.getLanguage())
                    .setTokenName(requestParameterBean.getTokenName())
                    .setSDKToken(requestParameterBean.getSDKToken())
                    .setMerchentReference(requestParameterBean.getMerchentReference())
                    .setPaymentOption(requestParameterBean.getPaymentOption())
                    .setEci(requestParameterBean.getEci())
                    .setOrderDescription(requestParameterBean.getOrderDescription())
                    .setCustomerIP(requestParameterBean.getCustomerIp())
                    .setCustomerName(requestParameterBean.getCustomerName())
                    .setPhoneNumber(requestParameterBean.getPhoneNumber())
                    .setSettlementReference(requestParameterBean.getSettlementReference())
                    .setMerchantExtra(requestParameterBean.getMerchantExtra())
                    .setMerchantExtra1(requestParameterBean.getMerchantExtra1())
                    .setMerchantExtra2(requestParameterBean.getMerchantExtra2())
                    .setMerchantExtra3(requestParameterBean.getMerchantExtra3())
                    .setMerchantExtra4(requestParameterBean.getMerchantExtra4())
                    .setMerchantExtra5(requestParameterBean.getMerchantExtra5())
                    .setCallback(new FortInterfaces.OnTnxProcessed() {
                        @Override
                        public void onCancel(Map<String, Object> requestMap, Map<String, Object> responseMap) {
//                            Toast.makeText(reactContext, "Payment cancel by user", Toast.LENGTH_SHORT).show();
//                            Log.d("Hello", "onCancel() called with: map = [" + requestMap + "], map1 = [" + responseMap + "]");
                            errorCallback.invoke(converMapToJson(responseMap));
                        }
                        @Override
                        public void onSuccess(Map<String, Object> requestMap, Map<String, Object> responseMap) {
//                            Toast.makeText(reactContext, "Payment Success", Toast.LENGTH_SHORT).show();
//                            Log.d("Hello", "onSuccess() called with: map = [" + requestMap + "], map1 = [" + responseMap + "]");
                            successCallback.invoke(converMapToJson(responseMap));

                        }
                        @Override
                        public void onFailure(Map<String, Object> requestMap, Map<String, Object> responseMap) {
//                            Toast.makeText(reactContext, "Payment fail", Toast.LENGTH_SHORT).show();
//                            Log.d("Hello", "onFailure() called with: map = [" + requestMap + "], map1 = [" + responseMap + "]");
                            errorCallback.invoke(converMapToJson(responseMap));
                        }
                    })
                    .build();

            liFortPayment.requestPayment();
        }catch (IllegalViewOperationException e) {
            errorCallback.invoke("{\"response_message\":"+e.getMessage()+"}");
        }
    }

    private String converMapToJson(Map<String, Object> source){
        Gson gson=new Gson();
        Type gsonType = new TypeToken<HashMap>(){}.getType();
        String gsonString = gson.toJson(source,gsonType);
        return gsonString;
    }

    @NonNull
    @Override
    public String getName() {
        return "PayFort";
    }


    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        handleCallBack(requestCode,resultCode,data);
    }

    @Override
    public void onNewIntent(Intent intent) {

    }
}
package com.example.lastsdktesting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.duitku.sdk.DuitkuCallback.DuitkuCallbackTransaction;
import com.duitku.sdk.DuitkuClient;
import com.duitku.sdk.DuitkuUtility.BaseKitDuitku;
import com.duitku.sdk.DuitkuUtility.DuitkuKit;
import com.duitku.sdk.Model.ItemDetails;

import java.util.ArrayList;

public class MainActivity extends DuitkuClient {
    DuitkuKit duitku ;
    DuitkuCallbackTransaction callbackKit ;
    EditText amount;
    EditText orderId;
    EditText phone;
    EditText detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        duitku = new DuitkuKit();
        callbackKit = new DuitkuCallbackTransaction();

        Button btn = findViewById(R.id.btn_submit);
        amount = findViewById(R.id.amt);
        orderId = findViewById(R.id.moi);
        phone = findViewById(R.id.phonenum);
        detail = findViewById(R.id.productDetail);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s_amount = amount.getText().toString();

                if (s_amount.equals("")){
                    s_amount = "0" ;
                }
                int nominal = Integer.parseInt(s_amount);

                if ( nominal == 0 ){
                    amount.setError("Silahkan masukan jumlah pembayaran");
                }else if(nominal < 10000) {
                    amount.setError("Minimal pembayaran Rp 10.000");
                }
                else {

                    //setting merchant
                    settingMerchant();
                    startPayment(MainActivity.this); //optional

                }
                //Toast.makeText(MainActivity.this,"Amount : "+nominal + " OrderId : " + orderId.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public void onSuccessTransaction(String status, String reference, String amount, String Code) {
        Toast.makeText(MainActivity.this,"Transaction"+status,Toast.LENGTH_LONG).show();

        clearSdkTask(); //REQUIRED
        super.onSuccessTransaction(status, reference, amount, Code);
    }

    @Override
    public void onPendingTransaction(String status, String reference, String amount, String Code) {
        Toast.makeText(MainActivity.this,"Transaction"+status,Toast.LENGTH_LONG).show();

        clearSdkTask(); //REQUIRED
        super.onPendingTransaction(status, reference, amount, Code);
    }

    @Override
    public void onCancelTransaction(String status, String reference, String amount, String Code) {
        Toast.makeText(MainActivity.this,"Transaction :"+status,Toast.LENGTH_LONG).show();

        clearSdkTask(); //REQUIRED
        super.onCancelTransaction(status, reference, amount, Code);
    }

    protected void onResume() {
        super.onResume();

        run();

    }
    private void settingMerchant(){
        //set false if callback from duitku
        callbackKit.setCallbackFromMerchant(false);
        
        int nominal = Integer.parseInt(amount.getText().toString());
        run();
        //set base url merchant
        BaseKitDuitku.setBaseUrlApiDuitku("https://merchant.com/duitku/api/");
        BaseKitDuitku.setUrlRequestTransaction("requestTransaction.php");
        BaseKitDuitku.setUrlCheckTransaction("checkTransaction.php");
        BaseKitDuitku.setUrlListPayment("listPayment.php");
        duitku.setPaymentAmount(nominal);
        duitku.setProductDetails(detail.getText().toString());
        duitku.setEmail(orderId.getText().toString());
        duitku.setPhoneNumber(phone.getText().toString());
        duitku.setAdditionalParam(""); //optional
        duitku.setMerchantUserInfo(""); //optional
        duitku.setCustomerVaName("");
        duitku.setExpiryPeriod("10");
        duitku.setCallbackUrl("http://merchant.com/callbackUrl");
        duitku.setReturnUrl("http://merchant.com/returnUrl");

        //set item details
        ItemDetails itemDetails = new ItemDetails(10000,2,"shoes");
        ArrayList<ItemDetails> arrayList = new  ArrayList<ItemDetails> ();
        arrayList.add(itemDetails);
        duitku.setItemDetails(arrayList);
    }
}

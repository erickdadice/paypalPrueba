package com.icb.paypalsdk;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.icb.paypalsdk.Config.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class activity_principal extends AppCompatActivity {

private  static final int PAYPAL_REQUEST_CODE=7171;

private static PayPalConfiguration config =new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)

    //utilizar cuenta de sandBox para test

    .clientId(Config.PAYPAL_CLIENT_ID);

    Button btnPagar;
    EditText txtMonto;


    String monto="";


    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Iniciar servio PayPal

        Intent intent= new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        btnPagar=(Button)findViewById(R.id.btnPagar);
        txtMonto=(EditText)findViewById(R.id.txtMonto);

        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procesarPago();
            }
        });
    }

    private void procesarPago() {

        //recibir el monto del editText
        monto= txtMonto.getText().toString();
        PayPalPayment payPalPayment= new PayPalPayment(new BigDecimal(String.valueOf(monto)),"MXN","Pagado por Erick",PayPalPayment
                .PAYMENT_INTENT_SALE);

        //enviar parametros

        Intent intent =new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PAYPAL_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);


                if (confirmation != null) {
                    try {

                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, ActivityDetallesPago.class).putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", monto));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this,"CANCELADA",Toast.LENGTH_SHORT).show();


        }else if (resultCode==PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this,"Invalida",Toast.LENGTH_SHORT).show();




    }


}
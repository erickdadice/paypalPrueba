package com.icb.paypalsdk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityDetallesPago extends AppCompatActivity {

    TextView txtID,txtEstatus,txtMonto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pago);

        //referencia de componentes de la vista
        txtID=(TextView)findViewById(R.id.txtId);
        txtEstatus=(TextView)findViewById(R.id.txtEstatus);
        txtMonto= (TextView)findViewById(R.id.txtMonto);

        //Recibir los datos del intent del activity anterior
        Intent intent =getIntent();
        try {
            JSONObject jsonObject=new JSONObject(intent.getStringExtra("PaymentDetails"));
            verDetalles(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));

        }catch (JSONException e){
            e.printStackTrace();

        }
    }

    private void verDetalles(JSONObject response, String monto) {

        // seteo de parametros Text view

        try {
            txtID.setText(response.getString("id"));
            txtEstatus.setText(response.getString("status"));
            txtMonto.setText("$" + monto);

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }
}
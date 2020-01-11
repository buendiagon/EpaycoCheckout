package com.example.newepayco;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.epaycocheckout.check.CheckoutDialog;
import com.example.epaycocheckout.classes.EpaycoRest;
import com.example.epaycocheckout.entities.Checkout;
import com.example.epaycocheckout.check.Util;

import org.json.JSONObject;

import co.epayco.android.models.Authentication;
import co.epayco.android.util.EpaycoCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Si el permiso de escritura externa esta concedido en el dispositivo, Permite que se realize un screenshot a los datos de la compra en efectivo (numero de proyecto y pin)
        if (!Util.isStoragePermissionGranted(this)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        Button btn_test = findViewById(R.id.btn_test);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Authentication auth = new Authentication();
                auth.setPrivateKey(getString(R.string.epayco_private_key));
                auth.setApiKey(getString(R.string.epayco_public_key));
                auth.setLang("ES");
                auth.setTest(true);

                // En lugar de usar la clase original de Epayco, se usa una descendiente para poder manejar pagos con tarjetas de credito sin asociar clientes y algunas otras API's que la original no posee.
                final EpaycoRest epayco = new EpaycoRest(auth);
                final Checkout checkout = new Checkout();


                // REQUERIDOS

                checkout.setInvoice("OR - 1234");
                checkout.setTitle("Compra vestido");
                checkout.setDescription("Compra de un vestido rojo");
                checkout.setValue("30000");
                checkout.setTax("0");
                checkout.setTaxBase("0");
                checkout.setCurrency(getString(R.string.currency));
                checkout.setCountry(getString(R.string.country));

                // OPCIONALES

                // El formulario por defecto utiliza appcompat y el formulario 2 utiliza material design para las vistas
                checkout.setForm(2);
                // Los siguientes datos permiten rellenar automaticamente los datos en los formularios
                checkout.setDocType("CC");
                checkout.setDocNumber("1035851980");
                checkout.setName("John");
                checkout.setLastName("Doe");
                checkout.setEmail("example@email.com");
                checkout.setPhoneCode("+57");
                checkout.setPhoneNumber("3010000001");
//                checkout.setUrlConfirmation("");
//                checkout.setUrlResponse("");
                // Se debe colocar una fecha para la expiración del pago por efectivo (Por default se dá un día)
//                checkout.setCashDateEnd("YYYY-MM-DD");
                // añade la opción de pago en un click (Para permitir el pago en un solo click deben estar vacio o con los datos correspondientes)
                checkout.setCustomerId("");
                checkout.setTokenCard("");



                // checkoutDialog recibe como parametro (Context, Epayco, Checkout, EpaycoCallback)
                CheckoutDialog checkoutDialog = new CheckoutDialog(MainActivity.this, epayco, checkout, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        //El tipo de transacción viene dado por el parametro "transaction_type" dentro del json

                        Toast.makeText(MainActivity.this, "La transacción se realizo con éxito", Toast.LENGTH_SHORT).show();
                        Log.e("Prueba", jsonObject.toString());
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(MainActivity.this, "Ocurrio un error con los servidores de epayco", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });

                // start checkout
                View view = checkoutDialog.selectMethod();

                // Para limitar los tipos de pago, se puede esconder su respectiva vista.

//                View card_transaction = view.findViewById(R.id.epayco_btn_card);
//                View pse_transaction = view.findViewById(R.id.epayco_btn_pse);
//                View cash_transaction = view.findViewById(R.id.epayco_btn_cash);
//
//                card_transaction.setVisibility(View.GONE);
//                pse_transaction.setVisibility(View.GONE);
//                cash_transaction.setVisibility(View.GONE);


                // Los siguientes metodos estan públicos para poder usarlos en caso de error para terminar con el checkout
                // O para crear y manejar nuevas vistas con dialogs
//                checkoutDialog.removeAllDialogs();
//                checkoutDialog.removeDialog();
//                checkoutDialog.showDialog(new View(MainActivity.this));
            }
        });
    }
}

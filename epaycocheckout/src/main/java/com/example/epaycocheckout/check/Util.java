package com.example.epaycocheckout.check;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.epaycocheckout.R;
import com.example.epaycocheckout.entities.BankCode;
import com.example.epaycocheckout.entities.Checkout;
import com.example.epaycocheckout.entities.OnceCredit;
import com.example.epaycocheckout.entities.TypeDoc;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.epayco.android.models.Cash;
import co.epayco.android.models.Charge;
import co.epayco.android.models.Client;
import co.epayco.android.models.Pse;

public class Util extends co.epayco.android.util.Util {
    static ArrayList<BankCode> bankArray = new ArrayList<>();

    static ArrayList<TypeDoc> getTypeIdArray() {
        ArrayList<TypeDoc> typeIdArray = new ArrayList<>();
        typeIdArray.add(new TypeDoc("CC", "C.C."));
        typeIdArray.add(new TypeDoc("NIT", "NIT"));
        typeIdArray.add(new TypeDoc("CE", "C.E."));
        typeIdArray.add(new TypeDoc("PPN", "pasaporte"));
        return typeIdArray;
    }

    static OnceCredit checkoutToOnceCredit(Checkout checkout) {
        OnceCredit onceCredit = new OnceCredit();
        onceCredit.setInvoice(checkout.getInvoice());
        onceCredit.setDescription(checkout.getDescription());
        onceCredit.setValue(checkout.getValue());
        onceCredit.setTax(checkout.getTax());
        onceCredit.setTaxBase(checkout.getTaxBase());
        onceCredit.setCurrency(checkout.getCurrency());
        onceCredit.setCountry(checkout.getCountry());
        onceCredit.setDocType(checkout.getDocType());
        onceCredit.setDocNumber(checkout.getDocNumber());
        onceCredit.setName(checkout.getName());
        onceCredit.setLastName(checkout.getLastName());
        onceCredit.setEmail(checkout.getEmail());
        onceCredit.setPhone(checkout.getPhoneCode() + checkout.getPhoneNumber());
        onceCredit.setUrlResponse(checkout.getUrlResponse());
        onceCredit.setUrlConfirmation(checkout.getUrlConfirmation());
        return onceCredit;
    }

    static Pse checkoutToPse(Checkout checkout) {
        Pse pse = new Pse();
        pse.setInvoice(checkout.getInvoice());
        pse.setDescription(checkout.getDescription());
        pse.setValue(checkout.getValue());
        pse.setTax(checkout.getTax());
        pse.setTaxBase(checkout.getTaxBase());
        pse.setCurrency(checkout.getCurrency());
        pse.setCountry(checkout.getCountry());
        pse.setDocType(checkout.getDocType());
        pse.setDocNumber(checkout.getDocNumber());
        pse.setName(checkout.getName());
        pse.setLastName(checkout.getLastName());
        pse.setEmail(checkout.getEmail());
        pse.setPhone(checkout.getPhoneCode() + checkout.getPhoneNumber());
        pse.setUrlResponse(checkout.getUrlResponse());
        pse.setUrlConfirmation(checkout.getUrlConfirmation());
        return pse;
    }

    static Cash checkoutToCash(Checkout checkout) {
        Cash cash = new Cash();
        cash.setInvoice(checkout.getInvoice());
        cash.setDescription(checkout.getDescription());
        cash.setValue(checkout.getValue());
        cash.setTax(checkout.getTax());
        cash.setTaxBase(checkout.getTaxBase());
        cash.setCurrency(checkout.getCurrency());
        cash.setCountry(checkout.getCountry());
        cash.setDocType(checkout.getDocType());
        cash.setDocNumber(checkout.getDocNumber());
        cash.setName(checkout.getName());
        cash.setLastName(checkout.getLastName());
        cash.setEmail(checkout.getEmail());
        cash.setPhone(checkout.getPhoneCode() + checkout.getPhoneNumber());
        cash.setUrlResponse(checkout.getUrlResponse());
        cash.setUrlConfirmation(checkout.getUrlConfirmation());
        return cash;
    }

    static Client onceCreditToClient(OnceCredit onceCredit) {
        Client client = new Client();
        client.setName(onceCredit.getName() + " " + onceCredit.getLastName());
        client.setEmail(onceCredit.getEmail());
        client.setPhone(onceCredit.getPhone());
        client.setDefaultCard(true);
        return client;
    }

    static Charge onceCreditToCharge(OnceCredit cardData) {
        Charge charge = new Charge();
        charge.setDocType(cardData.getDocType());
        charge.setDocNumber(cardData.getDocNumber());
        charge.setName(cardData.getName());
        charge.setLastName(cardData.getLastName());
        charge.setEmail(cardData.getEmail());
        charge.setInvoice(cardData.getInvoice());
        charge.setDescription(cardData.getDescription());
        charge.setValue(cardData.getValue());
        charge.setTax(cardData.getTax());
        charge.setTaxBase(cardData.getTaxBase());
        charge.setCurrency(cardData.getCurrency());
        charge.setDues(cardData.getDues());
        charge.setUrlResponse(cardData.getUrlResponse());
        charge.setUrlConfirmation(cardData.getUrlConfirmation());
        charge.setExtra1(cardData.getExtra1());
        charge.setExtra2(cardData.getExtra2());
        charge.setExtra3(cardData.getExtra3());
        charge.setCity(cardData.getCity());
        charge.setDepartament(cardData.getDepartament());
        charge.setCountry(cardData.getCountry());
        return charge;
    }

    public static boolean isStoragePermissionGranted(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public static AlertDialog alertWaiting(Context context, String message) {
        assert context != null;
        View view = View.inflate(context, R.layout.waiting, null);
        TextView txt_message = view.findViewById(R.id.epayco_waiting_message);
        txt_message.setText(message);
        MaterialAlertDialogBuilder alerta = new MaterialAlertDialogBuilder(context, R.style.MaterialTheme);
        alerta
                .setView(view)
                .setCancelable(false);
        AlertDialog dialog = alerta.create();
        dialog.show();
        return dialog;
    }

    static void disableViewTemporally(final View view) {
        view.setEnabled(false);
        view.post(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        });
    }


    static void saveViewOnGallery(View view){
        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/EpaycoScreenShot.jpg";

            // create bitmap screen capture
            View v1 = view.getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(view.getContext(), "Imágen guardada", Toast.LENGTH_SHORT).show();

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }


    static void nextFocus(EditText editText) {
        View right = editText.focusSearch(View.FOCUS_RIGHT);
        View down = editText.focusSearch(View.FOCUS_DOWN);
        View up = editText.focusSearch(View.FOCUS_UP);
        View left = (right != null) ? right.focusSearch(View.FOCUS_LEFT) : null;

        if (editText == left && up != right && right instanceof EditText && !(right instanceof AutoCompleteTextView)) {
            right.requestFocus();
        } else if (down instanceof EditText && !(down instanceof AutoCompleteTextView)) {
            down.requestFocus();
        } else {
            InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            if (right != null && left == editText && right != up)
                right.requestFocus();
            else if (down != null) down.requestFocus();
            else editText.clearFocus();

        }
    }


    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
    }

}

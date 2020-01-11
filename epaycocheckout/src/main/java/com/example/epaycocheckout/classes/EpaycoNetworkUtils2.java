package com.example.epaycocheckout.classes;


import com.example.epaycocheckout.entities.OnceCredit;
import com.example.epaycocheckout.entities.TokenCard;
import com.example.epaycocheckout.check.Util;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import co.epayco.android.models.Card;
import co.epayco.android.util.EpaycoNetworkUtils;
import co.epayco.android.util.EpaycoTextUtils;

public class EpaycoNetworkUtils2 extends EpaycoNetworkUtils {
    public EpaycoNetworkUtils2() {
    }

    static RequestParams hashMapFromCardClient(OnceCredit onceCredit, JSONObject options) {
        RequestParams cardClientParams = new RequestParams();
        String apiKey = null;
        String secretKey = null;
        String test = null;

        try {
            apiKey = options.getString("apiKey");
            secretKey = options.getString("privateKey");
            test = options.getString("test");
        } catch (JSONException var7) {
            var7.printStackTrace();
        }

        assert secretKey != null;
        assert test != null;
        cardClientParams.put("public_key", apiKey);
        cardClientParams.put("tipo_doc", encrypt(onceCredit.getDocType(), secretKey));
        cardClientParams.put("documento", encrypt(onceCredit.getDocNumber(), secretKey));
        if (onceCredit.getExpeditionDate() != null)
            cardClientParams.put("fechaExpedicion", encrypt(onceCredit.getExpeditionDate(), secretKey));
        cardClientParams.put("nombres", encrypt(onceCredit.getName(), secretKey));
        cardClientParams.put("apellidos", encrypt(onceCredit.getLastName(), secretKey));
        cardClientParams.put("email", encrypt(onceCredit.getEmail(), secretKey));
        cardClientParams.put("pais", encrypt(onceCredit.getCountry(), secretKey));
        cardClientParams.put("depto", encrypt(onceCredit.getDepartament(), secretKey));
        cardClientParams.put("ciudad", encrypt(onceCredit.getCity(), secretKey));
        cardClientParams.put("telefono", encrypt(onceCredit.getCellphone(), secretKey));
        cardClientParams.put("celular", encrypt(onceCredit.getPhone(), secretKey));
        cardClientParams.put("direccion", encrypt(onceCredit.getDirection(), secretKey));
        cardClientParams.put("ip", encrypt(Util.getIPAddress(true), secretKey));
        cardClientParams.put("factura", encrypt(onceCredit.getInvoice(), secretKey));
        cardClientParams.put("descripcion", encrypt(onceCredit.getDescription(), secretKey));
        cardClientParams.put("iva", encrypt(onceCredit.getTax(), secretKey));
        cardClientParams.put("baseiva", encrypt(onceCredit.getTaxBase(), secretKey));
        cardClientParams.put("valor", encrypt(onceCredit.getValue(), secretKey));
        cardClientParams.put("moneda", encrypt(onceCredit.getCurrency(), secretKey));
        cardClientParams.put("tarjeta", encrypt(onceCredit.getCard().getNumber(), secretKey));
        cardClientParams.put("fechaexpiracion", encrypt(onceCredit.getCard().getYear() + "-" + onceCredit.getCard().getMonth(), secretKey));
        cardClientParams.put("codigoseguridad", encrypt(onceCredit.getCard().getCvc(), secretKey));
        cardClientParams.put("franquicia", encrypt(getFranchise(onceCredit.getCard().getNumber()), secretKey));
        cardClientParams.put("cuotas", encrypt(onceCredit.getDues(), secretKey));
        cardClientParams.put("extra1", encrypt(onceCredit.getExtra1(), secretKey));
        cardClientParams.put("extra2", encrypt(onceCredit.getExtra2(), secretKey));
        cardClientParams.put("extra3", encrypt(onceCredit.getExtra3(), secretKey));
        cardClientParams.put("url_respuesta", encrypt(onceCredit.getUrlResponse(), secretKey));
        cardClientParams.put("url_confirmacion", encrypt(onceCredit.getUrlConfirmation(), secretKey));
        cardClientParams.put("metodoconfirmacion", encrypt(onceCredit.getConfirmationMethod(), secretKey));
        cardClientParams.put("enpruebas", encrypt((test).toUpperCase(), secretKey));
        cardClientParams.put("i", generateI());
        cardClientParams.put("lenguaje", "android");
        return cardClientParams;
    }

    public static RequestParams hashMapFromTokenCard(TokenCard tokenCard) {
        RequestParams cardData = new RequestParams();
        cardData.put("franchise", tokenCard.getFranchise());
        cardData.put("mask", tokenCard.getMask());
        cardData.put("customer_id", tokenCard.getCustomer_id());
        return cardData;
    }

    public static String getFranchise(String number) {
        if (EpaycoTextUtils.hasAnyPrefix(number, Card.PREFIXES_AMERICAN_EXPRESS))
            return Card.AMERICAN_EXPRESS;
        else if (EpaycoTextUtils.hasAnyPrefix(number, Card.PREFIXES_DINERS_CLUB))
            return Card.DINERS_CLUB;
        else if (EpaycoTextUtils.hasAnyPrefix(number, Card.PREFIXES_DISCOVER)) return Card.DISCOVER;
        else if (EpaycoTextUtils.hasAnyPrefix(number, Card.PREFIXES_JCB)) return Card.JCB;
        else if (EpaycoTextUtils.hasAnyPrefix(number, Card.PREFIXES_MASTERCARD))
            return Card.MASTERCARD;
        else if (EpaycoTextUtils.hasAnyPrefix(number, Card.PREFIXES_VISA)) return Card.VISA;
        else return Card.UNKNOWN;
    }
}

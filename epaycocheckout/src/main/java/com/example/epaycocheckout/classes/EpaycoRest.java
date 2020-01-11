package com.example.epaycocheckout.classes;

import com.example.epaycocheckout.entities.OnceCredit;
import com.example.epaycocheckout.entities.TokenCard;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import co.epayco.android.Epayco;
import co.epayco.android.models.Authentication;
import co.epayco.android.util.EpaycoCallback;

public class EpaycoRest extends Epayco {
    private String apiKey;
    private String privateKey;
    private String lang;
    private boolean test;

    public EpaycoRest(Authentication auth) {
        super(auth);
        this.apiKey = auth.getApiKey();
        this.privateKey = auth.getPrivateKey();
        this.lang = auth.getLang();
        this.test = auth.getTest();
    }

    private JSONObject buildOptions() {
        JSONObject options = new JSONObject();

        try {
            options.put("apiKey", this.apiKey);
            options.put("privateKey", this.privateKey);
            options.put("lang", this.lang);
            options.put("test", this.test);
        } catch (JSONException var3) {
            var3.printStackTrace();
        }

        return options;
    }

    public void cardTransaction(@NonNull OnceCredit onceCredit, @NonNull EpaycoCallback callback) {
        String Base = this.base(true);

        try {
            post(Base + "/restpagos/pagos/comercios.json", EpaycoNetworkUtils2.hashMapFromCardClient(onceCredit, this.buildOptions()), this.apiKey, callback);
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    public void removeToken(TokenCard tokenCard, EpaycoCallback callback) {

        String Base = this.base(false);

        try {
            post(Base + "/v1/remove/token", EpaycoNetworkUtils2.hashMapFromTokenCard(tokenCard), this.apiKey, callback);
        } catch (Exception var5) {
            callback.onError(var5);
        }
    }

    public void getPseBanks(@NonNull EpaycoCallback callback) {
        String Base = this.base(true);

        try {
            get(Base + "/restpagos/pse/bancos.json?public_key=" + this.apiKey, callback);
        } catch (Exception e) {
            callback.onError(e);
        }
    }

}

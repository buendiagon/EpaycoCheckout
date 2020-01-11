package com.example.epaycocheckout.check;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.example.epaycocheckout.R;
import com.example.epaycocheckout.classes.EpaycoRest;
import com.example.epaycocheckout.entities.BankCode;
import com.example.epaycocheckout.entities.Checkout;
import com.example.epaycocheckout.entities.OnceCredit;
import com.example.epaycocheckout.entities.TokenCard;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Stack;

import co.epayco.android.models.Cash;
import co.epayco.android.models.Charge;
import co.epayco.android.models.Client;
import co.epayco.android.models.Pse;
import co.epayco.android.util.EpaycoCallback;

public class CheckoutDialog {
    private Context context;
    private EpaycoRest epayco;
    private TokenCard tokenCard;
    private Checkout checkout;
    private Charge chargeToken;
    private EpaycoCallback callback;
    private boolean createToken = false;
    private Stack<AlertDialog> dialogsToShow = new Stack<>();

    public CheckoutDialog(Context context, EpaycoRest epayco, Checkout checkout, EpaycoCallback callback) {
        this.context = context;
        this.epayco = epayco;
        this.checkout = checkout;
        this.callback = callback;
    }

    public View selectMethod() {
        final View viewMethod = View.inflate(context, GetFormViews.getSelectMethodView(checkout.getForm()), null);
        View btn_card = viewMethod.findViewById(R.id.epayco_btn_card);
        final View btn_pse = viewMethod.findViewById(R.id.epayco_btn_pse);
        View btn_cash = viewMethod.findViewById(R.id.epayco_btn_cash);
        final View container_token = viewMethod.findViewById(R.id.epayco_container_token_card);
        final View btn_token_card = viewMethod.findViewById(R.id.epayco_btn_token_card);
        final View btn_delete_token = viewMethod.findViewById(R.id.epayco_btn_delete_token_card);

        if (checkout.getCustomerId() != null && checkout.getTokenCard() != null) {
            if (!checkout.getCustomerId().isEmpty() && !checkout.getTokenCard().isEmpty())
                getUserCards(container_token);
        } else if (container_token != null) container_token.setVisibility(View.GONE);

        if (btn_card != null) btn_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.disableViewTemporally(view);
                cardPay();
            }
        });

        if (btn_pse != null) btn_pse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.disableViewTemporally(view);
                psePay();
            }
        });

        if (btn_cash != null) btn_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.disableViewTemporally(view);
                cashPay();
            }
        });
        final ListPopupWindow menu = new ListPopupWindow(context);
        if (viewMethod.findViewById(R.id.epayco_spinner_dues) instanceof TextView) {
            final TextView spinner_dues = viewMethod.findViewById(R.id.epayco_spinner_dues);
            spinner_dues.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (spinner_dues instanceof MaterialButton) {
                        ((MaterialButton) spinner_dues).setChecked(false);
                    }
                    if (menu.isShowing()) {
                        menu.dismiss();
                        return;
                    }
                    final ArrayList<String> cuotas = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.cuotas_items)));
                    menu.setAnchorView(view);
                    menu.setDropDownGravity(Gravity.END);
                    menu.setHeight(500);
                    menu.setModal(true);
                    menu.setWidth(ListPopupWindow.WRAP_CONTENT);
                    menu.setAdapter(new ArrayAdapter<>(context, R.layout.dropdown_menu_popup_item, cuotas));
                    menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            spinner_dues.setText(cuotas.get(position));
                            menu.dismiss();
                        }
                    });
                    menu.show();
                }
            });
        }

        if (btn_delete_token != null)
            btn_delete_token.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.disableViewTemporally(view);
                    if (btn_delete_token instanceof MaterialButton) {
                        if (((MaterialButton) btn_delete_token).isChecked()) {
                            ((MaterialButton) btn_delete_token).setChecked(false);
                        }
                    }
                    deleteCard(container_token);
                }
            });

        if (btn_token_card != null) btn_token_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.disableViewTemporally(view);
                if (btn_token_card instanceof MaterialButton)
                    ((MaterialButton) btn_token_card).setChecked(false);
                if (viewMethod.findViewById(R.id.epayco_spinner_dues) != null &&
                        viewMethod.findViewById(R.id.epayco_spinner_dues) instanceof TextView &&
                        !((TextView) viewMethod.findViewById(R.id.epayco_spinner_dues)).getText().toString().isEmpty())
                    tokenCardPay(((TextView) viewMethod.findViewById(R.id.epayco_spinner_dues)).getText().toString());
                else if (viewMethod.findViewById(R.id.epayco_spinner_dues) != null && viewMethod.findViewById(R.id.epayco_spinner_dues) instanceof Spinner)
                    tokenCardPay(((Spinner) viewMethod.findViewById(R.id.epayco_spinner_dues)).getSelectedItem().toString());
                else
                    Toast.makeText(context, "Debes elegir las cuotas.", Toast.LENGTH_SHORT).show();
            }
        });
        showDialog(viewMethod);
        return viewMethod;
    }

    private void getUserCards(final View container_card) {
        if (container_card == null) return;
        final View container_wait = container_card.findViewById(R.id.epayco_container_waiting_card);
        final View container_token = container_card.findViewById(R.id.epayco_container_card);
        final TextView mask = container_card.findViewById(R.id.epayco_card_token_mask);
        if (container_wait == null || container_token == null || mask == null) return;

        container_token.setVisibility(View.GONE);
        container_wait.setVisibility(View.VISIBLE);
        epayco.getCustomer(checkout.getCustomerId(), new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if (jsonObject.optBoolean("status") && jsonObject.optBoolean("success")) {
                    JSONObject data = jsonObject.optJSONObject("data");
                    assert data != null;
                    JSONArray cards = data.optJSONArray("cards");
                    assert cards != null;
                    if (cards.length() > 0) {
                        tokenCard = new TokenCard();
                        chargeToken = new Charge();
                        tokenCard.setCustomer_id(checkout.getCustomerId());
                        tokenCard.setFranchise(cards.optJSONObject(0).optString("franchise"));
                        tokenCard.setMask(cards.optJSONObject(0).optString("mask"));
                        tokenCard.setTokenCard(checkout.getTokenCard());
                        chargeToken.setDocType(data.optString("doc_type"));
                        chargeToken.setDocNumber(data.optString("doc_number"));
                        String[] splitName = data.optString("name").split(" ");
                        StringBuilder name = new StringBuilder();
                        StringBuilder lastName = new StringBuilder();
                        for (int i = 0; i < splitName.length; i++) {
                            if (i <= splitName.length / 2) name.append(splitName[i]).append(" ");
                            else lastName.append(splitName[i]).append(" ");
                        }
                        chargeToken.setName(name.toString());
                        chargeToken.setLastName(lastName.toString());
                        chargeToken.setEmail(data.optString("email"));
                        chargeToken.setInvoice(checkout.getInvoice());
                        chargeToken.setDescription(checkout.getDescription());
                        chargeToken.setValue(checkout.getValue());
                        chargeToken.setTax(checkout.getTax());
                        chargeToken.setTaxBase(checkout.getTaxBase());
                        chargeToken.setCurrency(checkout.getCurrency());
                        chargeToken.setUrlResponse(checkout.getUrlResponse());
                        chargeToken.setUrlConfirmation(checkout.getUrlConfirmation());
                        chargeToken.setCountry(checkout.getCountry());
                        mask.setText(tokenCard.getMask());
                        container_wait.setVisibility(View.GONE);
                        container_token.setVisibility(View.VISIBLE);
                    } else container_card.setVisibility(View.GONE);
                } else onError(new Exception("Not success search"));
            }

            @Override
            public void onError(Exception e) {
                checkout.setCustomerId(null);
                checkout.setTokenCard(null);
                e.printStackTrace();
                container_card.setVisibility(View.GONE);
            }
        });
    }

    private void cardPay() {
        final View cardView = View.inflate(context, GetFormViews.getCardView(checkout.getForm()), null);
        showDialog(cardView);
        Button btn_pay_card = cardView.findViewById(R.id.epayco_btn_pay_card);
        FormErrors.getCardErrors(cardView);
        AutoCompleteTextView spinner_cuotas = (cardView.findViewById(R.id.epayco_spinner_dues) instanceof AutoCompleteTextView) ? (AutoCompleteTextView) cardView.findViewById(R.id.epayco_spinner_dues) : null;
        ArrayList<String> cuotas = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.cuotas_items)));
        if (spinner_cuotas != null) fixMaterializeSpinner(spinner_cuotas, cuotas);
        final CheckBox checked_token_card = cardView.findViewById(R.id.epayco_checked_token_card);
        if (checked_token_card != null) {
            if (checkout.getCustomerId() != null && checkout.getTokenCard() != null) {
                checked_token_card.setVisibility(View.VISIBLE);
                final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.MaterialTheme);
                builder
                        .setTitle("¿Pago en un solo clic?")
                        .setMessage("Al presionar esta casilla, autorizas a ePayco para almacenar de forma segura tu información para futuras compras a un clic.")
                        .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                checked_token_card.setChecked(false);
                            }
                        })
                        .setPositiveButton("ACEPTAR", null)
                        .create();
                checked_token_card.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) builder.show();
                    }
                });
            }
        }
        if (btn_pay_card != null) btn_pay_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.disableViewTemporally(view);
                final OnceCredit cardData = GetFormData.getCardData(cardView, Util.checkoutToOnceCredit(checkout));
                if (!FormErrors.getCardFormErrors(cardView)) {
                    if (checked_token_card != null && checked_token_card.isChecked()) {
                        if (tokenCard != null) {
                            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.MaterialTheme);
                            builder
                                    .setTitle("Asociar nueva tarjeta")
                                    .setMessage("Antes de asociar una nueva tarjeta debes eliminar la actual.")
                                    .setPositiveButton("De acuerdo", null)
                                    .setCancelable(true);
                            builder.create().show();
                            checked_token_card.setChecked(false);
                        } else {
                            createToken = true;
                            createTokenCard(cardData);
                        }
                    } else invoiceCard(cardData);
                }
            }
        });
    }

    private void invoiceCard(final OnceCredit onceCredit) {
        final View invoiceView = View.inflate(context, GetFormViews.getInvoiceView(checkout.getForm()), null);
        showDialog(invoiceView);
        Button btn_pay_card = invoiceView.findViewById(R.id.epayco_btn_pay_card);
        if (btn_pay_card != null) btn_pay_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.disableViewTemporally(view);
                OnceCredit invoiceData = GetFormData.getCardInvoiceData(invoiceView, onceCredit);
                if (!FormErrors.getCardInvoiceFormErrors(invoiceView)) {
                    final AlertDialog waitingDialog = Util.alertWaiting(context, "Espere mientras realizamos la transacción...");
                    if (createToken) createClient(invoiceData, waitingDialog);
                    else epayco.cardTransaction(invoiceData, new EpaycoCallback() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) throws JSONException {
                            waitingDialog.dismiss();
                            jsonObject.put("transaction_type", "creditTransaction");
                            if (jsonObject.has("errores")) {
                                JSONArray errores = jsonObject.optJSONArray("errores");
                                assert errores != null;
                                Toast.makeText(context, errores.optJSONObject(0).optString("errorMessage"), Toast.LENGTH_SHORT).show();
                            } else {
                                removeAllDialogs();
                                callback.onSuccess(jsonObject);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            waitingDialog.dismiss();
                            callback.onError(e);
                        }
                    });
                }
            }
        });
    }

    private void psePay() {
        final View pseView = View.inflate(context, GetFormViews.getPseView(checkout.getForm()), null);
        final Pse pse = Util.checkoutToPse(checkout);
        showDialog(pseView);
        View spinnerBank = pseView.findViewById(R.id.epayco_spinner_bank);
        if (spinnerBank != null) {
            if (Util.bankArray.isEmpty()) {
                callBankList(spinnerBank);
            } else {
                fillSpinnerBanks(spinnerBank);
            }
        }

        Button btn_pay_pse = pseView.findViewById(R.id.epayco_btn_pse_payment);
        if (btn_pay_pse != null) btn_pay_pse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.disableViewTemporally(view);
                Pse pseData = GetFormData.getPseData(pseView, pse);
                if (!FormErrors.getPseFormErrors(pseView)) {
                    final AlertDialog waitingDialog = Util.alertWaiting(context, "Espere mientras realizamos la transacción");
                    EpaycoCallback call = new EpaycoCallback() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) throws JSONException {
                            waitingDialog.dismiss();
                            jsonObject.put("transaction_type", "pseTransaction");
                            if (jsonObject.has("urlbanco") && jsonObject.optString("urlbanco").equals("")) {
                                Toast.makeText(context, "La url del banco es vacia, pon Epayco en modo producción", Toast.LENGTH_LONG).show();
                            } else {
                                if (jsonObject.has("errores")) {
                                    JSONArray errores = jsonObject.optJSONArray("errores");
                                    assert errores != null;
                                    Toast.makeText(context, errores.optJSONObject(0).optString("errorMessage"), Toast.LENGTH_SHORT).show();
                                } else {
                                    String url = jsonObject.optString("urlbanco");
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    context.startActivity(browserIntent);
                                    removeAllDialogs();
                                    callback.onSuccess(jsonObject);
                                }
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            waitingDialog.dismiss();
                            callback.onError(e);
                        }
                    };
                    epayco.createPseTransaction(pseData, call);
                }
            }
        });
    }

    private void cashPay() {
        final View cashView = View.inflate(context, GetFormViews.getCashView(checkout.getForm()), null);
        final Cash cash = Util.checkoutToCash(checkout);
        if (checkout.getCashDateEnd() == null) {
            Date dt = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.add(Calendar.DATE, 1);
            dt = c.getTime();
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.CANADA);
            cash.setEndDate(dateFormat.format(dt));
        } else cash.setEndDate(checkout.getCashDateEnd());
        showDialog(cashView);
        Button btn_pay_cash = cashView.findViewById(R.id.epayco_btn_pay_cash);
        if (btn_pay_cash != null) btn_pay_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.disableViewTemporally(view);
                Cash cashData = GetFormData.getCashData(cashView, cash);
                if (!FormErrors.getCashFormErrors(cashView)) {
                    final AlertDialog waitingDialog = Util.alertWaiting(context, "Espere mientras realizamos la transacción...");
                    EpaycoCallback call = new EpaycoCallback() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) throws JSONException {
                            waitingDialog.dismiss();
                            jsonObject.put("transaction_type", "cashTransaction");
                            if (jsonObject.has("errores")) {
                                JSONArray errores = jsonObject.optJSONArray("errores");
                                assert errores != null;
                                Toast.makeText(context, errores.optJSONObject(0).optString("errorMessage"), Toast.LENGTH_SHORT).show();
                            } else {
                                removeAllDialogs();
                                cashResponse(jsonObject);
                                callback.onSuccess(jsonObject);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            waitingDialog.dismiss();
                            callback.onError(e);
                        }
                    };
                    epayco.createCashTransaction(cashData, call);
                }
            }
        });
    }

    private void cashResponse(final JSONObject response) {
        final View responseView = View.inflate(context, GetFormViews.getCashResponseView(checkout.getForm()), null);
        showDialog(responseView);
        TextView txt_indicate = responseView.findViewById(R.id.epayco_text_indicate);
        TextView pin_number = responseView.findViewById(R.id.epayco_pin);
        TextView cod_proyecto = responseView.findViewById(R.id.epayco_codigo_proyecto);
        ImageView img_cash = responseView.findViewById(R.id.epayco_img_cash);
        TextView date_pay = responseView.findViewById(R.id.epayco_date_pay);
        TextView date_expire = responseView.findViewById(R.id.epayco_date_expire);
        TextView total = responseView.findViewById(R.id.epayco_total_cash);
        TextView save_img = responseView.findViewById(R.id.epayco_txt_save);
        Button btn_finalize = responseView.findViewById(R.id.epayco_btn_finalize_cash);

        if (txt_indicate != null)
            txt_indicate.setText(Html.fromHtml("Teniendo en cuenta la <b>fecha de pago</b>, acércate al punto <b>" + response.optString("banco") + "</b> más cercano e indica los siguientes datos:"));
        if (pin_number != null) pin_number.setText(response.optString("ref_payco"));
        if (cod_proyecto != null) cod_proyecto.setText(response.optString("codigoproyecto"));
        switch (response.optString("banco")) {
            case "Efecty":
                if (img_cash != null) img_cash.setImageResource(R.drawable.efecty);
                break;
            case "BALOTO":
                if (img_cash != null) img_cash.setImageResource(R.drawable.baloto);
                break;
            case "GANA":
                if (img_cash != null) img_cash.setImageResource(R.drawable.gana);
                break;
        }
        if (date_pay != null) date_pay.setText(response.optString("fechapago"));
        if (date_expire != null) date_expire.setText(response.optString("fechaexpiracion"));
        NumberFormat formatter = new DecimalFormat("#,###.00");
        String formattedNumber = "";
        try {
            double myNumber = Double.valueOf(response.optString("valor"));
            formattedNumber = "$" + formatter.format(myNumber) + " COP";
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (total != null) total.setText(formattedNumber);
        if (Util.isStoragePermissionGranted(context) && save_img != null) {
            save_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.saveViewOnGallery(view);
                }
            });
        } else if (save_img != null) save_img.setVisibility(View.GONE);
        if (btn_finalize != null) {
            btn_finalize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeDialog();
                }
            });
        }
    }

    private void tokenCardPay(String dues) {
        if (chargeToken != null) {
            chargeToken.setDues(dues);
            createCharge(chargeToken, Util.alertWaiting(context, "Espera mientras realizamos la transacción..."));
        } else {
            Toast.makeText(context, "No se pudo completar la compra", Toast.LENGTH_SHORT).show();
        }
    }

    private void createTokenCard(final OnceCredit cardData) {
        final AlertDialog waitingDialog = Util.alertWaiting(context, "Resolviendo transacción...");
        epayco.createToken(cardData.getCard(), new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                waitingDialog.dismiss();
                if (jsonObject.optString("status").equals("exitoso")) {
                    checkout.setTokenCard(jsonObject.optString("id"));
                    invoiceCard(cardData);
                } else this.onError(new Exception(jsonObject.optString("description")));
            }

            @Override
            public void onError(Exception e) {
                waitingDialog.dismiss();
                Toast.makeText(context, "Ocurrio un error, por favor vuelve a intentarlo más tarde", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createClient(final OnceCredit cardData, final AlertDialog waitingDialog) {
        Client client = Util.onceCreditToClient(cardData);
        client.setTokenId(checkout.getTokenCard());
        epayco.createCustomer(client, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if (jsonObject.optString("status").equals("exitoso")) {
                    checkout.setCustomerId(jsonObject.optString("customerId"));
                    createCharge(Util.onceCreditToCharge(cardData), waitingDialog);
                } else this.onError(new Exception(jsonObject.optString("description")));
            }

            @Override
            public void onError(Exception e) {
                waitingDialog.dismiss();
                e.printStackTrace();
                Toast.makeText(context, "Ocurrio un error, por favor vuelve a intentarlo más tarde", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void createCharge(Charge charge, final AlertDialog waitingDialog) {
        charge.setTokenCard(checkout.getTokenCard());
        charge.setCustomerId(checkout.getCustomerId());
        epayco.createCharge(charge, new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                waitingDialog.dismiss();
                if (jsonObject.has("errors")) {
                    Toast.makeText(context, jsonObject.optString("errors"), Toast.LENGTH_SHORT).show();
                } else {
                    jsonObject.put("customer_id", checkout.getCustomerId());
                    jsonObject.put("token_card", checkout.getTokenCard());
                    jsonObject.put("transaction_type", "creditTokenTransaction");
                    removeAllDialogs();
                    callback.onSuccess(jsonObject);
                }
            }

            @Override
            public void onError(Exception e) {
                waitingDialog.dismiss();
                callback.onError(e);
            }
        });

    }

    private void deleteCard(final View viewToken) {

        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.MaterialTheme);
        builder
                .setTitle("¿Eliminar Tarjeta?")
                .setMessage("Al aceptar se dejara de asociar esta tarjeta a ti.")
                .setNegativeButton("CANCELAR", null)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final AlertDialog waitingDialog = Util.alertWaiting(context, "Eliminando tarjeta...");
                        epayco.removeToken(tokenCard, new EpaycoCallback() {
                            @Override
                            public void onSuccess(JSONObject jsonObject) throws JSONException {
                                waitingDialog.dismiss();
                                if (jsonObject.has("errors")) {
                                    Toast.makeText(context, jsonObject.optString("errors"), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "La tarjeta se ha eliminado con éxito", Toast.LENGTH_SHORT).show();
                                    // Quitar tajeta de selectMethod.
                                    viewToken.setEnabled(false);
                                    viewToken.setVisibility(View.GONE);
                                    tokenCard = null;
                                    jsonObject.put("transaction_type", "deleteCard");
                                    callback.onSuccess(jsonObject);
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(context, "La tarjeta no se pudo eliminar", Toast.LENGTH_SHORT).show();
                                waitingDialog.dismiss();
                            }
                        });
                    }
                })
                .create().show();
    }

    private void callBankList(final View spinnerBank) {
        final AlertDialog waitingDialog = Util.alertWaiting(context, "Cargando bancos...");
        epayco.getPseBanks(new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if (jsonObject.optBoolean("success")) {
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    assert jsonArray != null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject bank = jsonArray.optJSONObject(i);
                        Util.bankArray.add(new BankCode(bank.optInt("bankCode"), bank.optString("bankName")));
                    }
                    fillSpinnerBanks(spinnerBank);
                    waitingDialog.dismiss();
                } else {
                    this.onError(new Exception());
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(context, "La lista de bancos no se pudó cargar", Toast.LENGTH_SHORT).show();
                waitingDialog.dismiss();
                removeDialog();
            }
        });
    }

    private void fillSpinnerBanks(View spinnerView) {
        ArrayList<String> bankName = new ArrayList<>();
        for (BankCode bank : Util.bankArray) {
            bankName.add(bank.getBankName());
        }
        if (spinnerView instanceof AutoCompleteTextView) {
            bankName.remove(0);
            AutoCompleteTextView spinnerBank = (AutoCompleteTextView) spinnerView;
            fixMaterializeSpinner(spinnerBank, bankName);
            return;
        }
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, bankName) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }

        };

        Spinner spinnerBank = (Spinner) spinnerView;
        spinnerBank.setAdapter(spinnerArrayAdapter);
    }

    private void fixMaterializeSpinner(AutoCompleteTextView spinner, ArrayList<String> items) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        context,
                        R.layout.dropdown_menu_popup_item,
                        items);
        spinner.setAdapter(adapter);
    }

    private void fillView(View view) {
        EditText doc_number = view.findViewById(R.id.epayco_doc_number);
        EditText name = view.findViewById(R.id.epayco_name_person);
        EditText last_name = view.findViewById(R.id.epayco_last_name_person);
        EditText email = view.findViewById(R.id.epayco_email);
        final EditText phone_code = view.findViewById(R.id.epayco_spinner_phone_code);
        EditText phone_number = view.findViewById(R.id.epayco_phone_number);

        int doc_position = 0;
        ArrayList<String> docName = new ArrayList<>();
        for (int i = 0; i < Util.getTypeIdArray().size(); i++) {
            docName.add(Util.getTypeIdArray().get(i).getTypeName());
            if (Util.getTypeIdArray().get(i).getTypeCode().equals(checkout.getDocType())) {
                doc_position = i;
            }
        }
        if (view.findViewById(R.id.epayco_spinner_doc_type) instanceof AutoCompleteTextView) {
            AutoCompleteTextView doc_type = view.findViewById(R.id.epayco_spinner_doc_type);
            doc_type.setText(docName.get(doc_position));
            fixMaterializeSpinner(doc_type, docName);
        } else {
            Spinner doc_type = view.findViewById(R.id.epayco_spinner_doc_type);
            if (doc_type != null) {
                ArrayAdapter<String> doc_type_adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, docName);
                doc_type.setAdapter(doc_type_adapter);
                doc_type.setSelection(doc_position);
            }
        }
        if (doc_number != null) doc_number.setText(checkout.getDocNumber());
        if (name != null) name.setText(checkout.getName());
        if (last_name != null) last_name.setText(checkout.getLastName());
        if (email != null) email.setText(checkout.getEmail());
        if (phone_code != null) {
            phone_code.setText(checkout.getPhoneCode());
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.requestFocus();
                    final CountryCodePicker countryCodePicker = new CountryCodePicker(context);
                    countryCodePicker.setDialogKeyboardAutoPopup(false);
                    countryCodePicker.launchCountrySelectionDialog();
                    countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
                        @Override
                        public void onCountrySelected() {
                            phone_code.setText(countryCodePicker.getSelectedCountryCodeWithPlus());
                        }
                    });
                }
            };
            phone_code.setOnClickListener(listener);
        }
        if (phone_number != null) phone_number.setText(checkout.getPhoneNumber());
        Toolbar toolbar = view.findViewById(R.id.epayco_toolbar);

        if (toolbar != null) {
            toolbar.setTitle(checkout.getTitle());
            NumberFormat formatter = new DecimalFormat("#,###");
            double myNumber = Double.valueOf(checkout.getValue());
            String formattedNumber = "$" + formatter.format(myNumber) + " COP";
            toolbar.setSubtitle(formattedNumber);
            if (dialogsToShow.size() > 1) {
                if (toolbar.getNavigationIcon() == null) {
                    toolbar.setNavigationIcon(R.drawable.ic_back);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            removeDialog();
                        }
                    });
                }
            }

            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.epayco_btn_close) {
                        removeAllDialogs();
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    public void showDialog(View view) {
        MaterialAlertDialogBuilder alerta = new MaterialAlertDialogBuilder(context, R.style.MaterialTheme);
        alerta.setView(view);
        alerta.setCancelable(false);
        AlertDialog dialog = alerta.create();
        if (!dialogsToShow.isEmpty()) {
            AlertDialog dialogPeek = dialogsToShow.peek();
            assert dialogPeek != null;
            dialogPeek.hide();
        }
        dialog.show();
        dialogsToShow.push(dialog);
        fillView(view);
    }

    public void removeDialog() {
        if (!dialogsToShow.isEmpty()) {
            dialogsToShow.pop().dismiss();
            if (!dialogsToShow.isEmpty()) dialogsToShow.peek().show();
        }
    }

    public void removeAllDialogs() {
        if (!dialogsToShow.isEmpty()) {
            dialogsToShow.pop().dismiss();
            removeAllDialogs();
        }
    }

}

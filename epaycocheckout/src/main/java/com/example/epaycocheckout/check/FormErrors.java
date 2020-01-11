package com.example.epaycocheckout.check;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ReplacementSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.epaycocheckout.R;
import com.example.epaycocheckout.classes.EpaycoNetworkUtils2;
import com.google.android.material.textfield.TextInputLayout;

import co.epayco.android.models.Card;

class FormErrors {

    private static String franchise = Card.UNKNOWN;
    private static boolean internalStopFormatFlag;

    static void getCardErrors(View cardView) {
        final EditText card_number = cardView.findViewById(R.id.epayco_card_number);
        final EditText expire_card = cardView.findViewById(R.id.epayco_expire_card);
        final EditText cvc = cardView.findViewById(R.id.epayco_cvc);
        final View spinner_dues = cardView.findViewById(R.id.epayco_spinner_dues);
        final InputFilter[] FilterArray = new InputFilter[1];
        if (card_number != null) {
            final TextInputLayout textInputCardNumber = (card_number.getParent().getParent() instanceof TextInputLayout) ? (TextInputLayout) card_number.getParent().getParent() : null;
            FilterArray[0] = new InputFilter.LengthFilter(16);
            card_number.setFilters(FilterArray);
            card_number.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (textInputCardNumber != null)
                        textInputCardNumber.setError(null);
                    int maxLength = getCardLength(s.toString());
                    if (s.length() > maxLength) {
                        s.delete(maxLength, s.length());
                    } else if (s.length() == maxLength) {
                        if (Util.validateNumber(s.toString())) {
                            Util.nextFocus(card_number);
                        } else {
                            if (textInputCardNumber != null)
                                textInputCardNumber.setError("Tarjeta Inválida");
                            else card_number.setError("Tarjeta Inválida");
                        }
                    }
                }
            });
        }
        if (expire_card != null) {
            final TextInputLayout textInputexpireCard = (expire_card.getParent().getParent() instanceof TextInputLayout) ? (TextInputLayout) expire_card.getParent().getParent() : null;
            FilterArray[0] = new InputFilter.LengthFilter(4);
            expire_card.setFilters(FilterArray);
            expire_card.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (internalStopFormatFlag) {
                        return;
                    }
                    internalStopFormatFlag = true;
                    if (textInputexpireCard != null) textInputexpireCard.setError(null);
                    if (getExpireErrors(s)) {
                        if (textInputexpireCard != null)
                            textInputexpireCard.setError("Fecha Inválida");
                        else expire_card.setError("Fecha Inválida");
                    } else if (!getExpireErrors(s) && s.length() == 4) Util.nextFocus(expire_card);
                    formatExpiryDate(s);
                    internalStopFormatFlag = false;
                }
            });
        }
        if (cvc != null) {
            final TextInputLayout textInputCvc = (cvc.getParent().getParent() instanceof TextInputLayout) ? (TextInputLayout) cvc.getParent().getParent() : null;
            FilterArray[0] = new InputFilter.LengthFilter(3);
            cvc.setFilters(FilterArray);
            cvc.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int maxLength = 3;
                    if (franchise.equals(Card.AMERICAN_EXPRESS)) maxLength = 4;
                    FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                    cvc.setFilters(FilterArray);
                    if (textInputCvc != null)
                        textInputCvc.setError(null);
                    if (s.length() == maxLength && !Util.validateCVC(cvc.getText().toString())) {
                        if (textInputCvc != null)
                            textInputCvc.setError("CVC inválido");
                        else cvc.setError("CVC inválido");
                    } else if (s.length() == maxLength) {
                        Util.nextFocus(cvc);
                    }
                }
            });
        }
        if (spinner_dues instanceof EditText) {
            final TextInputLayout textInputSpinnerDues = (spinner_dues.getParent().getParent() instanceof TextInputLayout) ? (TextInputLayout) spinner_dues.getParent().getParent() : null;
            ((EditText) spinner_dues).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        if (textInputSpinnerDues != null) textInputSpinnerDues.setError(null);
                        else ((EditText) spinner_dues).setError(null);
                    }
                }
            });
        }
    }

    private static int getCardLength(String s) {
        String cardFranchise = EpaycoNetworkUtils2.getFranchise(s);
        assert cardFranchise != null;
        franchise = cardFranchise;
        switch (cardFranchise) {
            case Card.DINERS_CLUB:
                return Util.LENGTH_DINERS_CLUB;
            case Card.AMERICAN_EXPRESS:
                return Util.LENGTH_AMERICAN_EXPRESS;
            case Card.UNKNOWN:
                return 4;
            default:
                return Util.LENGTH_COMMON_CARD;
        }
    }

    private static void formatExpiryDate(@NonNull Editable expiryDate) {
        int textLength = expiryDate.length();
        // first remove any previous span
        SlashSpan[] spans = expiryDate.getSpans(0, expiryDate.length(), SlashSpan.class);
        for (SlashSpan span : spans) {
            expiryDate.removeSpan(span);
        }
        // finally add margin spans
        for (int i = 1; i <= ((textLength - 1) / 2); ++i) {
            int end = i * 2 + 1;
            int start = end - 1;
            SlashSpan marginSPan = new SlashSpan();
            expiryDate.setSpan(marginSPan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private static class SlashSpan extends ReplacementSpan {

        SlashSpan() {
        }

        @Override
        public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            float[] widths = new float[end - start];
            float[] slashWidth = new float[1];
            paint.getTextWidths(text, start, end, widths);
            paint.getTextWidths("/", slashWidth);
            int sum = (int) slashWidth[0];
            for (float width : widths) {
                sum += width;
            }
            return sum;
        }

        @Override
        public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
            String xtext = "/" + text.subSequence(start, end);
            canvas.drawText(xtext, 0, xtext.length(), x, y, paint);
        }
    }

    private static boolean getExpireErrors(Editable expiryDate) {
        if (expiryDate.length() > 0) {
            for (int i = 0; i < expiryDate.length(); i++) {
                String character = expiryDate.subSequence(i, i + 1).toString();
                if (character.equals("/") || character.equals(".") || character.equals("-")) {
                    expiryDate.delete(i, i + 1);
                }
            }
        }

        switch (expiryDate.length()) {
            case 1:
                if (Integer.valueOf(expiryDate.toString()) > 1)
                    expiryDate.insert(0, "0");
                break;
            case 2:
                if (Integer.valueOf(expiryDate.toString()) > 12)
                    expiryDate.insert(0, "0");
                break;
            case 4:
                return !Util.validateExpMonth(expiryDate.subSequence(0, 2).toString())
                        || !Util.validateExpYear(expiryDate.subSequence(2, 4).toString());
        }
        return false;
    }

    static boolean getCardFormErrors(View cardView) {
        boolean error = false;
        EditText card_number = cardView.findViewById(R.id.epayco_card_number);
        EditText expire_date = cardView.findViewById(R.id.epayco_expire_card);
        EditText cvc = cardView.findViewById(R.id.epayco_cvc);
        EditText spinner_dues = (cardView.findViewById(R.id.epayco_spinner_dues) instanceof EditText) ? (EditText) cardView.findViewById(R.id.epayco_spinner_dues) : null;
        if (card_number != null && !Util.validateNumber(card_number.getText().toString())) {
            TextInputLayout textInputLayout = (card_number.getParent().getParent() instanceof TextInputLayout) ? ((TextInputLayout) card_number.getParent().getParent()) : null;
            error = true;
            if (textInputLayout != null)
                textInputLayout.setError("Tarjeta Inválida");
            else card_number.setError("Tarjeta Inválida");
        }
        if (expire_date != null
                && (expire_date.getText().toString().length() != 4
                || (!Util.validateExpMonth(expire_date.getText().toString().substring(0, 2)))
                && !Util.validateExpYear(expire_date.getText().toString().substring(2, 4)))) {
            TextInputLayout textInputLayout = (expire_date.getParent().getParent() instanceof TextInputLayout) ? ((TextInputLayout) expire_date.getParent().getParent()) : null;
            error = true;
            if (textInputLayout != null)
                textInputLayout.setError("Fecha Inválida");
            else expire_date.setError("Fecha Inválida");
        }
        int cvcLength = (franchise.equals(Card.AMERICAN_EXPRESS)) ? 4 : 3;
        if (cvc != null && (!Util.validateCVC(cvc.getText().toString()) || cvc.getText().toString().length() != cvcLength)) {
            TextInputLayout textInputLayout = (cvc.getParent().getParent() instanceof TextInputLayout) ? ((TextInputLayout) cvc.getParent().getParent()) : null;
            error = true;
            if (textInputLayout != null)
                textInputLayout.setError("CVC Inválido");
            else cvc.setError("CVC Inválido");
        }
        if (spinner_dues != null && spinner_dues.getText().toString().isEmpty()) {
            final TextInputLayout textInputLayout = (spinner_dues.getParent().getParent() instanceof TextInputLayout) ? ((TextInputLayout) spinner_dues.getParent().getParent()) : null;
            error = true;
            if (textInputLayout != null)
                textInputLayout.setError("Cuotas");
            else spinner_dues.setError("Cuotas");
        }
        return error;
    }

    private static boolean getBasicFormErrors(View view) {
        boolean error = false;
        EditText docType = (view.findViewById(R.id.epayco_spinner_doc_type) instanceof EditText) ? (EditText) view.findViewById(R.id.epayco_spinner_doc_type) : null;
        EditText docNumber = view.findViewById(R.id.epayco_doc_number);
        EditText name = view.findViewById(R.id.epayco_name_person);
        EditText lastName = view.findViewById(R.id.epayco_last_name_person);
        EditText email = view.findViewById(R.id.epayco_email);
        EditText phoneCode = view.findViewById(R.id.epayco_spinner_phone_code);
        EditText phoneNumber = view.findViewById(R.id.epayco_phone_number);
        if (docType != null && docType.getText().toString().isEmpty()) {
            error = true;
            docType.setError("Elija un tipo de documento");
        }
        if (docNumber != null && docNumber.getText().toString().isEmpty()) {
            error = true;
            docNumber.setError("Inserte su documento");
        }
        if (name != null && name.getText().toString().isEmpty()) {
            error = true;
            name.setError("Inserte su nombre completo");
        }
        if (lastName != null && lastName.getText().toString().isEmpty()) {
            error = true;
            lastName.setError("Inserte su apellido completo");
        }
        if (email != null && email.getText().toString().isEmpty()) {
            error = true;
            email.setError("inserte su email");
        }
        if (phoneCode != null && phoneCode.getText().toString().isEmpty()) {
            error = true;
            phoneCode.setError("inserte el código de su país");
        }
        if (phoneNumber != null && phoneNumber.getText().toString().isEmpty()) {
            error = true;
            phoneNumber.setError("inserte su número de móvil");
        }
        return error;
    }

    static boolean getCardInvoiceFormErrors(View invoiceView) {
        return getBasicFormErrors(invoiceView);
    }

    static boolean getPseFormErrors(View pseView) {
        boolean errors = false;
        View spinnerBanks = pseView.findViewById(R.id.epayco_spinner_bank);
        if (spinnerBanks instanceof EditText && ((EditText) spinnerBanks).getText().toString().isEmpty()) {
            errors = true;
            Toast.makeText(pseView.getContext(), "Seleccione un banco", Toast.LENGTH_SHORT).show();
        } else if (spinnerBanks instanceof Spinner && ((Spinner) spinnerBanks).getSelectedItemPosition() == 0) {
            errors = true;
            Toast.makeText(pseView.getContext(), "Seleccione un banco", Toast.LENGTH_SHORT).show();
        }
        return (getBasicFormErrors(pseView) || errors);
    }

    static boolean getCashFormErrors(View cashView) {
        boolean errors = false;
        RadioGroup radioCash = cashView.findViewById(R.id.epayco_radio_cash);
        if (radioCash != null && radioCash.getCheckedRadioButtonId() == -1) {
            errors = true;
            Toast.makeText(cashView.getContext(), "Selecciona el tipo de pago", Toast.LENGTH_SHORT).show();
        }
        return (getBasicFormErrors(cashView) || errors);

    }
}

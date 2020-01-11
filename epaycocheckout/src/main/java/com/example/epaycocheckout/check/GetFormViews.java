package com.example.epaycocheckout.check;

import com.example.epaycocheckout.R;

class GetFormViews {

    static int getSelectMethodView(int form) {
        int resource;
        switch (form) {
            case 1:
                resource = R.layout.select_method_1;
                break;
            case 2:
                resource = R.layout.select_method_2;
                break;
            case 3:
                resource = R.layout.custom_select_method;
                break;
            default:
                resource = R.layout.select_method_1;
        }
        return resource;
    }

    static int getCardView(int form) {
        int resource;
        switch (form) {
            case 1:
                resource = R.layout.card_form_1;
                break;
            case 2:
                resource = R.layout.card_form_2;
                break;
            case 3:
                resource = R.layout.custom_card_form;
                break;
            default:
                resource = R.layout.card_form_1;
        }
        return resource;
    }

    static int getCashResponseView(int form) {
        int resource;
        switch (form) {
            case 1:
                resource = R.layout.response_cash_1;
                break;
            case 2:
                resource = R.layout.response_cash_2;
                break;
            case 3:
                resource = R.layout.custom_response_cash;
                break;
            default:
                resource = R.layout.response_cash_1;
        }
        return resource;
    }

    static int getInvoiceView(int form) {
        int resource;
        switch (form) {
            case 1:
                resource = R.layout.card_invoice_1;
                break;
            case 2:
                resource = R.layout.card_invoice_2;
                break;
            case 3:
                resource = R.layout.custom_card_invoice;
                break;
            default:
                resource = R.layout.card_invoice_1;
        }
        return resource;
    }

    static int getPseView(int form) {
        int resource;
        switch (form) {
            case 1:
                resource = R.layout.pse_form_1;
                break;
            case 2:
                resource = R.layout.pse_form_2;
                break;
            case 3:
                resource = R.layout.custom_pse_form;
                break;
            default:
                resource = R.layout.pse_form_1;
        }
        return resource;
    }

    static int getCashView(int form) {
        int resource;
        switch (form) {
            case 1:
                resource = R.layout.cash_form_1;
                break;
            case 2:
                resource = R.layout.cash_form_2;
                break;
            case 3:
                resource = R.layout.custom_cash_form;
                break;
            default:
                resource = R.layout.cash_form_1;
        }
        return resource;
    }

}

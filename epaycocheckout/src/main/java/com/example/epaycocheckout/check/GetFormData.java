package com.example.epaycocheckout.check;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.epaycocheckout.R;
import com.example.epaycocheckout.entities.OnceCredit;

import co.epayco.android.models.Card;
import co.epayco.android.models.Cash;
import co.epayco.android.models.Pse;

class GetFormData {

    static OnceCredit getCardData(View cardView, OnceCredit onceCredit) {
        Card card = new Card();
        EditText card_number = cardView.findViewById(R.id.epayco_card_number);
        EditText expire_card = cardView.findViewById(R.id.epayco_expire_card);
        EditText cvc = cardView.findViewById(R.id.epayco_cvc);
        if (cardView.findViewById(R.id.epayco_spinner_dues) instanceof AutoCompleteTextView) {
            AutoCompleteTextView spinner_cuotas = cardView.findViewById(R.id.epayco_spinner_dues);
            onceCredit.setDues(spinner_cuotas == null ? null : spinner_cuotas.getText().toString());
        } else {
            Spinner spinner_cuotas = card_number.findViewById(R.id.epayco_spinner_dues);
            onceCredit.setDues(spinner_cuotas == null ? null : spinner_cuotas.getSelectedItem().toString());
        }

        card.setNumber(card_number == null || card_number.getText().toString().isEmpty() ? null : card_number.getText().toString());
        if (expire_card != null && !expire_card.getText().toString().isEmpty()) {
            char [] date = expire_card.getText().toString().toCharArray();
            if(date.length == 4){
                card.setMonth(String.valueOf(date[0]) + date[1]);
                card.setYear("20" + date[2] + date[3]);
            }
        }
        card.setCvc(cvc == null || cvc.getText().toString().isEmpty() ? null : cvc.getText().toString());
        onceCredit.setCard(card);
        return onceCredit;
    }

    static OnceCredit getCardInvoiceData(View cardInvoice, OnceCredit onceCredit) {
        if(cardInvoice.findViewById(R.id.epayco_spinner_doc_type) instanceof AutoCompleteTextView){
            AutoCompleteTextView docType = cardInvoice.findViewById(R.id.epayco_spinner_doc_type);
            if(docType !=null && !docType.getText().toString().isEmpty()){
                for (int i = 0; i < Util.getTypeIdArray().size(); i++) {
                    if (Util.getTypeIdArray().get(i).getTypeName().equals(docType.getText().toString())) {
                        onceCredit.setDocType(Util.getTypeIdArray().get(i).getTypeCode());
                        break;
                    }
                }
            }
        }else {
            Spinner docType = cardInvoice.findViewById(R.id.epayco_spinner_doc_type);
            onceCredit.setDocType(docType == null ? null : Util.getTypeIdArray().get(docType.getSelectedItemPosition()).getTypeCode());
        }
        EditText doc_number = cardInvoice.findViewById(R.id.epayco_doc_number);
        EditText name_person = cardInvoice.findViewById(R.id.epayco_name_person);
        EditText last_name_person = cardInvoice.findViewById(R.id.epayco_last_name_person);
        EditText email = cardInvoice.findViewById(R.id.epayco_email);
        EditText phone_code = cardInvoice.findViewById(R.id.epayco_spinner_phone_code);
        EditText phone_number = cardInvoice.findViewById(R.id.epayco_phone_number);

        onceCredit.setDocNumber(doc_number == null || doc_number.getText().toString().isEmpty() ? null : doc_number.getText().toString());
        onceCredit.setName(name_person == null || name_person.getText().toString().isEmpty() ? null : name_person.getText().toString());
        onceCredit.setLastName(last_name_person == null || last_name_person.getText().toString().isEmpty() ? null : last_name_person.getText().toString());
        onceCredit.setEmail(email == null || email.getText().toString().isEmpty() ? null : email.getText().toString());
        onceCredit.setPhone(phone_number == null || phone_code == null || phone_number.getText().toString().isEmpty() ? null : phone_code.getText().toString() + phone_number.getText().toString());
        return onceCredit;
    }

    static Pse getPseData(View pseView, Pse pse) {
        RadioGroup typePerson = pseView.findViewById(R.id.epayco_radio_group);
        if(pseView.findViewById(R.id.epayco_spinner_bank) instanceof AutoCompleteTextView){
            AutoCompleteTextView spinner_bank = pseView.findViewById(R.id.epayco_spinner_bank);
            if(spinner_bank != null && !spinner_bank.getText().toString().isEmpty()){
                for (int i = 0; i<Util.bankArray.size(); i++){
                    if(spinner_bank.getText().toString().equals(Util.bankArray.get(i).getBankName())){
                        pse.setBank(String.valueOf(Util.bankArray.get(i).getBankCode()));
                        break;
                    }
                }
            }
        }else{
            Spinner spinner_bank = pseView.findViewById(R.id.epayco_spinner_bank);
            pse.setBank(spinner_bank == null || spinner_bank.getSelectedItemPosition() == 0 ? null : String.valueOf(Util.bankArray.get(spinner_bank.getSelectedItemPosition()).getBankCode()));
        }
        if(pseView.findViewById(R.id.epayco_spinner_doc_type) instanceof AutoCompleteTextView){
            AutoCompleteTextView docType = pseView.findViewById(R.id.epayco_spinner_doc_type);
            if(docType !=null && !docType.getText().toString().isEmpty()){
                for (int i = 0; i < Util.getTypeIdArray().size(); i++) {
                    if (Util.getTypeIdArray().get(i).getTypeName().equals(docType.getText().toString())) {
                        pse.setDocType(Util.getTypeIdArray().get(i).getTypeCode());
                        break;
                    }
                }
            }
        }else {
            Spinner docType = pseView.findViewById(R.id.epayco_spinner_doc_type);
            pse.setDocType(docType == null ? null : Util.getTypeIdArray().get(docType.getSelectedItemPosition()).getTypeCode());
        }
        EditText doc_number = pseView.findViewById(R.id.epayco_doc_number);
        EditText name_person = pseView.findViewById(R.id.epayco_name_person);
        EditText last_name_person = pseView.findViewById(R.id.epayco_last_name_person);
        EditText email = pseView.findViewById(R.id.epayco_email);
        EditText phone_code = pseView.findViewById(R.id.epayco_spinner_phone_code);
        EditText phone_number = pseView.findViewById(R.id.epayco_phone_number);

        pse.setTypePerson(typePerson == null ? null : typePerson.getCheckedRadioButtonId() == R.id.juridic_person ? "1" : "0");
        pse.setDocNumber(doc_number == null || doc_number.getText().toString().isEmpty() ? null : doc_number.getText().toString());
        pse.setName(name_person == null || name_person.getText().toString().isEmpty() ? null : name_person.getText().toString());
        pse.setLastName(last_name_person == null || last_name_person.getText().toString().isEmpty() ? null : last_name_person.getText().toString());
        pse.setEmail(email == null || email.getText().toString().isEmpty() ? null : email.getText().toString());
        pse.setPhone(phone_number == null || phone_code == null || phone_number.getText().toString().isEmpty() ? null : phone_code.getText().toString() + phone_number.getText().toString());
        return pse;
    }

    static Cash getCashData(View cashView, Cash cash) {
        RadioGroup radio_cash = cashView.findViewById(R.id.epayco_radio_cash);
        EditText doc_number = cashView.findViewById(R.id.epayco_doc_number);
        EditText name_person = cashView.findViewById(R.id.epayco_name_person);
        EditText last_name_person = cashView.findViewById(R.id.epayco_last_name_person);
        EditText email = cashView.findViewById(R.id.epayco_email);
        EditText phone_code = cashView.findViewById(R.id.epayco_spinner_phone_code);
        EditText phone_number = cashView.findViewById(R.id.epayco_phone_number);


        if (radio_cash != null) {
            if (radio_cash.getCheckedRadioButtonId() == R.id.epayco_radio_efecty) {
                cash.setType("efecty");
            } else if (radio_cash.getCheckedRadioButtonId() == R.id.epayco_radio_baloto) {
                cash.setType("baloto");
            } else if (radio_cash.getCheckedRadioButtonId() == R.id.epayco_radio_gana) {
                cash.setType("gana");
            }
        }

        if(cashView.findViewById(R.id.epayco_spinner_doc_type) instanceof AutoCompleteTextView){
            AutoCompleteTextView docType = cashView.findViewById(R.id.epayco_spinner_doc_type);
            if(docType !=null && !docType.getText().toString().isEmpty()){
                for (int i = 0; i < Util.getTypeIdArray().size(); i++) {
                    if (Util.getTypeIdArray().get(i).getTypeName().equals(docType.getText().toString())) {
                        cash.setDocType(Util.getTypeIdArray().get(i).getTypeCode());
                        break;
                    }
                }
            }
        }else {
            Spinner docType = cashView.findViewById(R.id.epayco_spinner_doc_type);
            cash.setDocType(docType == null ? null : Util.getTypeIdArray().get(docType.getSelectedItemPosition()).getTypeCode());
        }
        cash.setDocNumber(doc_number == null || doc_number.getText().toString().isEmpty() ? null : doc_number.getText().toString());
        cash.setName(name_person == null || name_person.getText().toString().isEmpty() ? null : name_person.getText().toString());
        cash.setLastName(last_name_person == null || last_name_person.getText().toString().isEmpty() ? null : last_name_person.getText().toString());
        cash.setEmail(email == null || email.getText().toString().isEmpty() ? null : email.getText().toString());
        cash.setPhone(phone_number == null || phone_code == null || phone_number.getText().toString().isEmpty() ? null : phone_code.getText().toString() + phone_number.getText().toString());
        return cash;
    }

}

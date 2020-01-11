# EpaycoCheckout

## Description

EpaycoCheckout for android facilitates the collection of credit card information without having sensitive data on your server.

These epayco methods can be used to generate data in your android application. If you have an application that charges by credit card, you must use the EpaycoCheckout library to prevent sensitive information from remaining on your server.

Additionally this library allows you have a custom checkout with a few lines of code.

This library is based in the original [epayco sdk for android](https://github.com/epayco/epayco-android), the methods of the original library are also available from this one, so if you want more information about the methods in the original library please check this [link](https://github.com/epayco/epayco-android)

## [](https://github.com/buendiagon/EpaycoCheckout#installation)Installation

Add maven  [https://jitpack.io](https://jitpack.io/)  to repositories

    allprojects {
      repositories {
        ......
        maven { url "https://jitpack.io" }
      }
    }

Add the dependency

    implementation 'com.github.buendiagon:epaycocheckout:1.0.0'
## Usage

    Authentication auth = new Authentication();
    
    auth.setApiKey("YOU_PUBLIC_API_KEY");
    auth.setPrivateKey("YOU_PRIVATE_API_KEY");
    auth.setLang("ES");
    auth.setTest(true);
    
    EpaycoRest epayco = new EpaycoRest(auth);

 ## Basic data

    Checkout checkout = new Checkout();  

      // REQUERIDOS  
    checkout.setInvoice("OR - 1234");  
    checkout.setTitle("Compra vestido");  
    checkout.setDescription("Compra de un vestido rojo");  
    checkout.setValue("30000");  
    checkout.setTax("0");  
    checkout.setTaxBase("0");  
    checkout.setCurrency("COP");  
    checkout.setCountry("CO");

      // OPCIONALES  
    checkout.setForm(2);  
    checkout.setDocType("CC");  
    checkout.setDocNumber("1035851980");  
    checkout.setName("John");  
    checkout.setLastName("Doe");  
    checkout.setEmail("example@email.com");  
    checkout.setPhoneCode("+57");  
    checkout.setPhoneNumber("3010000001");  
    checkout.setUrlConfirmation("");  
    checkout.setUrlResponse("");  
    checkout.setCashDateEnd("YYYY-MM-DD");  
    
    //si se insertan null no activan la función de pago en un solo clic
    checkout.setCustomerId("");  
    checkout.setTokenCard("");

    // checkoutDialog recibe como parametro (Context, Epayco, Checkout, EpaycoCallback)  
    CheckoutDialog checkoutDialog = new CheckoutDialog(MainActivity.this, epayco, checkout, new EpaycoCallback() {  
        @Override  
      public void onSuccess(JSONObject jsonObject) {  
      //El tipo de transacción viene dado por el parametro "transaction_type" dentro del json
      Toast.makeText(MainActivity.this, "La transacción se realizo con éxito", Toast.LENGTH_SHORT).show();  
      }  
      
        @Override  
      public void onError(Exception e) {  
            Toast.makeText(MainActivity.this, "Ocurrio un error con los servidores de epayco", Toast.LENGTH_SHORT).show();  
      e.printStackTrace();  
      }  
    });  
      
    // start checkout  
    View view = checkoutDialog.selectMethod();
## Additional methods
### Card Transaction (no token)

    OnceCredit onceCredit = new OnceCredit();  
      
    // Required  
    onceCredit.setDocType("CC");  
    onceCredit.setDocNumber("1035851980");  
    onceCredit.setName("John");  
    onceCredit.setLastName("Doe");  
    onceCredit.setEmail("example@email.com");  
    onceCredit.setInvoice("OR-1234");  
    onceCredit.setDescription("Test payment");  
    onceCredit.setValue("116000");  
    onceCredit.setTax("16000");  
    onceCredit.setTaxBase("100000");  
    onceCredit.setPhone("3010000001");  
    onceCredit.setCurrency("COP");  
    onceCredit.setCountry("CO");  
    onceCredit.setUrlResponse("https:/secure.payco.co/restpagos/testRest/endpagopse.php");  
    onceCredit.setUrlConfirmation("https:/secure.payco.co/restpagos/testRest/endpagopse.php");  
    onceCredit.setConfirmationMethod("POST");  
    onceCredit.setDues("6");  
      
      
    // Optional  
    onceCredit.setExtra1("");  
    onceCredit.setExtra2("");  
    onceCredit.setExtra3("");  
    onceCredit.setExpeditionDate("2020-06");  
    onceCredit.setDirection("");  
    onceCredit.setDepartament("");  
    onceCredit.setCity("");  
    onceCredit.setCellphone("");  
      
    epayco.cardTransaction(onceCredit, new EpaycoCallback() {  
        @Override  
      public void onSuccess(JSONObject jsonObject) throws JSONException {  
              
        }  
      
        @Override  
      public void onError(Exception e) {  
      
        }  
    });
### Pse banks

    epayco.getPseBanks(new EpaycoCallback() {  
        @Override  
      public void onSuccess(JSONObject jsonObject) throws JSONException {  
              
        }  
      
        @Override  
      public void onError(Exception e) {  
      
        }  
    });
### remove card token

    TokenCard tokenCard = new TokenCard();  
      
    tokenCard.setCustomer_id("f3DitY7tgtbxK5pEz");  
    tokenCard.setFranchise("visa");  
    tokenCard.setMask("457562******0326");  
      
    epayco.removeToken(tokenCard, new EpaycoCallback() {  
        @Override  
      public void onSuccess(JSONObject jsonObject) throws JSONException {  
      
        }  
      
        @Override  
      public void onError(Exception e) {  
      
        }  
    });

## Limit pay methods

    // start checkout  
    View view = checkoutDialog.selectMethod();  
    
    View card_transaction = view.findViewById(R.id.epayco_btn_card);  
    View pse_transaction = view.findViewById(R.id.epayco_btn_pse);  
    View cash_transaction = view.findViewById(R.id.epayco_btn_cash);  
      
    card_transaction.setVisibility(View.GONE); // no card transactions 
    pse_transaction.setVisibility(View.GONE);  // no pse transactions
    cash_transaction.setVisibility(View.GONE); // no cash transactions

## Add or remove views on dialog
this can help to show a response when the transaction ends.

    checkoutDialog.removeAllDialogs();  
    checkoutDialog.removeDialog();  
    checkoutDialog.showDialog(view);
## Modify view of checkout
### colors
in colors.xml

    <resources>  
     <color name="colorPrimary">#6200EE</color>  
     <color name="colorPrimaryDark">#3700B3</color>  
     <color name="colorAccent">#BB86FC</color>  
     <color name="colorOnPrimary">#FFFFFF</color>  
     <color name="colorSurface">#FFFFFF</color>  
    </resources>
in styles.xml

    // the style can descent from material design too.
    <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">  
     <item name="colorPrimary">@color/colorPrimary</item>  
     <item name="colorPrimaryDark">@color/colorPrimaryDark</item>  
     <item name="colorAccent">@color/colorAccent</item>  
     <item name="colorSurface">@color/colorSurface</item>  
     <item name="colorOnPrimary">@color/colorOnPrimary</item>  
    </style>
![colors example](https://github.com/buendiagon/EpaycoCheckout/blob/master/imgEpaycoCheckout/colors.png)

### styles for dialogs

    <style name="EpaycoMaterialAlertDialogBodyTextStyle" parent="TextAppearance.AppCompat.Body1">  
     <item name="android:textColor">#FF0000</item>  
    </style>  
    <style name="EpaycoMaterialAlertDialogTitleTextStyle" parent="MaterialAlertDialog.MaterialComponents.Title.Text">  
     <item name="android:textColor">#FF0000</item>  
    </style>  
    <style name="EpaycoMaterialButtonBarPositiveButtonStyle" parent="Widget.MaterialComponents.Button.TextButton.Dialog">  
     <item name="android:textColor">#FF0000</item>  
    </style>  
    <style name="EpaycoMaterialButtonBarNegativeButtonStyle" parent="Widget.MaterialComponents.Button.TextButton.Dialog">  
     <item name="android:textColor">#FF0000</item>  
    </style>

![styles example](https://github.com/buendiagon/EpaycoCheckout/blob/master/imgEpaycoCheckout/styles.png)

### strings.xml
you can modify the dues for card in the string.xml file

    <string-array name="cuotas_items">  
     <item>1</item>  
     <item>2</item>  
     <item>3</item>  
     <item>4</item>  
     <item>5</item>  
     <item>6</item>  
     <item>12</item>  
     <item>24</item>  
     <item>36</item>  
    </string-array>

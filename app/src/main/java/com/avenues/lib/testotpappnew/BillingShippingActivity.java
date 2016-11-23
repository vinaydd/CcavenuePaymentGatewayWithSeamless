package com.avenues.lib.testotpappnew;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import adapter.CardAdapter;
import adapter.CardNameAdapter;
import adapter.EMIAdapter;
import adapter.EMIPlansDTO;
import adapter.PayOptAdapter;
import dto.CardTypeDTO;
import dto.EMIOptionDTO;
import dto.PaymentOptionDTO;
import utility.AvenuesParams;
import utility.Constants;
import utility.ServiceHandler;
import utility.ServiceUtility;


public class BillingShippingActivity extends Activity {

    Intent initialScreen;

    Map<String,ArrayList<CardTypeDTO>> cardsList = new LinkedHashMap<String,ArrayList<CardTypeDTO>>();
    ArrayList<PaymentOptionDTO> payOptionList = new ArrayList<PaymentOptionDTO>();
    ArrayList<EMIOptionDTO> emiOptionList = new ArrayList<EMIOptionDTO>();

    private ProgressDialog pDialog;

    String selectedPaymentOption;
    CardTypeDTO selectedCardType;

    private EditText billingName, billingAddress,billingCountry, billingState, billingCity, billingZip, billingTel, billingEmail,
            deliveryName, deliveryAddress, deliveryCountry, deliveryState, deliveryCity, deliveryZip,
            deliveryTel, redirectUrl, cancelUrl, cardNumber, cardCvv, expiryMonth, expiryYear, issuingBank,
            rsaKeyUrl,vCardCVV;

    private CheckBox saveCard;

    private Map<String,String> paymentOptions = new LinkedHashMap<String,String>();

    private TextView orderId;

    private JSONObject jsonRespObj;

    private String emiPlanId, emiTenureId, amount, currency, cardName, allowedBins;

    int counter;

    private void init(){
        billingName = (EditText) findViewById(R.id.billingName);
        billingAddress = (EditText) findViewById(R.id.billingAddress);
        billingCountry = (EditText) findViewById(R.id.billingCountry);
        billingState = (EditText) findViewById(R.id.billingState);
        billingCity = (EditText) findViewById(R.id.billingCity);
        billingZip = (EditText) findViewById(R.id.billingZip);
        billingTel = (EditText) findViewById(R.id.billingTel);
        billingEmail = (EditText) findViewById(R.id.billingEmail);
        deliveryName = (EditText) findViewById(R.id.deliveryName);
        deliveryAddress = (EditText) findViewById(R.id.deliveryAddress);
        deliveryCountry = (EditText) findViewById(R.id.deliveryCountry);
        deliveryState = (EditText) findViewById(R.id.deliveryState);
        deliveryCity = (EditText) findViewById(R.id.deliveryCity);
        deliveryZip = (EditText) findViewById(R.id.deliveryZip);
        deliveryTel = (EditText) findViewById(R.id.deliveryTel);
        redirectUrl = (EditText) findViewById(R.id.redirectUrl);
        cancelUrl = (EditText) findViewById(R.id.cancelUrl);
        orderId= (TextView) findViewById(R.id.orderId);
        cardNumber= (EditText) findViewById(R.id.cardNumber);
        cardCvv= (EditText) findViewById(R.id.cardCVV);
        expiryYear= (EditText) findViewById(R.id.expiryYear);
        expiryMonth= (EditText) findViewById(R.id.expiryMonth);
        issuingBank= (EditText) findViewById(R.id.issuingBank);
        rsaKeyUrl= (EditText) findViewById(R.id.rsaUrl);
        saveCard= (CheckBox) findViewById(R.id.saveCard);
        vCardCVV= (EditText) findViewById(R.id.vCardCVV);

        //generating order number
        Integer randomNum = ServiceUtility.randInt(0, 9999999);
        orderId.setText(randomNum.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_shipping);

        // Initializing all fields
        init();

        //Setting values which were fetched from previous intent
        initialScreen = getIntent();

        // Calling async task to get json
        new GetData().execute();

        //((LinearLayout) findViewById(R.id.cardDetails)).setVisibility(View.GONE);
    }

    public void onClick(View view) {
        //Mandatory parameters. Other parameters can be added if required.
        String vOrderId = ServiceUtility.chkNull(orderId.getText()).toString().trim();
        String vRsaKeyUrl = ServiceUtility.chkNull(rsaKeyUrl.getText()).toString().trim();
        if(selectedCardType!=null && selectedPaymentOption!=null && !vOrderId.equals("") && !vRsaKeyUrl.equals("")){
            Intent intent = new Intent(this,WebViewActivity.class);
            intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(orderId.getText()).toString().trim());
            intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(initialScreen.getStringExtra(AvenuesParams.ACCESS_CODE)).toString().trim());
            intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(initialScreen.getStringExtra(AvenuesParams.MERCHANT_ID)).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_NAME, ServiceUtility.chkNull(billingName.getText()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_ADDRESS, ServiceUtility.chkNull(billingAddress.getText()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_COUNTRY, ServiceUtility.chkNull(billingCountry.getText()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_STATE, ServiceUtility.chkNull(billingState.getText()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_CITY, ServiceUtility.chkNull(billingCity.getText()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_ZIP, ServiceUtility.chkNull(billingZip.getText()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_TEL, ServiceUtility.chkNull(billingTel.getText()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_EMAIL, ServiceUtility.chkNull(billingEmail.getText()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_NAME, ServiceUtility.chkNull(deliveryName.getText()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_ADDRESS, ServiceUtility.chkNull(deliveryAddress.getText()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_COUNTRY, ServiceUtility.chkNull(deliveryCountry.getText()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_STATE, ServiceUtility.chkNull(deliveryState.getText()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_CITY, ServiceUtility.chkNull(deliveryCity.getText()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_ZIP, ServiceUtility.chkNull(deliveryZip.getText()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_TEL, ServiceUtility.chkNull(deliveryTel.getText()).toString().trim());

            String cardCVV = ServiceUtility.chkNull(cardCvv.getText()).toString().trim();
            if(((LinearLayout)findViewById(R.id.vCardCVVCont)).getVisibility()==0 && vCardCVV.getVisibility()==0){
                cardCVV = ServiceUtility.chkNull(vCardCVV.getText()).toString().trim();
            }
            intent.putExtra(AvenuesParams.CVV, cardCVV);
            intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(redirectUrl.getText()).toString().trim());
            intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(cancelUrl.getText()).toString().trim());
            intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(rsaKeyUrl.getText()).toString().trim());
            intent.putExtra(AvenuesParams.PAYMENT_OPTION, selectedPaymentOption);
            intent.putExtra(AvenuesParams.CARD_NUMBER, ServiceUtility.chkNull(cardNumber.getText()).toString().trim());
            intent.putExtra(AvenuesParams.EXPIRY_YEAR, ServiceUtility.chkNull(expiryYear.getText()).toString().trim());
            intent.putExtra(AvenuesParams.EXPIRY_MONTH, ServiceUtility.chkNull(expiryMonth.getText()).toString().trim());
            intent.putExtra(AvenuesParams.ISSUING_BANK, ServiceUtility.chkNull(issuingBank.getText()).toString().trim());
            if(selectedPaymentOption.equals("OPTEMI")){
                if(ServiceUtility.chkNull(cardNumber.getText()).toString().trim().equals("")){
                    showToast("Card Number is mandatory for EMI payments");return;
                }else if(ServiceUtility.chkNull(cardCvv.getText()).toString().trim().equals("")){
                    showToast("Card CVV is mandatory for EMI payments");return;
                }else if(ServiceUtility.chkNull(expiryMonth.getText()).toString().trim().equals("")){
                    showToast("Expiry month is mandatory for EMI payments");return;
                }else if(ServiceUtility.chkNull(expiryYear.getText()).toString().trim().equals("")){
                    showToast("Expiry year is mandatory for EMI payments");return;
                }
				/* validation for bin nos */
                if(!ServiceUtility.chkNull(allowedBins).equals("") && !ServiceUtility.chkNull(allowedBins).equals("allcards")){
                    String cardBin = cardNumber.getText().toString().substring(0,6);
                    boolean valid = false;
                    String[] bins = allowedBins.split(" ");
                    for(int i=0;i<bins.length;i++){
                        if(bins[i].equals(cardBin)){
                            valid = true; break;
                        }
                    }
                    if(!valid){
                        showToast("This card is not allowed for the selected EMI option");return;
                    }
                }
                intent.putExtra(AvenuesParams.EMI_PLAN_ID, ServiceUtility.chkNull(emiPlanId).toString().trim());
                intent.putExtra(AvenuesParams.EMI_TENURE_ID, ServiceUtility.chkNull(emiTenureId).toString().trim());
                intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(currency).toString().trim());
                intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(amount).toString().trim());
                intent.putExtra(AvenuesParams.CARD_TYPE, "CRDC");
                intent.putExtra(AvenuesParams.CARD_NAME, cardName);
            }else{
                intent.putExtra(AvenuesParams.CARD_TYPE, selectedCardType.getCardType());
                intent.putExtra(AvenuesParams.CARD_NAME, selectedCardType.getCardName());
                intent.putExtra(AvenuesParams.DATA_ACCEPTED_AT, selectedCardType.getDataAcceptedAt()!=null?(selectedCardType.getDataAcceptedAt().equals("CCAvenue")?"Y":"N"):null);
                intent.putExtra(AvenuesParams.CUSTOMER_IDENTIFIER, initialScreen.getStringExtra(AvenuesParams.CUSTOMER_IDENTIFIER));
                intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(initialScreen.getStringExtra(AvenuesParams.CURRENCY)).toString().trim());
                intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(initialScreen.getStringExtra(AvenuesParams.AMOUNT)).toString().trim());
            }
            if(saveCard.isChecked())
                intent.putExtra(AvenuesParams.SAVE_CARD, "Y");
            startActivity(intent);
        }else{
            showToast("Amount/Currency/Access code/Merchant Id & RSA key Url are mandatory."); //More validations can be added as per requirement.
        }
    }


    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(BillingShippingActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            List<NameValuePair> vParams = new ArrayList<NameValuePair>();
            vParams.add(new BasicNameValuePair(AvenuesParams.COMMAND,"getJsonDataVault"));
            vParams.add(new BasicNameValuePair(AvenuesParams.ACCESS_CODE,initialScreen.getStringExtra(AvenuesParams.ACCESS_CODE).toString().trim()));
            vParams.add(new BasicNameValuePair(AvenuesParams.CURRENCY,initialScreen.getStringExtra(AvenuesParams.CURRENCY).toString().trim()));
            vParams.add(new BasicNameValuePair(AvenuesParams.AMOUNT,initialScreen.getStringExtra(AvenuesParams.AMOUNT).toString().trim()));
            vParams.add(new BasicNameValuePair(AvenuesParams.CUSTOMER_IDENTIFIER,initialScreen.getStringExtra(AvenuesParams.CUSTOMER_IDENTIFIER).toString().trim()));

            String vJsonStr = sh.makeServiceCall(Constants.JSON_URL, ServiceHandler.POST, vParams);

            Log.d("Response: ", "> " + vJsonStr);

            if (vJsonStr!=null && !vJsonStr.equals("")) {
                try {
                    try {
                        jsonRespObj = new JSONObject(vJsonStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(jsonRespObj!=null){
                        if(jsonRespObj.getString("payOptions")!=null){
                            JSONArray vPayOptsArr = new JSONArray(jsonRespObj.getString("payOptions"));
                            for(int i=0;i<vPayOptsArr.length();i++){
                                JSONObject vPaymentOption = vPayOptsArr.getJSONObject(i);
                                if(vPaymentOption.getString("payOpt").equals("OPTIVRS")) continue;
                                payOptionList.add(new PaymentOptionDTO(vPaymentOption.getString("payOpt"),vPaymentOption.getString("payOptDesc").toString()));//Add payment option only if it includes any card
                                paymentOptions.put(vPaymentOption.getString("payOpt"),vPaymentOption.getString("payOptDesc"));
                                try{
                                    JSONArray vCardArr = new JSONArray(vPaymentOption.getString("cardsList"));
                                    if(vCardArr.length()>0){
                                        cardsList.put(vPaymentOption.getString("payOpt"), new ArrayList<CardTypeDTO>()); //Add a new Arraylist
                                        for(int j=0;j<vCardArr.length();j++){
                                            JSONObject card = vCardArr.getJSONObject(j);
                                            try{
                                                CardTypeDTO cardTypeDTO = new CardTypeDTO();
                                                cardTypeDTO.setCardName(card.getString("cardName"));
                                                cardTypeDTO.setCardType(card.getString("cardType"));
                                                cardTypeDTO.setPayOptType(card.getString("payOptType"));
                                                cardTypeDTO.setDataAcceptedAt(card.getString("dataAcceptedAt"));
                                                cardTypeDTO.setStatus(card.getString("status"));

                                                cardsList.get(vPaymentOption.getString("payOpt")).add(cardTypeDTO);
                                            }catch (Exception e) { Log.e("ServiceHandler", "Error parsing cardType",e); }
                                        }
                                    }
                                }catch (Exception e) { Log.e("ServiceHandler", "Error parsing payment option",e); }
                            }
                        }
                        if((jsonRespObj.getString("EmiBanks")!=null && jsonRespObj.getString("EmiBanks").length()>0) &&
                                (jsonRespObj.getString("EmiPlans")!=null && jsonRespObj.getString("EmiPlans").length()>0)){
                            paymentOptions.put("OPTEMI","Credit Card EMI");
                            payOptionList.add(new PaymentOptionDTO("OPTEMI", "Credit Card EMI"));
                        }
                    }
                } catch (JSONException e) {
                    Log.e("ServiceHandler", "Error fetching data from server", e);
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            try{
                // bind adapter to spinner
                final Spinner payOpt = (Spinner) findViewById(R.id.payopt);
                PayOptAdapter payOptAdapter = new PayOptAdapter(BillingShippingActivity.this, android.R.layout.simple_spinner_item, payOptionList);
                payOpt.setAdapter(payOptAdapter);

                //set a listener for selected items in the spinner
                payOpt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView parent, View view, int position, long id) {
                        ((LinearLayout) findViewById(R.id.vCardCVVCont)).setVisibility(View.GONE);

                        selectedPaymentOption = payOptionList.get(position).getPayOptId();
                        String vCustPayments = null;
                        try{
                            vCustPayments = jsonRespObj.getString("CustPayments");
                        }catch (Exception e) {}

                        if(counter!=0 || vCustPayments==null){
                            LinearLayout ll = (LinearLayout) findViewById(R.id.cardDetails);
                            if(selectedPaymentOption.equals("OPTDBCRD") ||
                                    selectedPaymentOption.equals("OPTCRDC")){
                                ll.setVisibility(View.VISIBLE);
                            }else{
                                ll.setVisibility(View.GONE);
                            }
                        }


                        if(selectedPaymentOption.equals("OPTEMI")){
                            ((LinearLayout) findViewById(R.id.cardDetails)).setVisibility(View.VISIBLE);
                            ((CheckBox) findViewById(R.id.saveCard)).setVisibility(View.GONE);
                            if(((LinearLayout) findViewById(R.id.vaultCont))!=null)
                                ((LinearLayout) findViewById(R.id.vaultCont)).setVisibility(View.GONE);

                            ((Spinner) findViewById(R.id.cardtype)).setVisibility(View.GONE);
                            ((TextView) findViewById(R.id.cardtypetv)).setVisibility(View.GONE);

                            ((LinearLayout) findViewById(R.id.emiDetails)).removeAllViews();

                            ((LinearLayout) findViewById(R.id.emiOptions)).setVisibility(View.VISIBLE);
                            try{
                                JSONArray vEmiBankArr = new JSONArray(jsonRespObj.getString("EmiBanks"));
                                for(int i=0;i<vEmiBankArr.length();i++){
                                    JSONObject vEmiBank = vEmiBankArr.getJSONObject(i);

                                    EMIOptionDTO vEmiOptionDTO = new EMIOptionDTO();
                                    vEmiOptionDTO.setGtwId(vEmiBank.getString("gtwId"));
                                    vEmiOptionDTO.setGtwName(vEmiBank.getString("gtwName"));
                                    vEmiOptionDTO.setSubventionPaidBy(vEmiBank.getString("subventionPaidBy"));
                                    vEmiOptionDTO.setTenureMonths(vEmiBank.getString("tenureMonths"));
                                    vEmiOptionDTO.setProcessingFeeFlat(vEmiBank.getString("processingFeeFlat"));
                                    vEmiOptionDTO.setProcessingFeePercent(vEmiBank.getString("processingFeePercent"));
                                    vEmiOptionDTO.setCcAvenueFeeFlat(vEmiBank.getString("ccAvenueFeeFlat"));
                                    vEmiOptionDTO.setCcAvenueFeePercent(vEmiBank.getString("ccAvenueFeePercent"));
                                    vEmiOptionDTO.setTenureData(vEmiBank.getString("tenureData"));
                                    vEmiOptionDTO.setPlanId(vEmiBank.getString("planId"));
                                    vEmiOptionDTO.setAccountCurrName(vEmiBank.getString("accountCurrName"));
                                    vEmiOptionDTO.setEmiPlanId(vEmiBank.getString("emiPlanId"));
                                    vEmiOptionDTO.setMidProcesses(vEmiBank.getString("midProcesses"));
                                    vEmiOptionDTO.setBins(vEmiBank.getString("BINs"));

                                    JSONArray vEmiPlanArr = new JSONArray(jsonRespObj.getString("EmiPlans"));
                                    for(int j=0;j<vEmiPlanArr.length();j++){
                                        JSONObject vEmiPlan = vEmiPlanArr.getJSONObject(j);

                                        if(vEmiBank.getString("planId").equals(vEmiPlan.getString("planId"))){
                                            EMIPlansDTO vEmiPlansDTO = new EMIPlansDTO();
                                            vEmiPlansDTO.setGtwId(vEmiPlan.getString("gtwId"));
                                            vEmiPlansDTO.setGtwName(vEmiPlan.getString("gtwName"));
                                            vEmiPlansDTO.setSubventionPaidBy(vEmiBank.getString("subventionPaidBy"));
                                            vEmiPlansDTO.setTenureMonths(vEmiPlan.getString("tenureMonths"));
                                            vEmiPlansDTO.setProcessingFeeFlat(vEmiPlan.getString("processingFeeFlat"));
                                            vEmiPlansDTO.setProcessingFeePercent(vEmiPlan.getString("processingFeePercent"));
                                            vEmiPlansDTO.setCcAvenueFeeFlat(vEmiPlan.getString("ccAvenueFeeFlat"));
                                            vEmiPlansDTO.setCcAvenueFeePercent(vEmiPlan.getString("ccAvenueFeePercent"));
                                            vEmiPlansDTO.setTenureData(vEmiPlan.getString("tenureData"));
                                            vEmiPlansDTO.setPlanId(vEmiPlan.getString("planId"));
                                            vEmiPlansDTO.setAccountCurrName(vEmiPlan.getString("accountCurrName"));
                                            vEmiPlansDTO.setEmiPlanId(vEmiPlan.getString("emiPlanId"));
                                            vEmiPlansDTO.setTenureId(vEmiPlan.getString("tenureId"));
                                            vEmiPlansDTO.setMidProcesses(vEmiPlan.getString("midProcesses"));
                                            vEmiPlansDTO.setEmiAmount(vEmiPlan.getString("emiAmount"));
                                            vEmiPlansDTO.setTotal(vEmiPlan.getString("total"));
                                            vEmiPlansDTO.setEmiProcessingFee(vEmiPlan.getString("emiProcessingFee"));
                                            vEmiPlansDTO.setTenureAmtGreaterThan(vEmiPlan.getString("tenureAmtGreaterThan"));
                                            vEmiPlansDTO.setCurrency(vEmiPlan.getString("currency"));

                                            vEmiOptionDTO.getEmiPlansDTO().add(vEmiPlansDTO);
                                        }
                                    }
                                    emiOptionList.add(vEmiOptionDTO);
                                }
                            }catch (Exception e) {
                                e.printStackTrace();
                            }

                            Spinner emiOption = (Spinner) findViewById(R.id.emiBanks);
                            EMIAdapter emiAdapter = new EMIAdapter(BillingShippingActivity.this, android.R.layout.simple_spinner_item, emiOptionList);
                            emiOption.setAdapter(emiAdapter);

                            emiOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                                @Override
                                public void onItemSelected(AdapterView parent, View view, int position, long id) {
                                    EMIOptionDTO vEmiOptionDTO = (EMIOptionDTO)emiOptionList.get(position);

                                    emiPlanId = vEmiOptionDTO.getPlanId();
                                    allowedBins = vEmiOptionDTO.getBins();

                                    String[] midProcessCards = vEmiOptionDTO.getMidProcesses().split("\\|");
                                    final ArrayList<String> cardNameList = new ArrayList<String>();
                                    for(int i=0;i<midProcessCards.length;i++)
                                        cardNameList.add(midProcessCards[i]);
                                    Spinner emiCardName = (Spinner) findViewById(R.id.emiCardName);
                                    CardNameAdapter cardNameAdapter = new CardNameAdapter(BillingShippingActivity.this, android.R.layout.simple_spinner_item, cardNameList);
                                    emiCardName.setAdapter(cardNameAdapter);

                                    emiCardName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                                        @Override
                                        public void onItemSelected(AdapterView parent, View view, int position, long id) {
                                            cardName = cardNameList.get(position);
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {}
                                    });

                                    final LinearLayout vEmiDetailsCont = (LinearLayout) findViewById(R.id.emiDetails);
                                    vEmiDetailsCont.removeAllViews();

                                    RadioGroup rg = new RadioGroup(BillingShippingActivity.this);
                                    rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
                                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                                            try{
                                                RadioButton rb = (RadioButton) findViewById(checkedId);

                                                EMIPlansDTO vEmiPlanDTO = (EMIPlansDTO)rb.getTag();

                                                emiTenureId = vEmiPlanDTO.getTenureId();
                                                amount = vEmiPlanDTO.getEmiAmount();
                                                currency = vEmiPlanDTO.getCurrency();

                                                TextView vProcFee = new TextView(BillingShippingActivity.this);
                                                vProcFee.setId(R.id.procFee);
                                                if(ServiceUtility.chkNull(vEmiPlanDTO.getSubventionPaidBy()).equals("Customer")){
                                                    if((TextView)findViewById(R.id.procFee)!=null)
                                                        vEmiDetailsCont.removeView((TextView)findViewById(R.id.procFee));

                                                    vProcFee.setText("Processing Fee: "+vEmiPlanDTO.getCurrency()+" "+vEmiPlanDTO.getEmiProcessingFee()+"(Processing fee will be charged only on the first EMI.)");
                                                    vEmiDetailsCont.addView(vProcFee);
                                                }else{
                                                    vEmiDetailsCont.removeView((TextView)findViewById(R.id.procFee));
                                                }
                                            }catch (Exception e) { e.printStackTrace(); }
                                        }
                                    });

                                    Iterator<EMIPlansDTO> vEmiPlanIt = vEmiOptionDTO.getEmiPlansDTO().iterator();
                                    while(vEmiPlanIt.hasNext()){
                                        EMIPlansDTO vEmiPlansDTO = vEmiPlanIt.next();

                                        RadioButton rb = new RadioButton(BillingShippingActivity.this);

                                        String processingFee = !ServiceUtility.chkNull(vEmiPlansDTO.getProcessingFeePercent()).equals("")?
                                                (vEmiPlansDTO.getProcessingFeePercent()+"% p.a."):(vEmiPlansDTO.getProcessingFeeFlat()+" flat p.a.");
                                        rb.setText(vEmiPlansDTO.getTenureMonths()+" EMIs.@ "+processingFee+" - "+vEmiPlansDTO.getCurrency()
                                                +" "+(Math.round(Double.parseDouble(vEmiPlansDTO.getEmiAmount())*100.0)/100.0)+" (Total: "+
                                                vEmiPlansDTO.getCurrency()+" "+(Math.round(Double.parseDouble(vEmiPlansDTO.getTotal())*100.0)/100.0)+")");
                                        rb.setTag(vEmiPlansDTO);
                                        rg.addView(rb);
                                    }
                                    vEmiDetailsCont.addView(rg);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {}
                            });
                        }else{
                            ((Spinner) findViewById(R.id.cardtype)).setVisibility(View.VISIBLE);
                            ((TextView) findViewById(R.id.cardtypetv)).setVisibility(View.VISIBLE);
                            ((CheckBox) findViewById(R.id.saveCard)).setVisibility(View.VISIBLE);
                            ((LinearLayout) findViewById(R.id.emiOptions)).setVisibility(View.GONE);

                            Spinner cardType = (Spinner) findViewById(R.id.cardtype);
                            CardAdapter cardTypeAdapter = new CardAdapter(BillingShippingActivity.this, android.R.layout.simple_spinner_item, cardsList.get(selectedPaymentOption));
                            cardType.setAdapter(cardTypeAdapter);

                            cardType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                                @Override
                                public void onItemSelected(AdapterView parent, View view, int position, long id) {
                                    ((LinearLayout) findViewById(R.id.vCardCVVCont)).setVisibility(View.GONE);
                                    selectedCardType = cardsList.get(selectedPaymentOption).get(position);
                                    if(ServiceUtility.chkNull(selectedPaymentOption).equals("OPTCRDC")
                                            || ServiceUtility.chkNull(selectedPaymentOption).equals("OPTDBCRD")){
                                        if(!ServiceUtility.chkNull(selectedCardType.getDataAcceptedAt()).equals("CCAvenue")){
                                            ((LinearLayout) findViewById(R.id.cardDetails)).setVisibility(View.GONE);
                                            cardNumber.setText("");
                                            expiryMonth.setText("");
                                            expiryYear.setText("");
                                            cardCvv.setText("");
                                            issuingBank.setText("");
                                        }
                                        else{
                                            //Setting default values here
                                            cardNumber.setText("4111111111111111");
                                            expiryMonth.setText("07");
                                            expiryYear.setText("2027");
                                            cardCvv.setText("328");
                                            issuingBank.setText("State Bank of India");
                                            ((LinearLayout) findViewById(R.id.cardDetails)).setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {}
                            });
                        }
                        counter++;
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
                try{
                    if(jsonRespObj!=null){
                        if(jsonRespObj.getString("CustPayments")!=null){
                            final JSONArray vJsonArr = new JSONArray(jsonRespObj.getString("CustPayments"));
                            if(vJsonArr.length()>0){
                                ((LinearLayout) findViewById(R.id.payOptions)).setVisibility(View.GONE);
                                ((LinearLayout) findViewById(R.id.cardDetails)).setVisibility(View.GONE);

                                LinearLayout vDataContainer = (LinearLayout)findViewById(R.id.linDataCont);

                                final LinearLayout vVaultOptionsCont = new LinearLayout(BillingShippingActivity.this);
                                vVaultOptionsCont.setId(R.id.vaultCont);
                                vVaultOptionsCont.setOrientation(LinearLayout.VERTICAL);
                                TextView tv = new TextView(BillingShippingActivity.this);
                                tv.setText("Vault Options");
                                vVaultOptionsCont.addView(tv);

                                RadioGroup rg = new RadioGroup(BillingShippingActivity.this);
                                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                        try{
                                            for(int i=0;i<vJsonArr.length();i++){
                                                JSONObject vVaultOpt = vJsonArr.getJSONObject(i);

                                                if(checkedId==Integer.parseInt(vVaultOpt.getString("payOptId"))){
                                                    selectedCardType = new CardTypeDTO();
                                                    selectedCardType.setCardName(vVaultOpt.getString("payCardName"));
                                                    selectedCardType.setCardType(vVaultOpt.getString("payCardType"));
                                                    selectedCardType.setPayOptType(vVaultOpt.getString("payOption"));

                                                    selectedPaymentOption = vVaultOpt.getString("payOption");

                                                    if(selectedPaymentOption.equals("OPTCRDC") || selectedPaymentOption.equals("OPTDBCRD"))
                                                        ((LinearLayout) findViewById(R.id.vCardCVVCont)).setVisibility(View.VISIBLE);
                                                    else
                                                        ((LinearLayout) findViewById(R.id.vCardCVVCont)).setVisibility(View.GONE);

                                                    String vCardStr = "";
                                                    try{
                                                        vCardStr = vVaultOpt.getString("payCardNo")!=null?vVaultOpt.getString("payCardNo"):cardNumber.getText().toString();
                                                    }catch(Exception e){}

                                                    cardNumber.setText(vCardStr);
                                                }
                                            }
                                        }catch (Exception e) {}
                                    }
                                });
                                for(int i=0;i<vJsonArr.length();i++){
                                    JSONObject vVaultOpt = vJsonArr.getJSONObject(i);

                                    String vCardStr = "";
                                    try{
                                        vCardStr = vVaultOpt.getString("payCardNo")!=null?" - XXXX XXXX XXXX " + vVaultOpt.getString("payCardNo"):"";
                                    }catch(Exception e){}

                                    //Radio Button
                                    String vLblText = paymentOptions.get(vVaultOpt.getString("payOption"))
                                            + " - " + vVaultOpt.getString("payCardName") + vCardStr;
                                    RadioButton rb = new RadioButton(BillingShippingActivity.this);
                                    rb.setId(Integer.parseInt(vVaultOpt.getString("payOptId")));
                                    rb.setText(vLblText);
                                    rb.setTextSize(11);

                                    rg.addView(rb);
                                }
                                vVaultOptionsCont.addView(rg);

                                vDataContainer.addView(vVaultOptionsCont);

                                final CheckBox vChb = new CheckBox(BillingShippingActivity.this);
                                vChb.setText("Pay using other payment option");
                                vChb.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(vChb.isChecked()){
                                            ((LinearLayout) findViewById(R.id.vCardCVVCont)).setVisibility(View.GONE);
                                            selectedPaymentOption = ((PaymentOptionDTO)payOpt.getItemAtPosition(payOpt.getSelectedItemPosition())).getPayOptId();
                                            ((LinearLayout) findViewById(R.id.payOptions)).setVisibility(View.VISIBLE);
                                            if(selectedPaymentOption.equals("OPTDBCRD")
                                                    || selectedPaymentOption.equals("OPTCRDC"))
                                                ((LinearLayout) findViewById(R.id.cardDetails)).setVisibility(View.VISIBLE);
                                            else if(selectedPaymentOption.equals("OPTEMI")){
                                                ((LinearLayout) findViewById(R.id.emiOptions)).setVisibility(View.VISIBLE);
                                                ((LinearLayout) findViewById(R.id.emiDetails)).setVisibility(View.VISIBLE);
                                                ((LinearLayout) findViewById(R.id.cardDetails)).setVisibility(View.VISIBLE);
                                            }
                                            else
                                                ((LinearLayout) findViewById(R.id.cardDetails)).setVisibility(View.GONE);
                                            ((CheckBox) findViewById(R.id.saveCard)).setVisibility(View.VISIBLE);
                                            vVaultOptionsCont.setVisibility(View.GONE);
                                        }
                                        else{
                                            ((LinearLayout) findViewById(R.id.payOptions)).setVisibility(View.GONE);
                                            ((LinearLayout) findViewById(R.id.cardDetails)).setVisibility(View.GONE);
                                            ((LinearLayout) findViewById(R.id.emiOptions)).setVisibility(View.GONE);
                                            ((CheckBox) findViewById(R.id.saveCard)).setVisibility(View.GONE);
                                            vVaultOptionsCont.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });
                                vDataContainer.addView(vChb);
                            }else{
                                ((LinearLayout) findViewById(R.id.payOptions)).setVisibility(View.VISIBLE);
                            }
                        }else{
                            LinearLayout ll = (LinearLayout) findViewById(R.id.cardDetails);
                            if(selectedPaymentOption.equals("OPTDBCRD") ||
                                    selectedPaymentOption.equals("OPTCRDC")){
                                ll.setVisibility(View.VISIBLE);
                            }else{
                                ll.setVisibility(View.GONE);
                            }
                            counter++;
                        }
                    }
                }catch (Exception e) {}
            }catch (Exception e) { showToast("Error loading payment options"); }
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_billing_shipping, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

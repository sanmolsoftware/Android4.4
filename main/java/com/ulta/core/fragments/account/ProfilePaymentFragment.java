package com.ulta.core.fragments.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ulta.R;
import com.ulta.core.activity.account.AddCreditCardActivity;
import com.ulta.core.activity.account.PaymentMethodListActvity;
import com.ulta.core.bean.account.CreditCardInfoBean;
import com.ulta.core.bean.account.PaymentMethodBean;
import com.ulta.core.bean.checkout.PaymentDetailsBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.net.WebserviceUtility;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.log.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ulta.core.conf.UltaConstants.LOADING_PROGRESS_TEXT;
import static com.ulta.core.util.Utility.displayUserErrorMessage;


public class ProfilePaymentFragment extends Fragment {

    public final static int ADD_NEW_CREDIT_CARD_REQ_CODE = 1;

    /**
     * The context.
     */
    Context context;

    ListView listView;
    ViewGroup footer;
    LinearLayout loadingDialog;
    private ProgressDialog pd;

    List<PaymentDetailsBean> listOfCreditCards;
    ProfilePaymentAdapter profilePaymentAdapter;
    PaymentMethodBean paymentMethodBean;

    private OnDeletePayment onDeletePayment;
    private String defCreditId;
    private ImageView cardTypeImage;
    public OnDeletePayment getOnDeletePayment() {
        return onDeletePayment;
    }

    public void setOnDeletePayment(OnDeletePayment onDeletePayment) {
        this.onDeletePayment = onDeletePayment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view1 = inflater.inflate(R.layout.address_profile_fragment,
                container, false);
        listView = (ListView) view1.findViewById(R.id.lists);

        loadingDialog = (LinearLayout) view1
                .findViewById(R.id.loadingprofileAddress);
        loadingDialog.setVisibility(View.GONE);
        footer = (ViewGroup) inflater.inflate(R.layout.footer_credit_card,
                null, false);
        listView.addFooterView(footer, null, false);
        pd = new ProgressDialog(getActivity());
        if (Build.VERSION.SDK_INT >= 21) {
            pd.setIndeterminateDrawable(context.getDrawable(R.drawable.progressdialog_loadingcolor));
        } else {
            pd.setIndeterminateDrawable(getResources().getDrawable(
                    R.drawable.progressdialog_loadingcolor));
        }
        pd.setMessage(LOADING_PROGRESS_TEXT);
        pd.setCancelable(false);

        Button btnAddNewCard = (Button) footer.findViewById(R.id.btnAddNewCard);
        btnAddNewCard.setPadding(20, 20, 20, 20);
        btnAddNewCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intentForaddingCard = new Intent(getActivity(),
                        AddCreditCardActivity.class);
                startActivityForResult(intentForaddingCard,
                        ADD_NEW_CREDIT_CARD_REQ_CODE);
            }
        });

        return view1;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    public void populateCardList(final List<PaymentDetailsBean> listofCards,
                                 String defaultCreditCardId) {
        listOfCreditCards = listofCards;
        profilePaymentAdapter = new ProfilePaymentAdapter(listofCards,
                defaultCreditCardId);
        defCreditId = defaultCreditCardId;
        listView.setAdapter(profilePaymentAdapter);
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }

    }

    public class ProfilePaymentAdapter extends BaseAdapter {
        private List<PaymentDetailsBean> list;
        private String defaultCreditCardId;

        public ProfilePaymentAdapter(List<PaymentDetailsBean> list,
                                     String defaultCreditCardId) {
            this.list = list;
            this.defaultCreditCardId = defaultCreditCardId;
            defCreditId = defaultCreditCardId;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            LayoutInflater inflator = (LayoutInflater) getActivity()
                    .getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.profile_address_fragment,
                    null);
            TextView rootItemText1 = (TextView) convertView
                    .findViewById(R.id.textView1);
            TextView rootItemText2 = (TextView) convertView
                    .findViewById(R.id.textView2);
            cardTypeImage = (ImageView) convertView.findViewById(R.id.cardTypeImage);
            cardTypeImage.setVisibility(View.VISIBLE);
            Button btnDelete = (Button) convertView
                    .findViewById(R.id.btnDelete);
            rootItemText1.setPadding(0, 20, 0, 0);
            rootItemText1.setText(list.get(position).getNickName());
            if (null != list.get(position).getCreditCardNumber()) {
                rootItemText2.setText("Name on card: " + list.get(position).getNameOnCard() + "\n" + "************"
                        + list.get(position).getCreditCardNumber()
                        .substring(12));
            }
            PaymentMethodListActvity paymentMethodListActvity = (PaymentMethodListActvity) context;
            if (paymentMethodListActvity.checkIfExpirationNeeded(list.get(position)
                    .getCreditCardType())) {
                rootItemText2.append("\n" + "Expiration Date: " + list.get(position)
                        .getExpirationMonth()
                        + "/"
                        + list.get(position)
                        .getExpirationYear());
            }
            CreditCardInfoBean cardInfoBean=paymentMethodListActvity.identifyCardType(list.get(position).getCreditCardNumber());
            if(null!=cardInfoBean) {
                LoadImageFromWebOperations(cardInfoBean.getCardImage(),cardTypeImage);
            }
            // Logger.Log(">>>>>>>>>>>>>>>>>>>>NickName in adapter"+list.get(0).getCreditCardNumber());
            btnDelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder alert = new AlertDialog.Builder(
                            getActivity());
                    alert.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    pd.show();
                                    inokeDeleteCardDetails(listOfCreditCards
                                                    .get(position).getNickName(),
                                            position);
                                    Logger.Log(listOfCreditCards.get(position)
                                            .getNickName() + "    " + position);
                                    // checkForLogin();
                                }
                            });
                    alert.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    alert.setMessage("Do you want to delete the Card Details ?");
                    alert.create().show();
					/*
					 * pd.show();
					 * 
					 * Logger.Log(">>>>>>>>>>>>>>>>>>>>NickName in onclick1"
					 * +listOfCreditCards.get(0).getNickName()); Logger.Log
					 * (">>>>>>>>>>>>>>>>>>>>NickName in onclick2"
					 * +list.get(0).getNickName());
					 * 
					 * inokeDeleteCardDetails(listOfCreditCards.get(position)
					 * .getNickName(), position);
					 * Logger.Log(listOfCreditCards.get(position) .getNickName()
					 * + "    " + position);
					 */
                }

            });

            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intentForEditCard = new Intent(getActivity()
                            .getApplicationContext(),
                            AddCreditCardActivity.class);
                    intentForEditCard.putExtra("CardToEdit",
                            listOfCreditCards.get(position));

                    intentForEditCard.putExtra("default", defaultCreditCardId);

                    startActivity(intentForEditCard);
                }
            });
            return convertView;
        }

    }

    private void inokeDeleteCardDetails(String creditCardNickName, int position) {

        InvokerParams<PaymentMethodBean> invokerParams = new InvokerParams<PaymentMethodBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.DELETE_CREDIT_CARD_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams
                .setUrlParameters(populateDeleteCardDetailsHandlerParameters(creditCardNickName));
        invokerParams.setUltaBeanClazz(PaymentMethodBean.class);
        RetrieveDeleteCardDetailsHandler retrieveDeleteCardDetailsHandler = new RetrieveDeleteCardDetailsHandler(
                position);
        invokerParams.setUltaHandler(retrieveDeleteCardDetailsHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<ProfilePaymentFragment><inokeDeleteCardDetails()><UltaException>>"
                    + ultaException);

        }

    }

    /**
     * Populate payment method details handler parameters.
     *
     * @param creditCardNickName
     * @return the map
     */
    private Map<String, String> populateDeleteCardDetailsHandlerParameters(
            String creditCardNickName) {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-properties", "TRUE");
        urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
        urlParams.put("atg-rest-depth", "0");
        urlParams.put("removeCard", creditCardNickName);
        // urlParams.put("deleteCCNickName",creditCardNickName);
        return urlParams;
    }

    /**
     * The Class RetrievePaymentDetailsHandler.
     */
    public class RetrieveDeleteCardDetailsHandler extends UltaHandler {

        int position;

        public RetrieveDeleteCardDetailsHandler(int position) {
            this.position = position;
        }

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<RetrieveDeleteCardDetailsHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            if (pd != null && pd.isShowing()) {
                pd.dismiss();

            }
            if (null != getErrorMessage()) {

                try {
                    displayUserErrorMessage(null,
                            Utility.formatDisplayError(getErrorMessage()),
                            context, null);
                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
            } else {

                Logger.Log("<RetrieveDeleteCardDetailsHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));

                // listInterface.ListInterface();

                String defaultId = listOfCreditCards.get(position).getId();

//                if (defaultId.equals(defCreditId)) {
                    onDeletePayment.OnDeletePayment();
//                }

                listOfCreditCards.remove(position);
                profilePaymentAdapter.notifyDataSetChanged();

                Logger.Log("<RetrieveDeleteCardDetailsHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                Toast.makeText(context, "Deletion Successful",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface OnDeletePayment {
        public void OnDeletePayment();
    }
    /**
     * Download image from url using Aquery and set to the card type
     *
     * @param url
     */
    public void LoadImageFromWebOperations(String url,final ImageView cardTypeImageView) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            AQuery androidQuery = new AQuery(context);
            androidQuery.ajax(url, Bitmap.class, 0, new AjaxCallback<Bitmap>() {
                @Override
                public void callback(String url, Bitmap bitmap, AjaxStatus status) {
                    Drawable drawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 62, false));
                    cardTypeImageView.setImageDrawable(drawable);

                }
            });
        } catch (Exception e) {
            Logger.Log("<UltaException>>"
                    + e);
        }
    }
}

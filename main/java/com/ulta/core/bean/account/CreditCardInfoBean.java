package com.ulta.core.bean.account;


import com.ulta.core.bean.UltaBean;

import java.util.List;

public class CreditCardInfoBean extends UltaBean {

    List<String> cardBINRange;
    String cardImageAndroid;
    String cardMaxCVVLength;
    String cardMaxNumberLength;
    String cardMinNumberLength;
    String cardType;
    Boolean cardUsesCVV;
    Boolean cardUsesExpirationDate;

    public String getCardMinNumberLength() {
        return cardMinNumberLength;
    }

    public void setCardMinNumberLength(String cardMinNumberLength) {
        this.cardMinNumberLength = cardMinNumberLength;
    }

    public List<String> getCardBINRange() {
        return cardBINRange;
    }

    public void setCardBINRange(List<String> cardBINRange) {
        this.cardBINRange = cardBINRange;
    }

    public String getCardImage() {
        return cardImageAndroid;
    }

    public void setCardImage(String cardImageAndroid) {
        this.cardImageAndroid = cardImageAndroid;
    }

    public String getCardMaxCVVLength() {
        return cardMaxCVVLength;
    }

    public void setCardMaxCVVLength(String cardMaxCVVLength) {
        this.cardMaxCVVLength = cardMaxCVVLength;
    }

    public String getCardMaxNumberLength() {
        return cardMaxNumberLength;
    }

    public void setCardMaxNumberLength(String cardMaxNumberLength) {
        this.cardMaxNumberLength = cardMaxNumberLength;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Boolean getCardUsesCVV() {
        return cardUsesCVV;
    }

    public void setCardUsesCVV(Boolean cardUsesCVV) {
        this.cardUsesCVV = cardUsesCVV;
    }

    public Boolean getCardUsesExpirationDate() {
        return cardUsesExpirationDate;
    }

    public void setCardUsesExpirationDate(Boolean cardUsesExpirationDate) {
        this.cardUsesExpirationDate = cardUsesExpirationDate;
    }


}

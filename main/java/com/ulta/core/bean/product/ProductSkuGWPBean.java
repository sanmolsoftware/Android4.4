package com.ulta.core.bean.product;
/**
 * The Class ProductSkuGWPBean.
 * To parse the GWP for each SKU
 */
public class ProductSkuGWPBean {
    String gwpSkuImageUrl;
    String gwpSkuName;
    String promoDescription;
    String promotionId;
    public String getGwpSkuImageUrl() {
        return gwpSkuImageUrl;
    }

    public void setGwpSkuImageUrl(String gwpSkuImageUrl) {
        this.gwpSkuImageUrl = gwpSkuImageUrl;
    }

    public String getGwpSkuName() {
        return gwpSkuName;
    }

    public void setGwpSkuName(String gwpSkuName) {
        this.gwpSkuName = gwpSkuName;
    }

    public String getPromoDescription() {
        return promoDescription;
    }

    public void setPromoDescription(String promoDescription) {
        this.promoDescription = promoDescription;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }


}

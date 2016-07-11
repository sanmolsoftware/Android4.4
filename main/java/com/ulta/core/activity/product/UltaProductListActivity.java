/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */

package com.ulta.core.activity.product;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.UltaBean;
import com.ulta.core.bean.product.FacetDetailBean;
import com.ulta.core.bean.product.FacetsBean;
import com.ulta.core.bean.product.ProductsInListBean;
import com.ulta.core.bean.product.PromotionListBean;
import com.ulta.core.bean.product.RootCategoryBean;
import com.ulta.core.bean.product.RootCategorySearchBean;
import com.ulta.core.bean.search.FacetListingBean;
import com.ulta.core.bean.search.SearchBean;
import com.ulta.core.bean.search.SearchResultsBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.sessiontimeout.interfaces.OnSessionTimeOut;
import com.ulta.core.util.ImageDownloader;
import com.ulta.core.util.ImageDownloader.ImageDownloadListener;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ulta.core.conf.UltaConstants.HOW_MANY;
import static com.ulta.core.conf.UltaConstants.LOADING_PROGRESS_TEXT;

/**
 * The Class UltaProductListActivity.
 */
public class UltaProductListActivity extends UltaBaseActivity implements
        OnClickListener, OnSessionTimeOut, ImageDownloadListener {
    private String productSearched, altText, Nstate;
    private boolean isSearch = false;
    private boolean isShopByBrand = false;
    private boolean isOnSale = false;
    private String sortSearch = "";
    private List<ProductsInListBean> tempListWithAllProducts;
    private boolean isSearchFilter = false;
    private int originalCount = 0;
    private boolean fromScan = false;
    private boolean promotion = false;
    private boolean fromNewArrival = false;
    private boolean isrootCategory = false;

    private String urlForProductDetailsTransition;

    /** The Constant REFINE_PRODUCTS_REQ_CODE. */
    private static final int REFINE_PRODUCTS_REQ_CODE = 1;

    /** The ll view options. */

    /** The grid view. */
    private GridView gridView;

    /** The lv product list. */
    private ListView lvProductList;

    /** The tv item number. */
    private TextView tvItemNumber;
    private TextView tvSortFilterStatus;

    /** The list of products in list bean. */
    private List<ProductsInListBean> listOfProductsInListBean;

    /** The category id. */
    private String categoryId = "";

    /** The brand id. */
    private String brandId = "";
    private String benefitId = "";
    private String colorId = "";
    private String concernId = "";
    private String coverageId = "";
    private String finishId = "";
    private String formId = "";
    private String ingredientId = "";
    private String tyepeId = "";
    private String purposeId = "";
    private String scentId = "";
    private String skinTypeId = "";
    private String spfId = "";
    private String treatmentId = "";
    private String otherIds = "";

    /** Brand ID from shop by brands */
    private String selectedBrandId = "";

    /** The filter price. */
    private String filterPrice = "";

    /** The filter price. */
    private String minPrice = "";

    /** The filter price. */
    private String maxPrice = "";

    /** The filter promtoiton id. */
    private String promotionid = "";

    /** The sort param. */
    String sortParam = "";

    String[] multiSelectionFacerts;

    /** The count. */
    private int count;

    /** The category name. */
    private String categoryName;

    /** The list with all products. */
    private List<ProductsInListBean> listWithAllProducts;

    /** The page num. */
    private int pageNum = 1;

    /** The facets bean. */
    private FacetsBean facetsBean;

    private List<FacetDetailBean> benefitFacets;
    private List<FacetDetailBean> brandFacets;
    private List<FacetDetailBean> colorFacets;
    private List<FacetDetailBean> concernsFacets;
    private List<FacetDetailBean> coverageFacets;
    private List<FacetDetailBean> finishFacets;
    private List<FacetDetailBean> formFacets;
    private List<FacetDetailBean> ingredientsFacets;
    private List<FacetDetailBean> priceFacets;
    private List<FacetDetailBean> purposeFacets;
    private List<FacetDetailBean> scentFacets;
    private List<FacetDetailBean> skinTypeFacets;
    private List<FacetDetailBean> spfFacets;
    private List<FacetDetailBean> treatmentFacets;
    private List<FacetDetailBean> typeFacets;
    private List<FacetDetailBean> promotionFacets;

    private View viewToBePassed;
    private int positionToBePassed;

    /** The custom dialog. */
    private Dialog customDialog;

    /** The spinner type. */
    private String spinnerType;

    /** The product list adapter. */
    private ProductListAdapter productListAdapter;

    private int previousSortSelection;
    AlertDialog sortDialog;

    private int previousFilterSelection;
    AlertDialog filterDialog;
    private ProgressDialog pd;

    FrameLayout loadingDialog;
    private int countOfTotalProducts = 0;

    private ImageView imageView2;// indicates the list or grid view
    private TextView sortTV, filterTV;// to sort or filter the products
    private LinearLayout linearLayout3;// layout of sort and filter
    private LinearLayout sortFilterStatusLayout;// sort filter status layout

    PromotionListBean promoBean;
    private SharedPreferences refreshTimeOutSharedPreferences;

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState
     *            the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list_grid_view);
        loadingDialog = (FrameLayout) findViewById(R.id.loadingDialog);
        loadingDialog.setVisibility(View.VISIBLE);
        pd = new ProgressDialog(UltaProductListActivity.this);
        setProgressDialogLoadingColor(pd);
        pd.setMessage(LOADING_PROGRESS_TEXT);
        pd.setCancelable(false);
        listWithAllProducts = new ArrayList<ProductsInListBean>();
        tempListWithAllProducts = new ArrayList<ProductsInListBean>();
        refreshTimeOutSharedPreferences = getSharedPreferences(
                WebserviceConstants.HOME_BANNER_REFRESH_SHAREDPREF,
                MODE_PRIVATE);
        fnSetViews();
        fnSetOnClickListeners();
        fnGetBundleData();
        filterTV.setEnabled(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Method to set the views.
     */
    public void fnSetViews() {
        gridView = (GridView) findViewById(R.id.gridView1);
        lvProductList = (ListView) findViewById(R.id.lvProductList);
        tvItemNumber = (TextView) findViewById(R.id.tvItemNumber);
        tvSortFilterStatus = (TextView) findViewById(R.id.tvSortFilterStatus);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        sortTV = (TextView) findViewById(R.id.sortTV);
        filterTV = (TextView) findViewById(R.id.filterTV);
        linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
        sortFilterStatusLayout = (LinearLayout) findViewById(R.id.sortFilterStatusLayout);
        tvItemNumber.setTypeface(setHelveticaRegulartTypeFace());
        tvSortFilterStatus.setTypeface(setHelveticaRegulartTypeFace());
    }

    /**
     * Method to set the OnClickListeners.
     */
    private void fnSetOnClickListeners() {
        imageView2.setOnClickListener(this);
        sortTV.setOnClickListener(this);
        filterTV.setOnClickListener(this);
    }

    /**
     * Removed the pop up and added option to sort and filter directly
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView2:
                // check if list is visible the change to grid
                if (lvProductList.getVisibility() == 0) {
                    gridView.setVisibility(GridView.VISIBLE);
                    lvProductList.setVisibility(ListView.GONE);
                    imageView2.setImageResource((R.drawable.main_filer_icon));

                }
                // check if grid is visible then change to list
                else {
                    lvProductList.setVisibility(ListView.VISIBLE);
                    gridView.setVisibility(GridView.GONE);
                    imageView2.setImageResource((R.drawable.grid_icon));

                }

                break;
            case R.id.sortTV:

                showSortOptionsDialog();
                break;

            case R.id.filterTV:

                customDialog = new Dialog(UltaProductListActivity.this,
                        R.style.CustomDialogTheme);
                customDialog.setContentView(R.layout.filterpopup);
                try {
                    customDialog.show();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                LinearLayout llFilterByBrand = (LinearLayout) customDialog
                        .findViewById(R.id.llFilterByBrand);
                LinearLayout llFilterByPrice = (LinearLayout) customDialog
                        .findViewById(R.id.llFilterByPrice);
                LinearLayout llFilterByCategory = (LinearLayout) customDialog
                        .findViewById(R.id.llFilterByCategory);
                LinearLayout llFilterByPromotions = (LinearLayout) customDialog
                        .findViewById(R.id.llFilterByPromotions);
                LinearLayout llFilterByBenefitFacets = (LinearLayout) customDialog
                        .findViewById(R.id.llFilterBenefitFacets);
                LinearLayout llFilterByColorFacets = (LinearLayout) customDialog
                        .findViewById(R.id.llFilterByColorFacets);
                LinearLayout llFilterByConcernsFacets = (LinearLayout) customDialog
                        .findViewById(R.id.llFilterByConcernsFacets);
                LinearLayout llFilterByCoverageFacets = (LinearLayout) customDialog
                        .findViewById(R.id.llFilterByCoverageFacets);
                LinearLayout llFilterByFinishFacets = (LinearLayout) customDialog
                        .findViewById(R.id.llFilterByFinishFacets);
                LinearLayout llFilterByFormFacets = (LinearLayout) customDialog
                        .findViewById(R.id.llFilterByFormFacets);
                LinearLayout llFilterByIngredientsFacets = (LinearLayout) customDialog
                        .findViewById(R.id.llFilterByIngredientsFacets);
                LinearLayout llFilterByPurposeFacets = (LinearLayout) customDialog
                        .findViewById(R.id.llFilterByPurposeFacets);
                LinearLayout llFilterByScentFacets = (LinearLayout) customDialog
                        .findViewById(R.id.llFilterByScentFacets);
                LinearLayout llFilterBySkinTypeFacets = (LinearLayout) customDialog
                        .findViewById(R.id.llFilterBySkinTypeFacets);
                LinearLayout llFilterBySpfFacets = (LinearLayout) customDialog
                        .findViewById(R.id.llFilterBySpfFacets);
                LinearLayout llFilterByTreatmentFacets = (LinearLayout) customDialog
                        .findViewById(R.id.llFilterByTreatmentFacets);
                LinearLayout llFilterByTypeFacets = (LinearLayout) customDialog
                        .findViewById(R.id.llFilterByTypeFacets);
                LinearLayout llFilterByRootBrands = (LinearLayout) customDialog
                        .findViewById(R.id.llFilterByBrand_root);
                LinearLayout llFilterByRootPromotions = (LinearLayout) customDialog
                        .findViewById(R.id.llFilterByPromotions_root);

                Button btnCancel = (Button) customDialog
                        .findViewById(R.id.btnCancel);
                Button btnReset = (Button) customDialog.findViewById(R.id.btnReset);
                if (isOnSale || isSearch || isShopByBrand || fromNewArrival) {
                    if (productsSearched.getFacetListing() != null) {
                        if (productsSearched.getFacetListing().getBrandFacets() != null
                                && productsSearched.getFacetListing()
                                .getBrandFacets().size() != 0) {
                            llFilterByBrand.setVisibility(View.VISIBLE);
                        }
                        if (productsSearched.getFacetListing().getCategoryFacets() != null
                                && productsSearched.getFacetListing()
                                .getCategoryFacets().size() != 0) {
                            llFilterByCategory.setVisibility(View.VISIBLE);
                        }
                        if (productsSearched.getFacetListing().getPriceFacets() != null
                                && productsSearched.getFacetListing()
                                .getPriceFacets().size() != 0) {
                            llFilterByPrice.setVisibility(View.VISIBLE);
                        }
                        if (productsSearched.getFacetListing().getPromotionFacets() != null
                                && productsSearched.getFacetListing()
                                .getPromotionFacets().size() != 0) {
                            llFilterByPromotions.setVisibility(View.VISIBLE);
                        }
                    }
                } else if (promotion) {

                    if (promoBean.getFacetListingForLeaf() != null) {
                        if (promoBean.getFacetListingForLeaf().getBrandFacets() != null
                                && promoBean.getFacetListingForLeaf()
                                .getBrandFacets().size() != 0) {
                            llFilterByBrand.setVisibility(View.VISIBLE);
                        }
                        if (promoBean.getFacetListingForLeaf().getCategoryFacets() != null
                                && promoBean.getFacetListingForLeaf()
                                .getCategoryFacets().size() != 0) {
                            llFilterByCategory.setVisibility(View.VISIBLE);
                        }
                        if (promoBean.getFacetListingForLeaf().getPriceFacets() != null
                                && promoBean.getFacetListingForLeaf()
                                .getPriceFacets().size() != 0) {
                            llFilterByPrice.setVisibility(View.VISIBLE);
                        }

                    }
                } else {
                    if (benefitFacets != null && benefitFacets.size() != 0) {
                        llFilterByBenefitFacets.setVisibility(View.VISIBLE);
                    } else {
                        llFilterByBenefitFacets.setVisibility(View.GONE);
                    }
                    if (brandFacets != null && brandFacets.size() != 0) {
                        llFilterByRootBrands.setVisibility(View.VISIBLE);
                    } else {
                        llFilterByRootBrands.setVisibility(View.GONE);
                    }
                    if (colorFacets != null && colorFacets.size() != 0) {
                        llFilterByColorFacets.setVisibility(View.VISIBLE);
                    } else {
                        llFilterByColorFacets.setVisibility(View.GONE);
                    }

                    // Reported error on playstore : Fixed

                    if (null != productsOfRoot) {
                        if (null != productsOfRoot.getFacetListingForLeaf()) {
                            if (null != productsOfRoot.getFacetListingForLeaf()
                                    .getConcernsFacets()) {
                                if (productsOfRoot.getFacetListingForLeaf()
                                        .getConcernsFacets().size() != 0) {
                                    llFilterByConcernsFacets
                                            .setVisibility(View.VISIBLE);

                                }

                            }
                        }

                        if (productsOfRoot.getFacetListingForLeaf()
                                .getConcernsFacets() != null
                                && productsOfRoot.getFacetListingForLeaf()
                                .getConcernsFacets().size() != 0) {
                            llFilterByConcernsFacets.setVisibility(View.VISIBLE);
                        } else {
                            llFilterByConcernsFacets.setVisibility(View.GONE);
                        }
                        if (productsOfRoot.getFacetListingForLeaf()
                                .getCoverageFacets() != null
                                && productsOfRoot.getFacetListingForLeaf()
                                .getCoverageFacets().size() != 0) {
                            llFilterByCoverageFacets.setVisibility(View.VISIBLE);
                        } else {
                            llFilterByCoverageFacets.setVisibility(View.GONE);
                        }
                        if (productsOfRoot.getFacetListingForLeaf()
                                .getFinishFacets() != null
                                && productsOfRoot.getFacetListingForLeaf()
                                .getFinishFacets().size() != 0) {
                            llFilterByFinishFacets.setVisibility(View.VISIBLE);
                        } else {
                            llFilterByFinishFacets.setVisibility(View.GONE);
                        }
                        if (productsOfRoot.getFacetListingForLeaf().getFormFacets() != null
                                && productsOfRoot.getFacetListingForLeaf()
                                .getFormFacets().size() != 0) {
                            llFilterByFormFacets.setVisibility(View.VISIBLE);
                        } else {
                            llFilterByFormFacets.setVisibility(View.GONE);
                        }
                        if (productsOfRoot.getFacetListingForLeaf()
                                .getIngredientsFacets() != null
                                && productsOfRoot.getFacetListingForLeaf()
                                .getIngredientsFacets().size() != 0) {
                            llFilterByIngredientsFacets.setVisibility(View.VISIBLE);
                        } else {
                            llFilterByIngredientsFacets.setVisibility(View.GONE);
                        }
                        if (productsOfRoot.getFacetListingForLeaf()
                                .getPriceFacets() != null
                                && productsOfRoot.getFacetListingForLeaf()
                                .getPriceFacets().size() != 0) {
                            llFilterByPrice.setVisibility(View.VISIBLE);
                        } else {
                            llFilterByPrice.setVisibility(View.GONE);
                        }
                        if (productsOfRoot.getFacetListingForLeaf()
                                .getPromotionFacets() != null
                                && productsOfRoot.getFacetListingForLeaf()
                                .getPromotionFacets().size() != 0) {
                            llFilterByRootPromotions.setVisibility(View.VISIBLE);
                        } else {
                            llFilterByRootPromotions.setVisibility(View.GONE);
                        }
                        if (productsOfRoot.getFacetListingForLeaf()
                                .getPurposeFacets() != null
                                && productsOfRoot.getFacetListingForLeaf()
                                .getPurposeFacets().size() != 0) {
                            llFilterByPurposeFacets.setVisibility(View.VISIBLE);
                        } else {
                            llFilterByPurposeFacets.setVisibility(View.GONE);
                        }
                        if (productsOfRoot.getFacetListingForLeaf()
                                .getScentFacets() != null
                                && productsOfRoot.getFacetListingForLeaf()
                                .getScentFacets().size() != 0) {
                            llFilterByScentFacets.setVisibility(View.VISIBLE);
                        } else {
                            llFilterByScentFacets.setVisibility(View.GONE);
                        }
                        if (productsOfRoot.getFacetListingForLeaf()
                                .getSkinTypeFacets() != null
                                && productsOfRoot.getFacetListingForLeaf()
                                .getSkinTypeFacets().size() != 0) {
                            llFilterBySkinTypeFacets.setVisibility(View.VISIBLE);
                        } else {
                            llFilterBySkinTypeFacets.setVisibility(View.GONE);
                        }
                        if (productsOfRoot.getFacetListingForLeaf().getSpfFacets() != null
                                && productsOfRoot.getFacetListingForLeaf()
                                .getSpfFacets().size() != 0) {
                            llFilterBySpfFacets.setVisibility(View.VISIBLE);
                        } else {
                            llFilterBySpfFacets.setVisibility(View.GONE);
                        }
                        if (productsOfRoot.getFacetListingForLeaf()
                                .getTreatmentFacets() != null
                                && productsOfRoot.getFacetListingForLeaf()
                                .getTreatmentFacets().size() != 0) {
                            llFilterByTreatmentFacets.setVisibility(View.VISIBLE);
                        } else {
                            llFilterByTreatmentFacets.setVisibility(View.GONE);
                        }
                        if (productsOfRoot.getFacetListingForLeaf().getTypeFacets() != null
                                && productsOfRoot.getFacetListingForLeaf()
                                .getTypeFacets().size() != 0) {
                            llFilterByTypeFacets.setVisibility(View.VISIBLE);
                        } else {
                            llFilterByTypeFacets.setVisibility(View.GONE);
                        }

                    }

                }
                btnCancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
                btnReset.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                        listWithAllProducts.clear();
                        pd.show();
                        pageNum = 1;
                        brandId = "";
                        categoryId = "";
                        promotionid = "";
                        maxPrice = "";
                        minPrice = "";
                        benefitId = "";
                        colorId = "";
                        concernId = "";
                        coverageId = "";
                        finishId = "";
                        formId = "";
                        ingredientId = "";
                        tyepeId = "";
                        purposeId = "";
                        scentId = "";
                        skinTypeId = "";
                        spfId = "";
                        treatmentId = "";
                        tvSortFilterStatus.setText("");
                        sortSearch = "bestSellers";
                        otherIds = "";

                        if (!isSearch) {
                            count = originalCount;
                            tvSortFilterStatus.setText("");
                            tvItemNumber.setText(count + "");
                            previousSortSelection = 0;
                            tvItemNumber.setText("" + count + " Products");
                            if (isOnSale) {
                                invokeOnSaleRootCategoryDetails();
                            } else if (isShopByBrand) {
                                invokeFetchProductsForSelectedBrand();
                            } else if (isrootCategory) {
                                invokeFetchProductsForRootCategory();
                            } else if (fromNewArrival) {
                                invokeNewArrivalDetails();
                            } else if (promotion) {
                                fnInvokeRetrieveProductListByPromoId(
                                        productSearched, pageNum, sortSearch);
                            }
                        } else {
                            fnInvokeSearch();
                        }
                    }
                });
                llFilterByBrand.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (facetsBean != null
                                && facetsBean.getBrandFacets() != null) {
                            showFilterOptionsDialog("Filter by Brands");
                        } else if (brandFacets != null && brandFacets.size() != 0) {
                            showFilterOptionsDialog("Filter by Brands");
                        }
                    }
                });
                llFilterByRootBrands.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (facetsBean != null
                                && facetsBean.getBrandFacets() != null) {
                            showFilterOptionsDialog("Filter by Brands");
                        } else if (brandFacets != null && brandFacets.size() != 0) {
                            showFilterOptionsDialog("Filter by Brands");
                        }
                    }
                });
                llFilterByPrice.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (facetsBean != null
                                && facetsBean.getPriceFacets() != null) {
                            showFilterOptionsDialog("Filter by Price");
                        } else if (priceFacets != null && priceFacets.size() != 0) {
                            showFilterOptionsDialog("Filter by Price");
                        }
                    }
                });
                llFilterByCategory.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (facetsBean != null
                                && facetsBean.getCategoryFacets() != null) {
                            showFilterOptionsDialog("Filter by Categories");
                        }
                    }
                });

                llFilterByPromotions.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (promotionFacets != null) {
                            showFilterOptionsDialog("Filter by Promotions");
                        } else if (promotionFacets != null
                                && promotionFacets.size() != 0) {
                            showFilterOptionsDialog("Filter by Promotions");
                        }
                    }
                });
                llFilterByRootPromotions.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (promotionFacets != null) {
                            showFilterOptionsDialog("Filter by Promotions");
                        } else if (promotionFacets != null
                                && promotionFacets.size() != 0) {
                            showFilterOptionsDialog("Filter by Promotions");
                        }
                    }
                });
                llFilterByBenefitFacets.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (benefitFacets != null && benefitFacets.size() != 0) {
                            showFilterOptionsDialog("Filter by Benefits");
                        }
                    }
                });
                llFilterByColorFacets.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (colorFacets != null && colorFacets.size() != 0) {
                            showFilterOptionsDialog("Filter by Colors");
                        }
                    }
                });
                llFilterByConcernsFacets.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (concernsFacets != null && concernsFacets.size() != 0) {
                            showFilterOptionsDialog("Filter by Concerns");
                        }
                    }
                });
                llFilterByCoverageFacets.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (coverageFacets != null && coverageFacets.size() != 0) {
                            showFilterOptionsDialog("Filter by Coverages");
                        }
                    }
                });
                llFilterByFinishFacets.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (finishFacets != null && finishFacets.size() != 0) {
                            showFilterOptionsDialog("Filter by Finish");
                        }
                    }
                });
                llFilterByFormFacets.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (formFacets != null && formFacets.size() != 0) {
                            showFilterOptionsDialog("Filter by Forms");
                        }
                    }
                });
                llFilterByIngredientsFacets
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (ingredientsFacets != null
                                        && ingredientsFacets.size() != 0) {
                                    showFilterOptionsDialog("Filter by Ingredients");
                                }
                            }
                        });
                llFilterByTypeFacets.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (typeFacets != null && typeFacets.size() != 0) {
                            showFilterOptionsDialog("Filter by Types");
                        }
                    }
                });
                llFilterByTreatmentFacets.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (treatmentFacets != null && treatmentFacets.size() != 0) {
                            showFilterOptionsDialog("Filter by Treatment");
                        }
                    }
                });
                llFilterByPurposeFacets.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (purposeFacets != null && purposeFacets.size() != 0) {
                            showFilterOptionsDialog("Filter by Pupose");
                        }
                    }
                });
                llFilterByScentFacets.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (scentFacets != null && scentFacets.size() != 0) {
                            showFilterOptionsDialog("Filter by Scents");
                        }
                    }
                });
                llFilterBySkinTypeFacets.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (skinTypeFacets != null && skinTypeFacets.size() != 0) {
                            showFilterOptionsDialog("Filter by Skin");
                        }
                    }
                });
                llFilterBySpfFacets.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (spfFacets != null && spfFacets.size() != 0) {
                            showFilterOptionsDialog("Filter by Spf");
                        }
                    }
                });
                // }
                break;
            default:
                break;
        }
    }

    /**
     * Method to set the adapters.
     */
    public void fnSetAdapters() {
        productListAdapter = new ProductListAdapter(this);
        gridView.setAdapter(productListAdapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                viewToBePassed = v;
                positionToBePassed = position;
                UltaDataCache.getDataCacheInstance().setTransitionView(
                        (ImageView) viewToBePassed
                                .findViewById(R.id.imgItemImage));
                UltaProductDetailsActivity.launch(UltaProductListActivity.this,
                        viewToBePassed.findViewById(R.id.imgItemImage),
                        urlForProductDetailsTransition, listWithAllProducts
                                .get(positionToBePassed).getId(),
                        positionToBePassed);

            }
        });
        lvProductList.setAdapter(productListAdapter);
        lvProductList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                viewToBePassed = v;
                positionToBePassed = position;
                UltaProductDetailsActivity.launch(UltaProductListActivity.this,
                        viewToBePassed.findViewById(R.id.imgItemImage),
                        urlForProductDetailsTransition, listWithAllProducts
                                .get(positionToBePassed).getId(),
                        positionToBePassed);

            }
        });
    }

    /**
     * The Class ProductListAdapter.
     */
    public class ProductListAdapter extends BaseAdapter {

        /** The context. */
        private Context context;

        /**
         * Instantiates a new product list adapter.
         *
         * @param context
         *            the context
         */
        public ProductListAdapter(Context context) {
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view;
            if (gridView.getVisibility() == GridView.VISIBLE) {
                view = inflater.inflate(
                        R.layout.product_list_inflate_image_in_grid_view, null);
            } else {
                view = inflater.inflate(
                        R.layout.product_list_inflate_image_in_list_view, null);
            }

            ImageView imgItemImage = (ImageView) view
                    .findViewById(R.id.imgItemImage);
            ImageView imgItemImageduringTransition = (ImageView) view
                    .findViewById(R.id.imgItemImageduringTransition);
            ImageView imgBadgeImage = (ImageView) view
                    .findViewById(R.id.BadgeImage);
            ImageView imgSale = (ImageView) view
                    .findViewById(R.id.img_product_list_sale);
            ImageView offersImageView = (ImageView) view
                    .findViewById(R.id.offersImage);
            TextView tvItemName = (TextView) view.findViewById(R.id.tvItemName);
            TextView tvBrandName = (TextView) view
                    .findViewById(R.id.tvBrandName);
            // 3.3 release text added instead of ad bug image
            TextView tvPromotionalOfferName = (TextView) view
                    .findViewById(R.id.tvPromotionalOfferName);
            tvPromotionalOfferName.setTypeface(setHelveticaRegulartTypeFace());
            tvItemName.setTypeface(setHelveticaRegulartTypeFace());
            tvBrandName.setTypeface(setHelveticaRegulartTypeFace(),
                    Typeface.BOLD);
            final TextView finalPromotionalOfferName = tvPromotionalOfferName;

            ImageView imgRating1 = (ImageView) view
                    .findViewById(R.id.imgRating1);
            ImageView imgRating2 = (ImageView) view
                    .findViewById(R.id.imgRating2);
            ImageView imgRating3 = (ImageView) view
                    .findViewById(R.id.imgRating3);
            ImageView imgRating4 = (ImageView) view
                    .findViewById(R.id.imgRating4);
            ImageView imgRating5 = (ImageView) view
                    .findViewById(R.id.imgRating5);

            TextView tvRating = (TextView) view.findViewById(R.id.tvRating);
            TextView tvActualPrice = (TextView) view
                    .findViewById(R.id.tvActualPrice);
            TextView tvSalePrice = (TextView) view
                    .findViewById(R.id.tvSalePrice);
//			if (promotion) {
//				count = countOfTotalProducts;
//				Logger.Log("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" + count);
//			}
            if (position == (listWithAllProducts.size() - 1)
                    && position < (count - 1)
                    && !(count < Integer.parseInt(HOW_MANY))) {
                Logger.Log("position: " + position + " count: " + count);
                loadingDialog.setVisibility(View.VISIBLE);
                if (isSearch) {
                    pageNum++;
                    fnInvokeSearch();
                } else {
                    pageNum++;
                    if (promotion) {
                        fnInvokeRetrieveProductListByPromoId(productSearched,
                                pageNum, sortParam);
                    } else {
                        if (isOnSale) {
                            invokeOnSaleRootCategoryDetails();
                        } else if (isShopByBrand) {
                            invokeFetchProductsForSelectedBrand();
                        } else if (isrootCategory) {
                            invokeFetchProductsForRootCategory();
                        } else if (fromNewArrival) {
                            invokeNewArrivalDetails();
                        }
                    }
                }
            } else {
                if (null != listWithAllProducts
                        && listWithAllProducts.size() != 0) {

                    if (null != listWithAllProducts.get(position)
                            .getSmallImageUrl()) {

                        if (!refreshTimeOutSharedPreferences
                                .getBoolean(
                                        WebserviceConstants.PROD_LARGE_IMAGE_URL,
                                        false)) {
                            new AQuery(imgItemImage).image(listWithAllProducts
                                            .get(position).getSmallImageUrl(), true,
                                    false, 200, R.drawable.dummy_product, null,
                                    AQuery.FADE_IN);
                            new AQuery(imgItemImageduringTransition).image(
                                    listWithAllProducts.get(position)
                                            .getSmallImageUrl(), true, false,
                                    200, R.drawable.dummy_product, null,
                                    AQuery.FADE_IN);
                            urlForProductDetailsTransition = listWithAllProducts
                                    .get(position).getSmallImageUrl();
                        } else {
                            String largeImageURL = listWithAllProducts
                                    .get(position).getSmallImageUrl()
                                    .replace("$sm$", "$lg$");
                            new AQuery(imgItemImage).image(largeImageURL, true,
                                    false, 200, R.drawable.dummy_product, null,
                                    AQuery.FADE_IN);
                            new AQuery(imgItemImageduringTransition).image(
                                    largeImageURL, true, false, 200,
                                    R.drawable.dummy_product, null,
                                    AQuery.FADE_IN);
                            urlForProductDetailsTransition = largeImageURL;
                        }

                        ImageDownloader imageDownloader = new ImageDownloader(
                                urlForProductDetailsTransition,
                                UltaProductListActivity.this, null, "prodList",
                                position);
                        imageDownloader.execute();

                    } else {
                        imgItemImage.setImageResource(R.drawable.dummy_product);
                    }
                    tvItemName.setText(listWithAllProducts.get(position)
                            .getDisplayName());
                    tvBrandName.setText(listWithAllProducts.get(position)
                            .getBrandName());
                    String badgeName = listWithAllProducts.get(position)
                            .getBadgeName();
                    setBadgeImage(imgBadgeImage, badgeName);
                    double listPriceFrom = listWithAllProducts.get(position)
                            .getListPriceFrom();
                    double listPriceTo = listWithAllProducts.get(position)
                            .getListPriceTo();
                    double salesPriceFrom = listWithAllProducts.get(position)
                            .getSalePriceFrom();
                    double salesPriceTo = listWithAllProducts.get(position)
                            .getSalePriceTo();
                    if (listWithAllProducts.get(position).isHasSkusOnSale()) {
                        imgSale.setVisibility(ImageView.VISIBLE);
                        if (listPriceFrom == listPriceTo
                                && salesPriceFrom == salesPriceTo
                                && listPriceFrom == salesPriceFrom) {
                            tvActualPrice.setText("$"
                                    + String.format("%.2f",
                                    Double.valueOf(listPriceFrom)));
                        } else if (listPriceFrom == listPriceTo
                                && salesPriceFrom == salesPriceTo
                                && listPriceFrom != salesPriceFrom) {
                            tvActualPrice.setText("$"
                                    + String.format("%.2f",
                                    Double.valueOf(listPriceFrom)));
                            tvActualPrice.setPaintFlags(tvActualPrice
                                    .getPaintFlags()
                                    | Paint.STRIKE_THRU_TEXT_FLAG);
                            tvSalePrice.setText("$"
                                    + String.format("%.2f",
                                    Double.valueOf(salesPriceFrom)));
                        } else {
                            if (listPriceFrom == 0.00 && salesPriceFrom == 0.00) {

                                tvActualPrice.setText("$"
                                        + String.format("%.2f",
                                        Double.valueOf(listPriceTo)));
                                if (listPriceTo != salesPriceTo) {

                                    tvActualPrice.setPaintFlags(tvActualPrice
                                            .getPaintFlags()
                                            | Paint.STRIKE_THRU_TEXT_FLAG);
                                    tvSalePrice.setText("$"
                                            + String.format("%.2f", Double
                                            .valueOf(salesPriceTo)));
                                }
                            } else {
                                tvActualPrice.setText("$"
                                        + String.format("%.2f",
                                        Double.valueOf(listPriceFrom))
                                        + "-"
                                        + "$"
                                        + String.format("%.2f",
                                        Double.valueOf(listPriceTo)));
                                tvActualPrice.setPaintFlags(tvActualPrice
                                        .getPaintFlags()
                                        | Paint.STRIKE_THRU_TEXT_FLAG);

                                if (salesPriceFrom == salesPriceTo) {
                                    tvSalePrice.setText("$"
                                            + String.format("%.2f", Double
                                            .valueOf(salesPriceFrom)));
                                } else {
                                    tvSalePrice.setText("$"
                                            + String.format("%.2f", Double
                                            .valueOf(salesPriceFrom))
                                            + "-"
                                            + "$"
                                            + String.format("%.2f", Double
                                            .valueOf(salesPriceTo)));
                                }

                            }
                        }
                        tvActualPrice.setTextColor(getResources().getColor(
                                R.color.sale_price_color));
                        tvSalePrice.setTextColor(getResources().getColor(
                                R.color.marked_down_orignal_price_color));
                    } else {
                        tvActualPrice.setVisibility(View.GONE);
                        tvSalePrice.setTextColor(getResources().getColor(
                                R.color.orignal_price_color));
                        if (listPriceFrom == listPriceTo) {
                            tvSalePrice.setText("$"
                                    + String.format("%.2f", Double
                                    .valueOf(listWithAllProducts.get(
                                            position)
                                            .getListPriceFrom())));
                        } else {
                            tvSalePrice.setText("$"
                                    + String.format("%.2f",
                                    Double.valueOf(listPriceFrom))
                                    + "-"
                                    + "$"
                                    + String.format("%.2f",
                                    Double.valueOf(listPriceTo)));
                        }
                    }

                    tvPromotionalOfferName.setBackgroundColor(getResources()
                            .getColor(R.color.white));
                    if (null != listWithAllProducts.get(position)
                            .getOfferDesc()
                            && !listWithAllProducts.get(position)
                            .getOfferDesc().isEmpty()) {



                        String offerMessages[] = listWithAllProducts
                                .get(position).getOfferDesc().split(";");

                        for (int i = 0; i < offerMessages.length; i++) {
                            if (!offerMessages[i]
                                    .equalsIgnoreCase("Special Free Gift With Purchase")) {
                                if (null != offerMessages[i]) {
                                    tvPromotionalOfferName
                                            .setVisibility(View.VISIBLE);
                                    tvPromotionalOfferName
                                            .setBackgroundColor(getResources()
                                                    .getColor(
                                                            R.color.offers_color));
                                    tvPromotionalOfferName
                                            .setText(offerMessages[i]);
                                    break;
                                }
                            }
                        }

                        // }

                        Log.e("Offerdesc", listWithAllProducts.get(position)
                                .getOfferDesc().toString());
                    }/*
					 * else { tvPromotionalOfferName.setVisibility(View.GONE); }
					 */
                    tvRating.setText("("
                            + (int) listWithAllProducts.get(position)
                            .getReviews() + ")");

                    double rating = listWithAllProducts.get(position)
                            .getRating();
                    if (rating == 0) {
                        imgRating1
                                .setBackgroundResource(R.drawable.icon_star_gray);
                        imgRating2
                                .setBackgroundResource(R.drawable.icon_star_gray);
                        imgRating3
                                .setBackgroundResource(R.drawable.icon_star_gray);
                        imgRating4
                                .setBackgroundResource(R.drawable.icon_star_gray);
                        imgRating5
                                .setBackgroundResource(R.drawable.icon_star_gray);
                    } else if (rating == 0.5) {
                        imgRating1
                                .setBackgroundResource(R.drawable.icon_star_halfcolored);
                        imgRating2
                                .setBackgroundResource(R.drawable.icon_star_gray);
                        imgRating3
                                .setBackgroundResource(R.drawable.icon_star_gray);
                        imgRating4
                                .setBackgroundResource(R.drawable.icon_star_gray);
                        imgRating5
                                .setBackgroundResource(R.drawable.icon_star_gray);
                    } else if (rating == 1) {
                        imgRating1
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating2
                                .setBackgroundResource(R.drawable.icon_star_gray);
                        imgRating3
                                .setBackgroundResource(R.drawable.icon_star_gray);
                        imgRating4
                                .setBackgroundResource(R.drawable.icon_star_gray);
                        imgRating5
                                .setBackgroundResource(R.drawable.icon_star_gray);
                    } else if (rating == 1.5) {
                        imgRating1
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating2
                                .setBackgroundResource(R.drawable.icon_star_halfcolored);
                        imgRating3
                                .setBackgroundResource(R.drawable.icon_star_gray);
                        imgRating4
                                .setBackgroundResource(R.drawable.icon_star_gray);
                        imgRating5
                                .setBackgroundResource(R.drawable.icon_star_gray);
                    } else if (rating == 2) {
                        imgRating1
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating2
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating3
                                .setBackgroundResource(R.drawable.icon_star_gray);
                        imgRating4
                                .setBackgroundResource(R.drawable.icon_star_gray);
                        imgRating5
                                .setBackgroundResource(R.drawable.icon_star_gray);
                    } else if (rating == 2.5) {
                        imgRating1
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating2
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating3
                                .setBackgroundResource(R.drawable.icon_star_halfcolored);
                        imgRating4
                                .setBackgroundResource(R.drawable.icon_star_gray);
                        imgRating5
                                .setBackgroundResource(R.drawable.icon_star_gray);
                    } else if (rating == 3) {
                        imgRating1
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating2
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating3
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating4
                                .setBackgroundResource(R.drawable.icon_star_gray);
                        imgRating5
                                .setBackgroundResource(R.drawable.icon_star_gray);
                    } else if (rating == 3.5) {
                        imgRating1
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating2
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating3
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating4
                                .setBackgroundResource(R.drawable.icon_star_halfcolored);
                        imgRating5
                                .setBackgroundResource(R.drawable.icon_star_gray);
                    } else if (rating == 4) {
                        imgRating1
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating2
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating3
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating4
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating5
                                .setBackgroundResource(R.drawable.icon_star_gray);
                    } else if (rating == 4.5) {
                        imgRating1
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating2
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating3
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating4
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating5
                                .setBackgroundResource(R.drawable.icon_star_halfcolored);
                    } else if (rating == 5) {
                        imgRating1
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating2
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating3
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating4
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                        imgRating5
                                .setBackgroundResource(R.drawable.icon_star_coloured);
                    }
                }
            }
            offersImageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    finalPromotionalOfferName.setVisibility(View.VISIBLE);
                }
            });

            return view;
        }

        @Override
        public int getCount() {
            return listWithAllProducts.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

    /**
     * The Class CustomArrayAdapter.
     *
     * @param <T>
     *            the generic type
     */
    public class CustomArrayAdapter<T> extends ArrayAdapter<T> {

        /**
         * Instantiates a new custom array adapter.
         *
         * @param ctx
         *            the ctx
         * @param objects
         *            the objects
         */
        public CustomArrayAdapter(Context ctx, T[] objects) {
            super(ctx, android.R.layout.simple_spinner_item, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView text = (TextView) view.findViewById(android.R.id.text1);
            text.setPadding(4, 8, 4, 8);
            text.setTextSize(16);
            return view;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REFINE_PRODUCTS_REQ_CODE == requestCode) {
        }
    }

    /**
     * Method to get the bundle data.
     */
    private void fnGetBundleData() {
        Bundle bundleFromSearch = getIntent().getExtras();
        if (getIntent().getAction() != null
                && getIntent().getAction().equalsIgnoreCase("fromHomeByBrand")) {
            isSearch = true;
            if (null != bundleFromSearch) {
                if (null != bundleFromSearch.getString("altText")) {
                    setTitle(bundleFromSearch.getString("altText"));
                    altText = bundleFromSearch.getString("altText");
                } else {
                    setTitle(" ");
                }

                productSearched = bundleFromSearch.getString("search");


                if (null != bundleFromSearch.getString("brandId")) {
                    selectedBrandId = bundleFromSearch.getString("brandId");
                }
            }
            sortSearch = "price";
            listWithAllProducts.clear();
            sortSearch = "bestSellers";
            tvSortFilterStatus.setText("-sorted by Best Sellers");
            tvSortFilterStatus.setVisibility(View.GONE);
            if (null != brandId) {
                invokeFetchProductsForSelectedBrand();
            } else {
                fnInvokeSearch();
            }

        } else if (getIntent().getAction() != null
                && getIntent().getAction()
                .equalsIgnoreCase("fromHomeBySplCode")) {
            isSearch = true;
            if (null != bundleFromSearch) {
                if (null != bundleFromSearch.getString("altText")) {
                    setTitle(bundleFromSearch.getString("altText"));
                    altText = bundleFromSearch.getString("altText");
                } else {
                    setTitle(" ");
                }

                productSearched = bundleFromSearch.getString("search");
            }
            sortSearch = "price";
            listWithAllProducts.clear();
            sortSearch = "bestSellers";
            tvSortFilterStatus.setText("-sorted by Best Sellers");
            tvSortFilterStatus.setVisibility(View.GONE);
            fnInvokeSearch();
        }
        // For new arrivals
        else if (getIntent().getAction() != null
                && getIntent().getAction().equalsIgnoreCase(
                "fromHomeByNewArrivals")) {
            pageNum = 1;
            sortSearch = "isNew";
            tvSortFilterStatus.setText("- Sorted by New Arrivals");
            tvSortFilterStatus.setVisibility(View.GONE);
            fromNewArrival = true;
            setTitle("New Arrivals");
            invokeNewArrivalDetails();

        } else if (getIntent().getAction() != null
                && getIntent().getAction().equalsIgnoreCase("fromPromotion")) {
            if (null != bundleFromSearch.getString("page")
                    && bundleFromSearch.getString("page").equalsIgnoreCase(
                    "gwp")) {
                filterTV.setVisibility(View.INVISIBLE);
            }
            if (null != bundleFromSearch) {
                if (null != bundleFromSearch.getString("altText")) {
                    setTitle(bundleFromSearch.getString("altText"));
                    altText = bundleFromSearch.getString("altText");
                } else {
                    setTitle(" ");
                }
                productSearched = bundleFromSearch.getString("promotionId");
            }
            promotion = true;
            sortSearch = "isNew";
            listWithAllProducts.clear();
            productSearched = bundleFromSearch.getString("promotionId");
            sortSearch = "bestSellers";
            tvSortFilterStatus.setText("-sorted by Best Sellers");
            tvSortFilterStatus.setVisibility(View.GONE);
            fnInvokeRetrieveProductListByPromoId(productSearched, pageNum,
                    sortSearch);
        } else if (bundleFromSearch != null
                && bundleFromSearch.get("id") != null
                && bundleFromSearch.get("id").equals("onSaleFormHomePage")) {
            if (null != bundleFromSearch.getString("altText")) {
                setTitle(bundleFromSearch.getString("altText"));
                altText = bundleFromSearch.getString("altText");
            } else {
                setTitle(" ");
            }
            isOnSale = true;
            listWithAllProducts.clear();
            sortSearch = "bestSellers";
            tvSortFilterStatus.setVisibility(View.GONE);
            tvSortFilterStatus.setText("-sorted by Best Sellers");
            invokeOnSaleRootCategoryDetails();
        } else if (bundleFromSearch != null
                && bundleFromSearch.get("From") != null
                && bundleFromSearch.get("From").equals("ExtendedListActivity")) {
            if (bundleFromSearch.get("catagoryIdFromRoot") != null) {
                String IdRoot = bundleFromSearch.get("catagoryIdFromRoot")
                        .toString();
                categoryId = IdRoot;
                isrootCategory = true;
                categoryName = bundleFromSearch.get("catNam").toString();
                setTitle(categoryName);
                altText = categoryName;
                listWithAllProducts.clear();
                sortSearch = "bestSellers";
                tvSortFilterStatus.setText("-sorted by Best Sellers");
                tvSortFilterStatus.setVisibility(View.GONE);
                invokeFetchProductsForRootCategory();
            }
        }
        // 3.3 release shop by brands
        else if (bundleFromSearch != null
                && bundleFromSearch.getString("fromShopByBrandsPage") != null
                && bundleFromSearch.getString("fromShopByBrandsPage")
                .equalsIgnoreCase("fromShopByBrandsPage")) {
            selectedBrandId = bundleFromSearch.get("selectedBrandId")
                    .toString();
            isShopByBrand = true;
            listWithAllProducts.clear();
            if (null != bundleFromSearch.getString("altText")) {
                setTitle(bundleFromSearch.getString("altText"));
                altText = bundleFromSearch.getString("altText");
            } else {
                setTitle(" ");
            }
            sortSearch = "bestSellers";
            tvSortFilterStatus.setText("-sorted by Best Sellers");
            tvSortFilterStatus.setVisibility(View.GONE);
            invokeFetchProductsForSelectedBrand();
        } else if (bundleFromSearch != null
                && bundleFromSearch.getString("search") != null) {
            isSearch = true;
            listWithAllProducts.clear();
            productSearched = bundleFromSearch.getString("search");
            setTitle(productSearched);
            if (null != bundleFromSearch.getString("scan")) {
                fromScan = bundleFromSearch.getString("scan").equals("scan");
            }
            if (null != bundleFromSearch.getString("categoryId")) {
                categoryId = bundleFromSearch.getString("categoryId");
            }
            sortSearch = "bestSellers";
            tvSortFilterStatus.setText("-sorted by Best Sellers");
            tvSortFilterStatus.setVisibility(View.GONE);
            fnInvokeSearch();
        } else if (null != bundleFromSearch && null != bundleFromSearch.getString("Nstate")) {
            isSearch = true;
            listWithAllProducts.clear();
            Nstate = bundleFromSearch.getString("Nstate");
            setTitle("ULTA");
            sortSearch = "bestSellers";
            tvSortFilterStatus.setText("-sorted by Best Sellers");
            tvSortFilterStatus.setVisibility(View.GONE);
            fnInvokeSearch();

        } else if (bundleFromSearch != null
                && bundleFromSearch.getString("from") != null
                && bundleFromSearch.get("from").equals("SaleListGWP")) {


        }
    }



    /**
     * Method to populate invoker params and fire the web service.
     *
     * @param id
     *            the id
     */
    private void fnInvokeSearch() {
        InvokerParams<SearchBean> invokerParams = new InvokerParams<SearchBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.KEYWORD_SEARCH_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(HttpProtocol.http);

        invokerParams.setUrlParameters(fnPopulateSearchParameters());
        invokerParams.setUltaBeanClazz(SearchBean.class);
        SearchHandler searchHandler = new SearchHandler();
        invokerParams.setUltaHandler(searchHandler);
        try {
            Logger.Log("<UltaProductListActivity><fnInvokeSearch()><Going to Execute the Delegator>>");
            new ExecutionDelegator(invokerParams);
            Logger.Log("<UltaProductListActivity><fnInvokeSearch()><Executed the Delegator>>");
        } catch (UltaException exception) {
            Logger.Log("<UltaProductListActivity><fnInvokeSearch()><UltaException>>"
                    + exception);
        }
    }

    private Map<String, String> fnPopulateSearchParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-depth", "2");
        urlParams.put("atg-rest-output", "json");
        urlParams.put("searchTerm", productSearched);
        urlParams.put("Nstate", Nstate);
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("pageNumber", pageNum + "");
        urlParams.put("howMany", "12");
        urlParams.put("sortBy", sortSearch);
        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("categoryDimId", categoryId);
        urlParams.put("brandDimIds", brandId);
        urlParams.put("colorDimId", colorId);
        urlParams.put("promotionDimIds", promotionid);
        urlParams.put("minPrice", minPrice);
        urlParams.put("maxPrice", maxPrice);
        return urlParams;
    }

    /**
     * Function to populate the parameters for new arrival web service
     *
     * @return urlParams
     */
    private Map<String, String> fnPopulateNewArrivalParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-depth", "2");
        urlParams.put("atg-rest-output", "json");
        urlParams.put("pageNumber", pageNum + "");
        urlParams.put("howMany", "12");
        urlParams.put("sortBy", sortSearch);
        urlParams.put("categoryDimId", categoryId);
        urlParams.put("brandDimIds", brandId);
        urlParams.put("promotionDimIds", promotionid);
        urlParams.put("minPrice", minPrice);
        urlParams.put("maxPrice", maxPrice);
        return urlParams;
    }

    private SearchBean productsSearched;

    public class SearchHandler extends UltaHandler {
        /**
         * on Handle message.
         *
         * @param msg
         *            the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<SearchHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            loadingDialog.setVisibility(View.GONE);
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (null != getErrorMessage()) {
                try {
                    notifyUser(Utility.formatDisplayError(getErrorMessage()),
                            UltaProductListActivity.this);
                    setError(UltaProductListActivity.this, getErrorMessage());
                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
            } else {
                Logger.Log("<SearchHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                productsSearched = (SearchBean) getResponseBean();

                if (null != productsSearched) {
                    filterTV.setEnabled(true);
                    linearLayout3.setVisibility(View.VISIBLE);
                    sortFilterStatusLayout.setVisibility(View.VISIBLE);
                    count = productsSearched.getTotalNoOfProducts();

                    if (count == 0) {


                        String message = "";

                        if (fromScan) {
                            message = "Unable to locate product online. Sold in store only.";
                        } else {
                            message = "Search was unable to find any results.";
                        }

                        final Dialog alert = showAlertDialog(
                                UltaProductListActivity.this, "Sorry", message,
                                "OK", "");
                        alert.show();
                        alert.setCancelable(false);

                        mDisagreeButton.setVisibility(View.GONE);
                        mAgreeButton.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                alert.dismiss();
                                finish();
                            }
                        });

                    } else {
                        if (!isSearchFilter) {
                            if (null != altText) {
                                tvItemNumber.setText("" + count + " Products");
                            } else {
                                tvItemNumber.setText("" + count + " Products");
                            }
                        }
                        listOfProductsInListBean = new ArrayList<ProductsInListBean>();
                        List<SearchResultsBean> products = new ArrayList<SearchResultsBean>();
                        products = productsSearched.getSearchResults();
                        for (int loop = 0; loop < products.size(); loop++) {
                            ProductsInListBean productinlistbean = new ProductsInListBean();
                            productinlistbean.setBrandName(products.get(loop)
                                    .getBrandName());
                            productinlistbean.setDisplayName(products.get(loop)
                                    .getDisplayName());
                            productinlistbean.setId(products.get(loop).getId());
                            productinlistbean.setIsGWP(products.get(loop)
                                    .getIsGWP());
                            productinlistbean.setListPriceFrom(products.get(
                                    loop).getListPriceFrom());
                            productinlistbean.setListPriceTo(products.get(loop)
                                    .getListPriceTo());
                            productinlistbean.setSalePriceFrom(products.get(
                                    loop).getSalePriceFrom());
                            productinlistbean.setSalePriceTo(products.get(loop)
                                    .getSalePriceTo());
                            productinlistbean.setSmallImageUrl(products.get(
                                    loop).getSmallImageUrl());
                            productinlistbean.setRating(products.get(loop)
                                    .getRating());
                            productinlistbean.setReviews(products.get(loop)
                                    .getReviews());
                            if (products.get(loop).getBadgeName() != null) {

                                productinlistbean.setBadgeName(products.get(
                                        loop).getBadgeName());
                            }
                            if (products.get(loop).getGiftUrl() != null) {
                                productinlistbean.setGiftUrl(products.get(loop)
                                        .getGiftUrl());
                            }
                            if (products.get(loop).getOfferType() != null) {
                                productinlistbean.setOfferType(products.get(
                                        loop).getOfferType());
                            }
                            if (products.get(loop).getOfferDesc() != null) {
                                productinlistbean.setOfferDesc(products.get(
                                        loop).getOfferDesc());
                            }
                            if (products.get(loop).getHasSkusOnSale() != null
                                    && products.get(loop).getHasSkusOnSale()
                                    .equals("true")) {
                                productinlistbean.setHasSkusOnSale(true);
                            } else {
                                productinlistbean.setHasSkusOnSale(false);
                            }
                            listOfProductsInListBean.add(productinlistbean);
                        }
                        if (listOfProductsInListBean != null) {
                            listWithAllProducts
                                    .addAll(listOfProductsInListBean);
                        }
                        if (null == productListAdapter) {
                            fnSetAdapters();
                        } else {
                            productListAdapter.notifyDataSetChanged();
                        }
                        FacetListingBean facetGroupList = productsSearched
                                .getFacetListing();
                        FacetsBean fbean = new FacetsBean();
                        fbean.setBrandFacets(facetGroupList.getBrandFacets());
                        fbean.setCategoryFacets(facetGroupList
                                .getCategoryFacets());
                        fbean.setPriceFacets(facetGroupList.getPriceFacets());
                        promotionFacets = facetGroupList.getPromotionFacets();
                        facetsBean = fbean;
                        tvSortFilterStatus.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(UltaProductListActivity.this,
                            "search bean null", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    protected void fnInvokeRetrieveProductListByPromoId(String promotionId,
                                                        int pageNum, String sortBy) {
        InvokerParams<PromotionListBean> invokerParams = new InvokerParams<PromotionListBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.FETCH_PRODUCTS_FOR_PROMOTIONS);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams.setUrlParameters(populateShippmentParameters(promotionId,
                pageNum, sortBy));
        invokerParams.setUltaBeanClazz(PromotionListBean.class);
        PurchaseHandler userCreationHandler = new PurchaseHandler();
        invokerParams.setUltaHandler(userCreationHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<AddNewShippingAddressActivity><invokeForgotPassword><UltaException>>"
                    + ultaException);
        }
    }

    private Map<String, String> populateShippmentParameters(String promotionId,
                                                            int pageNum, String sortBy) {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("promotionId", promotionId);
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-depth", "2");
        urlParams.put("pageNumber", String.valueOf(pageNum));
        urlParams.put("howMany", HOW_MANY);
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("sortBy", sortBy);
        urlParams.put("brandDimIds", brandId);
        urlParams.put("promotionDimIds", promotionid);
        urlParams.put("minPrice", minPrice);
        urlParams.put("maxPrice", maxPrice);
        urlParams.put("pageNumber", pageNum + "");
        return urlParams;
    }

    public class PurchaseHandler extends UltaHandler {
        public void handleMessage(Message msg) {
            Logger.Log("<PurchaseHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            loadingDialog.setVisibility(View.GONE);
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (null != getErrorMessage()) {
                try {
                    notifyUser(getErrorMessage(), UltaProductListActivity.this);
                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
            } else {
                Logger.Log("<GiftOptionHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                UltaBean ultaBean = (UltaBean) getResponseBean();
                List<String> errors = ultaBean.getErrorInfos();
                if (null != errors && !(errors.isEmpty()))
                    try {
                        notifyUser(errors.get(0), UltaProductListActivity.this);
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                else {
                    Logger.Log("<GiftOptionHandler><handleMessage><getResponseBean>>"
                            + (getResponseBean()));
                    if (null != ultaBean) {
                        filterTV.setEnabled(true);
                        linearLayout3.setVisibility(View.VISIBLE);
                        sortFilterStatusLayout.setVisibility(View.VISIBLE);
                        Logger.Log("<HomeActivity>" + "BeanPopulated");
                        promoBean = (PromotionListBean) getResponseBean();

                        if (null != promoBean) {
                            linearLayout3.setVisibility(View.VISIBLE);
                            sortFilterStatusLayout.setVisibility(View.VISIBLE);
                            count = promoBean.getTotalNoOfProducts();

                            if (count == 0) {

                                String message = "";

                                if (fromScan) {
                                    message = "Unable to locate product online. Sold in store only.";
                                } else {
                                    message = "Search was unable to find any results.";
                                }

                                final Dialog alert = showAlertDialog(
                                        UltaProductListActivity.this, "Sorry",
                                        message, "OK", "");
                                alert.show();
                                alert.setCancelable(false);

                                mDisagreeButton.setVisibility(View.GONE);
                                mAgreeButton
                                        .setOnClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {

                                                alert.dismiss();
                                                finish();
                                            }
                                        });
                            } else {
                                if (!isSearchFilter) {
                                    if (null != altText) {
                                        tvItemNumber.setText("" + count
                                                + " Products");
                                    } else {
                                        tvItemNumber.setText("" + count
                                                + " Products");
                                    }
                                }
                                listOfProductsInListBean = new ArrayList<ProductsInListBean>();
                                List<SearchResultsBean> products = new ArrayList<SearchResultsBean>();
                                products = promoBean.getSearchResults();
                                for (int loop = 0; loop < products.size(); loop++) {
                                    ProductsInListBean productinlistbean = new ProductsInListBean();
                                    productinlistbean.setBrandName(products
                                            .get(loop).getBrandName());
                                    productinlistbean.setDisplayName(products
                                            .get(loop).getDisplayName());
                                    productinlistbean.setId(products.get(loop)
                                            .getId());
                                    productinlistbean.setIsGWP(products.get(
                                            loop).getIsGWP());
                                    productinlistbean.setListPriceFrom(products
                                            .get(loop).getListPriceFrom());
                                    productinlistbean.setListPriceTo(products
                                            .get(loop).getListPriceTo());
                                    productinlistbean.setSalePriceFrom(products
                                            .get(loop).getSalePriceFrom());
                                    productinlistbean.setSalePriceTo(products
                                            .get(loop).getSalePriceTo());
                                    productinlistbean.setSmallImageUrl(products
                                            .get(loop).getSmallImageUrl());
                                    productinlistbean.setRating(products.get(
                                            loop).getRating());
                                    productinlistbean.setReviews(products.get(
                                            loop).getReviews());
                                    if (products.get(loop).getBadgeName() != null) {

                                        productinlistbean.setBadgeName(products
                                                .get(loop).getBadgeName());
                                    }
                                    if (products.get(loop).getGiftUrl() != null) {
                                        productinlistbean.setGiftUrl(products
                                                .get(loop).getGiftUrl());
                                    }
                                    if (products.get(loop).getOfferType() != null) {
                                        productinlistbean.setOfferType(products
                                                .get(loop).getOfferType());
                                    }
                                    if (products.get(loop).getOfferDesc() != null) {
                                        productinlistbean.setOfferDesc(products
                                                .get(loop).getOfferDesc());
                                    }
                                    if (products.get(loop).getHasSkusOnSale() != null
                                            && products.get(loop)
                                            .getHasSkusOnSale()
                                            .equals("true")) {
                                        productinlistbean
                                                .setHasSkusOnSale(true);
                                    } else {
                                        productinlistbean
                                                .setHasSkusOnSale(false);
                                    }
                                    listOfProductsInListBean
                                            .add(productinlistbean);
                                }
                                if (listOfProductsInListBean != null) {
                                    listWithAllProducts
                                            .addAll(listOfProductsInListBean);
                                }
                                if (null == productListAdapter) {
                                    fnSetAdapters();
                                } else {
                                    productListAdapter.notifyDataSetChanged();
                                }
                                FacetListingBean facetGroupList = promoBean
                                        .getFacetListingForLeaf();
                                FacetsBean fbean = new FacetsBean();
                                fbean.setBrandFacets(facetGroupList
                                        .getBrandFacets());
                                fbean.setCategoryFacets(facetGroupList
                                        .getCategoryFacets());
                                fbean.setPriceFacets(facetGroupList
                                        .getPriceFacets());
                                promotionFacets = facetGroupList
                                        .getPromotionFacets();
                                facetsBean = fbean;
                                tvSortFilterStatus.setVisibility(View.VISIBLE);
                            }
                        }


                    }
                }
            }
        }
    }

    /**
     * Method to show sort options.
     */
    public void showSortOptionsDialog() {
        final List<String> listToSort = new ArrayList<String>();
        listToSort.add("Best Seller");
        listToSort.add("Price:Low To High");
        listToSort.add("Price:High To Low");
        listToSort.add("New Arrivals");
        listToSort.add("Top Rated");
        final ArrayAdapter<String> adapter = new SpinnerAddapter(this,
                android.R.layout.simple_list_item_1, listToSort);
        final AlertDialog.Builder b = new AlertDialog.Builder(this);
        // b.setTitle("Sort By");
        b.setTitle(Html.fromHtml("<font color='"
                + getResources().getColor(R.color.dialog_title) + "'>"
                + getResources().getString(R.string.plp_sort_by_title)
                + "</font>"));
        b.setSingleChoiceItems(adapter, previousSortSelection,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        previousSortSelection = which;
                        if (listToSort.get(which).equals("Best Seller")) {
                            sortSearch = "bestSellers";
                            tvItemNumber.setText("" + count + " Products ");
                            tvSortFilterStatus.setVisibility(View.GONE);
                            tvSortFilterStatus
                                    .setText("- Sorted by Best Sellers");
                        } else if (listToSort.get(which).equals("New Arrivals")) {
                            sortSearch = "isNew";
                            tvItemNumber.setText("" + count + " Products ");
                            tvSortFilterStatus
                                    .setText("- Sorted by New Arrivals");
                            tvSortFilterStatus.setVisibility(View.GONE);
                        } else if (listToSort.get(which).equals("Top Rated")) {
                            sortSearch = "topRated";
                            tvItemNumber.setText("" + count + " Products ");
                            tvSortFilterStatus.setVisibility(View.GONE);
                            tvSortFilterStatus.setText("- Sorted by Top Rated");
                        } else if (listToSort.get(which).equals(
                                "Price:Low To High")) {
                            sortSearch = "minSkuUltaPrice";
                            tvSortFilterStatus.setVisibility(View.GONE);
                            tvItemNumber.setText("" + count + " Products ");
                            tvSortFilterStatus
                                    .setText("- Sorted by Price:Low to High");
                        } else if (listToSort.get(which).equals(
                                "Price:High To Low")) {
                            sortSearch = "maxSkuUltaPrice";
                            tvItemNumber.setText("" + count + " Products ");
                            tvSortFilterStatus.setVisibility(View.GONE);
                            tvSortFilterStatus
                                    .setText("- Sorted by Price:High to Low");
                        }
                        listWithAllProducts.clear();
                        pageNum = 1;
                        pd.show();
                        if (promotion) {
                            fnInvokeRetrieveProductListByPromoId(
                                    productSearched, pageNum, sortSearch);
                        } else {
                            if (isSearch) {
                                fnInvokeSearch();
                            } else if (isrootCategory) {
                                invokeFetchProductsForRootCategory();
                            } else if (isOnSale) {
                                invokeOnSaleRootCategoryDetails();
                            } else if (isShopByBrand) {
                                invokeFetchProductsForSelectedBrand();
                            } else if (fromNewArrival) {
                                invokeNewArrivalDetails();
                            }
                        }
                    }
                });

        try {
            sortDialog = b.create();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sortDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                int titleDividerId = sortDialog.getContext().getResources()
                        .getIdentifier("titleDivider", "id", "android");

                View titleDivider = sortDialog.findViewById(titleDividerId);
                if (titleDivider != null) {
                    titleDivider.setBackgroundColor(sortDialog.getContext()
                            .getResources().getColor(R.color.primaryColor));
                }
            }
        });
        try {
            sortDialog.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    class SpinnerAddapter extends ArrayAdapter<String> {
        int textViewResourceId;
        Context context;
        List<String> list;

        public SpinnerAddapter(Context context, int textViewResourceId,
                               List<String> objects) {
            super(context, textViewResourceId, objects);
            this.textViewResourceId = textViewResourceId;
            this.context = context;
            this.list = objects;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View getView(int position, final View convertView,
                            ViewGroup parent) {
            int pos = 5;
            if (position == pos) {
                LinearLayout lytButton = new LinearLayout(context);
                LayoutParams buttons = new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lytButton.setPadding(0, 0, 0, 30);
                lytButton.setGravity(Gravity.CENTER_HORIZONTAL);
                Button cancel = new Button(context);
                cancel.setText("Cancel");
                // cancel.setWidth(400);
                cancel.setTextColor(getResources().getColor(R.color.melon));
                if (Build.VERSION.SDK_INT >= 21) {
                    cancel.setBackground(getResources().getDrawable(
                            R.drawable.primary_round));
                } else {
                    cancel.setBackgroundResource(R.drawable.button_rectangular_border);
                }

                cancel.setLayoutParams(buttons);

                lytButton.addView(cancel);
                cancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sortDialog.dismiss();
                    }
                });
                return lytButton;
            } else {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                TextView view = (TextView) inflater.inflate(textViewResourceId,
                        null);
                view.setText(list.get(position));
                return view;
            }
        }

        @Override
        public int getCount() {
            return list.size() + 1;
        }
    }

    class SpinnerAddapterFilter extends ArrayAdapter<String> {
        int textViewResourceId;
        Context context;
        List<String> list;

        public SpinnerAddapterFilter(Context context, int textViewResourceId,
                                     List<String> listToFilterByBrand) {
            super(context, textViewResourceId, listToFilterByBrand);
            this.textViewResourceId = textViewResourceId;
            this.context = context;
            this.list = listToFilterByBrand;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                TextView view = (TextView) inflater.inflate(textViewResourceId,
                        null);
                view.setText("Select");
                view.setVisibility(View.GONE);
                view.setHeight(0);
                return view;
            }
            if (position == (getCount() - 1)) {
                LinearLayout lytButton = new LinearLayout(context);
                LayoutParams buttons = new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lytButton.setGravity(Gravity.CENTER_HORIZONTAL);
                Button cancel = new Button(context);
                cancel.setTextColor(getResources().getColor(R.color.melon));
                if (Build.VERSION.SDK_INT > 16) {
                    cancel.setBackground(null);
                } else {
                    cancel.setBackgroundDrawable(null);
                }
                cancel.setText("Cancel");
                // cancel.setWidth(200);
                cancel.setLayoutParams(buttons);

                lytButton.addView(cancel);
                cancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filterDialog.dismiss();
                    }
                });
                return lytButton;
            } else {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                TextView view = (TextView) inflater.inflate(textViewResourceId,
                        null);
                view.setText(list.get(position));
                return view;
            }
        }

        @Override
        public int getCount() {
            return list.size() + 1;
        }
    }

    String filterName;

    public void showFilterOptionsDialog(String title) {
        spinnerType = title;
        final List<String> listOfBrands = new ArrayList<String>();
        final List<FacetDetailBean> list1FacetDetailBean;
        if (facetsBean != null && facetsBean.getBrandFacets() != null) {
            list1FacetDetailBean = facetsBean.getBrandFacets();
        } else {
            list1FacetDetailBean = brandFacets;
        }
        Collections.sort(list1FacetDetailBean,
                new Comparator<FacetDetailBean>() {
                    public int compare(FacetDetailBean s1, FacetDetailBean s2) {
                        return s1.getName().compareToIgnoreCase(s2.getName());
                    }
                });
        listOfBrands.add("Select");
        for (int loop = 0; loop < list1FacetDetailBean.size(); loop++) {
            FacetDetailBean facetDetailBean = list1FacetDetailBean.get(loop);
            listOfBrands.add(facetDetailBean.getName());
        }

        List<String> listOfPrice = new ArrayList<String>();
        List<FacetDetailBean> list2FacetDetailBean;
        if (facetsBean != null && facetsBean.getPriceFacets() != null) {
            list2FacetDetailBean = facetsBean.getPriceFacets();
        } else {
            list2FacetDetailBean = priceFacets;
        }
        listOfPrice.add("Select");
        for (int loop = 0; loop < list2FacetDetailBean.size(); loop++) {
            FacetDetailBean facetDetailBean = list2FacetDetailBean.get(loop);
            listOfPrice.add(facetDetailBean.getName());
        }
        List<String> listOfCategories = new ArrayList<String>();
        List<FacetDetailBean> list3FacetDetailBean;
        if (facetsBean != null && facetsBean.getCategoryFacets() != null) {
            list3FacetDetailBean = facetsBean.getCategoryFacets();

            Collections.sort(list3FacetDetailBean,
                    new Comparator<FacetDetailBean>() {
                        public int compare(FacetDetailBean s1,
                                           FacetDetailBean s2) {
                            return s1.getName().compareToIgnoreCase(
                                    s2.getName());
                        }
                    });
            listOfCategories.add("Select");
            for (int loop = 0; loop < list3FacetDetailBean.size(); loop++) {
                FacetDetailBean facetDetailBean = list3FacetDetailBean
                        .get(loop);
                listOfCategories.add(facetDetailBean.getName());
            }
        }

        List<String> listOfPromotions = new ArrayList<String>();
        List<FacetDetailBean> list4FacetDetailBean = promotionFacets;
        listOfPromotions.add("Select");
        for (int loop = 0; loop < list4FacetDetailBean.size(); loop++) {
            FacetDetailBean facetDetailBean = list4FacetDetailBean.get(loop);
            listOfPromotions.add(facetDetailBean.getName());
        }
        List<String> listOfBenefits = new ArrayList<String>();
        if (isrootCategory) {
            List<FacetDetailBean> list5FacetDetailBean = benefitFacets;
            listOfBenefits.add("Select");
            for (int loop = 0; loop < list5FacetDetailBean.size(); loop++) {
                FacetDetailBean facetDetailBean = list5FacetDetailBean
                        .get(loop);
                listOfBenefits.add(facetDetailBean.getName());
            }
        }
        List<String> listOfcolorFacets = new ArrayList<String>();
        if (isrootCategory) {
            List<FacetDetailBean> list6FacetDetailBean = colorFacets;
            listOfcolorFacets.add("Select");
            for (int loop = 0; loop < list6FacetDetailBean.size(); loop++) {
                FacetDetailBean facetDetailBean = list6FacetDetailBean
                        .get(loop);
                listOfcolorFacets.add(facetDetailBean.getName());
            }
        }
        List<String> listOfconcernsFacets = new ArrayList<String>();
        if (isrootCategory) {
            List<FacetDetailBean> list7FacetDetailBean = concernsFacets;
            listOfconcernsFacets.add("Select");
            for (int loop = 0; loop < list7FacetDetailBean.size(); loop++) {
                FacetDetailBean facetDetailBean = list7FacetDetailBean
                        .get(loop);
                listOfconcernsFacets.add(facetDetailBean.getName());
            }
        }
        List<String> listOfcoverageFacets = new ArrayList<String>();
        if (isrootCategory) {
            List<FacetDetailBean> list8FacetDetailBean = coverageFacets;
            listOfcoverageFacets.add("Select");
            for (int loop = 0; loop < list8FacetDetailBean.size(); loop++) {
                FacetDetailBean facetDetailBean = list8FacetDetailBean
                        .get(loop);
                listOfcoverageFacets.add(facetDetailBean.getName());
            }
        }
        List<String> listOffinishFacets = new ArrayList<String>();
        if (isrootCategory) {
            List<FacetDetailBean> list9FacetDetailBean = finishFacets;
            listOffinishFacets.add("Select");
            for (int loop = 0; loop < list9FacetDetailBean.size(); loop++) {
                FacetDetailBean facetDetailBean = list9FacetDetailBean
                        .get(loop);
                listOffinishFacets.add(facetDetailBean.getName());
            }
        }
        List<String> listOfformFacets = new ArrayList<String>();
        if (isrootCategory) {
            List<FacetDetailBean> list10FacetDetailBean = formFacets;
            listOfformFacets.add("Select");
            for (int loop = 0; loop < list10FacetDetailBean.size(); loop++) {
                FacetDetailBean facetDetailBean = list10FacetDetailBean
                        .get(loop);
                listOfformFacets.add(facetDetailBean.getName());
            }
        }
        List<String> listOfingredientsFacets = new ArrayList<String>();
        if (isrootCategory) {
            List<FacetDetailBean> list11FacetDetailBean = ingredientsFacets;
            listOfingredientsFacets.add("Select");
            for (int loop = 0; loop < list11FacetDetailBean.size(); loop++) {
                FacetDetailBean facetDetailBean = list11FacetDetailBean
                        .get(loop);
                listOfingredientsFacets.add(facetDetailBean.getName());
            }
        }
        List<String> listOfpurposeFacets = new ArrayList<String>();
        if (isrootCategory) {
            List<FacetDetailBean> list12FacetDetailBean = purposeFacets;
            listOfpurposeFacets.add("Select");
            for (int loop = 0; loop < list12FacetDetailBean.size(); loop++) {
                FacetDetailBean facetDetailBean = list12FacetDetailBean
                        .get(loop);
                listOfpurposeFacets.add(facetDetailBean.getName());
            }
        }
        List<String> listOfscentFacets = new ArrayList<String>();
        if (isrootCategory) {
            List<FacetDetailBean> list13FacetDetailBean = scentFacets;
            listOfscentFacets.add("Select");
            for (int loop = 0; loop < list13FacetDetailBean.size(); loop++) {
                FacetDetailBean facetDetailBean = list13FacetDetailBean
                        .get(loop);
                listOfscentFacets.add(facetDetailBean.getName());
            }
        }
        List<String> listOfskinTypeFacets = new ArrayList<String>();
        if (isrootCategory) {
            List<FacetDetailBean> list14FacetDetailBean = skinTypeFacets;
            listOfskinTypeFacets.add("Select");
            for (int loop = 0; loop < list14FacetDetailBean.size(); loop++) {
                FacetDetailBean facetDetailBean = list14FacetDetailBean
                        .get(loop);
                listOfskinTypeFacets.add(facetDetailBean.getName());
            }
        }
        List<String> listOfspfFacets = new ArrayList<String>();
        if (isrootCategory) {
            List<FacetDetailBean> list15FacetDetailBean = spfFacets;
            listOfspfFacets.add("Select");
            for (int loop = 0; loop < list15FacetDetailBean.size(); loop++) {
                FacetDetailBean facetDetailBean = list15FacetDetailBean
                        .get(loop);
                listOfspfFacets.add(facetDetailBean.getName());
            }
        }
        List<String> listOftreatmentFacets = new ArrayList<String>();
        if (isrootCategory) {
            List<FacetDetailBean> list16FacetDetailBean = treatmentFacets;
            listOftreatmentFacets.add("Select");
            for (int loop = 0; loop < list16FacetDetailBean.size(); loop++) {
                FacetDetailBean facetDetailBean = list16FacetDetailBean
                        .get(loop);
                listOftreatmentFacets.add(facetDetailBean.getName());
            }
        }
        List<String> listOftypeFacets = new ArrayList<String>();
        if (isrootCategory) {
            List<FacetDetailBean> list17FacetDetailBean = typeFacets;
            listOftypeFacets.add("Select");
            for (int loop = 0; loop < list17FacetDetailBean.size(); loop++) {
                FacetDetailBean facetDetailBean = list17FacetDetailBean
                        .get(loop);
                listOftypeFacets.add(facetDetailBean.getName());
            }
        }
        final AlertDialog.Builder f = new AlertDialog.Builder(this);
        if (spinnerType.equals("Filter by Brands")) {
            // f.setTitle("Filter By Brands");
            f.setTitle(Html.fromHtml("<font color='"
                    + getResources().getColor(R.color.dialog_title) + "'>"
                    + getResources().getString(R.string.Filter_By_Brands)
                    + "</font>"));
            final List<FacetDetailBean> listFacetDetailBean;
            if (isrootCategory) {
                listFacetDetailBean = brandFacets;
            } else {
                listFacetDetailBean = facetsBean.getBrandFacets();
            }
            final String[] items = new String[listFacetDetailBean.size()];
            if (listFacetDetailBean != null && listFacetDetailBean.size() != 0) {
                for (int i = 0; i < listFacetDetailBean.size(); i++) {
                    items[i] = listFacetDetailBean.get(i).getName();
                }
            }
            boolean markedBooleanArray[] = null;
            if (brandId != null && !brandId.isEmpty()) {
                String marked[] = brandId.split(",");
                markedBooleanArray = new boolean[items.length];
                for (int j = 0; j < marked.length; j++) {
                    for (int i = 0; i < items.length; i++) {
                        if (marked[j]
                                .equals(listFacetDetailBean.get(i).getId())) {
                            markedBooleanArray[i] = true;
                            continue;
                        }
                    }
                }
            } else {
                markedBooleanArray = new boolean[items.length];
                for (int i = 0; i < items.length; i++) {
                    if (altText != null && altText.equals(items[i])) {
                        markedBooleanArray[i] = true;
                        brandId = listFacetDetailBean.get(i).getId();
                    } else {
                        markedBooleanArray[i] = false;
                    }
                }
            }
            f.setMultiChoiceItems(items, markedBooleanArray,
                    new OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which,
                                            boolean isChecked) {
                            pageNum = 1;
                            listWithAllProducts.clear();
                            if (isChecked) {
                                if (brandId.equals("")) {
                                    brandId = listFacetDetailBean.get(which)
                                            .getId();
                                } else {
                                    brandId += ","
                                            + listFacetDetailBean.get(which)
                                            .getId();
                                }
                            } else {
                                if (!brandId.equals("")) {
                                    String[] temp = brandId.split(",");
                                    brandId = "";
                                    for (int k = 0; k < temp.length; k++) {
                                        if (!temp[k].equals(listFacetDetailBean
                                                .get(which).getId())) {
                                            if (!brandId.isEmpty()) {
                                                brandId += "," + temp[k];
                                            } else {
                                                brandId = temp[k];
                                            }
                                        }
                                    }
                                }
                            }
                            customDialog.dismiss();
                            dialog.dismiss();
                            pd.show();
                            tvSortFilterStatus
                                    .setText("- Filtered By Selected Brand(s)");
                            if (isSearch) {
                                fnInvokeSearch();
                            } else if (isrootCategory) {
                                invokeFetchProductsForRootCategory();
                            } else if (isOnSale) {
                                invokeOnSaleRootCategoryDetails();
                            } else if (isShopByBrand) {
                                invokeFetchProductsForSelectedBrand();
                            } else if (fromNewArrival) {
                                invokeNewArrivalDetails();
                            } else if (promotion) {
                                fnInvokeRetrieveProductListByPromoId(
                                        productSearched, pageNum, sortSearch);
                            }
                        }
                    });
            f.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.dismiss();
                        }
                    });
            filterDialog = f.create();
            filterDialog
                    .setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            int titleDividerId = filterDialog
                                    .getContext()
                                    .getResources()
                                    .getIdentifier("titleDivider", "id",
                                            "android");

                            View titleDivider = filterDialog
                                    .findViewById(titleDividerId);
                            if (titleDivider != null) {
                                titleDivider.setBackgroundColor(filterDialog
                                        .getContext().getResources()
                                        .getColor(R.color.primaryColor));
                            }
                        }
                    });
            filterDialog.show();
            Button b = filterDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            setCustomButton(b);
        } else if (spinnerType.equals("Filter by Price")) {
            final ArrayAdapter<String> adapter2 = new SpinnerAddapterFilter(
                    this, android.R.layout.simple_spinner_dropdown_item,
                    listOfPrice);
            // f.setTitle("Filter By Price");
            f.setTitle(Html.fromHtml("<font color='"
                    + getResources().getColor(R.color.dialog_title) + "'>"
                    + getResources().getString(R.string.Filter_By_Price)
                    + "</font>"));

            f.setSingleChoiceItems(adapter2, previousFilterSelection,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            customDialog.dismiss();
                            previousFilterSelection = which;
                            List<FacetDetailBean> listFacetDetailBean;
                            if (isrootCategory) {
                                listFacetDetailBean = priceFacets;
                            } else {
                                listFacetDetailBean = facetsBean
                                        .getPriceFacets();
                            }
                            FacetDetailBean facetDetailBean = listFacetDetailBean
                                    .get(which - 1);
                            filterPrice = facetDetailBean.getName();
                            if (filterPrice != null) {
                                String[] price = filterPrice.split("-");
                                if (price[0] != null) {
                                    minPrice = price[0].toString();
                                }
                                if (price[1] != null) {
                                    maxPrice = price[1].toString();
                                }
                            }
                            tvItemNumber.setText("" + count + " Products ");
                            tvSortFilterStatus.setVisibility(View.GONE);
                            tvSortFilterStatus.setText("- Filtered by Price "
                                    + facetDetailBean.getName());
                            filterName = facetDetailBean.getName();
                            for (int loop = 0; loop < listWithAllProducts
                                    .size(); loop++) {
                                tempListWithAllProducts.add(listWithAllProducts
                                        .get(loop));
                            }
                            listWithAllProducts.clear();
                            pageNum = 1;
                            pd.show();
                            if (isSearch) {
                                fnInvokeSearch();
                            } else if (isOnSale) {
                                invokeOnSaleRootCategoryDetails();
                            } else if (isShopByBrand) {
                                invokeFetchProductsForSelectedBrand();
                            } else if (isrootCategory) {
                                invokeFetchProductsForRootCategory();
                            } else if (fromNewArrival) {
                                invokeNewArrivalDetails();
                            } else if (promotion) {
                                fnInvokeRetrieveProductListByPromoId(
                                        productSearched, pageNum, sortSearch);
                            }
                        }
                    });


            filterDialog = f.create();
            filterDialog
                    .setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            int titleDividerId = filterDialog
                                    .getContext()
                                    .getResources()
                                    .getIdentifier("titleDivider", "id",
                                            "android");

                            View titleDivider = filterDialog
                                    .findViewById(titleDividerId);
                            if (titleDivider != null) {
                                titleDivider.setBackgroundColor(filterDialog
                                        .getContext().getResources()
                                        .getColor(R.color.primaryColor));
                            }
                        }
                    });
            filterDialog.show();
            Button b = filterDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            setCustomButton(b);
        } else if (spinnerType.equals("Filter by Categories")) {
            final ArrayAdapter<String> adapter3 = new SpinnerAddapterFilter(
                    this, android.R.layout.simple_spinner_dropdown_item,
                    listOfCategories);
            f.setTitle(Html.fromHtml("<font color='"
                    + getResources().getColor(R.color.dialog_title) + "'>"
                    + getResources().getString(R.string.Filter_By_Categories)
                    + "</font>"));
            f.setSingleChoiceItems(adapter3, previousFilterSelection,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            customDialog.dismiss();
                            previousFilterSelection = which;
                            List<FacetDetailBean> listFacetDetailBean = facetsBean
                                    .getCategoryFacets();
                            FacetDetailBean facetDetailBean = listFacetDetailBean
                                    .get(which - 1);
                            categoryId = facetDetailBean.getId();
                            tvItemNumber.setText("" + count + " Products ");
                            tvSortFilterStatus.setVisibility(View.GONE);
                            tvSortFilterStatus.setText("- Filtered by "
                                    + facetDetailBean.getName());
                            filterName = facetDetailBean.getName();
                            for (int loop = 0; loop < listWithAllProducts
                                    .size(); loop++) {
                                tempListWithAllProducts.add(listWithAllProducts
                                        .get(loop));
                            }
                            listWithAllProducts.clear();
                            pageNum = 1;
                            pd.show();
                            if (isSearch) {
                                fnInvokeSearch();
                            } else if (isOnSale) {
                                invokeOnSaleRootCategoryDetails();
                            } else if (isShopByBrand) {
                                invokeFetchProductsForSelectedBrand();
                            } else if (fromNewArrival) {
                                invokeNewArrivalDetails();
                            } else if (promotion) {
                                fnInvokeRetrieveProductListByPromoId(
                                        productSearched, pageNum, sortSearch);
                            }
                        }
                    });
            filterDialog = f.create();
            filterDialog
                    .setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            int titleDividerId = filterDialog
                                    .getContext()
                                    .getResources()
                                    .getIdentifier("titleDivider", "id",
                                            "android");

                            View titleDivider = filterDialog
                                    .findViewById(titleDividerId);
                            if (titleDivider != null) {
                                titleDivider.setBackgroundColor(filterDialog
                                        .getContext().getResources()
                                        .getColor(R.color.primaryColor));
                            }
                        }
                    });
            filterDialog.show();
            Button b = filterDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            setCustomButton(b);
        } else if (spinnerType.equals("Filter by Promotions")) {
            f.setTitle(Html.fromHtml("<font color='"
                    + getResources().getColor(R.color.dialog_title) + "'>"
                    + getResources().getString(R.string.Filter_By_Promotions)
                    + "</font>"));
            final List<FacetDetailBean> listFacetDetailBean = promotionFacets;
            final String[] items = new String[listFacetDetailBean.size()];
            if (listFacetDetailBean != null && listFacetDetailBean.size() != 0) {
                for (int i = 0; i < listFacetDetailBean.size(); i++) {
                    items[i] = listFacetDetailBean.get(i).getName();
                }
            }
            boolean markedBooleanArray[] = null;
            if (promotionid != null && !promotionid.isEmpty()) {
                String marked[] = promotionid.split(",");
                markedBooleanArray = new boolean[items.length];
                for (int j = 0; j < marked.length; j++) {
                    for (int i = 0; i < items.length; i++) {
                        if (marked[j]
                                .equals(listFacetDetailBean.get(i).getId())) {
                            markedBooleanArray[i] = true;
                            continue;
                        }
                    }
                }
            }

            f.setMultiChoiceItems(items, markedBooleanArray,
                    new OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which,
                                            boolean isChecked) {
                            customDialog.dismiss();
                            pageNum = 1;
                            if (isChecked) {
                                if (promotionid.equals("")) {
                                    promotionid = listFacetDetailBean
                                            .get(which).getId();
                                } else {
                                    promotionid += ","
                                            + listFacetDetailBean.get(which)
                                            .getId();
                                }
                            } else {
                                if (!promotionid.equals("")) {
                                    String[] temp = promotionid.split(",");
                                    promotionid = "";
                                    for (int k = 0; k < temp.length; k++) {
                                        if (!temp[k].equals(listFacetDetailBean
                                                .get(which).getId())) {
                                            if (!promotionid.isEmpty()) {
                                                promotionid += "," + temp[k];
                                            } else {
                                                promotionid = temp[k];
                                            }
                                        }
                                    }
                                }
                            }
                            dialog.dismiss();
                            pd.show();
                            listWithAllProducts.clear();
                            tvSortFilterStatus
                                    .setText("- Filtered By Selected Promotions");
                            if (isSearch) {
                                fnInvokeSearch();
                            } else if (isrootCategory) {
                                invokeFetchProductsForRootCategory();
                            } else if (isOnSale) {
                                invokeOnSaleRootCategoryDetails();

                            } else if (isShopByBrand) {
                                invokeFetchProductsForSelectedBrand();
                            } else if (fromNewArrival) {
                                invokeNewArrivalDetails();
                            } else if (promotion) {
                                fnInvokeRetrieveProductListByPromoId(
                                        productSearched, pageNum, sortSearch);
                            }
                        }
                    });
            f.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.dismiss();
                        }
                    });
            filterDialog = f.create();
            filterDialog
                    .setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            int titleDividerId = filterDialog
                                    .getContext()
                                    .getResources()
                                    .getIdentifier("titleDivider", "id",
                                            "android");

                            View titleDivider = filterDialog
                                    .findViewById(titleDividerId);
                            if (titleDivider != null) {
                                titleDivider.setBackgroundColor(filterDialog
                                        .getContext().getResources()
                                        .getColor(R.color.primaryColor));
                            }
                        }
                    });
            filterDialog.show();
            Button b = filterDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            setCustomButton(b);
        } else if (spinnerType.equals("Filter by Benefits")) {
            f.setTitle(Html.fromHtml("<font color='"
                    + getResources().getColor(R.color.dialog_title) + "'>"
                    + getResources().getString(R.string.Filter_By_Benefits)
                    + "</font>"));
            final List<FacetDetailBean> listFacetDetailBean = benefitFacets;
            final String[] items = new String[listFacetDetailBean.size()];
            if (listFacetDetailBean != null && listFacetDetailBean.size() != 0) {
                for (int i = 0; i < listFacetDetailBean.size(); i++) {
                    items[i] = listFacetDetailBean.get(i).getName();
                }
            }
            boolean markedBooleanArray[] = null;
            if (benefitId != null && !benefitId.isEmpty()) {
                String marked[] = benefitId.split(",");
                markedBooleanArray = new boolean[items.length];
                for (int j = 0; j < marked.length; j++) {
                    for (int i = 0; i < items.length; i++) {
                        if (marked[j]
                                .equals(listFacetDetailBean.get(i).getId())) {
                            markedBooleanArray[i] = true;
                            continue;
                        }
                    }
                }
            }
            f.setMultiChoiceItems(items, markedBooleanArray,
                    new OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which,
                                            boolean isChecked) {
                            if (isChecked) {
                                if (benefitId.equals("")) {
                                    benefitId = listFacetDetailBean.get(which)
                                            .getId();
                                } else {
                                    benefitId += ","
                                            + listFacetDetailBean.get(which)
                                            .getId();
                                }
                            } else {
                                if (!benefitId.equals("")) {
                                    String[] temp = benefitId.split(",");
                                    benefitId = "";
                                    for (int k = 0; k < temp.length; k++) {
                                        if (!temp[k].equals(listFacetDetailBean
                                                .get(which).getId())) {
                                            if (!benefitId.isEmpty()) {
                                                benefitId += "," + temp[k];
                                            } else {
                                                benefitId = temp[k];
                                            }
                                        }
                                    }
                                }
                            }
                            setOtherIds();
                            listWithAllProducts.clear();
                            customDialog.dismiss();
                            dialog.dismiss();
                            pd.show();
                            pageNum = 1;
                            tvSortFilterStatus.setVisibility(View.GONE);
                            tvSortFilterStatus
                                    .setText("- Filtered By Selected Benefits");
                            invokeFetchProductsForRootCategory();
                        }
                    });
            f.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.dismiss();
                        }
                    });
            filterDialog = f.create();
            filterDialog
                    .setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            int titleDividerId = filterDialog
                                    .getContext()
                                    .getResources()
                                    .getIdentifier("titleDivider", "id",
                                            "android");

                            View titleDivider = filterDialog
                                    .findViewById(titleDividerId);
                            if (titleDivider != null) {
                                titleDivider.setBackgroundColor(filterDialog
                                        .getContext().getResources()
                                        .getColor(R.color.primaryColor));
                            }
                        }
                    });
            filterDialog.show();
            Button b = filterDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            setCustomButton(b);
        } else if (spinnerType.equals("Filter by Colors")) {
            f.setTitle(Html.fromHtml("<font color='"
                    + getResources().getColor(R.color.dialog_title) + "'>"
                    + getResources().getString(R.string.Filter_By_Colors)
                    + "</font>"));
            final List<FacetDetailBean> listFacetDetailBean = colorFacets;
            final String[] items = new String[listFacetDetailBean.size()];

            if (listFacetDetailBean != null && listFacetDetailBean.size() != 0) {
                for (int i = 0; i < listFacetDetailBean.size(); i++) {
                    items[i] = listFacetDetailBean.get(i).getName();
                }
            }
            final ListAdapter adapter = new ArrayAdapter<String>(
                    UltaProductListActivity.this, R.layout.list_row, items) {

                ViewHolder holder;
                @SuppressWarnings("unused")
                Drawable icon;

                @SuppressWarnings("unused")
                Drawable check;

                class ViewHolder {
                    ImageView icon;
                    TextView title;
                    TextView missingColor;
					/* ImageView check; */
                }

                public View getView(int position, View convertView,
                                    ViewGroup parent) {
                    final LayoutInflater inflater = (LayoutInflater) UltaProductListActivity.this
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    if (convertView == null) {
                        convertView = inflater.inflate(R.layout.list_row, null);

                        holder = new ViewHolder();
                        holder.icon = (ImageView) convertView
                                .findViewById(R.id.icon);
                        holder.missingColor = (TextView) convertView
                                .findViewById(R.id.MissingColor);

                        holder.title = (TextView) convertView
                                .findViewById(R.id.title);
                        convertView.setTag(holder);
                    } else {
                        // view already defined, retrieve view holder
                        holder = (ViewHolder) convertView.getTag();
                    }
                    String Url = listFacetDetailBean.get(position).getImgURL();

                    if (Url != null && Url.length() > 2) {
                        Url = listFacetDetailBean.get(position).getImgURL();
                        new AQuery(holder.icon).image(Url, true, true, 0, 0,
                                null, AQuery.FADE_IN);
                        holder.missingColor.setVisibility(View.GONE);
                    } else {
                        holder.icon.setVisibility(View.GONE);
                        holder.missingColor.setText(items[position].substring(
                                0, 2));
                        holder.missingColor.setVisibility(View.VISIBLE);
                    }

                    holder.title.setText(items[position]);
                    return convertView;
                }
            };
            ListView listViewItems = new ListView(UltaProductListActivity.this);
            listViewItems.setAdapter(adapter);
            listViewItems.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    listWithAllProducts.clear();
                    if (!colorId.equals("")
                            && !colorId.contains(listFacetDetailBean.get(arg2)
                            .getId())) {
                        colorId += "," + listFacetDetailBean.get(arg2).getId();
                    } else if (!colorId.equals("")
                            && colorId.contains(listFacetDetailBean.get(arg2)
                            .getId())) {
                        String[] temp = colorId.split(",");
                        colorId = "";
                        for (int k = 0; k < temp.length; k++) {
                            if (temp[k].equals(listFacetDetailBean.get(arg2)
                                    .getId())) {
                                continue;
                            } else {
                                if (!colorId.equals(""))
                                    colorId += "," + temp[k];
                                else
                                    colorId = temp[k];
                            }
                        }
                    } else {
                        colorId = listFacetDetailBean.get(arg2).getId();
                    }
                    setOtherIds();
                    customDialog.dismiss();
                    pd.show();
                    filterDialog.dismiss();
                    pageNum = 1;
                    tvSortFilterStatus.setVisibility(View.GONE);
                    tvSortFilterStatus.setText("- Filtered By Selected Colors");
                    invokeFetchProductsForRootCategory();

                }
            });
            f.setView(listViewItems);
            f.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.dismiss();
                        }
                    });
            filterDialog = f.create();
            filterDialog
                    .setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            int titleDividerId = filterDialog
                                    .getContext()
                                    .getResources()
                                    .getIdentifier("titleDivider", "id",
                                            "android");

                            View titleDivider = filterDialog
                                    .findViewById(titleDividerId);
                            if (titleDivider != null) {
                                titleDivider.setBackgroundColor(filterDialog
                                        .getContext().getResources()
                                        .getColor(R.color.primaryColor));
                            }
                        }
                    });
            filterDialog.show();
            Button b = filterDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            setCustomButton(b);
        } else if (spinnerType.equals("Filter by Concerns")) {
            f.setTitle(Html.fromHtml("<font color='"
                    + getResources().getColor(R.color.dialog_title) + "'>"
                    + getResources().getString(R.string.Filter_By_Concerns)
                    + "</font>"));
            final List<FacetDetailBean> listFacetDetailBean = concernsFacets;
            final String[] items = new String[listFacetDetailBean.size()];
            if (listFacetDetailBean != null && listFacetDetailBean.size() != 0) {
                for (int i = 0; i < listFacetDetailBean.size(); i++) {
                    items[i] = listFacetDetailBean.get(i).getName();
                }
            }
            boolean markedBooleanArray[] = null;
            if (concernId != null && !concernId.isEmpty()) {
                String marked[] = concernId.split(",");
                markedBooleanArray = new boolean[items.length];
                for (int j = 0; j < marked.length; j++) {
                    for (int i = 0; i < items.length; i++) {
                        if (marked[j]
                                .equals(listFacetDetailBean.get(i).getId())) {
                            markedBooleanArray[i] = true;
                            continue;
                        }
                    }
                }
            }

            f.setMultiChoiceItems(items, markedBooleanArray,
                    new OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which,
                                            boolean isChecked) {
                            listWithAllProducts.clear();
                            if (isChecked) {
                                if (concernId.equals("")) {
                                    concernId = listFacetDetailBean.get(which)
                                            .getId();
                                } else {
                                    concernId += ","
                                            + listFacetDetailBean.get(which)
                                            .getId();
                                }
                            } else {
                                if (!concernId.equals("")) {
                                    String[] temp = concernId.split(",");
                                    concernId = "";
                                    for (int k = 0; k < temp.length; k++) {
                                        if (!temp[k].equals(listFacetDetailBean
                                                .get(which).getId())) {
                                            if (!concernId.isEmpty()) {
                                                concernId += "," + temp[k];
                                            } else {
                                                concernId = temp[k];
                                            }
                                        }
                                    }
                                }
                            }
                            setOtherIds();
                            customDialog.dismiss();
                            dialog.dismiss();
                            pd.show();
                            pageNum = 1;
                            tvSortFilterStatus.setVisibility(View.GONE);
                            tvSortFilterStatus
                                    .setText("- Filtered By Selected Concers");
                            invokeFetchProductsForRootCategory();
                        }
                    });
            f.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.dismiss();
                        }
                    });
            filterDialog = f.create();
            filterDialog
                    .setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            int titleDividerId = filterDialog
                                    .getContext()
                                    .getResources()
                                    .getIdentifier("titleDivider", "id",
                                            "android");

                            View titleDivider = filterDialog
                                    .findViewById(titleDividerId);
                            if (titleDivider != null) {
                                titleDivider.setBackgroundColor(filterDialog
                                        .getContext().getResources()
                                        .getColor(R.color.primaryColor));
                            }
                        }
                    });
            filterDialog.show();
            Button b = filterDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            setCustomButton(b);
        } else if (spinnerType.equals("Filter by Coverages")) {
            f.setTitle(Html.fromHtml("<font color='"
                    + getResources().getColor(R.color.dialog_title) + "'>"
                    + getResources().getString(R.string.Filter_By_Coverages)
                    + "</font>"));
            final List<FacetDetailBean> listFacetDetailBean = coverageFacets;
            final String[] items = new String[listFacetDetailBean.size()];
            if (listFacetDetailBean != null && listFacetDetailBean.size() != 0) {
                for (int i = 0; i < listFacetDetailBean.size(); i++) {
                    items[i] = listFacetDetailBean.get(i).getName();
                }
            }
            boolean markedBooleanArray[] = null;
            if (coverageId != null && !coverageId.isEmpty()) {
                String marked[] = coverageId.split(",");
                markedBooleanArray = new boolean[items.length];
                for (int j = 0; j < marked.length; j++) {
                    for (int i = 0; i < items.length; i++) {
                        if (marked[j]
                                .equals(listFacetDetailBean.get(i).getId())) {
                            markedBooleanArray[i] = true;
                            continue;
                        }
                    }
                }
            }
            f.setMultiChoiceItems(items, markedBooleanArray,
                    new OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which,
                                            boolean isChecked) {
                            listWithAllProducts.clear();
                            if (isChecked) {
                                if (coverageId.equals("")) {
                                    coverageId = listFacetDetailBean.get(which)
                                            .getId();
                                } else {
                                    coverageId += ","
                                            + listFacetDetailBean.get(which)
                                            .getId();
                                }
                            } else {
                                if (!coverageId.equals("")) {
                                    String[] temp = coverageId.split(",");
                                    coverageId = "";
                                    for (int k = 0; k < temp.length; k++) {
                                        if (!temp[k].equals(listFacetDetailBean
                                                .get(which).getId())) {
                                            if (!coverageId.isEmpty()) {
                                                coverageId += "," + temp[k];
                                            } else {
                                                coverageId = temp[k];
                                            }
                                        }
                                    }
                                }
                            }
                            setOtherIds();
                            customDialog.dismiss();
                            dialog.dismiss();
                            pd.show();
                            pageNum = 1;
                            tvSortFilterStatus.setVisibility(View.GONE);
                            tvSortFilterStatus
                                    .setText("- Filtered By Selected Coverages");
                            invokeFetchProductsForRootCategory();
                        }
                    });

            f.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.dismiss();
                        }
                    });
            filterDialog = f.create();
            filterDialog
                    .setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            int titleDividerId = filterDialog
                                    .getContext()
                                    .getResources()
                                    .getIdentifier("titleDivider", "id",
                                            "android");

                            View titleDivider = filterDialog
                                    .findViewById(titleDividerId);
                            if (titleDivider != null) {
                                titleDivider.setBackgroundColor(filterDialog
                                        .getContext().getResources()
                                        .getColor(R.color.primaryColor));
                            }
                        }
                    });
            filterDialog.show();
            Button b = filterDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            setCustomButton(b);
        } else if (spinnerType.equals("Filter by Finish")) {
            f.setTitle(Html.fromHtml("<font color='"
                    + getResources().getColor(R.color.dialog_title) + "'>"
                    + getResources().getString(R.string.Filter_By_Finish)
                    + "</font>"));
            final List<FacetDetailBean> listFacetDetailBean = finishFacets;
            final String[] items = new String[listFacetDetailBean.size()];
            if (listFacetDetailBean != null && listFacetDetailBean.size() != 0) {
                for (int i = 0; i < listFacetDetailBean.size(); i++) {
                    items[i] = listFacetDetailBean.get(i).getName();
                }
            }
            boolean markedBooleanArray[] = null;
            if (finishId != null && !finishId.isEmpty()) {
                String marked[] = finishId.split(",");
                markedBooleanArray = new boolean[items.length];
                for (int j = 0; j < marked.length; j++) {
                    for (int i = 0; i < items.length; i++) {
                        if (marked[j]
                                .equals(listFacetDetailBean.get(i).getId())) {
                            markedBooleanArray[i] = true;
                            continue;
                        }
                    }
                }
            }
            f.setMultiChoiceItems(items, markedBooleanArray,
                    new OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which,
                                            boolean isChecked) {
                            Logger.Log("Items Selecting" + items[which]);
                            listWithAllProducts.clear();
                            if (isChecked) {
                                if (finishId.equals("")) {
                                    finishId = listFacetDetailBean.get(which)
                                            .getId();
                                } else {
                                    finishId += ","
                                            + listFacetDetailBean.get(which)
                                            .getId();
                                }
                            } else {
                                if (!finishId.equals("")) {
                                    String[] temp = finishId.split(",");
                                    finishId = "";
                                    for (int k = 0; k < temp.length; k++) {
                                        if (!temp[k].equals(listFacetDetailBean
                                                .get(which).getId())) {
                                            if (!finishId.isEmpty()) {
                                                finishId += "," + temp[k];
                                            } else {
                                                finishId = temp[k];
                                            }
                                        }
                                    }
                                }
                            }
                            setOtherIds();
                            customDialog.dismiss();
                            dialog.dismiss();
                            pd.show();
                            pageNum = 1;
                            tvSortFilterStatus.setVisibility(View.GONE);
                            tvSortFilterStatus
                                    .setText("- Filtered By Selected Finish Facerts");
                            invokeFetchProductsForRootCategory();
                        }
                    });

            f.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.dismiss();
                        }
                    });
            filterDialog = f.create();
            filterDialog
                    .setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            int titleDividerId = filterDialog
                                    .getContext()
                                    .getResources()
                                    .getIdentifier("titleDivider", "id",
                                            "android");

                            View titleDivider = filterDialog
                                    .findViewById(titleDividerId);
                            if (titleDivider != null) {
                                titleDivider.setBackgroundColor(filterDialog
                                        .getContext().getResources()
                                        .getColor(R.color.primaryColor));
                            }
                        }
                    });
            filterDialog.show();
            Button b = filterDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            setCustomButton(b);
        } else if (spinnerType.equals("Filter by Forms")) {
            f.setTitle(Html.fromHtml("<font color='"
                    + getResources().getColor(R.color.dialog_title) + "'>"
                    + getResources().getString(R.string.Filter_By_Forms)
                    + "</font>"));
            final List<FacetDetailBean> listFacetDetailBean = formFacets;
            final String[] items = new String[listFacetDetailBean.size()];
            if (listFacetDetailBean != null && listFacetDetailBean.size() != 0) {
                for (int i = 0; i < listFacetDetailBean.size(); i++) {
                    items[i] = listFacetDetailBean.get(i).getName();
                }
            }
            boolean markedBooleanArray[] = null;
            if (formId != null && !formId.isEmpty()) {
                String marked[] = formId.split(",");
                markedBooleanArray = new boolean[items.length];

                for (int j = 0; j < marked.length; j++) {
                    for (int i = 0; i < items.length; i++) {
                        if (marked[j]
                                .equals(listFacetDetailBean.get(i).getId())) {
                            markedBooleanArray[i] = true;
                            continue;
                        }
                    }
                }
            }
            f.setMultiChoiceItems(items, markedBooleanArray,
                    new OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which,
                                            boolean isChecked) {
                            Logger.Log("Items Selecting" + items[which]);
                            listWithAllProducts.clear();
                            if (isChecked) {
                                if (formId.equals("")) {
                                    formId = listFacetDetailBean.get(which)
                                            .getId();
                                } else {
                                    formId += ","
                                            + listFacetDetailBean.get(which)
                                            .getId();
                                }
                            } else {
                                if (!formId.equals("")) {
                                    String[] temp = formId.split(",");
                                    formId = "";
                                    for (int k = 0; k < temp.length; k++) {
                                        if (!temp[k].equals(listFacetDetailBean
                                                .get(which).getId())) {
                                            if (!formId.isEmpty()) {
                                                formId += "," + temp[k];
                                            } else {
                                                formId = temp[k];
                                            }
                                        }
                                    }
                                }
                            }
                            setOtherIds();
                            customDialog.dismiss();
                            dialog.dismiss();
                            pd.show();
                            pageNum = 1;
                            tvSortFilterStatus.setVisibility(View.GONE);
                            tvSortFilterStatus
                                    .setText("- Filtered By Selected Forms");
                            invokeFetchProductsForRootCategory();
                        }
                    });
            f.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.dismiss();
                        }
                    });
            filterDialog = f.create();
            filterDialog
                    .setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            int titleDividerId = filterDialog
                                    .getContext()
                                    .getResources()
                                    .getIdentifier("titleDivider", "id",
                                            "android");

                            View titleDivider = filterDialog
                                    .findViewById(titleDividerId);
                            if (titleDivider != null) {
                                titleDivider.setBackgroundColor(filterDialog
                                        .getContext().getResources()
                                        .getColor(R.color.primaryColor));
                            }
                        }
                    });
            filterDialog.show();
            Button b = filterDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            setCustomButton(b);
        } else if (spinnerType.equals("Filter by Ingredients")) {
            f.setTitle(Html.fromHtml("<font color='"
                    + getResources().getColor(R.color.dialog_title) + "'>"
                    + getResources().getString(R.string.Filter_By_Ingredients)
                    + "</font>"));
            final List<FacetDetailBean> listFacetDetailBean = ingredientsFacets;
            final String[] items = new String[listFacetDetailBean.size()];
            if (listFacetDetailBean != null && listFacetDetailBean.size() != 0) {
                for (int i = 0; i < listFacetDetailBean.size(); i++) {
                    items[i] = listFacetDetailBean.get(i).getName();
                }
            }
            boolean markedBooleanArray[] = null;
            if (ingredientId != null && !ingredientId.isEmpty()) {
                String marked[] = ingredientId.split(",");
                markedBooleanArray = new boolean[items.length];
                for (int j = 0; j < marked.length; j++) {
                    for (int i = 0; i < items.length; i++) {
                        if (marked[j]
                                .equals(listFacetDetailBean.get(i).getId())) {
                            markedBooleanArray[i] = true;
                            continue;
                        }
                    }
                }
            }
            f.setMultiChoiceItems(items, markedBooleanArray,
                    new OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which,
                                            boolean isChecked) {
                            Logger.Log("Items Selecting" + items[which]);
                            listWithAllProducts.clear();
                            if (isChecked) {
                                if (ingredientId.equals("")) {
                                    ingredientId = listFacetDetailBean.get(
                                            which).getId();
                                } else {
                                    ingredientId += ","
                                            + listFacetDetailBean.get(which)
                                            .getId();
                                }
                            } else {
                                if (!ingredientId.equals("")) {
                                    String[] temp = ingredientId.split(",");
                                    ingredientId = "";
                                    for (int k = 0; k < temp.length; k++) {
                                        if (!temp[k].equals(listFacetDetailBean
                                                .get(which).getId())) {
                                            if (!ingredientId.isEmpty()) {
                                                ingredientId += "," + temp[k];
                                            } else {
                                                ingredientId = temp[k];
                                            }
                                        }
                                    }
                                }
                            }
                            setOtherIds();
                            customDialog.dismiss();
                            dialog.dismiss();
                            pd.show();
                            pageNum = 1;
                            tvSortFilterStatus.setVisibility(View.GONE);
                            tvSortFilterStatus
                                    .setText("- Filtered By Selected Ingredients");
                            invokeFetchProductsForRootCategory();
                        }
                    });
            f.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.dismiss();
                        }
                    });
            filterDialog = f.create();
            filterDialog
                    .setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            int titleDividerId = filterDialog
                                    .getContext()
                                    .getResources()
                                    .getIdentifier("titleDivider", "id",
                                            "android");

                            View titleDivider = filterDialog
                                    .findViewById(titleDividerId);
                            if (titleDivider != null) {
                                titleDivider.setBackgroundColor(filterDialog
                                        .getContext().getResources()
                                        .getColor(R.color.primaryColor));
                            }
                        }
                    });
            filterDialog.show();
            Button b = filterDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            setCustomButton(b);
        } else if (spinnerType.equals("Filter by Types")) {
            f.setTitle(Html.fromHtml("<font color='"
                    + getResources().getColor(R.color.dialog_title) + "'>"
                    + getResources().getString(R.string.Filter_By_Types)
                    + "</font>"));
            final List<FacetDetailBean> listFacetDetailBean = typeFacets;
            final String[] items = new String[listFacetDetailBean.size()];
            if (listFacetDetailBean != null && listFacetDetailBean.size() != 0) {
                for (int i = 0; i < listFacetDetailBean.size(); i++) {
                    items[i] = listFacetDetailBean.get(i).getName();
                }
            }
            boolean markedBooleanArray[] = null;
            if (tyepeId != null && !tyepeId.isEmpty()) {
                String marked[] = tyepeId.split(",");
                markedBooleanArray = new boolean[items.length];
                for (int j = 0; j < marked.length; j++) {
                    for (int i = 0; i < items.length; i++) {
                        if (marked[j]
                                .equals(listFacetDetailBean.get(i).getId())) {
                            markedBooleanArray[i] = true;
                            continue;
                        }
                    }
                }
            }
            f.setMultiChoiceItems(items, markedBooleanArray,
                    new OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which,
                                            boolean isChecked) {
                            Logger.Log("Items Selecting" + items[which]);
                            listWithAllProducts.clear();
                            if (isChecked) {
                                if (tyepeId.equals("")) {
                                    tyepeId = listFacetDetailBean.get(which)
                                            .getId();
                                } else {
                                    tyepeId += ","
                                            + listFacetDetailBean.get(which)
                                            .getId();
                                }
                            } else {
                                if (!tyepeId.equals("")) {
                                    String[] temp = tyepeId.split(",");
                                    tyepeId = "";
                                    for (int k = 0; k < temp.length; k++) {
                                        if (!temp[k].equals(listFacetDetailBean
                                                .get(which).getId())) {
                                            if (!tyepeId.isEmpty()) {
                                                tyepeId += "," + temp[k];
                                            } else {
                                                tyepeId = temp[k];
                                            }
                                        }
                                    }
                                }
                            }
                            setOtherIds();
                            customDialog.dismiss();
                            dialog.dismiss();
                            pd.show();
                            pageNum = 1;
                            tvSortFilterStatus.setVisibility(View.GONE);
                            tvSortFilterStatus
                                    .setText("- Filtered By Selected Types");
                            invokeFetchProductsForRootCategory();
                        }
                    });
            f.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.dismiss();
                        }
                    });
            filterDialog = f.create();
            filterDialog
                    .setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            int titleDividerId = filterDialog
                                    .getContext()
                                    .getResources()
                                    .getIdentifier("titleDivider", "id",
                                            "android");

                            View titleDivider = filterDialog
                                    .findViewById(titleDividerId);
                            if (titleDivider != null) {
                                titleDivider.setBackgroundColor(filterDialog
                                        .getContext().getResources()
                                        .getColor(R.color.primaryColor));
                            }
                        }
                    });
            filterDialog.show();
            Button b = filterDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            setCustomButton(b);
        } else if (spinnerType.equals("Filter by Treatment")) {
            f.setTitle(Html.fromHtml("<font color='"
                    + getResources().getColor(R.color.dialog_title) + "'>"
                    + getResources().getString(R.string.Filter_By_Treatment)
                    + "</font>"));
            final List<FacetDetailBean> listFacetDetailBean = treatmentFacets;
            final String[] items = new String[listFacetDetailBean.size()];
            if (listFacetDetailBean != null && listFacetDetailBean.size() != 0) {
                for (int i = 0; i < listFacetDetailBean.size(); i++) {
                    items[i] = listFacetDetailBean.get(i).getName();
                }
            }
            boolean markedBooleanArray[] = null;
            if (treatmentId != null && !treatmentId.isEmpty()) {
                String marked[] = treatmentId.split(",");
                markedBooleanArray = new boolean[items.length];
                for (int j = 0; j < marked.length; j++) {
                    for (int i = 0; i < items.length; i++) {
                        if (marked[j]
                                .equals(listFacetDetailBean.get(i).getId())) {
                            markedBooleanArray[i] = true;
                            continue;
                        }
                    }
                }
            }
            f.setMultiChoiceItems(items, markedBooleanArray,
                    new OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which,
                                            boolean isChecked) {
                            Logger.Log("Items Selecting" + items[which]);
                            listWithAllProducts.clear();
                            if (isChecked) {
                                if (treatmentId.equals("")) {
                                    treatmentId = listFacetDetailBean
                                            .get(which).getId();
                                } else {
                                    treatmentId += ","
                                            + listFacetDetailBean.get(which)
                                            .getId();
                                }
                            } else {
                                if (!treatmentId.equals("")) {
                                    String[] temp = treatmentId.split(",");
                                    treatmentId = "";
                                    for (int k = 0; k < temp.length; k++) {
                                        if (!temp[k].equals(listFacetDetailBean
                                                .get(which).getId())) {
                                            if (!treatmentId.isEmpty()) {
                                                treatmentId += "," + temp[k];
                                            } else {
                                                treatmentId = temp[k];
                                            }
                                        }
                                    }
                                }
                            }
                            setOtherIds();
                            customDialog.dismiss();
                            dialog.dismiss();
                            pd.show();
                            pageNum = 1;
                            tvSortFilterStatus.setVisibility(View.GONE);
                            tvSortFilterStatus
                                    .setText("- Filtered By Selected Treatment");
                            invokeFetchProductsForRootCategory();
                        }
                    });
            f.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.dismiss();
                        }
                    });
            filterDialog = f.create();
            filterDialog
                    .setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            int titleDividerId = filterDialog
                                    .getContext()
                                    .getResources()
                                    .getIdentifier("titleDivider", "id",
                                            "android");

                            View titleDivider = filterDialog
                                    .findViewById(titleDividerId);
                            if (titleDivider != null) {
                                titleDivider.setBackgroundColor(filterDialog
                                        .getContext().getResources()
                                        .getColor(R.color.primaryColor));
                            }
                        }
                    });
            filterDialog.show();
            Button b = filterDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            setCustomButton(b);
        } else if (spinnerType.equals("Filter by Pupose")) {
            f.setTitle(Html.fromHtml("<font color='"
                    + getResources().getColor(R.color.dialog_title) + "'>"
                    + getResources().getString(R.string.Filter_By_Pupose)
                    + "</font>"));
            final List<FacetDetailBean> listFacetDetailBean = purposeFacets;
            final String[] items = new String[listFacetDetailBean.size()];
            if (listFacetDetailBean != null && listFacetDetailBean.size() != 0) {
                for (int i = 0; i < listFacetDetailBean.size(); i++) {
                    items[i] = listFacetDetailBean.get(i).getName();
                }
            }
            boolean markedBooleanArray[] = null;
            if (purposeId != null && !purposeId.isEmpty()) {
                String marked[] = purposeId.split(",");
                markedBooleanArray = new boolean[items.length];
                for (int j = 0; j < marked.length; j++) {
                    for (int i = 0; i < items.length; i++) {
                        if (marked[j]
                                .equals(listFacetDetailBean.get(i).getId())) {
                            markedBooleanArray[i] = true;
                            continue;
                        }
                    }
                }
            }
            f.setMultiChoiceItems(items, markedBooleanArray,
                    new OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which,
                                            boolean isChecked) {
                            Logger.Log("Items Selecting" + items[which]);
                            listWithAllProducts.clear();
                            if (isChecked) {
                                if (purposeId.equals("")) {
                                    purposeId = listFacetDetailBean.get(which)
                                            .getId();
                                } else {
                                    purposeId += ","
                                            + listFacetDetailBean.get(which)
                                            .getId();
                                }
                            } else {
                                if (!purposeId.equals("")) {
                                    String[] temp = purposeId.split(",");
                                    purposeId = "";
                                    for (int k = 0; k < temp.length; k++) {
                                        if (!temp[k].equals(listFacetDetailBean
                                                .get(which).getId())) {
                                            if (!purposeId.isEmpty()) {
                                                purposeId += "," + temp[k];
                                            } else {
                                                purposeId = temp[k];
                                            }
                                        }
                                    }
                                }
                            }
                            setOtherIds();
                            customDialog.dismiss();
                            dialog.dismiss();
                            pd.show();
                            pageNum = 1;
                            tvSortFilterStatus.setVisibility(View.GONE);
                            tvSortFilterStatus
                                    .setText("- Filtered By Selected Purpose");
                            invokeFetchProductsForRootCategory();
                        }
                    });
            f.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.dismiss();
                        }
                    });
            filterDialog = f.create();
            filterDialog
                    .setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            int titleDividerId = filterDialog
                                    .getContext()
                                    .getResources()
                                    .getIdentifier("titleDivider", "id",
                                            "android");

                            View titleDivider = filterDialog
                                    .findViewById(titleDividerId);
                            if (titleDivider != null) {
                                titleDivider.setBackgroundColor(filterDialog
                                        .getContext().getResources()
                                        .getColor(R.color.primaryColor));
                            }
                        }
                    });
            filterDialog.show();
            Button b = filterDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            setCustomButton(b);
        } else if (spinnerType.equals("Filter by Scents")) {
            f.setTitle(Html.fromHtml("<font color='"
                    + getResources().getColor(R.color.dialog_title) + "'>"
                    + getResources().getString(R.string.Filter_By_Scents)
                    + "</font>"));
            final List<FacetDetailBean> listFacetDetailBean = scentFacets;
            final String[] items = new String[listFacetDetailBean.size()];
            if (listFacetDetailBean != null && listFacetDetailBean.size() != 0) {
                for (int i = 0; i < listFacetDetailBean.size(); i++) {
                    items[i] = listFacetDetailBean.get(i).getName();
                }
            }
            boolean markedBooleanArray[] = null;
            if (scentId != null && !scentId.isEmpty()) {
                String marked[] = scentId.split(",");
                markedBooleanArray = new boolean[items.length];
                for (int j = 0; j < marked.length; j++) {
                    for (int i = 0; i < items.length; i++) {
                        if (marked[j]
                                .equals(listFacetDetailBean.get(i).getId())) {
                            markedBooleanArray[i] = true;
                            continue;
                        }
                    }
                }
            }
            f.setMultiChoiceItems(items, markedBooleanArray,
                    new OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which,
                                            boolean isChecked) {
                            Logger.Log("Items Selecting" + items[which]);
                            listWithAllProducts.clear();
                            if (isChecked) {
                                if (scentId.equals("")) {
                                    scentId = listFacetDetailBean.get(which)
                                            .getId();
                                } else {
                                    scentId += ","
                                            + listFacetDetailBean.get(which)
                                            .getId();
                                }
                            } else {
                                if (!scentId.equals("")) {
                                    String[] temp = scentId.split(",");
                                    scentId = "";
                                    for (int k = 0; k < temp.length; k++) {
                                        if (!temp[k].equals(listFacetDetailBean
                                                .get(which).getId())) {
                                            if (!scentId.isEmpty()) {
                                                scentId += "," + temp[k];
                                            } else {
                                                scentId = temp[k];
                                            }
                                        }
                                    }
                                }
                            }
                            setOtherIds();
                            customDialog.dismiss();
                            dialog.dismiss();
                            pd.show();
                            pageNum = 1;
                            tvSortFilterStatus.setVisibility(View.GONE);
                            tvSortFilterStatus
                                    .setText("- Filtered By Selected Scents");
                            invokeFetchProductsForRootCategory();
                        }
                    });
            f.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.dismiss();
                        }
                    });
            filterDialog = f.create();
            filterDialog
                    .setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            int titleDividerId = filterDialog
                                    .getContext()
                                    .getResources()
                                    .getIdentifier("titleDivider", "id",
                                            "android");

                            View titleDivider = filterDialog
                                    .findViewById(titleDividerId);
                            if (titleDivider != null) {
                                titleDivider.setBackgroundColor(filterDialog
                                        .getContext().getResources()
                                        .getColor(R.color.primaryColor));
                            }
                        }
                    });
            filterDialog.show();
            Button b = filterDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            setCustomButton(b);
        } else if (spinnerType.equals("Filter by Skin")) {
            f.setTitle(Html.fromHtml("<font color='"
                    + getResources().getColor(R.color.dialog_title) + "'>"
                    + getResources().getString(R.string.Filter_By_Skin)
                    + "</font>"));
            final List<FacetDetailBean> listFacetDetailBean = skinTypeFacets;
            final String[] items = new String[listFacetDetailBean.size()];
            if (listFacetDetailBean != null && listFacetDetailBean.size() != 0) {
                for (int i = 0; i < listFacetDetailBean.size(); i++) {
                    items[i] = listFacetDetailBean.get(i).getName();
                }
            }
            boolean markedBooleanArray[] = null;
            if (skinTypeId != null && !skinTypeId.isEmpty()) {
                String marked[] = skinTypeId.split(",");
                markedBooleanArray = new boolean[items.length];
                for (int j = 0; j < marked.length; j++) {
                    for (int i = 0; i < items.length; i++) {
                        if (marked[j]
                                .equals(listFacetDetailBean.get(i).getId())) {
                            markedBooleanArray[i] = true;
                            continue;
                        }
                    }
                }
            }
            f.setMultiChoiceItems(items, markedBooleanArray,
                    new OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which,
                                            boolean isChecked) {
                            Logger.Log("Items Selecting" + items[which]);
                            listWithAllProducts.clear();
                            if (isChecked) {
                                if (skinTypeId.equals("")) {
                                    skinTypeId = listFacetDetailBean.get(which)
                                            .getId();
                                } else {
                                    skinTypeId += ","
                                            + listFacetDetailBean.get(which)
                                            .getId();
                                }
                            } else {
                                if (!skinTypeId.equals("")) {
                                    String[] temp = skinTypeId.split(",");
                                    skinTypeId = "";
                                    for (int k = 0; k < temp.length; k++) {
                                        if (!temp[k].equals(listFacetDetailBean
                                                .get(which).getId())) {
                                            if (!skinTypeId.isEmpty()) {
                                                skinTypeId += "," + temp[k];
                                            } else {
                                                skinTypeId = temp[k];
                                            }
                                        }
                                    }
                                }
                            }
                            setOtherIds();
                            customDialog.dismiss();
                            dialog.dismiss();
                            dialog.dismiss();
                            pd.show();
                            pageNum = 1;
                            tvSortFilterStatus.setVisibility(View.GONE);
                            tvSortFilterStatus
                                    .setText("- Filtered By Selected Skin");
                            invokeFetchProductsForRootCategory();
                        }
                    });

            f.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.dismiss();
                        }
                    });
            filterDialog = f.create();
            filterDialog
                    .setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            int titleDividerId = filterDialog
                                    .getContext()
                                    .getResources()
                                    .getIdentifier("titleDivider", "id",
                                            "android");

                            View titleDivider = filterDialog
                                    .findViewById(titleDividerId);
                            if (titleDivider != null) {
                                titleDivider.setBackgroundColor(filterDialog
                                        .getContext().getResources()
                                        .getColor(R.color.primaryColor));
                            }
                        }
                    });
            filterDialog.show();
            Button b = filterDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            setCustomButton(b);
        } else if (spinnerType.equals("Filter by Spf")) {
            f.setTitle("Filter By SPF");
            f.setTitle(Html.fromHtml("<font color='"
                    + getResources().getColor(R.color.dialog_title) + "'>"
                    + getResources().getString(R.string.Filter_By_Spf)
                    + "</font>"));
            final List<FacetDetailBean> listFacetDetailBean = spfFacets;
            final String[] items = new String[listFacetDetailBean.size()];
            if (listFacetDetailBean != null && listFacetDetailBean.size() != 0) {
                for (int i = 0; i < listFacetDetailBean.size(); i++) {
                    items[i] = listFacetDetailBean.get(i).getName();
                }
            }
            boolean markedBooleanArray[] = null;
            if (spfId != null && !spfId.isEmpty()) {
                String marked[] = spfId.split(",");
                markedBooleanArray = new boolean[items.length];
                for (int j = 0; j < marked.length; j++) {
                    for (int i = 0; i < items.length; i++) {
                        if (marked[j]
                                .equals(listFacetDetailBean.get(i).getId())) {
                            markedBooleanArray[i] = true;
                            continue;
                        }
                    }
                }
            }
            f.setMultiChoiceItems(items, markedBooleanArray,
                    new OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which,
                                            boolean isChecked) {
                            Logger.Log("Items Selecting" + items[which]);
                            listWithAllProducts.clear();
                            if (isChecked) {
                                if (spfId.equals("")) {
                                    spfId = listFacetDetailBean.get(which)
                                            .getId();
                                } else {
                                    spfId += ","
                                            + listFacetDetailBean.get(which)
                                            .getId();
                                }
                            } else {
                                if (!spfId.equals("")) {
                                    String[] temp = spfId.split(",");
                                    spfId = "";
                                    for (int k = 0; k < temp.length; k++) {
                                        if (!temp[k].equals(listFacetDetailBean
                                                .get(which).getId())) {
                                            if (!spfId.isEmpty()) {
                                                spfId += "," + temp[k];
                                            } else {
                                                spfId = temp[k];
                                            }
                                        }
                                    }
                                }
                            }
                            setOtherIds();
                            customDialog.dismiss();
                            dialog.dismiss();
                            pd.show();
                            pageNum = 1;
                            tvSortFilterStatus.setVisibility(View.GONE);
                            tvSortFilterStatus
                                    .setText("- Filtered By Selected SPF");
                            invokeFetchProductsForRootCategory();
                        }
                    });
            f.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.dismiss();
                        }
                    });
            filterDialog = f.create();
            filterDialog
                    .setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            int titleDividerId = filterDialog
                                    .getContext()
                                    .getResources()
                                    .getIdentifier("titleDivider", "id",
                                            "android");

                            View titleDivider = filterDialog
                                    .findViewById(titleDividerId);
                            if (titleDivider != null) {
                                titleDivider.setBackgroundColor(filterDialog
                                        .getContext().getResources()
                                        .getColor(R.color.primaryColor));
                            }
                        }
                    });
            filterDialog.show();
            Button b = filterDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            setCustomButton(b);
        }

    }

    private void invokeOnSaleRootCategoryDetails() {
        InvokerParams<SearchBean> invokerParamsOnSale = new InvokerParams<SearchBean>();
        invokerParamsOnSale
                .setServiceToInvoke(WebserviceConstants.FETCH_ONSALE_ITEMS);
        invokerParamsOnSale.setHttpMethod(HttpMethod.POST);
        invokerParamsOnSale.setHttpProtocol(HttpProtocol.http);
        invokerParamsOnSale
                .setUrlParameters(populateOnSaleRetrieveRootCategoriesDetailsHandlerParameters());
        invokerParamsOnSale.setUltaBeanClazz(SearchBean.class);
        RetrieveOnSaleRootCategoriesDetailsHandler retrieveOnSaleRootCategoriesDetailsHandler = new RetrieveOnSaleRootCategoriesDetailsHandler();
        invokerParamsOnSale
                .setUltaHandler(retrieveOnSaleRootCategoriesDetailsHandler);
        try {
            new ExecutionDelegator(invokerParamsOnSale);
        } catch (UltaException ultaException) {
            Logger.Log("<UltaHomeActivity><invokeRootCategoryDetails()><UltaException>>"
                    + ultaException);
        }
    }

    /**
     * Invoking web service for new arrivals
     */
    private void invokeNewArrivalDetails() {
        InvokerParams<SearchBean> invokerParamsOnSale = new InvokerParams<SearchBean>();
        invokerParamsOnSale
                .setServiceToInvoke(WebserviceConstants.FETCH_NEW_ARRIVAL_ITEMS);
        invokerParamsOnSale.setHttpMethod(HttpMethod.POST);
        invokerParamsOnSale.setHttpProtocol(HttpProtocol.http);
        invokerParamsOnSale.setUrlParameters(fnPopulateNewArrivalParameters());
        invokerParamsOnSale.setUltaBeanClazz(SearchBean.class);
        RetrieveOnSaleRootCategoriesDetailsHandler retrieveOnSaleRootCategoriesDetailsHandler = new RetrieveOnSaleRootCategoriesDetailsHandler();
        invokerParamsOnSale
                .setUltaHandler(retrieveOnSaleRootCategoriesDetailsHandler);
        try {
            new ExecutionDelegator(invokerParamsOnSale);
        } catch (UltaException ultaException) {
            Logger.Log("<UltaHomeActivity><invokeRootCategoryDetails()><UltaException>>"
                    + ultaException);
        }
    }

    private Map<String, String> populateOnSaleRetrieveRootCategoriesDetailsHandlerParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-depth", "2");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("pageNumber", "" + pageNum);
        urlParams.put("howMany", "12");
        urlParams.put("sortBy", sortSearch);
        urlParams.put("categoryDimId", categoryId);
        urlParams.put("brandDimIds", brandId);
        urlParams.put("promotionDimIds", promotionid);
        urlParams.put("minPrice", maxPrice);
        urlParams.put("maxPrice", minPrice);
        return urlParams;
    }

    /**
     * The Class RetrieveRootCategoriesDetailsHandler of Onsale
     */
    public class RetrieveOnSaleRootCategoriesDetailsHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg
         *            the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<RetrieveRootCategoriesDetailsHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));

            if (null != getErrorMessage()) {
                if (getErrorMessage().startsWith("401")) {
                    askRelogin(UltaProductListActivity.this);
                } else {
                    pd.dismiss();
                    loadingDialog.setVisibility(View.GONE);
                    try {
                        notifyUser(
                                Utility.formatDisplayError(getErrorMessage()),
                                UltaProductListActivity.this);
                        setError(UltaProductListActivity.this,
                                getErrorMessage());
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                }
            } else {
                pd.dismiss();
                loadingDialog.setVisibility(View.GONE);
                Logger.Log("<SearchHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                productsSearched = (SearchBean) getResponseBean();

                if (null != productsSearched) {
                    filterTV.setEnabled(true);
                    linearLayout3.setVisibility(View.VISIBLE);
                    sortFilterStatusLayout.setVisibility(View.VISIBLE);
                    count = productsSearched.getTotalNoOfProducts();

                    if (count == 0) {

                        String message = "";

                        if (fromScan) {
                            message = "Unable to locate product online. Sold in store only.";
                        } else {
                            message = "Search was unable to find any results.";
                        }

                        try {
                            final Dialog alert = showAlertDialog(
                                    UltaProductListActivity.this, "Sorry", message,
                                    "OK", "");
                            alert.show();
                            alert.setCancelable(false);


                        mDisagreeButton.setVisibility(View.GONE);
                        mAgreeButton.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                alert.dismiss();
                                finish();
                            }
                        });
                        }
                        catch (Exception e) {
                            Logger.Log("<UltaProductListActivity><RetrieveOnSaleRootCategoriesDetailsHandler><Exception>>"
                                    + e);

                        }
                    } else {
                        if (!isSearchFilter) {
                            if (null != altText) {
                                tvItemNumber.setText("" + count + " Products");
                            } else {
                                tvItemNumber.setText("" + count + " Products");
                            }
                        }
                        listOfProductsInListBean = new ArrayList<ProductsInListBean>();
                        List<SearchResultsBean> products = new ArrayList<SearchResultsBean>();
                        products = productsSearched.getSearchResults();
                        for (int loop = 0; loop < products.size(); loop++) {
                            ProductsInListBean productinlistbean = new ProductsInListBean();
                            productinlistbean.setBrandName(products.get(loop)
                                    .getBrandName());
                            productinlistbean.setDisplayName(products.get(loop)
                                    .getDisplayName());
                            productinlistbean.setId(products.get(loop).getId());
                            productinlistbean.setIsGWP(products.get(loop)
                                    .getIsGWP());
                            productinlistbean.setListPriceFrom(products.get(
                                    loop).getListPriceFrom());
                            productinlistbean.setListPriceTo(products.get(loop)
                                    .getListPriceTo());
                            productinlistbean.setSalePriceFrom(products.get(
                                    loop).getSalePriceFrom());
                            productinlistbean.setSalePriceTo(products.get(loop)
                                    .getSalePriceTo());
                            productinlistbean.setSmallImageUrl(products.get(
                                    loop).getSmallImageUrl());
                            productinlistbean.setRating(products.get(loop)
                                    .getRating());
                            if (products.get(loop).getBadgeName() != null) {
                                productinlistbean.setBadgeName(products.get(
                                        loop).getBadgeName());
                            }
                            if (products.get(loop).getGiftUrl() != null) {

                                productinlistbean.setGiftUrl(products.get(loop)
                                        .getGiftUrl());
                            }
                            if (products.get(loop).getOfferType() != null) {
                                productinlistbean.setOfferType(products.get(
                                        loop).getOfferType());
                            }
                            if (products.get(loop).getOfferDesc() != null) {
                                productinlistbean.setOfferDesc(products.get(
                                        loop).getOfferDesc());
                            }
                            productinlistbean.setReviews(products.get(loop)
                                    .getReviews());
                            if (products.get(loop).getHasSkusOnSale() != null
                                    && products.get(loop).getHasSkusOnSale()
                                    .equals("true")) {
                                productinlistbean.setHasSkusOnSale(true);
                            } else {
                                productinlistbean.setHasSkusOnSale(false);
                            }
                            listOfProductsInListBean.add(productinlistbean);
                            Logger.Log("<<<<<<<<<<<<<<<<<<<<<<<<<<list>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
                                    + listOfProductsInListBean.get(loop)
                                    .getBrandName());
                        }
                        if (listOfProductsInListBean != null) {
                            listWithAllProducts
                                    .addAll(listOfProductsInListBean);
                        }
                        if (null == productListAdapter) {
                            fnSetAdapters();
                        } else {
                            productListAdapter.notifyDataSetChanged();
                        }
                        FacetListingBean facetGroupList = productsSearched
                                .getFacetListing();
                        FacetsBean fbean = new FacetsBean();
                        fbean.setBrandFacets(facetGroupList.getBrandFacets());
                        fbean.setCategoryFacets(facetGroupList
                                .getCategoryFacets());
                        fbean.setPriceFacets(facetGroupList.getPriceFacets());
                        promotionFacets = facetGroupList.getPromotionFacets();
                        facetsBean = fbean;
                        tvSortFilterStatus.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(UltaProductListActivity.this,
                            "search bean null", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void invokeFetchProductsForSelectedBrand() {
        InvokerParams<SearchBean> invokerParamsOnSale = new InvokerParams<SearchBean>();
        invokerParamsOnSale
                .setServiceToInvoke(WebserviceConstants.FETCH_PRODUCTS_FOR_BRAND);
        invokerParamsOnSale.setHttpMethod(HttpMethod.POST);
        invokerParamsOnSale.setHttpProtocol(HttpProtocol.http);
        invokerParamsOnSale
                .setUrlParameters(populateParametersForFetchByBrand());
        invokerParamsOnSale.setUltaBeanClazz(SearchBean.class);
        RetrieveOnSaleRootCategoriesDetailsHandler retrieveOnSaleRootCategoriesDetailsHandler = new RetrieveOnSaleRootCategoriesDetailsHandler();
        invokerParamsOnSale
                .setUltaHandler(retrieveOnSaleRootCategoriesDetailsHandler);
        try {
            new ExecutionDelegator(invokerParamsOnSale);
        } catch (UltaException ultaException) {
            Logger.Log("<UltaHomeActivity><invokeRootCategoryDetails()><UltaException>>"
                    + ultaException);
        }
    }

    private Map<String, String> populateParametersForFetchByBrand() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-depth", "2");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("pageNumber", "" + pageNum);
        urlParams.put("howMany", HOW_MANY);
        urlParams.put("sortBy", sortSearch);
        urlParams.put("categoryDimId", categoryId);
        urlParams.put("brandDimIds", brandId);
        urlParams.put("promotionDimIds", promotionid);
        urlParams.put("minPrice", minPrice);
        urlParams.put("maxPrice", maxPrice);
        urlParams.put("brandId", selectedBrandId);
        urlParams.put("colorDimId", colorId);
        return urlParams;
    }

    private void invokeFetchProductsForRootCategory() {
        InvokerParams<RootCategoryBean> invokerParamsOnSale = new InvokerParams<RootCategoryBean>();
        invokerParamsOnSale
                .setServiceToInvoke(WebserviceConstants.FETCH_PRODUCTS_FOR_LEAF_CATEGORY);
        invokerParamsOnSale.setHttpMethod(HttpMethod.POST);
        invokerParamsOnSale.setHttpProtocol(HttpProtocol.http);
        invokerParamsOnSale
                .setUrlParameters(populateParametersForFetchProductsForRootCategory());
        invokerParamsOnSale.setUltaBeanClazz(RootCategoryBean.class);
        RetrieveRootCategoriesDetailsHandler retrieveRootCategoriesDetailsHandler = new RetrieveRootCategoriesDetailsHandler();
        invokerParamsOnSale
                .setUltaHandler(retrieveRootCategoriesDetailsHandler);
        try {
            new ExecutionDelegator(invokerParamsOnSale);
        } catch (UltaException ultaException) {
            Logger.Log("<UltaHomeActivity><invokeRootCategoryDetails()><UltaException>>"
                    + ultaException);
        }
    }

    private Map<String, String> populateParametersForFetchProductsForRootCategory() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-depth", "2");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("pageNumber", "" + pageNum);
        urlParams.put("howMany", "12");
        urlParams.put("sortBy", sortSearch);
        if (categoryId == "") {
            urlParams.put("N", getIntent().getExtras()
                    .get("catagoryIdFromRoot").toString());
        } else {
            urlParams.put("N", categoryId);
        }
        urlParams.put("brandDimIds", brandId);
        urlParams.put("promotionDimIds", promotionid);
        urlParams.put("categoryDimId", "");
        urlParams.put("minPrice", minPrice);
        urlParams.put("maxPrice", maxPrice);
        urlParams.put("otherIds", otherIds);
        return urlParams;
    }

    private void setBadgeImage(ImageView imgBadgeImage, String badgeName) {
        if (badgeName != null && !badgeName.isEmpty()) {
            imgBadgeImage.setVisibility(View.VISIBLE);
            if (badgeName.equals("isNew_badge")) {
                imgBadgeImage.setImageResource(R.drawable.badge_whats_new);
            } else if (badgeName.equals("gwp_badge")) {
                imgBadgeImage.setImageResource(R.drawable.badge_free_gift);
            } else if (badgeName.equals("onSale_badge")) {
                imgBadgeImage.setImageResource(R.drawable.badge_sale);
            } else if (badgeName.equals("onlineOnly_badge")) {
                imgBadgeImage.setImageResource(R.drawable.online_badge);
            } else if (badgeName.equals("ultaExclusive_badge")) {
                imgBadgeImage.setImageResource(R.drawable.badge_ulta_exclusive);
            } else if (badgeName.equals("ultaPick_badge")) {
                imgBadgeImage.setImageResource(R.drawable.badge_ulta_pick);
            } else if (badgeName.equals("fanFavorite_badge")) {
                imgBadgeImage.setImageResource(R.drawable.badge_fan_fave);
            } else if (badgeName.equals("inStoreOnly_badge")) {
                imgBadgeImage.setImageResource(R.drawable.badge_instore);
            } else if (badgeName.equals("comingSoon_badge")) {
                imgBadgeImage.setImageResource(R.drawable.badge_coming_soon);
            }

        } else {
            imgBadgeImage.setVisibility(View.GONE);
        }
    }

    private void setOtherIds() {
        otherIds = "";
        if (!benefitId.equals("")) {
            if (!otherIds.equals("")) {
                otherIds += "," + benefitId;
            } else
                otherIds = benefitId;
        }
        if (!colorId.equals("")) {
            if (!otherIds.equals("")) {
                otherIds += "," + colorId;
            } else
                otherIds = colorId;

        }
        if (!concernId.equals("")) {
            if (!otherIds.equals("")) {
                otherIds += "," + concernId;
            } else
                otherIds = concernId;
        }
        if (!coverageId.equals("")) {
            if (!otherIds.equals("")) {
                otherIds += "," + coverageId;
            } else
                otherIds = coverageId;
        }
        if (!finishId.equals("")) {
            if (!otherIds.equals("")) {
                otherIds += "," + finishId;
            } else
                otherIds = finishId;
        }
        if (!formId.equals("")) {
            if (!otherIds.equals("")) {
                otherIds += "," + formId;
            } else
                otherIds = formId;
        }
        if (!ingredientId.equals("")) {
            if (!otherIds.equals("")) {
                otherIds += "," + ingredientId;
            } else
                otherIds = ingredientId;
        }
        if (!tyepeId.equals("")) {
            if (!otherIds.equals("")) {
                otherIds += "," + tyepeId;
            } else
                otherIds = tyepeId;
        }
        if (!purposeId.equals("")) {
            if (!otherIds.equals("")) {
                otherIds += "," + purposeId;
            } else
                otherIds = purposeId;
        }
        if (!scentId.equals("")) {
            if (!otherIds.equals("")) {
                otherIds += "," + scentId;
            } else
                otherIds = scentId;
        }
        if (!skinTypeId.equals("")) {
            if (!otherIds.equals("")) {
                otherIds += "," + skinTypeId;
            } else
                otherIds = skinTypeId;
        }
        if (!spfId.equals("")) {
            if (!otherIds.equals("")) {
                otherIds += "," + spfId;
            } else
                otherIds = spfId;
        }
        if (!treatmentId.equals("")) {
            if (!otherIds.equals("")) {
                otherIds += "," + treatmentId;
            } else
                otherIds = treatmentId;
        }
    }

    private RootCategoryBean productsOfRoot;

    public class RetrieveRootCategoriesDetailsHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg
         *            the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<RetrieveRootCategoriesDetailsHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            loadingDialog.setVisibility(View.GONE);
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (null != getErrorMessage()) {
                try {
                    notifyUser(Utility.formatDisplayError(getErrorMessage()),
                            UltaProductListActivity.this);
                    setError(UltaProductListActivity.this, getErrorMessage());
                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
            } else {
                Logger.Log("<SearchHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                productsOfRoot = (RootCategoryBean) getResponseBean();
                if (null != productsOfRoot) {
                    filterTV.setEnabled(true);
                    linearLayout3.setVisibility(View.VISIBLE);
                    sortFilterStatusLayout.setVisibility(View.VISIBLE);
                    count = productsOfRoot.getTotalNoOfProducts();
                    if (count == 0) {
						final Dialog alert = showAlertDialog(
                                UltaProductListActivity.this, "Sorry",
                                "No more products found", "OK", "");
                        alert.show();
                        alert.setCancelable(false);
                        mDisagreeButton.setVisibility(View.GONE);

                        mAgreeButton.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                                finish();
                            }
                        });

                    } else {
                        if (!isSearchFilter) {
                            if (null != altText) {
                                tvItemNumber.setText("" + count + " Products");
                            } else {
                                tvItemNumber.setText("" + count + " Products");
                            }
                        }
                        listOfProductsInListBean = new ArrayList<ProductsInListBean>();
                        List<RootCategorySearchBean> products = new ArrayList<RootCategorySearchBean>();
                        products = productsOfRoot.getSearchResults();
                        tvSortFilterStatus.setVisibility(View.VISIBLE);
                        for (int loop = 0; loop < products.size(); loop++) {
                            ProductsInListBean productinlistbean = new ProductsInListBean();
                            productinlistbean.setBrandName(products.get(loop)
                                    .getBrandName());
                            productinlistbean.setDisplayName(products.get(loop)
                                    .getDisplayName());
                            productinlistbean.setId(products.get(loop).getId());
                            productinlistbean.setIsGWP(products.get(loop)
                                    .getIsGWP());
                            productinlistbean.setListPriceFrom(products.get(
                                    loop).getListPriceFrom());
                            productinlistbean.setListPriceTo(products.get(loop)
                                    .getListPriceTo());
                            productinlistbean.setSalePriceFrom(products.get(
                                    loop).getSalePriceFrom());
                            productinlistbean.setSalePriceTo(products.get(loop)
                                    .getSalePriceTo());
                            productinlistbean.setSmallImageUrl(products.get(
                                    loop).getSmallImageUrl());
                            productinlistbean.setRating(products.get(loop)
                                    .getRating());
                            productinlistbean.setReviews(products.get(loop)
                                    .getReviews());
                            if (products.get(loop).getGiftUrl() != null) {
                                productinlistbean.setGiftUrl(products.get(loop)
                                        .getGiftUrl());
                            }
                            if (products.get(loop).getOfferType() != null) {
                                productinlistbean.setOfferType(products.get(
                                        loop).getOfferType());
                            }
                            if (products.get(loop).getOfferDesc() != null) {
                                productinlistbean.setOfferDesc(products.get(
                                        loop).getOfferDesc());
                            }
                            if (products.get(loop).getBadgeName() != null) {
                                productinlistbean.setBadgeName(products.get(
                                        loop).getBadgeName());
                            }
                            if (products.get(loop).getHasSkusOnSale() != null
                                    && products.get(loop).getHasSkusOnSale()
                                    .equals("true")) {
                                productinlistbean.setHasSkusOnSale(true);
                            } else {
                                productinlistbean.setHasSkusOnSale(false);
                            }
                            listOfProductsInListBean.add(productinlistbean);
                            Logger.Log("<<<<<<<<<<<<<<<<<<<<<<<<<<list>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
                                    + listOfProductsInListBean.get(loop)
                                    .getBrandName());
                        }
                        if (listOfProductsInListBean != null) {
                            listWithAllProducts
                                    .addAll(listOfProductsInListBean);
                        }
                        if (null == productListAdapter) {
                            fnSetAdapters();
                        } else {
                            productListAdapter.notifyDataSetChanged();
                        }
                        if (productsOfRoot.getFacetListingForLeaf() != null) {
                            benefitFacets = productsOfRoot
                                    .getFacetListingForLeaf()
                                    .getBenefitFacets();
                            brandFacets = productsOfRoot
                                    .getFacetListingForLeaf().getBrandFacets();
                            colorFacets = productsOfRoot
                                    .getFacetListingForLeaf().getColorFacets();
                            concernsFacets = productsOfRoot
                                    .getFacetListingForLeaf()
                                    .getConcernsFacets();
                            coverageFacets = productsOfRoot
                                    .getFacetListingForLeaf()
                                    .getCoverageFacets();
                            finishFacets = productsOfRoot
                                    .getFacetListingForLeaf().getFinishFacets();
                            formFacets = productsOfRoot
                                    .getFacetListingForLeaf().getFormFacets();
                            ingredientsFacets = productsOfRoot
                                    .getFacetListingForLeaf()
                                    .getIngredientsFacets();
                            priceFacets = productsOfRoot
                                    .getFacetListingForLeaf().getPriceFacets();
                            promotionFacets = productsOfRoot
                                    .getFacetListingForLeaf()
                                    .getPromotionFacets();
                            purposeFacets = productsOfRoot
                                    .getFacetListingForLeaf()
                                    .getPurposeFacets();
                            scentFacets = productsOfRoot
                                    .getFacetListingForLeaf().getScentFacets();
                            skinTypeFacets = productsOfRoot
                                    .getFacetListingForLeaf()
                                    .getSkinTypeFacets();
                            spfFacets = productsOfRoot.getFacetListingForLeaf()
                                    .getSpfFacets();
                            treatmentFacets = productsOfRoot
                                    .getFacetListingForLeaf()
                                    .getTreatmentFacets();
                            typeFacets = productsOfRoot
                                    .getFacetListingForLeaf().getTypeFacets();
                        }
                    }

                }
            }
        }
    }

    @Override
    public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {

        if (isSuccess) {
            invokeNewArrivalDetails();
        } else {
            pd.dismiss();
            loadingDialog.setVisibility(View.GONE);
        }
    }

    @Override
    public void onImageDownload() {
        // UltaProductDetailsActivity.launch(UltaProductListActivity.this,
        // viewToBePassed.findViewById(R.id.imgItemImage),
        // urlForProductDetailsTransition,
        // listWithAllProducts.get(positionToBePassed).getId());

    }

    private void setCustomButton(Button button) {
        if (button != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            button.setLayoutParams(params);
            button.setTextColor(getResources().getColor(R.color.primaryColor));
        }
    }
}
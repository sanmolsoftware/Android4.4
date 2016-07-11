/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity;

import com.ulta.core.bean.UltaBean;
import com.ulta.core.bean.search.FacetGroupSearchBean;
import com.ulta.core.bean.search.FacetSearchBean;
import com.ulta.core.bean.search.ProductSearchBean;
import com.ulta.core.bean.search.ProductsSearchedBean;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arpan
 */
public class SearchSaxXMLHandler extends DefaultHandler
{
	private ProductsSearchedBean productsSearchedTemp = null;
	private List<FacetGroupSearchBean> facetGroupL;
	private FacetGroupSearchBean facetGroupTemp = null;
	private List<ProductSearchBean> productL = new ArrayList<ProductSearchBean>();
	private ProductSearchBean productTemp = null;
	private List<FacetSearchBean> facetL;
	private FacetSearchBean facetTemp = null;
	String elementValue = null;
	Boolean elementOn = false;

	private Class<UltaBean> parsedPopulationBeanClazz;

	public SearchSaxXMLHandler(Class<UltaBean> beanToPopulate) {
		this.parsedPopulationBeanClazz = beanToPopulate;
	}

	/**
	 * methos to return the products back to the parser
	 **/

	public ProductsSearchedBean getProducts() {
		return productsSearchedTemp;
	}

	/**
	 * This will be called when the tags of the XML starts.
	 **/
	@Override
	public void startElement(String uri, String s1, String elementName,
			Attributes attributes) throws SAXException {

		elementOn = true;
		if (elementName.equalsIgnoreCase("search_metadata")) {
			productsSearchedTemp = new ProductsSearchedBean();
		}
		else if (elementName.equalsIgnoreCase("result_count")) {
			productsSearchedTemp.setTotalResultCount(Integer
					.parseInt(attributes.getValue("total")));
			productsSearchedTemp.setThisPage(Integer.parseInt(attributes
					.getValue("this_page")));
		}
		else if (elementName.equalsIgnoreCase("pages")) {
			productsSearchedTemp.setPageTotal(Integer.parseInt(attributes
					.getValue("total")));
		}
		else if (elementName.equalsIgnoreCase("result")) {
			productTemp = new ProductSearchBean();
		}
		if (elementName.equalsIgnoreCase("facets")) {
			facetGroupL = new ArrayList<FacetGroupSearchBean>();
		}
		if (elementName.equalsIgnoreCase("facet_group")) {
			facetGroupTemp = new FacetGroupSearchBean();
			facetGroupTemp.setId(attributes.getValue("id"));
			facetGroupTemp.setName(attributes.getValue("name"));
			facetL = new ArrayList<FacetSearchBean>();
		}
		if (elementName.equalsIgnoreCase("facet")) {
			facetTemp = new FacetSearchBean();
			facetTemp.setId(attributes.getValue("id"));
			facetTemp.setName(attributes.getValue("name"));
			facetTemp.setCount(Integer.parseInt(attributes.getValue("count")));
		}
	}

	/**
	 * This will be called when the tags of the XML end.
	 **/
	@Override
	public void endElement(String uri, String s1, String element)
			throws SAXException {

		elementOn = false;

		if (element.equals("result")) {
			productL.add(productTemp);
		}
		else if (element.equalsIgnoreCase("url"))
			productTemp.setUrl(elementValue);
		else if (element.equalsIgnoreCase("title"))
			productTemp.setTitle(elementValue);
		else if (element.equalsIgnoreCase("text"))
			productTemp.setText(elementValue);
		else if (element.equalsIgnoreCase("image_url"))
			productTemp.setImage_url(elementValue);
		else if (element.equalsIgnoreCase("price"))
			productTemp.setPrice(Double.parseDouble(elementValue));
		else if (element.equalsIgnoreCase("review_rating"))
			productTemp.setReview_rating(Float.parseFloat(elementValue));
		else if (element.equalsIgnoreCase("review_count"))
			productTemp.setReview_count(Integer.parseInt(elementValue));
		else if (element.equalsIgnoreCase("product_id"))
			productTemp.setProduct_id(elementValue);
		else if (element.equalsIgnoreCase("sku_id"))
			productTemp.setSku_id(Double.parseDouble(elementValue));
		else if (element.equalsIgnoreCase("ad_bug"))
			productTemp.setAd_bug(elementValue);
		else if (element.equalsIgnoreCase("brand"))
			productTemp.setBrand(elementValue);
		else if (element.equalsIgnoreCase("has_variant"))
			productTemp.setHas_variant(Integer.parseInt(elementValue));
		else if (element.equalsIgnoreCase("url_title"))
			productTemp.setUrl_title(elementValue);
		else if (element.equalsIgnoreCase("rank"))
			productTemp.setRank(Integer.parseInt(elementValue));
		else if (element.equalsIgnoreCase("results"))
			productsSearchedTemp.setProductList(productL);

		else if (element.equals("facet")) {
			facetL.add(facetTemp);
		}
		else if (element.equals("facet_group")) {
			facetGroupTemp.setFacetList(facetL);
			facetGroupL.add(facetGroupTemp);
		}
		else if (element.equals("facets")) {
			productsSearchedTemp.setFacetGroupList(facetGroupL);
		}

	}

	/**
	 * This is called to get the tags value
	 **/
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (elementOn) {
			elementValue = new String(ch, start, length);
			elementOn = false;
		}
	}
}

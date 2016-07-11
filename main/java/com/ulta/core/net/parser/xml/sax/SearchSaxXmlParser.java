/**
 * 
 */
package com.ulta.core.net.parser.xml.sax;

import com.ulta.core.activity.SearchSaxXMLHandler;
import com.ulta.core.bean.UltaBean;
import com.ulta.core.bean.search.ProductsSearchedBean;
import com.ulta.core.net.parser.xml.UltaXmlParserException;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.log.Logger;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author viva
 *
 */
public class SearchSaxXmlParser {

	private ProductsSearchedBean productsSearchedTemp=null;
	private Class<UltaBean> parsedPopulationBeanClazz;
	
	public SearchSaxXmlParser(Class<UltaBean> beanToPopulate){
		this.parsedPopulationBeanClazz = beanToPopulate;
	}
	
	/**
	 * 
	 * @param parsedResult
	 * @return
	 */
	public ProductsSearchedBean parseSearchResults(InputStream responseStreamToParse)throws UltaXmlParserException{
			try {
				/**
				 * Create a new instance of the SAX parser
				 **/
				SAXParserFactory saxPF = SAXParserFactory.newInstance();
				SAXParser saxP = saxPF.newSAXParser();
				XMLReader xmlR = saxP.getXMLReader();
				SearchSaxXMLHandler myXMLHandler = new SearchSaxXMLHandler(this.parsedPopulationBeanClazz);
				xmlR.setContentHandler(myXMLHandler);
				SearchSaxXMLHandler searchSaxXMLHandler=(SearchSaxXMLHandler) xmlR.getContentHandler();
				xmlR.parse(new InputSource(responseStreamToParse)); 
				productsSearchedTemp=searchSaxXMLHandler.getProducts();
			}  catch (ParserConfigurationException e) {  
				Logger.Log("ParserConfig error");
				throw new UltaXmlParserException("PARSING_ERROR","Parser Configuration Error");
			} catch (SAXException e) {  
				Logger.Log("SAXException : xml not well formed");
				throw new UltaXmlParserException("PARSING_ERROR","XML not well formed");
			} catch (IOException e) {  
				Logger.Log("IO error");
				throw new UltaXmlParserException("PARSING_ERROR",UltaException.NETWORK_ERROR_IO_EXCEPTION);
			}  
			return productsSearchedTemp;
	 }  
	  
}

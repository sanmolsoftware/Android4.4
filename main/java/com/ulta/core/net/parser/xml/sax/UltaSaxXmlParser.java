/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net.parser.xml.sax;

import com.ulta.core.bean.UltaBean;
import com.ulta.core.conf.types.XmlParserIdentifier;
import com.ulta.core.net.parser.xml.UltaXmlParser;
import com.ulta.core.net.parser.xml.UltaXmlParserException;

import java.io.InputStream;

/**
 * @author viva
 * 
 */
public class UltaSaxXmlParser extends UltaXmlParser{

	private XmlParserIdentifier xmlParserIdentifier;

	private Class<UltaBean> parsedPopulationBeanClazz;
	/**
	 * 
	 * Empty constructor
	 */
	public UltaSaxXmlParser(){
		
	}

	/**
	 * 
	 * @param invokerParams
	 */
	public UltaSaxXmlParser(XmlParserIdentifier xmlParserId, Class<UltaBean> beanToPopulate) {
		this.xmlParserIdentifier = xmlParserId;
		this.parsedPopulationBeanClazz = beanToPopulate;
		
	}

	/**
	 * @param responseStream
	 * @return UltaBean
	 */

	public UltaBean saxXmlParsedBean(InputStream responseStream)
			throws UltaXmlParserException {
		UltaBean xmlParsedPopulatedBean = null;
		switch (this.xmlParserIdentifier) {
		case SearchSaxXmlParser:
			xmlParsedPopulatedBean = new SearchSaxXmlParser(
					this.parsedPopulationBeanClazz)
					.parseSearchResults(responseStream);
			break;
		default:
			throw new UltaXmlParserException("INVALID_PARSER",
					"Please provide a valid XML SAX Parser Identifier");
		}

		return xmlParsedPopulatedBean;
	}
}

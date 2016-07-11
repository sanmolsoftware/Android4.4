/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net.parser.xml.pull;

import com.ulta.core.bean.UltaBean;
import com.ulta.core.conf.types.XmlParserIdentifier;
import com.ulta.core.net.parser.xml.UltaXmlParser;
import com.ulta.core.net.parser.xml.UltaXmlParserException;

import java.io.InputStream;

/**
 * @author Vijish_Varghese
 * 
 */
public class UltaPullXmlParser extends UltaXmlParser {

	/*private XmlParserIdentifier xmlParserIdentifier;*/

	/*private Class<UltaBean> parsedPopulationBeanClazz;*/

	/**
	 * 
	 * @param invokerParams
	 */
	public UltaPullXmlParser(XmlParserIdentifier XmlParserId, Class<UltaBean> beanToPopulate) {
		/*this.xmlParserIdentifier = XmlParserId;*/
	/*	this.parsedPopulationBeanClazz = beanToPopulate;*/
	}

	/**
	 * @param responseStream
	 * @return
	 */
	public UltaBean pullXmlParsedBean(InputStream responseStream) throws UltaXmlParserException {
		UltaBean xmlParsedPopulatedBean = null;

		return xmlParsedPopulatedBean;
	}
}

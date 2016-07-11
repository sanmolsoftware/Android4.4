/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net.parser.xml.dom;

import com.ulta.core.bean.UltaBean;
import com.ulta.core.conf.types.XmlParserIdentifier;
import com.ulta.core.net.parser.xml.UltaXmlParser;
import com.ulta.core.net.parser.xml.UltaXmlParserException;

import java.io.InputStream;

/**
 * @author viva
 * 
 */
public class UltaDomXmlParser extends UltaXmlParser{

	/*private XmlParserIdentifier xmlParserIdentifier;

	private Class<UltaBean> parsedPopulationBeanClazz;*/

	/**
	 * 
	 * @param invokerParams
	 */
	public UltaDomXmlParser(XmlParserIdentifier xmlParserId, Class<UltaBean> beanToPopulate) {
		/*this.xmlParserIdentifier = xmlParserId;
		this.parsedPopulationBeanClazz = beanToPopulate;*/
	}

	/**
	 * @param responseStream
	 * @return UltaBean
	 */
	public UltaBean domXmlParsedBean(InputStream responseStream) throws UltaXmlParserException {
		UltaBean xmlParsedPopulatedBean = null;
		return xmlParsedPopulatedBean;
	}
}

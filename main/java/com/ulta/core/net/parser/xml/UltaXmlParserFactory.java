/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net.parser.xml;

import com.ulta.core.bean.UltaBean;
import com.ulta.core.conf.types.ResponseParserFormat;
import com.ulta.core.conf.types.XmlParserIdentifier;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.net.parser.xml.dom.UltaDomXmlParser;
import com.ulta.core.net.parser.xml.pull.UltaPullXmlParser;
import com.ulta.core.net.parser.xml.sax.UltaSaxXmlParser;
import com.ulta.core.util.log.Logger;

import java.io.InputStream;


/**
 * @author viva
 *
 */
public class UltaXmlParserFactory {
	
	private Class<UltaBean> parsedPopulationBeanClazz;
	
	private ResponseParserFormat xmlParserFormat;
	
	private XmlParserIdentifier xmlParserIdentifier;
	
	/**
	 * 
	 * @param invokerParams
	 * @param responseStringToParse
	 */
	public UltaXmlParserFactory(InvokerParams<UltaBean> invokerParams){
		this.parsedPopulationBeanClazz = invokerParams.getUltaBeanClazz(); 
		this.xmlParserFormat = invokerParams.getResponseParserFormat();
		this.xmlParserIdentifier = invokerParams.getCustomXMLParser();
	}
	/**
	 * 
	 * @return
	 * throws UltaXmlParserException
	 */
	public UltaBean getParsedPopulatedBean(InputStream responseStream) throws UltaXmlParserException {
		UltaBean populatedParsedBean = null;
		switch (this.xmlParserFormat) {
		case DOMXML:
			populatedParsedBean = new UltaDomXmlParser(this.xmlParserIdentifier, this.parsedPopulationBeanClazz).domXmlParsedBean(responseStream);
			break;
		case SAXXML:
			Logger.Log("<UltaXmlParserFactory><getParsedPopulatedBean()><SAXXML><ENTRY>");
			populatedParsedBean = new UltaSaxXmlParser(this.xmlParserIdentifier, this.parsedPopulationBeanClazz).saxXmlParsedBean(responseStream);
			if(null!=populatedParsedBean)
			{
				Logger.Log("success1");
			}
			
			Logger.Log("<UltaXmlParserFactory><getParsedPopulatedBean()><SAXXML><RETURN>");
			break;
		case PULLXML:
			populatedParsedBean = new UltaPullXmlParser(this.xmlParserIdentifier, this.parsedPopulationBeanClazz).pullXmlParsedBean(responseStream);
			break;
		default:
			break;
		}
		return populatedParsedBean;
	}
}

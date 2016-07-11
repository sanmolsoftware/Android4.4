/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import static com.ulta.core.conf.UltaConstants.BEAN_LEGIBILITY_ENABLED;



/**
 * The Class UltaBean.
 *
 * @author viva
 */
public class UltaBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7270728654276531686L;

	/** The error infos. */
	@SerializedName("formExceptions")
	private List<String> errorInfos;
	
	/**
	 * Use for simple status only cases for handling of responses.
	 */
	private String responseStatus;
	
	
	
	/**
	 * Gets the use for simple status only cases for handling of responses.
	 *
	 * @return the responseStatus
	 */
	public String getResponseStatus() {
		return responseStatus;
	}

	


	/**
	 * Gets the error infos.
	 *
	 * @return the errorInfos
	 */
	public List<String> getErrorInfos() {
		return errorInfos;
	}




	/**
	 * Sets the error infos.
	 *
	 * @param errorInfos the errorInfos to set
	 */
	public void setErrorInfos(List<String> errorInfos) {
		this.errorInfos = errorInfos;
	}




	/**
	 * Sets the use for simple status only cases for handling of responses.
	 *
	 * @param responseStatus the responseStatus to set
	 */
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}


	/**
	 * Method to display the information of Bean in legible format.
	 *
	 * @return the string
	 */
	public String toString() {
		
		if(BEAN_LEGIBILITY_ENABLED){
			try {
	
				final StringBuilder builder = new StringBuilder(" {\t");
				final Method[] methods = getClass().getMethods();
				for (Method method : methods) {
					if ((method.getName().startsWith("is")
							|| method.getName().startsWith("get")) && (method.getParameterTypes() == null || method.getParameterTypes().length == 0)) {
						final Object object = method.invoke(this);
						if (object instanceof Collection) {
							builder.append("\n\t").append(method.getName())
									.append("=").append("[");
							final Collection<?> collection = (Collection<?>) object;
								for (Object item : collection) {
									builder.append(item).append(",");
								}
							builder.append("]");
						} else {
							builder.append("\n\t").append(method.getName()).append("=").append(object).append(",");
						}
					}
				}
				builder.append("\n  }");
				return builder.toString();
			} catch (Exception e) {
				return "Not Legible !";
			}
		}else {
			return super.toString();
		}
	}
}

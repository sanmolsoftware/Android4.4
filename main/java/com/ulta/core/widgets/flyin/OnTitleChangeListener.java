/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.widgets.flyin;



/**
 * The listener interface for receiving onTitleChange events.
 * The class that is interested in processing a onTitleChange
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addOnTitleChangeListener<code> method. When
 * the onTitleChange event occurs, that object's appropriate
 * method is invoked.
 *
 * @see OnTitleChangeEvent
 */
public interface OnTitleChangeListener {
	
	/**
	 * On title changed.
	 */
	public void onTitleChanged();
}

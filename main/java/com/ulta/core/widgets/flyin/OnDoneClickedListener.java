/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.widgets.flyin;



/**
 * The listener interface for receiving onDoneClicked events.
 * The class that is interested in processing a onDoneClicked
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addOnDoneClickedListener<code> method. When
 * the onDoneClicked event occurs, that object's appropriate
 * method is invoked.
 *
 * @see OnDoneClickedEvent
 */
public interface OnDoneClickedListener {
	 
 	/**
 	 * On done clicked.
 	 */
 	public void onDoneClicked();
}

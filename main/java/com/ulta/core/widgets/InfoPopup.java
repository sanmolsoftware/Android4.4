package com.ulta.core.widgets;

import android.app.Service;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.util.caching.UltaDataCache;

public class InfoPopup extends PopupWindow {

/*	private String message;*/
/*	private String title;*/
/*	private String strOkButton;*/
	private String strEditButton;
  
	private TextView txtMessage;
	private TextView txtTitle;
	private Button btnOk;
	private Button btnEdit;
/*	private LinearLayout linearLayout1;*/
	
 LayoutInflater inflater;
 Context context;
 ViewGroup rootView;
	private WindowManager mWindowManager;
 
	/** The root width. */
	private int rootWidth;
	
	/** The anchor width. */
	int anchorWidth;
	
	/** The anchor height. */
	int anchorHeight;


OnEditListener editListener;
public void setEditListener(OnEditListener editListener) {
	this.editListener = editListener;
}
public OnEditListener getEditListener() {
	return editListener;
}
	public InfoPopup(Context context,String message, String title, String strOkButton,String target) {
		super(context);
		mWindowManager = (WindowManager) context
		.getSystemService(Context.WINDOW_SERVICE);
		/*
		this.message = message;*/
	/*	this.title = title;*/
	/*	this.strOkButton = strOkButton;*/
		this.strEditButton = target;
		this.context=context;
		init();
		setView(message,title,target);
	}
	
	
	 private void setView(String message,String title,String target) {
		setMessage(message);
		setTitle(title);
		this.strEditButton = target;
		btnEdit.setVisibility(View.VISIBLE);
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
						dismiss();
			}
		});
		btnEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
						//UltaDataCache.getDataCacheInstance().setMovingBackInChekout(true);
				UltaDataCache.getDataCacheInstance().setMoveBackTo(strEditButton);
				editListener.onEditClicked();
				dismiss();
			}
		});
		
	}

   public interface OnEditListener{
	   public void onEditClicked();
   }


	private void setView(String message,String title) {
		setMessage(message);
		setTitle(title);
		btnEdit.setVisibility(View.GONE);
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
						dismiss();
			}
		});
		
	}


	public InfoPopup(Context context,String message, String title, String strOkButton) {
		super(context);
		mWindowManager = (WindowManager) context
		.getSystemService(Context.WINDOW_SERVICE);
	/*	this.message = message;*/
		/*this.title = title;*/
		/*this.strOkButton = strOkButton;*/
		this.context=context;
		init();
		setView(message,title);
	}

	
	
	private void init() {
		inflater=(LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
	
		rootView=(ViewGroup) inflater.inflate(R.layout.info_popup, null);
		rootView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		rootView.setBackgroundDrawable(new BitmapDrawable());
		if(rootView!=null){
			setContentView(rootView);
		}
         txtMessage=(TextView)rootView.findViewById(R.id.txtMessage);
         txtTitle=(TextView)rootView.findViewById(R.id.txtTitle);
         btnOk=(Button)rootView.findViewById(R.id.buttonDone);
         btnEdit=(Button)rootView.findViewById(R.id.buttonEdit);
        /* linearLayout1=(LinearLayout)rootView.findViewById(R.id.linearLayout1);*/
	}

	public void setMessage(String message){
          txtMessage.setText(message);
	}
	
	public void setTitle(String title) {
		txtTitle.setText(title);
	}
	
	
	/**
	 * Show.
	 *
	 * @param anchor the anchor
	 */
	public void show(View anchor){
		if (rootView == null) {
			throw new IllegalStateException("setContentView was not called with a view to display.");
		}
		setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
		setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		setTouchable(true);
		setFocusable(true);
		setOutsideTouchable(true);
		//setContentView(rootView);

		int xPos, yPos, arrowPos;
		int[] location 		= new int[2];
		anchor.getLocationOnScreen(location);

		Rect anchorRect 	= new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1]          	                                                                               + anchor.getHeight());

		//mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		rootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int rootHeight 		= rootView.getMeasuredHeight();
		if (rootWidth == 0) {
			rootWidth		= rootView.getMeasuredWidth();
		}
		int screenWidth 	= mWindowManager.getDefaultDisplay().getWidth();
		int screenHeight	= mWindowManager.getDefaultDisplay().getHeight();

		//automatically get X coord of popup (top left)
		if ((anchorRect.left + rootWidth) > screenWidth) {
			xPos 		= anchorRect.left - (rootWidth-anchor.getWidth());			
			xPos 		= (xPos < 0) ? 0 : xPos;
			arrowPos 	= anchorRect.centerX()-xPos;
		} else {
			if (anchor.getWidth() > rootWidth) {
				xPos = anchorRect.centerX() - (rootWidth/2);
			} else {
				xPos = anchorRect.left;
			}
			arrowPos = anchorRect.centerX()-xPos;
		}

		int dyTop			= anchorRect.top;
		int dyBottom		= screenHeight - anchorRect.bottom;

		boolean onTop		= (dyTop > dyBottom) ? true : false;
		showAtLocation(anchor, Gravity.CENTER, 0, 0);
	}
	
}

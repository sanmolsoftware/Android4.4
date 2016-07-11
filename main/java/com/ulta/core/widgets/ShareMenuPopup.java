package com.ulta.core.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ulta.R;


public class ShareMenuPopup extends PopupWindow implements OnTouchListener{
	/** The on options clicked listener. */
	private OnOptionsClickedListener onOptionsClickedListener;

	/** The Constant ANIM_GROW_FROM_LEFT. */
	public static final int ANIM_GROW_FROM_LEFT = 1;

	/** The Constant ANIM_GROW_FROM_RIGHT. */
	public static final int ANIM_GROW_FROM_RIGHT = 2;

	/** The Constant ANIM_GROW_FROM_CENTER. */
	public static final int ANIM_GROW_FROM_CENTER = 3;

	/** The Constant ANIM_REFLECT. */
	public static final int ANIM_REFLECT = 4;

	/** The Constant ANIM_AUTO. */
	public static final int ANIM_AUTO = 5;

	/** The context. */
	private Context context;

	/** The inflater. */
	private LayoutInflater inflater;

	/** The anim style. */
	private int animStyle;

	/** The m window manager. */
	private WindowManager mWindowManager;

	/** The root view. */
	private View rootView;

	/** The arrow up. */
	private ImageView arrowUp;

	/** The arrow down. */
	private ImageView arrowDown;

	/** The main container. */
	private ViewGroup mainContainer;

	/** The ll view type. */
	private LinearLayout lytFB;

	/** The ll sort. */
	private LinearLayout lytTwitter;

	/** The ll filter. */
	private LinearLayout lytEmail;


	/** The root width. */
	private int rootWidth;

	/** The anchor width. */
	int anchorWidth;

	/** The anchor height. */
	int anchorHeight;

	private String text;

	/**
	 * Instantiates a new options pop up.
	 *
	 * @param context the context
	 * @param viewType the view type
	 */
	public ShareMenuPopup(Context context,String imageUrl, String text,String link,String productId) {
		super(context);
		this.context = context;
		this.text=text;
		
		mWindowManager = (WindowManager) context
		.getSystemService(Context.WINDOW_SERVICE);
		init();
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int idClicked = v.getId();
		switch (idClicked) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:

			break;
		}
		return false;
	}

	/**
	 * Inits the.
	 */
	private void init() {
		setTouchInterceptor(this);
		inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setRootViewId(R.layout.share_menu_popup);
		animStyle = ANIM_AUTO;
		// animStyle = ANIM_REFLECT;

	}

	/**
	 * Sets the root view id.
	 *
	 * @param id the new root view id
	 */
	@SuppressWarnings("deprecation")
	public void setRootViewId(int id) {
		rootView = (ViewGroup) inflater.inflate(id, null);
		mainContainer = (ViewGroup) rootView.findViewById(R.id.tracks);

		arrowDown = (ImageView) rootView.findViewById(R.id.arrow_down);
		arrowUp = (ImageView) rootView.findViewById(R.id.arrow_up);

		lytFB = (LinearLayout) rootView.findViewById(R.id.shareMenuFB);
		lytFB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		lytTwitter = (LinearLayout) rootView.findViewById(R.id.shareMenuTwitter);
		lytTwitter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		lytEmail = (LinearLayout) rootView.findViewById(R.id.shareMenuEmail);
		lytEmail.setOnClickListener(new OnClickListener() {
			String subject="From ulta";
			@Override
			public void onClick(View v) {
				final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				emailIntent.setType("plain/text"); 
				//emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{addresses}); 
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject); 
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text); 
				context.startActivity(Intent.createChooser(emailIntent, "Email")); 
			}
		});

		rootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		setContentView(rootView);
		setBackgroundDrawable(new BitmapDrawable());
	}

	/**
	 * Show.
	 *
	 * @param anchor the anchor
	 * @param anchorWidth the anchor width
	 * @param anchorHeight the anchor height
	 */
	public void show(final View anchor ,int anchorWidth,int anchorHeight){
		if (rootView == null) {
			throw new IllegalStateException("setContentView was not called with a view to display.");
		}
		final int[] location 		= new int[2];

		/*ViewTreeObserver vto = anchor.getViewTreeObserver();    
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
				@Override             
				public void onGlobalLayout() {   

					ViewTreeObserver obs = anchor.getViewTreeObserver();   
					anchor.getLocationOnScreen(location);
					anchorWidth=anchor.getWidth();
					anchorHeight=anchor.getHeight();
					obs.removeGlobalOnLayoutListener(this); 

				}        
			}); */


		setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
		setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		setTouchable(true);
		setFocusable(true);
		setOutsideTouchable(true);
		//setContentView(rootView);

		int xPos, yPos, arrowPos;
		anchor.getLocationOnScreen(location);
		Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] + anchor.getHeight());

		rootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int rootHeight 		= rootView.getMeasuredHeight();
		if (rootWidth == 0) {
			rootWidth		= rootView.getMeasuredWidth();
		}
		int screenWidth 	= mWindowManager.getDefaultDisplay().getWidth();
		int screenHeight	= mWindowManager.getDefaultDisplay().getHeight();

		if ((anchorRect.left + rootWidth) > screenWidth) {
			xPos 		= anchorRect.left - (rootWidth-anchorWidth);			
			xPos 		= (xPos < 0) ? 0 : xPos;
			arrowPos 	= anchorRect.centerX()-xPos;
		} else {
			if (anchorWidth > rootWidth) {
				xPos = anchorRect.centerX() - (rootWidth/2);
			} else {
				xPos = anchorRect.left;
			}
			arrowPos = anchorRect.centerX()-xPos;
		}

		int dyTop			= anchorRect.top;
		int dyBottom		= screenHeight - anchorRect.bottom;

		boolean onTop		= (dyTop > dyBottom) ? true : false;


		if (onTop) {
			if (rootHeight > dyTop) {
				yPos 			= 15;
				LayoutParams l 	=  mainContainer.getLayoutParams();
				l.height		= dyTop - anchorHeight;
			} else {
				yPos = anchorRect.top - rootHeight;
			}
		} else {
			yPos = anchorRect.bottom;
			if (rootHeight > dyBottom) { 

				LayoutParams l 	= mainContainer.getLayoutParams();
				l.height		= dyBottom;
			}
		}


		//setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
		arrowDown.setVisibility(View.INVISIBLE);
		arrowUp.setVisibility(View.INVISIBLE);
		//rootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 120));
		showAtLocation(anchor, Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);


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

		Rect anchorRect 	= new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] 
		                	                                                                               + anchor.getHeight());

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

		if (onTop) {
			if (rootHeight > dyTop) {
				yPos 			= 15;
				LayoutParams l 	= (LayoutParams) mainContainer.getLayoutParams();
				l.height		= dyTop - anchor.getHeight();
			} else {
				yPos = anchorRect.top - rootHeight;
			}
		} else {
			yPos = anchorRect.bottom;

			if (rootHeight > dyBottom) { 
				LayoutParams l 	= (LayoutParams) mainContainer.getLayoutParams();
				l.height		= dyBottom;
			}
		}

		showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowPos);

		setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);

		showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);




		/*	//setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
			arrowDown.setVisibility(View.INVISIBLE);
			arrowUp.setVisibility(View.INVISIBLE);
			//rootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 120));
			showAtLocation(anchor, Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);*/


	}

	/**
	 * Show arrow.
	 *
	 * @param whichArrow arrow type resource id
	 * @param requestedX distance from left screen
	 */
	private void showArrow(int whichArrow, int requestedX) {
		final View showArrow = (whichArrow == R.id.arrow_up) ? arrowUp
				: arrowDown;
		final View hideArrow = (whichArrow == R.id.arrow_up) ? arrowDown
				: arrowUp;

		final int arrowWidth = arrowUp.getMeasuredWidth();

		//showArrow.setVisibility(View.VISIBLE);

		ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) showArrow
		.getLayoutParams();

		param.leftMargin = requestedX - arrowWidth / 2;

		hideArrow.setVisibility(View.INVISIBLE);
	}

	/**
	 * Set animation style.
	 *
	 * @param screenWidth screen width
	 * @param requestedX distance from left edge
	 * @param onTop flag to indicate where the popup should be displayed. Set TRUE
	 * if displayed on top of anchor view and vice versa
	 */
	private void setAnimationStyle(int screenWidth, int requestedX,
			boolean onTop) {
		int arrowPos = requestedX - arrowUp.getMeasuredWidth() / 2;

		switch (animStyle) {
		case ANIM_GROW_FROM_LEFT:
			setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left
					: R.style.Animations_PopDownMenu_Left);
			break;

		case ANIM_GROW_FROM_RIGHT:
			setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right
					: R.style.Animations_PopDownMenu_Right);
			break;

		case ANIM_GROW_FROM_CENTER:
			setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center
					: R.style.Animations_PopDownMenu_Center);
			break;

		case ANIM_REFLECT:
			setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Reflect
					: R.style.Animations_PopDownMenu_Reflect);
			break;

		case ANIM_AUTO:
			if (arrowPos <= screenWidth / 4) {
				setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left
						: R.style.Animations_PopDownMenu_Left);
			} else if (arrowPos > screenWidth / 4
					&& arrowPos < 3 * (screenWidth / 4)) {
				setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center
						: R.style.Animations_PopDownMenu_Center);
			} else {
				setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right
						: R.style.Animations_PopDownMenu_Right);
			}

			break;
		}
	}

	/**
	 * Gets the on options clicked listener.
	 *
	 * @return the onRateClickedListener
	 */
	public OnOptionsClickedListener getOnOptionsClickedListener() {
		return onOptionsClickedListener;
	}

	/**
	 * Sets the on options clicked listener.
	 *
	 * @param onOptionsClickedListener the new on options clicked listener
	 */
	public void setOnOptionsClickedListener(OnOptionsClickedListener onOptionsClickedListener) {
		this.onOptionsClickedListener = onOptionsClickedListener;
	}



	/**
	 * The listener interface for receiving onOptionsClicked events.
	 * The class that is interested in processing a onOptionsClicked
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnOptionsClickedListener<code> method. When
	 * the onOptionsClicked event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnOptionsClickedEvent
	 */
	public interface OnOptionsClickedListener {

		/**
		 * On options clicked.
		 *
		 * @param click the click
		 */
		public void onOptionsClicked(int click);
	}
}

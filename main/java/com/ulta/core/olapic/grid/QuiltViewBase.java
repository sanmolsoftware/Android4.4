package com.ulta.core.olapic.grid;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.Toast;

import com.ulta.core.olapic.custom.GridLayoutListener;

import java.util.ArrayList;


public class QuiltViewBase extends GridLayout {
	
	public int[] size;
	public int columns;
	public int rows;
	public int view_width = -1;
	public int view_height = -1;
	public boolean isVertical = true;
	public ArrayList<View> views;
	
	public QuiltViewBase(Context context, boolean isVertical) {
		super(context);
		this.isVertical = isVertical;
		if(view_width == -1){
			DisplayMetrics metrics = this.getResources().getDisplayMetrics();
			int width = metrics.widthPixels;
			int height = metrics.heightPixels - 120;
			view_width = width - this.getPaddingLeft() - this.getPaddingRight();
			view_height = height - this.getPaddingTop() - this.getPaddingBottom();
		}
		views = new ArrayList<View>();
		setup();
	}
	
	public void setup(){
		if(isVertical){
			setupVertical();
		} else {
//			setupHorizontal();
			setupVertical();
		}
	}
	
	public void setupVertical(){
		size = getBaseSizeVertical();
		this.setColumnCount(columns);
//		this.setRowCount(-1);
		this.setOrientation(GridLayout.HORIZONTAL);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		this.setLayoutParams(params);
	}
	
//	public void setupHorizontal(){
//		size = getBaseSizeHorizontal();
//		this.setRowCount(rows);
////		this.setColumnCount(-1);
//		this.setOrientation(GridLayout.VERTICAL);
//		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//		this.setLayoutParams(params);
//	}
	
	public void addPatch(View view){
		
		int count = this.getChildCount();
		
		QuiltViewPatch child = QuiltViewPatch.init(count, columns);
		
		GridLayout.LayoutParams params = new GridLayout.LayoutParams();
		params.width = size[0]*child.width_ratio;
		params.height = size[1]*child.height_ratio;
		params.rowSpec = GridLayout.spec(Integer.MIN_VALUE, child.height_ratio);
		params.columnSpec = GridLayout.spec(Integer.MIN_VALUE, child.width_ratio);
		view.setLayoutParams(params);
		addView(view);
		views.add(view);
	}
	
	public void refresh(){
		this.removeAllViewsInLayout();
		setup();
		for(View view : views){
			addPatch(view);
		}
	}
	
//	public int[] getBaseSize(){
//		int[] size = new int[2];
//		
//		float width_height_ratio = (3.0f/4.0f);
//		
//		int base_width = getBaseWidth();
//		int base_height = (int) (base_width*width_height_ratio);
//		
//		size[0] = base_width; // width
//		size[1] = base_height; // height
//		return size;
//	}
	
	public int[] getBaseSizeVertical(){
		int[] size = new int[2];
		
		float width_height_ratio = (3.0f/4.0f);
		
		int base_width = getBaseWidth();
		int base_height = (int) (base_width*width_height_ratio);
		
		size[0] = base_width; // width
		size[1] = base_width; // height
		return size;
	}
	
//	public int[] getBaseSizeHorizontal(){
//		int[] size = new int[2];
//		
//		float width_height_ratio = (4.0f/3.0f);
//		
//		int base_height = getBaseHeight();
//		int base_width = (int) (base_height*width_height_ratio);
//		
//		size[0] = base_width; // width
//		size[1] = base_height; // height
//		return size;
//	}
	
	public int getBaseWidth(){
		columns = 3;
		return (view_width / columns);
	}
	
//	public int getBaseHeight(){
//		if(view_height < 350){
//			rows = 2;
//		} else if(view_height < 650){
//			rows = 3;
//		} else if(view_height < 1050){
//			rows = 4;
//		} else if(view_height < 1250){
//			rows = 2;
//		} else {
//			rows = 6;
//		}
//		return (view_height / rows);
//	}
	 
	 /*@Override 
	 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
	    int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
	    int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    
	    view_width = parentWidth;
        view_height = parentHeight;
        
        setup(isVertical);
	 }*/
	
	@Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
            super.onSizeChanged(xNew, yNew, xOld, yOld);
            view_width = xNew;
            view_height = yNew;
    }
	
	private GridLayoutListener gridListener = null;
    
    public void setGridLayoutListener(GridLayoutListener listener) {
        this.gridListener = listener;
    }

//    @Override
//    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        super.onScrollChanged(l, t, oldl, oldt);
//        if (gridListener != null) {
//        	gridListener.onScrollChanged(this, l, t, oldl, oldt);
//        }
//    }
    
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    	Toast.makeText(getContext(), "scrolled", Toast.LENGTH_SHORT).show();
    	super.onScrollChanged(l, t, oldl, oldt);
		if (gridListener != null) {
			gridListener.onScrollChanged(this, l, t, oldl, oldt);
		}
    }
    
//    @Override
//    public void computeScroll() {
//    	Toast.makeText(getContext(), "computed", Toast.LENGTH_SHORT).show();
//    	super.computeScroll();
//    }
}

package com.ulta.core.olapic.custom;

import com.ulta.core.olapic.grid.QuiltViewBase;

public interface GridLayoutListener {
	void onScrollChanged(QuiltViewBase scrollView, 
            int x, int y, int oldx, int oldy);
}
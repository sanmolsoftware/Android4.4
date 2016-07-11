package com.ulta.core.activity.product;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ulta.R;

import java.util.List;
import java.util.Map;

public class ExpandableListAdapter extends BaseExpandableListAdapter{
	
	private Activity context;
	private Map<String, List<String>> categoryCollection;
	private List<String> groupList;
	
	public ExpandableListAdapter(Activity context, List<String> groupList,
			Map<String, List<String>> categoryCollection) {
		this.context = context;
		this.categoryCollection = categoryCollection;
		this.groupList = groupList;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return categoryCollection.get(groupList.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final String childItems = (String) getChild(groupPosition, childPosition);
		LayoutInflater inflater = context.getLayoutInflater();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.root_list_image_new, null);
		}
		
		TextView item = (TextView) convertView.findViewById(R.id.root_menu_list_item_text);
		item.setTextAppearance(context,Typeface.NORMAL);
		TextView rootItemCountText = (TextView) convertView
				.findViewById(R.id.root_menu_list_count_text);
		rootItemCountText.setVisibility(View.GONE);
		ImageView delete = (ImageView) convertView.findViewById(R.id.imageViwe);
		delete.setVisibility(View.GONE);		
		item.setGravity(Gravity.CENTER_VERTICAL);
		item.setText("\t\t\t"+childItems);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return categoryCollection.get(groupList.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groupList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	private static final int[] EMPTY_STATE_SET = {};
    private static final int[] GROUP_EXPANDED_STATE_SET =
            {android.R.attr.state_expanded};
    private static final int[][] GROUP_STATE_SETS = {
         EMPTY_STATE_SET, // 0
         GROUP_EXPANDED_STATE_SET // 1
	};
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) { 
		String categoryName = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.root_list_image_new, null);
		}
		
		System.out.println("Is expanded : groupPosition : "+isExpanded +groupPosition);
		View ind = convertView.findViewById( R.id.imageViwe);
		TextView rootItemText = (TextView) convertView
				.findViewById(R.id.root_menu_list_item_text);
		if( ind != null ) {
			ImageView indicator = (ImageView)ind;
			if( getChildrenCount( groupPosition ) == 0 ) {
				indicator.setVisibility( View.INVISIBLE );				
				//rootItemText.setTypeface(null, Typeface.BOLD_ITALIC);
			} else {
				indicator.setVisibility( View.VISIBLE );
				
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					if (isExpanded) {
						indicator.setImageResource(R.drawable.shop_arrow_up);
					} else {
						indicator.setImageResource(R.drawable.shop_arrow_down);
					}
					rootItemText.setTypeface(null, Typeface.NORMAL);
				} else {
					int stateSetIndex = ( isExpanded ? 1 : 0) ;
					Drawable drawable = indicator.getDrawable();
					drawable.setState(GROUP_STATE_SETS[stateSetIndex]);	
					rootItemText.setTypeface(null, Typeface.NORMAL);
				}
				
			}
		} else {
			System.out.println("Ind is null");
		}
		TextView rootItemCountText = (TextView) convertView
				.findViewById(R.id.root_menu_list_count_text);
		rootItemCountText.setVisibility(View.GONE);
		rootItemText.setText(categoryName);
		rootItemText.setGravity(Gravity.CENTER_VERTICAL);
		

		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}

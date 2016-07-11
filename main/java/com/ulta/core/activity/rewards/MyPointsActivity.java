/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.rewards;

import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The Class MyPointsActivity.
 */
public class MyPointsActivity extends UltaBaseActivity
{

	/** The Constant ITEM_TITLE. */
	public final static String ITEM_TITLE = "title";

	/** The Constant ITEM_CAPTION. */
	public final static String ITEM_CAPTION = "caption";

	/** The Constant HEADER_LEVEL_ONE. */
	public final static int HEADER_LEVEL_ONE = 1;

	/** The Constant HEADER_LEVEL_TWO. */
	public final static int HEADER_LEVEL_TWO = 2;

	/**
	 * Creates the item.
	 * 
	 * @param title
	 *            the title
	 * @param caption
	 *            the caption
	 * @return the map
	 */
	public Map<String, ?> createItem(String title, String caption) {
		Map<String, String> item = new HashMap<String, String>();
		item.put(ITEM_TITLE, title);
		item.put(ITEM_CAPTION, caption);
		return item;
	}

	/** The list view. */
	private ListView listView;

	/** The main layout. */
/*	private LinearLayout mainLayout;*/

	/*
	 * (non-Javadoc)
	 * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_points);
		setTitle("Points");
		initViews();

		/*
		 * List<Map<String,?>> security = new LinkedList<Map<String,?>>();
		 * security.add(createItem("Remember passwords",
		 * "Save usernames and passwords for Web sites"));
		 * security.add(createItem("Clear passwords",
		 * "Save usernames and passwords for Web sites"));
		 * security.add(createItem("Show security warnings",
		 * "Show warning if there is a problem with a site's security"));
		 */

		// create our list and custom adapter
	/*	SeparatedListAdapter adapter = new SeparatedListAdapter(this,
				HEADER_LEVEL_ONE);*/

		SeparatedListAdapter adapter1 = new SeparatedListAdapter(this,
				HEADER_LEVEL_TWO);
		adapter1.addSection("Date1", new CustomAdapter());
		// SeparatedListAdapter adapter2 = new
		// SeparatedListAdapter(this,HEADER_LEVEL_TWO);
		adapter1.addSection("Date2", new CustomAdapter());
		// SeparatedListAdapter adapter3 = new
		// SeparatedListAdapter(this,HEADER_LEVEL_TWO);
		adapter1.addSection("Date3", new CustomAdapter());
		/*
		 * adapter.addSection("Main", adapter1); adapter.addSection("Main1",
		 * adapter1); adapter.addSection("Main2", adapter1);
		 */

		// ListView list = new ListView(this);
		listView.setAdapter(adapter1);
		// this.setContentView(list);

	}

	/**
	 * Inits the views.
	 */
	private void initViews() {
		/*mainLayout = (LinearLayout) findViewById(R.id.order_history_main);*/
		listView = (ListView) findViewById(R.id.order_history_list);
	}

	/**
	 * The Class CustomAdapter.
	 */
	public class CustomAdapter extends BaseAdapter
	{

		/*
		 * (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
				return 2;
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int position) {
				return null;
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
				return 0;
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			LinearLayout itemLayout = (LinearLayout) inflater.inflate(
					R.layout.my_points_item, null);
			/*
			 * LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			 * LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			 * itemLayout.setLayoutParams(params);
			 */
			((TextView) (itemLayout.findViewById(R.id.order_history_orderNo)))
					.setText("Item" + position);

			itemLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Toast.makeText(MyPointsActivity.this,
					// "clicked on"+position, Toast.LENGTH_LONG).show();

				}
			});
			return itemLayout;
		}

	}

	/**
	 * The Class Header.
	 */
	class Header
	{

		/** The header. */
		String header;

		/** The level. */
		int level;
	}

	/**
	 * The Class SeparatedListAdapter.
	 */
	public class SeparatedListAdapter extends BaseAdapter
	{

		/** The sections. */
		public final Map<String, Adapter> sections = new LinkedHashMap<String, Adapter>();

		/** The headers. */
		public final ArrayAdapter<String> headers;

		/** The Constant TYPE_SECTION_HEADER. */
		public final static int TYPE_SECTION_HEADER = 0;

		/**
		 * Instantiates a new separated list adapter.
		 * 
		 * @param context
		 *            the context
		 * @param level
		 *            the level
		 */
		public SeparatedListAdapter(Context context, int level) {

			headers = new ArrayAdapter<String>(context,
					R.layout.seperated_list_header);
		}

		/**
		 * Adds the section.
		 * 
		 * @param section
		 *            the section
		 * @param adapter
		 *            the adapter
		 */
		public void addSection(String section, Adapter adapter) {
			/*
			 * Header header=new Header(); header.header=section;
			 * header.level=level;
			 */
			this.headers.add(section);
			this.sections.put(section, adapter);
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		public Object getItem(int position) {
			for (Object section : this.sections.keySet()) {
				Adapter adapter = sections.get(section);
				int size = adapter.getCount() + 1;

				// check if position inside this section
				if (position == 0)
					return section;
				if (position < size)
					return adapter.getItem(position - 1);

				// otherwise jump into next section
				position -= size;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		public int getCount() {
			// total together all sections, plus one for each section header
			int total = 0;
			for (Adapter adapter : this.sections.values())
				total += adapter.getCount() + 1;
			return total;
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.BaseAdapter#getViewTypeCount()
		 */
		public int getViewTypeCount() {
			// assume that headers count as one, then total all sections
			int total = 1;
			for (Adapter adapter : this.sections.values())
				total += adapter.getViewTypeCount();
			return total;
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.BaseAdapter#getItemViewType(int)
		 */
		public int getItemViewType(int position) {
			int type = 1;
			for (Object section : this.sections.keySet()) {
				Adapter adapter = sections.get(section);
				int size = adapter.getCount() + 1;

				// check if position inside this section
				if (position == 0)
					return TYPE_SECTION_HEADER;
				if (position < size)
					return type + adapter.getItemViewType(position - 1);

				// otherwise jump into next section
				position -= size;
				type += adapter.getViewTypeCount();
			}
			return -1;
		}

		/**
		 * Are all items selectable.
		 * 
		 * @return true, if successful
		 */
		public boolean areAllItemsSelectable() {
			return false;
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.BaseAdapter#isEnabled(int)
		 */
		public boolean isEnabled(int position) {
			return (getItemViewType(position) != TYPE_SECTION_HEADER);
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int sectionnum = 0;
			for (Object section : this.sections.keySet()) {
				Adapter adapter = sections.get(section);
				int size = adapter.getCount() + 1;

				// check if position inside this section
				if (position == 0)
					return headers.getView(sectionnum, convertView, parent);
				if (position < size)
					return adapter.getView(position - 1, convertView, parent);

				// otherwise jump into next section
				position -= size;
				sectionnum++;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			return position;
		}

	}
}

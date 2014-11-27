package com.ojk.business;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ojk.R;
import com.ojk.entities.*;

// here's our beautiful adapter
public class ArrayAdapterItem extends ArrayAdapter<ObjectItemListView> {

	Context mContext;
	int layoutResourceId;
	ObjectItemListView data[] = null;

	public ArrayAdapterItem(Context mContext, int layoutResourceId,
			ObjectItemListView[] data) {

		super(mContext, layoutResourceId, data);

		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		/*
		 * The convertView argument is essentially a "ScrapView" as described is
		 * Lucas post
		 * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
		 * It will have a non-null value when ListView is asking you recycle the
		 * row layout. So, when convertView is not null, you should simply
		 * update its contents instead of inflating a new row layout.
		 */
		if (convertView == null) {
			// inflate the layout
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(layoutResourceId, parent, false);
		}

		// object item based on the position
		ObjectItemListView objectItem = data[position];

		// get the TextView and then set the text (item name) and tag (item ID)
		// values

		TextView textViewItem = (TextView) convertView
				.findViewById(R.id.TextViewMenu);
		
		textViewItem.setText(objectItem.itemName);
		
		textViewItem.setTag(objectItem.itemId);

		TextView textViewItemCount = (TextView) convertView
				.findViewById(R.id.TextViewCount);
		RelativeLayout relayoutcount = (RelativeLayout) convertView
				.findViewById(R.id.relayoutcount);
		if (textViewItemCount != null) {
			if (objectItem.anakCount != 0) {
				textViewItemCount.setText(objectItem.anakCount + "");
				textViewItemCount.setTag(objectItem.itemId);
			} else {
				textViewItemCount.setVisibility(TextView.INVISIBLE);
				relayoutcount.setVisibility(RelativeLayout.INVISIBLE);
			}
		}

		return convertView;
	}

}

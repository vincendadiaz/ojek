package com.ojk.business;

import com.ojk.R;
import com.ojk.entities.ObjectItemGridView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseAdapterItem extends BaseAdapter {
	private Context mContext;
	private final String[] judul;
	ObjectItemGridView[] data = null;

	public BaseAdapterItem(Context c, String[] judul) {
		mContext = c;
		this.judul = judul;
	}

	@Override
	public int getCount() {
		return judul.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View grid;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			grid = new View(mContext);
			grid = inflater.inflate(R.layout.gridviewadapter, null);
			ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
			TextView textView = (TextView) grid.findViewById(R.id.textViewJudul);
			textView.setText(judul[position]);
			//imageView.setImageResource(Imageid[position]);
		} else {
			grid = (View) convertView;
		}
		return grid;
	}
}

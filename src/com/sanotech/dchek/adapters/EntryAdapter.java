package com.sanotech.dchek.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sanotech.dchek.R;
import com.sanotech.dchek.models.FoodEntity;
import com.sanotech.dchek.models.Item;
import com.sanotech.dchek.models.SectionItem;

public class EntryAdapter extends ArrayAdapter<Item> {

	private ArrayList<Item> items;
	private LayoutInflater vi;

	public EntryAdapter(Context context,ArrayList<Item> items)//chnge this to map
	{
		super(context,0, items);
		this.items = items;
		vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		final Item i = items.get(position);
		if (i != null) {
			if(i.isSection()){
				SectionItem si = (SectionItem)i;
				v = vi.inflate(com.sanotech.dchek.R.layout.list_header, null);

				v.setOnClickListener(null);
				v.setOnLongClickListener(null);
				v.setLongClickable(false);
				
				final TextView sectionView = (TextView) v.findViewById(R.id.list_header_title);
				sectionView.setText(si.getTitle());
			}else if(i.isEntity()){
				FoodEntity ei = (FoodEntity)i;
				v = vi.inflate(R.layout.list_item, null);
				final TextView title = (TextView)v.findViewById(R.id.list_item_title);
				//final TextView subtitle = (TextView)v.findViewById(R.id.list_item_entry_summary);
				
				if (title != null) 
					title.setText(ei.getEntity().getFoodName());
				title.setTag(ei.getEntity());
			/*	if(subtitle != null)
					subtitle.setText(ei.subtitle);*/
			}
		/*	else
			{
				Contacts ei = (Contacts)i;
				v = vi.inflate(R.layout.list_item_contacts, null);
				final TextView name = (TextView)v.findViewById(R.id.list_item_name);
				final TextView contactNo = (TextView)v.findViewById(R.id.list_item_entry_contact);
				
				if (name != null) 
					name.setText(ei.name);
				if(contactNo != null)
					contactNo.setText(ei.contactNo);
			}*/
		}
		return v;
	}

}

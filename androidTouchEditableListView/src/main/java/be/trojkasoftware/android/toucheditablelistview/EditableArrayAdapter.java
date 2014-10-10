package be.trojkasoftware.android.toucheditablelistview;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public abstract class EditableArrayAdapter<T> 
extends ArrayAdapter<T> 
implements TouchEditableListView.InsertViewFactory, TouchEditableListView.OnInsertListener,
	TouchEditableListView.DeleteViewFactory, TouchEditableListView.OnDeleteListener {

	public EditableArrayAdapter(Context context, int resource) {
		super(context, resource);
		
		mResource = resource;
	}
	
	public EditableArrayAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId);
		
		mResource = resource;
		mTextViewResourceId = textViewResourceId;
	}
	
	public EditableArrayAdapter(Context context, int resource, T[] objects) {
		super(context, resource, objects);
		
		mResource = resource;
	}
	
	public EditableArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
		super(context, resource, textViewResourceId, objects);
		
		mResource = resource;
		mTextViewResourceId = textViewResourceId;
	}
	
	public EditableArrayAdapter(Context context, int resource, List<T> objects) {
		super(context, resource, objects);
		
		mResource = resource;
	}
	
	public EditableArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
		super(context, resource, textViewResourceId, objects);
		
		mObjects = objects;
		
		mResource = resource;
		mTextViewResourceId = textViewResourceId;
	}
	
	@Override
	public View getInsertView(int afterPosition, int beforePosition) {
		
		LayoutInflater inflater = LayoutInflater.from(super.getContext());
		View v = inflater.inflate(mResource, null);
		
		TextView txtView = (TextView)v.findViewById(mTextViewResourceId);
		
		Object o = getNewObject(afterPosition, beforePosition);
		txtView.setText(o.toString());
		
		return v;
	}
	
	@Override
	public void insert(int afterPosition, int beforePosition) {
		if (afterPosition == -1 || beforePosition == 0) {
			super.insert(getNewObject(afterPosition, beforePosition), 0);	
		}
		else if (afterPosition == (mObjects.size() - 1) || beforePosition == -1) {
			super.insert(getNewObject(afterPosition, beforePosition), mObjects.size());						
		}
		else {
			super.insert(getNewObject(afterPosition, beforePosition), afterPosition + 1);						
		}
	}
	
	@Override
	public View getDeleteView(int position) {
		
		LayoutInflater inflater = LayoutInflater.from(super.getContext());
		View v = inflater.inflate(R.layout.delete_action_layout, null);
		
		return v;
	}
	
	@Override
	public void delete(int position) {
		super.remove(super.getItem(position));
	}
	
	public abstract T getNewObject(int afterPosition, int beforePosition);
	
	protected List<T> mObjects;
	
	protected int mResource;
	protected int mTextViewResourceId;

}

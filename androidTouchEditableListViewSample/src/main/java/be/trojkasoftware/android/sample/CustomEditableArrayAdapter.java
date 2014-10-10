package be.trojkasoftware.android.sample;

import java.util.List;

import android.content.Context;
import be.trojkasoftware.android.toucheditablelistview.EditableArrayAdapter;

public class CustomEditableArrayAdapter extends EditableArrayAdapter<String> {

	public CustomEditableArrayAdapter(Context context, int resource,
			int textViewResourceId, List<String> objects) {
		super(context, resource, textViewResourceId, objects);

	}

	public String getNewObject(int afterPosition, int beforePosition) {
		return "Insert[" + (afterPosition + 1) + "][" + (beforePosition + 1) + "]";
	}
}

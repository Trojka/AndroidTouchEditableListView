package be.trojkasoftware.android.sample;

import java.util.ArrayList;
import java.util.List;

import be.trojkasoftware.android.toucheditablelistview.TouchEditableListView;
import be.trojkasoftware.android.toucheditablelistview.EditableArrayAdapter;
import be.trojkasoftware.android.sample.toucheditablelistview.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class AndroidTouchEditableListViewSampleActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_test);
        
        
        view = new TouchEditableListView(this);
        view.setDividerHeight(2);
        
        LinearLayout ll1 = (LinearLayout)findViewById(R.id.ll1);
        ll1.addView(view);
        
        PrepareTestCase2();
        
    }
    
    void PrepareTestCase1() {
        List<String> values = new ArrayList<String>();
        values.add("Item 001");
        values.add("Item 002");
        values.add("Item 003");
        values.add("Item 004");
        values.add("Item 005");
        values.add("Item 006");
        
        EditableArrayAdapter<String> adapter = new CustomEditableArrayAdapter(this, R.layout.sample_listitem, R.id.text, values);

        view.setAdapter(adapter);       
    }
    
    void PrepareTestCase2() {
        List<String> values = new ArrayList<String>();
        values.add("Item 001");
        values.add("Item 002");
        values.add("Item 003");
        values.add("Item 004");
        values.add("Item 005");
        values.add("Item 006");
		values.add("Item 007");
		values.add("Item 008");
		values.add("Item 009");
		values.add("Item 010");
		values.add("Item 011");
		values.add("Item 012");
		values.add("Item 013");
		values.add("Item 014");
		values.add("Item 015");
		values.add("Item 016");
		values.add("Item 017");
		values.add("Item 018");
		values.add("Item 019");
		values.add("Item 020");
		        
        EditableArrayAdapter<String> adapter = new CustomEditableArrayAdapter(this, R.layout.sample_listitem, R.id.text, values);

        view.setAdapter(adapter); 
        view.setSelectionFromTop(5, 10);
    }

	TouchEditableListView view;
	int newYOffset = 0;
}

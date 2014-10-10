package be.trojkasoftware.android.toucheditablelistview;

import android.content.Context;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.AbsListView;
import android.util.Log;

public class TouchEditableActionView extends ViewGroup {

    private static final String TAG = "TouchEditableActionHeaderView";

    public TouchEditableActionView(Context context, int type) {
        super(context);
        
        mType = type;

        setLayoutParams(new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

//        ShapeDrawable sd1 = new ShapeDrawable(new RectShape());
//        sd1.getPaint().setColor(0xFFFF00FF);
//        sd1.getPaint().setStyle(Style.STROKE);
//        sd1.getPaint().setStrokeWidth(1);        
//        
//        setBackgroundDrawable(sd1);

    }
    
    public void setStretch(int height) {
    	if(height != 0)
    		mHeight = height;
    	else
    		mHeight = 0;
		//Log.i(TAG, "Stretch: " + mHeight);
    }
    
    public void setInsertView(View v, double showAtFactor) {
    	mShowAtFactor = showAtFactor;
    	removeAllViews();
    	addView(v);
    }
    
    public boolean isSet() {
    	return getChildCount() > 0;
    }
    
    public void reset() {
    	removeAllViews();
    	mHeight = 0;
    	mShowAtFactor = 1.0;
    	mShowAtHeight = Integer.MAX_VALUE;
    }
    
    public boolean isInserting() {
    	return mIsInserting;
    }

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		//Log.i(TAG, "onLayout");
        final View child = getChildAt(0);
        if(child == null) {
        	return;
        }
        if(mShowAtHeight <= mHeight){
        	mIsInserting= true;
        	if(mType == TYPE_HEADER) {
        		child.layout(0, getMeasuredHeight() - child.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
        	}
        	else if (mType == TYPE_FOOTER) {
        		child.layout(0, 0, getMeasuredWidth(), child.getMeasuredHeight());
        	}
        }
        else {
        	mIsInserting = false;
        	child.layout(0, 0, 0, 0);        	
        }
	}
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//Log.i(TAG, "onMeasure: " + mHeight);
        int height = mHeight;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        
        View child = getChildAt(0);
        if(child != null) {
            if (child.isLayoutRequested()) {
                // Always let child be as tall as it wants.
                measureChild(child, widthMeasureSpec,
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            }
            
            mShowAtHeight = (int)(mShowAtFactor * child.getMeasuredHeight());
        }
        
		setMeasuredDimension(width, height);    	
    }
    
    public static int TYPE_ITEM = 0;
    public static int TYPE_FOOTER = 1;
    public static int TYPE_HEADER = 2;
    
    private int mType = TYPE_ITEM;
    
    private int mHeight = 0;
    private double mShowAtFactor = 1.0;
    
    private int mShowAtHeight = Integer.MAX_VALUE;
    
    private boolean mIsInserting = false;
}

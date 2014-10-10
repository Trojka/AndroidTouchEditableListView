package be.trojkasoftware.android.toucheditablelistview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.AbsListView;

public class TouchEditableItemView extends ViewGroup {

    private static final String TAG = "TouchEditableItemView";

    public TouchEditableItemView(Context context, int type) {
        super(context);

        setLayoutParams(new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        ShapeDrawable sd1 = new ShapeDrawable(new RectShape());
        sd1.getPaint().setColor(0xFF0000FF);
        sd1.getPaint().setStyle(Style.STROKE);
        sd1.getPaint().setStrokeWidth(1);        
        
        setBackgroundDrawable(sd1);

    }
    
    public void setActionView(View v, double showAtFactor, int gravity) {
    	mGravity = gravity;
    	mShowAtFactor = showAtFactor;
    	removeView(actionView);
    	actionView = v;
    	addView(actionView);
    }
    
    public boolean isSet() {
    	return ((mType == TYPE_ITEM && getChildCount() > 1)
    			|| (mType != TYPE_ITEM && getChildCount() > 0));
    }
    
    public void reset() {
    	removeView(actionView);
    	actionView = null;
    	mShowAtFactor = 1.0;
    	mShowAtHeight = Integer.MAX_VALUE;
    }
    
    public boolean isShowingInsertView() {
    	return mShowingInsertView;
    }

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		//Log.i(TAG, "onLayout");
        View child = null;
        for(int i = 0; i < getChildCount(); i++) {
        	View v = getChildAt(i);
        	if (v != actionView) {
        		child = v;
        		break;
        	}
        }
        
        if(child == null) {
        	return;
        }
//		Log.i(TAG, "child.Layout[0, 0, " + getMeasuredWidth() +", " + child.getMeasuredHeight() + "]");
        
        int height = getMeasuredHeight();
        if(mShowAtHeight <= height && actionView != null)
        {
        	if(mGravity == Gravity.CENTER) {
	        	mShowingInsertView= true;
	        	int diff = (height - child.getMeasuredHeight() - actionView.getMeasuredHeight()) / 2;
	//    		Log.i(TAG, "insertView.Components[" + height + ", " + diff + ", " 
	//    				+ child.getMeasuredHeight() + ", " + insertView.getMeasuredHeight() + "]");
	//    		Log.i(TAG, "insertView.Layout[0, " + (child.getMeasuredHeight() + diff)
	//    				+ ", " + getMeasuredWidth() + ", " + (child.getMeasuredHeight() + diff + insertView.getMeasuredHeight()) + "]");
	            child.layout(0, 0, getMeasuredWidth(), child.getMeasuredHeight());
	        	actionView.layout(0, child.getMeasuredHeight() + diff, 
	        			getMeasuredWidth(), child.getMeasuredHeight() + diff + actionView.getMeasuredHeight());
        	} else if(mGravity == Gravity.RIGHT) {
	        	mShowingInsertView= true;
	        	int diff = (height - actionView.getMeasuredHeight()) / 2;
	            child.layout(-1 * actionView.getMeasuredWidth(), 0, 
	            		getMeasuredWidth() - actionView.getMeasuredWidth(), child.getMeasuredHeight());
	        	actionView.layout(getMeasuredWidth() - actionView.getMeasuredWidth(), diff, 
	        			getMeasuredWidth(), diff + actionView.getMeasuredHeight());
        	} else {
                child.layout(0, 0, getMeasuredWidth(), child.getMeasuredHeight());        		
        	}
        }
        else if (mShowAtHeight > height && actionView != null) {
        	mShowingInsertView= false;
            child.layout(0, 0, getMeasuredWidth(), child.getMeasuredHeight());
        	actionView.layout(0, 0, 0, 0);        	
        }
        else {
            child.layout(0, 0, getMeasuredWidth(), child.getMeasuredHeight());        	
        }
	}
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);        

        View child = null;
        for(int i = 0; i < getChildCount(); i++) {
        	View v = getChildAt(i);
        	if (v != actionView) {
        		child = v;
        		break;
        	}
        }

        if (child.isLayoutRequested()) {
            measureChild(child, widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        }

        int childHeight = child.getMeasuredHeight();
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            ViewGroup.LayoutParams lp = getLayoutParams();

            if (lp.height > 0) {
                height = lp.height;
            } else {
                height = childHeight;
            }
        }
        
        if(actionView != null) {
            if (actionView.isLayoutRequested()) {
                measureChild(actionView, widthMeasureSpec,
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            }
            
            mShowAtHeight = (int)(mShowAtFactor * actionView.getMeasuredHeight()) + childHeight;
//    		Log.i(TAG, "insertView.Measure[" + mShowAtHeight + ", " + height + ", "  + childHeight + ", " + insertView.getMeasuredHeight() + "]");
        }
        
		//Log.i(TAG, "onMeasure[Offset:" + Math.abs(mParent.getOffsetY()) + "]");
		setMeasuredDimension(width, height);
    }
    
    View actionView = null;
    
    int mGravity = Gravity.NO_GRAVITY;
    
    public static int TYPE_ITEM = 0;
    public static int TYPE_FOOTER = 1;
    public static int TYPE_HEADER = 2;
    private int mType = TYPE_ITEM;
    
    private int mHeight = 0;

    private double mShowAtFactor = 1.0;    
    private int mShowAtHeight = Integer.MAX_VALUE;
    
    private boolean mShowingInsertView = false;

}

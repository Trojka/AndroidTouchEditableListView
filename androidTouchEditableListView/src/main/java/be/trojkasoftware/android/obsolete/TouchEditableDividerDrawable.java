package be.trojkasoftware.android.obsolete;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.AbsListView;

public class TouchEditableDividerDrawable extends Drawable {

    private static final String TAG = "TouchEditableDividerDrawable";

	public TouchEditableDividerDrawable(Drawable originalDivider, TouchEditableListView2 parent)
	{
		this.originalDivider = originalDivider;
        mParent = parent;
	}

	public Drawable getOriginalDivider()
	{
		return originalDivider;
	}
	
	@Override
	public void draw(Canvas canvas) {
		Log.i(TAG, "draw:"+Math.abs(mParent.getOffsetY()));
    	
    	Matrix matrix;
    	matrix = new Matrix();
		matrix.postTranslate(0, -1 * Math.abs(mParent.getOffsetY()));
		
    	final Matrix currentMatrix = canvas.getMatrix();   	
    	canvas.concat(matrix);
    	
    	originalDivider.draw(canvas);
        
        canvas.setMatrix(currentMatrix);
	}

	@Override
	public int getOpacity() {
		return originalDivider.getOpacity();
	}

	@Override
	public void setAlpha(int arg0) {
		originalDivider.setAlpha(arg0);
	}

	@Override
	public void setColorFilter(ColorFilter arg0) {
		originalDivider.setColorFilter(arg0);
	}
    
    TouchEditableListView2 mParent;
	Drawable originalDivider;

}

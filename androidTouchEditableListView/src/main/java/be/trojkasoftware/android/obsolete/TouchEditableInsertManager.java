package be.trojkasoftware.android.obsolete;

import be.trojkasoftware.android.toucheditablelistview.TouchEditableListView;
import be.trojkasoftware.android.toucheditablelistview.TouchEditableStateManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class TouchEditableInsertManager {

    private static final String TAG = "TouchEditableInsertManager";
	
	public TouchEditableInsertManager(TouchEditableStateManager manager, TouchEditableListView listView, int topItemPosition, int bottomItemPosition, int lastDownY) {
		mListView = listView;
		mManager = manager;
		
    	mTopItemPosition = topItemPosition;
    	mBottomItemPosition = bottomItemPosition;
    	
    	mLastDownY = lastDownY;
    	mLastMoveY = lastDownY;
	}
	
	public void CleanUp()
	{
		
	}
	
	public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        
        if (action == MotionEvent.ACTION_UP) {

//        	View v = mListView.getChildAt(mTopItemPosition - mListView.getFirstVisiblePosition());
//        	if(v != null)
//        	{
//        		AdjustItem(mTopItemPosition, v);
//        		mListView.requestLayout();
//        	}        	
//
//        	mListView.invalidateViews();

//        	mListView.setSelectionFromTop(mTopItemPosition, mV0TopY);
        	
//        	mManager.getActionHeader().Stretch(0);
//        	mManager.getActionHeader().requestLayout();        	

        	Log.i(TAG, "onTouchEvent.ACTION_UP");
        }
        if (action == MotionEvent.ACTION_DOWN) {

        	View v = mListView.getChildAt(mTopItemPosition - mListView.getFirstVisiblePosition());
        	mV0TopY = v.getTop();
        	mOriginalTouchDownPosHeight = v.getMeasuredHeight();
        	//Log.i(TAG, "onInterceptTouchEvent.ACTION_DOWN: FirstVisiblePosition[" + mFirstVisiblePosition + "]ViewFVPTopY[" + mV0TopY + "]TopItemPosition[" + mTopItemPosition + "]LastDownY[" + mLastDownY + "]");

        }
        if (action == MotionEvent.ACTION_MOVE) {

        	int y = (int) ev.getY();

        	mLastMoveY = y;
        	
        	
        	int stretch = Math.abs(mLastDownY - mLastMoveY);
    		Log.i(TAG, "onTouchEvent: LastDownY[" + mLastDownY + "]LastMoveY[" + mLastMoveY + "]Stretch[" + stretch + "]");
//        	mManager.getActionHeader().Stretch(stretch);
//        	mManager.getActionHeader().requestLayout();

        	//mListView.invalidate();

//        	int scrollByY = mV0TopY + y - mLastDownY; 
//        	mListView.setSelectionFromTop(mTopItemPosition, scrollByY);
//        	Log.i(TAG, "onTouchEvent.ACTION_MOVE.SetSelectionFromTop V0TopY["+mV0TopY+"]Y["+y+"]LastDownY["+mLastDownY+"][ScrollByY:"+scrollByY+"]");
//
//        	View v = mListView.getChildAt(mTopItemPosition - mListView.getFirstVisiblePosition());
//        	if(v != null)
//        	{
//        		Log.i(TAG, "onTouchEvent.ACTION_MOVE.AdjustItem[TopItemPosition:"+mTopItemPosition+"]");
//        		AdjustItem(mTopItemPosition, v);
//        		mListView.requestLayout();
//        	} 
//        	else
//        	{
//        		Log.i(TAG, "onTouchEvent.ACTION_MOVE.GetChildAt[TopItemPosition:"+mTopItemPosition+"] is NULL");
//        	}

        }

        return true;

	}

    public void AdjustItem(int position, View v)
    {
//        ViewGroup.LayoutParams lp = v.getLayoutParams();
//    	if(position == mTopItemPosition)
//    	{
//    		// the touched item will stretch
//    		lp.height = mOriginalTouchDownPosHeight + Math.abs(mLastDownY - mLastMoveY);
//        	Log.i(TAG, "AdjustItem[Indx:"+position+"][StretchHeight:" + lp.height + "][MeasuredHeight"+v.getMeasuredHeight()+"]");
//    	}
//    	else
//    	{
//    		// everything else remains as is
//        	lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        	Log.i(TAG, "AdjustItem[Indx:" + position + "][SetHeight:WRAP_CONTENT]");
//    	}
//        v.setLayoutParams(lp);
    }    
    
    TouchEditableListView mListView = null;
    TouchEditableStateManager mManager = null;
    
    private int mTopItemPosition = 0;
    private int mBottomItemPosition = 0;

    private int mV0TopY = 0;
    private int mOriginalTouchDownPosHeight;
    
    private int mLastDownY;
    private int mLastMoveY;
    
    private int mHeightOfInsertedItem = 30;

	
}

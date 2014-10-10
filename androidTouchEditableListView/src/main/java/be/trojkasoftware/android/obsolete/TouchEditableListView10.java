package be.trojkasoftware.android.obsolete;

import java.util.ArrayList;

import be.trojkasoftware.android.toucheditablelistview.TouchEditableItemView;
import be.trojkasoftware.android.toucheditablelistview.TouchEditableStateManager;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ListAdapter;
import android.widget.ListView;

public class TouchEditableListView10 extends ListView {

    private static final String TAG = "TouchEditableListView";

	public TouchEditableListView10(Context context) {
		super(context);
    	
		this.setClipChildren(false);
		this.setClipToPadding(false);
	}

    private class AdapterWrapper extends BaseAdapter {
        private ListAdapter mAdapter;

        public AdapterWrapper(ListAdapter adapter) {
            super();
            mAdapter = adapter;
            
            mAdapter.registerDataSetObserver(new DataSetObserver() {
                public void onChanged() {
                    notifyDataSetChanged();
                }

                public void onInvalidated() {
                    notifyDataSetInvalidated();
                }
            });
        }

        public ListAdapter getAdapter() {
            return mAdapter;
        }

        @Override
        public long getItemId(int position) {
            return mAdapter.getItemId(position);
        }

        @Override
        public Object getItem(int position) {
            return mAdapter.getItem(position);
        }

        @Override
        public int getCount() {
            return mAdapter.getCount();
        }

        @Override
        public boolean areAllItemsEnabled() {
            return mAdapter.areAllItemsEnabled();
        }

        @Override
        public boolean isEnabled(int position) {
            return mAdapter.isEnabled(position);
        }
        
        @Override
        public int getItemViewType(int position) {
            return mAdapter.getItemViewType(position);
        }

        @Override
        public int getViewTypeCount() {
            return mAdapter.getViewTypeCount();
        }
        
        @Override
        public boolean hasStableIds() {
            return mAdapter.hasStableIds();
        }
        
        @Override
        public boolean isEmpty() {
            return mAdapter.isEmpty();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	
//        	Log.i(TAG, "AdapterWrapper.getView[Indx:" + position + "]");

        	TouchEditableItemView v;
            View child;
            if (convertView != null) {
                v = (TouchEditableItemView) convertView;
                View oldChild = v.getChildAt(0);

            	child = mAdapter.getView(position, oldChild, TouchEditableListView10.this);
                
                if (child != oldChild) {
                    if (oldChild != null) {
                        v.removeViewAt(0);
                    }
                    if(child != null) {
                    	v.addView(child);
                    }
                }
            } else {
            	child = mAdapter.getView(position, null, TouchEditableListView10.this);

                v = new TouchEditableItemView(getContext(), 0);

                v.setLayoutParams(new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                v.addView(child);
            }
            
            //TouchEditableListView10.this.mStateManager.AdjustItem(position, v);

            return v;
        }

    }
	
    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter != null) {
            mAdapterWrapper = new AdapterWrapper(adapter);
//            adapter.registerDataSetObserver(mObserver);

//            if (adapter instanceof DropListener) {
//                setDropListener((DropListener) adapter);
//            }
//            if (adapter instanceof DragListener) {
//                setDragListener((DragListener) adapter);
//            }
//            if (adapter instanceof RemoveListener) {
//                setRemoveListener((RemoveListener) adapter);
//            }
        } else {
            mAdapterWrapper = null;
        }

        super.setAdapter(mAdapterWrapper);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
    	if(!mIsTouched)
    	{
    		if(action == MotionEvent.ACTION_UP) {
        		Log.i(TAG, "onTouchEvent.ACTION_UP");
    			mIsTouched = true;
    		}
    		
    		Log.i(TAG, "onTouchEvent super()");
    		return super.onTouchEvent(ev);
    	}
    	
    	return mStateManager.onTouchEvent(ev);

//
//        if (action == MotionEvent.ACTION_UP) {
////            if (getParent() != null) {
////            	// Defer all calls to the onTouchEvent handler
////                getParent().requestDisallowInterceptTouchEvent(false);
////            }
//        	
//        	mState = STATE_NORMAL;
//        	
//    		View v = getChildAt(mTopItemPosition - this.getFirstVisiblePosition());
//        	if(v != null)
//        	{
//            	AdjustItem(mTopItemPosition, v);
//            	requestLayout();
//        	}        	
//        	
//        	invalidateViews();
//        	
//        	setSelectionFromTop(mTouchPosition, mV0TopY);
//        	
//        	Log.i(TAG, "onTouchEvent.ACTION_UP");
//        }
//        if (action == MotionEvent.ACTION_DOWN) {
//        	//mLastDownX = (int) ev.getX();
//        	//mLastDownY = (int) ev.getY();
//
//        	//mTouchDownPos = this.pointToPosition((int) ev.getX(), mLastDownY); 
//
//            //if (getParent() != null) {
//            //    getParent().requestDisallowInterceptTouchEvent(true);
//            //}
//        	//Log.i(TAG, "onTouchEvent.ACTION_DOWN[TouchDownPos" + mTouchDownPos + "]["+mLastDownY+"]");
//        }
//        if (action == MotionEvent.ACTION_MOVE) {
//            
//        	//int x= (int) ev.getX();
//        	int y = (int) ev.getY();
//        	
//        	
//        	//int scrollByY = mLastMoveY - y; 
//        	mLastMoveY = y;
//
//        	int scrollByY = mV0TopY + y - mLastDownY; 
//        	setSelectionFromTop(mTouchPosition, scrollByY);
//        	Log.i(TAG, "onTouchEvent.ACTION_MOVE.SetSelectionFromTop V0TopY["+mV0TopY+"]Y["+y+"]LastDownY["+mLastDownY+"][ScrollByY:"+scrollByY+"]");
//        	
//    		//View v = getChildAt(mTopItemPosition);
//        	View v = getChildAt(mTopItemPosition - this.getFirstVisiblePosition());
//        	if(v != null)
//        	{
//            	Log.i(TAG, "onTouchEvent.ACTION_MOVE.AdjustItem[TopItemPosition:"+mTopItemPosition+"]");
//            	AdjustItem(mTopItemPosition, v);
//            	requestLayout();
//        	} 
//        	else
//        	{
//            	Log.i(TAG, "onTouchEvent.ACTION_MOVE.GetChildAt[TopItemPosition:"+mTopItemPosition+"] is NULL");
//        	}
//
//
//        }
//        
//    	return true;
    }
    
//    @Override
//    protected void layoutChildren() 
//    {
//    	Log.i(TAG, "layoutChildren.START");
//        super.layoutChildren();
//        Log.i(TAG, "layoutChildren.END");
//    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();

//        if (action == MotionEvent.ACTION_DOWN)
//        {
//	    	mTouchDownPos = this.pointToPosition((int)ev.getX(), (int) ev.getY());
//	    	mTouchDownIndx = (int)this.pointToRowId((int)ev.getX(), (int) ev.getY());
//			Log.i(TAG, "onInterceptTouchEvent.ACTION_DOWN: TouchDownPos[" + mTouchDownPos + "]TouchDownIndx[" + mTouchDownIndx + "]");
//        }
//        
//		return super.onInterceptTouchEvent(ev);
		
    	if(!mIsTouched)
    	{
    		if(action == MotionEvent.ACTION_UP) {
        		Log.i(TAG, "onInterceptTouchEvent.ACTION_UP");
    			mIsTouched = true;
    		}
    		
    		Log.i(TAG, "onInterceptTouchEvent super()");
    		return super.onInterceptTouchEvent(ev);
    	}

        
//        if (action == MotionEvent.ACTION_DOWN) {
//        	//mLastDownX = (int) ev.getX();
//        	mLastDownY = (int) ev.getY();
//        	
//        	mLastMoveY = mLastDownY;
//        	
//        	mTouchPosition = this.pointToPosition((int)ev.getX(), mLastDownY);
//        	//mFirstVisiblePosition = this.getFirstVisiblePosition();
//        	
//        	mTopItemPosition = mTouchPosition;// - this.getFirstVisiblePosition();
//        	mBottomItemPosition = mTopItemPosition + 1;
//        	
////        	mBottomItemPosition = touchDownPos - mFirstVisiblePosition;
////        	mTopItemPosition = mBottomItemPosition - 1;
//        	
//        	View v = getChildAt(mTopItemPosition - this.getFirstVisiblePosition());
//        	mV0TopY = v.getTop();
//        	//View v0 = getChildAt(0);
//        	//mV0TopY = v0.getTop();
//        	mOriginalTouchDownPosHeight = v.getMeasuredHeight();
//    		//Log.i(TAG, "onInterceptTouchEvent.ACTION_DOWN: FirstVisiblePosition[" + mFirstVisiblePosition + "]ViewFVPTopY[" + mV0TopY + "]TopItemPosition[" + mTopItemPosition + "]LastDownY[" + mLastDownY + "]");
//        	
//        	mState = STATE_INSERT;
//
////            if (getParent() != null) {
////            	// Defer all calls to the onTouchEvent handler
////                getParent().requestDisallowInterceptTouchEvent(true);
////            }
//        }
        
		Log.i(TAG, "onInterceptTouchEvent.StateManager");        
		return mStateManager.onInterceptTouchEvent(ev);
    }


//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//		Log.i(TAG, "dispatchDraw");
//    	
//		this.setClipChildren(false);
//		this.setClipToPadding(false);
//
//		super.dispatchDraw(canvas);
//    }
       
   
//    private void AdjustItem(int position, View v)
//    {
//        ViewGroup.LayoutParams lp = v.getLayoutParams();
//    	if(mState == STATE_NORMAL)
//    	{
//            // we have no offset, draw everything as normal
//        	Log.i(TAG, "AdjustItem[OffsetY:ZERO][Indx:" + position + "][SetHeight:WRAP_CONTENT]");
//        	lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        	return;
//    	}
//
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
//    }    
     
    private AdapterWrapper mAdapterWrapper;
   
    private TouchEditableStateManager mStateManager;
    
    private boolean mIsTouched = false;

//    private int mOriginalTouchDownPosHeight;
//    
//    private int mLastDownY;
//    private int mLastMoveY;
//    
//    private int mHeightOfInsertedItem = 30;
//    
//    private int mTouchPosition = 0;
//    private int mV0TopY = 0;
//    
////    private int mTopItemPosition = 0;
////    private int mBottomItemPosition = 0;
//    
//    private final int STATE_NORMAL = 0;
//    private final int STATE_INSERT = 1;
//    
//    private int mState = STATE_NORMAL;
    
}

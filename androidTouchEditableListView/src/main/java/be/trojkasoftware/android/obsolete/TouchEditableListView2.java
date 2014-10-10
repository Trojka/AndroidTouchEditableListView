package be.trojkasoftware.android.obsolete;

import java.util.ArrayList;

import be.trojkasoftware.android.toucheditablelistview.TouchEditableItemView;

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

public class TouchEditableListView2 extends ListView {

    private static final String TAG = "TouchEditableListView";


	public TouchEditableListView2(Context context) {
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
        
        public void setCollapsLimit(int limit)
        {
        	mCollapseLimit = limit;
        }

        private void addCollapseItems(ArrayList<Integer> collapsedPositions)
        {
//        	if(mCollapsedPositions.contains(position)) {
//            	Log.i(TAG, "addCollapseItem.Contains[Pos:"+position+"]");
//        		return;
//        	}
//        	Log.i(TAG, "addCollapseItem.Add[Pos:"+position+"]");
        	mCollapsedPositions.addAll(collapsedPositions);
        }

//        private void addCollapseItem(int position)
//        {
//        	if(mCollapsedPositions.contains(position)) {
//            	Log.i(TAG, "addCollapseItem.Contains[Pos:"+position+"]");
//        		return;
//        	}
//        	Log.i(TAG, "addCollapseItem.Add[Pos:"+position+"]");
//        	mCollapsedPositions.add(position);
//        }
        
        public void clearCollapseItem()
        {
        	mCollapsedPositions.clear();
        }
        
        public int countCollapseItem()
        {
        	return mCollapsedPositions.size();
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
        	int newCount = mAdapter.getCount() - mCollapsedPositions.size();
        	Log.i(TAG, "getCount.Replace[Original:"+mAdapter.getCount()+"][New:"+newCount+"]");
            return newCount;
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

        	TouchEditableItemView v;
            View child;
            if (convertView != null) {
                v = (TouchEditableItemView) convertView;
                View oldChild = v.getChildAt(0);

//                if(mCollapsedPositions.size() != 0 && position <= (mCollapseLimit - mCollapsedPositions.size()))
//                {
//                	int withPos = position + mCollapsedPositions.size();
//	            	Log.i(TAG, "getView.ReFill[CollapseLimit:"+mCollapseLimit+"][RequestPos:"+position+"][WithPos:" + withPos + "]");
//                	child = mAdapter.getView(withPos, null, TouchEditableListView.this);                	
//                	//child.setVisibility(View.VISIBLE);
//                }
//                else if(mCollapsedPositions.size() != 0 && (mCollapsedPositions.size() < position) && (position < mCollapseLimit))
//                {
//                	int withPos = position - mCollapsedPositions.size();
//	            	Log.i(TAG, "getView.ReFill[CollapseLimit:"+mCollapseLimit+"][RequestPos:"+position+"][WithHiddenPos:" + withPos + "]");
//                	//child = null;
//	            	child = mAdapter.getView(withPos, null, TouchEditableListView.this);  
//                	//child.setVisibility(View.GONE);
//                }
//                else
                {
	            	Log.i(TAG, "getView.ReFill[CollapseLimit:"+mCollapseLimit+"][RequestPos:"+position+"][WithPos:nochange]");
                	child = mAdapter.getView(position, oldChild, TouchEditableListView2.this);
                	//child.setVisibility(View.VISIBLE);
                }
                
                if (child != oldChild) {
                    if (oldChild != null) {
                        v.removeViewAt(0);
                    }
                    if(child != null) {
                    	v.addView(child);
                    }
                }
            } else {
//                if(mCollapsedPositions.size() != 0 && position < (mCollapseLimit - mCollapsedPositions.size()))
//                {
//                	int withPos = position + mCollapsedPositions.size();
//	            	Log.i(TAG, "getView.FillNew[CollapseLimit:"+mCollapseLimit+"][RequestPos:"+position+"][WithPos:" + withPos + "]");
//                	child = mAdapter.getView(withPos, null, TouchEditableListView.this);                	
//                	//child.setVisibility(View.VISIBLE);
//                }
//                else if(mCollapsedPositions.size() != 0 && (mCollapsedPositions.size() < position) && (position < mCollapseLimit))
//                {
//                	int withPos = position - mCollapsedPositions.size();
//	            	Log.i(TAG, "getView.ReFill[CollapseLimit:"+mCollapseLimit+"][RequestPos:"+position+"][WithHiddenPos:" + withPos + "]");
//                	//child = null;
//	            	child = mAdapter.getView(withPos, null, TouchEditableListView.this);  
//                	//child.setVisibility(View.GONE);
//                }
//                else
                {
	            	Log.i(TAG, "getView.FillNew[CollapseLimit:"+mCollapseLimit+"][RequestPos:"+position+"][WithPos:nochange]");
                	child = mAdapter.getView(position, null, TouchEditableListView2.this);
                	//child.setVisibility(View.VISIBLE);
                }
//                if (child instanceof Checkable) {
//                    v = new DragSortItemViewCheckable(getContext());
//                } else {
                    v = new TouchEditableItemView(getContext(), 0);
//                }
                v.setLayoutParams(new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                v.addView(child);
            }

//            // Set the correct item height given drag state; passed
//            // View needs to be measured if measurement is required.
//            adjustItem(position + getHeaderViewsCount(), v, true);
            
//            AdjustItem(position, v);
            
//            if(position < TouchEditableListView.this.mTouchDownPos)
//            {
//            	Log.i(TAG, "getView.setTranslationY[RequestPos:"+position+"][OffsetY:"+TouchEditableListView.this.mOffsetY+"]");
//            	
//            	//v.offsetTopAndBottom(TouchEditableListView.this.mOffsetY);
//            	v.setTranslationY(-1 * TouchEditableListView.this.mOffsetY);
//            }

            return v;
        }

        ArrayList<Integer> mCollapsedPositions = new ArrayList<Integer>();
        int mCollapseLimit;
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
    
//    @Override
//    public Drawable getDivider()
//    {
//		Log.i(TAG, "getDivider");
//    	return new TouchEditableDividerDrawable(super.getDivider(), this);
//    }
//    
//    @Override
//    public void setDivider(Drawable divider) {
//    	TouchEditableDividerDrawable useDivider = new TouchEditableDividerDrawable(divider, this);
//    	
//    	super.setDivider(useDivider);
//    	
////        if (divider != null) {
////            mDividerHeight = divider.getIntrinsicHeight();
////        } else {
////            mDividerHeight = 0;
////        }
////        mDivider = divider;
////        mDividerIsOpaque = divider == null || divider.getOpacity() == PixelFormat.OPAQUE;
////        requestLayout();
////        invalidate();
//    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
    	if(!isTouched)
    	{
    		if(action == MotionEvent.ACTION_UP)
    			isTouched = true;
    		return super.onTouchEvent(ev);
    	}
    	

        if (action == MotionEvent.ACTION_UP) {
        	
//            mAdapterWrapper.setCollapsLimit(0);
//    		mAdapterWrapper.clearCollapseItem();
//    		mOffsetY = 0;
//    		
//            int first = getFirstVisiblePosition();
//            int last = getLastVisiblePosition();
//        	
//        	for(int i = first; i <= last; i++)
//        	{
//	        	View v = getChildAt(i);
//	            ViewGroup.LayoutParams lp = v.getLayoutParams();
//            	lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//	            v.setLayoutParams(lp);
//        	}
        	
        	requestLayout();
        	invalidateViews();
        	Log.i(TAG, "onTouchEvent.ACTION_UP");
        }
        if (action == MotionEvent.ACTION_DOWN) {
        	mLastDownX = (int) ev.getX();
        	mLastDownY = (int) ev.getY();
        	
        	mLastOffsetDownY = mLastDownY;

        	mTouchDownPos = this.pointToPosition(mLastDownX, mLastDownY); // includes headers/footers
            mAdapterWrapper.setCollapsLimit(mTouchDownPos);

            if (getParent() != null) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        	Log.i(TAG, "onTouchEvent.ACTION_DOWN[TouchDownPos" + mTouchDownPos + "][Offset:"+mOffsetY+"]["+mLastDownX+"]["+mLastDownY+"]");
        }
        if (action == MotionEvent.ACTION_MOVE) {
        	boolean anItemWasCollapsed = false;
            int collapsedHeight = 0;
            
        	int x= (int) ev.getX();
        	int y = (int) ev.getY();
        	
        	//mOffsetY = (int) Math.sqrt((Math.pow(mLastDownX - x, 2.0) + Math.pow(mLastDownY - y, 2.0)));
//        	mLastOffsetDownY = mLastOffsetDownY - y;
        	mOffsetY = mLastOffsetDownY - y;
        	
        	int offsetBy = mLastDownY - y; 
        	int offsetIndx = 0;
        	
        	Log.i(TAG, "onTouchEvent.ACTION_MOVE.setSelectionFromTop[OffsetIndx:" + offsetIndx + "][OffsetBy:"+offsetBy+"]");
        	//setSelectionFromTop(mTouchDownPos, mOffsetY);
        	//setSelectionFromTop(offsetIndx, offsetBy);
        	
        	scrollBy(0, offsetBy);
        	
//        	this.post(new Runnable() {
//                @Override
//                public void run() {
//                    TouchEditableListView.this.setSelectionFromTop(mTouchDownPos, mOffsetY);
//                }
//            });
        	
//            int first = getFirstVisiblePosition();
//            int last = getLastVisiblePosition();
//        	Log.i(TAG, "onTouchEvent.ACTION_MOVE[TouchDownPos" + mTouchDownPos + "][First"+first+"][Last"+last+"]");
//            
//            //int heightToOverlap = mLastOffsetDownY; // - (ListItemHeight * mAdapterWrapper.countCollapseItem());
//            ArrayList<Integer> itemsToCollapsedPositions = new ArrayList<Integer>();
//        	for(int i = first; i <= last; i++)
//        	{
//        		View v = getChildAt(i);
//	        	if(v == null)
//	        	{
//	        		continue;
//	        	}
//	        	
//	        	boolean viewWasCollapsed = AdjustItem(i, v);
//	        	if(viewWasCollapsed)
//	        	{
//	        		itemsToCollapsedPositions.add(i);
//	        	}
//	        	anItemWasCollapsed = anItemWasCollapsed || viewWasCollapsed;
//	        	
//	        	if(viewWasCollapsed)
//	        	{
//	        		collapsedHeight = collapsedHeight + ListItemHeight;
//	        	}
//
//        	}
//        	
//        	mAdapterWrapper.addCollapseItems(itemsToCollapsedPositions);
        	requestLayout();

    		//mAdapterWrapper.notifyDataSetChanged();
//            if(anItemWasCollapsed)
            {
            	//mLastOffsetDownY = mLastOffsetDownY - collapsedHeight;
            	//mTouchDownPos = mTouchDownPos - itemsToCollapsedPositions.size();
        		Log.i(TAG, "onTouchEvent.invalidateViews");
            	invalidateViews();
            }
            
        }
        
    	return true;
    }
    
    @Override
    protected void layoutChildren() 
    {
    	Log.i(TAG, "layoutChildren.START");
        super.layoutChildren();
        Log.i(TAG, "layoutChildren.END");
    }
    
//    private boolean AdjustItem(int position, View v)
//    {
//        ViewGroup.LayoutParams lp = v.getLayoutParams();
//    	if(mOffsetY <= 0)
//    	{
//            // we have no offset, draw everything as normal
//        	Log.i(TAG, "AdjustItem[OffsetY:ZERO][Indx:" + position + "][SetHeight:WRAP_CONTENT]");
//        	lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        	return false;
//    	}
//
//    	// there is an offset, process it  
//    	boolean isCollapsed = false;
//    	if(position <= Math.floor( mOffsetY/ListItemHeight))
//    	{
//    		if(position == 0)
//    		{
//        		lp.height = ListItemHeight - (int) (mOffsetY - (Math.floor( mOffsetY/ListItemHeight) * ListItemHeight));
//            	Log.i(TAG, "AdjustItem[OffsetY:" + mOffsetY + "][Indx:"+position+"][ShrinkHeight:" + lp.height + "][MeasuredHeight"+v.getMeasuredHeight()+"]");
//    		}
//    		else
//    		{
//	    		lp.height = 0;
//	    		//mAdapterWrapper.addCollapseItem(position);
//	        	Log.i(TAG, "AdjustItem[OffsetY:" + mOffsetY + "][Indx:"+position+"]CollapseItem");
//	        	isCollapsed = true;
//    		}
//    	}
//    	else if(position == mTouchDownPos - mAdapterWrapper.countCollapseItem() )
//    	{
//    		// the touched item will stretch
//    		lp.height = ListItemHeight + mOffsetY;
//        	Log.i(TAG, "AdjustItem[OffsetY:" + mOffsetY + "][Indx:"+position+"][StretchHeight:" + lp.height + "][MeasuredHeight"+v.getMeasuredHeight()+"]");
//    	}
//    	else
//    	{
//    		// everything else remains as is
//        	lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        	Log.i(TAG, "AdjustItem[OffsetY:" + mOffsetY + "][Indx:" + position + "][SetHeight:WRAP_CONTENT]");
//    	}
//        v.setLayoutParams(lp);
//        return isCollapsed;
//    }    
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        
    	if(!isTouched)
    	{
    		if(action == MotionEvent.ACTION_UP)
    			isTouched = true;
    		return super.onInterceptTouchEvent(ev);
    	}
    	
        if (action == MotionEvent.ACTION_DOWN) {
        	mLastDownX = (int) ev.getX();
        	mLastDownY = (int) ev.getY();
        	
        	mLastOffsetDownY = mLastDownY;
        	
            final int x = (int) ev.getX();
            final int y = (int) ev.getY();

        	mTouchDownPos = this.pointToPosition(mLastDownX, mLastDownY); // includes headers/footers
            mAdapterWrapper.setCollapsLimit(mTouchDownPos);

            if (getParent() != null) {
            	// Defer all calls to the onTouchEvent handler
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        }
//        if (action == MotionEvent.ACTION_MOVE) {
//        	int x= (int) ev.getX();
//        	int y = (int) ev.getY();
//        	
//        	mOffsetY = (int) Math.sqrt((Math.pow(mLastDownX - x, 2.0) + Math.pow(mLastDownX - x, 2.0)));
//        	
//            int first = getFirstVisiblePosition();
//            int last = getLastVisiblePosition();
//        	
//        	for(int i = first; i <= last; i++)
//        	{
//	        	View v = getChildAt(i);
//	            ViewGroup.LayoutParams lp = v.getLayoutParams();
//	            if( i == first)
//	            {
//	            	lp.height = 20 - mOffsetY;
//	            }
//	            else
//	            {
//	            	lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//	            }
//	            v.setLayoutParams(lp);
//        	}
//        	
//        	requestLayout();
//        }
        
		Log.i(TAG, "onInterceptTouchEvent.invalidate");
		//mAdapterWrapper.notifyDataSetChanged();
        //invalidateViews();
        
		return true;
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
		Log.i(TAG, "dispatchDraw");
    	
		this.setClipChildren(false);
		this.setClipToPadding(false);

		super.dispatchDraw(canvas);
    }
    
    public int getOffsetY()
    {
    	return mOffsetY;
    }
    
    private boolean isTouched = false;
    
    private AdapterWrapper mAdapterWrapper;

    private int mOffsetY;
    private int mTouchDownPos;
    
    // positions of the last DOWN touch event
    private int mLastDownX;
    private int mLastDownY;
    
    private int mLastOffsetDownY;
    
    private final int ListItemHeight = 30;

}

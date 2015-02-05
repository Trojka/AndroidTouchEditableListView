package be.trojkasoftware.android.toucheditablelistview;

import be.trojkasoftware.android.touch.TouchDataStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class TouchEditableStateManager {

    private static final String TAG = "TouchEditableStateManager";

    public TouchEditableStateManager(TouchEditableListView listView, TouchEditableActionView actionHeader, TouchEditableActionView actionFooter)
    {
    	mListView = listView;
    	mActionHeader = actionHeader;
    	mActionFooter = actionFooter;
    }

	public boolean onTouchEvent(MotionEvent event) {
		mTouchDataStore.add(event);
		
        int action = event.getActionMasked();
    	int pointerIndex = event.getActionIndex();
    	int pointerId = event.getPointerId(pointerIndex);    	

    	//Log.i(TAG, "onTouchEvent Action[" + action + "][PointerIndex:" + pointerIndex + "][PointerId:" + pointerId + "]X["+(int)event.getX(pointerIndex)+"]Y["+(int)event.getY(pointerIndex)+"]");
    	
        switch (action) {
        case MotionEvent.ACTION_DOWN:
    	case MotionEvent.ACTION_POINTER_DOWN:
        	Log.i(TAG, "onTouchEvent ACTION_DOWN[PointerIndex:" + pointerIndex + "][PointerId:" + pointerId + "]X["+(int)event.getX(pointerIndex)+"]Y["+(int)event.getY(pointerIndex)+"]");
    		//Log.i(TAG, "onTouchEvent: Action[ACTION_DOWN]FirstVisiblePosition[" + mListView.getFirstVisiblePosition() + "]ChildAtIndexTag[" + mListView.getChildAt(0).getTag() + "]ChildAtIndexTop[" + mListView.getChildAt(0).getTop() + "]");
    		//Log.i(TAG, "onTouchEvent: Action[ACTION_DOWN]LastVisiblePosition[" + mListView.getLastVisiblePosition() + "]ChildAtEndTag[" + mListView.getChildAt(mListView.getChildCount()-1).getTag() + "]ChildAtEndBottom[" + mListView.getChildAt(mListView.getChildCount()-1).getBottom() + "]Height[" + mListView.getHeight() + "]");

        	int currentTouchPosition = mListView.pointToPosition((int)event.getX(pointerIndex), (int)event.getY(pointerIndex));
//    		Log.i(TAG, "onTouchEvent: Action[ACTION_DOWN]CurrentTouchPosition[" + currentTouchPosition + "]TopItemPosition[" + mTopItemPosition + "]BottomItemPosition[" + mBottomItemPosition + "]");
        	
            if(pointerIndex == 0) {

                Log.i(TAG, "onTouchEvent: Action[ACTION_DOWN] First touch: we always consider this TOP");

                mTopItemPosition = currentTouchPosition;
                mPointerIdTop = pointerId;
                Log.i(TAG, "onTouchEvent: Action[ACTION_DOWN] PointerIdTop[" + mPointerIdTop + "]");

            }
            if(pointerIndex == 1 && mState == STATE_NORMAL) {

                Log.i(TAG, "onTouchEvent: Action[ACTION_DOWN] Second touch");

                if(Math.abs(currentTouchPosition - mTopItemPosition) != 1) {
                    Log.i(TAG, "onTouchEvent: Action[ACTION_DOWN] Touched items are not consecutive: nothing to do");
                }
                else if(currentTouchPosition > mTopItemPosition) {
                    Log.i(TAG, "onTouchEvent: Action[ACTION_DOWN] Current is BOTTOM: no need to switch");
                    mBottomItemPosition = currentTouchPosition;
                    mPointerIdBottom = pointerId;

                    Log.i(TAG, "onTouchEvent: Action[ACTION_DOWN] PointerIdTop[" + mPointerIdTop + "] PointerIdBottom[" + mPointerIdBottom + "]");

                    mState = STATE_INSERT_INBETWEEN;
                }
                else {
                    Log.i(TAG, "onTouchEvent: Action[ACTION_DOWN] Current is TOP: switch with previous top");
                    mBottomItemPosition = mTopItemPosition;
                    mPointerIdBottom = mPointerIdTop;
                    mTopItemPosition = currentTouchPosition;
                    mPointerIdTop = pointerId;

                    Log.i(TAG, "onTouchEvent: Action[ACTION_DOWN] PointerIdTop[" + mPointerIdTop + "] PointerIdBottom[" + mPointerIdBottom + "]");

                    mState = STATE_INSERT_INBETWEEN;
                }
            }

    		Log.i(TAG, "onTouchEvent: Action[ACTION_DOWN]TopItemPosition[" + mTopItemPosition + "]BottomItemPosition[" + mBottomItemPosition + "]");

    		View v = mListView.getChildAt(mTopItemPosition - mListView.getFirstVisiblePosition());
        	mOriginalTopItemY = v.getTop();
        	mOriginalTopItemHeight = v.getMeasuredHeight();

            String viewTag = "NO_TAG";
            if(v.getTag() != null) {
                viewTag = (String)v.getTag();
            }

            Log.i(TAG, "onTouchEvent: Action[ACTION_DOWN]View[" + viewTag + "]OriginalTopItemY[" + mOriginalTopItemY + "]OriginalTopItemHeight[" + mOriginalTopItemHeight + "]");
    		
    		mInsertAtYStart = Integer.MAX_VALUE;
        	
        	break;
    	case MotionEvent.ACTION_MOVE:
        	
        	int pointerCount = event.getPointerCount();

    		// we only start doing something if at least one pointer moved mMinimalDirectionalMovement pixels from its touchdown position
        	int topPointerDeviation = 0;
        	int bottomPointerDeviation = 0;
        	for(int i = 0; i < pointerCount; i++) {
//        		if (mFirstPointerId == event.getPointerId(i)) {
//		        	//mFirstPointerMoveX = (int)event.getX(pointerIndex);
//		        	//mFirstPointerMoveY = (int)event.getY(pointerIndex);
//        		}
				if (mPointerIdTop == event.getPointerId(i)) {
					topPointerDeviation = Math.abs(mTouchDataStore.get(mTouchDataStore.getPointerId(mTouchDataStore.size() - 1)).posYOnPointerDown.get(mPointerIdTop)
                            - (int) event.getY(i));
				}
				if (mPointerIdBottom == event.getPointerId(i)) {
					bottomPointerDeviation = Math.abs(mTouchDataStore.get(mTouchDataStore.getPointerId(mTouchDataStore.size() - 1)).posYOnPointerDown.get(mPointerIdBottom)
                            - (int) event.getY(i));
				}
        	}

        	if(topPointerDeviation < mMinimalDirectionalMovement
        			&& bottomPointerDeviation < mMinimalDirectionalMovement) {
        		return false;
        	}
        	
        	if (mInitialMainDirection == DIRECTION_NONE && getMainDirection(mTouchDataStore.getPointerId(0)) == DIRECTION_NONE) {
        		return false;
        	}
        	else if (mInitialMainDirection == DIRECTION_NONE) {
        		mInitialMainDirection = getMainDirection(mTouchDataStore.getPointerId(0));
        	}
    		
    		if(mState == STATE_INSERT_AT_BOTTOM) {
	        	
    			if(mInsertAtYStart > mTouchDataStore.get(mPointerIdTop).getMoveY(0) /*mLastTopMoveY*/) {
		        	int stretch = Math.min((int)(mElasticity * Math.abs(mInsertAtYStart - mTouchDataStore.get(mPointerIdTop).getMoveY(0) /*mLastTopMoveY*/)), mMaxStretchHeight);
		        	// the header will stretch downward, so will have to scroll upward to compensate
		        	int newFooterTop = mInitialFooterTop - stretch;
		        	mListView.setSelectionFromTop(mListView.getCount()-1, newFooterTop);
		        	// start stretching the header
//		    		Log.i(TAG, "onTouchEvent: Action[ACTION_MOVE:UP:STRETCH]InsertAtYStart[" + mInsertAtYStart + "]LastTopMoveY[" + mLastTopMoveY + "]NewFooterTop[" + newFooterTop + "]Stretch[" + stretch + "]");
		    		if(!mActionFooter.isSet()) {
		    			View insertView = mInsertViewFactory.getInsertView(mListView.getCount() - 1 - (mListView.getFooterViewsCount() + mListView.getHeaderViewsCount()), -1);
		    			mActionFooter.setInsertView(insertView, mInsertStretchFactor);
		    		}
		    		mActionFooter.setStretch(stretch);
		    		mActionFooter.requestLayout();
		    		
		    		mIsInserting = mActionHeader.isInserting();
	        		
	        		return false;
    			}
    			
    			// we've moved passed our startpoint, so we're back to normal
    			mIsInserting = false;
	    		mActionFooter.setStretch(0);
	    		mActionFooter.requestLayout();
    			mState = STATE_NORMAL;
        	}
    		else if(mState == STATE_INSERT_AT_TOP) {
	        	
    			if(mInsertAtYStart < mTouchDataStore.get(mPointerIdTop).getMoveY(0) /*mLastTopMoveY*/) {
		        	// start stretching the header
		        	int stretch = Math.min((int)(mElasticity * Math.abs(mInsertAtYStart - mTouchDataStore.get(mPointerIdTop).getMoveY(0) /*mLastTopMoveY*/)), mMaxStretchHeight);
//		    		Log.i(TAG, "onTouchEvent: Action[ACTION_MOVE:DOWN:STRETCH]InsertAtYStart[" + mInsertAtYStart + "]LastTopMoveY[" + mLastTopMoveY + "]Stretch[" + stretch + "]");
		    		if(!mActionHeader.isSet()) {
		    			View insertView = mInsertViewFactory.getInsertView(-1, 0);
		    			mActionHeader.setInsertView(insertView, mInsertStretchFactor);
		    		}
		    		mActionHeader.setStretch(stretch);
		    		mActionHeader.requestLayout();
		    		
		    		mIsInserting = mActionHeader.isInserting();
		        	
		        	return false;
    			}
    			
    			// we've moved passed our startpoint, so we're back to normal
    			mIsInserting = false;
	    		mActionHeader.setStretch(0);
	    		mActionHeader.requestLayout();
    			mState = STATE_NORMAL;
        	}
    		else if(mState == STATE_INSERT_INBETWEEN) {
                Log.i(TAG, "onTouchEvent.ACTION_MOVE.STATE_INSERT_INBETWEEN");

    			mCurrentTopItemY = mOriginalTopItemY; 
    			// if we can show all the items in the arrayadapter at once
    			//		and the last item is not beyond the bottom of the listview
    			//	then there is no need to scroll
            	if(isScrollable()) {
                    int lastTopMoveY = mTouchDataStore.get(mPointerIdTop).getMoveY(0);
                    if(mTouchDataStore.size() != 2)
                    {
                        throw new AssertionError("Size must be 2");
                    }
                    if(mTouchDataStore.get(mTouchDataStore.getPointerId(mTouchDataStore.size() - 1)) == null)
                    {
                        throw new AssertionError("PointerId must have entry");
                    }
                    if(mTouchDataStore.get(mTouchDataStore.getPointerId(mTouchDataStore.size() - 1)).posYOnPointerDown == null)
                    {
                        throw new AssertionError("PointerId must have history");
                    }
                    if(!mTouchDataStore.get(mTouchDataStore.getPointerId(mTouchDataStore.size() - 1)).posYOnPointerDown.containsKey(mPointerIdTop))
                    {
                        throw new AssertionError("PointerId must have history for pointeridtop");
                    }
                    int topDownYOnSecondTouch = mTouchDataStore.get(mTouchDataStore.getPointerId(mTouchDataStore.size() - 1)).posYOnPointerDown.get(mPointerIdTop);
                    Log.i(TAG, "onTouchEvent.ACTION_MOVE.isScrollable "
                            // the position of the top pointer during this move event
                            + "PointerTopMoveY[" + lastTopMoveY + "]"
                            // the position of the top pointer when the last pointer went down
                            + "PointerTopDownYOnSecondTouch[" + topDownYOnSecondTouch + "]");
            		mCurrentTopItemY = mOriginalTopItemY +
                            Math.min((int)(mElasticity * (
                                    lastTopMoveY /*mLastTopMoveY*/ - topDownYOnSecondTouch /*mTopDownYOnSecondTouch*/))
                                    , mMaxStretchHeight / 2);
            	}
            	mListView.setSelectionFromTop(mTopItemPosition, mCurrentTopItemY);
            	Log.i(TAG, "onTouchEvent.ACTION_MOVE.SetSelectionFromTop V0TopY["+mOriginalTopItemY+"]"
                        //+ "LastTopMoveY["+mLastTopMoveY+"]"
                        //+ "TopDownY["+mTopDownYOnSecondTouch+"]"
                        + "[CurrentTopItemY:"+mCurrentTopItemY+"]");
    
            	TouchEditableItemView topItemView = (TouchEditableItemView)mListView.getChildAt(mTopItemPosition - mListView.getFirstVisiblePosition());
            	if(topItemView != null)
            	{
		    		if(!topItemView.isSet()) {
		    			View insertView = mInsertViewFactory.getInsertView(mTopItemPosition - mListView.getFooterViewsCount(), mBottomItemPosition - mListView.getFooterViewsCount());
		    			topItemView.setActionView(insertView, mInsertStretchFactor, Gravity.CENTER);
		    		}
		    		
		    		mIsInserting = topItemView.isShowingInsertView();
            		
//            		Log.i(TAG, "onTouchEvent.ACTION_MOVE.AdjustItem[TopItemPosition:" + mTopItemPosition + "]");
            		// We calculate the stretch size here and store it
            		// A layoutrequest can cause the childviews of the listviews (thus the items in the list) to be destroyed
            		//	and recreated. In that case we lose our information to be able to calculate the stretch-size
            		mCurrentTopItemStretch = calculateCurrentTopItemStretch(mTopItemPosition, topItemView);
            		adjustItem(mTopItemPosition, topItemView, true);
            		mListView.requestLayout();
            	} 
            	else
            	{
//            		Log.i(TAG, "onTouchEvent.ACTION_MOVE.GetChildAt[TopItemPosition:"+mTopItemPosition+"] is NULL");
            	}
    		}
        	
        	if(mTouchDataStore.get(mPointerIdTop).getMoveY(0) /*mLastTopMoveY*/ <= mTouchDataStore.get(mPointerIdTop).posYDown /*mPreviousTopMoveY*/ 
        			&& getMainDirection(mPointerIdTop) == DIRECTION_VERTICAL 
        			&& mState == STATE_NORMAL) {
//        		Log.i(TAG, "onTouchEvent: Action[ACTION_MOVE:UP]LastVisiblePosition[" + mListView.getLastVisiblePosition() + "]ChildAtEndTag[" + mListView.getChildAt(mListView.getChildCount()-1).getTag() + "]ChildAtEndBottom[" + mListView.getChildAt(mListView.getChildCount()-1).getBottom() + "]Height[" + mListView.getHeight() + "]");
        		// we are moving up,
        		// are we at the bottom of the control? if not, let standard handle this
	    		// 	we are not yet at the bottom 
	    		//		if the last visible position is not the footer (mind that the size of the adapter actually accounts for the header and footer !!!
	        	if((mListView.getLastVisiblePosition() < mListView.getCount()-1)
		        				) {
//	        		Log.i(TAG, "onTouchEvent: Action[ACTION_MOVE:UP:NOT_AT_BOTTOM]Count[" + mListView.getCount() + "]LastVisiblePosition[" + mListView.getLastVisiblePosition() + "]ChildAtEndTag[" + mListView.getChildAt(mListView.getChildCount()-1).getTag() + "]ChildAtEndBottom[" + mListView.getChildAt(mListView.getChildCount()-1).getBottom() + "]");
	        		return true;
	        	}
	        	
	        	// if so start insertion
	        	mState = STATE_INSERT_AT_BOTTOM;
	        	
	        	// initialize our startposition
        		mInsertAtYStart = mTouchDataStore.get(mPointerIdTop).getMoveY(0) /*mLastTopMoveY*/;
        		mInitialFooterTop = mListView.getChildAt(mListView.getChildCount()-1).getTop();
        		
//        		Log.i(TAG, "onTouchEvent: Action[ACTION_MOVE:UP:INIT_STARTY]InsertAtYStart[" + mInsertAtYStart + "]LastVisiblePosition[" + mListView.getLastVisiblePosition() + "]ChildAtEndTag[" + mListView.getChildAt(mListView.getChildCount()-1).getTag() + "]AbsChildAtEndBottom[" + Math.abs(mListView.getChildAt(mListView.getChildCount()-1).getBottom()) + "]");
	        		
        	}
        	
        	if(mTouchDataStore.get(mPointerIdTop).getMoveY(0) /*mLastTopMoveY*/ >= mTouchDataStore.get(mPointerIdTop).posYDown /*mPreviousTopMoveY*/ 
        			&& getMainDirection(mPointerIdTop) == DIRECTION_VERTICAL 
        			&& mState == STATE_NORMAL) {
//        		Log.i(TAG, "onTouchEvent: Action[ACTION_MOVE:DOWN]FirstVisiblePosition[" + mListView.getFirstVisiblePosition() + "]ChildAtStartTag[" + mListView.getChildAt(0).getTag() + "]ChildAtStartTop[" + mListView.getChildAt(0).getTop() + "]");
	        	// we are moving down, 
	        	// are we at the top of the control? if not, let standard handle this
	    		// 	we are not yet at the top 
	    		//		if the first visible position is not the header
	        	if((mListView.getFirstVisiblePosition() != 0)
	        				) {
//	        		Log.i(TAG, "onTouchEvent: Action[ACTION_MOVE:DOWN:NOT_AT_TOP]FirstVisiblePosition[" + mListView.getFirstVisiblePosition() + "]ChildAtStartTag[" + mListView.getChildAt(0).getTag() + "]ChildAtStartTop[" + mListView.getChildAt(0).getTop() + "]");
	        		return true;
	        	}
	        	
	        	// if so start insertion
	        	mState = STATE_INSERT_AT_TOP;
	        	
	        	// initialize our startposition
        		mInsertAtYStart = mTouchDataStore.get(mPointerIdTop).getMoveY(0) /*mLastTopMoveY*/;
        		
//        		Log.i(TAG, "onTouchEvent: Action[ACTION_MOVE:DOWN:INIT_STARTY]InsertAtYStart[" + mInsertAtYStart + "]FirstVisiblePosition[" + mListView.getFirstVisiblePosition() + "]ChildAtStartTag[" + mListView.getChildAt(0).getTag() + "]AbsChildAtStartTop[" + Math.abs(mListView.getChildAt(0).getTop()) + "]");
        	}
        	
        	if((mTouchDataStore.get(mPointerIdTop).posXDown - mTouchDataStore.get(mPointerIdTop).getMoveX(0)) > (mListView.getWidth() / 2)
        			&& getMainDirection(mPointerIdTop) == DIRECTION_HORIZONTAL 
        			&& mState == STATE_NORMAL) {
        		
//            	TouchEditableItemView topItemView = (TouchEditableItemView)mListView.getChildAt(mTopItemPosition - mListView.getFirstVisiblePosition());
//            	if(topItemView != null)
//            	{
//		    		if(!topItemView.isSet()) {
//		    			View deleteView = mDeleteViewFactory.getDeleteView(mTopItemPosition - mListView.getFooterViewsCount());
//		    			topItemView.setActionView(deleteView, 0, Gravity.RIGHT);
//		    		}
//
//            		mListView.requestLayout();
//            	} 
    			
        		mState = STATE_DELETE;
        	}
        	
        	break;
    	case MotionEvent.ACTION_UP:
    	case MotionEvent.ACTION_POINTER_UP:
        	//mNumberOfPointersDown--;
        	
    		if(mState == STATE_INSERT_AT_BOTTOM) {
    			if(mIsInserting && mInsertListener != null) {
    				mInsertListener.insert(mListView.getCount() - 1 - (mListView.getFooterViewsCount() + mListView.getHeaderViewsCount()), -1);
    				mListView.invalidate();
    			}
    			
	    		mActionFooter.reset();
	    		mActionFooter.requestLayout();
	    		
	        	mState = STATE_NORMAL;
    		}
    		else if (mState == STATE_INSERT_AT_TOP) {
    			if(mIsInserting && mInsertListener != null) {
    				mInsertListener.insert(-1, 0);
    				mListView.invalidate();
    			}
    			
	    		mActionHeader.reset();
	    		mActionHeader.requestLayout();
	    		
	        	mState = STATE_NORMAL;
    		}
    		else if (mState == STATE_INSERT_INBETWEEN) {            	
    			if(mIsInserting && mInsertListener != null) {
    				mInsertListener.insert(mTopItemPosition - mListView.getFooterViewsCount(), mBottomItemPosition - mListView.getFooterViewsCount());
    				mListView.invalidate();
    			}
    			
            	TouchEditableItemView topItemView = (TouchEditableItemView)mListView.getChildAt(mTopItemPosition - mListView.getFirstVisiblePosition());
            	if(topItemView != null)
            	{
            		topItemView.reset();
            	}
            	
            	mListView.invalidateViews();
            	mListView.setSelectionFromTop(mTopItemPosition, mOriginalTopItemY);
        		
            	mState = STATE_NORMAL;
    		}
    		else if (mState == STATE_DELETE) {
        		mDeleteListener.delete(mTopItemPosition - mListView.getFooterViewsCount());
				mListView.invalidate();
	    		
	        	mState = STATE_NORMAL;
    		}
        	
    		mTouchDataStore.clean();
    		
//    		VOLGENDE SECTIENOG EENS BEKIJKEN !!!
//    		SWITCHEN VAN TOP EN BOTTOM HIER KAN GEVOLGEN HEBBEN VOOR ANDERE VARIABELEN ZOALS mOriginalTopItemY
    		
    		if (mTouchDataStore.size() == 1) {
	    		mTopItemPosition = INVALID_POINTERID;
    		}
    		else if (mTouchDataStore.size() == 1) {
	    		mTopItemPosition = INVALID_POINTERID;
	    		mBottomItemPosition = INVALID_POINTERID;
    		}
    		
//        	Log.i(TAG, "onTouchEvent ACTION_UP[PointerIndex:" + pointerIndex + "][PointerId:" + pointerId + "]");
        	break;
    	case MotionEvent.ACTION_OUTSIDE:
//        	Log.i(TAG, "onTouchEvent ACTION_OUTSIDE[PointerIndex:" + pointerIndex + "][PointerId:" + pointerId + "]");
        	break;
        }
		
  		return true;
	}
	
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return false;
	}
	
	public int getState()
	{
		return mState;
	}
	
	public int getMainDirection(int pointerId)
	{
		//Log.i(TAG, "getMainDirection"
		//		+ "[FirstPointerDownX:" + mTouchDataStore.get(pointerId /*mFirstPointerId*/).posXDown //mFirstPointerDownX
		//		+ "][FirstPointerMoveX:" + mTouchDataStore.get(pointerId /*mFirstPointerId*/).getMoveX(pointerId) //mFirstPointerMoveX
		//		+ "][FirstPointerDownY:" + mTouchDataStore.get(pointerId /*mFirstPointerId*/).posYDown //mFirstPointerDownY
		//		+ "][FirstPointerMoveY:" + mTouchDataStore.get(pointerId /*mFirstPointerId*/).getMoveY(pointerId) //mFirstPointerMoveY
		//		+ "]");

		if (Math.abs(mTouchDataStore.get(pointerId).posXDown - mTouchDataStore.get(pointerId).getMoveX(pointerId))
				 > Math.abs(mTouchDataStore.get(pointerId).posYDown - mTouchDataStore.get(pointerId).getMoveY(pointerId))) {
			//Log.i(TAG, "getMainDirection: DIRECTION_HORIZONTAL");
			return DIRECTION_HORIZONTAL;
		}
		
		if (Math.abs(mTouchDataStore.get(pointerId).posYDown - mTouchDataStore.get(pointerId).getMoveY(pointerId))
				 > Math.abs(mTouchDataStore.get(pointerId).posXDown - mTouchDataStore.get(pointerId).getMoveX(pointerId))) {
			//Log.i(TAG, "getMainDirection: DIRECTION_VERTICAL");
			return DIRECTION_VERTICAL;
		}
		
		//Log.i(TAG, "getMainDirection: DIRECTION_NONE");
		return DIRECTION_NONE;
	}
	
	public int getCurrentTopItemY() {
		return mCurrentTopItemY;
	}
	
	public int calculateCurrentTopItemStretch(int position, View v)
	{
    	int newHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    	int stretchHeight = 0;
    	if(isScrollable())
    	{
    		stretchHeight = Math.abs(mTouchDataStore.get(mPointerIdBottom).getMoveY(0) /*mLastBottomMoveY*/ - mTouchDataStore.get(mPointerIdTop).getMoveY(0) /*mLastTopMoveY*/);
    	}
    	else
    	{
    		stretchHeight = Math.abs(mTouchDataStore.get(mPointerIdBottom).posYOnPointerDown.get(mTouchDataStore.getPointerId(mTouchDataStore.size() - 1)) /*/*mBottomDownYOnSecondTouch*/ - mTouchDataStore.get(mPointerIdBottom).getMoveY(0) /*mLastBottomMoveY*/);
    	}
//		Log.i(TAG, "getCurrentTopItemStretch[Indx:" + position + "][OriginalTopItemHeight:" + mOriginalTopItemHeight + "][TopItemHeightPortion:" + topItemHeightPortion + "][BottomItemHeightPortion" + bottomItemHeightPortion + "]");
		if(position == mTopItemPosition && (stretchHeight > mOriginalTopItemHeight))
		{
			// the touched item will stretch
			newHeight = mOriginalTopItemHeight
					+ Math.min((int)(mElasticity * (stretchHeight - mOriginalTopItemHeight)), mMaxStretchHeight);
//    		Log.i(TAG, "getCurrentTopItemStretch[Indx:" + position + "][StretchHeight:" + newHeight + "][MeasuredHeight" + v.getMeasuredHeight() + "]");
		}
		else
		{
			// everything else remains as is
			newHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
//    		Log.i(TAG, "getCurrentTopItemStretch[Indx:" + position + "][SetHeight:WRAP_CONTENT]");
		}
		
		return newHeight;
	}
	
	public int getOriginalTopItemHeight()
	{
		return mOriginalTopItemHeight;
	}
	
	public int getCurrentTopItemStretch()
	{
		return mCurrentTopItemStretch;
	}
	
	public void adjustItem(int position, View v, boolean withLogging)
	{
        ViewGroup.LayoutParams lp = v.getLayoutParams();
    	if(mState != STATE_INSERT_INBETWEEN)
    	{
            // we have no offset, draw everything as normal
//    		if(withLogging)
//    			Log.i(TAG, "adjustItem[Indx:" + position + "][State.NOT:STATE_INSERT_INBETWEEN][SetHeight:WRAP_CONTENT]");
        	lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        	return;
    	}
    	
		if(position == mTopItemPosition)
		{
			// the touched item will stretch
			lp.height = mCurrentTopItemStretch;
		}
		else
		{
			// everything else remains as is
			lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		}
		
		v.setLayoutParams(lp);

    }
	
	public boolean canLayout() {
		return mCanLayout;
	}	
    
    public void setInsertViewFactory(TouchEditableListView.InsertViewFactory factory) {
    	mInsertViewFactory = factory;
    }
    
    public void setOnInsertListener(TouchEditableListView.OnInsertListener insertListener) {
    	mInsertListener = insertListener;
    }
    
    public void setDeleteViewFactory(TouchEditableListView.DeleteViewFactory factory) {
    	mDeleteViewFactory = factory;
    }
    
    public void setOnDeleteListener(TouchEditableListView.OnDeleteListener deleteListener) {
    	mDeleteListener = deleteListener;
    }

	private boolean isScrollable()
	{
		// if we cannot show all the items in the arrayadapter at once
		//		or the last item is beyond the bottom of the listview
		mCanLayout = false;

//		Log.i(TAG, "MustCompensateForTop[NormalizedCount:" + (mListView.getCount() - (mListView.getHeaderViewsCount() + mListView.getFooterViewsCount())) + "]"
//		+ "[ChildCount:" + mListView.getChildCount() + "]"
//		+ "[Bottom:" + mListView.getChildAt(mListView.getChildCount()-1).getBottom() + "]"
//		+ "[Height:" + mListView.getHeight() + "]"
//		);
		
		boolean result = (mListView.getCount() - (mListView.getHeaderViewsCount() + mListView.getFooterViewsCount()) > mListView.getChildCount()
    			|| (mListView.getChildCount() > 0 
    					&& Math.abs(mListView.getChildAt(mListView.getChildCount()-1).getBottom() - mListView.getHeight()) > 5 ));
		
		mCanLayout = true;
		
		return result;
	}

	private TouchEditableListView mListView;
	
	public static int STATE_NORMAL = 0;
	public static int STATE_UNSUPPORTED = 1;
	public static int STATE_INSERT_AT_TOP = 2;
	public static int STATE_INSERT_AT_BOTTOM = 3;
	public static int STATE_INSERT_INBETWEEN = 4;
	public static int STATE_DELETE = 5;
	private int mState = STATE_NORMAL;
	
	public static int DIRECTION_NONE = 0;
	public static int DIRECTION_HORIZONTAL = 1;
	public static int DIRECTION_VERTICAL = 2;
	
	private int mInitialMainDirection = DIRECTION_NONE;
    
    private int mMinimalDirectionalMovement = 20;
//    private int mMaximalPerpendicularMovement = 10;
    
    private TouchDataStore mTouchDataStore = new TouchDataStore();

	private boolean mIsInserting = false;
    
    private int mPointerIdTop = -1;
    private int mPointerIdBottom = -1;
    
    private int mInsertAtYStart = 0;

    private int mOriginalTopItemY = 0;
    private int mCurrentTopItemY = 0;
    private int mOriginalTopItemHeight;
    private int mCurrentTopItemStretch = ViewGroup.LayoutParams.WRAP_CONTENT;
    
    private static int INVALID_POINTERID = -1;
    private int mTopItemPosition = INVALID_POINTERID;
    private int mBottomItemPosition = INVALID_POINTERID;
    
    private TouchEditableActionView mActionHeader;
    private TouchEditableActionView mActionFooter;
    
    private int mInitialFooterTop = 0;

    private boolean mCanLayout = true;
    
    private double mElasticity = 0.5; 	// must be less than 1.0
    private double mInsertStretchFactor = 1.25;
    private double mMaxStretchFactor = 1.5;
    private int mMaxStretchHeight = 180; 
    
    private TouchEditableListView.InsertViewFactory mInsertViewFactory;
    private TouchEditableListView.OnInsertListener mInsertListener;
    
    private TouchEditableListView.DeleteViewFactory mDeleteViewFactory;
    private TouchEditableListView.OnDeleteListener mDeleteListener;
}

package be.trojkasoftware.android.touch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.view.MotionEvent;

public class TouchDataStore {
	public void add(MotionEvent event) {
        int action = event.getActionMasked();
    	int pointerCount = event.getPointerCount();
    	for(int i = 0; i < pointerCount; i++) {
    		int pointerId = event.getPointerId(i);
    		if(!mTouchStore.containsKey(pointerId)){
    			TouchData touchData = new TouchData(mHistorySize);
//    			touchData.historyPosXMove = new ArrayList(mHistorySize);
//    			touchData.historyPosYMove = new ArrayList(mHistorySize);
    			mTouchStore.put(pointerId, touchData);
    		}
            switch (action) {
            case MotionEvent.ACTION_DOWN:
        	case MotionEvent.ACTION_POINTER_DOWN:
                if(!mPointerDownOrder.contains(pointerId))
            		mPointerDownOrder.add(pointerId);
        		
            	mTouchStore.get(pointerId).posXDown = (int)event.getX(i);
            	mTouchStore.get(pointerId).posYDown = (int)event.getY(i);
            	mTouchStore.get(pointerId).pushMove(mTouchStore.get(pointerId).posXDown, mTouchStore.get(pointerId).posYDown);
            	//mTouchStore.get(pointerId).lastPosXMove = mTouchStore.get(pointerId).posXDown;
            	//mTouchStore.get(pointerId).lastPosYMove = mTouchStore.get(pointerId).posYDown;
            	for(Entry<Integer, TouchData> entry : mTouchStore.entrySet()){
            		mTouchStore.get(pointerId).posXOnPointerDown.put(entry.getKey(), mTouchStore.get(entry.getKey()).getMoveX(0));
            		mTouchStore.get(pointerId).posYOnPointerDown.put(entry.getKey(), mTouchStore.get(entry.getKey()).getMoveY(0));
            	}
            	break;
        	case MotionEvent.ACTION_MOVE:
            	mTouchStore.get(pointerId).pushMove((int)event.getX(i), (int)event.getY(i));
            	//mTouchStore.get(pointerId).lastPosXMove = (int)event.getX(i);
            	//mTouchStore.get(pointerId).lastPosYMove = (int)event.getY(i);
            	break;
        	case MotionEvent.ACTION_UP:
        	case MotionEvent.ACTION_POINTER_UP:
            	mTouchStore.get(pointerId).posXUp = (int)event.getX(i);
            	mTouchStore.get(pointerId).posYUp = (int)event.getY(i);
            	break;
        	case MotionEvent.ACTION_OUTSIDE:
            	break;
            }
    	}		
	}
	
	public Set<Integer> getPointers() {
		return mTouchStore.keySet();
	}
	
	public TouchData get(int pointerId) {
		return mTouchStore.get(pointerId);
	}
	
	public int getPointerId(int index) {
		return mPointerDownOrder.get(index);
	}
	
	public int size() {
		return mTouchStore.size();
	}
	
	public void remove(int pointerId) {
		if (mTouchStore.containsKey(pointerId)) {
			mTouchStore.remove(pointerId);
		}
	}
	
	public void clean() {
		List<Integer> mInvalidPointerList = new ArrayList<Integer>();
    	for(Entry<Integer, TouchData> entry : mTouchStore.entrySet()){
    		if(!entry.getValue().isValid())
    		{
    			mInvalidPointerList.add(entry.getKey());
    		}
    	}
    	
    	mTouchStore.keySet().removeAll(mInvalidPointerList);
    	mPointerDownOrder.removeAll(mInvalidPointerList);
	}
	
	public void clear() {
		mTouchStore.clear();
		mPointerDownOrder.clear();
	}
	
	public void setHistorySize(int size) {
		mHistorySize = size;
	}
	
	private Map<Integer, TouchData> mTouchStore = new HashMap<Integer, TouchData>();
	private List<Integer> mPointerDownOrder = new ArrayList<Integer>();
	private int mHistorySize = 2;
}

package be.trojkasoftware.android.toucheditablelistview;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class TouchEditableListView extends ListView {

    private static final String TAG = "TouchEditableListView";

    public interface InsertViewFactory {
        public View getInsertView(int after, int before);
    }
    
    public interface OnInsertListener {
    	public void insert(int after, int before);
    }

    public interface DeleteViewFactory {
        public View getDeleteView(int position);
    }
    
    public interface OnDeleteListener {
    	public void delete(int position);
    }    
    
	public TouchEditableListView(Context context) {
		super(context);
    	
		this.setClipChildren(false);
		this.setClipToPadding(false);
		
		
		mActionHeader = new TouchEditableActionView(context, TouchEditableActionView.TYPE_HEADER);
		mActionHeader.setTag("header");
		this.addHeaderView(mActionHeader);
		
		mActionFooter = new TouchEditableActionView(context, TouchEditableActionView.TYPE_FOOTER);
		mActionFooter.setTag("footer");
		this.addFooterView(mActionFooter);
		
		mStateManager = new TouchEditableStateManager(this, mActionHeader, mActionFooter);
		
	}

    private class AdapterWrapper extends BaseAdapter {

        private static final String TAG = "AdapterWrapper";
    	
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
    		//Log.i(AdapterWrapper.TAG, "getView: Position[" + position + "]");

        	TouchEditableItemView v;
            View child;
            if (convertView != null) {
                v = (TouchEditableItemView) convertView;
                v.setTag("postion:" + position);
                View oldChild = v.getChildAt(0);

            	child = mAdapter.getView(position, oldChild, TouchEditableListView.this);
                
                if (child != oldChild) {
                    if (oldChild != null) {
                        v.removeViewAt(0);
                    }
                    if(child != null) {
                    	v.addView(child);
                    }
                }
            } else {
            	child = mAdapter.getView(position, null, TouchEditableListView.this);

                v = new TouchEditableItemView(getContext(), TouchEditableItemView.TYPE_ITEM);
                v.setTag("postion:" + position);

                v.setLayoutParams(new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                v.addView(child);
            }
            
            // also compensate for the added header
            TouchEditableListView.this.mStateManager.adjustItem(position + 1, v, false);

            return v;
        }

    }
	
    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter != null) {
        	
        	if(adapter instanceof InsertViewFactory) {
        		setInsertViewFactory((InsertViewFactory)adapter);
        	}
        	
        	if(adapter instanceof OnInsertListener) {
        		setOnInsertListener((OnInsertListener)adapter);
        	}
        	
        	if(adapter instanceof DeleteViewFactory) {
        		setDeleteViewFactory((DeleteViewFactory)adapter);
        	}
        	
        	if(adapter instanceof OnDeleteListener) {
        		setOnDeleteListener((OnDeleteListener)adapter);
        	}
        	
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
    
    public void setInsertViewFactory(InsertViewFactory factory) {
		mStateManager.setInsertViewFactory(factory);
    }
    
    public void setOnInsertListener(OnInsertListener insertListener) {
		mStateManager.setOnInsertListener(insertListener);
    }
    
    public void setDeleteViewFactory(DeleteViewFactory factory) {
		mStateManager.setDeleteViewFactory(factory);
    }
    
    public void setOnDeleteListener(OnDeleteListener deleteListener) {
		mStateManager.setOnDeleteListener(deleteListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
    	if(mStateManager.onTouchEvent(ev)) {
    		return super.onTouchEvent(ev);
    	}

    	return true;
    }
    
    @Override
    protected void layoutChildren() 
    {
    	if(!mStateManager.canLayout())
    		return;
    	
    	//Log.i(TAG, "layoutChildren.START");
        super.layoutChildren();
        //Log.i(TAG, "layoutChildren.END");
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	mStateManager.onInterceptTouchEvent(ev);
    	return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        final Drawable divider = getDivider();
        final int dividerHeight = getDividerHeight();

        if (divider != null && dividerHeight != 0 
        		&& mStateManager != null && mStateManager.getState() == TouchEditableStateManager.STATE_INSERT_INBETWEEN
        		&& mStateManager.getCurrentTopItemStretch() != 0) {
            int drawAtY = mStateManager.getCurrentTopItemY() + mStateManager.getOriginalTopItemHeight();
//            if (expItem != null) {
//                final int l = getPaddingLeft();
//                final int r = getWidth() - getPaddingRight();
//                final int t;
//                final int b;
//
//                final int childHeight = expItem.getChildAt(0).getHeight();
//
//                if (expPosition > mSrcPos) {
//                    t = expItem.getTop() + childHeight;
//                    b = t + dividerHeight;
//                } else {
//                    b = expItem.getBottom() - childHeight;
//                    t = b - dividerHeight;
//                }

                // Have to clip to support ColorDrawable on <= Gingerbread
                //canvas.save();
                //canvas.clipRect(l, t, r, b);
                divider.setBounds(0, drawAtY, getWidth(), drawAtY + dividerHeight);
                divider.draw(canvas);
                //canvas.restore();
//            }
        }
        
        
        
//        if(mStateManager == null)
//        	return;
//
//        //canvas.drawColor(Color.RED);
//        /* recreate the blue rectangle on the canvas */
//        Paint paint = new Paint();
//        paint.setStyle(Style.STROKE);
//        Path path = new Path();
//        path.moveTo(0.0f, 0.0f);        // Top Left
//        path.lineTo(0.0f, mStateManager.getPublishedY());      // Bottom Left
//        path.lineTo(10.0f, mStateManager.getPublishedY());     // Bottom Right
//        path.lineTo(10.0f, 0.0f);      // Top Right
//        path.close(); 
//        canvas.drawPath(path,paint);    
    }
    
    private boolean mIsTouched = false;
    
    private AdapterWrapper mAdapterWrapper;
    
    private TouchEditableStateManager mStateManager;
    
    private TouchEditableActionView mActionHeader;
    private TouchEditableActionView mActionFooter;
    
}

package com.blackbox.apps.karay.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.blackbox.apps.karay.R;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;



/*
 * Copied (with permission) from the super-awesome Dave Smith
 * https://github.com/devunwired/recyclerview-playground/blob/master/app/src/main/java/com/example/android/recyclerplayground/GridDividerDecoration.java
 */

public class GridDividerDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = {android.R.attr.listDivider};


    private Drawable mDivider;
    private int mInsets;

    public GridDividerDecoration(Context context) {
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        mDivider.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        a.recycle();

        mInsets = context.getResources().getDimensionPixelSize(R.dimen.margin_2);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVertical(c, parent);
        drawHorizontal(c, parent);
    }

    /**
     * Draw dividers at each expected grid interval
     */
    public void drawVertical(Canvas c, RecyclerView parent) {
        if (parent.getChildCount() == 0) return;

        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final View child = parent.getChildAt(0);
        if (child.getHeight() == 0) return;

        final RecyclerView.LayoutParams params =
                (RecyclerView.LayoutParams) child.getLayoutParams();
        int top = child.getBottom() + params.bottomMargin + mInsets;
        int bottom = top + mDivider.getIntrinsicHeight();

        final int parentBottom = parent.getHeight() - parent.getPaddingBottom();
        while (bottom < parentBottom) {
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);

            top += mInsets + params.topMargin + child.getHeight() + params.bottomMargin + mInsets;
            bottom = top + mDivider.getIntrinsicHeight();
        }
    }

    /**
     * Draw dividers to the right of each child view
     */
    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin + mInsets;
            final int right = left + mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //We can supply forced insets for each sortBy view here in the Rect
        outRect.set(mInsets, mInsets, mInsets, mInsets);
    }

    /*@Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int left = outRect.left;
        int right = outRect.right;
        int top = outRect.top;
        int bottom = outRect.bottom;
        int idx = parent.getChildPosition(view);
        int perRow = gridLayoutManager.getSpanCount();

        int adj = mInsets;

        if (idx < itemsPerRow) {
            // on first row, adjust top if needed
        }

        if(idx % perRow == 0){
            // on first column, adjust. Left magically adjusts bottom, so adjust it too...
            left += adj;
            bottom -= adj;
        }

        if(idx % itemsPerRow == perRow - 1){
            // on last column, adjust. Right magically adjusts bottom, so adjust it too...
            right += adjustment;
            bottom -= adjustment;
        }

        outRect.set(left, top, right, bottom);
    }*/
}
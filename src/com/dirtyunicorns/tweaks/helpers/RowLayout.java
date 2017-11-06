package com.dirtyunicorns.tweaks.helpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.android.settings.R;

public final class RowLayout extends ViewGroup {
    private static final int DEFAULT_HORIZONTAL_SPACING = 5;
    private static final int DEFAULT_VERTICAL_SPACING = 5;
    private final int horizontalSpacing;
    private final int verticalSpacing;
    private List<RowMeasurement> currentRows = Collections.emptyList();

    public RowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.RowLayout);
        horizontalSpacing = styledAttributes.getDimensionPixelSize(R.styleable.RowLayout_android_horizontalSpacing, DEFAULT_HORIZONTAL_SPACING);
        verticalSpacing = styledAttributes.getDimensionPixelSize(R.styleable.RowLayout_android_verticalSpacing, DEFAULT_VERTICAL_SPACING);
        styledAttributes.recycle();
    }

    @SuppressWarnings("RefusedBequest")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int maxInternalWidth = MeasureSpec.getSize(widthMeasureSpec) - getHorizontalPadding();
        int maxInternalHeight = MeasureSpec.getSize(heightMeasureSpec) - getVerticalPadding();
        @SuppressWarnings("AndroidLintDrawAllocation") List<RowMeasurement> rows = new ArrayList<RowMeasurement>();
        @SuppressWarnings("AndroidLintDrawAllocation") RowMeasurement currentRow = new RowMeasurement(maxInternalWidth, widthMode);
        rows.add(currentRow);
        for (View child : getLayoutChildren()) {
            LayoutParams childLayoutParams = child.getLayoutParams();
            int childWidthSpec = createChildMeasureSpec(childLayoutParams.width, maxInternalWidth, widthMode);
            int childHeightSpec = createChildMeasureSpec(childLayoutParams.height, maxInternalHeight, heightMode);
            child.measure(childWidthSpec, childHeightSpec);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            if (currentRow.wouldExceedMax(childWidth)) {
                currentRow = new RowMeasurement(maxInternalWidth, widthMode);
                rows.add(currentRow);
            }
            currentRow.addChildDimensions(childWidth, childHeight);
        }

        int longestRowWidth = 0;
        int totalRowHeight = 0;
        for (int index = 0; index < rows.size(); index++) {
            RowMeasurement row = rows.get(index);
            totalRowHeight += row.getHeight();
            if (index < (rows.size() - 1)) {
                totalRowHeight += verticalSpacing;
            }
            longestRowWidth = Math.max(longestRowWidth, row.getWidth());
        }
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? MeasureSpec.getSize(widthMeasureSpec) : (longestRowWidth + getHorizontalPadding()), (heightMode == MeasureSpec.EXACTLY) ? MeasureSpec.getSize(heightMeasureSpec) : (totalRowHeight + getVerticalPadding()));
        currentRows = Collections.unmodifiableList(rows);
    }

    private int createChildMeasureSpec(int childLayoutParam, int max, int parentMode) {
        int spec;
        if (childLayoutParam == LayoutParams.MATCH_PARENT) {
            spec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
        }
        else if (childLayoutParam == LayoutParams.WRAP_CONTENT) {
            spec = MeasureSpec.makeMeasureSpec(max, (parentMode == MeasureSpec.UNSPECIFIED) ? MeasureSpec.UNSPECIFIED : MeasureSpec.AT_MOST);
        } else {
            spec = MeasureSpec.makeMeasureSpec(childLayoutParam, MeasureSpec.EXACTLY);
        }
        return spec;
    }

    @Override
    protected void onLayout(boolean changed, int leftPosition, int topPosition, int rightPosition, int bottomPosition) {
        int widthOffset = getMeasuredWidth() - getPaddingRight();
        int x = getPaddingLeft();
        int y = getPaddingTop();

        Iterator<RowMeasurement> rowIterator = currentRows.iterator();
        RowMeasurement currentRow = rowIterator.next();
        for (View child : getLayoutChildren()) {
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            if ((x + childWidth) > widthOffset) {
                x = getPaddingLeft();
                y += currentRow.height + verticalSpacing;
                if (rowIterator.hasNext()) {
                    currentRow = rowIterator.next();
                }
            }
            child.layout(x, y, x + childWidth, y + childHeight);
            x += childWidth + horizontalSpacing;
        }
    }

    private List<View> getLayoutChildren() {
        List<View> children = new ArrayList<View>();
        for (int index = 0; index < getChildCount(); index++) {
            View child = getChildAt(index);
            if (child.getVisibility() != View.GONE) {
                children.add(child);
            }
        }
        return children;
    }

    private int getVerticalPadding() {
        return getPaddingTop() + getPaddingBottom();
    }

    private int getHorizontalPadding() {
        return getPaddingLeft() + getPaddingRight();
    }

    @SuppressWarnings("NonStaticInnerClassInSecureContext")
    private final class RowMeasurement {
        private int maxWidth;
        private int widthMode;
        private int width;
        private int height;

        private RowMeasurement(int maxWidth, int widthMode) {
            this.maxWidth = maxWidth;
            this.widthMode = widthMode;
        }

        @SuppressWarnings("WeakerAccess")
        public int getHeight() {
            return height;
        }

        @SuppressWarnings("WeakerAccess")
        public int getWidth() {
            return width;
        }

        @SuppressWarnings({"BooleanMethodNameMustStartWithQuestion", "WeakerAccess"})
        public boolean wouldExceedMax(int childWidth) {
            return (widthMode != MeasureSpec.UNSPECIFIED) && (getNewWidth(childWidth) > maxWidth);
        }

        @SuppressWarnings("WeakerAccess")
        public void addChildDimensions(int childWidth, int childHeight) {
            width = getNewWidth(childWidth);
            height = Math.max(height, childHeight);
        }

        private int getNewWidth(int childWidth) {
            return (width == 0) ? childWidth : (width + horizontalSpacing + childWidth);
        }
    }
}

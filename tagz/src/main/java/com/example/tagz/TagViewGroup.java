package com.example.tagz;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import java.util.ArrayList;

public class TagViewGroup extends ViewGroup {

    private int mGravity = Gravity.CENTER;


    private int horizontalInterval;
    private static final float DEFAULT_HORIZONTAL_INTERVAL = 5;

    private int verticalInterval;
    private static final float DEFAULT_VERTICAL_INTERVAL = 7;

    /** The amount of space used by children in the left gutter. */
    private int mLeftWidth;

    /** The amount of space used by children in the right gutter. */
    private int mRightWidth;

    /** These are used for computing child frames based on their gravity. */
    private final Rect mTmpContainerRect = new Rect();
    private final Rect mTmpChildRect = new Rect();

    public TagViewGroup(Context context) {
        super(context);
    }

    public TagViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    public static float dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.TagViewGroup,
                defStyleAttr, 0);
        verticalInterval = (int) attributes.getDimension(R.styleable.TagViewGroup_vertical_interval,
                dp2px(context, DEFAULT_VERTICAL_INTERVAL));
        horizontalInterval = (int) attributes.getDimension(R.styleable.TagViewGroup_horizontal_interval,
                dp2px(context, DEFAULT_HORIZONTAL_INTERVAL));
        mGravity = attributes.getInt(R.styleable.TagViewGroup_gravity, mGravity);
    }

    /**
     * Any layout manager that doesn't scroll will want this.
     */
    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    /**
     * Ask all children to measure themselves and compute the measurement of this
     * layout based on the children.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int contentRightBorder = getMeasuredWidth() - getPaddingRight();
        int contentLeftBorder = getPaddingLeft();
        int leftVal = contentLeftBorder;

        int totalHeight = 0;

        int rowHeight = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();

                if(leftVal + childWidth > contentRightBorder){
                    totalHeight += rowHeight + verticalInterval;
                    leftVal = contentLeftBorder;
                    rowHeight = 0;
                }
                rowHeight = Math.max(rowHeight, childHeight);
                leftVal += childWidth + horizontalInterval;

                if(i == count - 1){
                    totalHeight += rowHeight + verticalInterval;
                }
            }
        }

        setMeasuredDimension(
            width,
            Math.max(height, totalHeight - verticalInterval + getPaddingTop() + getPaddingBottom())
        );
    }

    /**
     * Position all children within this layout.
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        int contentRightBorder = getMeasuredWidth() - getPaddingRight();
        int contentLeftBorder = getPaddingLeft();

        int topVal = getPaddingTop();

        int rowHeight = 0;
        if (mGravity == Gravity.LEFT) {
            int leftVal = contentLeftBorder;
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);
                if (child.getVisibility() != GONE) {
                    int width = child.getMeasuredWidth();
                    int height = child.getMeasuredHeight();

                    if(leftVal + width > contentRightBorder){
                        topVal += rowHeight + verticalInterval;
                        leftVal = contentLeftBorder;
                        rowHeight = 0;
                    }
                    child.layout(leftVal, topVal,leftVal + width,topVal + height);
                    rowHeight = Math.max(rowHeight, height);
                    leftVal += width + horizontalInterval;
                }
            }


        } else if (mGravity == Gravity.RIGHT) {
            int rightVal = contentRightBorder;
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);
                if (child.getVisibility() != GONE) {
                    int width = child.getMeasuredWidth();
                    int height = child.getMeasuredHeight();

                    if (rightVal - width < contentLeftBorder) {
                        topVal += rowHeight + verticalInterval;
                        rightVal = contentRightBorder;
                        rowHeight = 0;
                    }
                    child.layout(rightVal - width, topVal, rightVal, topVal + height);
                    rowHeight = Math.max(rowHeight, height);
                    rightVal -= width + horizontalInterval;
                }
            }
        } else { //mGravity = Gravity.CENTER
            int leftVal = contentLeftBorder;
            int coordinates[] = new int[count * 2];
            ArrayList<Integer> xCoords = new ArrayList<>();
            int startNotCenteredIndex = 0;
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);
                if (child.getVisibility() != GONE) {
                    int width = child.getMeasuredWidth();
                    int height = child.getMeasuredHeight();

                    if(leftVal + width > contentRightBorder){

                        for(int x = startNotCenteredIndex; x < i; x++){
                            final View centeredChild = getChildAt(x);

                            int leftCoord = xCoords.get(x - startNotCenteredIndex) + (contentRightBorder - leftVal)/2;
                            centeredChild.layout(
                                    leftCoord,
                                    topVal,
                                    leftCoord + centeredChild.getMeasuredWidth(),
                                    topVal + centeredChild.getMeasuredHeight()
                            );

                        }
                        topVal += rowHeight + verticalInterval;
                        leftVal = contentLeftBorder;
                        rowHeight = 0;
                        startNotCenteredIndex = i;
                        xCoords = new ArrayList<>();
                    }
                    xCoords.add(leftVal);
                    rowHeight = Math.max(rowHeight, height);
                    leftVal += width + horizontalInterval;

                    if(i == count - 1){
                        for(int x = startNotCenteredIndex; x <= i; x++){
                            final View centeredChild = getChildAt(x);

                            int leftCoord = xCoords.get(x - startNotCenteredIndex) + (contentRightBorder - leftVal)/2;
                            centeredChild.layout(
                                    leftCoord,
                                    topVal,
                                    leftCoord + centeredChild.getMeasuredWidth(),
                                    topVal + centeredChild.getMeasuredHeight()
                            );
                        }
                    }
                }
            }
        }
    }



//    // ----------------------------------------------------------------------
//    // The rest of the implementation is for custom per-child layout parameters.
//    // If you do not need these (for example you are writing a layout manager
//    // that does fixed positioning of its children), you can drop all of this.
//
//    @Override
//    public LayoutParams generateLayoutParams(AttributeSet attrs) {
//        return new TagViewGroup.LayoutParams(getContext(), attrs);
//    }
//
//    @Override
//    protected LayoutParams generateDefaultLayoutParams() {
//        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//    }
//
//    @Override
//    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
//        return new LayoutParams(p);
//    }
//
//    @Override
//    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
//        return p instanceof LayoutParams;
//    }
//
//    /**
//     * Custom per-child layout information.
//     */
//    public static class LayoutParams extends MarginLayoutParams {
//        /**
//         * The gravity to apply with the View to which these layout parameters
//         * are associated.
//         */
//        public int gravity = Gravity.TOP | Gravity.START;
//
//        public static int POSITION_MIDDLE = 0;
//        public static int POSITION_LEFT = 1;
//        public static int POSITION_RIGHT = 2;
//
//        public int position = POSITION_MIDDLE;
//
//        public LayoutParams(Context c, AttributeSet attrs) {
//            super(c, attrs);
//
//            // Pull the layout param values from the layout XML during
//            // inflation.  This is not needed if you don't care about
//            // changing the layout behavior in XML.
//            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.CustomLayoutLP);
//            gravity = a.getInt(R.styleable.CustomLayoutLP_android_layout_gravity, gravity);
//            position = a.getInt(R.styleable.CustomLayoutLP_layout_position, position);
//            a.recycle();
//        }
//
//        public LayoutParams(int width, int height) {
//            super(width, height);
//        }
//
//        public LayoutParams(ViewGroup.LayoutParams source) {
//            super(source);
//        }
//    }
}

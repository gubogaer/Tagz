package com.example.tagz;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;

import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

public class TagViewGroup extends ViewGroup {


    private TagView.Tagdata td = new TagView.Tagdata();

    private int gravity = Gravity.CENTER;
    private int horizontalInterval = 5;
    private int verticalInterval = 7;

    private List<TagView> tagViews;
    private int numberOfFrontTags;


    public void removeFilter(){
        filterTags(".*");
    }


    public void filterTags(String regex){
        for (TagView entry : tagViews) {
            if (entry.getText().matches(regex)) {
                entry.setVisibility(View.VISIBLE);
                entry.invalidate();
                System.out.println(entry.getText() + " is visible");
            } else {
                entry.setVisibility(View.GONE);
                entry.invalidate();
                System.out.println(entry.getText() + " is gone");
            }
        }
        invalidate();
    }





    public void setTags(List<String> tags){

        if(tags != null){
            for(String tagString : tags){
                TagView tv = new TagView(getContext(),tagString, td);



                tagViews.add(tv);
                tv.setTag(tagViews.size() - 1);
                addView(tv);

            }
            postInvalidate();
        }
    }

    public void addTag(String text){
        addTag(text, tagViews.size());
    }
    public void addTag(String text, int pos){

        for(View view : tagViews){
            int absPos = (int)view.getTag();
            if (absPos >= pos){
                view.setTag(absPos + 1);
            }
        }
        TagView tv = new TagView(getContext(),text, td);
        tv.setTag(pos);


        tagViews.add(pos, tv);
        addView(tv, pos);
        postInvalidate();
    }

    public void removeTag(int position){

        for(View view : tagViews){
            int absPos = (int)view.getTag();
            if (absPos > position){
                view.setTag(absPos - 1);
            }
        }


        removeView(tagViews.get(position));
        tagViews.remove(position);
        postInvalidate();
    }

    public void toggleTagToFront(TagView tv){
        toggleTagToFront(tagViews.indexOf(tv));
    }

    public void toggleTagToFront(int position){
        if (position < numberOfFrontTags) { // tag is in front
            tagToOriginalPosition(position);
            System.out.println("terug naar abs pos");

        } else { // tag is not in front
            tagToFrontPosition(position);
            System.out.println("naar voor");
        }
    }

    public void tagToFrontPosition(int position){ //abspos past niet aan
        numberOfFrontTags ++;
        TagView tv = tagViews.get(position);

        removeView(tagViews.get(position));
        tagViews.remove(position);
        addView(tv, 0);
        tagViews.add(0, tv);

        postInvalidate();
    }

    public void tagToOriginalPosition(int position){
        numberOfFrontTags --;
        TagView tv = tagViews.get(position);

        removeView(tagViews.get(position));
        tagViews.remove(position);
        int newPos = 0;
        for(int i=0; i < numberOfFrontTags; i++){
            if ((int)tagViews.get(i).getTag() > (int)tv.getTag()){
                newPos ++;
            }
        }

        newPos += (int)tv.getTag();
        addView(tv, newPos);
        tagViews.add(newPos, tv);

    }

    public void repositionTag(int src, int dest){ //abspos veranderd niet
        TagView tagview = tagViews.get(src);
        removeTag(src);
        addTag(tagview.getText(),dest);

    }

    public void toggleSelectTag(int position){
        tagViews.get(position).toggleIsSelected();
    }
    public void toggleSelectTags(List<Integer> positions){
        for(int pos : positions){
            toggleSelectTag(pos);
        }
    }


    public void selectTag(int position){
        tagViews.get(position).setIsSelected(true);
    }
    public void selectTags(List<Integer> positions){
        for(int pos : positions){
            selectTag(pos);
        }
    }


    public void deselectTag(int position){
        tagViews.get(position).setIsSelected(false);
    }
    public void deselectTags(List<Integer> positions){
        for(int pos : positions){
            deselectTag(pos);
        }
    }


    public List<String> getSelectedTags(){
        List<String> tagText = new ArrayList<>();
        for(TagView tv: tagViews){
            if(tv.getIsSelected()){
                tagText.add(tv.getText());
            }
        }
        return tagText;
    }




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


    public int dpToPx(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }
    public int spToPx(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getContext().getResources().getDisplayMetrics());
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        tagViews = new ArrayList<>();

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.TagViewGroup, defStyleAttr, 0);

        verticalInterval = (int)attributes.getDimension(R.styleable.TagViewGroup_vertical_interval, dpToPx(verticalInterval));
        horizontalInterval = (int)attributes.getDimension(R.styleable.TagViewGroup_horizontal_interval, dpToPx(horizontalInterval));
        gravity = attributes.getInt(R.styleable.TagViewGroup_gravity, gravity);

        td.parent = this;
        td.textSize = (int)attributes.getDimension(R.styleable.TagViewGroup_tag_text_size, spToPx(td.textSize));
        td.padding = (int)attributes.getDimension(R.styleable.TagViewGroup_tag_padding, dpToPx(td.padding));
        td.backgroundColor = attributes.getColor(R.styleable.TagViewGroup_tag_background_color, td.backgroundColor);
        td.backgroundColorSelected = attributes.getColor(R.styleable.TagViewGroup_tag_background_color_selected, td.backgroundColorSelected);
        td.borderColor = attributes.getColor(R.styleable.TagViewGroup_tag_border_color, td.borderColor);
        td.borderColorSelected = attributes.getColor(R.styleable.TagViewGroup_tag_border_color_selected, td.borderColorSelected);;
        td.borderWidth = (int)attributes.getDimension(R.styleable.TagViewGroup_tag_border_width, dpToPx(td.borderWidth));
        td.borderRadius = (int)attributes.getDimension(R.styleable.TagViewGroup_tag_border_radious, dpToPx(td.borderRadius));
        td.textColor = attributes.getColor(R.styleable.TagViewGroup_tag_textcolor, td.textColor);
        td.textColorSelected = attributes.getColor(R.styleable.TagViewGroup_tag_textcolor_selected, td.textColorSelected);;
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
        if (gravity == Gravity.LEFT) {
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


        } else if (gravity == Gravity.RIGHT) {
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
        } else { //gravity = Gravity.CENTER
            int leftVal = contentLeftBorder;
            int coordinates[] = new int[count * 2];
            ArrayList<Integer> xCoords = new ArrayList<>();
            int startNotCenteredIndex = 0;
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);
                if (child.getVisibility() != INVISIBLE) {
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
                    if(child.getVisibility() != GONE) {
                        leftVal += horizontalInterval;
                    }
                    leftVal += width;


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

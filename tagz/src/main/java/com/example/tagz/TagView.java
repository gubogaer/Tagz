package com.example.tagz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;

public class TagView extends View {

    TagView thiis = this;

    private View.OnClickListener OnTagClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            toggleIsSelected();
            td.parent.toggleTagToFront(thiis);
        }
    };

    public static class Tagdata {
        public TagViewGroup parent;
        public int textSize = 17;
        public int padding = 13;

        public int backgroundColor = 0xff3c78d8;
        public int backgroundColorSelected = 0xff7247c2;

        public int borderColor = Color.TRANSPARENT;
        public int borderColorSelected = Color.TRANSPARENT;
        public int borderWidth = 2;
        public int borderRadius = 50;

        public int textColor = Color.WHITE;
        public int textColorSelected = Color.WHITE;
    }

    private Tagdata td;


    private String text;
    private float textWidth;
    private float textHeight;

    private static Paint paint;

    private boolean isSelected = false;

    private RectF rectF;

    public void toggleIsSelected(){
        isSelected = !isSelected;
        postInvalidate();
    }

    public void setIsSelected(boolean isSelected){
        if(this.isSelected != isSelected){
            this.isSelected = isSelected;
            postInvalidate();
        }
    }
    public boolean getIsSelected(){
        return isSelected;
    }

    public String getText(){
        return text;
    }

    public TagView(Context context, String text, Tagdata tagdata){
        super(context);

        td = tagdata;
        this.setOnClickListener(OnTagClickListener);
        init(context, text);

    }

    private void init(Context context, String text){

        this.text = text;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectF = new RectF();
        calcTextSize();
    }

    private void calcTextSize(){
        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        paint.setTypeface(boldTypeface);
        paint.setTextSize(td.textSize);
        final Paint.FontMetrics fontMetrics = paint.getFontMetrics();

        //textHeight = fontMetrics.bottom - fontMetrics.ascent;
        textWidth = paint.measureText(text);
        Rect rect = new Rect();
        paint.getTextBounds ("t", 0, 1, rect);
        textHeight = rect.bottom - rect.top;

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int)textWidth + td.padding * 2,(int)textHeight + td.padding * 2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF.set(td.borderWidth, td.borderWidth, w - td.borderWidth, h - td.borderWidth);
    }

    // calc TextSize moet hier zeker opgeroepen zijn
    @Override
    protected void onDraw(Canvas canvas) {

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(isSelected ? td.backgroundColorSelected : td.backgroundColor);
        canvas.drawRoundRect(rectF, td.borderRadius, td.borderRadius, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(isSelected ? td.borderColorSelected : td.borderColor);
        paint.setStrokeWidth(td.borderWidth);

        canvas.drawRoundRect(rectF, td.borderRadius, td.borderRadius, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(isSelected ? td.textColorSelected : td.textColor);
        //canvas.drawText(text,getWidth() / 2 - textWidth/2,getHeight()/ 2 - textHeight/2, paint);
        canvas.drawText(text,getWidth() / 2 - textWidth/2,getHeight()/ 2 + textHeight/2, paint);
    }


}

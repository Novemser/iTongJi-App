package com.example.aitongji.Section_Information;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.aitongji.R;

/**
 * Created by Novemser on 2015/11/27.
 */
public class Circle_with_color_text extends View {
    private int shapeColor;
    private Paint paintCircle;
    private Paint paintText;
    private String text;

    private float circleRadius;
    private float textSize;

    public Circle_with_color_text(Context context, AttributeSet attrs) {
        super(context, attrs);
        circleRadius = 145 / 2.0f;
        textSize = 52;
        setupAttributes(attrs);
        setupPaint();
    }

    private void setupPaint() {
        paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle.setColor(shapeColor);
        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(Color.parseColor("#000000"));
        paintText.setTextSize(textSize);
    }

    private void setupAttributes(AttributeSet attrs) {
        // Obtain a typed array of attributes
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.Circle_with_color_text, 0, 0);
        // Extract custom attributes into member variables
        try {
            shapeColor = typedArray.getColor(R.styleable.Circle_with_color_text_shapeColor, Color.GREEN);
        } finally {
            // TypedArray objects are shared and must be recycled.
            typedArray.recycle();
        }
    }

    public int getShapeColor() {
        return this.shapeColor;
    }

    public void setShapeColor(int color) {
        this.shapeColor = color;
        paintCircle.setColor(shapeColor);
        // Notice that when the view properties are changed and
        // might require a redraw, be sure to call
        // invalidate() and requestLayout() to update the appearance.
        invalidate();
        requestLayout();
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
        requestLayout();
    }

    public String getText() {
        return text;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Resolve the width based on our minimum and the measure spec
        int minWidth = (int) (circleRadius * 2) + 1;
        int w = resolveSizeAndState(minWidth, widthMeasureSpec, 0);

        int minHeight = minWidth;
        int h = resolveSizeAndState(minHeight, heightMeasureSpec, 0);
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画出一个圆圈
        canvas.drawCircle(circleRadius, circleRadius, circleRadius, paintCircle);
        // 设置文字居中
        Paint.FontMetricsInt fontMetricsInt = paintText.getFontMetricsInt();
        float baseline = (circleRadius * 2 - fontMetricsInt.bottom - fontMetricsInt.top) / 2;
        paintText.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, circleRadius, baseline , paintText);
    }
}

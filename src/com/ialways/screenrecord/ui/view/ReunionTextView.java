package com.ialways.screenrecord.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.ialways.screenrecord.R;

public class ReunionTextView extends View {

    private Canvas mCanvas;

    private float mWidth;

    private float mHeight;

    private int mTvColor;

    private float mTvSize;

    private String mTvContent;
    
    private int mTvWidth;
    private int mTvHeight;

    private Paint mTextPaint = new Paint();

    private Rect mBound = new Rect();


    public ReunionTextView(Context context) {
        this(context, null);
    }

    public ReunionTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReunionTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ReunionTextView, defStyle, 0);

        if (attributes != null) {

            mWidth = attributes.getDimension(R.styleable.ReunionTextView_rt_tv_width, 0);

            mHeight = attributes.getDimension(R.styleable.ReunionTextView_rt_tv_height, 0);

            mTvColor = attributes.getColor(R.styleable.ReunionTextView_rt_in_color, Color.WHITE);

            mTvSize = attributes.getDimension(R.styleable.ReunionTextView_rt_tv_size, 0);

            mTvContent = attributes.getString(R.styleable.ReunionTextView_rt_tv_content);

            attributes.recycle();
        }

        if (mTvContent != null) {
            // 绘制背景文字
            mTextPaint.setColor(mTvColor);
            mTextPaint.setTextSize(mTvSize);
            mTextPaint.setAntiAlias(true);
            mTextPaint.getTextBounds(mTvContent, 0, mTvContent.length(), mBound);
            
            mTvWidth = mBound.width();
            mTvHeight = mBound.height();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        this.mCanvas = canvas;
        
        drawTextProgress();
    }

    private void drawTextProgress() {
        if (mCanvas == null || mTvContent == null) {
            return;
        }
        // 左上
        mCanvas.save();
        mCanvas.clipRect(0, 0, mTvWidth / 2, mTvHeight / 2);
        mCanvas.drawText(mTvContent, -mBound.left, -mBound.top, mTextPaint);
        mCanvas.restore();
        
        // 左下
        mCanvas.save();
        mCanvas.clipRect(0, mHeight - mTvHeight / 2, mTvWidth / 2, mHeight);
        mCanvas.drawText(mTvContent, -mBound.left, mHeight - mBound.bottom, mTextPaint);
        mCanvas.restore();
        
        // 右上
        mCanvas.save();
        mCanvas.clipRect(mWidth - mTvWidth / 2, 0, mWidth, mTvHeight / 2);
        mCanvas.drawText(mTvContent, mWidth - mBound.right, -mBound.top, mTextPaint);
        mCanvas.restore();
        
        // 右下
        mCanvas.save();
        mCanvas.clipRect(mWidth - mTvWidth / 2, mHeight - mTvHeight / 2, mWidth, mHeight);
        mCanvas.drawText(mTvContent, mWidth - mBound.right, mHeight - mBound.bottom, mTextPaint);
        mCanvas.restore();
    }

}

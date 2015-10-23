package com.ialways.screenrecord.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.ialways.screenrecord.R;

public class ReunionTextView extends View {

    private final static float STEP = 0.05f;

    // 动画间隔
    private final static int ANIM_TIME = 50;
    
    private float mWidth;
       
    private float mHeight;

    private int mTvColor;

    private float mTvSize;

    private String mTvContent;
    
    private int mTvWidth;
    private int mTvHeight;
    
    private float mAnimWidth;
    private float mAnimHeight;
    
    private float mProgress = 0;

    private Paint mTextPaint = new Paint();

    private Rect mBound = new Rect();

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            if (mProgress < 1) {
                mProgress += STEP;
                ReunionTextView.this.invalidate();
                mHandler.sendEmptyMessageDelayed(0, ANIM_TIME);
            } 
        }
    };

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
        
        mAnimWidth = mWidth - mTvWidth;
        mAnimHeight = mHeight - mTvHeight;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        drawTextProgress(canvas);
        if (mProgress == 0) {
            mHandler.sendEmptyMessageDelayed(0, ANIM_TIME);
        }
    }

    private void drawTextProgress(Canvas canvas) {
        if (canvas == null || mTvContent == null) {
            return;
        }
        float xDistance = mAnimWidth / 2 * mProgress;
        float yDistance = mAnimHeight / 2 * mProgress;
        // 左上
        canvas.save();
        canvas.clipRect(0, 0, mTvWidth / 2 + xDistance, mTvHeight / 2 + yDistance);
        canvas.drawText(mTvContent, -mBound.left + xDistance, -mBound.top + yDistance, mTextPaint);
        canvas.restore();
        
        // 左下
        canvas.save();
        canvas.clipRect(0, mHeight - mTvHeight / 2 - yDistance, mTvWidth / 2 + xDistance, mHeight);
        canvas.drawText(mTvContent, -mBound.left + xDistance, mHeight - mBound.bottom - yDistance, mTextPaint);
        canvas.restore();
        
        // 右上
        canvas.save();
        canvas.clipRect(mWidth - mTvWidth / 2 - xDistance, 0, mWidth, mTvHeight / 2 + yDistance);
        canvas.drawText(mTvContent, mWidth - mBound.right - xDistance, -mBound.top + yDistance, mTextPaint);
        canvas.restore();
        
        // 右下
        canvas.save();
        canvas.clipRect(mWidth - mTvWidth / 2 - xDistance, mHeight - mTvHeight / 2 - yDistance, mWidth, mHeight);
        canvas.drawText(mTvContent, mWidth - mBound.right - xDistance, mHeight - mBound.bottom - yDistance, mTextPaint);
        canvas.restore();
    }

}

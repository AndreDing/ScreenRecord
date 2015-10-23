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

public class ProgressTextView extends View {

    private final static float STEP = 0.01f;

    // 动画间隔
    private final static int ANIM_TIME = 50;

    private float mWidth;

    private float mHeight;

    private int mTvColor;

    private float mTvSize;

    private String mTvContent;

    private float mStep;

    private float mProgress = 0;

    private Paint mTextPaint = new Paint();

    private Rect mBound = new Rect();

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            if (mProgress < 1) {
                mProgress += mStep;
                ProgressTextView.this.invalidate();
                mHandler.sendEmptyMessageDelayed(0, ANIM_TIME);
            } 
        }
    };

    public ProgressTextView(Context context) {
        this(context, null);
    }

    public ProgressTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ProgressTextView, defStyle, 0);

        if (attributes != null) {

            mWidth = attributes.getDimension(R.styleable.ProgressTextView_tv_width, 0);

            mHeight = attributes.getDimension(R.styleable.ProgressTextView_tv_height, 0);

            mTvColor = attributes.getColor(R.styleable.ProgressTextView_in_color, Color.WHITE);

            mTvSize = attributes.getDimension(R.styleable.ProgressTextView_tv_size, 0);

            mTvContent = attributes.getString(R.styleable.ProgressTextView_tv_content);

            mStep = attributes.getFloat(R.styleable.ProgressTextView_progress_step, STEP);

            attributes.recycle();
        }

        if (mTvContent != null) {
            // 绘制背景文字
            mTextPaint.setColor(mTvColor);
            mTextPaint.setTextSize(mTvSize);
            mTextPaint.setAntiAlias(true);
            mTextPaint.getTextBounds(mTvContent, 0, mTvContent.length(), mBound);
        }

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
        // 绘制文字内容
        canvas.clipRect(0, 0, mWidth * mProgress, mHeight);
        mTextPaint.getTextBounds(mTvContent, 0, mTvContent.length(), mBound);
        canvas.drawText(mTvContent, mWidth / 2 - (mBound.right + mBound.left) / 2, mHeight / 2
                - (mBound.bottom + mBound.top) / 2, mTextPaint);
        canvas.restore();
    }

}

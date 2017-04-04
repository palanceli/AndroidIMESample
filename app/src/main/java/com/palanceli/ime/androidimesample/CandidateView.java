package com.palanceli.ime.androidimesample;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class CandidateView extends View {
    private List<String> mSuggestions;      // 存放候选列表
    private static final int X_GAP = 10;    // 每个候选之间的间隔
    private Paint mPaint;                   // 用于绘制候选
    private int mCandidateVPadding;         // 候选文字上下边距

    public CandidateView(Context context) {
        super(context);
        Log.d(this.getClass().toString(), "CandidateView: ");

        Resources r = context.getResources();
        mCandidateVPadding = r.getDimensionPixelSize(R.dimen.candidateVerticalPadding);
        setBackgroundColor(r.getColor(R.color.candidateBackground, null)); // 设置背景色
        mPaint = new Paint();
        mPaint.setColor(r.getColor(R.color.candidate, null));               // 设置前景色
        mPaint.setAntiAlias(true);      // 设置字体
        mPaint.setTextSize(r.getDimensionPixelSize(R.dimen.candidateFontHeight));   // 设置字号
        mPaint.setStrokeWidth(0);

        setWillNotDraw(false);  // 覆盖了onDraw函数应清除该标记
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(this.getClass().toString(), "onMeasure: ");
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);

        int measuredWidth = resolveSize(50, widthMeasureSpec);
        final int desiredHeight = ((int)mPaint.getTextSize()) + mCandidateVPadding;

        // 系统会根据返回值确定窗体的大小
        setMeasuredDimension(measuredWidth, resolveSize(desiredHeight, heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(this.getClass().toString(), "onDraw: ");
        super.onDraw(canvas);

        if (mSuggestions == null)
            return;

        // 依次绘制每组候选字串
        int x = 0;
        final int count = mSuggestions.size();
        final int height = getHeight();
        final int y = (int) (((height - mPaint.getTextSize()) / 2) - mPaint.ascent());

        for (int i = 0; i < count; i++) {
            String suggestion = mSuggestions.get(i);
            float textWidth = mPaint.measureText(suggestion);
            final int wordWidth = (int) textWidth + X_GAP * 2;

            canvas.drawText(suggestion, x + X_GAP, y, mPaint);
            x += wordWidth;
        }
    }

    public void setSuggestions(List<String> suggestions) {
        // 设置候选字串列表
        if (suggestions != null) {
            mSuggestions = new ArrayList<String>(suggestions);
        }
        invalidate();
        requestLayout();
    }
}
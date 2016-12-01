/*
 * Copyright (C) 2016 Dardle Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package au.com.dardle.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;


public class LabelLayout extends FrameLayout {
    private final int mLabelDistance;
    private final int mLabelHeight;
    private final int mLabelBackground;
    private final Gravity mLabelGravity;

    private final String mLabelText;
    private final int mLabelTextSize;
    private final int mLabelTextColor;

    private final Path mLabelPath;
    private final Paint mBackgroundPaint;
    private final Paint mTextPaint;

    public LabelLayout(Context context) {
        this(context, null);
    }

    public LabelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Enable 'onDraw()'
        setWillNotDraw(false);

        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.LabelLayout);
        mLabelDistance = tintTypedArray.getDimensionPixelSize(R.styleable.LabelLayout_labelDistance, 0);
        mLabelHeight = tintTypedArray.getDimensionPixelSize(R.styleable.LabelLayout_labelHeight, 0);
        mLabelBackground = tintTypedArray.getColor(R.styleable.LabelLayout_labelBackground, Color.BLACK);
        mLabelGravity = Gravity.values()[tintTypedArray.getInteger(R.styleable.LabelLayout_labelGravity, Gravity.TOP_LEFT.ordinal())];

        mLabelText = tintTypedArray.getString(R.styleable.LabelLayout_labelText);
        mLabelTextSize = tintTypedArray.getDimensionPixelSize(R.styleable.LabelLayout_labelTextSize, -1);
        mLabelTextColor = tintTypedArray.getColor(R.styleable.LabelLayout_labelTextColor, Color.BLACK);
        tintTypedArray.recycle();

        // Create the region of the label
        mLabelPath = new Path();

        // Setup the paint for the label background
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setDither(true);
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setStrokeJoin(Paint.Join.ROUND);
        mBackgroundPaint.setStrokeCap(Paint.Cap.SQUARE);

        // Setup the paint for the label text
        mTextPaint = new Paint();
        mTextPaint.setDither(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeJoin(Paint.Join.ROUND);
        mTextPaint.setStrokeCap(Paint.Cap.SQUARE);
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);

        // Calculate start and end positions of label
        final int vertexToMid = (int) (Math.sqrt(2) * (mLabelDistance + mLabelHeight / 2.0));
        int startX;
        int startY;
        int endX;
        int endY;
        switch (mLabelGravity) {
            case TOP_RIGHT:
                startY = 0;
                startX = getMeasuredWidth() - vertexToMid;
                endX = getMeasuredWidth();
                endY = vertexToMid;
                break;

            case BOTTOM_RIGHT:
                startX = getMeasuredWidth() - vertexToMid;
                startY = getMeasuredHeight();
                endX = getMeasuredWidth();
                endY = getMeasuredHeight() - vertexToMid;
                break;

            case BOTTOM_LEFT:
                startX = 0;
                startY = getMeasuredHeight() - vertexToMid;
                endX = vertexToMid;
                endY = getMeasuredHeight();
                break;

            default:
                startX = 0;
                startY = vertexToMid;
                endX = vertexToMid;
                endY = 0;
                break;

        }
        mLabelPath.moveTo(startX, startY);
        mLabelPath.lineTo(endX, endY);

        // Set background paint parameter
        mBackgroundPaint.setStrokeWidth(mLabelHeight);
        mBackgroundPaint.setColor(mLabelBackground);

        // Set text paint parameter
        if (mLabelTextSize != -1) {
            mTextPaint.setTextSize(mLabelTextSize);
        }
        mTextPaint.setColor(mLabelTextColor);

        // Draw background
        canvas.drawPath(mLabelPath, mBackgroundPaint);

        // Draw text
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(mLabelText, 0, mLabelText.length(), textBounds);
        float hOffset = (float) (vertexToMid / Math.sqrt(2) - textBounds.width() / 2.0);
        float vOffset;
        if (mLabelDistance >= mLabelHeight) {
            vOffset = (textBounds.height() * 0.5f);
        } else {
            vOffset = (textBounds.height() * ((mLabelHeight - mLabelDistance) / mLabelHeight * 0.5f + 0.5f));
        }
        canvas.drawTextOnPath(mLabelText, mLabelPath, hOffset, vOffset, mTextPaint);
    }

    public enum Gravity {
        TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT
    }
}

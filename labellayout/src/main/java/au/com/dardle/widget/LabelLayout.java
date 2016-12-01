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
        mLabelBackground = tintTypedArray.getColor(R.styleable.LabelLayout_labelBackground, new Paint().getColor());
        mLabelGravity = Gravity.values()[tintTypedArray.getInteger(R.styleable.LabelLayout_labelGravity, Gravity.TOP_LEFT.ordinal())];
        mLabelText = tintTypedArray.getString(R.styleable.LabelLayout_labelText);
        mLabelTextSize = tintTypedArray.getDimensionPixelSize(R.styleable.LabelLayout_labelTextSize, (int) new Paint().getTextSize());
        mLabelTextColor = tintTypedArray.getColor(R.styleable.LabelLayout_labelTextColor, Color.BLACK);
        tintTypedArray.recycle();

        // Setup background paint
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setDither(true);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStrokeJoin(Paint.Join.ROUND);
        mBackgroundPaint.setStrokeCap(Paint.Cap.SQUARE);

        // Setup text paint
        mTextPaint = new Paint();
        mTextPaint.setDither(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeJoin(Paint.Join.ROUND);
        mTextPaint.setStrokeCap(Paint.Cap.SQUARE);
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);

        // Calculate label region
        Path bisectorPath = new Path();
        int[] bisectorCoordinates = calculateBisectorCoordinates(mLabelDistance, mLabelHeight, mLabelGravity);
        bisectorPath.moveTo(bisectorCoordinates[0], bisectorCoordinates[1]);
        bisectorPath.lineTo(bisectorCoordinates[2], bisectorCoordinates[3]);

        // Draw background
        mBackgroundPaint.setStrokeWidth(mLabelHeight);
        mBackgroundPaint.setColor(mLabelBackground);
        canvas.drawPath(bisectorPath, mBackgroundPaint);

        // Draw text
        mTextPaint.setTextSize(mLabelTextSize);
        mTextPaint.setColor(mLabelTextColor);
        float[] offsets = calculateTextOffsets(mLabelText, mTextPaint, mLabelDistance, mLabelHeight);
        canvas.drawTextOnPath(mLabelText, bisectorPath, offsets[0], offsets[1], mTextPaint);
    }

    private int calculateBisectorIntersectPosition(int distance, int height) {
        return (int) (Math.sqrt(2) * (distance + (height / 2)));
    }

    private int[] calculateBisectorCoordinates(int distance, int height, Gravity gravity) {
        final int bisectorIntersectPosition = calculateBisectorIntersectPosition(distance, height);
        int[] results = new int[4];

        int bisectorStartX;
        int bisectorStartY;
        int bisectorEndX;
        int bisectorEndY;
        switch (gravity) {
            case TOP_RIGHT:
                bisectorStartY = 0;
                bisectorStartX = getMeasuredWidth() - bisectorIntersectPosition;
                bisectorEndX = getMeasuredWidth();
                bisectorEndY = bisectorIntersectPosition;
                break;

            case BOTTOM_RIGHT:
                bisectorStartX = getMeasuredWidth() - bisectorIntersectPosition;
                bisectorStartY = getMeasuredHeight();
                bisectorEndX = getMeasuredWidth();
                bisectorEndY = getMeasuredHeight() - bisectorIntersectPosition;
                break;

            case BOTTOM_LEFT:
                bisectorStartX = 0;
                bisectorStartY = getMeasuredHeight() - bisectorIntersectPosition;
                bisectorEndX = bisectorIntersectPosition;
                bisectorEndY = getMeasuredHeight();
                break;

            default:
                bisectorStartX = 0;
                bisectorStartY = bisectorIntersectPosition;
                bisectorEndX = bisectorIntersectPosition;
                bisectorEndY = 0;
                break;
        }

        results[0] = bisectorStartX;
        results[1] = bisectorStartY;
        results[2] = bisectorEndX;
        results[3] = bisectorEndY;

        return results;
    }

    private float[] calculateTextOffsets(String text, Paint paint, int distance, int height) {
        float[] offsets = new float[2];

        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);

        float hOffset = (float) (calculateBisectorIntersectPosition(distance, height) / Math.sqrt(2) - textBounds.width() / 2.0);
        float vOffset;
        if (distance >= height) {
            vOffset = (textBounds.height() * 0.5f);
        } else {
            vOffset = (textBounds.height() * ((height - distance) / height * 0.5f + 0.5f));
        }

        offsets[0] = hOffset;
        offsets[1] = vOffset;

        return offsets;
    }

    public enum Gravity {
        TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT
    }
}

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
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * LabelLayout provides a label at the corner to display text
 */
public class LabelLayout extends FrameLayout {
    private int mLabelDistance;
    private int mLabelHeight;
    private Drawable mLabelBackground;
    private Gravity mLabelGravity;

    private String mLabelText;
    private int mLabelTextSize;
    private int mLabelTextColor;

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
        mLabelBackground = tintTypedArray.getDrawable(R.styleable.LabelLayout_labelBackground);
        mLabelGravity = Gravity.values()[tintTypedArray.getInteger(R.styleable.LabelLayout_labelGravity, Gravity.TOP_LEFT.ordinal())];
        mLabelText = tintTypedArray.getString(R.styleable.LabelLayout_labelText);
        mLabelTextSize = tintTypedArray.getDimensionPixelSize(R.styleable.LabelLayout_labelTextSize, (int) new Paint().getTextSize());
        mLabelTextColor = tintTypedArray.getColor(R.styleable.LabelLayout_labelTextColor, Color.BLACK);
        tintTypedArray.recycle();

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
        int[] centerCoordinate = calculateCenterCoordinate(mLabelDistance, mLabelHeight, mLabelGravity);
        int labelHalfWidth = calculateWidth(mLabelDistance, mLabelHeight) / 2;
        int labelHalfHeight = mLabelHeight / 2;
        Rect labelRect = new Rect(centerCoordinate[0] - labelHalfWidth, centerCoordinate[1] - labelHalfHeight, centerCoordinate[0] + labelHalfWidth, centerCoordinate[1] + labelHalfHeight);
        mLabelBackground.setBounds(calculateBackgroundBounds(mLabelBackground, labelRect));

        canvas.save();
        canvas.rotate(calculateRotateDegree(mLabelGravity), centerCoordinate[0], centerCoordinate[1]);
        mLabelBackground.draw(canvas);
        canvas.restore();

        // Draw text
        mTextPaint.setTextSize(mLabelTextSize);
        mTextPaint.setColor(mLabelTextColor);
        float[] offsets = calculateTextOffsets(mLabelText, mTextPaint, mLabelDistance, mLabelHeight);
        canvas.drawTextOnPath(mLabelText, bisectorPath, offsets[0], offsets[1], mTextPaint);
    }

    /**
     * Get the distance from vertex to label's edge
     *
     * @return The distance from vertex to label's edge
     */
    public int getLabelDistance() {
        return mLabelDistance;
    }

    /**
     * Set the distance from vertex to label's edge
     *
     * @param labelDistance The distance from vertex to label's edge
     */
    public void setLabelDistance(int labelDistance) {
        mLabelDistance = labelDistance;
        invalidate();
    }

    /**
     * Get the height of label
     *
     * @return The height of label
     */
    public int getLabelHeight() {
        return mLabelHeight;
    }

    /**
     * Set the height of label
     *
     * @param labelHeight The height of label
     */
    public void setLabelHeight(int labelHeight) {
        mLabelHeight = labelHeight;
        invalidate();
    }

    /**
     * Get the background of label
     *
     * @return The background of label
     */
    public Drawable getLabelBackground() {
        return mLabelBackground;
    }

    /**
     * Set the background of label
     *
     * @param labelBackground The background of label
     */
    public void setLabelBackground(Drawable labelBackground) {
        mLabelBackground = labelBackground;
        invalidate();
    }

    /**
     * Get the gravity of label
     *
     * @return The gravity of label
     * @see Gravity
     */
    public Gravity getLabelGravity() {
        return mLabelGravity;
    }

    /**
     * Set the gravity of label
     *
     * @param labelGravity The gravity of label
     */
    public void setLabelGravity(Gravity labelGravity) {
        mLabelGravity = labelGravity;
        invalidate();
    }

    /**
     * Get the text of label
     *
     * @return The text of label
     */
    public String getLabelText() {
        return mLabelText;
    }

    /**
     * Set the text of label
     *
     * @param labelText The text of label
     */
    public void setLabelText(String labelText) {
        mLabelText = labelText;
        invalidate();
    }

    /**
     * Get the text size of label
     *
     * @return The text size of label
     */
    public int getLabelTextSize() {
        return mLabelTextSize;
    }

    /**
     * Set the text size of label
     *
     * @param labelTextSize The text size of label
     */
    public void setLabelTextSize(int labelTextSize) {
        mLabelTextSize = labelTextSize;
        invalidate();
    }

    /**
     * Get the text color of label
     *
     * @return The text color of label
     */
    public int getLabelTextColor() {
        return mLabelTextColor;
    }

    /**
     * Set the text color of label
     *
     * @param labelTextColor The text color of label
     */
    public void setLabelTextColor(int labelTextColor) {
        mLabelTextColor = labelTextColor;
        invalidate();
    }

    // Calculate the absolute position of point intersecting between canvas edge and bisector
    private int calculateBisectorIntersectAbsolutePosition(int distance, int height) {
        return (int) (Math.sqrt(2) * (distance + (height / 2)));
    }

    // Calculate the starting and ending points coordinates of bisector line
    private int[] calculateBisectorCoordinates(int distance, int height, Gravity gravity) {
        final int bisectorIntersectAbsolutePosition = calculateBisectorIntersectAbsolutePosition(distance, height);
        int[] results = new int[4];

        int bisectorStartX;
        int bisectorStartY;
        int bisectorEndX;
        int bisectorEndY;
        switch (gravity) {
            case TOP_RIGHT:
                bisectorStartY = 0;
                bisectorStartX = getMeasuredWidth() - bisectorIntersectAbsolutePosition;
                bisectorEndX = getMeasuredWidth();
                bisectorEndY = bisectorIntersectAbsolutePosition;
                break;

            case BOTTOM_RIGHT:
                bisectorStartX = getMeasuredWidth() - bisectorIntersectAbsolutePosition;
                bisectorStartY = getMeasuredHeight();
                bisectorEndX = getMeasuredWidth();
                bisectorEndY = getMeasuredHeight() - bisectorIntersectAbsolutePosition;
                break;

            case BOTTOM_LEFT:
                bisectorStartX = 0;
                bisectorStartY = getMeasuredHeight() - bisectorIntersectAbsolutePosition;
                bisectorEndX = bisectorIntersectAbsolutePosition;
                bisectorEndY = getMeasuredHeight();
                break;

            default:
                bisectorStartX = 0;
                bisectorStartY = bisectorIntersectAbsolutePosition;
                bisectorEndX = bisectorIntersectAbsolutePosition;
                bisectorEndY = 0;
                break;
        }

        results[0] = bisectorStartX;
        results[1] = bisectorStartY;
        results[2] = bisectorEndX;
        results[3] = bisectorEndY;

        return results;
    }

    // Calculate text horizontal and vertical offset
    private float[] calculateTextOffsets(String text, Paint paint, int distance, int height) {
        float[] offsets = new float[2];

        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);

        float hOffset = (float) (calculateBisectorIntersectAbsolutePosition(distance, height) / Math.sqrt(2) - textBounds.width() / 2.0);
        float vOffset;
        if (distance >= height) {
            vOffset = (textBounds.height() * 0.5f);
        } else {
            vOffset = (textBounds.height() * ((height - distance) / (float) height * 0.5f + 0.5f));
        }

        Log.d(LabelLayout.class.getSimpleName(), String.format("%d, %d, %f", distance, height, vOffset));

        offsets[0] = hOffset;
        offsets[1] = vOffset;

        return offsets;
    }

    private int[] calculateCenterCoordinate(int distance, int height, Gravity gravity) {
        int[] results = new int[2];
        int x;
        int y;

        int centerAbsolutePosition = calculateCenterAbsolutePosition(distance, height);
        switch (gravity) {
            default:
                x = centerAbsolutePosition;
                y = centerAbsolutePosition;
                break;

            case TOP_RIGHT:
                x = getMeasuredWidth() - centerAbsolutePosition;
                y = centerAbsolutePosition;
                break;

            case BOTTOM_RIGHT:
                x = getMeasuredWidth() - centerAbsolutePosition;
                y = getMeasuredHeight() - centerAbsolutePosition;
                break;

            case BOTTOM_LEFT:
                x = centerAbsolutePosition;
                y = getMeasuredHeight() - centerAbsolutePosition;
                break;

        }

        results[0] = x;
        results[1] = y;
        return results;
    }

    private int calculateCenterAbsolutePosition(int distance, int height) {
        return (int) ((distance + height / 2) / Math.sqrt(2));
    }

    private int calculateWidth(int distance, int height) {
        return 2 * (distance + height);
    }

    private Rect calculateBackgroundBounds(Drawable drawable, Rect labelRect) {
        Rect rect;

        if (drawable instanceof ColorDrawable) {
            rect = new Rect(labelRect);
        } else {
            rect = new Rect();

            if (drawable.getIntrinsicWidth() <= labelRect.width() && drawable.getIntrinsicHeight() <= labelRect.height()) {
                // No need to scale
                rect.left = labelRect.centerX() - drawable.getIntrinsicWidth() / 2;
                rect.top = labelRect.centerY() - drawable.getIntrinsicHeight() / 2;
                rect.right = labelRect.centerX() + drawable.getIntrinsicWidth() / 2;
                rect.bottom = labelRect.centerY() + drawable.getIntrinsicHeight() / 2;
            } else {
                // Need to scale
                int width;
                int height;
                int ratio = drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight();
                if (drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight() >= labelRect.width() / labelRect.height()) {
                    // Scale to fill width
                    width = labelRect.width();
                    height = labelRect.width() / ratio;
                } else {
                    // Scale to fill height
                    width = labelRect.height() * ratio;
                    height = labelRect.height();
                }

                rect.left = labelRect.centerX() - width / 2;
                rect.top = labelRect.centerY() - height / 2;
                rect.right = labelRect.centerX() + width / 2;
                rect.bottom = labelRect.centerY() + height / 2;
            }
        }

        return rect;
    }

    private float calculateRotateDegree(Gravity gravity) {
        float degree;

        switch (gravity) {
            case BOTTOM_RIGHT:
            default:
                degree = -45;
                break;

            case TOP_RIGHT:
            case BOTTOM_LEFT:
                degree = 45;
                break;
        }

        return degree;
    }

    public enum Gravity {
        TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT
    }
}

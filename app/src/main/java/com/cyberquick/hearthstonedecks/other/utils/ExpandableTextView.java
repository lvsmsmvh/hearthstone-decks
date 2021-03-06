package com.cyberquick.hearthstonedecks.other.utils;
import android.content.Context;
import android.graphics.Canvas;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

public class ExpandableTextView extends AppCompatTextView {

    private int collapsedHeight;
    private int expandedHeight;
    private boolean collapsed = true;
    private int speed = 100;
    private boolean initialized = false;


    public ExpandableTextView(Context context) {
        super(context);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialized = false;
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialized = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!initialized) {
            setCollapsedLines(1);
            setMeasuredDimension(widthMeasureSpec, collapsedHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setExpandedLines(getLineCount());
        init();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
    }

    public void init(){
        if (!initialized) {
            initialized = true;

            this.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    setExpandedLines(getLineCount());
                }
            });
        }
    }
    private void updateState(){
        if (collapsed) {
            expand();
        } else {
            collapse();
        }
    }

    public boolean isCollapsed(){
        return collapsed;
    }
    public void updateStatePublic(){
        updateState();
    }

    private void expand(){
        collapsed = false;
        invalidate();
    }

    private void collapse(){
        setCollapsedLines(0);
        collapsed = true;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((!collapsed) && (getHeight() < expandedHeight)){
            setHeight(Math.min(getHeight() + getSpeed(), expandedHeight));
            invalidate();
        }
        if ((collapsed) && (getHeight() > collapsedHeight)){
            setHeight(Math.max(getHeight() - getSpeed(), collapsedHeight));
            invalidate();
        }
    }

    public void setCollapsedLines(int collapsedLines) {
        if (collapsedLines == 0)
            collapsedHeight = 0;
        else
            collapsedHeight = (int) Math.ceil(collapsedLines * (getLineHeight() + getLineSpacingExtra()) + getPaddingBottom()  + getPaddingTop() +  getLastBaselineToBottomHeight());
    }

    private void setExpandedLines(int expandedLines) {
        expandedHeight = (int) Math.ceil(expandedLines * (getLineHeight() + getLineSpacingExtra()) + getPaddingBottom()  + getPaddingTop() + getLastBaselineToBottomHeight());
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }
}
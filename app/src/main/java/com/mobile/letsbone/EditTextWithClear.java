package com.mobile.letsbone;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


// overrided automatically
public class EditTextWithClear extends AppCompatEditText
{
    Drawable mClearButtonImage;



    public EditTextWithClear(Context context) {
        super(context);
        init();
    }

    public EditTextWithClear(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextWithClear(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init()
    {
        mClearButtonImage = ResourcesCompat.getDrawable(
                                            getResources(),
                                            R.drawable.ic_clear_opaque_24dp,
                                    null);

        // TODO: If the clear (X) button is tapped, clear the text
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                // pos: 0, 1, [2], 3 in setCompoundDrawablesRelativeWithIntrinsicBounds
                if ((getCompoundDrawablesRelative()[2] != null))
                {
                    float clearButtonStart; // Used for LTR languages
                    float clearButtonEnd;  // Used for RTL languages
                    boolean isClearButtonClicked = false;

                    // TODO: Detect the touch in RTL or LTR layout direction.
                    // Detect the touch in RTL or LTR layout direction.
                    if (getLayoutDirection() == LAYOUT_DIRECTION_RTL) {

                        clearButtonEnd = mClearButtonImage.getIntrinsicWidth() + getPaddingStart();

                        if (event.getX() < clearButtonEnd) {
                            isClearButtonClicked = true;
                        }
                    }
                    else {
                        // Layout is LTR: normal style
                        // Get the start of the button on the right side.
                        clearButtonStart = getWidth() - mClearButtonImage.getIntrinsicWidth() - getPaddingEnd();
                        // If the touch occurred after the start of the button,
                        // set isClearButtonClicked to true.
                        if (event.getX() > clearButtonStart) {
                            isClearButtonClicked = true;
                        }
                    }

                    // TODO: Check for actions if the button is tapped.
                    // Check for actions if the button is tapped.
                    if (isClearButtonClicked)
                    {
                        // Check for ACTION_DOWN (always occurs before ACTION_UP).
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            // Switch to the black version of clear button.
                            mClearButtonImage =
                                    ResourcesCompat.getDrawable(getResources(),
                                            R.drawable.ic_clear_black_24dp, null);
                            showClearButton();
                        }
                        // Check for ACTION_UP.
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            // Switch to the opaque version of clear button.
                            mClearButtonImage =
                                    ResourcesCompat.getDrawable(getResources(),
                                            R.drawable.ic_clear_opaque_24dp, null);
                            // Clear the text and hide the clear button.
                            getText().clear();
                            hideClearButton();
                            return true;
                        }
                    } else {
                        return false;
                    }
                }
                return false;
            }
        });

        // TODO: If the text change, show/hide (X)
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                showClearButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void showClearButton()
    {
        setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                mClearButtonImage,
                null
        );
    }

    private void hideClearButton()
    {
        setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                null,
                null
        );
    }
}

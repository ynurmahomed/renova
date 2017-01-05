package br.ufu.renova.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;
import br.ufu.renova.R;

/**
 * Created by yassin on 1/4/17.
 */
public class NumberPickerPreference extends DialogPreference {

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 0;

    private int mNumber;

    private int mMinValue;

    private int mMaxValue;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.number_picker_dialog);
        setDialogTitle(R.string.notification_preference_dialog_title);
        setPositiveButtonText(R.string.number_picker_dialog_positive_text);
        setNegativeButtonText(R.string.dialog_negative_text);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.NumberPickerPreference);
        mMinValue = a.getInteger(R.styleable.NumberPickerPreference_minValue, MIN_VALUE);
        mMaxValue = a.getInteger(R.styleable.NumberPickerPreference_maxValue, MAX_VALUE);
        a.recycle();
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            mNumber = getPersistedInt(mNumber);
        } else {
            mNumber = (Integer) defaultValue;
            persistInt(mNumber);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, mNumber);
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int number) {
        mNumber = number;
        persistInt(mNumber);
    }

    public int getMinValue() {
        return mMinValue;
    }

    public int getMaxValue() {
        return mMaxValue;
    }
}

package br.ufu.renova.preferences;

import android.os.Bundle;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import br.ufu.renova.R;

/**
 * Created by yassin on 1/4/17.
 */
public class NumberPickerPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {

    private static final String ARG_MIN_VALUE = "minValue";
    private static final String ARG_MAX_VALUE = "maxValue";

    private NumberPicker mNumberPicker;

    private int mNumber;

    public static NumberPickerPreferenceDialogFragmentCompat newInstance(String key, int minValue, int maxValue) {
        final NumberPickerPreferenceDialogFragmentCompat
                fragment = new NumberPickerPreferenceDialogFragmentCompat();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        b.putInt(ARG_MIN_VALUE, minValue);
        b.putInt(ARG_MAX_VALUE, maxValue);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNumber = getNumberPickerPreference().getNumber();
    }

    public NumberPickerPreference getNumberPickerPreference() {
        return (NumberPickerPreference) getPreference();
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        final TextView mLabel = (TextView) view.findViewById(R.id.number_picker_label);
        mLabel.setText(getResources().getQuantityString(R.plurals.dia, mNumber));
        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        mNumberPicker.setMinValue(getArguments().getInt(ARG_MIN_VALUE));
        mNumberPicker.setMaxValue(getArguments().getInt(ARG_MAX_VALUE));
        mNumberPicker.setValue(mNumber);
        mNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mLabel.setText(getResources().getQuantityString(R.plurals.dia, newVal));
            }
        });
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            int value = mNumberPicker.getValue();
            if (getNumberPickerPreference().callChangeListener(value)) {
                getNumberPickerPreference().setNumber(value);
            }
        }
    }
}

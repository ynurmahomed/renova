package br.ufu.renova.preferences;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import br.ufu.renova.R;

import java.io.Serializable;


/**
 * A simple {@link DialogFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NumberPickerDialogFragment.NumberPickerDialogFragmentResultHandler} interface
 * to handle interaction events.
 * Use the {@link NumberPickerDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class NumberPickerDialogFragment extends android.support.v4.app.DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MIN_VALUE = "param1";
    private static final String ARG_MAX_VALUE = "param2";
    private static final String ARG_LABEL = "param3";
    private static final String ARG_TITLE = "param4";
    private static final String ARG_VALUE = "param5";
    private static final String ARG_HANDLER = "param6";

    // TODO: Rename and change types of parameters
    private int minValue;
    private int maxValue;
    private int labelPluralString;
    private String title;
    private int value;
    private NumberPickerDialogFragmentResultHandler mHandler;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Parameter 1.
     * @param minValue Parameter 2.
     * @param maxValue Parameter 3.
     * @param value Parameter 4.
     * @param labelPluralStringId Parameter 5.
     * @return A new instance of fragment NumberPickerDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NumberPickerDialogFragment newInstance(String title, int minValue, int maxValue, int value, int labelPluralStringId, NumberPickerDialogFragmentResultHandler handler) {
        NumberPickerDialogFragment fragment = new NumberPickerDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_MIN_VALUE, minValue);
        args.putInt(ARG_MAX_VALUE, maxValue);
        args.putInt(ARG_VALUE, value);
        args.putInt(ARG_LABEL, labelPluralStringId);
        args.putSerializable(ARG_HANDLER, handler);
        fragment.setArguments(args);
        return fragment;
    }
    public NumberPickerDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            minValue = getArguments().getInt(ARG_MIN_VALUE);
            maxValue = getArguments().getInt(ARG_MAX_VALUE);
            labelPluralString = getArguments().getInt(ARG_LABEL);
            title = getArguments().getString(ARG_TITLE);
            value = getArguments().getInt(ARG_VALUE);
            mHandler = ((NumberPickerDialogFragmentResultHandler) getArguments().getSerializable(ARG_HANDLER));
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.number_picker_dialog, null);

        final TextView labelTextView = (TextView) view.findViewById(R.id.number_picker_label);
        labelTextView.setText(getResources().getQuantityString(labelPluralString, value));

        final NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.number_picker);

        numberPicker.setMinValue(minValue);
        numberPicker.setMaxValue(maxValue);
        numberPicker.setValue(value);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                labelTextView.setText(getResources().getQuantityString(labelPluralString, newVal));
            }
        });

        builder.setTitle(title)
                .setView(view)
                .setPositiveButton(getString(R.string.number_picker_dialog_positive_text), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onPositiveClick(numberPicker.getValue());
                    }
                })
                .setNegativeButton(getString(R.string.dialog_negative_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    public void onPositiveClick(int result) {
        if (mHandler != null) {
            mHandler.handleNumberPickerDialogFragmentResult(result);
        }
    }

    public interface NumberPickerDialogFragmentResultHandler extends Serializable{
        void handleNumberPickerDialogFragmentResult(int result);
    }

}

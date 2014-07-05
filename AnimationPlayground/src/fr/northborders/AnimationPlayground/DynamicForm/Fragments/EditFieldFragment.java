package fr.northborders.AnimationPlayground.DynamicForm.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import fr.northborders.AnimationPlayground.DynamicForm.ExtendedEditText;
import fr.northborders.AnimationPlayground.DynamicForm.Listeners.EditTextFocusListener;
import fr.northborders.AnimationPlayground.R;

/**
 * Created by thibaultguegan on 31/05/2014.
 */
public class EditFieldFragment extends Fragment {
    private static final String TAG = EditFieldFragment.class.getName();

    private LinearLayout editTextContainer1;
    private ExtendedEditText editText1;

    private LinearLayout editTextContainer2;
    private ExtendedEditText editText2;

    private EditTextFocusListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.edit_field_fragment, null);

        editTextContainer1 = (LinearLayout) view.findViewById(R.id.editTextContainer1);
        editText1 = (ExtendedEditText) view.findViewById(R.id.editText1);

        editTextContainer2 = (LinearLayout) view.findViewById(R.id.editTextContainer2);
        editText2 = (ExtendedEditText) view.findViewById(R.id.editText2);

        editText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    //Log.d(TAG, "Edit1 Text has focus");
                    listener.focusOn(view, editTextContainer1, true);
                }else {
                    Log.d(TAG, "Edit1 does not have focus");
                    listener.focusOff(view, editTextContainer1, true);
                }
            }
        });

        editText2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Log.d(TAG, "Edit2 Text has focus");
                    listener.focusOn(view, editTextContainer2, true);
                }else {
                    Log.d(TAG, "Edit2 does not have focus");
                    listener.focusOff(view, editTextContainer2, true);
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(android.view.View view, android.os.Bundle savedInstanceState)
    {

    }

    public void setListener(EditTextFocusListener listener) {
        this.listener = listener;
    }

}

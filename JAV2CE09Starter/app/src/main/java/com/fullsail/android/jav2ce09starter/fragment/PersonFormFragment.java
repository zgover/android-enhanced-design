package com.fullsail.android.jav2ce09starter.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fullsail.android.jav2ce09starter.R;
import com.fullsail.android.jav2ce09starter.object.Person;

public class PersonFormFragment extends Fragment {

    public static final String TAG = "PersonFormFragment.TAG";

    /**
     * Factory method for creating a PersonFormFragment.
     * Not necessary right now, but helpful if you need to add arguments.
     *
     * @return A new PersonFormFragment.
     */
    public static PersonFormFragment newInstance() {
        PersonFormFragment fragment = new PersonFormFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    /**
     * Helper method for converting all form values into a Person object.
     *
     * @return The resulting person or null if the form is incomplete.
     */
    public Person getPersonFromForm() {

        // Make sure the root view is available.
        View view = getView();
        if(view == null) {
            return null;
        }

        String first = null;
        String last = null;
        int age = -1;

        // Get each value from the form, one at a time.
        EditText et = (EditText)view.findViewById(R.id.edit_first);
        if(et != null) {
            first = et.getText().toString();
        }

        et = (EditText)view.findViewById(R.id.edit_last);
        if(et != null) {
            last = et.getText().toString();
        }

        et = (EditText)view.findViewById(R.id.edit_age);
        if(et != null) {
            // Make sure we have a valid int value passed in.
            String ageString = et.getText().toString();
            try {
                age = Integer.parseInt(ageString);
            } catch(NumberFormatException e) {
                age = -1;
                e.printStackTrace();
            }
        }

        // Return null if the form is incomplete.
        if(first == null || first.trim().length() == 0 ||
                last == null || last.trim().length() == 0 ||
                age == -1) {
            return null;
        }

        // Return the resulting person if the form is complete.
        return new Person(first, last, age);
    }
}

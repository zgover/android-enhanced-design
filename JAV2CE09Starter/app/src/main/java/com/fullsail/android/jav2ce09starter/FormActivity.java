package com.fullsail.android.jav2ce09starter;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.fullsail.android.jav2ce09starter.fragment.PersonFormFragment;
import com.fullsail.android.jav2ce09starter.object.Person;
import com.fullsail.android.jav2ce09starter.util.PersonUtil;

public class FormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        if(savedInstanceState == null) {
            PersonFormFragment fragment = PersonFormFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment, PersonFormFragment.TAG)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_save) {
            PersonFormFragment fragment = (PersonFormFragment)getFragmentManager()
                    .findFragmentByTag(PersonFormFragment.TAG);

            // Getting a person from the form fragment.
            // A null value represents an incomplete form.
            Person person = fragment.getPersonFromForm();
            if(person != null) {
                // Save the person and set the proper result.
                PersonUtil.savePerson(this, person);
                setResult(RESULT_OK);
                finish();
            } else {
                // Show an error message if the form is incomplete.
                showErrorDialog();
            }
        }

        return true;
    }

    /**
     * Helper method for showing an error dialog.
     */
    private void showErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.form_incomplete)
                .setMessage(R.string.please_complete_form)
                .setPositiveButton(R.string.ok, null)
                .show();
    }
}

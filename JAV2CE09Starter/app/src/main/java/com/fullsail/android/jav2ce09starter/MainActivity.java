package com.fullsail.android.jav2ce09starter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.fullsail.android.jav2ce09starter.fragment.PersonListFragment;
import com.fullsail.android.jav2ce09starter.object.Person;
import com.fullsail.android.jav2ce09starter.util.PersonUtil;

public class MainActivity extends AppCompatActivity implements
        PersonListFragment.OnPersonInteractionListener, AdapterView.OnItemSelectedListener {

    private static final int REQUEST_FORM = 0x01001;

    private int mCurrentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up the filter options spinner.
        Spinner filterSpinner = (Spinner)findViewById(R.id.filterSpinner);
        String[] filterValues = getResources().getStringArray(R.array.spinner_values);
        filterSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filterValues));
        filterSpinner.setOnItemSelectedListener(this);

        // Assigning the default filter.
        mCurrentFilter = PersonListFragment.FILTER_ALL;

        // Adding our list fragment one time only.
        if(savedInstanceState == null) {
            PersonListFragment fragment = PersonListFragment.newInstance(mCurrentFilter);
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment, PersonListFragment.TAG)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_add) {
            Intent intent = new Intent(this, FormActivity.class);
            startActivityForResult(intent, REQUEST_FORM);
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Only refresh the list if a save operation was successful.
        if(requestCode == REQUEST_FORM && resultCode == RESULT_OK) {
            refreshList();
        }
    }

    @Override
    public void onPersonClicked(Person p) {
        Toast.makeText(this, p.getFullName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPersonLongClicked(final Person p) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete)
                .setMessage(R.string.confirm_delete_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PersonUtil.deletePerson(MainActivity.this, p);
                        refreshList();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    /**
     * Refreshes the current list fragment with the most recent filter.
     */
    private void refreshList() {
        PersonListFragment fragment = (PersonListFragment) getFragmentManager()
                .findFragmentByTag(PersonListFragment.TAG);
        if(fragment != null) {
            fragment.refresh(mCurrentFilter);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // Switching out the filter when a new spinner option is selected.
        mCurrentFilter = i;
        refreshList();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

package com.fullsail.android.jav2ce09starter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import com.fullsail.android.jav2ce09starter.fragment.PersonListFragment;
import com.fullsail.android.jav2ce09starter.object.Person;
import com.fullsail.android.jav2ce09starter.util.PersonUtil;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity implements
        PersonListFragment.OnPersonInteractionListener, AdapterView.OnItemSelectedListener {

    private static final int REQUEST_FORM = 0x01001;

    private int mCurrentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // Setting up the filter options spinner.
//        Spinner filterSpinner = (Spinner)findViewById(R.id.filterSpinner);
//        String[] filterValues = getResources().getStringArray(R.array.spinner_values);
//        filterSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filterValues));
//        filterSpinner.setOnItemSelectedListener(this);

        // Assigning the default filter.
        mCurrentFilter = PersonListFragment.FILTER_ALL;

        // Adding our list fragment one time only.
        if(savedInstanceState == null) {
            PersonListFragment fragment = PersonListFragment.newInstance(mCurrentFilter);
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment, PersonListFragment.TAG)
                    .commit();
        }

        // Configure the custom toolbar
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        final ActionBar ab = getSupportActionBar();

        ab.setTitle("People");
        ab.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        ab.setDisplayHomeAsUpEnabled(true);

        ((BottomBar) findViewById(R.id.bottomBar)).setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.all) {
                    mCurrentFilter = 0;
                } else if (tabId == R.id.less_than) {
                    mCurrentFilter = 1;
                } else if (tabId == R.id.greater_than) {
                    mCurrentFilter = 2;
                }

                refreshList();
            }
        });
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
        final Snackbar snack = Snackbar.make(findViewById(R.id.mainActivityCoord), p.getFullName(), Snackbar.LENGTH_LONG);
        snack.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snack.dismiss();
            }
        }).setActionTextColor(Color.parseColor("#FFFFFF")).show();
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

    public void addPerson(View view) {
        Intent intent = new Intent(this, FormActivity.class);
        startActivityForResult(intent, REQUEST_FORM);
    }
}

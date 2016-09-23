package com.fullsail.android.jav2ce09starter.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fullsail.android.jav2ce09starter.R;
import com.fullsail.android.jav2ce09starter.object.Person;
import com.fullsail.android.jav2ce09starter.util.PersonUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public class PersonListFragment extends ListFragment implements AdapterView.OnItemLongClickListener {

    // Don't be scared by the many annotations. This is here to ensure that you only
    // ever use a proper filter value with the newInstance or refresh methods.
    // Think of this like an enum, but without the enum keyword.
    @Retention(RetentionPolicy.CLASS)
    @IntDef({FILTER_ALL, FILTER_UNDER_30, FILTER_30_UP})
    public @interface FilterMode {}
    public static final int FILTER_ALL = 0;
    public static final int FILTER_UNDER_30 = 1;
    public static final int FILTER_30_UP = 2;

    public static final String TAG = "PersonListFragment.TAG";

    private static final String ARG_FILTER = "PersonListFragment.ARG_FILTER";

    /**
     * Factory method for creating a PersonListFragment with appropriate bundle arguments.
     *
     * @param filter Defines the data to be shown based on one of the FilterMode values.
     * @return A new PersonListFragment with the proper arguments set.
     */
    public static PersonListFragment newInstance(@FilterMode int filter) {
        PersonListFragment fragment = new PersonListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FILTER, filter);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Should be implemented in any containing activity. Used to handle item clicks.
     */
    public interface OnPersonInteractionListener {
        void onPersonClicked(Person p);
        void onPersonLongClicked(Person p);
    }

    private OnPersonInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getListenerFromContext(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getListenerFromContext(activity);
    }

    /**
     * onAttach(Context) is only available in API 23+ and onAttach(Activity)
     * is used for older versions. This method unifies the two onAttach methods
     * so that the proper functionality is called on all platforms.
     *
     * @param context The context from either onAttach method.
     */
    private void getListenerFromContext(Context context) {
        if(context instanceof OnPersonInteractionListener) {
            mListener = (OnPersonInteractionListener)context;
        } else {
            throw new ClassCastException("Containing activity must " +
                    "implement OnPersonInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(getString(R.string.no_people_available));

        // ListFragment has a helper for regular item clicks, but not long clicks.
        // Therefore, we must attach our own long click listener.
        ListView lv = getListView();
        if(lv != null) {
            lv.setOnItemLongClickListener(this);
        }

        // Getting the filter that was passed into the fragment via newInstance.
        Bundle args = getArguments();
        @FilterMode int filter = args.getInt(ARG_FILTER, FILTER_ALL);

        // Update the list with the currently loaded data.
        refresh(filter);
    }

    /**
     * Helper method for loading data, filtering it, and showing it in the list.
     *
     * @param filter FilterMode value that defines what data should show in the list.
     */
    public void refresh(@FilterMode int filter) {

        ArrayList<Person> people = PersonUtil.loadPeople(getActivity());
        people = filterPeople(people, filter);

        ArrayAdapter<Person> peopleAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, people);
        setListAdapter(peopleAdapter);
    }

    /**
     * Helper method for taking a list of all saved data and filtering it to match
     * the passed in FilterMode value.
     *
     * @param people The list of people to be filtered.
     * @param filter How the list should be filtered.
     * @return The filtered list of people.
     */
    private ArrayList<Person> filterPeople(ArrayList<Person> people, @FilterMode int filter) {
        if(filter == FILTER_ALL) {
            return people;
        }

        ArrayList<Person> filtered = new ArrayList<>();
        for(Person p : people) {
            if(filter == FILTER_UNDER_30 && p.getAge() < 30) {
                filtered.add(p);
            } else if(filter == FILTER_30_UP && p.getAge() >= 30) {
                filtered.add(p);
            }
        }
        return filtered;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Person person = (Person)l.getAdapter().getItem(position);
        mListener.onPersonClicked(person);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        Person person = (Person)adapterView.getAdapter().getItem(position);
        mListener.onPersonLongClicked(person);
        return true;
    }
}

package com.fullsail.android.jav2ce09starter.object;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Person {

    private static final String KEY_FIRST = "first";
    private static final String KEY_LAST = "last";
    private static final String KEY_AGE = "age";

    private String mFirstName;
    private String mLastName;
    private int mAge;

    public Person() {
        mFirstName = mLastName = "";
        mAge = -1;
    }

    public Person(String first, String last, int age) {
        this();
        mFirstName = first;
        mLastName = last;
        mAge = age;
    }

    /**
     * Constructor for creating a person out of a loaded JSONObject.
     * Corresponds to the data returned from getPersonAsJSON().
     *
     * @param person JSONObject with all person data.
     */
    public Person(JSONObject person) {
        try {
            mFirstName = person.getString(KEY_FIRST);
            mLastName = person.getString(KEY_LAST);
            mAge = person.getInt(KEY_AGE);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts the person and all parameters to JSON for storage.
     * This is done since we're purposely not using regular serialization.
     *
     * @return A JSONObject representing this person.
     */
    public JSONObject getPersonAsJSON() {
        JSONObject person = new JSONObject();

        try {
            person.put(KEY_FIRST, mFirstName);
            person.put(KEY_LAST, mLastName);
            person.put(KEY_AGE, mAge);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return person;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public int getAge() {
        return mAge;
    }

    @Override
    public String toString() {
        return getFullName() + " - " + getAge();
    }

    @Override
    public boolean equals(Object obj) {

        // Not equals if they're not the same type.
        if(!(obj instanceof Person)) {
            return false;
        }

        Person other = (Person)obj;

        // All member variables must match to be equal.
        return Objects.equals(mFirstName, other.mFirstName) &&
                Objects.equals(mLastName, other.mLastName) &&
                mAge == other.mAge;
    }
}

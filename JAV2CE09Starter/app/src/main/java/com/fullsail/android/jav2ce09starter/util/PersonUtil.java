package com.fullsail.android.jav2ce09starter.util;

import android.content.Context;

import com.fullsail.android.jav2ce09starter.object.Person;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PersonUtil {

    private static final String PERSON_FILE = "people.dat";

    /**
     * Helper method for saving a single person to internal storage.
     *
     * @param context Needed to access internal storage.
     * @param person The person to be saved.
     */
    public static void savePerson(Context context, Person person) {

        ArrayList<Person> people = loadPeople(context);
        people.add(person);
        savePeople(context, people);
    }

    /**
     * Helper method for deleting all matching people from internal storage.
     *
     * @param context Needed to access internal storage.
     * @param person The person to be deleted.
     */
    public static void deletePerson(Context context, Person person) {
        ArrayList<Person> people = loadPeople(context);
        while(people.remove(person));
        savePeople(context, people);
    }

    /**
     * Private helper method for resaving the entire list of people after
     * an add or delete action occurs.
     *
     * @param context Needed to access internal storage.
     * @param people The list of people to be saved.
     */
    private static void savePeople(Context context, ArrayList<Person> people) {
        JSONArray peopleJson = new JSONArray();

        for(Person person : people) {
            peopleJson.put(person.getPersonAsJSON());
        }

        try {
            FileOutputStream fos = context.openFileOutput(PERSON_FILE, Context.MODE_PRIVATE);
            fos.write(peopleJson.toString().getBytes());
            fos.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method for loading all person data from internal storage
     * and parsing it from JSON and converting it to a list of Person objects.
     *
     * @param context Needed to access internal storage.
     * @return The list of people as retrieved from internal storage.
     */
    public static ArrayList<Person> loadPeople(Context context) {

        ArrayList<Person> people = null;

        try {
            FileInputStream fis = context.openFileInput(PERSON_FILE);
            String data = IOUtils.toString(fis);
            fis.close();

            JSONArray peopleJson = new JSONArray(data);
            people = new ArrayList<>();

            for(int i = 0; i < peopleJson.length(); i++) {
                JSONObject obj = peopleJson.getJSONObject(i);
                Person person = new Person(obj);
                people.add(person);
            }

        } catch(IOException | JSONException e) {
            e.printStackTrace();
        }

        if(people == null) {
            people = new ArrayList<>();
        }

        return people;
    }
}

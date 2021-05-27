package com.sdp.movemeet.models;

import com.sdp.movemeet.utility.UserDatabase;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserDatabaseTest {
    @Test
    public void DatabaseConstructorIsCorrect() throws IOException {
        UserDatabase db = new UserDatabase();

        db.cleanFile();
    }

    @Test
    public void AddingUsersIsCorrect() throws IOException {
        UserDatabase db = new UserDatabase();

        db.addUser("hello", "world");
        db.addUser("hello", "world");
        assertEquals(true, db.isInUsers("hello"));
        assertEquals(false, db.isInUsers("goodbye"));
    }

    @Test
    public void SavingAndLoadingIsCorrect() throws IOException {
        UserDatabase db = new UserDatabase();

        db.addUser("hello", "world");

        db.writeToFile();
        db.readFromFile();

        db.cleanFile();
    }

    @Test
    public void PersistenceTest() throws IOException {
        UserDatabase db = new UserDatabase();

        db.addUser("hello", "world");

        db.writeToFile();
        db.readFromFile();

        assertEquals(true, db.isInUsers("hello"));
        assertEquals(false, db.isInUsers("goodbye"));

        db.cleanFile();
    }

}

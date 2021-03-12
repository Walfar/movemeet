package com.sdp.movemeet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserDatabaseTest {
    @Test
    public void DatabaseConstructorIsCorrect() {
        UserDatabase db = new UserDatabase();
    }

    @Test
    public void AddingUsersIsCorrect() {
        UserDatabase db = new UserDatabase();

        db.addUser("hello", "world");
        assertEquals(true, db.isInUsers("hello"));
        assertEquals(false, db.isInUsers("goodbye"));
    }

    @Test
    public void SavingAndLoadingIsCorrect() {
        UserDatabase db = new UserDatabase();
        db.readFromFile();
        db.writeToFile();
    }


}
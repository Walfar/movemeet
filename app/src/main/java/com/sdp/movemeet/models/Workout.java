package com.sdp.movemeet.models;

/**
 * This class represents a workout.
 */
public class Workout {
    String title;
    String desc;
    int icon;

    /**
     * Constructor for a new workout
     * @param title : Workout title
     * @param desc  : Workout description
     * @param icon  : Workout icon
     */
    public Workout(String title, String desc, int icon) {
        this.title = title;
        this.desc = desc;
        this.icon = icon;
    }

    /**
     * @return the title of the workout
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @return the description of the workout
     */
    public String getDesc() {
        return this.desc;
    }

    /**
     * @return the icon of the workout
     */
    public int getIcon() {
        return this.icon;
    }
}

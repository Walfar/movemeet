## Summary for week 5
 * * *


### Anthony
 * * *
This week I dived into the development of the backend for the chat of our application. I looked for different ways of doing it and implemented different solutions on mock apps integrating Firebase for easy testing.

After quite a lot of research, I finally agreed on a way to implement it using Firebase Realtime Database. Despite a mock version of the chat, I had nothing to merge into the main branch this week.

Next week, I am going to finalize the implementation of the chat backend and implement its real functionality.


### Jean-Luc
* * *
This week, I improved the tests of what was implemented. We had a little bit mismanaged these tests so this exceptional improvement of the tests is to start again on good basis with a better coverage answering the requirements and thus not to be penalized by the previous bad tests. This also allows us to better understand the function of the tests in order to make better ones later on.

I underestimated a bit the time needed to rectify all the tests and better understand the functioning but this experience will be very useful for the weeks to come.

Next week, I will probably work on frontend to implement a sidebar/menu for connected users.


### Kepler
 * * *
This week, I implemented an Activity through which the user can track their own live location on the map and record a path that they have travelled.

My time estimation was poor, as dealing with the map and Maps API turned out to be more difficult than I had anticipated.

Next week, I will keep working on this Activity to better integrate it into the app and add more information to it.


### Mark
* * *
This week I finalized my work on the DistanceCalculator class and was able to merge it with the GUI of the app. This functionality is now more optimized and thoroughly tested.

I have slightly misjudged the time estimates, because I ran into a lot of problems with the Android emulator. I will take these difficulties into account when working on future Sprints.

Since the User Stories linked to this functionality are complete, next week I am planning on working on a different part of the codebase, possibly the workout-related stories.


### Roxane
* * *
This week I implemented the layout of our future chat activity based on our discussions with Anthony.

I also created the Message class in order to properly handle the Message objects and later display useful information to the user

Next week, I'll look at making this layout consistent with the finally implemented version of the backend. I will also take the time to develop tests for the chat layout.


### Victor
* * *
This week, I made the logged user able to access the map. I added the ActivitiesUpdater class to retrieve the
activities from the database and add them to a list. This list is then displayed on the map. When a user adds an activity, the list of activities in the database is updated.
Also, I've worked with Mark on displaying only the markers in a given parameter (the top 4
activities near the user). 
This week, the time spent on each task was fitting.

Next week, I intend to let the user choose the location of the activity by clicking
directly on the map. I'll also make the current location of the user updates in real
time when he is moving.




### Overall team
* * *
From a Scrum master perspective, we experienced great cohesion and teamwork. The standup meetings were long enough to exchange the main information about each other's progress. This week, we tackled big parts of our app (such as the chat) and consequently we have not a lot of new functional features to present. Due to the time taken to look for information to correctly implement important new features, our time estimate was a bit as usual.

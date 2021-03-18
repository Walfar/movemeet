## Summary for week 4
 * * *

### Roxane
* * *
This week I have implemented two screens, one for  showing an activity for a register user and the other for an unregister user. My part joins the work of Jean-Luc and Victor.

This week the time estimation was good.

Next week, I will choose a task in backend, because I make front end this week.


### Jean-Luc
* * *
This week, I have implemented the backend part to show activity and the possibility for a user to register for an activity

The time to create good tests was a bit underestimated and should be better taken into account next time.

Next week, I'll probably try to increase the test coverage, and alternate with frontend


### Victor
* * *
This week, I’ve added markers to the map associated to the activities. The markers are retrieved from a list of activities that is arbitrary for the moment. Each marker has an icon that corresponds to the sport of the activity. On click, the marker brings to the description of the activity (that Jean-Luc and Roxane implemented). 

Once again, I underestimated the time to make tests for the map. It is hard to figure out how exactly such a class can be tested, as all its methods are mostly callbacks, and we keep losing coverage because of this.

Next week, I will make the list of activities being retrieved from the database and implement a maximum perimeter for visible activities chosen by the user.


### Kepler
 * * *
This week, I implemented a class capable of converting an Activity to and from a Map<String, Object> for easier serialization and fine-tuning attributes. I also implemented an Android Activity allowing registered users to upload activities to the service.

Because I decided to use Pickers to allow the user to select date and time parameters, I had difficulty writing good tests and spent a lot more time on that aspect than I had estimated or would have liked.

Next week, I will switch back to a more backend-focused task, as well as aim to improve code coverage across the project.


### Mark
* * *

This week, I have finalized the UserDatabase class and linked it with the Register activity, which now saves a newly logged user into the local cache. 
I have also created a new class for efficiently calculating the distance between the user and a list of sport activities. However, I still have to fully test this code.

Next week, I will work on linking this class with the Map activity, so that this functionality becomes available to the users.

### Anthony
 * * *
This week, I have implemented two activities: ProfileActivity and EditProfileActivity. The first allows the user to access his profile data and the latter can be used to update the user data stored on Firebase Firestore and Cloud Storage (user picture, full name, email, phone number and description).

Again, the time taken for creating good tests and hence improving the code coverage might have been better taken into account.

Next week, I will probably try to integrate additional features to the ProfileActivity (verify email and change password) and probably look for info about how to implement a step counter in Android.


### Overall team
* * *
Overall, there has been a great cohesion, and the stand-up meetings went really well. Everyone was able to do what they wanted to do. Our time estimate was again a bit high, because of the tests. 


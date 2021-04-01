# movemeet

### SDP spring semester 2021

[![Build Status](https://api.cirrus-ci.com/github/movemeet/movemeet.svg)](https://cirrus-ci.com/github/movemeet/movemeet)

This week, I've worked on letting the organizer choose the location of his activity directly by clicking on the map. This is now possible when accessing the map, with
the requirment of being logged in, or also by using a newly added button in the fragment for the activity creation. For the moment, the intent doesn't save the text 
of the fields, which is cumbersome, but I intend to resolve this problem during the holidays. 
The problem is that my implementations uses a lot of different intents that made some tests failed, and I still have to figure out why. I also found a few problems that
I wanted to report, and that could be interesting to work on for the weeks to come, such as the fact that the description is not updated in the activity details fragment,
or that the list of activities is not always displayed when the user is not logged in. I also thought about changing the Home screen layout when the user is already logged 
in, since quitting the app doesn't log him out, and it doesn't make sense to let the logged user have access to a login fragment.

For the holidays, I intend to resolve the different problems that I stated above. Then, for the next week, I'll be able to work on the location of the user being updated in
real-time, and maybe also start working on the cache.

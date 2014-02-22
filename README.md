====================
CS275 Midterm Practicum: Weather App
====================

====================
   HOW IT WORKS
====================

My weather app identifies the location of the phone, and then it pulls
the data from wunderground.com. The location is being determined
automatically by either using the IP address of the phone in order to get
the ZIP code (through freegeoip.com), or by using the GPS connection, to get
the latitude and longitude.

The next step is to pull the forecast for the next 10 days from
wunderground.com. The weather forecast response is serializes to JSON, but
the application will identify the useful data from the JSON string. Then,
the data is displayed on the screen.

In case there is no internet connection at all, an error message will be
shown right away.


===================
   HOW TO RUN IT
===================

The app was built in Eclipse. The file UWeather.zip contains the entire
project. GSON library was also used for deserializing JSON, so make sure
that is still in the build path. Next, an emulator could be used.


==================
   CHALLENGES
==================

I tried to put a little bit of effort in the UI and make it look better.
Therefore, I looked up some ways I could add images that represent the state
of the weather for a certain day. This was a little bit challenging,
because the images are first downloaded from WUnderground.com and then
displayed on the screen.

Another challenge was to create a list with a custom entry design.
Therefore, I had to create a custom list adaper, and a layout entry. Finally
the list containing an image, day and date, summary and temperature was
shown in a way that is friendly and easy to read.


==================
   DOCUMENTAION
==================

The documentation could be easily generated using JavaDoc. I commented every
single function and procedure, explained what they do, what parameters they
take and what return values mean.

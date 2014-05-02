Welcome to weatherservice's documentation!
==========================================

The WeatherService library offers access to the National Weather Service for the
United States. You can either get information via latitude and longitude, or
by passing in an address (which will be geocoded through Google). This library
can work online (connected) or offline (disconnected). When offline, only certain
addresses are available.

>>> from weatherservice import weatherservice
>>> weatherservice.connect() #Starts offline by default
>>> report = weatherservice.get_report("New York, NY")
>>> report
<weatherservice.weatherservice.Report object>
>>> report.weather.temp
41
>>> weatherservice.get_report("Tokoyo, China")
weatherservice.weatherservice.WeatherException: This city was outside of the continental United States.
>>> weatherservice.disconnect()
>>> weatherservice.get_report("New York, NY").weather.temp
88
>>> weatherservice.get_report("Newark, DE")
weatherservice.weatherservice.GeocodeException: The given city was not in the cache.


To run the unit tests from the command line:

>>> python -m tests.test


Further documentation is available in the `docs/_builds/index.html` file.


Potential changes:
    created field
        -> epoch time: no changes required (default)
        -> python datetime object
            datetime.fromtimestamp(parse_int(data['created'], 0))
        -> human-readable string:
            datetime.fromtimestamp(parse_int(data['created'], 0)).strftime('%Y-%m-%d %H:%M:%S')
    
    Comments
        Currently, all comments are loaded recursively, but you might choose to only get a swath of them

Other fields of interest
    'selftext_html'
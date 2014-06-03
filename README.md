CalendarAdapter
==========

This library provides a set of views to display a calendar with symbols representing events and an adapter to
manage them in a ListView.

The API uses the builder pattern to construct the adapter with several (optional) customizable parameters.

## Overview

Add a `ListView` to your XML layout file:

```xml
<ListView
    android:id="@+id/calendar_list_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

Grab a reference to it:

```java
ListView calendarListView = (ListView) findViewById(R.id.calendar_list_view);
```

Construct a `CalendarAdapter` with `CalendarAdapter.Builder`:

```java
CalendarAdapter adapter = new CalendarAdapter.Builder(this)
        .startDate(new Date())
        .eventDates(eventDates)
        .onDateSelectedListener(this)
        .create();
```

Then hand off the `CalendarAdapter` to your `ListView` and you're good to go:

```java
calendarListView.setAdapter(adapter);
```

## Customization

There are several additional parameters you can pass to the `CalendarAdapter.Builder`.

##### startDate(Date startDate)

Any date in the first month to be displayed.

##### endDate(Date endDate)

Any date in the last month to be displayed.

##### titleTypeface(Typeface titleTypeface)

The typeface for the name of the month (i.e. January, February).

##### daysOfWeekTypeface(Typeface daysOfWeekTypeface)

The typeface for the days of the week (i.e. Sun, Mon, Tue).

##### calendarCellTypeface(Typeface calendarCellTypeface)

The typeface for the numbers in the calendar cells.

##### titleTextColor(int titleTextColor)

The text color for the name of the month (i.e. January, February).

##### daysOfWeekTextColor(int daysOfWeekTextColor)

The text color for the days of the week (i.e. Sun, Mon, Tue).

##### calendarCellTextColor(int calendarCellTextColor)

The text color for the numbers in the calendar cells.

##### eventColor(int eventColor)

The color of the squares to symbolize events in the calendar cells.

##### pastFutureCalendarCellBackgroundColor(int pastFutureCalendarCellBackgroundColor)

The color for the background of the calendar cells outside of the current displayed month.

##### pastFutureCalendarCellTextColor(int pastFutureCalendarCellTextColor)

The text color for the calendar cells outside of the current displayed month.

##### pastFutureEventColor(int pastFutureEventColor)

The color of the squares to symbolize events in calendar cells outside of the current displayed month.

##### onDateSelectedListener(OnDateSelectedListener listener)

A listener that implements the method:

```java
public void onDateSelected(Date date)
```

##### eventDates(List<Date> eventDates)

A `List` of Java `Date` objects representing an event.  These are shown as colored squares on the calendar.


## License

The MIT License (MIT)

Copyright (c) 2014 Avocado Software, Inc.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

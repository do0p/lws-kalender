package ws.lernwerkstatt.cal.server.dao.impl;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ws.lernwerkstatt.cal.server.dao.EventDao;
import ws.lernwerkstatt.cal.server.util.EventServiceException;
import ws.lernwerkstatt.cal.shared.Event;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

public class EventDaoImpl implements EventDao {

	private static final String TIMEZONE_VIENNA = "Europe/Vienna";
	private static final String FIELDS = "items(id, summary, description, start, end,  location)";
	private final Calendar calendar;

	public EventDaoImpl(HttpRequestInitializer requestInitializer) {
		this.calendar = new Calendar(new NetHttpTransport(),
				new JacksonFactory(), requestInitializer);
	}

	@Override
	public List<Event> getEvents(String calendarId, Date pStartDate,
			Date pEndDate) throws EventServiceException {
		try {
			return map(calendar.events().list(calendarId).setFields(FIELDS)
					.setTimeMin(convertToDateTime(pStartDate))
					.setTimeMax(convertToDateTime(pEndDate)).execute());

		} catch (IOException e) {
			throw new EventServiceException(
					"could not get Events with startdate " + pStartDate
							+ " and enddate " + pEndDate, e);
		}
	}

	@Override
	public String storeEvent(String calendarId, Event event)
			throws EventServiceException {
		try {
			return calendar.events().insert(calendarId, map(event)).execute()
					.getId();
		} catch (IOException e) {
			throw new EventServiceException("could not store event " + event
					+ " in calendar " + calendarId, e);
		}
	}

	@Override
	public void removeEvent(String calendarId, String eventId)
			throws EventServiceException {
		try {
			calendar.events().delete(calendarId, eventId).execute();
		} catch (IOException e) {
			throw new EventServiceException("could not delete eventId "
					+ eventId + " from calendar " + calendarId, e);
		}
	}

	@Override
	public void updateEvent(String calendarId, Event pEvent)
			throws EventServiceException {
		try {
			calendar.events().update(calendarId, pEvent.getId(), map(pEvent)).execute();
		} catch (IOException e) {
			throw new EventServiceException("could not update event " + pEvent
					+ " in calendar " + calendarId, e);
		}

	}

	public String createCalendar(String name) throws IOException {
		com.google.api.services.calendar.model.Calendar content = new com.google.api.services.calendar.model.Calendar();
		content.setSummary(name);
		content.setTimeZone(TIMEZONE_VIENNA);
		return calendar.calendars().insert(content).execute().getId();
	}

	public void deleteCalendar(String id) throws IOException {
		calendar.calendars().delete(id).execute();
	}

	public List<String> listVisibleCalendarIds(String summary)
			throws IOException {
		CalendarList calendars = calendar.calendarList().list().execute();
		final List<String> ids = new ArrayList<String>();
		for (CalendarListEntry cal : calendars.getItems()) {
			if (cal.getSummary().equals(summary)) {
				ids.add(cal.getId());
			}
		}
		return ids;
	}

	private DateTime convertToDateTime(Date date) {
		return new DateTime(date, TimeZone.getTimeZone(TIMEZONE_VIENNA));
	}

	private EventDateTime convertToEventDateTime(Date date) {
		final EventDateTime eventDateTime = new EventDateTime();
		eventDateTime.setDateTime(convertToDateTime(date));
		eventDateTime.setTimeZone(TIMEZONE_VIENNA);
		return eventDateTime;
	}

	private List<Event> map(Events events) {
		final List<Event> result = new ArrayList<Event>();
		final List<com.google.api.services.calendar.model.Event> items = events
				.getItems();
		if (items != null && !items.isEmpty()) {
			for (com.google.api.services.calendar.model.Event jsonEvent : items) {
				result.add(map(jsonEvent));
			}
		}
		return result;
	}

	private com.google.api.services.calendar.model.Event map(Event event) {
		final com.google.api.services.calendar.model.Event jsonEvent = new com.google.api.services.calendar.model.Event();
		jsonEvent.setSummary(event.getTitle());
		jsonEvent.setDescription(event.getDescription());
		jsonEvent.setStart(convertToEventDateTime(event.getStartDate()));
		jsonEvent.setEnd(convertToEventDateTime(event.getEndDate()));
		jsonEvent.setLocation(event.getLocation());
		return jsonEvent;
	}

	private Event map(com.google.api.services.calendar.model.Event jsonEvent) {
		final Event event = new Event();
		event.setId(jsonEvent.getId());
		event.setTitle(jsonEvent.getSummary());
		event.setDescription(jsonEvent.getDescription());
		event.setStartDate(convert(jsonEvent.getStart()));
		event.setEndDate(convert(jsonEvent.getEnd()));
		event.setLocation(jsonEvent.getLocation());
		return event;
	}

	private Date convert(EventDateTime date) {
		return new Date(getDateOrDateTime(date).getValue());
	}

	public DateTime getDateOrDateTime(EventDateTime date) {
		return date.getDateTime() != null ? date.getDateTime() : date.getDate();
	}

	public static GoogleCredential createCredentialForServiceAccount(
			String serviceAccountId, File p12File,
			String... serviceAccountScopes) throws GeneralSecurityException,
			IOException {
		return new GoogleCredential.Builder()
				.setTransport(new NetHttpTransport())
				.setJsonFactory(new JacksonFactory())
				.setServiceAccountId(serviceAccountId)
				.setServiceAccountScopes(serviceAccountScopes)
				.setServiceAccountPrivateKeyFromP12File(p12File).build();
	}

}

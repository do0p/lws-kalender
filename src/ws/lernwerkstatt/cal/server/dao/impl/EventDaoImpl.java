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
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

public class EventDaoImpl implements EventDao {

	private static final String FIELDS = "items(id, summary, description, start, end,  location)";

	private final String calendarId;

	private final Calendar calendar;

	public EventDaoImpl(String calendarId,
			HttpRequestInitializer requestInitializer) {
		this.calendarId = calendarId;
		this.calendar = new Calendar(new NetHttpTransport(),
				new JacksonFactory(), requestInitializer);
	}

	@Override
	public List<Event> getEvents(Date pStartDate, Date pEndDate)
			throws EventServiceException {
		try {
			return map(calendar.events().list(calendarId)
					.setTimeMin(convert(pStartDate))
					.setTimeMax(convert(pEndDate)).setFields(FIELDS).execute());

		} catch (IOException e) {
			throw new EventServiceException(
					"could not get Events with startdate " + pStartDate
							+ " and enddate " + pEndDate, e);
		}
	}

	@Override
	public void storeEvent(Event pEvent) throws EventServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeEvent(Event pEvent) throws EventServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateEvent(Event pEvent) throws EventServiceException {
		// TODO Auto-generated method stub

	}

	private DateTime convert(Date pStartDate) {
		return new DateTime(pStartDate, TimeZone.getTimeZone("CET"));
	}

	private List<Event> map(Events events) {
		final List<Event> result = new ArrayList<Event>();
		for (com.google.api.services.calendar.model.Event jsonEvent : events
				.getItems()) {
			result.add(map(jsonEvent));
		}
		return result;
	}

	private Event map(com.google.api.services.calendar.model.Event jsonEvent) {
		final Event event = new Event();
		event.setDescription(jsonEvent.getDescription());
		event.setStartDate(convert(jsonEvent.getStart()));
		event.setEndDate(convert(jsonEvent.getEnd()));
		event.setTitle(jsonEvent.getSummary());
		event.setId(jsonEvent.getId());
		event.setLocation(jsonEvent.getLocation());
		return event;
	}

	private Date convert(EventDateTime start) {
		return new Date(getDateOrDateTime(start).getValue());
	}

	public DateTime getDateOrDateTime(EventDateTime start) {
		return start.getDateTime() != null ? start.getDateTime() : start
				.getDate();
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

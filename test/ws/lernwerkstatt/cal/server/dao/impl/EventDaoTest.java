package ws.lernwerkstatt.cal.server.dao.impl;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ws.lernwerkstatt.cal.shared.Event;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;

public class EventDaoTest {

	private static final int HOUR = 3600000;
	private static final long START_DATE_MILLIS = (System.currentTimeMillis() / 1000) * 1000;
	private static final String TITLE = "Test Event";
	private static final String DESCRIPTION = "Beschreibung mit Umlauten äöü und Sonderzeichen ?/\\@'\"€ß";
	private static final String TESTCALENDAR = "testcalendar";
	private static final String RAUMMIETE_CAL_ID = "raummiete.lws@gmail.com";
	private static final String CLIENT_ID = "418212430257@developer.gserviceaccount.com";
	private static final String KEY_FILE_PATH = "C:\\Users\\BrD\\git\\lws-kalender\\war\\WEB-INF\\key.p12";
	private static final Date START_DATE = new Date(START_DATE_MILLIS);
	private static final Date END_DATE = new Date(START_DATE_MILLIS + HOUR);
	private static final String LOCATION = "Event Location";

	private static EventDaoImpl eventDao;
	private static String testCalendarId;

	private Event event;

	@BeforeClass
	public static void setUpBeforeClass() throws GeneralSecurityException,
			IOException {
		GoogleCredential googleCredential = EventDaoImpl
				.createCredentialForServiceAccount(CLIENT_ID, new File(
						KEY_FILE_PATH), CalendarScopes.CALENDAR);
		eventDao = new EventDaoImpl(googleCredential);
		testCalendarId = eventDao.createCalendar(TESTCALENDAR);

	}

	@Before
	public void setUp() {
		event = new Event();
		event.setTitle(TITLE);
		event.setDescription(DESCRIPTION);
		event.setStartDate(START_DATE);
		event.setEndDate(END_DATE);
		event.setLocation(LOCATION);
	}

	@Test
	public void testGetRaummieteEvents() {
		final Date startDate = new Date(DateTime.parseRfc3339(
				"2012-10-22T00:00:00Z").getValue());
		final Date endDate = new Date(DateTime.parseRfc3339(
				"2012-10-31T00:00:00Z").getValue());
		final List<Event> events = eventDao.getEvents(RAUMMIETE_CAL_ID,
				startDate, endDate);
		Assert.assertEquals(6, events.size());
	}

	@Test
	public void testStoreAndDeleteEvent() {
		final String eventId = eventDao.storeEvent(testCalendarId, event);
		final List<Event> events = eventDao.getEvents(testCalendarId,
				START_DATE, END_DATE);
		Assert.assertEquals(1, events.size());
		final Event storedEvent = events.get(0);
		assertEvent(eventId, storedEvent);

		eventDao.removeEvent(testCalendarId, eventId);
		Assert.assertTrue(eventDao.getEvents(testCalendarId, START_DATE,
				END_DATE).isEmpty());
	}

	@Test
	public void testFindEvents() {
		eventDao.storeEvent(testCalendarId, event);
		Assert.assertTrue(eventDao.getEvents(testCalendarId,
				new Date(START_DATE_MILLIS - HOUR),
				new Date(START_DATE_MILLIS - HOUR / 2)).isEmpty());
		Assert.assertTrue(eventDao
				.getEvents(testCalendarId, new Date(START_DATE_MILLIS - HOUR),
						new Date(START_DATE_MILLIS)).isEmpty());
		Assert.assertEquals(
				1,
				eventDao.getEvents(testCalendarId,
						new Date(START_DATE_MILLIS - HOUR),
						new Date(START_DATE_MILLIS + HOUR / 2)).size());
		Assert.assertEquals(
				1,
				eventDao.getEvents(testCalendarId,
						new Date(START_DATE_MILLIS - HOUR),
						new Date(START_DATE_MILLIS + HOUR)).size());
		Assert.assertEquals(
				1,
				eventDao.getEvents(testCalendarId,
						new Date(START_DATE_MILLIS - HOUR),
						new Date(START_DATE_MILLIS + HOUR * 2)).size());
		Assert.assertEquals(
				1,
				eventDao.getEvents(testCalendarId, new Date(START_DATE_MILLIS),
						new Date(START_DATE_MILLIS + HOUR / 2)).size());
		Assert.assertEquals(
				1,
				eventDao.getEvents(testCalendarId, new Date(START_DATE_MILLIS),
						new Date(START_DATE_MILLIS + HOUR)).size());
		Assert.assertEquals(
				1,
				eventDao.getEvents(testCalendarId, new Date(START_DATE_MILLIS),
						new Date(START_DATE_MILLIS + HOUR * 2)).size());
		Assert.assertEquals(
				1,
				eventDao.getEvents(testCalendarId,
						new Date(START_DATE_MILLIS + HOUR / 4),
						new Date(START_DATE_MILLIS + HOUR / 2)).size());
		Assert.assertEquals(
				1,
				eventDao.getEvents(testCalendarId,
						new Date(START_DATE_MILLIS + HOUR / 4),
						new Date(START_DATE_MILLIS + HOUR)).size());
		Assert.assertEquals(
				1,
				eventDao.getEvents(testCalendarId,
						new Date(START_DATE_MILLIS + HOUR / 4),
						new Date(START_DATE_MILLIS + HOUR * 2)).size());
		Assert.assertTrue(eventDao.getEvents(testCalendarId,
				new Date(START_DATE_MILLIS + HOUR),
				new Date(START_DATE_MILLIS + HOUR * 2)).isEmpty());
		Assert.assertTrue(eventDao.getEvents(testCalendarId,
				new Date(START_DATE_MILLIS + HOUR * 2),
				new Date(START_DATE_MILLIS + HOUR * 3)).isEmpty());
	}

	public void assertEvent(final String eventId, final Event storedEvent) {
		Assert.assertEquals(eventId, storedEvent.getId());
		Assert.assertEquals(TITLE, storedEvent.getTitle());
		Assert.assertEquals(DESCRIPTION, storedEvent.getDescription());
		Assert.assertEquals(START_DATE, storedEvent.getStartDate());
		Assert.assertEquals(END_DATE, storedEvent.getEndDate());
		Assert.assertEquals(LOCATION, storedEvent.getLocation());
	}

	@Test
	public void testCreateAndDeleteCalendar() throws IOException {
		final String id = eventDao.createCalendar(TESTCALENDAR);
		Assert.assertTrue(eventDao.listVisibleCalendarIds(TESTCALENDAR)
				.contains(id));

		eventDao.deleteCalendar(id);
		Assert.assertFalse(eventDao.listVisibleCalendarIds(TESTCALENDAR)
				.contains(id));
	}

	public static void tearDown() throws IOException {
		eventDao.deleteCalendar(testCalendarId);
		final List<String> remainingTestCalendars = eventDao
				.listVisibleCalendarIds(TESTCALENDAR);
		if (!remainingTestCalendars.isEmpty()) {
			for (String calid : remainingTestCalendars) {
				eventDao.deleteCalendar(calid);
			}
		}
	}

}

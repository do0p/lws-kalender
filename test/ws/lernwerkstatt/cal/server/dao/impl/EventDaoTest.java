package ws.lernwerkstatt.cal.server.dao.impl;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ws.lernwerkstatt.cal.server.dao.EventDao;
import ws.lernwerkstatt.cal.shared.Event;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;

public class EventDaoTest {

	private static final String CLIENT_ID = "418212430257@developer.gserviceaccount.com";
	private static final String KEY_FILE_PATH = "C:\\Users\\BrD\\git\\lws-kalender\\war\\WEB-INF\\key.p12";
	
	private EventDao eventDao;
	
	@Before
	public void setUp() throws GeneralSecurityException, IOException
	{
		eventDao = new EventDaoImpl("raummiete.lws@gmail.com", EventDaoImpl.createCredentialForServiceAccount(CLIENT_ID, new File(KEY_FILE_PATH), CalendarScopes.CALENDAR_READONLY));
	}
	
	@Test
	public void testGetCalendar()
	{
		final Date startDate = new Date(DateTime.parseRfc3339("2012-10-22T00:00:00Z").getValue());
		final Date endDate = new Date(DateTime.parseRfc3339("2012-10-31T00:00:00Z").getValue());
		List<Event> events = eventDao.getEvents(startDate,endDate);
		Assert.assertEquals(6, events.size());
	}
	
}

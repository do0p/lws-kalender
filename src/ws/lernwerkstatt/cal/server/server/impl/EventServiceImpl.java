package ws.lernwerkstatt.cal.server.server.impl;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;

import ws.lernwerkstatt.cal.client.service.EventService;
import ws.lernwerkstatt.cal.server.dao.EventDao;
import ws.lernwerkstatt.cal.server.dao.impl.EventDaoImpl;
import ws.lernwerkstatt.cal.server.model.CalendarDescription;
import ws.lernwerkstatt.cal.shared.Event;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.calendar.CalendarScopes;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class EventServiceImpl extends RemoteServiceServlet implements
		EventService {

	private static final String RAUMMIETE_CAL_ID = "raummiete.lws@gmail.com";
	private static final String DEFAULT_CAL_ID = "418212430257@developer.gserviceaccount.com";

	private static final Map<String, CalendarDescription> CALENDARS;
	static {
		CALENDARS = new HashMap<String, CalendarDescription>();
		CALENDARS.put("Raummiete", new CalendarDescription(RAUMMIETE_CAL_ID, "blue",
				false));
		CALENDARS.put("Büro", new CalendarDescription(DEFAULT_CAL_ID, "green", true));
	}

	private static final String CLIENT_ID = "418212430257@developer.gserviceaccount.com";
	private static final String KEY_FILE_REL_PATH = "/WEB-INF/key.p12";
	private static final long serialVersionUID = -1946885535483288323L;

	private EventDao eventDao;

	@Override
	public void init() throws ServletException {
		super.init();

		final File keyFile = new File(getServletContext().getRealPath(
				KEY_FILE_REL_PATH));
		try {
			final HttpRequestInitializer requestInitializer = EventDaoImpl
					.createCredentialForServiceAccount(CLIENT_ID, keyFile,
							CalendarScopes.CALENDAR);
			eventDao = new EventDaoImpl(requestInitializer);
		} catch (GeneralSecurityException e) {
			throw new ServletException("could not create event dao", e);
		} catch (IOException e) {
			throw new ServletException("could not create event dao", e);
		}
	}

	@Override
	public List<Event> getEvents(Date pStart, Date pEnd) {
		final List<Event> result = new ArrayList<Event>();
		for (Entry<String, CalendarDescription> description : CALENDARS
				.entrySet()) {
			final List<Event> events = eventDao.getEvents(description
					.getValue().getCalendarId(), pStart, pEnd);
			for (Event event : events) {
				event.setCalendarName(description.getKey());
				event.setStyle(description.getValue().getStyle());
			}
			result.addAll(events);
		}
		return result;
	}

	@Override
	public void storeEvent(Event pEvent) {
		checkWriteAble(pEvent);
		eventDao.storeEvent(DEFAULT_CAL_ID, pEvent);
	}

	@Override
	public void updateEvent(Event pEvent) {
		checkWriteAble(pEvent);
		eventDao.updateEvent(DEFAULT_CAL_ID, pEvent);
	}

	@Override
	public void deleteEvent(Event pEvent) {
		checkWriteAble(pEvent);
		eventDao.removeEvent(DEFAULT_CAL_ID, pEvent.getId());
	}

	@Override
	public List<String> getWriteableCalendars() {
		final List<String> names = new ArrayList<String>();
		for (Entry<String, CalendarDescription> description : CALENDARS
				.entrySet()) {
			if (description.getValue().isWriteable()) {
				names.add(description.getKey());
			}
		}
		return names;
	}

	private void checkWriteAble(Event pEvent) {
		final CalendarDescription calendarDescription = CALENDARS.get(pEvent
				.getCalendarName());
		if (!calendarDescription.isWriteable()) {
			throw new IllegalArgumentException(pEvent.getCalendarName()
					+ " is not writeable");
		}
	}
}

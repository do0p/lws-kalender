package ws.lernwerkstatt.cal.server.server.impl;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import ws.lernwerkstatt.cal.client.EventService;
import ws.lernwerkstatt.cal.server.dao.EventDao;
import ws.lernwerkstatt.cal.server.dao.impl.EventDaoImpl;
import ws.lernwerkstatt.cal.shared.Event;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.calendar.CalendarScopes;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class EventServiceImpl extends RemoteServiceServlet implements
		EventService {

	private static final String RAUMMIETE_CAL_ID = "raummiete.lws@gmail.com";
	private static final String CLIENT_ID = "418212430257@developer.gserviceaccount.com";
	private static final String KEY_FILE_REL_PATH = "/WEB-INF/key.p12";
	private static final long serialVersionUID = -1946885535483288323L;

	private EventDao raummieteEvents;

	@Override
	public void init() throws ServletException {
		super.init();

		final File keyFile = new File(getServletContext().getRealPath(
				KEY_FILE_REL_PATH));
		try {
			final HttpRequestInitializer requestInitializer = EventDaoImpl.createCredentialForServiceAccount(
					CLIENT_ID, keyFile, CalendarScopes.CALENDAR_READONLY);
			raummieteEvents = new EventDaoImpl(RAUMMIETE_CAL_ID, requestInitializer);
		} catch (GeneralSecurityException e) {
			throw new ServletException("could not create event dao", e);
		} catch (IOException e) {
			throw new ServletException("could not create event dao", e);
		}
	}

	@Override
	public List<Event> getEvents(Date pStart, Date pEnd) {
		return raummieteEvents.getEvents(pStart, pEnd);
	}

	@Override
	public void storeEvent(Event pEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateEvent(Event pEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteEvent(Event pEvent) {
		// TODO Auto-generated method stub

	}

}

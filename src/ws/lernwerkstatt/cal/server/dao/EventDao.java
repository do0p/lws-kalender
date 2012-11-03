package ws.lernwerkstatt.cal.server.dao;

import java.util.Date;
import java.util.List;

import ws.lernwerkstatt.cal.server.util.EventServiceException;
import ws.lernwerkstatt.cal.shared.Event;

public interface EventDao {

	public abstract List<Event> getEvents(String calendarId, Date pStartDate, Date pEndDate) throws EventServiceException;

	public abstract String storeEvent(String calendarId, Event pEvent) throws EventServiceException;

	public abstract void removeEvent(String calendarId, String pEventId) throws EventServiceException;

	public abstract void updateEvent(String calendarId, Event pEvent) throws EventServiceException;

}
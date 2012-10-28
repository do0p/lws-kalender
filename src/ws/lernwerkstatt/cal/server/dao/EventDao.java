package ws.lernwerkstatt.cal.server.dao;

import java.util.Date;
import java.util.List;

import ws.lernwerkstatt.cal.server.util.EventServiceException;
import ws.lernwerkstatt.cal.shared.Event;

public interface EventDao {

	public abstract List<Event> getEvents(Date pStartDate, Date pEndDate) throws EventServiceException;

	public abstract void storeEvent(Event pEvent) throws EventServiceException;

	public abstract void removeEvent(Event pEvent) throws EventServiceException;

	public abstract void updateEvent(Event pEvent) throws EventServiceException;

}
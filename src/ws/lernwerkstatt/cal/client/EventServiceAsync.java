package ws.lernwerkstatt.cal.client;

import java.util.Date;
import java.util.List;

import ws.lernwerkstatt.cal.shared.Event;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EventServiceAsync {

	void deleteEvent(Event pEvent, AsyncCallback<Void> callback);

	void getEvents(Date pStart, Date pEnd, AsyncCallback<List<Event>> callback);

	void storeEvent(Event pEvent, AsyncCallback<Void> callback);

	void updateEvent(Event pEvent, AsyncCallback<Void> callback);

}

package ws.lernwerkstatt.cal.client;

import java.util.Date;

import ws.lernwerkstatt.cal.shared.Event;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DateField extends VerticalPanel {

	public DateField(Date date)
	{
		@SuppressWarnings("deprecation")
		final Label label = new Label(Integer.toString(date.getDate()));
		add(label);
	}

	public void addEevent(Event event) {
		add(new Label(event.toString()));
	}
	
}

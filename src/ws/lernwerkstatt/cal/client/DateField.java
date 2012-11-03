package ws.lernwerkstatt.cal.client;

import java.util.Date;

import ws.lernwerkstatt.cal.shared.Event;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DateField extends VerticalPanel {

	private DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("HH:mm");
	private final EventForm eventBox;

	@SuppressWarnings("deprecation")
	public DateField(final Date date, final EventForm eventBox) {
		this.eventBox = eventBox;
		setVerticalAlignment(ALIGN_TOP);
		
		
		final HorizontalPanel dateFieldHead = new HorizontalPanel();
		dateFieldHead.setSpacing(10);
		dateFieldHead.add(new Label(Integer.toString(date.getDate())));
		final Anchor newLink = new Anchor("neu");
		newLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBox.setupForNew();
				eventBox.showRelativeTo(newLink);
				eventBox.setStartDate(date);
			}
		});
		dateFieldHead.add(newLink);
		
		add(dateFieldHead);
		
	}

	public void addEevent(final Event event) {
		final Label eventLabel = new Label(formatEvent(event));
		eventLabel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent clickEvent) {
				eventBox.setupForEdit();
				eventBox.showRelativeTo(eventLabel);
				eventBox.setEvent(event);
			}
		});
		add(eventLabel);
	}

	private String formatEvent(Event event) {
		final String date;
		if (event.getStartDate() != null) {
			date = dateTimeFormat.format(event.getStartDate()) + " ";
		} else {
			date = "";
		}
		return date + event.getTitle();
	}

}

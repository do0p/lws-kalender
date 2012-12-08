package ws.lernwerkstatt.cal.client.util;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import ws.lernwerkstatt.cal.shared.Event;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class DateField extends FlowPanel {

	private DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("HH:mm");
	private final EventForm eventBox;
	private final Date day;
	private SortedMap<Date, Event> events = new TreeMap<Date, Event>();
	private VerticalPanel pane;

	@SuppressWarnings("deprecation")
	public DateField(final Date date, final EventForm eventBox) {

		this.day = date;
		this.eventBox = eventBox;
		
		pane = new VerticalPanel();
		add(pane);

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

		pane.add(dateFieldHead);

	}

	public void addEvent(Event event) {
		if (event.getStartDate() != null
				&& CalendarUtil.isSameDate(event.getStartDate(), day)) {
			events.put(event.getStartDate(), event);
		} else {
			events.put(day, event);
		}
	}


	public void formatDay() {
		for(final Event event : events.values())
		{
			final Label eventLabel = createLabelFromEvent(event);
			eventLabel.addClickHandler(new ClickHandler() {
			
				@Override
				public void onClick(ClickEvent clickEvent) {
					if (eventBox.getWriteableCalendars().contains(
							event.getCalendarName())) {
						eventBox.setupForEdit();
					} else {
						eventBox.setupForShow();
					}
					eventBox.showRelativeTo(eventLabel);
					eventBox.setEvent(event);
				}
			});
			pane.add(eventLabel);
		}

	}

	private Label createLabelFromEvent(Event event) {
		final String date;
		if (event.getStartDate() != null) {
			if (CalendarUtil.isSameDate(event.getStartDate(), day)) {
				date = dateTimeFormat.format(event.getStartDate()) + " ";
			} else {
				date = dateTimeFormat.format(day) + " ";
			}
		} else {
			date = "";
		}
		final Label label = new Label(date + event.getTitle());
		label.addStyleName(event.getStyle());
		return label;
	}

}

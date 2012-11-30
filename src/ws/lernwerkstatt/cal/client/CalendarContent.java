package ws.lernwerkstatt.cal.client;

import java.util.Date;
import java.util.List;

import ws.lernwerkstatt.cal.client.service.EventService;
import ws.lernwerkstatt.cal.client.service.EventServiceAsync;
import ws.lernwerkstatt.cal.client.util.CalenderMonth;
import ws.lernwerkstatt.cal.client.util.EventForm;
import ws.lernwerkstatt.cal.client.util.EventForm.UpdateListener;
import ws.lernwerkstatt.cal.client.util.PopUp;
import ws.lernwerkstatt.cal.shared.Event;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CalendarContent extends VerticalPanel implements UpdateListener {

	private final EventServiceAsync eventService = GWT.create(EventService.class);
	private final EventForm eventForm;
	private final ListBox yearSelection;
	private final ListBox monthSelection;
	private PopUp popUp;
	private  CalenderMonth month;
	

	@SuppressWarnings("deprecation")
	public CalendarContent()
	{
		popUp = new PopUp();
		eventForm = new EventForm();
		eventForm.registerUpdateListener(this);
		eventService.getWriteableCalendars(new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				popUp.setErrorMessage(caught.getMessage());
				popUp.show();
			}

			@Override
			public void onSuccess(List<String> result) {
				eventForm.setWriteAbleCalendars(result);		
			}
		});
		
		final Date date = new Date();
		month = new CalenderMonth(date, eventForm);
		addEvents(month);

		month.resize(Window.getClientWidth());

		yearSelection = new ListBox();
		
		final String thisYear = Integer.toString(date.getYear());
		for (int i = 2012; i < 2025; i++) {
			final String year = Integer.toString(i);
			final String yearDate = Integer.toString(i - 1900);
			yearSelection.addItem(year, yearDate);
			if (yearDate.equals(thisYear)) {
				yearSelection.setItemSelected(yearSelection.getItemCount() - 1,
						true);
			}
		}
		yearSelection.addChangeHandler(new SelectionChangeHandler());

		monthSelection = new ListBox();
		final String[] monthNames = LocaleInfo.getCurrentLocale()
				.getDateTimeFormatInfo().monthsFullStandalone();
		for (int i = 0; i < 12; i++) {
			monthSelection.addItem(monthNames[i], Integer.toString(i));
		}
		monthSelection.setItemSelected(date.getMonth(), true);
		monthSelection.addChangeHandler(new SelectionChangeHandler());

		final HorizontalPanel selections = new HorizontalPanel();
		selections.setSpacing(10);
		selections.add(monthSelection);
		selections.add(yearSelection);

		add(selections);
		add(month);

		

		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				month.resize(event.getWidth());

			}
		});

	}
	
	private void updateMonth(Date date) {
		month = new CalenderMonth(date, eventForm);

		addEvents(month);

		month.resize(Window.getClientWidth());
		remove(1);
		add(month);
	}
	
	private void addEvents(final CalenderMonth tmpMonth) {
		eventService.getEvents(tmpMonth.getStart(), tmpMonth.getEnd(),
				new AsyncCallback<List<Event>>() {


					@Override
					public void onSuccess(List<Event> events) {
						tmpMonth.setEvents(events);
					}

					@Override
					public void onFailure(Throwable caught) {
						popUp.setErrorMessage(caught.getMessage());
						popUp.show();

						
					}
				});
	}

	@Override
	public void update() {
		updateMonth(month.getDate());

	}
	
	public class SelectionChangeHandler implements ChangeHandler {

		@SuppressWarnings("deprecation")
		@Override
		public void onChange(ChangeEvent event) {
			final Date date = new Date();
			date.setYear(Integer.valueOf(
					yearSelection.getValue(yearSelection.getSelectedIndex()))
					.intValue());
			date.setMonth(monthSelection.getSelectedIndex());
			date.setHours(12);
			date.setDate(1);
			updateMonth(date);

		}



	}

}

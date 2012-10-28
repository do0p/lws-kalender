package ws.lernwerkstatt.cal.client;

import java.util.Date;
import java.util.List;

import ws.lernwerkstatt.cal.shared.Event;

import com.google.gwt.core.client.EntryPoint;
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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LwsKalender implements EntryPoint {
	private ListBox yearSelection;
	private ListBox monthSelection;
	private CalenderMonth month;
	private VerticalPanel panel;

	//
	// private static final String SERVER_ERROR = "An error occurred while "
	// + "attempting to contact the server. Please check your network "
	// + "connection and try again.";
	
	private final EventServiceAsync eventService = GWT.create(EventService.class);

	@SuppressWarnings("deprecation")
	public void onModuleLoad() {


		final Date date = new Date();
		month = new CalenderMonth(date);
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

		panel = new VerticalPanel();
		panel.add(selections);
		panel.add(month);

		final RootPanel rootPanel = RootPanel.get("content");
		rootPanel.add(panel);

		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				month.resize(event.getWidth());

			}
		});

	}

	public  class SelectionChangeHandler implements ChangeHandler {

		@SuppressWarnings("deprecation")
		@Override
		public void onChange(ChangeEvent event) {
			final Date date = new Date();
			date.setYear(Integer.valueOf(yearSelection.getValue(yearSelection.getSelectedIndex())).intValue());
			date.setMonth(monthSelection.getSelectedIndex());
			date.setHours(12);
			date.setDate(1);
			month = new CalenderMonth(date);

			addEvents(month);
			
			month.resize(Window.getClientWidth());
			panel.remove(1);
			panel.add(month);

		}


	}
	
	private void addEvents(final CalenderMonth tmpMonth) {
		eventService.getEvents(tmpMonth.getStart(), tmpMonth.getEnd(), new AsyncCallback<List<Event>>() {
			
			@Override
			public void onSuccess(List<Event> events) {
				tmpMonth.setEvents(events);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}

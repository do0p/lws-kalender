package ws.lernwerkstatt.cal.client.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import ws.lernwerkstatt.cal.client.service.EventService;
import ws.lernwerkstatt.cal.client.service.EventServiceAsync;
import ws.lernwerkstatt.cal.shared.Event;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class EventForm extends DialogBox {
	public interface UpdateListener {

		void update();

	}

	private final DateTimeFormat dateTimeFormat;

	private TextBox title;
	private DateBox startDate;
	private DateBox endDate;
	private TextArea description;
	private Label calendarName;

	private Button closeButton;
	private Button newButton;
	private Button editButton;
	private Button deleteButton;

	private Panel panel;
	private Panel buttons;
	private String id;

	private EventServiceAsync eventService = GWT.create(EventService.class);

	private Collection<UpdateListener> updateListeners = new ArrayList<EventForm.UpdateListener>();

	private List<String> writeableCalendars;

	public EventForm() {
		setModal(true);
		calendarName = new Label();
		dateTimeFormat = DateTimeFormat.getFormat("d.M.yy HH:mm");

		setText("Termin");
		setAnimationEnabled(true);

		// setup fields
		startDate = createDateBox();
		startDate.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				if (endDate.getValue() == null
						|| endDate.getValue().before(event.getValue())) {
					endDate.setValue(event.getValue());
				}
			}
		});
		endDate = createDateBox();
		endDate.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				if (startDate.getValue() == null
						|| startDate.getValue().after(event.getValue())) {
					startDate.setValue(event.getValue());
				}
			}
		});
		title = new TextBox();
		description = new TextArea();

		buttons = new HorizontalPanel();

		// create panel
		panel = createPanel();
		setWidget(panel);

		// create buttons
		createCloseButton();
		createNewButton();
		createEditButton();
		createDeleteButton();

	}

	private DateBox createDateBox() {
		final DateBox dateBox = new DateBox();
		dateBox.setFormat(new DateBox.DefaultFormat(dateTimeFormat));
		return dateBox;
	}

	public void setEvent(Event pEvent) {
		id = pEvent.getId();
		title.setValue(pEvent.getTitle());
		startDate.setValue(pEvent.getStartDate());
		endDate.setValue(pEvent.getEndDate());
		description.setValue(pEvent.getDescription());
		calendarName.setText(pEvent.getCalendarName());
	}

	private void clearEvent() {
		id = null;
		title.setValue(null);
		startDate.setValue(null);
		endDate.setValue(null);
		description.setValue(null);
	}

	private Event getEvent() {
		final Event event = new Event();
		event.setCalendarName(calendarName.getText());
		event.setId(id);
		event.setStartDate(startDate.getValue());
		event.setEndDate(endDate.getValue());
		event.setDescription(description.getValue());
		event.setTitle(title.getText());
		return event;
	}

	public void setupForEdit() {
		setText("Termin editieren");

		buttons.add(editButton);
		buttons.add(deleteButton);

		buttons.add(closeButton);
	}

	public void setupForNew() {
		setText("Termin anlegen");
		calendarName.setText(writeableCalendars.get(0));
		buttons.add(newButton);
		buttons.add(closeButton);
	}

	public void setupForShow() {
		setText("Termin anzeigen");
		buttons.add(closeButton);
	}

	private void createNewButton() {
		newButton = new Button("Speichern");
		newButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent pClickEvent) {
				final Event event = getEvent();
				eventService.storeEvent(event, createAsyncSuccessCallback());
			}

		});
	}

	private void createDeleteButton() {
		deleteButton = new Button("L&ouml;schen");
		deleteButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent pClickEvent) {
				final Event event = getEvent();
				eventService.deleteEvent(event, createAsyncSuccessCallback());
			}
		});
	}

	private void createEditButton() {
		editButton = new Button("Editieren");
		editButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent pClickEvent) {
				final Event event = getEvent();
				eventService.updateEvent(event, createAsyncSuccessCallback());
			}
		});
	}

	private void createCloseButton() {
		closeButton = new Button("Abbrechen");
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent pClickEvent) {
				hide();
				clearEvent();
				removeAllButtons();
			}

		});
	}

	private AsyncCallback<Void> createAsyncSuccessCallback() {
		return new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
			}

			public void onSuccess(Void result) {
				hide();
				clearEvent();
				removeAllButtons();
				for (UpdateListener listener : updateListeners) {
					listener.update();
				}
			}
		};
	}

	public void registerUpdateListener(UpdateListener listener) {
		updateListeners.add(listener);
	}

	private Panel createPanel() {
		final FlexTable eventForm = new FlexTable();
		eventForm.addStyleName("dialogVPanel");
		int row = 0;
		insertRow(row++, "Kalender", calendarName, eventForm);
		insertRow(row++, "Title", title, eventForm);
		insertRow(row++, "Beginn", startDate, eventForm);
		insertRow(row++, "Ende", endDate, eventForm);
		insertRow(row++, "Beschreibung", description, eventForm);
		eventForm.insertRow(row);
		eventForm.addCell(row);
		eventForm.setWidget(row, 0, buttons);
		eventForm.getFlexCellFormatter().setColSpan(row, 0, 2);
		return eventForm;
	}

	private void insertRow(int pRow, String pLabel, Widget pField,
			FlexTable pEventForm) {
		int column = 0;
		pEventForm.insertRow(pRow);
		pEventForm.addCell(pRow);
		pEventForm.setHTML(pRow, column, pLabel);
		pEventForm.getFlexCellFormatter().setAlignment(pRow, column++,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_TOP);
		pEventForm.addCell(pRow);
		pEventForm.setWidget(pRow, column, pField);
		pEventForm.getFlexCellFormatter().setAlignment(pRow, column,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
	}

	private void removeAllButtons() {
		buttons.remove(closeButton);
		buttons.remove(newButton);
		buttons.remove(editButton);
		buttons.remove(deleteButton);
	}

	public void setStartDate(Date startDate) {
		this.startDate.setValue(startDate, true);
	}

	public void setWriteAbleCalendars(List<String> writeableCalendars) {
		this.writeableCalendars = writeableCalendars;

	}

	public List<String> getWriteableCalendars() {
		return writeableCalendars;
	}

	public void setWriteableCalendars(List<String> writeableCalendars) {
		this.writeableCalendars = writeableCalendars;
	}

}

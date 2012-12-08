package ws.lernwerkstatt.cal.client.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ws.lernwerkstatt.cal.shared.Event;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class CalenderMonth extends Grid {

	private final Date startDate;
	private final Date endDate;
	private final Date date;
	private Map<Date, DateField> days = new HashMap<Date, DateField>();

	public CalenderMonth(Date date, final EventForm eventForm) {
		super(CalUtil.getWeeksFromDate(date), 7);
		this.date = date;
		setBorderWidth(1);
		setCellSpacing(0);
		setCellPadding(5);

		startDate = CalUtil.getFirstDayForCalendar(date);
		final int count = iterateFields(new FieldExecutor() {
			public void execute(int r, int c, int i) {
				final Date date = CalendarUtil.copyDate(startDate);
				CalendarUtil.addDaysToDate(date, i);
				final DateField dateField = new DateField(date, eventForm);
				days.put(date, dateField);
				setWidget(r, c, dateField);
			}
		});
		endDate = CalendarUtil.copyDate(startDate);
		CalendarUtil.addDaysToDate(endDate, count);

	}

	public void resize(int clientWidth) {
		final int width = (clientWidth - 50 - 70) / 7;
		final int height = width;

		iterateFields(new FieldExecutor() {
			public void execute(int r, int c, int i) {
				final Widget dateField = getWidget(r, c);
				dateField.setHeight(height + "px");
				dateField.setWidth(width + "px");
			}
		});
	}

	private int iterateFields(FieldExecutor executor) {
		int i = 0;
		for (int r = 0; r < getRowCount(); r++) {
			for (int c = 0; c < getColumnCount(); c++) {
				executor.execute(r, c, i++);
			}
		}
		return i;
	}

	public interface FieldExecutor {
		void execute(int r, int c, int i);
	}

	public Date getStart() {
		return startDate;
	}

	public Date getEnd() {
		return endDate;
	}

	public void setEvents(List<Event> events) {
		for (Event event : events) {
			final Date startDay = CalendarUtil.copyDate(event.getStartDate());
			CalUtil.resetTimeToMidnight(startDay);
			addEvent(startDay, event);

			final Date endDay = CalendarUtil.copyDate(event.getEndDate());
			CalUtil.resetTimeToMidnight(endDay);
			if(endDay.after(startDay))
			{
				int numDays = CalendarUtil.getDaysBetween(startDay, endDay);
				for(int i = 1; i < numDays; i++)
				{
					final Date dayBetween = CalendarUtil.copyDate(startDay);
					CalUtil.resetTimeToMidnight(dayBetween);
					CalendarUtil.addDaysToDate(dayBetween, i);
					addEvent(dayBetween, event);
				}
				addEvent(endDay, event);
			}
		}
		for(DateField day : days.values())
		{
			day.formatDay();
		}
	}

	private void addEvent(Date day, Event event) {
		final DateField dateField = days.get(day);
		if (dateField != null) {
			dateField.addEvent(event);
		}
	}

	public Date getDate() {
		return date;
	}

}

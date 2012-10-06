package ws.lernwerkstatt.cal.client;

import java.util.Date;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class CalenderMonth extends Grid {

	public CalenderMonth(Date date) {
		super(CalUtil.getWeeksFromDate(date), 7);
		setBorderWidth(1);
		setCellSpacing(0);
		setCellPadding(5);

		final Date startDate = CalUtil.getFirstDayForCalendar(date);
		iterateFields(new FieldExecutor() {
			public void execute(int r, int c, int i) {
				final Date date = CalendarUtil.copyDate(startDate);
				CalendarUtil.addDaysToDate(date, i);
				final Widget dateField = new DateField(date);
				setWidget(r, c, dateField);
			}
		});

	}

	public CalenderMonth() {
		this(new Date());
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

	private void iterateFields(FieldExecutor executor) {
		int i = 0;
		for (int r = 0; r < getRowCount(); r++) {
			for (int c = 0; c < getColumnCount(); c++) {
				executor.execute(r, c, i++);
			}
		}
	}


	public interface FieldExecutor {
		void execute(int r, int c, int i);
	}


	public void setDate(Date date) {
		// TODO Auto-generated method stub
		
	}
}

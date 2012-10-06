package ws.lernwerkstatt.cal.client;

import java.util.Date;

import com.google.gwt.user.datepicker.client.CalendarUtil;

public class CalUtil {

	static Date getFirstDayForCalendar(Date date) {
		final Date tmpDate = CalendarUtil.copyDate(date);
		CalendarUtil.setToFirstDayOfMonth(tmpDate);
		final int dayOfWeek = getDayOfWeek(tmpDate);
		CalendarUtil.addDaysToDate(tmpDate, -dayOfWeek);
		return tmpDate;
	}

	static int getWeeksFromDate(Date date) {
		final Date firstDayOfMonth = CalendarUtil.copyDate(date);
		CalendarUtil.setToFirstDayOfMonth(firstDayOfMonth);
		final int dayOfWeek = getDayOfWeek(firstDayOfMonth);
		final int countDaysMonth = CalUtil.getCountDaysMonth(firstDayOfMonth);
		return (dayOfWeek + countDaysMonth - 1) / 7 + 1;
	}

	static int getCountDaysMonth(Date date) {
		final Date firstDayOfMonth = CalendarUtil.copyDate(date);
		CalendarUtil.setToFirstDayOfMonth(firstDayOfMonth);
		final Date firstDayOfNextMonth = CalendarUtil.copyDate(date);
		CalendarUtil.addMonthsToDate(firstDayOfNextMonth, 1);
		return CalendarUtil.getDaysBetween(date, firstDayOfNextMonth);
	}

	static int getDayOfWeek(Date date) {
		@SuppressWarnings("deprecation")
		final int dayOfWeek = date.getDay();
		return dayOfWeek == 0 ? 6 : dayOfWeek - 1;
	}

}

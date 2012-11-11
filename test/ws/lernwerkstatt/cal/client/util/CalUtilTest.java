package ws.lernwerkstatt.cal.client.util;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import ws.lernwerkstatt.cal.client.util.CalUtil;

import com.google.gwt.user.datepicker.client.CalendarUtil;

@SuppressWarnings("deprecation")
public class CalUtilTest {

	private static final Date MON_01_10_2012;
	private static final Date TUE_02_10_2012;
	private static final Date WEN_03_10_2012;
	private static final Date THU_04_10_2012;
	private static final Date FRI_05_10_2012;
	private static final Date SAT_06_10_2012;
	private static final Date SUN_07_10_2012;
	private static final Date WEN_01_02_2012;
	private static final Date TUE_01_02_2011;
	private static final Date SAT_07_07_2012;
	
	static
	{
		MON_01_10_2012 = new Date();
		MON_01_10_2012.setYear(112);
		MON_01_10_2012.setMonth(9);
		MON_01_10_2012.setDate(1);
		MON_01_10_2012.setHours(12);
		MON_01_10_2012.setMinutes(0);
		MON_01_10_2012.setSeconds(0);
		TUE_02_10_2012 = CalendarUtil.copyDate(MON_01_10_2012);
		CalendarUtil.addDaysToDate(TUE_02_10_2012, 1);
		WEN_03_10_2012 = CalendarUtil.copyDate(TUE_02_10_2012);
		CalendarUtil.addDaysToDate(WEN_03_10_2012, 1);
		THU_04_10_2012 = CalendarUtil.copyDate(WEN_03_10_2012);
		CalendarUtil.addDaysToDate(THU_04_10_2012, 1);
		FRI_05_10_2012 = CalendarUtil.copyDate(THU_04_10_2012);
		CalendarUtil.addDaysToDate(FRI_05_10_2012, 1);
		SAT_06_10_2012 = CalendarUtil.copyDate(FRI_05_10_2012);
		CalendarUtil.addDaysToDate(SAT_06_10_2012, 1);
		SUN_07_10_2012 = CalendarUtil.copyDate(SAT_06_10_2012);
		CalendarUtil.addDaysToDate(SUN_07_10_2012, 1);
		WEN_01_02_2012 = CalendarUtil.copyDate(MON_01_10_2012);
		CalendarUtil.addMonthsToDate(WEN_01_02_2012, -8);
		TUE_01_02_2011 = CalendarUtil.copyDate(WEN_01_02_2012);
		CalendarUtil.addMonthsToDate(TUE_01_02_2011, -12);
		SAT_07_07_2012 = CalendarUtil.copyDate(SUN_07_10_2012);
		CalendarUtil.addMonthsToDate(SAT_07_07_2012, -3);
	}

	@Test
	public void testNotModified()
	{
		final long time = System.currentTimeMillis();
		final Date date = new Date(time);
		Assert.assertEquals(time, date.getTime());
		CalUtil.getDayOfWeek(date);
		Assert.assertEquals(time, date.getTime());
		CalUtil.getCountDaysMonth(date);
		Assert.assertEquals(time, date.getTime());
		CalUtil.getFirstDayForCalendar(date);
		Assert.assertEquals(time, date.getTime());
		CalUtil.getWeeksFromDate(date);
		Assert.assertEquals(time, date.getTime());
	}
	
	@Test
	public void testGetDayOfWeek()
	{
		Assert.assertEquals(0, CalUtil.getDayOfWeek(MON_01_10_2012));
		Assert.assertEquals(1, CalUtil.getDayOfWeek(TUE_02_10_2012));
		Assert.assertEquals(1, CalUtil.getDayOfWeek(TUE_01_02_2011));
		Assert.assertEquals(2, CalUtil.getDayOfWeek(WEN_03_10_2012));
		Assert.assertEquals(2, CalUtil.getDayOfWeek(WEN_01_02_2012));
		Assert.assertEquals(3, CalUtil.getDayOfWeek(THU_04_10_2012));
		Assert.assertEquals(4, CalUtil.getDayOfWeek(FRI_05_10_2012));
		Assert.assertEquals(5, CalUtil.getDayOfWeek(SAT_06_10_2012));
		Assert.assertEquals(5, CalUtil.getDayOfWeek(SAT_07_07_2012));
		Assert.assertEquals(6, CalUtil.getDayOfWeek(SUN_07_10_2012));
	}
	
	@Test
	public void testGetCountDaysMonth()
	{
		Assert.assertEquals(31, CalUtil.getCountDaysMonth(MON_01_10_2012));
		Assert.assertEquals(31, CalUtil.getCountDaysMonth(SUN_07_10_2012));
		Assert.assertEquals(31, CalUtil.getCountDaysMonth(SAT_07_07_2012));
		Assert.assertEquals(29, CalUtil.getCountDaysMonth(WEN_01_02_2012));
		Assert.assertEquals(28, CalUtil.getCountDaysMonth(TUE_01_02_2011));
	}
	
	@Test
	public void getWeeksFromDate()
	{
		Assert.assertEquals(5, CalUtil.getWeeksFromDate(MON_01_10_2012));
		Assert.assertEquals(5, CalUtil.getWeeksFromDate(SUN_07_10_2012));
		Assert.assertEquals(6, CalUtil.getWeeksFromDate(SAT_07_07_2012));
	}
	
	@Test
	public void getFirstDayForCalendar()
	{
		Date firstDayForCalendar = CalUtil.getFirstDayForCalendar(MON_01_10_2012);
		Assert.assertEquals(1, firstDayForCalendar.getDate());
		Assert.assertEquals(9, firstDayForCalendar.getMonth());
		Assert.assertEquals(1, firstDayForCalendar.getDay());
		
		firstDayForCalendar = CalUtil.getFirstDayForCalendar(SUN_07_10_2012);
		Assert.assertEquals(1, firstDayForCalendar.getDate());
		Assert.assertEquals(9, firstDayForCalendar.getMonth());
		Assert.assertEquals(1, firstDayForCalendar.getDay());
		
		firstDayForCalendar = CalUtil.getFirstDayForCalendar(SAT_07_07_2012);
		Assert.assertEquals(1, firstDayForCalendar.getDay());
		Assert.assertEquals(5, firstDayForCalendar.getMonth());
		Assert.assertEquals(25, firstDayForCalendar.getDate());
	}
}

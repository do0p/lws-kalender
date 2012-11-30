package ws.lernwerkstatt.cal.server.model;

public class CalendarDescription {

	private final String calendarId;
	private final boolean writeable;
	private final String style;

	public CalendarDescription(String calId, String style, boolean writeable) {
		calendarId = calId;
		this.style = style;
		this.writeable = writeable;
	}

	public String getCalendarId() {
		return calendarId;
	}

	public boolean isWriteable() {
		return writeable;
	}

	public String getStyle() {
		return style;
	}

}

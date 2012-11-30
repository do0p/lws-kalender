package ws.lernwerkstatt.cal.shared;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;


public class Event implements Serializable, Comparable<Event>, IsSerializable {

	private static final long serialVersionUID = 4912333683847788038L;
	private String id;
	
	private String title;
	private Date startDate;
	private Date endDate;
	private String description;
	private String location;
	private String calendarName;
	private String style;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date pStartDate) {
		startDate = pStartDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String pTitle) {
		title = pTitle;
	}

	
	public int compareTo(Event o) {
		return startDate.compareTo(o.getStartDate());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String pId) {
		id = pId;
	}
	
	@Override
	public boolean equals(Object pObj) {
		if(!(pObj instanceof Event))
		{
			return false;
		}
		final Event other = (Event) pObj;
		return id == null ? other == null : id.equals(other.id);
	}
	
	@Override
	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}
	
	@Override
	public String toString() {
		return title + " " + startDate.toString() ;
	}

	public void setEndDate(Date pEndDate) {
		endDate = pEndDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setDescription(String pDescription) {
		description = pDescription;
	}

	public String getDescription() {
		return description;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}

	public String getCalendarName() {
		return calendarName;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}


}

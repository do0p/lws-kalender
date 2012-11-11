package ws.lernwerkstatt.cal.client.util;

import ws.lernwerkstatt.cal.client.CalendarContent;
import ws.lernwerkstatt.cal.client.admin.AdminContent;
import ws.lernwerkstatt.cal.shared.Authorization;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;

public class Navigation extends HorizontalPanel {

	private static final String ADMIN = "admin";
	private static final String LIST_ENTRY = "list";
	private final Authorization authorization;

	public Navigation(Authorization authorization) {
		this.authorization = authorization;
		setSpacing(10);
		add(new Hyperlink("anzeigen", LIST_ENTRY));
		if (authorization.isAdmin()) {
			add(new Hyperlink("administrieren", ADMIN));
		}
	}
	
	public Widget getContent(String token) {
		if (token.isEmpty()) {
			token = LIST_ENTRY;
		}
		if (token.equals(LIST_ENTRY)) {
			return new CalendarContent();
		}
		if (token.equals(ADMIN)) {
			return new AdminContent(authorization, "550px");
		}
		return null;
	}

}

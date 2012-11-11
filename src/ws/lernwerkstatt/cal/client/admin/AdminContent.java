package ws.lernwerkstatt.cal.client.admin;

import ws.lernwerkstatt.cal.shared.Authorization;

import com.google.gwt.user.client.ui.TabPanel;

public class AdminContent extends TabPanel {
	public AdminContent(Authorization authorization, String width) {

		setSize("100%", width);
		if (authorization.isAdmin()) {
			add(new AuthorizationAdmin(), "Benutzer");
		}
		if (authorization.isAdmin() ) {
			selectTab(0);
		}
	}
}

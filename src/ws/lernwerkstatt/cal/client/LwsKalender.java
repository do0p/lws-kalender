package ws.lernwerkstatt.cal.client;

import ws.lernwerkstatt.cal.client.util.Navigation;
import ws.lernwerkstatt.cal.shared.Authorization;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class LwsKalender extends SecuredContent implements
		ValueChangeHandler<String> {

	private Navigation navigation;

	@Override
	protected void onLogin(Authorization authorization) {
		navigation = new Navigation(authorization);
		if (navigation.getWidgetCount() > 1) {
			RootPanel.get("navigation").add(navigation);
		}
		History.addValueChangeHandler(this);
		changePage(History.getToken());
		RootPanel.get("logout").add(
				new Anchor("logout", authorization.getLogoutUrl()));
	}

	@Override
	protected void onLogOut(Authorization authorization) {
		final RootPanel rootPanel = RootPanel.get("content");
		rootPanel.clear();
		rootPanel.add(new LoginFailedContent(authorization.getLoginUrl()));
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		changePage(History.getToken());
	}

	private void changePage(String token) {
		final Widget content = navigation.getContent(token);
		if (content != null) {
			final RootPanel rootPanel = RootPanel.get("content");
			rootPanel.clear();
			rootPanel.add(content);
		}
	}
}

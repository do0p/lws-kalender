package ws.lernwerkstatt.cal.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class LwsKalender implements EntryPoint {
	//
	// private static final String SERVER_ERROR = "An error occurred while "
	// + "attempting to contact the server. Please check your network "
	// + "connection and try again.";

	public void onModuleLoad() {

		final CalenderMonth month = new CalenderMonth(new Date());

		month.resize(Window.getClientWidth());

		final RootPanel rootPanel = RootPanel.get("content");
		rootPanel.add(month);

		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				month.resize(event.getWidth());

			}
		});

	}

}

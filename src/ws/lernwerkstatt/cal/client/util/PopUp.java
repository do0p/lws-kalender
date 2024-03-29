package ws.lernwerkstatt.cal.client.util;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PopUp extends DialogBox {

	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private final HTML serverResponseLabel;
	private final Button closeButton;

	private FocusWidget[] focusOnClose;

	public PopUp() {
		serverResponseLabel = new HTML();
		closeButton = new Button("Close");
		closeButton.getElement().setId("closeButton");
		closeButton.addClickHandler(new CloseButtonHandler());
		setText("Remote Procedure Call");
		setAnimationEnabled(true);
		setWidget(createPanel());
	}

	public void setDisableWhileShown(FocusWidget... focusWidget) {
		focusOnClose = focusWidget;
	}

	private VerticalPanel createPanel() {
		final VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		return dialogVPanel;
	}

	public void setErrorMessage() {
		setErrorMessage(SERVER_ERROR);
	}

	public void setErrorMessage(String errorMessage) {
		setText("Remote Procedure Call - Failure");
		serverResponseLabel.addStyleName("serverResponseLabelError");
		serverResponseLabel.setHTML(errorMessage);
		closeButton.setFocus(true);
	}

	public void setMessage(String message) {
		setText("Remote Procedure Call");
		serverResponseLabel.removeStyleName("serverResponseLabelError");
		serverResponseLabel.setHTML(message);
		closeButton.setFocus(true);
	}

	private class CloseButtonHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			hide();
		}
	}

	@Override
	public void hide() {
		if (focusOnClose != null) {
			for (FocusWidget widget : focusOnClose) {
				widget.setEnabled(true);
				widget.setFocus(true);
			}
		}super.hide();
	}

	@Override
	public void center() {
		if (focusOnClose != null) {
			for (FocusWidget widget : focusOnClose) {
				widget.setEnabled(false);
			}
		}
		super.center();
	}

}

package ws.lernwerkstatt.cal.client;

import ws.lernwerkstatt.cal.client.service.AuthorizationService;
import ws.lernwerkstatt.cal.client.service.AuthorizationServiceAsync;
import ws.lernwerkstatt.cal.shared.Authorization;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class SecuredContent implements EntryPoint {

	private final AuthorizationServiceAsync authService = GWT
			.create(AuthorizationService.class);

	private Authorization authorization;

	public final void onModuleLoad() {
		executeSecured(this);
	}

	public void executeSecured(final SecuredContent securedContent) {
		authService.getAuthorizationForCurrentUser(Window.Location
				.createUrlBuilder().buildString(),
				new AsyncCallback<Authorization>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Authorization authorization) {
						SecuredContent.this.authorization = authorization; 
						if (authorization.isLoggedIn()) {
							securedContent.onLogin(authorization);
						} else {
							securedContent.onLogOut(authorization);
						}
					}

				});
	}

	
	protected abstract void onLogin(Authorization authorization);

	protected abstract void onLogOut(Authorization authorization);

	protected Authorization getAuthorization() {
		return authorization;
	}

}

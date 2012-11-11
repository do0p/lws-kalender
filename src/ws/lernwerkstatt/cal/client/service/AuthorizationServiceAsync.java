package ws.lernwerkstatt.cal.client.service;

import java.util.Collection;

import ws.lernwerkstatt.cal.shared.Authorization;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthorizationServiceAsync {

	void currentUserIsAdmin(AsyncCallback<Boolean> callback);

	void deleteAuthorization(String email, AsyncCallback<Void> callback);

	void getAuthorizationForCurrentUser(String followUpUrl,
			AsyncCallback<Authorization> callback);

	void queryAuthorizations(AsyncCallback<Collection<Authorization>> callback);

	void storeAuthorization(Authorization aut, AsyncCallback<Void> callback);

}

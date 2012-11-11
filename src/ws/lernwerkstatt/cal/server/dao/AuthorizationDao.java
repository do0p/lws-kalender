package ws.lernwerkstatt.cal.server.dao;

import java.util.Collection;

import com.google.appengine.api.users.User;

import ws.lernwerkstatt.cal.shared.Authorization;

public interface AuthorizationDao {

	Collection<Authorization> queryAuthorizations();

	void storeAuthorization(Authorization aut);

	void deleteAuthorization(String email);

	Authorization getAuthorization(User currentUser);

}

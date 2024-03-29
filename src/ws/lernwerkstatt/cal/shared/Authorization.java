package ws.lernwerkstatt.cal.shared;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
public class Authorization implements Serializable, IsSerializable {

	private static final long serialVersionUID = -5057466504583370610L;

	@Id
	private String userId; // lowercase Email

	private String email;

	private boolean admin;
	
	private boolean seeAll;
	
	@Transient
	private String loginUrl;
	
	@Transient
	private String logoutUrl;

	@Transient
	private boolean loggedIn;
	
	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public boolean isSeeAll() {
		return seeAll;
	}

	public void setSeeAll(boolean seeAll) {
		this.seeAll = seeAll;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Authorization)) {
			return false;
		}
		final Authorization other = (Authorization) obj;
		return userId == null ? other == null : userId.equals(other.userId);
	}

	@Override
	public int hashCode() {
		return userId == null ? 0: userId.hashCode();
	}

}

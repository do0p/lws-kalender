package ws.lernwerkstatt.cal.server.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ws.lernwerkstatt.cal.server.dao.AuthorizationDao;
import ws.lernwerkstatt.cal.shared.Authorization;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.User;

public class AuthorizationDaoImpl implements AuthorizationDao {

	private static final Set<String> SUPER_USER_IDS = new HashSet<String>();
	static {
		SUPER_USER_IDS.add("dbrandl72@gmail.com");
	}

	private final MemcacheService cache = MemcacheServiceFactory
			.getMemcacheService("authDao");

	public AuthorizationDaoImpl() {
		initCache();
	}

	public Authorization getAuthorization(User user) {
		final String userId = createUserId(user);
		Authorization authorization = (Authorization) cache.get(userId);
		if (authorization == null) {
			if (SUPER_USER_IDS.contains(userId)) {
				authorization = createSuperUser(userId);
			} else {
				final EntityManager em = EMF.get().createEntityManager();
				try {
					authorization = em.find(Authorization.class, userId);
				} finally {
					em.close();
				}
			}
			cache.put(userId, authorization);
		}
		return authorization;
	}

	public Collection<Authorization> queryAuthorizations() {
		return new ArrayList<Authorization>(queryAuthInternal().values());
	}

	public void storeAuthorization(Authorization aut) {
		aut.setUserId(createUserId(aut.getEmail()));
		cache.put(aut.getUserId(), aut);
		final EntityManager em = EMF.get().createEntityManager();
		try {
			em.persist(aut);
		} finally {
			em.close();
		}
	}

	public void deleteAuthorization(String email) {
		final String userId = createUserId(email);
		cache.delete(userId);
		final EntityManager em = EMF.get().createEntityManager();
		try {
			final Query query = em
					.createQuery("delete from Authorization a where a.userId = :userId");
			query.setParameter("userId", userId);
			query.executeUpdate();
		} finally {
			em.close();
		}
	}

	private String createUserId(String email) {
		return email.toLowerCase();
	}

	private Authorization createSuperUser(String email) {
		final Authorization superUser = new Authorization();
		superUser.setUserId(email);
		superUser.setEmail(email);
		superUser.setAdmin(true);
		superUser.setSeeAll(false);
		return superUser;
	}

	private void initCache() {
		cache.putAll(queryAuthInternal());
	}

	@SuppressWarnings("unchecked")
	private Map<String, Authorization> queryAuthInternal() {
		final Map<String, Authorization> tmpCache = new HashMap<String, Authorization>();
		for (String superUserId : SUPER_USER_IDS) {
			tmpCache.put(superUserId, createSuperUser(superUserId));
		}
		final EntityManager em = EMF.get().createEntityManager();
		try {
			final Query query = em
					.createQuery("select auth from Authorization auth");
			for (Authorization auth : (List<Authorization>) query
					.getResultList()) {
				tmpCache.put(auth.getUserId(), auth);
			}
		} finally {
			em.close();
		}
		return tmpCache;
	}

	private String createUserId(User user) {
		return user == null ? null : user.getEmail().toLowerCase();
	}

}

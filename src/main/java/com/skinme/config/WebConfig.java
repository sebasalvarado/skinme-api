package com.skinme.config;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.SparkBase.staticFileLocation;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;

import com.skinme.model.Entry;
import com.skinme.model.User;
import com.skinme.service.impl.SkinService;
import com.skinme.model.LoginResult;

import spark.Request;


public class WebConfig {
	private static final String USER_SESSION_ID = "user";
	private SkinService service;
	
	public WebConfig(SkinService service) {
		this.service = service;
		staticFileLocation("/public");
		setupRoutes();
	}

	private void setupRoutes(){
	/*
	 * This is the root that displays the Home page for the authenticated user.
	 */
	get("/", (req, res) -> {
		User user = getAuthenticatedUser(req);
		Map<String, Object> map = new HashMap<>();
		map.put("Title", "Journal");
		map.put("user", user);
		// Return the JSON Object
		return map;
    });
	
	before("/", (req, res) -> {
		User user = getAuthenticatedUser(req);
		if(user == null) {
			res.redirect("/public");
			halt();
		}
	});

	
	/*
	 * Displays the products that a given user has
	 */
	get("/products/:username", (req, res) -> {
		User user = getAuthenticatedUser(req);
		Map<String, Object> map = new HashMap<>();
		// Encodi
		map.put("pageTitle", "Public Products");
		map.put("user", user);
		return map;
    });
	
	
	/*
	 * Displays a user's entries
	 */
	get("/view/:username", (req, res) -> {
		String username = req.params(":username");
		User profileUser = service.getUserbyUsername(username);
		
		User authUser = getAuthenticatedUser(req);
		boolean followed = false;

		List<Entry> messages = service.getUserTimelineMessages(profileUser);
		
		Map<String, Object> map = new HashMap<>();
		map.put("pageTitle", username + "'s Entries");
		map.put("user", authUser);
		map.put("profileUser", profileUser);
		map.put("followed", followed);
		map.put("messages", messages);
		return map;
    });
	
	/*
	 * Checks if the user exists
	 */
	before("/view/:username", (req, res) -> {
		String username = req.params(":username");
		User profileUser = service.getUserbyUsername(username);
		if(profileUser == null) {
			halt(404, "User not Found");
		}
	});
	
	
	
	
	/*
	 * Removes an entry from the user
	 */
	get("/remove/:username/:entryid", (req, res) -> {
		String username = req.params(":username");
		User profileUser = service.getUserbyUsername(username);
		User authUser = getAuthenticatedUser(req);
		
		//
		service.unfollowUser(authUser, profileUser);
		res.redirect("/t/" + username);
		return null;
    });
	/*
	 * Checks if the user is authenticated and the user to unfollow exists
	 */
	before("/t/:username/unfollow", (req, res) -> {
		String username = req.params(":username");
		User authUser = getAuthenticatedUser(req);
		User profileUser = service.getUserbyUsername(username);
		if(authUser == null) {
			res.redirect("/login");
			halt();
		} else if(profileUser == null) {
			halt(404, "User not Found");
		}
	});
	
	
	/*
	 * Presents the login form or redirect the user to
	 * her timeline if it's already logged in
	 */
	get("/login", (req, res) -> {
		Map<String, Object> map = new HashMap<>();
		if(req.queryParams("r") != null) {
			map.put("message", "You were successfully registered and can login now");
		}
		return map;
    });
	/*
	 * Logs the user in.
	 */
	post("/login", (req, res) -> {
		Map<String, Object> map = new HashMap<>();
		User user = new User();
		try {
			MultiMap<String> params = new MultiMap<String>();
			UrlEncoded.decodeTo(req.body(), params, "UTF-8", -1);
			BeanUtils.populate(user, params);
		} catch (Exception e) {
			halt(501);
			return null;
		}
		LoginResult result = service.checkUser(user);
		if(result.getUser() != null) {
			addAuthenticatedUser(req, result.getUser());
			res.redirect("/");
			halt();
		} else {
			map.put("error", result.getError());
		}
		map.put("username", user.getUsername());
		return map;
    });
	/*
	 * Checks if the user is already authenticated
	 */
	before("/login", (req, res) -> {
		User authUser = getAuthenticatedUser(req);
		if(authUser != null) {
			res.redirect("/");
			halt();
		}
	});
	
	
	/*
	 * Presents the register form or redirect the user to
	 * her timeline if it's already logged in
	 */
	get("/register", (req, res) -> {
		Map<String, Object> map = new HashMap<>();
		return map;
    });
	/*
	 * Registers the user.
	 */
	post("/register", (req, res) -> {
		Map<String, Object> map = new HashMap<>();
		User user = new User();
		try {
			MultiMap<String> params = new MultiMap<String>();
			UrlEncoded.decodeTo(req.body(), params, "UTF-8", -1);
			BeanUtils.populate(user, params);
		} catch (Exception e) {
			halt(501);
			return null;
		}
		String error = user.validate();
		if(StringUtils.isEmpty(error)) {
			User existingUser = service.getUserbyUsername(user.getUsername());
			if(existingUser == null) {
				service.registerUser(user);
				res.redirect("/login?r=1");
				halt();
			} else {
				error = "The username is already taken";
			}
		}
		map.put("error", error);
		map.put("username", user.getUsername());
		map.put("email", user.getEmail());
		return map;
    });
	/*
	 * Checks if the user is already authenticated
	 */
	before("/register", (req, res) -> {
		User authUser = getAuthenticatedUser(req);
		if(authUser != null) {
			res.redirect("/");
			halt();
		}
	});
	
	
	/*
	 * Registers a new entry for the user.
	 */
	post("/entry", (req, res) -> {
		User user = getAuthenticatedUser(req);
		MultiMap<String> params = new MultiMap<String>();
		UrlEncoded.decodeTo(req.body(), params, "UTF-8", -1);
		Entry m = new Entry();
		m.setUserId(user.getId());
		m.setPubDate(new Date());
		BeanUtils.populate(m, params);
		service.addMessage(m);
		res.redirect("/");
		return null;
    });
	/*
	 * Checks if the user is authenticated
	 */
	before("/entry", (req, res) -> {
		User authUser = getAuthenticatedUser(req);
		if(authUser == null) {
			res.redirect("/login");
			halt();
		}
	});
	
	
}
	
	private void addAuthenticatedUser(Request request, User u) {
		request.session().attribute(USER_SESSION_ID, u);
		
	}

	private void removeAuthenticatedUser(Request request) {
		request.session().removeAttribute(USER_SESSION_ID);
		
	}

	private User getAuthenticatedUser(Request request) {
		return request.session().attribute(USER_SESSION_ID);
	}

}

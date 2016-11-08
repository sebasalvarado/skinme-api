package com.skinme.service.impl;

import java.sql.Date;
import java.util.List;

import com.skinme.model.Entry;
import com.skinme.model.LoginResult;
import com.skinme.model.User;
import com.skinme.util.PasswordUtil;
import com.skinme.dao.EntryDao;
import com.skinme.dao.UserDao;

public class SkinService {

	private UserDao userDao;
	
	private EntryDao EntryDao;
	
	public List<Entry> getUserEntries(User user) {
		return EntryDao.getUserEntries(user);
	}
	

	public List<Entry> getUserEntriesByDate (User user, Date d1, Date date2){
		return EntryDao.getUserEntriesByDate(user, d1,date2);
	}
	
	public User getUserbyUsername(String username) {
		return userDao.getUserbyUsername(username);
	}
	
	
	public void addEntry(Entry Entry) {
		EntryDao.insertEntry(Entry);
	}
	
	public LoginResult checkUser(User user) {
		LoginResult result = new LoginResult();
		User userFound = userDao.getUserbyUsername(user.getUsername());
		if(userFound == null) {
			result.setError("Invalid username");
		} else if(!PasswordUtil.verifyPassword(user.getPassword(), userFound.getPassword())) {
			result.setError("Invalid password");
		} else {
			result.setUser(userFound);
		}
		
		return result;
	}
	
	public void registerUser(User user) {
		user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
		userDao.registerUser(user);
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setEntryDao(EntryDao EntryDao) {
		this.EntryDao = EntryDao;
	}
}
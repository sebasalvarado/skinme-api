package com.skinme.dao;

import com.skinme.model.Entry;
import com.skinme.model.User;

public interface UserDao {

	User getUserbyUsername(String username);
	
	void insertEntry(User user, Entry entry);
	
	void deleteEntry(User user, Entry entry);
	
	Entry lastEntry(User user);
	
	void registerUser(User user);
}

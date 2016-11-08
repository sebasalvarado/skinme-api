package com.skinme.dao;


import java.sql.Date;
import java.util.List;
import com.skinme.model.Entry;
import com.skinme.model.User;

public interface EntryDao {
	
	
	
	//Follow CRUD
	//Create
	//Read
	//Update
	//Delete
	
	public int insertEntry (Entry ent);
	
	//Lists make sense, since we really can't get a single entry
	
	//PLACEHOLDER Object user is really a User user, change later on
	public List <Entry> getUserEntries(Object user);
	
	//Get user Entries from Date D1 to Date D2, or if either is non-specified,
	//then just get the entries that occurred on the date
	public List<Entry> getUserEntriesByDate (User user, Date d1, Date date2);
	
	//we can choose to delete either based on some criteria in the entire DB
	//OR we get a pointer and we delete that, or we look for an entry with
	//the exact same stuff as specified in ent and delete that
	public boolean deleteEntry(Object criteria, Entry ent);
	
	
	//findEntries based on some arbitrary criteria, this is related to 
	//getUserEntries (can be thought of as a more general case)
	public Entry findEntry(Object o);
	
	
	//Based on the review, we can either choose to implement more concrete 
	//DAO methods, such as findEntryByIssue, or leave it overloaded/general
	//like this
	
	//look into this
	//http://tutorials.jenkov.com/java-persistence/dao-design-problems.html

}
package com.skinme.model;

import java.util.ArrayList;
import java.util.List;

public class Entry implements Comparable<Entry>{
	/* To implement observable pattern */

	// List of issues
	private List<String> issues = new ArrayList<String>();

	// List of products
	private List<Product> products = new ArrayList<Product>();

	// path to the photo
	private String photoPath;

	// Description of the entry
	private String description;

	// Date of the entry
	private String date;

	// Rating of the entry
	private int rating;

	/**
	 * The constructor takes a path to a photo and the date that the entry was
	 * created
	 * 
	 * @param date
	 *            the date that the entry was created
	 */
	public Entry(String date) {
		
		this.date = date;
	}
	
	/**
	 * Adds a picture using the location of that picture
	 * @param photoPath the location of the picture
	 */
	public void addPhoto(String photoPath){
		this.photoPath = photoPath;
	}

	/**
	 * Add an product to the list of products if it is not already there
	 * @param product
	 */
	public void addProduct(Product product) {
		// Add this product to the user entry
		if (!products.contains(product)) {
			products.add(product);
		}
	}

	/**
	 * Gets an issue and adds it to the list of the issues if the issue was not
	 * already inside the list
	 * 
	 * @param issue
	 */
	public void addIssue(String issue) {
		if (!issues.contains(issue)) {
			issues.add(issue);
		}
	}

	/**
	 * Adds a description for the entry
	 * 
	 * @param description
	 *            takes an String as the description
	 */
	public void addDescription(String description) {

		this.description = description;
	}

	/**
	 * Updates the path to the photo
	 * 
	 * @param newPath
	 *            the new path of the photo
	 */
	public void updatePhoto(String newPath) {

		this.photoPath = newPath;
	}

	/**
	 * Add rating to this entry
	 * @param rating
	 */
	public void addRating(int rating) {
		// Setting a new rating
		this.rating = rating;
	}

	/**
	 * Gets the list of the issues that is tagged to this entry
	 * 
	 * @return list of all issues
	 */
	public List<String> getIssues() {
		return issues;
	}

	/**
	 * Gets the list of all products that are tagged to this entry
	 * 
	 * @return list of all products
	 */
	public List<Product> getProducts() {
		return products;
	}

	/**
	 * Gets the description of this entry
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the date that the entry was created
	 * 
	 * @return date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Gets the rating of this entry
	 * 
	 * @return rating
	 */
	public int getRating() {
		return rating;
	}

	
	public int compareTo(Entry entry) {
		return Integer.compare(this.getRating(), entry.getRating());
	}
}
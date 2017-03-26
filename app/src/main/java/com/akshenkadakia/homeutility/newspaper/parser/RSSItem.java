package com.akshenkadakia.homeutility.newspaper.parser;

import java.io.Serializable;

public class RSSItem implements Serializable {

	// Create the strings we need to store
	private String	title, description, date, Thumb, author, URL;
	// Serializable ID
	private static final long serialVersionUID = 1L;

	void setTitle(String nTitle) {
		// Sets the title
		title = nTitle;
	}

	void setAuthor(String nAuthor) {
		// Sets the author
		author = nAuthor;
	}

	void setURL(String nURL) {
		// Sets the URL
		URL = nURL;
	}

	void setDescription(String nDescription) {
		// Sets the description
		description = nDescription;
	}

	void setDate(String nDate) {
		// Sets the date
		date = nDate;
	}

	void setThumb(String nThumb) {
		// Sets the thumbnail
		Thumb = nThumb;
	}

	public String getTitle() {
		// Returns the title
		return title;
	}

	public String getDescription() {
		// Returns the description
		return description;
	}

	public String getDate() {
		// Returns the date
		return date;
	}

	public String getThumb() {
		// Returns the thumbnail
		return Thumb;
	}

	public String getAuthor(){
		// Returns the author
		return author;
	}

	public String getURL(){
		// Returns the URL
		return URL;
	}
}

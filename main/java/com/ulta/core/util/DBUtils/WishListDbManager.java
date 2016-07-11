/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.util.DBUtils;

import android.content.ContentValues;
import android.database.Cursor;

import com.ulta.core.bean.account.WishListProductBean;
import com.ulta.core.bean.account.WishListUserBean;
import com.ulta.core.util.log.Logger;

import java.util.ArrayList;
import java.util.List;



/**
 * Class used to perform database operations for the product attribute table.
 * 
 * @author Infosys
 */
public class WishListDbManager {
	// constants
	/**
	 * The dbHelper.
	 */
	private DBHelper dbHelper = new DBHelper("DBUltaWishList");

	/**
	 * The Constant BLANK_SPACE.
	 */
	private static final String BLANK_SPACE = " ";

	/**
	 * The Constant BASE_SCRIPT.
	 */
	private static final String BASE_SCRIPT = "CREATE TABLE IF NOT EXISTS";

	/**
	 * The Constant WISHLIST_TABLE.
	 */
	private static final String WISHLIST_TABLE = "WISHLIST";

	/**
	 * The Constant WISHLIST_TABLE.
	 */
	private static final String WISHLIST_MANAGE_TABLE = "WISHLIST_USER";

	/**
	 * The Constant FROM.
	 */
	private static final String FROM = "from";

	/**
	 * The Constant WHERE.
	 */
	private static final String WHERE = "where";

	/**
	 * The Constant COMMA_SEPERATOR.
	 */
	private static final String COMMA_SEPERATOR = ",";
	
	/**
	 * The Constant COMMA_SEPERATOR.
	 */
	private static final String APOSTROPHE = "'";

	/**
	 * The Constant SELECT.
	 */
	private static final String SELECT = "Select";

	/**
	 * The Constant STAR.
	 */
	private static final String STAR = "*";

	/**
	 * The Constant DISTINCT.
	 */
	/*private static final String DISTINCT = "distinct";*/

	/**
	 * The Constant PRIMARY_KEY.
	 */
	private static final String PRIMARY_KEY = "PRIMARY KEY";

	/**
	 * The Constant INTEGER.
	 */
	private static final String INTEGER = "INTEGER";

	/**
	 * The Constant TEXT.
	 */
	private static final String TEXT = "TEXT";

	// Table column names
	/**
	 * The itemId.
	 */
	private String itemId = "ITEMID";

	/**
	 * The imageUrl.
	 */
	private String imageUrl = "IMAGEURL";

	/**
	 * The itemName.
	 */
	private String itemName = "ITEMNAME";

	/**
	 * The brandName.
	 */
	private String brandName = "BRANDNAME";

	/**
	 * The selectedOption.
	 */
	private String selectedOption = "SELECTEDOPTION";

	/**
	 * The price.
	 */
	private String price = "PRICE";

	/** The user name. */
	private String userName = "USERNAME";

	/** The wish list id. */
	private String wishListId = "WISHLISTID";

	/** The wish list name. */
	private String wishListName = "WISHLISTNAME";

	/** The cursor. */
	private Cursor cursor;
	
	/**
	 * Method to create the table for products.
	 */
	public void createProductTable() {
		// Create table query
		final StringBuffer catalogTableAttribute = new StringBuffer("(")
				.append(itemId).append(BLANK_SPACE).append(TEXT)
				.append(BLANK_SPACE).append(PRIMARY_KEY)
				.append(COMMA_SEPERATOR).append(wishListId).append(BLANK_SPACE)
				.append(INTEGER).append(COMMA_SEPERATOR).append(imageUrl)
				.append(BLANK_SPACE).append(TEXT).append(COMMA_SEPERATOR)
				.append(itemName).append(BLANK_SPACE).append(TEXT)
				.append(COMMA_SEPERATOR).append(brandName).append(BLANK_SPACE)
				.append(TEXT).append(COMMA_SEPERATOR).append(selectedOption)
				.append(BLANK_SPACE).append(TEXT).append(COMMA_SEPERATOR)
				.append(price).append(BLANK_SPACE).append(INTEGER).append(")");
		final StringBuffer createProductAttributeTable = new StringBuffer();
		createProductAttributeTable.append(BASE_SCRIPT).append(BLANK_SPACE)
				.append(WISHLIST_TABLE).append(BLANK_SPACE)
				.append(catalogTableAttribute);
		// Opening the database
		dbHelper.open();
		// Create table if not exists
		dbHelper.createTable(createProductAttributeTable.toString());
		dbHelper.close();
	}

	/**
	 * Method to create the table for wishlist manager.
	 */
	public void createWishListManagerTable() {
		// Create table query
		final StringBuffer catalogTableAttribute = new StringBuffer("(")
				.append(userName).append(BLANK_SPACE).append(TEXT)
				.append(COMMA_SEPERATOR).append(wishListId).append(BLANK_SPACE)
				.append(INTEGER).append(BLANK_SPACE).append(PRIMARY_KEY)
				.append(COMMA_SEPERATOR).append(wishListName)
				.append(BLANK_SPACE).append(TEXT).append(")");
		final StringBuffer createProductAttributeTable = new StringBuffer();
		createProductAttributeTable.append(BASE_SCRIPT).append(BLANK_SPACE)
				.append(WISHLIST_MANAGE_TABLE).append(BLANK_SPACE)
				.append(catalogTableAttribute);
		// Opening the database
		dbHelper.open();
		// Create table if not exists
		dbHelper.createTable(createProductAttributeTable.toString());
		dbHelper.close();
	}

	/**
	 * Method to insert values to the wishlist manager table.
	 *
	 * @param wishListName the wish list name
	 */
	// TODO Pass the bean to the metod
	public void insertIntoWishListManagerTable(String wishListName) {
		dbHelper.open();
		// Setting the values to be inserted
		final ContentValues contentValues = new ContentValues();
		contentValues.put(userName, "abhi");
		contentValues.put(wishListId, 4);
		contentValues.put(this.wishListName, wishListName);
		dbHelper.insertIntoTable(WISHLIST_MANAGE_TABLE, contentValues);
		dbHelper.close();
	}

	/**
	 * Method to insert values to the product table.
	 * 
	 */
	// TODO Pass the bean to the metod
	public void insertIntoProductTable() {
		dbHelper.open();
		// Setting the values to be inserted
		final ContentValues contentValues = new ContentValues();
		contentValues.put(itemId, "123");
		contentValues.put(wishListId, 1);
		contentValues.put(imageUrl, "http://somthing.jpg");
		contentValues.put(itemName, "Checking");
		contentValues.put(brandName, "Wow brand name");
		contentValues.put(selectedOption, "selected option2");
		contentValues.put(price, 12);
		dbHelper.insertIntoTable(WISHLIST_TABLE, contentValues);
		dbHelper.close();
	}

	/**
	 * Method to select data from the product table.
	 *
	 * @param wishListId the wish list id
	 * @return the list
	 */

	public List<WishListProductBean> selectFromProductTable(int wishListId) {
		try {
			dbHelper.open();
			cursor = dbHelper.selectFromTable(
					selectQueryGeneratorForProductTable(wishListId), null);
			final int count = cursor.getCount();
			if (0 != count) {
				List<WishListProductBean> wishListProductBeans = new ArrayList<WishListProductBean>();
				while (cursor.moveToNext()) {
					WishListProductBean wishListProductBean = new WishListProductBean();
					wishListProductBean.setItemId(cursor.getString(cursor
							.getColumnIndex(itemId)));
					wishListProductBean.setImageUrl(cursor.getString(cursor
							.getColumnIndex(imageUrl)));
					wishListProductBean.setBrandName(cursor.getString(cursor
							.getColumnIndex(brandName)));
					wishListProductBean.setItemName(cursor.getString(cursor
							.getColumnIndex(itemName)));
					wishListProductBean.setSelectedOption(cursor
							.getString(cursor.getColumnIndex(selectedOption)));
					wishListProductBean.setPrice(String.valueOf(cursor
							.getString(cursor.getColumnIndex(price))));
					wishListProductBeans.add(wishListProductBean);
				}
				return wishListProductBeans;
			}
			return null;
		} catch (final Exception e) {
			Logger.Log("Exception: " + e.getMessage());
			return null;
		} finally {
			if (null != cursor) {
				cursor.close();
			}
			dbHelper.close();
		}
	}

	/**
	 * Method to generate the select query.
	 *
	 * @param wishListId the wish list id
	 * @return the string
	 */
	public String selectQueryGeneratorForProductTable(int wishListId) {
		final StringBuffer selectQuery = new StringBuffer(SELECT);

		selectQuery.append(BLANK_SPACE);
		selectQuery.append(STAR).append(BLANK_SPACE).append(FROM);
		selectQuery.append(BLANK_SPACE).append(WISHLIST_TABLE)
				.append(BLANK_SPACE).append(WHERE).append(BLANK_SPACE);
		selectQuery.append(this.wishListId).append("=").append(wishListId);
		Logger.Log(selectQuery.toString());
		return selectQuery.toString();
	}
	
	/**
	 * Method to select data from the user table.
	 *
	 * @param userName the user name
	 * @return the list
	 */

	public List<WishListUserBean> selectFromUserTable(String userName) {
		try {
			dbHelper.open();
			cursor = dbHelper.selectFromTable(
					selectQueryGeneratorForUserTable(userName), null);
			final int count = cursor.getCount();
			if (0 != count) {
				List<WishListUserBean> wishListUserBeans = new ArrayList<WishListUserBean>();
				while (cursor.moveToNext()) {
					WishListUserBean wishListUserBean = new WishListUserBean();
					wishListUserBean.setWishListId(Integer.parseInt(cursor.getString(cursor
							.getColumnIndex(wishListId))));
					wishListUserBean.setWishListName(cursor.getString(cursor
							.getColumnIndex(wishListName)));
					wishListUserBeans.add(wishListUserBean);
				}
				return wishListUserBeans;
			}
			return null;
		} catch (final Exception e) {
			Logger.Log("Exception: " + e.getMessage());
			return null;
		} finally {
			if (null != cursor) {
				cursor.close();
			}
			dbHelper.close();
		}
	}
	
	/**
	 * Method to generate the select query for user table.
	 *
	 * @param userName the user name
	 * @return the string
	 */
	public String selectQueryGeneratorForUserTable(String userName) {
		final StringBuffer selectQuery = new StringBuffer(SELECT);
		selectQuery.append(BLANK_SPACE);
		selectQuery.append(STAR).append(BLANK_SPACE).append(FROM);
		selectQuery.append(BLANK_SPACE).append(WISHLIST_MANAGE_TABLE)
				.append(BLANK_SPACE).append(WHERE).append(BLANK_SPACE);
		selectQuery.append(this.userName).append("=").append(APOSTROPHE).append(userName).append(APOSTROPHE);
		Logger.Log(selectQuery.toString());
		return selectQuery.toString();
	}

	/**
	 * Method to check if data exist in the table.
	 *
	 * @param item the item
	 * @param value the value
	 */
	/*
	 * public void checkTable(String[] columName, Map<String, String>
	 * whereConditionMap, boolean isCountRequired) { try { dbHelper.open();
	 * cursor = dbHelper.selectFromTable(selectQueryGenerator(columName,
	 * whereConditionMap), null); final int count = cursor.getCount(); if (0 <
	 * count) { setResultAvailable(true); } else { setResultAvailable(false); }
	 * if (!isCountRequired) { if (0 != count) { while (cursor.moveToNext()) {
	 * if ((null != cursor.getString(cursor.getColumnIndex(columName[0]))) &&
	 * !cursor.getString(cursor.getColumnIndex(columName[0])).equals("")) {
	 * result = cursor.getString(cursor.getColumnIndex(columName[0])); } } }
	 * else { result = null; } } } catch (final Exception e) {
	 * Logger.Log("Exception: " + e.getMessage()); } finally { if (null !=
	 * cursor) { cursor.close(); } dbHelper.close(); } }
	 */
	/**
	 * Method to delete data from table.
	 * 
	 * @param item
	 *            the item
	 * @param value
	 *            the value
	 */
	public void deleteFromTable(String item, String value) {
		try {
			dbHelper.open();
			dbHelper.deleteFromTable(WISHLIST_TABLE, item, value);
		} catch (final Exception e) {
			Logger.Log("Exception: " + e.getMessage());
		} finally {
			Logger.Log("DELETE FROM " + WISHLIST_TABLE + " WHERE " + item
					+ " = '" + value + "'");
			dbHelper.close();
		}
	}

	/**
	 * Method to delete complete data from table.
	 * 
	 */
	public void deleteFromTable() {
		// Create the table if not exists
		createProductTable();
		Logger.Log("Deleting from product attribute table...");
		final String query = "DELETE FROM " + WISHLIST_TABLE;
		Logger.Log(query);
		try {
			dbHelper.open();
			dbHelper.deleteFromTable(query);
		} catch (final Exception e) {
			Logger.Log("Delete form table exception: " + e.getMessage());
		} finally {
			dbHelper.close();
		}
	}

	/**
	 * Method to drop the table.
	 */
	public void dropTable() {
		final String query = "DROP TABLE IF EXISTS " + WISHLIST_TABLE;
		Logger.Log(query);
		try {
			dbHelper.open();
			dbHelper.dropTable(query);
		} catch (final Exception e) {
			Logger.Log("Drop Table exception: " + e.getMessage());
		} finally {
			dbHelper.close();
		}
	}

}
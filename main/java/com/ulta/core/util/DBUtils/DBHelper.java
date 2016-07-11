/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.util.DBUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.ulta.core.util.log.Logger;



/**
 * Class to do the data base operations.
 *
 * @author Infosys
 */
public class DBHelper {

	/**
	 * The sqlLiteDatabase.
	 */
	private SQLiteDatabase sqlLiteDatabase = null;

	/**
	 * The databaseName.
	 */
	private String databaseName;

	/**
	 * The databasePath.
	 */
	private String databasePath;

	/**
	 * Instantiates a new DBHelper.
	 */
	public DBHelper() {
	}

	/**
	 * Method to define the database path.
	 *
	 * @param dbName the db name
	 */
	public DBHelper(String dbName) {
		databaseName = dbName;
		databasePath = "data/data/com.ulta/" + databaseName + ".db";
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	public boolean checkDataBase() {
		try {
			sqlLiteDatabase = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READONLY);
		} catch (final SQLiteException e) {
			Logger.Log(e.getMessage());
		}
		if (sqlLiteDatabase != null) {
			sqlLiteDatabase.close();
		}
		return sqlLiteDatabase != null ? true : false;
	}

	/**
	 * Method to open database.
	 */
	public void open() {
		try {
			sqlLiteDatabase = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
		} catch (final Exception e) {
			Logger.Log(e.getMessage());
		}
	}

	/**
	 * Method to close database.
	 */
	public void close() {
		sqlLiteDatabase.close();
	}

	/**
	 * Method to create table.
	 *
	 * @param query the query
	 */
	public void createTable(String query) {
		try {
			sqlLiteDatabase.execSQL(query);
		} catch (final SQLiteException e) {
			Logger.Log(e.getMessage());
		}
	}

	/**
	 * Method to delete data a particular from table.
	 *
	 * @param table the table
	 * @param item the item
	 * @param value the value
	 */
	public void deleteFromTable(String table, String item, String value) {
		try {
			sqlLiteDatabase.delete(table, item + " = '" + value + "'", null);
		} catch (final SQLiteException e) {
			Logger.Log(e.getMessage());
		}
	}

	/**
	 * Method to delete complete data from table.
	 *
	 * @param query the query
	 */
	public void deleteFromTable(String query) {
		try {
			sqlLiteDatabase.execSQL(query);
		} catch (final SQLiteException e) {
			Logger.Log(e.getMessage());
		}
	}

	/**
	 * Method to drop table.
	 *
	 * @param query the query
	 */
	public void dropTable(String query) {
		try {
			sqlLiteDatabase.execSQL(query);
		} catch (final SQLiteException e) {
			Logger.Log(e.getMessage());
		}
	}

	/**
	 * Method to insert into table.
	 *
	 * @param tablename the tablename
	 * @param insertionValues the insertion values
	 */
	public void insertIntoTable(String tablename, ContentValues insertionValues) {
		try {
			sqlLiteDatabase.insert(tablename, null, insertionValues);
		} catch (final SQLiteException e) {
			Logger.Log(e.getMessage());
		}
	}

	/**
	 * Method to update a row of given table.
	 *
	 * @param tableName the table name
	 * @param contentValues the content values
	 * @param whereClause the where clause
	 * @param whereArgs the where args
	 */
	public void updateTableValues(String tableName,
			ContentValues contentValues, String whereClause, String[] whereArgs) {
		try {
			sqlLiteDatabase.update(tableName, contentValues, whereClause,
					whereArgs);
		} catch (final SQLiteException e) {
			Logger.Log(e.getMessage());
		}
	}
	
	/**
	 * Method to delete a particular row from a given table.
	 *
	 * @param tableName the table name
	 * @param whereClause the where clause
	 * @param whereArgs the where args
	 */
	public void deleteTableValues(
			String tableName, String whereClause, String[] whereArgs) {
		try {
			sqlLiteDatabase.delete(tableName, whereClause, whereArgs);
		} catch (final SQLiteException e) {
			Logger.Log(e.getMessage());
		}
	}

	/**
	 * Method to select from table.
	 *
	 * @param query the query
	 * @param params the params
	 * @return the cursor
	 */
	public Cursor selectFromTable(String query, String[] params) {
		final Cursor cur = sqlLiteDatabase.rawQuery(query, params);
		return cur;
	}
}

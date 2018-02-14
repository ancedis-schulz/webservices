package database.entities; 

/**
 * This Interface provides sql statements for performing database actions on the user group table
 * 
 * @author Stephan Schulz
 *
 */
public interface UserGroupEntity {
	
	/**
	 * table name
	 */
	String TABLE = "UserGroup";
	
	/**
	 * table column id
	 */
	String ID = "_id";

	/**
	 * table column name 
	 */
	String NAME = "_name";

	/**
	 * right for adding and removing users related to a project
	 */
	String CHANGE_USER_ATHORIZATION = "_userRights";

	/**
	 * right for adding and removing files related to a project
	 */
	String CHANGE_FILE_ATHORIZATION = "_fileRights";

	/**
	 * right for adding and removing files related to a project
	 */
	String CHANGE_PROJECT_ATHORIZATION = "_projectRights";

	/**
	 * create table sql statement
	 */
	String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE + "( "
			+ ID + " INT PRIMARY KEY AUTO_INCREMENT, "
			+ NAME + " VARCHAR(255) NOT NULL, " 
			+ CHANGE_PROJECT_ATHORIZATION + " BOOLEAN DEFAULT FALSE, "
			+ CHANGE_USER_ATHORIZATION + " BOOLEAN DEFAULT FALSE, "
			+ CHANGE_FILE_ATHORIZATION + " BOOLEAN DEFAULT FALSE, "
			+ "UNIQUE(" + NAME + ")"
			+ " )";

	/**
	 * insert values sql statement
	 */
	String FULL_INSERT = "INSERT INTO " + TABLE + "(" + NAME + ", " + CHANGE_PROJECT_ATHORIZATION + ", " + CHANGE_USER_ATHORIZATION + ", " + CHANGE_FILE_ATHORIZATION + ") VALUES (?, ?, ?, ?)";

	/**
	 * select all sql statement
	 */
	String SELECT_ALL = "SELECT " + ID + ", " + NAME + ", "  + CHANGE_PROJECT_ATHORIZATION + ", " + CHANGE_USER_ATHORIZATION + ", " + CHANGE_FILE_ATHORIZATION + " FROM " + TABLE;

	/**
	 * select one data set for a given name sql statement
	 */
	String SELECT_FOR_NAME = "SELECT " + ID + ", " + NAME + ", " + CHANGE_PROJECT_ATHORIZATION + ", "  + CHANGE_USER_ATHORIZATION + ", " + CHANGE_FILE_ATHORIZATION + " FROM " + TABLE + " WHERE " + NAME + " LIKE ?";

	/**
	 * select one data set for a given id sql statement
	 */
	String SELECT_FOR_ID = "SELECT " + ID + ", " + NAME + ", " + CHANGE_PROJECT_ATHORIZATION + ", "  + CHANGE_USER_ATHORIZATION + ", " + CHANGE_FILE_ATHORIZATION + " FROM " + TABLE + " WHERE " + ID + " = ?";

	/**
	 * select all sql statement
	 */
	String SELECT_ALL_WITHOUT_CREATOR = "SELECT " + ID + ", " + NAME + ", "  + CHANGE_PROJECT_ATHORIZATION + ", " + CHANGE_USER_ATHORIZATION + ", " + CHANGE_FILE_ATHORIZATION + " FROM " + TABLE + " WHERE " + NAME + " != ?";

	/**
	 * delete one data set for a given id sql statement
	 */
	String DELETE_FOR_ID = "DELETE FROM " + TABLE + " WHERE " + ID + " = ?";
}

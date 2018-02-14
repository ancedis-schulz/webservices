package database.entities;


/**
 * This Interface provides sql statements for performing database actions on the user table
 * 
 * @author Stephan Schulz
 *
 */
public interface UserEntity{

	/**
	 * table name
	 */
	String TABLE = "User";
	
	/**
	 * table column id
	 */
	String ID = "_id";

	/**
	 * table column user name 
	 */
	String USER_NAME = "_userName";

	/**
	 * table column password
	 */
	String PASSWORD = "_password";

	/**
	 * create table sql statement
	 */
	String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE + "( "
			+ ID + " INT PRIMARY KEY AUTO_INCREMENT, "
			+ USER_NAME + " VARCHAR(255) NOT NULL, " 
			+ PASSWORD + " VARCHAR(255) NOT NULL, "
			+ "UNIQUE(" + USER_NAME + ")"
			+ " ); ";

	/**
	 * insert sql statement
	 */
	String INSERT = "INSERT INTO " + TABLE + "(" + USER_NAME + ", " + PASSWORD + ") VALUES (?,?)";

	/**
	 * select for id sql statement
	 */
	String SELECT_FOR_ID = "SELECT " + ID + ", " + USER_NAME + ", " + PASSWORD + " FROM " + TABLE + " WHERE " + ID + " = ?";

	/**
	 * select all
	 */
	String SELECT_ALL = "SELECT " + ID + ", " + USER_NAME + ", " + PASSWORD + " FROM " + TABLE + "";

	/**
	 * select for username and password sql statement
	 */
	String SELECT_FOR_USERNAME_PASSWORD = "SELECT " + ID + ", " + USER_NAME + ", " + PASSWORD + " FROM " + TABLE + " WHERE " + USER_NAME + " = ? AND " + PASSWORD + " = ?" ;

	/**
	 * update sql statement for id 
	 */
	String UPDATE = "UPDATE " + TABLE + " SET " + USER_NAME + " = ?, " + PASSWORD + " = ? WHERE " + ID + " = ?";

	/**
	 * delete sql statement for id 
	 */
	String DELETE_FOR_ID = "DELETE FROM " + TABLE + " WHERE " + ID + " = ?";
	
	/**
	 * sql string to select all users and their respective user groups for a project
	 */
	String SELECT_USER_FOR_PROJECT = "SELECT " 
										+ UserEntity.TABLE + "." + UserEntity.ID + ", " 
										+ UserEntity.TABLE + "." + UserEntity.USER_NAME + ", "
										+ UserEntity.TABLE + "." + UserEntity.PASSWORD + ", "
										+ UserGroupEntity.TABLE + "." + UserGroupEntity.ID + ", "
										+ UserGroupEntity.TABLE + "." + UserGroupEntity.NAME + ", "
										+ UserGroupEntity.TABLE + "." + UserGroupEntity.CHANGE_USER_ATHORIZATION + ", "
										+ UserGroupEntity.TABLE + "." + UserGroupEntity.CHANGE_FILE_ATHORIZATION
										+ " FROM "
										+ ProjectUserEntity.TABLE
										+ " JOIN " + UserEntity.TABLE + " ON " + ProjectUserEntity.TABLE + "." + ProjectUserEntity.USER + "=" + UserEntity.TABLE + "." + UserEntity.ID
										+ " JOIN " + UserGroupEntity.TABLE + " ON " + ProjectUserEntity.TABLE + "." + ProjectUserEntity.USER_GROUP + "=" + UserGroupEntity.TABLE + "." + UserGroupEntity.ID
										+ " WHERE "
										+ ProjectUserEntity.PROJECT + " = ?";

	/**
	 * select sql string for getting the user that are not part of the project
	 */
	String SELECT_PROJECT_OUTSIDE_USER = "SELECT DISTINCT "
			+ UserEntity.ID + ", "
			+ UserEntity.USER_NAME + ", "
			+ UserEntity.PASSWORD + " "
			+ " FROM " + TABLE
			+ " WHERE " + UserEntity.TABLE + "." + UserEntity.ID + " NOT IN "
					+ "(SELECT "
					+ ProjectUserEntity.USER 
					+ " FROM " 
					+ ProjectUserEntity.TABLE 
					+ " WHERE "
					+ ProjectUserEntity.TABLE + "." + ProjectUserEntity.PROJECT + " = ?)";
}

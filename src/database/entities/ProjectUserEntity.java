package database.entities;

/**
 * Database Entity for Project Users
 * 
 * @author Stephan Schulz
 *
 */
public interface ProjectUserEntity {

	/**
	 * table name
	 */
	String TABLE = "ProjectUser";

	/**
	 * column project
	 */
	String PROJECT = "_project";

	/**
	 * column user 
	 */
	String USER = "_user";

	/**
	 * column user group
	 */
	String USER_GROUP = "_userGroup";
	
	/**
	 * create table sql string
	 */
	String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE + " ( " 
				+ PROJECT + " INT NOT NULL, "
				+ USER + " INT NOT NULL, "
				+ USER_GROUP + " INT NOT NULL, "
				+ "PRIMARY KEY(" + PROJECT + ", " + USER + "), "
				+ "FOREIGN KEY(" + PROJECT + ") REFERENCES " + ProjectEntity.TABLE + "(" + ProjectEntity.ID +"),"
				+ "FOREIGN KEY(" + USER + ") REFERENCES " + UserEntity.TABLE + "(" + UserEntity.ID +"),"
				+ "FOREIGN KEY(" + USER_GROUP + ") REFERENCES " + UserGroupEntity.TABLE + "(" + UserGroupEntity.ID +")"
				+ ")";
	
	/**
	 * inserts a new data set
	 */
	String FULL_INSERT = "INSERT INTO " + TABLE + "(" + PROJECT + ", " + USER + ", " + USER_GROUP + ") VALUES (?, ?, ?)";

	/**
	 * delete sql statement for id 
	 */
	String DELETE_FOR_PROJECT = "DELETE FROM " + TABLE + " WHERE " + PROJECT + " = ?";

	/**
	 * delete for project user sql statement for id 
	 */
	String DELETE_FOR_PROJECT_USER = "DELETE FROM " + TABLE + " WHERE " + PROJECT + " = ? AND " + USER + " = ?";
	
	/**
	 * select sql string for getting the user group of a specific project user
	 */
	String SELECT_USER_GROUP_FOR_PROJECT_USER = "SELECT "
			+ UserGroupEntity.ID + ", "
			+ UserGroupEntity.NAME + ", "
			+ UserGroupEntity.CHANGE_PROJECT_ATHORIZATION + ", "
			+ UserGroupEntity.CHANGE_USER_ATHORIZATION + ", "
			+ UserGroupEntity.CHANGE_FILE_ATHORIZATION + " "
			+ " FROM " + TABLE
			+ " JOIN " + UserGroupEntity.TABLE + " ON " + USER_GROUP + " = " + UserGroupEntity.ID
			+ " WHERE " + PROJECT + " = ? AND " + USER + " = ?";
	
}

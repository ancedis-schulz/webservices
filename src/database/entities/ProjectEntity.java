package database.entities;

/**
 * Database Entity for Projects
 * 
 * @author Stephan Schulz
 *
 */
public interface ProjectEntity {

	/**
	 * table name
	 */
	String TABLE = "Project";

	/**
	 * column id
	 */
	String ID = "_id";

	/**
	 * column name
	 */
	String NAME = "_name";

	/**
	 * column description 
	 */
	String DESCRIPTION = "_description";
	
	/**
	 * create table sql string
	 */
	String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE + " ( " 
				+ ID + " INT PRIMARY KEY AUTO_INCREMENT, "
				+ NAME + " VARCHAR(255) NOT NULL, "
				+ DESCRIPTION + " TEXT, "
				+ "UNIQUE(" + NAME + ")"
				+ ")";
	
	/**
	 * insert string to create a new project
	 */
	String CREATE_PROJECT = "INSERT INTO " + TABLE + "(" + NAME + ") VALUES (?)";

	/**
	 * select all rojects for a user
	 */
	String SELECT_ALL_FOR_USER = "SELECT DISTINCT " + ID + ", " + NAME + ", " + DESCRIPTION + " FROM " + TABLE + " JOIN " + ProjectUserEntity.TABLE + " ON " + TABLE + "." + ID + " = " + ProjectUserEntity.TABLE + "." + ProjectUserEntity.PROJECT + " WHERE " + ProjectUserEntity.USER + " = ?";

	/**
	 * delete sql statement for id 
	 */
	String DELETE_FOR_ID = "DELETE FROM " + TABLE + " WHERE " + ID + " = ?";

	/**
	 * delete sql statement for id 
	 */
	String UPDATE_PROJECT_DESCRIPTION = "UPDATE " + TABLE + " SET " + DESCRIPTION + " = ? WHERE " + ID + " = ?";
	
	String SEARCH = "SELECT "
			+ ProjectEntity.TABLE + "." + ProjectEntity.ID + ", " 
			+ ProjectEntity.TABLE + "." + ProjectEntity.NAME + ", " 
			+ FileEntity.TABLE + "." + FileEntity.ID + ", " 
			+ FileEntity.TABLE + "." + FileEntity.FILE_NAME + " " 
			+ " FROM "
			+ ProjectEntity.TABLE
			+ " LEFT JOIN "
			+ ProjectUserEntity.TABLE
			+ " ON " + ProjectEntity.TABLE + "." + ProjectEntity.ID + " = " + ProjectUserEntity.TABLE + "." + ProjectUserEntity.PROJECT
			+ " LEFT JOIN "
			+ UserEntity.TABLE
			+ " ON " + UserEntity.TABLE + "." + UserEntity.ID + " = " + ProjectUserEntity.TABLE + "." + ProjectUserEntity.USER
			+ " LEFT JOIN "
			+ FileEntity.TABLE
			+ " ON " + ProjectEntity.TABLE + "." + ProjectEntity.ID + " = " + FileEntity.TABLE + "." + FileEntity.PROJECT
			+ " LEFT JOIN "
			+ FileDescriptorEntity.TABLE
			+ " ON " + FileDescriptorEntity.TABLE + "." + FileDescriptorEntity.FILE + " = " + FileEntity.TABLE + "." + FileEntity.ID
			+ " WHERE "
			+ ProjectEntity.TABLE + "." + ProjectEntity.NAME + " LIKE ?"
			+ " AND "
			+ UserEntity.TABLE + "." + UserEntity.USER_NAME + " LIKE ?"
			+ " AND "
			+ FileDescriptorEntity.TABLE + "." + FileDescriptorEntity.DESCRIPTOR + " LIKE ?";
}
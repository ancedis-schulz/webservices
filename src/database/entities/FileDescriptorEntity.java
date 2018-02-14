package database.entities;


/**
 * DB entity for file descriptors
 * 
 * @author Stephan Schulz
 *
 */
public interface FileDescriptorEntity {

	/**
	 * table name
	 */
	String TABLE = "FileDescriptor";

	/**
	 * column id
	 */
	String ID = "_id";

	/**
	 * column project
	 */
	String FILE = "_file";

	/**
	 * column descriptor
	 */
	String DESCRIPTOR = "_descriptor";
	
	/**
	 * create table sql string
	 */
	String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE + " ( " 
				+ ID + " INT PRIMARY KEY AUTO_INCREMENT, "
				+ FILE + " INT NOT NULL, "
				+ DESCRIPTOR + " TEXT, "
				+ "FOREIGN KEY(" + FILE + ") REFERENCES " + FileEntity.TABLE + "(" + FileEntity.ID +")"
				+ ")";
	
	/**
	 * insert string to create a new project
	 */
	String CREATE_FILE = "INSERT INTO " + TABLE + "(" + FILE + ", " + DESCRIPTOR + ") VALUES (?,?)";

	/**
	 * sql string to select all descriptors for a file
	 */
	String SELECT_DESCRIPTORS_FOR_FILE = "SELECT " + ID + "," + DESCRIPTOR + " FROM " + TABLE + " WHERE " + FILE + " = ?";

	/**
	 * sql string to select all descriptors for a id
	 */
	String SELECT_DESCRIPTORS_FOR_ID = "SELECT " + ID + "," + DESCRIPTOR + " FROM " + TABLE + " WHERE " + ID + " = ?";

	/**
	 * sql string to select all descriptors for a id
	 */
	String DELETE_FOR_ID = "DELETE FROM " + TABLE + " WHERE " + ID + " = ?";

}

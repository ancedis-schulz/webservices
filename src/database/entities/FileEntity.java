package database.entities;

/**
 * DB entity for files
 * 
 * @author Stephan Schulz
 *
 */
public interface FileEntity {

	/**
	 * table name
	 */
	String TABLE = "File";

	/**
	 * column id
	 */
	String ID = "_id";

	/**
	 * column name
	 */
	String PROJECT = "_project";

	/**
	 * column file name
	 */
	String FILE_NAME = "_fileName";

	/**
	 * column file
	 */
	String FILE = "_file";

	/**
	 * column file name
	 */
	String CONTENT_TYPE = "_contentType";

	/**
	 * column metaFile
	 */
	String META_FILE = "_metaFile";

	/**
	 * column metaFilename
	 */
	String META_FILE_NAME = "_metaFileName";

	/**
	 * column file name
	 */
	String META_CONTENT_TYPE = "_metaContentType";

	/**
	 * column file description
	 */
	String FILE_DESCRIPTION = "_fileDescription";
	
	
	/**
	 * create table sql string
	 */
	String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE + " ( " 
				+ ID + " INT PRIMARY KEY AUTO_INCREMENT, "
				+ PROJECT + " INT NOT NULL, "
				+ FILE_NAME + " VARCHAR(255) NOT NULL, "
				+ FILE + " BLOB NOT NULL, "
				+ CONTENT_TYPE + " VARCHAR(255) NOT NULL, "
				+ META_FILE + " BLOB NULL, "
				+ META_FILE_NAME + " VARCHAR(255) NULL, "
				+ META_CONTENT_TYPE + " VARCHAR(255) NULL, "
				+ FILE_DESCRIPTION + " TEXT, "
				+ "FOREIGN KEY(" + PROJECT + ") REFERENCES " + ProjectEntity.TABLE + "(" + ProjectEntity.ID +")"
				+ ")";
	
	/**
	 * insert string to create a new project
	 */
	String CREATE_FILE = "INSERT INTO " + TABLE + "(" + PROJECT + ", " + FILE_NAME + ", " + FILE + ", " + CONTENT_TYPE +  ") VALUES (?,?,?,?)";

	/**
	 * select all files for a project
	 */
	String SELECT_FILES_FOR_PROJECT = "SELECT " + ID + ", " + FILE_NAME + ", " + FILE + ", " + CONTENT_TYPE + " FROM " + TABLE + " WHERE " + PROJECT + " = ?";

	/**
	 * select a file for id
	 */
	String SELECT_FOR_ID = "SELECT " + ID + ", " + FILE_NAME + ", " + FILE + ", " + CONTENT_TYPE + " FROM " + TABLE + " WHERE " + ID + " = ?";

	/**
	 * select a file for id
	 */
	String SELECT_PROJECT_FOR_ID = "SELECT " + ProjectEntity.TABLE + "." + ProjectEntity.ID + ", " + ProjectEntity.TABLE + "." + ProjectEntity.NAME + ", " + ProjectEntity.TABLE + "." + ProjectEntity.DESCRIPTION + " FROM " + TABLE + " JOIN " + ProjectEntity.TABLE + " ON " + ProjectEntity.TABLE + "." + ProjectEntity.ID + " = " + TABLE + "." + PROJECT +  " WHERE " + TABLE + "." + ID + " = ?";

	/**
	 * select a file for id
	 */
	String SELECT_METAFILE_NAME_FOR_ID = "SELECT " + META_FILE_NAME + " FROM " + TABLE + " WHERE " + ID + " = ?";

	/**
	 * select a file for id
	 */
	String SELECT_DESCRIPTION_FOR_ID = "SELECT " + FILE_DESCRIPTION + " FROM " + TABLE + " WHERE " + ID + " = ?";

	/**
	 * select a file for id
	 */
	String SELECT_METAFILE_FOR_ID = "SELECT " + META_FILE + ", " + META_FILE_NAME + ", " + META_CONTENT_TYPE + " FROM " + TABLE + " WHERE " + ID + " = ?";


	/**
	 * sql string for updating a project file to set the meta files
	 */
	String UPDATE_META_FILE = "UPDATE " + TABLE + " SET " + META_FILE + " = ?, " + META_FILE_NAME + " = ?, " + META_CONTENT_TYPE + " = ? WHERE " + ID + " = ?";
	
	/**
	 * sql string for updating the file description
	 */
	String UPDATE_DESCRIPTION = "UPDATE " + TABLE + " SET " + FILE_DESCRIPTION + " = ? WHERE " + ID + " = ?";

	/**
	 * delete file for project and filename
	 */
	String DELETE_PROJECT_FILE_FOR_FILENAME = "DELETE FROM " + TABLE + " WHERE " + PROJECT + " = ? AND " + ID + " = ?";
}

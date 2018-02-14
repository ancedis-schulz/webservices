package database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.h2.Driver;

import database.entities.FileDescriptorEntity;
import database.entities.FileEntity;
import database.entities.ProjectEntity;
import database.entities.ProjectUserEntity;
import database.entities.UserEntity;
import database.entities.UserGroupEntity;
import objects.FileDescriptor;
import objects.Project;
import objects.ProjectFile;
import objects.SearchResult;
import objects.User;
import objects.UserGroup;

/**
 * This class controls the access to the database
 * 
 * @author Stephan Schulz
 *
 */
public class DatabaseAccessObject {

	/**
	 * singleton instance
	 */
	private static DatabaseAccessObject mInstance = null;
	
	/**
	 * database connection object
	 */
	private Connection mDbCon= null;
	
	/**
	 * private constructor
	 * 
	 * @throws 	SQLException  if the db connection fails
	 */
	private DatabaseAccessObject() throws SQLException {
		connect();
		createTables();
	}
	
	/**
	 * returns the DatabaseAccessObject instance
	 * 
	 * @return	instance of DatabaseAccessObject class
	 * 
	 * @throws 	SQLException  if the db connection fails
	 */
	public static DatabaseAccessObject getInstance() throws SQLException {
		if(DatabaseAccessObject.mInstance == null){
			mInstance = new DatabaseAccessObject();
		}
		
		return mInstance;
	}
	
	/**
	 * creates the database connection
	 * 
	 * @throws 	SQLException  if the db connection fails
	 */
	private void connect() throws SQLException{
		Properties p = loadDbProperties();
		String url = p.getProperty("url");
		String catalog = p.getProperty("database");
		String username = p.getProperty("username");
		String password = p.getProperty("passwort");

		Driver.load();
		mDbCon = DriverManager.getConnection(url, username, password);
		mDbCon.setCatalog(catalog);
	}
	
	/**
	 * loads the db connection properties from a properties file
	 * 
	 * @return 	Properties Object with the db connection parameters
	 */
	private Properties loadDbProperties(){
		Properties connectionDetails = new Properties();
		InputStream is = getClass().getClassLoader().getResourceAsStream("DatabaseConnection.properties");
		try {
			connectionDetails.load(is);
		} catch (IOException e) {
			e.printStackTrace();
			connectionDetails = null;
		}
		finally {
			try{
				is.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		
		return connectionDetails;
	}

	/**
	 * closes the database connection
	 */
	public void closeDb(){
		if(mDbCon == null){
			return;
		}
		
		try{
			mDbCon.close();
			mDbCon = null;
		}
		catch (SQLException e) {
			mDbCon = null;
		}
	}
	
	/**
	 * creates new database tables if the tables not exists
	 * 
	 * @throws 	SQLException  thrown, when the table creation fails
	 */
	private void createTables() throws SQLException{
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return;
		}
		
		PreparedStatement createUserGroupTableStatement = mDbCon.prepareStatement(UserGroupEntity.CREATE_TABLE);
		createUserGroupTableStatement.executeUpdate();
		System.out.println("UserGroup Table created");
		createUserGroupTableStatement.close();

		PreparedStatement createUserTableStatement = mDbCon.prepareStatement(UserEntity.CREATE_TABLE);
		createUserTableStatement.executeUpdate();
		System.out.println("User Table created");
		createUserTableStatement.close();

		PreparedStatement createProjectStatement = mDbCon.prepareStatement(ProjectEntity.CREATE_TABLE);
		createProjectStatement.executeUpdate();
		System.out.println("Project Table created");
		createProjectStatement.close();

		PreparedStatement createProjectUserStatement = mDbCon.prepareStatement(ProjectUserEntity.CREATE_TABLE);
		createProjectUserStatement.executeUpdate();
		System.out.println("ProjectUser Table created");
		createProjectUserStatement.close();

		PreparedStatement createFileEntityStatement = mDbCon.prepareStatement(FileEntity.CREATE_TABLE);
		createFileEntityStatement.executeUpdate();
		System.out.println("FileEntity Table created");
		createFileEntityStatement.close();

		PreparedStatement createFileDescriptorEntityStatement = mDbCon.prepareStatement(FileDescriptorEntity.CREATE_TABLE);
		createFileDescriptorEntityStatement.executeUpdate();
		System.out.println("FileDescriptorEntity Table created");
		createFileDescriptorEntityStatement.close();
	}
	
	/**
	 * inserts a new user to the database
	 * 
	 * @param newUser	user that has to be inserted
	 * 
	 * @return		returns the user object with the id if the insert was successful, null otherwise
	 */
	public User createUser(User newUser){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		User result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(UserEntity.INSERT, Statement.RETURN_GENERATED_KEYS);
			insertStatement.setString(1, newUser.getUsername());
			insertStatement.setString(2, newUser.getPassword());
			insertStatement.executeUpdate();
			ResultSet keySet = insertStatement.getGeneratedKeys();
			if(keySet.next()){
				int id = keySet.getInt(1);
				result = new User(id, newUser.getUsername(), newUser.getPassword());
			}
			else{
				result = null;
			}
			
			keySet.close();
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = null;
		}
		
		return result;
	}
	
	/**
	 * reads a user with a given id from the database
	 * 
	 * @param userId	id of the user, that should be loaded
	 * 
	 * @return		returns the user with the given id
	 */
	public User readUser(Integer userId){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		User result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(UserEntity.SELECT_FOR_ID);
			insertStatement.setInt(1, userId);
			ResultSet resultSet = insertStatement.executeQuery();
			if(resultSet.isBeforeFirst() && !resultSet.isAfterLast() && resultSet.next()){
				int id = resultSet.getInt(UserEntity.ID);
				String username = resultSet.getString(UserEntity.USER_NAME);
				String password = resultSet.getString(UserEntity.PASSWORD);
				
				result = new User(id, username, password);
			}
			else{
				result = null;
			}
			
			resultSet.close();
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = null;
		}
		
		return result;
	}
	
	/**
	 * reads a user with a given id from the database
	 * 
	 * @param updatedUser	user object with updated values
	 * 
	 * @return		returns true if the update was successful, false otherwise
	 */
	public boolean updateUser(User updatedUser){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return false;
		}
		
		boolean result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(UserEntity.UPDATE);
			insertStatement.setString(1, updatedUser.getUsername());
			insertStatement.setString(2, updatedUser.getPassword());
			insertStatement.setInt(3, updatedUser.getUserId());
			int rows = insertStatement.executeUpdate();
			
			if(rows > 0){
				result = true;
			}
			else{
				result = false;
			}
			
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	/**
	 * deletes a user with a given id from the database
	 * 
	 * @param userId	id of the user that should be deleted
	 * 
	 * @return		returns true if the delete was successful, false otherwise
	 */
	public boolean deleteUser(int userId){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return false;
		}
		
		boolean result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(UserEntity.DELETE_FOR_ID);
			insertStatement.setInt(1, userId);
			int rows = insertStatement.executeUpdate();
			
			if(rows > 0){
				result = true;
			}
			else{
				result = false;
			}
			
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	/**
	 * searches after a user
	 * 
	 * @param username	user name
	 * @param password	password
	 * 
	 * @return			User if the user was found, null otherwise
	 */
	public User selectUser(String username, String password) {
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		User result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(UserEntity.SELECT_FOR_USERNAME_PASSWORD);
			insertStatement.setString(1, username);
			insertStatement.setString(2, password);
			ResultSet resultSet = insertStatement.executeQuery();
			if(resultSet.isBeforeFirst() && !resultSet.isAfterLast() && resultSet.next()){
				int id = resultSet.getInt(UserEntity.ID);
				String user = resultSet.getString(UserEntity.USER_NAME);
				String pass = resultSet.getString(UserEntity.PASSWORD);
				
				result = new User(id, user, pass);
			}
			else{
				result = null;
			}
			
			resultSet.close();
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = null;
		}
		
		return result;
	}
	
	/**
	 * loads all available users
	 * 
	 * @return	Map<String, Integer> 	returns a map with the user name as key and the user id as value
	 */
	public Map<String, Integer> selectAllUsers(){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		Map<String, Integer> result = new HashMap<>();
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(UserEntity.SELECT_ALL);
			ResultSet resultSet = insertStatement.executeQuery();
			if(resultSet.isBeforeFirst() && !resultSet.isAfterLast()){
				while (resultSet.next()) {
					int id = resultSet.getInt(UserEntity.ID);
					String username = resultSet.getString(UserEntity.USER_NAME);
					
					result.put(username, id);
				}
			}
			else{
				result = new HashMap<>();
			}
			
			resultSet.close();
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = new HashMap<>();
		}
		
		return result;
	}
	
	/**
	 * inserts a new user group to the database
	 * 
	 * @param userGroup 	user group that has to be inserted
	 * 
	 * @return		returns the user group object with the id if the insert was successful, null otherwise
	 */
	public UserGroup createUserGroup(UserGroup userGroup){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		UserGroup result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(UserGroupEntity.FULL_INSERT, Statement.RETURN_GENERATED_KEYS);
			insertStatement.setString(1, userGroup.getName());
			insertStatement.setBoolean(2, userGroup.isChangeProject());
			insertStatement.setBoolean(3, userGroup.isChangeUsers());
			insertStatement.setBoolean(4, userGroup.isChangeFiles());
			insertStatement.executeUpdate();
			ResultSet keySet = insertStatement.getGeneratedKeys();
			if(keySet.next()){
				int id = keySet.getInt(1);
				result = new UserGroup(id, userGroup.getName(), userGroup.isChangeProject(), userGroup.isChangeUsers(), userGroup.isChangeFiles());
			}
			else{
				result = null;
			}

			keySet.close();
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = null;
		}
		
		return result;
	}

	/**
	 * loads all available user groups
	 * 
	 * @return	Map<Integer, UserGroup> 	returns a map with the group id as key and the user group as value
	 */
	public Map<Integer, UserGroup> selectAllGroups(){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		Map<Integer, UserGroup> result = new HashMap<>();
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(UserGroupEntity.SELECT_ALL);
			ResultSet resultSet = insertStatement.executeQuery();
			if(resultSet.isBeforeFirst() && !resultSet.isAfterLast()){
				while (resultSet.next()) {
					int id = resultSet.getInt(UserGroupEntity.ID);
					String name = resultSet.getString(UserGroupEntity.NAME);
					boolean flagChangeProject = resultSet.getBoolean(UserGroupEntity.CHANGE_PROJECT_ATHORIZATION);
					boolean flagChangeUser = resultSet.getBoolean(UserGroupEntity.CHANGE_USER_ATHORIZATION);
					boolean flagChangeFile = resultSet.getBoolean(UserGroupEntity.CHANGE_FILE_ATHORIZATION);
					
					result.put(id, new UserGroup(id, name, flagChangeProject, flagChangeUser, flagChangeFile));
				}
			}
			else{
				result = new HashMap<>();
			}
			
			resultSet.close();
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = new HashMap<>();
		}
		
		return result;
	}
	
	/**
	 * creates a new project
	 * 
	 * @param projectName	name of the new project
	 * @param loggedUser 	User, which is logged in
	 * 
	 * @return		true if the insert was successful, false otherwise
	 */
	public boolean createProject(String projectName, User loggedUser){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return false;
		}
		
		boolean result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(ProjectEntity.CREATE_PROJECT, Statement.RETURN_GENERATED_KEYS);
			insertStatement.setString(1, projectName);
			insertStatement.executeUpdate();
			ResultSet keySet = insertStatement.getGeneratedKeys();
			int projectId = -1;
			if(keySet.next()){
				projectId = keySet.getInt(1);
				result = true;
			}
			else{
				result = false;
			}
			
			keySet.close();
			insertStatement.close();
			
			UserGroup group = getUserGroupForName("Ersteller");
			
			if(result && group != null){
				PreparedStatement insertProjectUserStatement = mDbCon.prepareStatement(ProjectUserEntity.FULL_INSERT);
				insertProjectUserStatement.setInt(1, projectId);
				insertProjectUserStatement.setInt(2, loggedUser.getUserId());
				insertProjectUserStatement.setInt(3, group.getId());
				int affectedRows = insertProjectUserStatement.executeUpdate();
				if(affectedRows > 0){
					result = true;
				}
				else{
					result = false;
				}
				
				insertProjectUserStatement.close();
			}
			else{
				result = false;
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	/**
	 * returns the user group for a given name
	 * 
	 * @param groupName 	group name which should be searched after
	 * 
	 * @return		returns the user group with the given name or null if the group does not exist
	 */
	public UserGroup getUserGroupForName(String groupName){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		UserGroup result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(UserGroupEntity.SELECT_FOR_NAME);
			insertStatement.setString(1, groupName);
			ResultSet resultSet = insertStatement.executeQuery();
			if(resultSet.isBeforeFirst() && !resultSet.isAfterLast() && resultSet.next()){
				int id = resultSet.getInt(UserGroupEntity.ID);
				String user = resultSet.getString(UserGroupEntity.NAME);
				boolean flagChangeProject = resultSet.getBoolean(UserGroupEntity.CHANGE_PROJECT_ATHORIZATION);
				boolean changeUser = resultSet.getBoolean(UserGroupEntity.CHANGE_USER_ATHORIZATION);
				boolean changeFile = resultSet.getBoolean(UserGroupEntity.CHANGE_FILE_ATHORIZATION);
				
				result = new UserGroup(id, user, flagChangeProject, changeUser, changeFile);
			}
			else{
				result = null;
			}
			
			resultSet.close();
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = null;
		}
		
		return result;
	}
	
	/**
	 * loads all available projects for a specific user
	 * 
	 * @return	Map<String, Integer> 	returns a map with the project name as key and the project as value
	 */
	public ArrayList<Project> selectAllProjects(User loggedUser){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		ArrayList<Project> result = new ArrayList<>();
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(ProjectEntity.SELECT_ALL_FOR_USER);
			insertStatement.setInt(1, loggedUser.getUserId());
			ResultSet resultSet = insertStatement.executeQuery();
			if(resultSet.isBeforeFirst() && !resultSet.isAfterLast()){
				while (resultSet.next()) {
					int id = resultSet.getInt(ProjectEntity.ID);
					String name= resultSet.getString(ProjectEntity.NAME);
					String description = resultSet.getString(ProjectEntity.DESCRIPTION);
					
					result.add(new Project(id, name, description));
				}
			}
			else{
				result = new ArrayList<>();
			}
			
			resultSet.close();
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = new ArrayList<>();
		}
		
		return result;
	}
	
	/**
	 * deletes existing project user
	 * 
	 * @param projectId	project id that has to be deleted
	 * 
	 * @return	true if the delete was successful
	 */
	public boolean deleteProjectUser(int projectId){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return false;
		}
		
		boolean result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(ProjectUserEntity.DELETE_FOR_PROJECT);
			insertStatement.setInt(1, projectId);
			int rows = insertStatement.executeUpdate();
			
			if(rows > 0){
				result = true;
			}
			else{
				result = false;
			}
			
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}

	/**
	 * deletes an existing project
	 * 
	 * @param projectId	project id that has to be deleted
	 * 
	 * @return	true if the delete was successful
	 */
	public boolean deleteProject(int projectId){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return false;
		}
		
		boolean result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(ProjectEntity.DELETE_FOR_ID);
			insertStatement.setInt(1, projectId);
			int rows = insertStatement.executeUpdate();
			
			if(rows > 0){
				result = true;
			}
			else{
				result = false;
			}
			
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	/**
	 * updates a given project and sets the project description
	 * 
	 * @param project 	project with updated values
	 * 
	 * @return		returns true if the update was successful, false otherwise
	 */
	public boolean updateProjectDescription(Project project){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return false;
		}
		
		boolean result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(ProjectEntity.UPDATE_PROJECT_DESCRIPTION);
			insertStatement.setString(1, project.getmProjectDescription());
			insertStatement.setInt(2, project.getmId());
			int rows = insertStatement.executeUpdate();
			
			if(rows > 0){
				result = true;
			}
			else{
				result = false;
			}
			
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	/**
	 * loads all available users for a project
	 * 
	 * @return	ArrayList<User> 	returns a list of users
	 */
	public ArrayList<User> selectAllUsersForProject(Project selected){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		ArrayList<User> result = new ArrayList<>();
		
		try{
			PreparedStatement selectStatement = mDbCon.prepareStatement(UserEntity.SELECT_USER_FOR_PROJECT);
			selectStatement.setInt(1, selected.getmId());
			ResultSet resultSet = selectStatement.executeQuery();
			if(resultSet.isBeforeFirst() && !resultSet.isAfterLast()){
				while (resultSet.next()) {
					int groupId = resultSet.getInt(UserGroupEntity.ID);
					String groupName = resultSet.getString(UserGroupEntity.NAME);
					boolean flagChangeProject = resultSet.getBoolean(UserGroupEntity.CHANGE_PROJECT_ATHORIZATION);
					boolean userAuth = resultSet.getBoolean(UserGroupEntity.CHANGE_USER_ATHORIZATION);
					boolean fileAuth = resultSet.getBoolean(UserGroupEntity.CHANGE_USER_ATHORIZATION);
					
					UserGroup group = new UserGroup(groupId, groupName, flagChangeProject, userAuth, fileAuth);
					
					int userId = resultSet.getInt(UserEntity.ID);
					String userName = resultSet.getString(UserEntity.USER_NAME);
					String password = resultSet.getString(UserEntity.PASSWORD);
					
					User user = new User(userId, userName, password);
					user.setUserGroup(group);
					result.add(user);
				}
			}
			else{
				result = new ArrayList<>();
			}
			
			resultSet.close();
			selectStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = new ArrayList<>();
		}
		
		return result;
	}
	
	/**
	 * inserts a new to the database
	 * 
	 * @param selectedProject	project where the file belongs to
	 * @param fileData			file data
	 * 
	 * @return	true if the insert was successful
	 */
	public boolean insertNewFile(Project selectedProject, ProjectFile fileData){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return false;
		}
		
		boolean result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(FileEntity.CREATE_FILE, Statement.RETURN_GENERATED_KEYS);
			insertStatement.setInt(1, selectedProject.getmId());
			insertStatement.setString(2, fileData.getmFilename());
			insertStatement.setBinaryStream(3, fileData.getmFileStream());
			int rows = insertStatement.executeUpdate();
			if(rows > 0){
				result = true;
			}
			else{
				result = false;
			}
			
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	/**
	 * loads all available files for a project
	 * 
	 * @return	ArrayList<ProjectFile> 	returns a list of files
	 */
	public ArrayList<ProjectFile> selectAllFilesForProject(Project selected){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		ArrayList<ProjectFile> result = new ArrayList<>();
		
		try{
			PreparedStatement selectStatement = mDbCon.prepareStatement(FileEntity.SELECT_FILES_FOR_PROJECT);
			selectStatement.setInt(1, selected.getmId());
			ResultSet resultSet = selectStatement.executeQuery();
			if(resultSet.isBeforeFirst() && !resultSet.isAfterLast()){
				while (resultSet.next()) {
					int fileId = resultSet.getInt(FileEntity.ID);
					String fileName = resultSet.getString(FileEntity.FILE_NAME);
					Blob file = resultSet.getBlob(FileEntity.FILE);
					InputStream filestream = file.getBinaryStream();
					String contentType = resultSet.getString(FileEntity.CONTENT_TYPE);
					
					ProjectFile projectFile = new ProjectFile(fileId, fileName, filestream, contentType);
					
					result.add(projectFile);
				}
			}
			else{
				result = new ArrayList<>();
			}
			
			resultSet.close();
			selectStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = new ArrayList<>();
		}
		
		return result;
	}

	/**
	 * deletes an existing project file
	 * 
	 * @param projectId	project id that has to be deleted
	 * @param filename  filename of the file that has to be deleted
	 * 
	 * @return	true if the delete was successful
	 */
	public boolean deleteProjectFile(int projectId, int fileId){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return false;
		}
		
		boolean result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(FileEntity.DELETE_PROJECT_FILE_FOR_FILENAME);
			insertStatement.setInt(1, projectId);
			insertStatement.setInt(2, fileId);
			int rows = insertStatement.executeUpdate();
			
			if(rows > 0){
				result = true;
			}
			else{
				result = false;
			}
			
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	
	/**
	 * loads the user group for a specific user in a specific project
	 * 
	 * @param projectId		selected project
	 * @param userId		user id
	 * 
	 * @return		user group of the user in the project
	 */
	public UserGroup getUserGroupForProjectUser(Project projectId, int userId){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		UserGroup result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(ProjectUserEntity.SELECT_USER_GROUP_FOR_PROJECT_USER);
			insertStatement.setInt(1, projectId.getmId());
			insertStatement.setInt(2, userId);
			ResultSet resultSet = insertStatement.executeQuery();
			if(resultSet.isBeforeFirst() && !resultSet.isAfterLast() && resultSet.next()){
				int id = resultSet.getInt(UserGroupEntity.ID);
				String user = resultSet.getString(UserGroupEntity.NAME);
				boolean changeProject = resultSet.getBoolean(UserGroupEntity.CHANGE_PROJECT_ATHORIZATION);
				boolean changeUser = resultSet.getBoolean(UserGroupEntity.CHANGE_USER_ATHORIZATION);
				boolean changeFile = resultSet.getBoolean(UserGroupEntity.CHANGE_FILE_ATHORIZATION);
				
				result = new UserGroup(id, user, changeProject, changeUser, changeFile);
			}
			else{
				result = null;
			}
			
			resultSet.close();
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = null;
		}
		
		return result;
	}

	/**
	 * deletes an existing project user
	 * 
	 * @param project	project where the user has to be removed from
	 * @param userId  	id of the user which has to be removed
	 * 
	 * @return	true if the delete was successful
	 */
	public boolean deleteProjectUser(Project project, int userId){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return false;
		}
		
		boolean result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(ProjectUserEntity.DELETE_FOR_PROJECT_USER);
			insertStatement.setInt(1, project.getmId());
			insertStatement.setInt(2, userId);
			int rows = insertStatement.executeUpdate();
			
			if(rows > 0){
				result = true;
			}
			else{
				result = false;
			}
			
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	/**
	 * loads all available users for that are not in a project
	 * 
	 * @return	ArrayList<User> 	returns a list of users
	 */
	public ArrayList<User> selectAllUsersOutSideOfProject(Project selected){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		ArrayList<User> result = new ArrayList<>();
		
		try{
			PreparedStatement selectStatement = mDbCon.prepareStatement(UserEntity.SELECT_PROJECT_OUTSIDE_USER);
			selectStatement.setInt(1, selected.getmId());
			ResultSet resultSet = selectStatement.executeQuery();
			if(resultSet.isBeforeFirst() && !resultSet.isAfterLast()){
				while (resultSet.next()) {
					int userId = resultSet.getInt(UserEntity.ID);
					String userName = resultSet.getString(UserEntity.USER_NAME);
					String password = resultSet.getString(UserEntity.PASSWORD);
					
					User user = new User(userId, userName, password);
					result.add(user);
				}
			}
			else{
				result = new ArrayList<>();
			}
			
			resultSet.close();
			selectStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = new ArrayList<>();
		}
		
		return result;
	}
	
	/**
	 * loads all available user groups without creator
	 * 
	 * @return	Map<Integer, UserGroup> 	returns a map with the group id as key and the user group as value
	 */
	public ArrayList<UserGroup> selectAllGroupsWithoutCreator(){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		ArrayList<UserGroup> result = new ArrayList<>();
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(UserGroupEntity.SELECT_ALL_WITHOUT_CREATOR);
			insertStatement.setString(1, "Ersteller");
			ResultSet resultSet = insertStatement.executeQuery();
			if(resultSet.isBeforeFirst() && !resultSet.isAfterLast()){
				while (resultSet.next()) {
					int id = resultSet.getInt(UserGroupEntity.ID);
					String name = resultSet.getString(UserGroupEntity.NAME);
					boolean flagChangeProject = resultSet.getBoolean(UserGroupEntity.CHANGE_PROJECT_ATHORIZATION);
					boolean flagChangeUser = resultSet.getBoolean(UserGroupEntity.CHANGE_USER_ATHORIZATION);
					boolean flagChangeFile = resultSet.getBoolean(UserGroupEntity.CHANGE_FILE_ATHORIZATION);
					
					result.add(new UserGroup(id, name, flagChangeProject, flagChangeUser, flagChangeFile));
				}
			}
			else{
				result = new ArrayList<>();
			}
			
			resultSet.close();
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = new ArrayList<>();
		}
		
		return result;
	}
	
	/**
	 * returns the user group for a given id
	 * 
	 * @param groupID 	group id which should be searched after
	 * 
	 * @return		returns the user group with the given name or null if the group does not exist
	 */
	public UserGroup getUserGroupForId(int groupId){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		UserGroup result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(UserGroupEntity.SELECT_FOR_ID);
			insertStatement.setInt(1, groupId);
			ResultSet resultSet = insertStatement.executeQuery();
			if(resultSet.isBeforeFirst() && !resultSet.isAfterLast() && resultSet.next()){
				int id = resultSet.getInt(UserGroupEntity.ID);
				String user = resultSet.getString(UserGroupEntity.NAME);
				boolean changeProject = resultSet.getBoolean(UserGroupEntity.CHANGE_PROJECT_ATHORIZATION);
				boolean changeUser = resultSet.getBoolean(UserGroupEntity.CHANGE_USER_ATHORIZATION);
				boolean changeFile = resultSet.getBoolean(UserGroupEntity.CHANGE_FILE_ATHORIZATION);
				
				result = new UserGroup(id, user, changeProject, changeUser, changeFile);
			}
			else{
				result = null;
			}
			
			resultSet.close();
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = null;
		}
		
		return result;
	}
	
	/**
	 * adds a new user and a new user group to a project
	 * 
	 * @param project		selected project
	 * @param user			selected user
	 * @param userGroup		selected user group
	 * 
	 * @return	true if the insert was successful, false otherwise
	 */
	public boolean insertProjectUser(Project project, User user, UserGroup userGroup){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return false;
		}
		
		boolean result;
		
		try{
			PreparedStatement insertProjectUserStatement = mDbCon.prepareStatement(ProjectUserEntity.FULL_INSERT);
			insertProjectUserStatement.setInt(1, project.getmId());
			insertProjectUserStatement.setInt(2, user.getUserId());
			insertProjectUserStatement.setInt(3, userGroup.getId());
			int affectedRows = insertProjectUserStatement.executeUpdate();
			if(affectedRows > 0){
				result = true;
			}
			else{
				result = false;
			}
			
			insertProjectUserStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}

	/**
	 * returns the project file for id
	 * 
	 * @param fileID 	file id which should be searched after
	 * 
	 * @return		returns the file with the given id or null if the file does not exist
	 */
	public ProjectFile selectFileForId(int fileId){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		ProjectFile result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(FileEntity.SELECT_FOR_ID);
			insertStatement.setInt(1, fileId);
			ResultSet resultSet = insertStatement.executeQuery();
			if(resultSet.isBeforeFirst() && !resultSet.isAfterLast() && resultSet.next()){
				int id = resultSet.getInt(FileEntity.ID);
				String filename = resultSet.getString(FileEntity.FILE_NAME);
				Blob file = resultSet.getBlob(FileEntity.FILE);
				String contentType = resultSet.getString(FileEntity.CONTENT_TYPE);
				
				result = new ProjectFile(id, filename, file.getBinaryStream(), contentType);
			}
			else{
				result = null;
			}
			
			resultSet.close();
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = null;
		}
		
		return result;
	}

	/**
	 * updates a given project file and sets the meta file
	 * 
	 * @param projectFile 	project file with updated values
	 * 
	 * @return		returns true if the update was successful, false otherwise
	 */
	public boolean updateProjectFile(ProjectFile projectFile){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return false;
		}
		
		boolean result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(FileEntity.UPDATE_META_FILE);
			insertStatement.setBlob(1, projectFile.getmMetaFileStream());
			insertStatement.setString(2, projectFile.getmMetaFilename());
			insertStatement.setString(3, projectFile.getmMetaContentType());
			insertStatement.setInt(4, projectFile.getmId());
			int rows = insertStatement.executeUpdate();
			
			if(rows > 0){
				result = true;
			}
			else{
				result = false;
			}
			
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	/**
	 * updates a given project file description
	 * 
	 * @param fileDescription 	new file description
	 * 
	 * @return		returns true if the update was successful, false otherwise
	 */
	public boolean updateProjectFileDescription(ProjectFile projectFile){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return false;
		}
		
		boolean result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(FileEntity.UPDATE_DESCRIPTION);
			insertStatement.setString(1, projectFile.getFileDescription());
			insertStatement.setInt(2, projectFile.getmId());
			int rows = insertStatement.executeUpdate();
			
			if(rows > 0){
				result = true;
			}
			else{
				result = false;
			}
			
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	/**
	 * adds a descriptor to file
	 * 
	 * @param projectFile		selected project
	 * @param descriptor		selected user
	 * 
	 * @return	true if the insert was successful, false otherwise
	 */
	public boolean insertProjectFileDescriptor(ProjectFile projectFile, String descriptor){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return false;
		}
		
		boolean result;
		
		try{
			PreparedStatement insertProjectUserStatement = mDbCon.prepareStatement(FileDescriptorEntity.CREATE_FILE);
			insertProjectUserStatement.setInt(1, projectFile.getmId());
			insertProjectUserStatement.setString(2, descriptor);
			int affectedRows = insertProjectUserStatement.executeUpdate();
			if(affectedRows > 0){
				result = true;
			}
			else{
				result = false;
			}
			
			insertProjectUserStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}

	/**
	 * loads all available descriptors for the selected project file
	 * 
	 * @param 	projectFile				selected project file
	 * 
	 * @return	ArrayList<FileDescriptor> 	returns a list of file descriptors
	 */
	public ArrayList<FileDescriptor> selectAllFileDescriptors(ProjectFile projectFile){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		ArrayList<FileDescriptor> result = new ArrayList<>();
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(FileDescriptorEntity.SELECT_DESCRIPTORS_FOR_FILE);
			insertStatement.setInt(1, projectFile.getmId());
			ResultSet resultSet = insertStatement.executeQuery();
			if(resultSet.isBeforeFirst() && !resultSet.isAfterLast()){
				while (resultSet.next()) {
					int id = resultSet.getInt(FileDescriptorEntity.ID);
					String descriptor = resultSet.getString(FileDescriptorEntity.DESCRIPTOR);
					
					result.add(new FileDescriptor(id, descriptor));
				}
			}
			else{
				result = new ArrayList<>();
			}
			
			resultSet.close();
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = new ArrayList<>();
		}
		
		return result;
	}

	/**
	 * loads a file descriptor for an id
	 * 
	 * @param 	descriptorId				id of the descriptor
	 * 
	 * @return	FileDescriptor 				returns a the file descriptor object
	 */
	public FileDescriptor selectFileDescriptor(int descriptorId){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		FileDescriptor result = null;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(FileDescriptorEntity.SELECT_DESCRIPTORS_FOR_ID);
			insertStatement.setInt(1, descriptorId);
			ResultSet resultSet = insertStatement.executeQuery();
			if(resultSet.isBeforeFirst() && !resultSet.isAfterLast() && resultSet.next()){
				int id = resultSet.getInt(FileDescriptorEntity.ID);
				String descriptor = resultSet.getString(FileDescriptorEntity.DESCRIPTOR);
				
				result = new FileDescriptor(id, descriptor);
			}
			else{
				result = null;
			}
			
			resultSet.close();
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = null;
		}
		
		return result;
	}

	/**
	 * deletes an existing descriptor
	 * 
	 * @param projectId	project id that has to be deleted
	 * 
	 * @return	true if the delete was successful
	 */
	public boolean deleteProjectFileDescriptor(FileDescriptor descriptor){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return false;
		}
		
		boolean result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(FileDescriptorEntity.DELETE_FOR_ID);
			insertStatement.setInt(1, descriptor.getmId());
			int rows = insertStatement.executeUpdate();
			
			if(rows > 0){
				result = true;
			}
			else{
				result = false;
			}
			
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}

	/**
	 * select meta file name for file
	 * 
	 * @param projectFile	project file
	 * 
	 * @return				returns the file name of the meta file
	 */
	public String selectMetaFilenameForFile(ProjectFile projectFile){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		String result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(FileEntity.SELECT_METAFILE_NAME_FOR_ID);
			insertStatement.setInt(1, projectFile.getmId());
			ResultSet resultSet = insertStatement.executeQuery();
			
			if(resultSet != null && resultSet.isBeforeFirst() && !resultSet.isAfterLast() && resultSet.next()){
				result = resultSet.getString(FileEntity.META_FILE_NAME);
			}
			else{
				result = null;
			}
			
			resultSet.close();
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = null;
		}
		
		return result;
	}

	/**
	 * select meta file for file
	 * 
	 * @param projectFile	project file
	 * 
	 * @return				returns the project file object
	 */
	public ProjectFile selectMetaFileForFile(ProjectFile projectFile){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		ProjectFile result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(FileEntity.SELECT_METAFILE_FOR_ID);
			insertStatement.setInt(1, projectFile.getmId());
			ResultSet resultSet = insertStatement.executeQuery();
			
			if(resultSet != null && resultSet.isBeforeFirst() && !resultSet.isAfterLast() && resultSet.next()){
				Blob blob = resultSet.getBlob(FileEntity.META_FILE);
				String filename = resultSet.getString(FileEntity.META_FILE_NAME);
				String contentType = resultSet.getString(FileEntity.META_CONTENT_TYPE);

				if(blob != null){
					InputStream stream = blob.getBinaryStream();
					result = new ProjectFile(filename, stream, contentType);
				}
				else{
					result = null;
				}
			}
			else{
				result = null;
			}
			
			resultSet.close();
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = null;
		}
		
		return result;
	}

	/**
	 * select file description
	 * 
	 * @param projectFile	project file
	 * 
	 * @return				returns the file name of the meta file
	 */
	public String selectDescriptionForFile(ProjectFile projectFile){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		String result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(FileEntity.SELECT_DESCRIPTION_FOR_ID);
			insertStatement.setInt(1, projectFile.getmId());
			ResultSet resultSet = insertStatement.executeQuery();
			
			if(resultSet != null && resultSet.isBeforeFirst() && !resultSet.isAfterLast() && resultSet.next()){
				result = resultSet.getString(FileEntity.FILE_DESCRIPTION);
			}
			else{
				result = null;
			}
			
			resultSet.close();
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = null;
		}
		
		return result;
	}

	/**
	 * searches for files
	 * 
	 * @param projectName		project name
	 * @param username			user name
	 * @param fileDescriptor	file descriptor
	 * 
	 * @return 	list of search results
	 */
	public ArrayList<SearchResult> search(String projectName, String username, String fileDescriptor){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		String searchProjectName = "%" + projectName + "%";
		String searchUsername = "%" + username + "%";
		String searchFileDescriptor = "%" + fileDescriptor + "%";
		
		ArrayList<SearchResult> result = new ArrayList<>();
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(ProjectEntity.SEARCH);
			insertStatement.setString(1, searchProjectName);
			insertStatement.setString(2, searchUsername);
			insertStatement.setString(3, searchFileDescriptor);
			ResultSet resultSet = insertStatement.executeQuery();
			
			if(resultSet != null && resultSet.isBeforeFirst() && !resultSet.isAfterLast()){
				while(resultSet.next()){
					int projectId = resultSet.getInt(ProjectEntity.TABLE + "." + ProjectEntity.ID);
					String project = resultSet.getString(ProjectEntity.TABLE + "." + ProjectEntity.NAME);
					int fileId = resultSet.getInt(FileEntity.TABLE + "." + FileEntity.ID);
					String filename = resultSet.getString(FileEntity.TABLE + "." + FileEntity.FILE_NAME);
					
					result.add(new SearchResult(projectId, project, fileId, filename));
				}
			}
			else{
				result = null;
			}
			
			resultSet.close();
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = null;
		}
		
		return result;
	}

	/**
	 * returns the project file for file id
	 * 
	 * @param fileID 	file id which should be searched after
	 * 
	 * @return		returns the file with the given id or null if the file does not exist
	 */
	public Project selectProjectForId(int fileId){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		Project result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(FileEntity.SELECT_PROJECT_FOR_ID);
			insertStatement.setInt(1, fileId);
			ResultSet resultSet = insertStatement.executeQuery();
			if(resultSet.isBeforeFirst() && !resultSet.isAfterLast() && resultSet.next()){
				int id = resultSet.getInt(ProjectEntity.ID);
				String name = resultSet.getString(ProjectEntity.NAME);
				String desc = resultSet.getString(ProjectEntity.DESCRIPTION);
				
				result = new Project(id, name, desc);
			}
			else{
				result = null;
			}
			
			resultSet.close();
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = null;
		}
		
		return result;
	}


	/**
	 * returns all user groups
	 * 
	 * @return		returns all user groups
	 */
	public ArrayList<UserGroup> selectAllUserGroups(){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return null;
		}
		
		ArrayList<UserGroup> result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(UserGroupEntity.SELECT_ALL);
			ResultSet resultSet = insertStatement.executeQuery();
			if(resultSet.isBeforeFirst() && !resultSet.isAfterLast()){
				result = new ArrayList<>();
				while(resultSet.next()){
					int groupId = resultSet.getInt(UserGroupEntity.ID);
					String groupName = resultSet.getString(UserGroupEntity.NAME);
					boolean userProject = resultSet.getBoolean(UserGroupEntity.CHANGE_PROJECT_ATHORIZATION);
					boolean userRight = resultSet.getBoolean(UserGroupEntity.CHANGE_USER_ATHORIZATION);
					boolean fileRight = resultSet.getBoolean(UserGroupEntity.CHANGE_FILE_ATHORIZATION);
					
					UserGroup userGroup = new UserGroup(groupId, groupName, userProject, userRight, fileRight);
					if(userGroup != null){
						result.add(userGroup);
					}
				}
			}
			else{
				result = null;
			}
			
			resultSet.close();
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = null;
		}
		
		return result;
	}

	/**
	 * deletes an existing descriptor
	 * 
	 * @param id	user group id
	 * 
	 * @return	true if the delete was successful
	 */
	public boolean deleteUserGroupForId(int id){
		if(mDbCon == null){
			System.err.println("Connection could not established");
			return false;
		}
		
		boolean result;
		
		try{
			PreparedStatement insertStatement = mDbCon.prepareStatement(UserGroupEntity.DELETE_FOR_ID);
			insertStatement.setInt(1, id);
			int rows = insertStatement.executeUpdate();
			
			if(rows > 0){
				result = true;
			}
			else{
				result = false;
			}
			
			insertStatement.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}

}

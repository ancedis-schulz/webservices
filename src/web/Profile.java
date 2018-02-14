package web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


import database.DatabaseAccessObject;
import objects.Project;
import objects.User;
import objects.UserGroup;

/**
 * Class to control to profile html page
 * 
 * @author Stephan Schulz
 *
 */
@SessionScoped
@ManagedBean(name="profile")
public class Profile {

	/**
	 * database object
	 */
	private DatabaseAccessObject mDatabaseObject;

	/**
	 * Inputed user name
	 */
	private String username;

	/**
	 * password field to change password
	 */
	private String password;

	/**
	 * repeat password field to change password
	 */
	private String rptPassword;

	/**
	 * user name for new user
	 */
	private String newUsername;

	/**
	 * password for new user
	 */
	private String newPassword;

	/**
	 * Session values
	 */
	private Map<String, Object> mSessionValues;

	/**
	 * user project list
	 */
	private List<Project> listOfProjects;

	/**
	 * user list
	 */
	private Map<String, Integer> userValues;

	/**
	 * selected user id
	 */
	private Integer selectedUserId;

	/**
	 * password change result string
	 */
	private String passwordChangeResult;
	
	/**
	 * add User result string
	 */
	private String addUserResult;
	
	/**
	 * delete User result string
	 */
	private String deleteUserResult;
	
	/**
	 * message bundle
	 */
	private ResourceBundle mMessageBundle;
	
	/**
	 * map to list all user groups
	 */
	private Map<String, Integer> userGroups;
	
	/**
	 * selected user groups
	 */
	private Integer selectedUserGroup;
	
	/**
	 * result for deleting user group
	 */
	private String deleteUserGroupResult;
	
	/**
	 * new user group name
	 */
	private String newUserGroupName;
	
	/**
	 * new project right
	 */
	private boolean newProjectRight;
		
	/**
	 * new user right
	 */
	private boolean newUserRight;
	
	/**
	 * new file right
	 */
	private boolean newFileRight;
	
	/**
	 * result for save user group
	 */
	private String saveUserGroupResult;

	/**
	 * default constructor
	 */
	public Profile() {
		try{
			mDatabaseObject = DatabaseAccessObject.getInstance();
		}
		catch(SQLException e){
			e.printStackTrace();
			mDatabaseObject = null;
		}
		
		FacesContext context = FacesContext.getCurrentInstance();
		mSessionValues = context.getExternalContext().getSessionMap();
		mMessageBundle = context.getApplication().getResourceBundle(context, "msg");
		passwordChangeResult = "";
		addUserResult = "";
		deleteUserResult = "";
	}

	/**
	 * initialize attributes on page load
	 */
	@PostConstruct
	public void init(){
		User logged = (User)mSessionValues.get(Constants.SESSION_USER);
		username = logged.getUsername();
		
		userValues = mDatabaseObject.selectAllUsers();
		selectedUserId = logged.getUserId();
		
		listOfProjects = mDatabaseObject.selectAllProjects(logged);
		ArrayList<UserGroup> allUserGroups = mDatabaseObject.selectAllUserGroups();
		if(allUserGroups != null){
			userGroups = new HashMap<>();
			
			for(int i = 0; i < allUserGroups.size(); i++){
				UserGroup group = allUserGroups.get(i);
				userGroups.put(group.getName(), group.getId());
			}
		}
	}
	
	/**
	 * returns the inputed user name
	 * 
	 * @return	user name that was inputed
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * sets a new user name
	 * 
	 * @param username	new user name
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * returns password field value to change users password
	 * 
	 * @return	returns the inputed password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * sets the new password
	 * 
	 * @param password		new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * returns the user name for the new user
	 * 
	 * @return	user name for a new user
	 */
	public String getNewUsername() {
		return newUsername;
	}

	/**
	 * sets a user name for a new user
	 * 
	 * @param username	user name for a new user
	 */
	public void setNewUsername(String username) {
		this.newUsername = username;
	}

	/**
	 * returns password for a new user
	 * 
	 * @return	returns the password for a new user
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * sets the password for a new user
	 * 
	 * @param password		password for a new user
	 */
	public void setNewPassword(String password) {
		this.newPassword = password;
	}


	/**
	 * returns repeated password field value to change users password
	 * 
	 * @return	returns the inputed password
	 */
	public String getRptPassword() {
		return rptPassword;
	}

	/**
	 * sets the new repeated password
	 * 
	 * @param password		new password
	 */
	public void setRptPassword(String rptPassword) {
		this.rptPassword = rptPassword;
	}
	
	/**
	 * returns the actual user list
	 * 
	 * @return	map with user name as key and user id as value
	 */
	public Map<String, Integer> getUserList() {
		return userValues;
	}

	/**
	 * sets the new user list
	 * 
	 * @param userList	new user map
	 */
	public void setUserList(Map<String, Integer> userList) {
		this.userValues = userList;
	}

	/**
	 * returns the actual selected user item
	 * 
	 * @return	Integer 	user id
	 */
	public Integer getSelectedUserId() {
		return selectedUserId;
	}

	/**
	 * sets the new selected user id
	 * 
	 * @param userId	new user id
	 */
	public void setSelectedUserId(Integer userId) {
		this.selectedUserId = userId;
	}

	/**
	 * returns the password change result
	 * 
	 * @return	password change result string
	 */
	public String getPasswordChangeResult() {
		return passwordChangeResult;
	}

	/**
	 * sets the new password change result
	 * 	
	 * @param passwordChangeResult	new result message
	 */
	public void setPasswordChangeResult(String passwordChangeResult) {
		this.passwordChangeResult = passwordChangeResult;
	}

	/**
	 * returns the add user result
	 * 
	 * @return	add user result string
	 */
	public String getAddUserResult() {
		return addUserResult;
	}

	/**
	 * sets the new add user result
	 * 	
	 * @param addUserResult	new result message
	 */
	public void setAddUserResult(String addUserResult) {
		this.addUserResult = addUserResult;
	}

	/**
	 * returns the delete user result
	 * 
	 * @return	delete user result string
	 */
	public String getDeleteUserResult() {
		return deleteUserResult;
	}

	/**
	 * sets the new delete user result
	 * 	
	 * @param deleteUserResult	new result message
	 */
	public void setDeleteUserResult(String deleteUserResult) {
		this.deleteUserResult = deleteUserResult;
	}

	/**
	 * @return the listOfProjects
	 */
	public List<Project> getListOfProjects() {
		return listOfProjects;
	}

	/**
	 * @param listOfProjects the listOfProjects to set
	 */
	public void setListOfProjects(List<Project> listOfProjects) {
		this.listOfProjects = listOfProjects;
	}

	/**
	 * @return the selectedUserGroup
	 */
	public Integer getSelectedUserGroup() {
		return selectedUserGroup;
	}

	/**
	 * @param selectedUserGroup the selectedUserGroup to set
	 */
	public void setSelectedUserGroup(Integer selectedUserGroup) {
		this.selectedUserGroup = selectedUserGroup;
	}

	/**
	 * @return the userGroups
	 */
	public Map<String, Integer> getUserGroups() {
		return userGroups;
	}

	/**
	 * @return the deleteUserGroupResult
	 */
	public String getDeleteUserGroupResult() {
		return deleteUserGroupResult;
	}

	/**
	 * @return the newUserGroupName
	 */
	public String getNewUserGroupName() {
		return newUserGroupName;
	}

	/**
	 * @param newUserGroupName the newUserGroupName to set
	 */
	public void setNewUserGroupName(String newUserGroupName) {
		this.newUserGroupName = newUserGroupName;
	}

	/**
	 * @return the newUserRight
	 */
	public boolean isNewUserRight() {
		return newUserRight;
	}

	/**
	 * @param newUserRight the newUserRight to set
	 */
	public void setNewUserRight(boolean newUserRight) {
		this.newUserRight = newUserRight;
	}

	/**
	 * @return the newFileRight
	 */
	public boolean isNewFileRight() {
		return newFileRight;
	}

	/**
	 * @param newFileRight the newFileRight to set
	 */
	public void setNewFileRight(boolean newFileRight) {
		this.newFileRight = newFileRight;
	}

	/**
	 * @return the saveUserGroupResult
	 */
	public String getSaveUserGroupResult() {
		return saveUserGroupResult;
	}

	/**
	 * @return the newProjectRight
	 */
	public boolean isNewProjectRight() {
		return newProjectRight;
	}

	/**
	 * @param newProjectRight the newProjectRight to set
	 */
	public void setNewProjectRight(boolean newProjectRight) {
		this.newProjectRight = newProjectRight;
	}

	/**
	 * this method changes the actual user password
	 */
	public void changePassword(){
		passwordChangeResult = "";
		addUserResult = "";
		deleteUserResult = "";
		addUserResult = "";
		deleteUserGroupResult = "";

		
		if(password.length() >= Constants.MIN_PASSWORD_LENGTH && password.compareTo(rptPassword) == 0){
			User actualUser = (User)mSessionValues.get(Constants.SESSION_USER);
			actualUser.setPassword(password);
			boolean updateResult = mDatabaseObject.updateUser(actualUser);
			
			if(updateResult){
				passwordChangeResult = mMessageBundle.getString("passwordChangeSuccessful");
			}
			else{
				passwordChangeResult = mMessageBundle.getString("passwordChangeFailed");
			}
		}
		else if(password.compareTo(rptPassword) != 0){
			passwordChangeResult = mMessageBundle.getString("passwordsNotMatching");
			return;
		}
		else{
			passwordChangeResult = mMessageBundle.getString("passwordLengthError");
			return;
		}
	}
	
	/**
	 * this method adds a new user
	 */
	public void addUser(){
		passwordChangeResult = "";
		addUserResult = "";
		deleteUserResult = "";
		addUserResult = "";
		deleteUserGroupResult = "";


		if(newUsername == null || newUsername.isEmpty()){
			addUserResult = mMessageBundle.getString("usernameEmpty");
			return;
		}
		
		if(newPassword.length() >= Constants.MIN_PASSWORD_LENGTH){
			User newUser = new User(newUsername, newPassword);
			User createdUser = mDatabaseObject.createUser(newUser);
			if(createdUser != null){
				addUserResult = mMessageBundle.getString("userAddedSuccessful");
			}
			else{
				addUserResult = mMessageBundle.getString("userAddedFailed");
			}
			
		}
		else{
			addUserResult = mMessageBundle.getString("passwordLengthError");
			return;
		}
	}

	/**
	 * this method deletes a user
	 */
	public void deleteUser(){
		passwordChangeResult = "";
		addUserResult = "";
		deleteUserResult = "";
		addUserResult = "";
		deleteUserGroupResult = "";

		User actualUser = (User)mSessionValues.get(Constants.SESSION_USER);
		if(selectedUserId.intValue() == actualUser.getUserId()){
			deleteUserResult = mMessageBundle.getString("userCantBeDeleted");
			return;
		}
		else{
			boolean result = mDatabaseObject.deleteUser(selectedUserId);
			
			if(result){
				deleteUserResult = mMessageBundle.getString("userDeletionSuccessful");
			}
			else{
				deleteUserResult = mMessageBundle.getString("userDeletionFailed");
			}
		}
	}

	/**
	 * opens a selected project
	 * 
	 * @param selected	selected project
	 */
	public String openProject(Project selected){
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put(Constants.SESSION_SELECTED_PROJECT, selected);
		
		return Constants.SINGLE_PROJECT_PAGE;
	}

	/**
	 * this method deletes a user group
	 */
	public void deleteUserGroup(){
		passwordChangeResult = "";
		addUserResult = "";
		deleteUserResult = "";
		addUserResult = "";
		deleteUserGroupResult = "";

		if(selectedUserGroup == null){
			return;
		}
		
		UserGroup selected = mDatabaseObject.getUserGroupForId(selectedUserGroup);
		
		if(selected.getName().compareTo("Ersteller") != 0){
			boolean result = mDatabaseObject.deleteUserGroupForId(selectedUserGroup);
			
			if(result){
				deleteUserGroupResult = mMessageBundle.getString("userGroupDeleted");
			}
			else{
				deleteUserGroupResult = mMessageBundle.getString("userGroupNotDeleted");
			}
		}
		else{
			deleteUserGroupResult = mMessageBundle.getString("userGroupCreator");
		}
	}

	/**
	 * this method saves a new user group
	 */
	public void saveUserGroup(){
		passwordChangeResult = "";
		addUserResult = "";
		deleteUserResult = "";
		addUserResult = "";
		deleteUserGroupResult = "";

		if(newUserGroupName == null){
			return;
		}
		
		UserGroup newGroup = new UserGroup(newUserGroupName, newProjectRight, newUserRight, newFileRight);
		
		UserGroup result = mDatabaseObject.createUserGroup(newGroup);
		
		if(result != null){
			saveUserGroupResult = mMessageBundle.getString("userGroupSaved");
		}
		else{
			saveUserGroupResult = mMessageBundle.getString("userGroupNotSaved");
		}
	}
}
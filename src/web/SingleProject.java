package web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import database.DatabaseAccessObject;
import objects.Project;
import objects.ProjectFile;
import objects.User;
import objects.UserGroup;

/**
 * This class represents the project administration
 * 
 * @author Stephan Schulz
 *
 */
@SessionScoped
@ManagedBean(name="singleProject")
public class SingleProject {

	/**
	 * user name of the logged user
	 */
	private String username;

	/**
	 * database object
	 */
	private DatabaseAccessObject mDatabaseObject;

	/**
	 * message bundle
	 */
	private ResourceBundle mMessageBundle;

	/**
	 * Session values
	 */
	private Map<String, Object> mSessionValues;
	
	/**
	 * user group of project user
	 */
	private UserGroup mProjectUserGroup;
	
	/**
	 * user project list
	 */
	private List<Project> listOfProjects;

	/**
	 * selected project
	 */
	private Project mSelectedProject;

	/**
	 * name of the selected project
	 */
	private String projectName;

	/**
	 * description of the selected project
	 */
	private String projectDescription;

	/**
	 * update project description result
	 */
	private String projectDescriptionResult;
	
	/**
	 * user list for a project
	 */
	private Map<String, Integer> userList;
	
	/**
	 * new file to upload
	 */
	private Part newFile;
	
	/**
	 * add file result
	 */
	private String addFileResult;
	
	/**
	 * project file list
	 */
	private Map<String, Integer> fileList;

	/**
	 * selected File
	 */
	private Integer selectedFile;

	/**
	 * result of removing file
	 */
	private String removeFileResult;

	/**
	 * selected user
	 */
	private Integer selectedUser;

	/**
	 * result of removing user
	 */
	private String removeUserResult;
	
	/**
	 * list of users that are not part of the project
	 */
	private Map<String, Integer> availableUsers;
	
	/**
	 * list of all user groups except the creator group
	 */
	private Map<String, Integer> availableGroups;

	/**
	 * selected new user
	 */
	private Integer selectedAvailableUser;

	/**
	 * selected new user group
	 */
	private Integer selectedAvailableGroup;

	/**
	 * result of add user
	 */
	private String addUserResult;

	/**
	 * result of open file
	 */
	private String openFileResult;
	
	/**
	 * constructor
	 */
	public SingleProject(){
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
	}

	/**
	 * initialize attributes on page load
	 */
	@PostConstruct
	public void init(){
		User logged = (User)mSessionValues.get(Constants.SESSION_USER);
		username = logged.getUsername();
		
		listOfProjects = mDatabaseObject.selectAllProjects(logged);
		
		mSelectedProject = (Project)mSessionValues.get(Constants.SESSION_SELECTED_PROJECT);
		mProjectUserGroup = (UserGroup)mSessionValues.get(Constants.SESSION_PROJECT_USER_GROUP);
		projectName = mSelectedProject.getmProjectName();
		projectDescription = mSelectedProject.getmProjectDescription();
		
		ArrayList<User> projectUsers = mDatabaseObject.selectAllUsersForProject(mSelectedProject);
		userList = new HashMap<>();
		
		for(int i = 0; i < projectUsers.size(); i++){
			User user = projectUsers.get(i);
			userList.put(user.getUserLabel(), user.getUserId());
		}
		
		ArrayList<ProjectFile> projectFileList = mDatabaseObject.selectAllFilesForProject(mSelectedProject);
		fileList = new HashMap<>();
		for(int i = 0; i < projectFileList.size(); i++){
			ProjectFile projectFile = projectFileList.get(i);
			fileList.put(projectFile.getmFilename(), projectFile.getmId());
		}
		
		ArrayList<User> missingUsers = mDatabaseObject.selectAllUsersOutSideOfProject(mSelectedProject);
		availableUsers = new HashMap<>();
		
		for(int i = 0; i < missingUsers.size(); i++){
			User user = missingUsers.get(i);
			availableUsers.put(user.getUsername(), user.getUserId());
		}
		
		ArrayList<UserGroup> groups = mDatabaseObject.selectAllGroupsWithoutCreator();
		availableGroups = new HashMap<>();
		
		for(int i = 0; i < groups.size(); i++){
			UserGroup group = groups.get(i);
			availableGroups.put(group.getName(), group.getId());
		}
	}

	/**
	 * sets a new user name
	 * 
	 * @param username 	new user name
	 */
	public void setUsername(String username){
		this.username = username;
	}
	
	/**
	 * returns the achtual username
	 * 
	 * @return  	actual user name
	 */
	public String getUsername(){
		return this.username;
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
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return the projectDescription
	 */
	public String getProjectDescription() {
		return projectDescription;
	}

	/**
	 * @param projectDescription the projectDescription to set
	 */
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	/**
	 * @return the projectDescriptionResult
	 */
	public String getProjectDescriptionResult() {
		return projectDescriptionResult;
	}

	/**
	 * @param projectDescriptionResult the projectDescriptionResult to set
	 */
	public void setProjectDescriptionResult(String projectDescriptionResult) {
		this.projectDescriptionResult = projectDescriptionResult;
	}

	/**
	 * @return the userList
	 */
	public Map<String, Integer> getUserList() {
		return userList;
	}

	/**
	 * @param userList the userList to set
	 */
	public void setUserList(Map<String, Integer> userList) {
		this.userList = userList;
	}

	/**
	 * @return the newFile
	 */
	public Part getNewFile() {
		return newFile;
	}

	/**
	 * @param newFile the newFile to set
	 */
	public void setNewFile(Part newFile) {
		this.newFile = newFile;
	}

	/**
	 * @return the addFileResult
	 */
	public String getAddFileResult() {
		return addFileResult;
	}

	/**
	 * @param addFileResult the addFileResult to set
	 */
	public void setAddFileResult(String addFileResult) {
		this.addFileResult = addFileResult;
	}

	/**
	 * @return the fileList
	 */
	public Map<String, Integer> getFileList() {
		return fileList;
	}

	/**
	 * @param fileList the fileList to set
	 */
	public void setFileList(Map<String, Integer> fileList) {
		this.fileList = fileList;
	}

	/**
	 * @return the selectedFile
	 */
	public Integer getSelectedFile() {
		return selectedFile;
	}

	/**
	 * @param selectedFile the selectedFile to set
	 */
	public void setSelectedFile(Integer selectedFile) {
		this.selectedFile = selectedFile;
	}

	/**
	 * @return the removeFileResult
	 */
	public String getRemoveFileResult() {
		return removeFileResult;
	}

	/**
	 * @param removeFileResult the removeFileResult to set
	 */
	public void setRemoveFileResult(String removeFileResult) {
		this.removeFileResult = removeFileResult;
	}

	/**
	 * @return the selectedUser
	 */
	public Integer getSelectedUser() {
		return selectedUser;
	}

	/**
	 * @param selectedUser the selectedUser to set
	 */
	public void setSelectedUser(Integer selectedUser) {
		this.selectedUser = selectedUser;
	}

	/**
	 * @return the removeUserResult
	 */
	public String getRemoveUserResult() {
		return removeUserResult;
	}

	/**
	 * @param removeUserResult the removeUserResult to set
	 */
	public void setRemoveUserResult(String removeUserResult) {
		this.removeUserResult = removeUserResult;
	}

	/**
	 * @return the availableUsers
	 */
	public Map<String, Integer> getAvailableUsers() {
		return availableUsers;
	}

	/**
	 * @param availableUsers the availableUsers to set
	 */
	public void setAvailableUsers(Map<String, Integer> availableUsers) {
		this.availableUsers = availableUsers;
	}

	/**
	 * @return the availableGroups
	 */
	public Map<String, Integer> getAvailableGroups() {
		return availableGroups;
	}

	/**
	 * @param availableGroups the availableGroups to set
	 */
	public void setAvailableGroups(Map<String, Integer> availableGroups) {
		this.availableGroups = availableGroups;
	}

	/**
	 * @return the selectedAvailableUser
	 */
	public Integer getSelectedAvailableUser() {
		return selectedAvailableUser;
	}

	/**
	 * @param selectedAvailableUser the selectedAvailableUser to set
	 */
	public void setSelectedAvailableUser(Integer selectedAvailableUser) {
		this.selectedAvailableUser = selectedAvailableUser;
	}

	/**
	 * @return the selectedAvailableGroup
	 */
	public Integer getSelectedAvailableGroup() {
		return selectedAvailableGroup;
	}

	/**
	 * @param selectedAvailableGroup the selectedAvailableGroup to set
	 */
	public void setSelectedAvailableGroup(Integer selectedAvailableGroup) {
		this.selectedAvailableGroup = selectedAvailableGroup;
	}

	/**
	 * @return the addUserResult
	 */
	public String getAddUserResult() {
		return addUserResult;
	}

	/**
	 * @param addUserResult the addUserResult to set
	 */
	public void setAddUserResult(String addUserResult) {
		this.addUserResult = addUserResult;
	}

	/**
	 * @return the openFileResult
	 */
	public String getOpenFileResult() {
		return openFileResult;
	}

	/**
	 * @param openFileResult the openFileResult to set
	 */
	public void setOpenFileResult(String openFileResult) {
		this.openFileResult = openFileResult;
	}

	/**
	 * saves the project description
	 */
	public void saveDescription(){
		addFileResult = "";
		addUserResult = "";
		removeFileResult = "";
		removeUserResult = "";
		projectDescriptionResult = "";
		
		if(!mProjectUserGroup.isChangeProject()){
			projectDescriptionResult = mMessageBundle.getString("noRights");
			return;
		}
		
		if(projectDescription != null){
			mSelectedProject.setmProjectDescription(projectDescription);
		}
		else{
			mSelectedProject.setmProjectDescription("");
		}

		boolean result = mDatabaseObject.updateProjectDescription(mSelectedProject);
			
		if(result){
			projectDescriptionResult = mMessageBundle.getString("updateProjectDescriptionSuccessful");
		}
		else{
			projectDescriptionResult = mMessageBundle.getString("updateProjectDescriptionFailed");
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
	 * uploads a new file to the selected project
	 */
	public void uploadFile(){
		addFileResult = "";
		addUserResult = "";
		removeFileResult = "";
		removeUserResult = "";
		projectDescriptionResult = "";
		
		if(!mProjectUserGroup.isChangeFiles()){
			addFileResult = mMessageBundle.getString("noRights");
			return;
		}	
		
		try {
			InputStream inputStream = newFile.getInputStream();
			String filename = newFile.getSubmittedFileName();
			String contentFile = newFile.getContentType();
			boolean result = mDatabaseObject.insertNewFile(mSelectedProject, new ProjectFile(filename, inputStream, contentFile));

			if(result){
				addFileResult = mMessageBundle.getString("addFileSuccessful");
			}
			else{
				addFileResult = mMessageBundle.getString("addFileFailed");
			}
		} catch (IOException e) {
			addFileResult = mMessageBundle.getString("addFileFailed");
			e.printStackTrace();
		}catch (NullPointerException e) {
			addFileResult = mMessageBundle.getString("addFileNoFile");
			e.printStackTrace();
		}	
	}

	/**
	 * deletes an uploaded file
	 */
	public void deleteFile(){
		addFileResult = "";
		addUserResult = "";
		removeFileResult = "";
		removeUserResult = "";
		projectDescriptionResult = "";
		
		if(!mProjectUserGroup.isChangeFiles()){
			removeFileResult = mMessageBundle.getString("noRights");
			return;
		}	
		if(selectedFile == null){
			return;
		}
		
		boolean result = mDatabaseObject.deleteProjectFile(mSelectedProject.getmId(), selectedFile);
		
		if(result){
			removeFileResult = mMessageBundle.getString("removeFileFailed");
		}
		else{
			removeFileResult = mMessageBundle.getString("removeFileSuccessful");
		}
	}

	/**
	 * deletes an uploaded file
	 */
	public void deleteUserFromProject(){
		addFileResult = "";
		addUserResult = "";
		removeFileResult = "";
		removeUserResult = "";
		projectDescriptionResult = "";
		
		if(!mProjectUserGroup.isChangeUsers()){
			removeUserResult = mMessageBundle.getString("noRights");
			return;
		}	
		
		if(selectedUser == null){
			return;
		}

		User logged = (User)mSessionValues.get(Constants.SESSION_USER);
		UserGroup selectedGroup = mDatabaseObject.getUserGroupForProjectUser(mSelectedProject, selectedUser);
				
		if(selectedUser == logged.getUserId()){
			removeUserResult = mMessageBundle.getString("removeSelfProjectUser");
			return;
		}
		else if(selectedGroup != null && selectedGroup.getName().compareTo("Ersteller") == 0){
			removeUserResult = mMessageBundle.getString("removeCreatorProjectUser");
			return;
		}
		 
		boolean result = mDatabaseObject.deleteProjectUser(mSelectedProject, selectedUser);
		
		if(result){
			removeUserResult = mMessageBundle.getString("removeProjectUserFailed");
		}
		else{
			removeUserResult = mMessageBundle.getString("removeProjectUserSuccessful");
		}
	}
	
	/**
	 * adds a new user to the project
	 */
	public void addUserToProject(){
		addFileResult = "";
		addUserResult = "";
		removeFileResult = "";
		removeUserResult = "";
		projectDescriptionResult = "";

		if(!mProjectUserGroup.isChangeUsers()){
			removeUserResult = mMessageBundle.getString("noRights");
			return;
		}	
		
		if(selectedAvailableUser == null || selectedAvailableGroup == null){
			addUserResult = mMessageBundle.getString("addUserEmptySelection");
			return;
		}
		
		User selectedUser = mDatabaseObject.readUser(selectedAvailableUser);
		UserGroup group = mDatabaseObject.getUserGroupForId(selectedAvailableGroup);
		
		if(selectedUser == null || group == null){
			addUserResult = mMessageBundle.getString("addUserDatabaseError");
			return;
		}
		
		boolean result = mDatabaseObject.insertProjectUser(mSelectedProject, selectedUser, group);
		
		if(result){
			addUserResult = mMessageBundle.getString("addUserFailed");
		}
		else{
			addUserResult = mMessageBundle.getString("addUserSuccessful");
		}
	}
	
	/**
	 * open a selected file
	 */
	public String openFile(){

		if(!mProjectUserGroup.isChangeFiles()){
			removeUserResult = mMessageBundle.getString("noRights");
			return "";
		}	
		
		if(selectedFile == null){
			openFileResult = mMessageBundle.getString("openFileNoSelection");
			return null;
		}
		
		ProjectFile file = mDatabaseObject.selectFileForId(selectedFile);
		
		if(file != null){
			mSessionValues.put(Constants.SESSION_SELECTED_FILE, file);	
			return Constants.FILE_PAGE;	
		}
		else{
			openFileResult = mMessageBundle.getString("openFileNotFound");
			return "";	
		}
	}

	/**
	 * provides the download of the meta file
	 */
	public void downloadFile(){
		if(selectedFile == null){
			openFileResult = mMessageBundle.getString("openFileNoSelection");
			return;
		}
		
		ProjectFile metaFile = mDatabaseObject.selectFileForId(selectedFile);
		InputStream fileStream = metaFile.getmFileStream();
		
		if(fileStream == null){
			openFileResult = mMessageBundle.getString("downloadMetaFileNoFile");
		}
		else{
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
				
			externalContext.responseReset();
			
			try {
				OutputStream outputStream = externalContext.getResponseOutputStream();
				byte[] buffer = new byte[10240];
				int contentLength = 0;
				int tmp = 0;
				
				while((tmp = fileStream.read(buffer)) > -1){
					contentLength += tmp;
					outputStream.write(buffer, 0, buffer.length);
				}
				
				externalContext.setResponseContentLength(contentLength);
				externalContext.setResponseContentType(metaFile.getmContentType());
				externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + metaFile.getmFilename() + "\"");
				
				outputStream.close();
				
				
			} catch (IOException e) {
				openFileResult = mMessageBundle.getString("downloadMetaFileDownloadError");
				e.printStackTrace();
			}
		}
		
		
	}
}

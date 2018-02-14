package web;

import java.sql.SQLException;
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
 * Class to control the projects web page
 * 
 * @author Stephan Schulz
 *
 */
@SessionScoped
@ManagedBean(name="projects")
public class Projects {
	
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
	 * project list values
	 */
	private Map<String, Integer> projectList;
	
	/**
	 * user project list
	 */
	private List<Project> listOfProjects;

	/**
	 * result string for add project method
	 */
	private String addProjectsResult;;

	/**
	 * string representation of new project name
	 */
	private String newProjectName;

	/**
	 * project id
	 */
	private Integer projectId;
	
	/**
	 * result string for delete project method
	 */
	private String deleteProjectsResult;

	/**
	 * constructor
	 */
	public Projects(){
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
		projectList = new HashMap<>();
		for(int i = 0; i < listOfProjects.size(); i++){
			Project singleProject = listOfProjects.get(i);
			projectList.put(singleProject.getmProjectName(), singleProject.getmId());
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
	 * @return the projectList
	 */
	public Map<String, Integer> getProjectList() {
		return projectList;
	}

	/**
	 * @param projectList the projectList to set
	 */
	public void setProjectList(Map<String, Integer> projectList) {
		this.projectList = projectList;
	}

	/**
	 * @return the addProjectsResult
	 */
	public String getAddProjectsResult() {
		return addProjectsResult;
	}

	/**
	 * @param addProjectsResult the addProjectsResult to set
	 */
	public void setAddProjectsResult(String addProjectsResult) {
		this.addProjectsResult = addProjectsResult;
	}

	/**
	 * @return the newProjectName
	 */
	public String getNewProjectName() {
		return newProjectName;
	}

	/**
	 * @param newProjectName the newProjectName to set
	 */
	public void setNewProjectName(String newProjectName) {
		this.newProjectName = newProjectName;
	}

	/**
	 * @return the projectId
	 */
	public Integer getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
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
	 * @return the deleteProjectsResult
	 */
	public String getDeleteProjectsResult() {
		return deleteProjectsResult;
	}

	/**
	 * @param deleteProjectsResult the deleteProjectsResult to set
	 */
	public void setDeleteProjectsResult(String deleteProjectsResult) {
		this.deleteProjectsResult = deleteProjectsResult;
	}

	/**
	 * adds a new Project 
	 */
	public void addProject(){
		if(this.newProjectName == null || this.newProjectName.length() == 0){
			this.addProjectsResult = mMessageBundle.getString("projectNameEmpty");
			return;
		}

		User logged = (User)mSessionValues.get(Constants.SESSION_USER);
		boolean createResult = mDatabaseObject.createProject(newProjectName, logged);
		
		if(createResult){
			addProjectsResult = mMessageBundle.getString("addProjectSuccessful");
		}
		else{
			addProjectsResult = mMessageBundle.getString("addProjectFailed");
		}
	}

	/**
	 * deletes a Project 
	 */
	public void deleteProject(){
		if(projectId > 0){
			boolean user = mDatabaseObject.deleteProjectUser(projectId);
			
			if(user){
				boolean result = mDatabaseObject.deleteProject(projectId);	
				if(result){
					deleteProjectsResult = mMessageBundle.getString("deleteProjectSuccessful");	
				}
				else{
					deleteProjectsResult = mMessageBundle.getString("deleteProjectFailed");	
				}
			}
			else{
				deleteProjectsResult = mMessageBundle.getString("deleteProjectUserFailed");	
			}
		}
		else{
			deleteProjectsResult = mMessageBundle.getString("deleteProjectWithoutRow");	
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
		
		User logged = (User)mSessionValues.get(Constants.SESSION_USER);
		
		if(logged != null){
			UserGroup group = mDatabaseObject.getUserGroupForProjectUser(selected, logged.getUserId());
			
			if(group != null){
				context.getExternalContext().getSessionMap().put(Constants.SESSION_PROJECT_USER_GROUP, group);
				return Constants.SINGLE_PROJECT_PAGE;		
			}
		}
		
		return "";
	}
}

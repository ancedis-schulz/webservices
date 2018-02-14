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
import objects.ProjectFile;
import objects.SearchResult;
import objects.User;

/**
 * Managed Bean for search
 * 
 * @author Stephan Schulz
 *
 */
@SessionScoped
@ManagedBean(name="search")
public class Search {
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
	 * user project list
	 */
	private List<Project> listOfProjects;
	
	/**
	 * search word descriptor
	 */
	public String descriptor;
	
	/**
	 * search word project name 
	 */
	public String projectName;
	
	/**
	 * search word user name 
	 */
	public String userName;
	
	/**
	 * search results
	 */
	private Map<String, Integer> searchValues;
	
	/**
	 * selected search result
	 */ private Integer selectedValue;
	
	/**
	 * constructor
	 */
	public Search(){
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
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the listOfProjects
	 */
	public List<Project> getListOfProjects() {
		return listOfProjects;
	}
	
	/**
	 * @return the descriptor
	 */
	public String getDescriptor() {
		return descriptor;
	}

	/**
	 * @param descriptor the descriptor to set
	 */
	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
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
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the selectedValue
	 */
	public Integer getSelectedValue() {
		return selectedValue;
	}

	/**
	 * @param selectedValue the selectedValue to set
	 */
	public void setSelectedValue(Integer selectedValue) {
		this.selectedValue = selectedValue;
	}

	/**
	 * @return the searchValues
	 */
	public Map<String, Integer> getSearchValues() {
		return searchValues;
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
	 * search for files
	 */
	public void search(){
		if(descriptor == null){
			descriptor = "";
		}

		if(userName == null){
			userName = "";
		}

		if(projectName == null){
			projectName = "";
		}
		
		ArrayList<SearchResult> results = mDatabaseObject.search(projectName, userName, descriptor);
		searchValues = new HashMap<>();

		if(results != null && !results.isEmpty()){
			for(int i = 0; i < results.size(); i++){
				SearchResult result = results.get(i);
				searchValues.put(result.searchOutput(), result.getFileId());
			}
		}
		else{
			searchValues.put(mMessageBundle.getString("searchNoResults"), 0);
		}
		
	}
	
	/**
	 * opens a found File
	 * 
	 * @return	String for navigation
	 */
	public String openFile(){
		if(selectedValue == null || selectedValue == 0){
			return "";
		}
		else{
			ProjectFile selectedFile = mDatabaseObject.selectFileForId(selectedValue);
			Project selectedProject = mDatabaseObject.selectProjectForId(selectedValue);
			
			if(selectedFile != null && selectedProject != null){
				mSessionValues.put(Constants.SESSION_SELECTED_FILE, selectedFile);	
				mSessionValues.put(Constants.SESSION_SELECTED_PROJECT, selectedProject);	
				return Constants.FILE_PAGE;
			}
			else{
				return "";
			}
		}
	}
}

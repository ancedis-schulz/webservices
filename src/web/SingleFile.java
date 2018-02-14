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
import objects.FileDescriptor;
import objects.Project;
import objects.ProjectFile;
import objects.User;

/**
 * Managed Bean for file administration
 * 
 * @author Stephan Schulz
 *
 */
@SessionScoped
@ManagedBean(name="singleFile")
public class SingleFile {

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
	 * selected file
	 */
	private ProjectFile mSelectedProjectFile;

	/**
	 * selected filename
	 */
	private String filename;

	/**
	 * Map of File descriptors
	 */
	private Map<String, Integer> descriptorList;

	/**
	 * selected descriptor
	 */
	private Integer selectedDescriptor;
	
	/**
	 * result for meta file up or download
	 */
	private String metaFileResult;
		
	/**
	 * contains the meta file
	 */
	private Part metaFile;
	
	/**
	 * description for meta file
	 */
	private String fileDescription;

	/**
	 * filename of the meta file
	 */
	private String metaFilename;

	/**
	 * result for description saving
	 */
	private String descriptionResult;

	/**
	 * new Descriptor
	 */
	private String newDescriptor;
	
	/**
	 * new Descriptor
	 */
	private String descriptorResult;

	/**
	 * constructor
	 */
	public SingleFile(){
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
		mSelectedProjectFile = (ProjectFile)mSessionValues.get(Constants.SESSION_SELECTED_FILE);
		metaFilename = mDatabaseObject.selectMetaFilenameForFile(mSelectedProjectFile);
		fileDescription = mDatabaseObject.selectDescriptionForFile(mSelectedProjectFile);
		
		if(metaFilename == null){
			metaFilename = mMessageBundle.getString("noActualMetaFile");
		}
		
		if(filename != null){
			filename = mSelectedProjectFile.getmFilename();	
		}
		else{
			filename = "";
		}
		
		ArrayList<FileDescriptor> descriptorArray = mDatabaseObject.selectAllFileDescriptors(mSelectedProjectFile);
		descriptorList = new HashMap<>();
		
		if(descriptorArray != null){
			for(int i = 0; i < descriptorArray.size(); i++){
				FileDescriptor object = descriptorArray.get(i);
				descriptorList.put(object.getmDescriptor(), object.getmId());
			}
		}
		
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
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @return the descriptorList
	 */
	public Map<String, Integer> getDescriptorList() {
		return descriptorList;
	}

	/**
	 * @return the selectedDescriptor
	 */
	public Integer getSelectedDescriptor() {
		return selectedDescriptor;
	}

	/**
	 * @param selectedDescriptor the selectedDescriptor to set
	 */
	public void setSelectedDescriptor(Integer selectedDescriptor) {
		this.selectedDescriptor = selectedDescriptor;
	}

	/**
	 * @return the metaFileResult
	 */
	public String getMetaFileResult() {
		return metaFileResult;
	}

	/**
	 * @param metaFile the metaFile to set
	 */
	public void setMetaFile(Part metaFile) {
		this.metaFile = metaFile;
	}

	/**
	 * @return the metaFile
	 */
	public Part getMetaFile() {
		return metaFile;
	}

	/**
	 * @param fileDescription the fileDescription to set
	 */
	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}

	/**
	 * @return the fileDescription
	 */
	public String getFileDescription() {
		return fileDescription;
	}
	
	/**
	 * @return the metaFilename
	 */
	public String getMetaFilename() {
		return metaFilename;
	}

	/**
	 * @return the descriptionResult
	 */
	public String getDescriptionResult() {
		return descriptionResult;
	}

	/**
	 * @return the newDescriptor
	 */
	public String getNewDescriptor() {
		return newDescriptor;
	}

	/**
	 * @param newDescriptor the newDescriptor to set
	 */
	public void setNewDescriptor(String newDescriptor) {
		this.newDescriptor = newDescriptor;
	}

	/**
	 * @return the descriptorResult
	 */
	public String getDescriptorResult() {
		return descriptorResult;
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
		metaFileResult = "";
		descriptionResult = "";
		descriptorResult = "";
		
		if(metaFile != null){
			try {
				InputStream inputStream = metaFile.getInputStream();
				String filename = metaFile.getSubmittedFileName();
				String contentType = metaFile.getContentType();
				
				mSelectedProjectFile.setmMetaFilename(filename);
				mSelectedProjectFile.setmMetaFileStream(inputStream);
				mSelectedProjectFile.setmMetaContentType(contentType);

				boolean result = mDatabaseObject.updateProjectFile(mSelectedProjectFile);

				if(result){
					metaFileResult = mMessageBundle.getString("uploadMetaFileSuccessful");
				}
				else{
					metaFileResult = mMessageBundle.getString("uploadMetaFileFailed");
				}
			} catch (IOException e) {
				metaFileResult = mMessageBundle.getString("uploadMetaFileFailed");
				e.printStackTrace();
			}catch (NullPointerException e) {
				metaFileResult = mMessageBundle.getString("uploadMetaFileNoFile");
				e.printStackTrace();
			}	
		}
		else{
			metaFileResult = mMessageBundle.getString("uploadMetaFileNoFile");
		}
	}
	
	/**
	 * saves the file description for meta file
	 */
	public void saveFileDescription(){
		metaFileResult = "";
		descriptionResult = "";
		descriptorResult = "";
		
		if(fileDescription == null){
			fileDescription = "";
		}
		
		mSelectedProjectFile.setFileDescription(fileDescription);
		
		boolean result = mDatabaseObject.updateProjectFileDescription(mSelectedProjectFile);
		
		if(result){
			descriptionResult = mMessageBundle.getString("saveFileDescriptionSuccessful");
		}
		else{
			descriptionResult = mMessageBundle.getString("saveFileDescriptionFailed");
		}
		
	}
	
	/**
	 * adds a new descriptor
	 */
	public void addDescriptor(){
		metaFileResult = "";
		descriptionResult = "";
		descriptorResult = "";
		
		if(newDescriptor == null || newDescriptor.isEmpty()){
			return;
		}
		
		boolean result = mDatabaseObject.insertProjectFileDescriptor(mSelectedProjectFile, newDescriptor);
		
		if(result){
			descriptorResult = mMessageBundle.getString("addDescriptorSuccessful");
		}
		else{
			descriptorResult = mMessageBundle.getString("addDescriptorFailed");
		}
		
	}
	
	/**
	 * deletes a descriptor
	 */
	public void deleteDescriptor(){
		metaFileResult = "";
		descriptionResult = "";
		descriptorResult = "";
		
		if(selectedDescriptor == null){
			return;
		}
		
		FileDescriptor descriptor = mDatabaseObject.selectFileDescriptor(selectedDescriptor);
		
		if(descriptor != null){
			boolean result = mDatabaseObject.deleteProjectFileDescriptor(descriptor);
			
			if(result){
				descriptorResult = mMessageBundle.getString("deleteDescriptorSuccessful");
			}
			else{
				descriptorResult = mMessageBundle.getString("deleteDescriptorFailed");
			}
		}
		else{
			descriptorResult = mMessageBundle.getString("deleteDescriptorNotFound");
		}
	}
	
	/**
	 * provides the download of the meta file
	 */
	public void downloadMetaFile(){
		ProjectFile metaFile = mDatabaseObject.selectMetaFileForFile(mSelectedProjectFile);
		InputStream fileStream = metaFile.getmFileStream();
		
		if(fileStream == null){
			metaFileResult = mMessageBundle.getString("downloadMetaFileNoFile");
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
				metaFileResult = mMessageBundle.getString("downloadMetaFileDownloadError");
				e.printStackTrace();
			}
		}
		
		
	}
}

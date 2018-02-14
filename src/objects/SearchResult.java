package objects;

import java.io.Serializable;

/**
 * this class represents the search results
 * 
 * @author Stephan Schulz
 *
 */
public class SearchResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7791121217335261255L;

	/**
	 * project id
	 */
	private int projectId;

	/**
	 * project name
	 */
	private String projectName;

	/**
	 * file id
	 */
	private int fileId;

	/**
	 * file name
	 */
	private String filename;

	/**
	 * @param projectId
	 * @param projectName
	 * @param fileId
	 * @param filename
	 */
	public SearchResult(Integer projectId, String projectName, int fileId, String filename) {
		super();
		this.projectId = projectId;
		this.projectName = projectName;
		this.fileId = fileId;
		this.filename = filename;
	}

	/**
	 * @return the projectId
	 */
	public int getProjectId() {
		return projectId;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @return the fileId
	 */
	public int getFileId() {
		return fileId;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	
	/**
	 * returns the output for the search list
	 * 
	 * @return	string 
	 */
	public String searchOutput(){
		return filename + "(" + projectName + ")";
	}
}

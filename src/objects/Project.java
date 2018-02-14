package objects;

import java.io.Serializable;


/**
 * This class represents the project administration
 * 
 * @author Stephan Schulz
 *
 */
public class Project implements Serializable {

	/**
	 * key to get the attribute id from a json object
	 */
	public static final String JSON_KEY_ID = "id";

	/**
	 * key to get the attribute project name from a json object
	 */
	public static final String JSON_KEY_PROJECT_NAME = "projectName";

	/**
	 * key to get the attribute project description from a json object
	 */
	public static final String JSON_KEY_PROJECT_DESCRIPTION = "projectDescription";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5559188646038002286L;

	/**
	 * project id
	 */
	private int mId;

	/**
	 * project name
	 */
	private String mProjectName;

	/**
	 * project description
	 */
	private String mProjectDescription;

	/**
	 * @param mIda					project id
	 * @param mProjectName			project name
	 * @param mProjectDescription	project description
	 */
	public Project(int mId, String mProjectName, String mProjectDescription) {
		super();
		this.mId = mId;
		this.mProjectName = mProjectName;
		this.mProjectDescription = mProjectDescription;
	}

	/**
	 * @return the mId
	 */
	public int getmId() {
		return mId;
	}

	/**
	 * @return the mProjectName
	 */
	public String getmProjectName() {
		return mProjectName;
	}

	/**
	 * @return the mProjectDescription
	 */
	public String getmProjectDescription() {
		return mProjectDescription;
	}

	/**
	 * @param mProjectDescription the mProjectDescription to set
	 */
	public void setmProjectDescription(String mProjectDescription) {
		this.mProjectDescription = mProjectDescription;
	}
}

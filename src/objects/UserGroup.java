package objects;

import java.io.Serializable;

import org.json.simple.JSONObject;

/**
 * This class represents a user group
 * 
 * @author Stephan Schulz
 *
 */
public class UserGroup implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5739766286935596533L;

	/**
	 * group id
	 */
	private int mID;
	
	/**
	 * group name
	 */
	private String mName;

	/**
	 * right to change project 
	 */
	private boolean mChangeProject;

	/**
	 * right to change project users
	 */
	private boolean mChangeUsers;
	
	/**
	 * right to change project files 
	 */
	private boolean mChangeFiles;

	/**
	 * id key for json object
	 */
	public static final String JSON_KEY_ID = "id";

	/**
	 * name key for json object
	 */
	public static final String JSON_KEY_NAME = "id";

	/**
	 * flag change user key for json object
	 */
	public static final String JSON_KEY_CHANGE_PROJECT = "changeProjectRight";

	/**
	 * flag change user key for json object
	 */
	public static final String JSON_KEY_CHANGE_USER = "changeUserRight";

	/**
	 * flag change files key for json object
	 */
	public static final String JSON_KEY_CHANGE_FILES = "changeFileRight";

	/**
	 * @param mID				group id
	 * @param mName				group name
	 * @param mChangeProject	change project flag
	 * @param mChangeUsers		change project users flag
	 * @param mChangeFiles		change project files flag
	 */
	public UserGroup(int mID, String mName, boolean mChangeProject, boolean mChangeUsers, boolean mChangeFiles) {
		super();
		this.mID = mID;
		this.mName = mName;
		this.mChangeProject = mChangeProject;
		this.mChangeUsers = mChangeUsers;
		this.mChangeFiles = mChangeFiles;
	}
	
	/**
	 * @param mName			group name
	 * @param mChangeUsers	change project users flag
	 * @param mChangeFiles	change project files flag
	 */
	public UserGroup(String mName, boolean mChangeProject,  boolean mChangeUsers, boolean mChangeFiles) {
		super();
		this.mID = -1;
		this.mName = mName;
		this.mChangeProject = mChangeProject;
		this.mChangeUsers = mChangeUsers;
		this.mChangeFiles = mChangeFiles;
	}

	
	/**
	 * @return the group od
	 */
	public int getId() {
		return mID;
	}
	
	/**
	 * @return the mName
	 */
	public String getName() {
		return mName;
	}

	/**
	 * @return the mChangeProject
	 */
	public boolean isChangeProject() {
		return mChangeProject;
	}

	/**
	 * @return the mChangeUsers
	 */
	public boolean isChangeUsers() {
		return mChangeUsers;
	}

	/**
	 * @return the mChangeFiles
	 */
	public boolean isChangeFiles() {
		return mChangeFiles;
	}
	
	/**
	 * converts this object to a valid json object
	 * 
	 * @return	JSONObject representation of this object
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJsonObject(){
		JSONObject object = new JSONObject();
		
		if(mID > 0){
			object.put(JSON_KEY_ID, mID);	
		}
		
		object.put(JSON_KEY_NAME, mName);
		object.put(JSON_KEY_CHANGE_PROJECT, mChangeProject);
		object.put(JSON_KEY_CHANGE_USER, mChangeUsers);
		object.put(JSON_KEY_CHANGE_FILES, mChangeFiles);
		
		return object;
	}
	
	/**
	 * converts the given JSON Object to a user group object
	 * 
	 * @return	UserGroup object with json values
	 */
	public static UserGroup fromJSON(JSONObject jsonObject){
		String name = "";
		boolean changeProject = false;
		boolean changeUser = false;
		boolean changeFiles = false;
		
		if(jsonObject.containsKey(JSON_KEY_NAME)){
			name = (String)jsonObject.get(JSON_KEY_NAME);
		}
		
		if(jsonObject.containsKey(JSON_KEY_CHANGE_PROJECT)){
			changeProject = (Boolean)jsonObject.get(JSON_KEY_CHANGE_PROJECT);
		}
		
		if(jsonObject.containsKey(JSON_KEY_CHANGE_USER)){
			changeUser = (Boolean)jsonObject.get(JSON_KEY_CHANGE_USER);
		}
		
		if(jsonObject.containsKey(JSON_KEY_CHANGE_FILES)){
			changeFiles = (Boolean)jsonObject.get(JSON_KEY_CHANGE_FILES);
		}
		
		UserGroup userGroup;
		
		if(jsonObject.containsKey(JSON_KEY_ID)){
			Integer id = ((Long)jsonObject.get(JSON_KEY_ID)).intValue();
			userGroup = new UserGroup(id, name, changeProject, changeUser, changeFiles);
		}
		else{
			userGroup = new UserGroup(name, changeProject, changeUser, changeFiles);
		}
		
		return userGroup;
	}
}

package objects;

import java.io.Serializable;

import org.json.simple.JSONObject;

/**
 * class to represent a repository user
 * 
 * @author Stephan Schulz
 *
 */
public class User implements Serializable{

	/**
	 * key to get the attribute id from a json object
	 */
	public static final String JSON_KEY_ID = "id";

	/**
	 * key to get the attribute user name from a json object
	 */
	public static final String JSON_KEY_USER_NAME = "username";

	/**
	 * key to get the attribute password from a json object
	 */
	public static final String JSON_KEY_PASSWORD = "password";

	/**
	 * key to get the attribute user group from a json object
	 */
	public static final String JSON_KEY_USER_GROUP = "userGroup";

	/**
	 * 
	 */
	private static final long serialVersionUID = 2604479265693531118L;

	/**
	 * user id
	 */
	private int mUserId;
	
	/**
	 * user name
	 */
	private String mUserName;
	
	/**
	 * users password
	 */
	private String mPassword;
	
	/**
	 * user group
	 */
	private UserGroup mUserGroup;

	/**
	 * constructor
	 * 
	 * @param mUserId 		user id
	 * @param mUserName		user name
	 * @param mPassword		password of the user
	 */
	public User(int userId, String userName, String password) {
		super();
		mUserId = userId;
		mUserName = userName;
		mPassword = password;
		mUserGroup = null;
	}

	/**
	 * constructor
	 * 
	 * @param mUserName		user name
	 * @param mPassword		password of the user
	 */
	public User(String userName, String password) {
		super();
		mUserId = -1;
		mUserName = userName;
		mPassword = password;
		mUserGroup = null;
	}

	/**
	 * returns the user id
	 * 
	 * @return 	id of the user
	 */
	public int getUserId(){
		return mUserId;
	}
	
	/**
	 * returns the login name of the user
	 * 
	 * @return	user name
	 */
	public String getUsername(){
		return mUserName;
	}
	
	/**
	 * returns the users password
	 * 
	 * @return	password of the user
	 */
	public String getPassword(){
		return mPassword;
	}

	/**
	 * sets the new password
	 * 
	 * @param	newPassword		password of the user
	 */
	public void setPassword(String newPassword){
		mPassword = newPassword;
	}
	
	/**
	 * returns the users user group
	 * 
	 * @return	user group of the user
	 */
	public UserGroup getUserGroup(){
		return mUserGroup;
	}

	/**
	 * sets the user group
	 * 
	 * @param	newUserGroup		user group of the user
	 */
	public void setUserGroup(UserGroup newUserGroup){
		mUserGroup = newUserGroup;
	}
	
	/**
	 * returns the label for a user list
	 * 
	 * @return	string 
	 */
	public String getUserLabel(){
		return mUserName + " (" + mUserGroup.getName() + ")";
	}
	
	/**
	 * converts the given object to a json object
	 * 
	 * @return	json representation of this object
	 */
	@SuppressWarnings("unchecked")
	public String toJSON(){
		JSONObject object = new JSONObject();
		object.put(JSON_KEY_USER_NAME, mUserName);
		object.put(JSON_KEY_PASSWORD, mPassword);
		
		if(mUserGroup != null){
			object.put(JSON_KEY_USER_GROUP, mUserGroup.toJsonObject());	
		}
		
		if(mUserId > 0){
			object.put(JSON_KEY_ID, mUserId);
		}
		
		return object.toJSONString();
	}
	
	/**
	 * converts the given JSON Object to a user object
	 * 
	 * @return	User object with json values
	 */
	public static User fromJSON(JSONObject jsonObject){
		String userName = "";
		String password = "";
		
		if(jsonObject.containsKey(JSON_KEY_USER_NAME)){
			userName = (String)jsonObject.get(JSON_KEY_USER_NAME);
		}
		
		if(jsonObject.containsKey(JSON_KEY_PASSWORD)){
			password = (String)jsonObject.get(JSON_KEY_PASSWORD);
		}

		User user = null;

		if(jsonObject.containsKey(JSON_KEY_ID)){
			Integer id = ((Long)jsonObject.get(JSON_KEY_ID)).intValue();
			user = new User(id, userName, password);
		}
		else{
			user = new User(userName, password);
		}
		
		if(jsonObject.containsKey(JSON_KEY_USER_GROUP)){
			JSONObject userGroup = (JSONObject)jsonObject.get(JSON_KEY_USER_GROUP);
			UserGroup group = UserGroup.fromJSON(userGroup);
			user.setUserGroup(group);
		}

		return user;
	}
}

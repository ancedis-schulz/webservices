package web;

import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import database.DatabaseAccessObject;
import objects.User;

/**
 * Controller for Login page
 * 
 * @author Stephan Schulz
 *
 */
@SessionScoped
@ManagedBean(name="login")
public class Login {

	/**
	 * database object
	 */
	private DatabaseAccessObject mDatabaseObject;

	/**
	 * Inputed user name
	 */
	private String username;

	/**
	 * Inputed password
	 */
	private String password;
	
	/**
	 * login result
	 */
	private String loginResult;
	
	/**
	 * message bundle
	 */
	private ResourceBundle mMessageBundle;

	/**
	 * default constructor
	 */
	public Login() {
		try{
			mDatabaseObject = DatabaseAccessObject.getInstance();
		}
		catch(SQLException e){
			e.printStackTrace();
			mDatabaseObject = null;
		}

		FacesContext context = FacesContext.getCurrentInstance();
		mMessageBundle = context.getApplication().getResourceBundle(context, "msg");
		
		loginResult = "";
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
	 * returns the actual password
	 * 
	 * @return	actual password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * sets a new password
	 * 
	 * @param password		new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * returns the login result message
	 * 
	 * @return	result message
	 */
	public String getLoginResult() {
		return loginResult;
	}

	/**
	 * sets a new login result message
	 * 
	 * @param loginResult	new message
	 */
	public void setLoginResult(String loginResult) {
		this.loginResult = loginResult;
	}

	/**
	 * checks the given user name and password, for an existing user account 
	 * 
	 * @param username	user name
	 * @param password	password
	 * 
	 * @return			returns the page name, where the result has redirect to
	 */
	public String checkLogin(){
		loginResult = "";
		String page = "";
		
		if(username == null || username.length() == 0 || password == null || password.length() < Constants.MIN_PASSWORD_LENGTH){
			page = Constants.INDEX_PAGE; 
		}
		else if(mDatabaseObject == null){
			page = Constants.ERROR_PAGE; 
		}
		else{
			User foundUser = mDatabaseObject.selectUser(username, password);
			
			if(foundUser == null){
				loginResult = mMessageBundle.getString("loginFailed");
				page = Constants.INDEX_PAGE; 
			}
			else{
				FacesContext actualContext = FacesContext.getCurrentInstance();
				actualContext.getExternalContext().getSessionMap().put(Constants.SESSION_USER, foundUser);
				page = Constants.PROFILE_PAGE;
			}
		}
		
		return page;
	}
	
	/**
	 * creates a new account
	 */
	public String createAccount(){
		loginResult = "";
		String page = "";
		
		if(username == null || username.length() == 0 || password == null || password.length() < Constants.MIN_PASSWORD_LENGTH){
			page = Constants.INDEX_PAGE; 
		}
		else if(mDatabaseObject == null){
			page = Constants.ERROR_PAGE; 
		}
		else{
			User newUser = new User(username, password);
			User createdUser = mDatabaseObject.createUser(newUser);
			
			if(createdUser != null){
				loginResult = mMessageBundle.getString("accountCreated");
				page = Constants.INDEX_PAGE;
			}
			else{
				loginResult = mMessageBundle.getString("accountCreationFailed");
				page = Constants.INDEX_PAGE;
			}
		}
		
		return page;
	}
}

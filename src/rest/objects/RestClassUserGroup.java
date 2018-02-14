package rest.objects;

import java.sql.SQLException;
import java.util.Map;

import javax.faces.bean.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

import database.DatabaseAccessObject;
import objects.UserGroup;

/**
 * Rest entry point for user group specific operations
 * 
 * @author Stephan Schulz
 *
 */
@RequestScoped
@Path("/userGroup")
public class RestClassUserGroup {

	/**
	 * database access object
	 */
	private DatabaseAccessObject mDao;

	/**
	 * json uer group key
	 */
	public static final String JSON_USER_GROUP_KEY = "usergroups";
	
	/**
	 * default constructor
	 */
	public RestClassUserGroup(){
		try{
			mDao = DatabaseAccessObject.getInstance();
		}
		catch(SQLException e){
			e.printStackTrace();
			mDao = null;
		}
	}
	
	/**
	 * creates a new user group
	 * 
	 * @param groupName	
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@GET
	@Path("/create/{groupname}/{changeProject}/{changeuser}/{changefile}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUserGroup(@PathParam("groupname") String groupname, @PathParam("changeProject") Boolean changeProject, @PathParam("changeuser") Boolean changeuser, @PathParam("changefile") Boolean changeFile){
		UserGroup userGroup = new UserGroup(groupname, changeProject, changeuser, changeFile);
		UserGroup insertedGroup = mDao.createUserGroup(userGroup);
		
		JSONObject userGroupJson = new JSONObject();
		if(insertedGroup != null){
			userGroupJson.put("status", "ok");
		}
		else{
			userGroupJson.put("status", "failure");
		}
		
		mDao.closeDb();

		return Response.ok(userGroupJson, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * select all user groups
	 * 
	 * @return 	returns the Response with the select result
	 */
	@SuppressWarnings("unchecked")
	@GET
	@Path("/selectAll")
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectAllUserGroup(){
		
		Map<Integer, UserGroup> allGroups = mDao.selectAllGroups();
		JSONObject userGroupJson = new JSONObject();
		if(allGroups != null){
			userGroupJson.put("status", "ok");
			JSONObject object = new JSONObject();
			object.putAll(allGroups);
			userGroupJson.put(JSON_USER_GROUP_KEY, object);
		}
		else{
			userGroupJson.put("status", "failure");
		}
		
		mDao.closeDb();

		return Response.ok(userGroupJson, MediaType.APPLICATION_JSON).build();
		
	}
}

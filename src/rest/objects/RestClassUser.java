package rest.objects;

import java.sql.SQLException;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import database.DatabaseAccessObject;
import objects.User;

/**
 * class to administrate user
 */
@RequestScoped
@Path("/user")
public class RestClassUser {
	
	private DatabaseAccessObject mDao = null;

	/**
	 * default constructor
	 */
	public RestClassUser() {
		try{
			mDao = DatabaseAccessObject.getInstance();
		}
		catch(SQLException e){
			e.printStackTrace();
			mDao = null;
		}
	}

	/**
	 * creates a new user
	 * <br />
	 * the rest web service expects the user name and the password as get parameters
	 * 
	 * @param username	login name of the user
	 * @param password	user password
	 * 
	 * @return			boolean returns true if the creation was successful and false if the user already exists or an error occurs
	 */
	@SuppressWarnings("unchecked")
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser(String userJson){
		JSONParser parser = new JSONParser();
		JSONObject object;
		JSONObject resultJson = new JSONObject();
		try {
			object = (JSONObject)parser.parse(userJson);
			User newUser = User.fromJSON(object);
			User insertResult = mDao.createUser(newUser);
			
			resultJson = (JSONObject)parser.parse(insertResult.toJSON());
		} catch (ParseException e) {
			e.printStackTrace();
			resultJson.put("status", "failure");
		}

		return Response.ok(resultJson.toJSONString(), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * reads a user
	 * <br />
	 * the rest web service expects the user id as parameter
	 * 
	 * @param username	login name of the user
	 * 
	 * @return			boolean returns true if the creation was successful and false if the user already exists or an error occurs
	 */
	@SuppressWarnings("unchecked")
	@POST
	@Path("/read")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response readUser(String readJson){
		JSONParser parser = new JSONParser();
		JSONObject object;
		JSONObject resultJson = new JSONObject();
		try {
			object = (JSONObject)parser.parse(readJson);
			Integer userId = ((Long)object.get("userId")).intValue();
			User user = mDao.readUser(userId);
			resultJson = (JSONObject)parser.parse(user.toJSON());
		} catch (ParseException e) {
			e.printStackTrace();
			resultJson.put("status", "failure");
		}

		return Response.ok(resultJson.toJSONString(), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * updates a user
	 * <br />
	 * the rest web service expects the user id, the user name and the password as parameter
	 * 
	 * @param userId	id of the user
	 * @param username	users login name
	 * @param password	users password
	 * 
	 * @return			boolean returns true if the update was successful and false otherwise
	 */
	@SuppressWarnings("unchecked")
	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(String userJson){
		JSONParser parser = new JSONParser();
		JSONObject object;
		JSONObject resultJson = new JSONObject();
		try {
			object = (JSONObject)parser.parse(userJson);
			User updateUser = User.fromJSON(object);
			Boolean result = mDao.updateUser(updateUser);
			resultJson.put("status", result);
		} catch (ParseException e) {
			e.printStackTrace();
			resultJson.put("status", "failure");
		}
		
		return Response.ok(resultJson.toJSONString(), MediaType.APPLICATION_JSON).build();
	}


	/**
	 * deletes a user
	 * <br />
	 * the rest web service expects the user id as parameter
	 * 
	 * @param userId	id of the user
	 * 
	 * @return			boolean returns true if the delete was successful and false otherwise
	 */
	@SuppressWarnings("unchecked")
	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(String userJson){
		JSONParser parser = new JSONParser();
		JSONObject object;
		JSONObject resultJson = new JSONObject();
		try {
			object = (JSONObject)parser.parse(userJson);
			Integer userId = ((Long)object.get("userId")).intValue();
			boolean result = mDao.deleteUser(userId);
			resultJson.put("status", result);
		} catch (ParseException e) {
			e.printStackTrace();
			resultJson.put("status", "failure");
		}

		return Response.ok(resultJson.toJSONString(), MediaType.APPLICATION_JSON).build();
	}


	/**
	 * creates a new user
	 * <br />
	 * the rest web service expects 
	 * 
	 * @param username	login name of the user
	 * @param password	user password
	 * 
	 * @return			boolean returns true if the creation was successful and false if the user already exists or an error occurs
	 */
	@GET
	@Path("/test")
	@Produces("text/plain")
	public Response hello(){ 
		return Response.ok("Hello World", MediaType.TEXT_PLAIN).build();
	}
}

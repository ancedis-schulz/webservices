package rest.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import objects.User;

/**
 * class to test the user webservice
 * 
 * @author steph
 *
 */
public class RestUserClient {

	//@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// Create User
		Client client = ClientBuilder.newClient();
		User user = new User("User1", "User1");
		Entity<String> entity = Entity.entity(user.toJSON(), MediaType.APPLICATION_JSON);
		
		
		WebTarget target = client.target("http://localhost:8080/WebServiceRest/rest/user/create");
		Response response = target.request().accept(MediaType.APPLICATION_JSON).post(entity);
		String user2 = (String)response.readEntity(String.class);
		
		System.out.println(user2.toString());
		
		// Parse create user result
		JSONParser parser = new JSONParser();
		JSONObject userObject;
		try {
			 userObject = (JSONObject)parser.parse(user2);
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}
		
		
		// Update created user
		User newUser = User.fromJSON(userObject);
		newUser.setPassword("User234");
		
		JSONObject userIdJson = new JSONObject();
		userIdJson.put("userId", newUser.getUserId());

		Entity<String> updateEntity = Entity.entity(newUser.toJSON(), MediaType.APPLICATION_JSON);
		
		WebTarget updateTarget = client.target("http://localhost:8080/WebServiceRest/rest/user/update");
		Response updateResponse = updateTarget.request().accept(MediaType.APPLICATION_JSON).post(updateEntity);
		String updateResponseString = (String)updateResponse.readEntity(String.class);
		
		System.out.println(updateResponseString.toString());


		// Read updated user
		WebTarget readTarget = client.target("http://localhost:8080/WebServiceRest/rest/user/read");
		

		Entity<String> readEntity = Entity.entity(userIdJson.toJSONString(), MediaType.APPLICATION_JSON);
		Response readResponse = readTarget.request().accept(MediaType.APPLICATION_JSON).post(readEntity);
		String readResponseString = (String)readResponse.readEntity(String.class);
		
		System.out.println(readResponseString.toString());
		
		//Delete updated user
		WebTarget deleteTarget = client.target("http://localhost:8080/WebServiceRest/rest/user/delete");
		Entity<String> deleteEntity = Entity.entity(userIdJson.toJSONString(), MediaType.APPLICATION_JSON);
		Response deleteResponse = deleteTarget.request().accept(MediaType.APPLICATION_JSON).post(deleteEntity);
		String deleteResponseString = (String)deleteResponse.readEntity(String.class);
		
		System.out.println(deleteResponseString.toString());
		
	}

}

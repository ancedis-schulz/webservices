package rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import rest.objects.RestClassUser;
import rest.objects.RestClassUserGroup;

/**
 * This class is the entry point for the rest application
 * 
 * @author Stephan Schulz
 *
 */
@ApplicationPath("/rest")
public class RestWebservice extends Application {

	/**
	 * set of singleton objects
	 */
	private Set<Object> mSingleton = new HashSet<Object>();

	/**
	 * set of used classes
	 */
	private Set<Class<?>> mClasses = new HashSet<Class<?>>();

	/**
	 * default constructor sets classes and singleton sets
	 */
	public RestWebservice() {
		mSingleton.add(new RestClassUser());
		mSingleton.add(new RestClassUserGroup());
		//singletons.add(new RestParam());
		//mClasses.add(User.class);
		//classes.add(RestFile.class);
	}

	/**
	 * returns the class set
	 * 
	 * @return 	Set<Class<?>> 	returns the Set of available classes
	 */
	@Override
	public Set<Class<?>> getClasses() {
		return mClasses;
	}


	/**
	 * returns the singleton set
	 * 
	 * @return 	Set<Object> 	returns the Set of available singleton objects 
	 */
	@Override
	public Set<Object> getSingletons() {
		return mSingleton;
	}

}

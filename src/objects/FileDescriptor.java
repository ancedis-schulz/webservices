package objects;

import java.io.Serializable;

/**
 * This class represents a file descriptor
 * 
 * @author Stephan Schulz
 *
 */
public class FileDescriptor implements Serializable {

	/**
	 * key to get the attribute id from a json object
	 */
	public static final String JSON_KEY_ID = "id";

	/**
	 * key to get the attribute descriptor from a json object
	 */
	public static final String JSON_KEY_DESCRIPTOR = "descriptor";

	/**
	 * 
	 */
	private static final long serialVersionUID = -5433303413573965178L;

	/**
	 * descriptor id
	 */
	private int mId;
	
	/**
	 * file descriptor
	 */
	private String mDescriptor;

	/**
	 * @param mDescriptor
	 */
	public FileDescriptor(String mDescriptor) {
		super();
		this.mDescriptor = mDescriptor;
	}

	/**
	 * @param mId
	 * @param mDescriptor
	 */
	public FileDescriptor(int mId, String mDescriptor) {
		super();
		this.mId = mId;
		this.mDescriptor = mDescriptor;
	}

	/**
	 * @return the mId
	 */
	public int getmId() {
		return mId;
	}

	/**
	 * @return the mDescriptor
	 */
	public String getmDescriptor() {
		return mDescriptor;
	}
}

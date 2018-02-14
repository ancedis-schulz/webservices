package objects;

import java.io.InputStream;
import java.io.Serializable;

/**
 * This class describes the uploaded file for a project
 * 
 * @author Stephan Schulz
 *
 */
public class ProjectFile implements Serializable {

	/**
	 * key to get the attribute id from a json object
	 */
	public static final String JSON_KEY_ID = "id";

	/**
	 * key to get the attribute filename from a json object
	 */
	public static final String JSON_KEY_FILE_NAME = "fileName";

	/**
	 * key to get the attribute content type of file from a json object
	 */
	public static final String JSON_KEY_CONTENT_TYPE = "contentType";

	/**
	 * key to get the attribute meta file name from a json object
	 */
	public static final String JSON_KEY_META_FILE_NAME = "metaFileName";

	/**
	 * key to get the attribute meta content type from a json object
	 */
	public static final String JSON_KEY_META_CONTENT_TYPE = "metaContentType";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4473441059064643507L;

	/**
	 * file id
	 */
	private Integer mId;
	
	/**
	 * name of the file
	 */
	private String mFilename;
	
	/**
	 * file stream
	 */
	private InputStream mFileStream;
	
	/**
	 * Content type of the file
	 */
	private String mContentType;
	
	/**
	 * meta name of the file
	 */
	private String mMetaFilename;
	
	/**
	 * meta file stream
	 */
	private InputStream mMetaFileStream;
	
	/**
	 * meta file content type
	 */
	private String mMetaContentType;

	/**
	 * project file description
	 */
	private String mFileDescription;
	
	/**
	 * @param mFilename
	 * @param mFileStream
	 */
	public ProjectFile(String mFilename, InputStream mFileStream, String contentType) {
		super();
		this.mFilename = mFilename;
		this.mFileStream = mFileStream;
		this.mContentType = contentType;
	}

	/**
	 * @param mId			file id
	 * @param mFilename		file name
	 * @param mFileStream	file stream
	 */
	public ProjectFile(Integer mId, String mFilename, InputStream mFileStream, String contentType) {
		super();
		this.mId = mId;
		this.mFilename = mFilename;
		this.mFileStream = mFileStream;
		this.mContentType = contentType;
	}

	/**
	 * @return the mId
	 */
	public Integer getmId() {
		return mId;
	}

	/**
	 * @return the mFilename
	 */
	public String getmFilename() {
		return mFilename;
	}

	/**
	 * @return the mFileStream
	 */
	public InputStream getmFileStream() {
		return mFileStream;
	}
	
	/**
	 * @return the mContentType
	 */
	public String getmContentType() {
		return mContentType;
	}

	/**
	 * @param mContentType the mContentType to set
	 */
	public void setmContentType(String mContentType) {
		this.mContentType = mContentType;
	}

	@Override
	public String toString() {
		return mFilename;
	}

	/**
	 * @return the mMetaFilename
	 */
	public String getmMetaFilename() {
		return mMetaFilename;
	}

	/**
	 * @param mMetaFilename the mMetaFilename to set
	 */
	public void setmMetaFilename(String mMetaFilename) {
		this.mMetaFilename = mMetaFilename;
	}

	/**
	 * @return the mMetaFileStream
	 */
	public InputStream getmMetaFileStream() {
		return mMetaFileStream;
	}

	/**
	 * @param mMetaFileStream the mMetaFileStream to set
	 */
	public void setmMetaFileStream(InputStream mMetaFileStream) {
		this.mMetaFileStream = mMetaFileStream;
	}

	/**
	 * @return the mMetaContentType
	 */
	public String getmMetaContentType() {
		return mMetaContentType;
	}

	/**
	 * @param mMetaContentType the mMetaContentType to set
	 */
	public void setmMetaContentType(String mMetaContentType) {
		this.mMetaContentType = mMetaContentType;
	}

	/**
	 * @return the mFileDescription
	 */
	public String getFileDescription() {
		return mFileDescription;
	}

	/**
	 * @param mFileDescription the mFileDescription to set
	 */
	public void setFileDescription(String mFileDescription) {
		this.mFileDescription = mFileDescription;
	}
}

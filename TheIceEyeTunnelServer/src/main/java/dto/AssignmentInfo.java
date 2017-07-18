package dto;

import java.io.Serializable;

/**
 * 
 * @author bishoysamir
 *
 */
public class AssignmentInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long userId;
	long id;
	String title;
	String body;

	public AssignmentInfo(long userId, long id, String title, String body) {
		super();
		this.userId = userId;
		this.id = id;
		this.title = title;
		this.body = body;
	}

	@Override
	public String toString() {
		return "AssignmentInfo [userId=" + userId + ", id=" + id + ", title=" + title + ", body=" + body + "]";
	}

}

package entitys;

import java.util.Date;

import org.hwork.annotation.Json;
import org.hwork.annotation.PrimaryKey;
import org.hwork.annotation.Table;

@Table("users")
public class Blog {
	
	@PrimaryKey
	private int id;
	private String title;
	private String email;
	private String content;
	private Date date;
	private Users users;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public void setUsers(Users users){
		this.users = users;
	}
	
	public Users getUsers(){
		return users;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

}

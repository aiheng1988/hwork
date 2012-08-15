package models;


import java.sql.SQLException;

import org.hwork.model.Model;

import entitys.Blog;

public class BlogModel extends Model<Blog> {
	
	public static void main(String[] args) {
		try {
			new BlogModel().execSql("delete from users where password = ?", new Object[]{"asdfa"});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

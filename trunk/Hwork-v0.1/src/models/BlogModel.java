package models;

import org.hwork.model.Model;

import entitys.Blog;

public class BlogModel extends Model<Blog> {
	
	public static void main(String[] args) throws Exception {
		System.out.println(new BlogModel().findAll().get(0).getContent());
	}

}

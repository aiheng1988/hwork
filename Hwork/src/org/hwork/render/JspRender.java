package org.hwork.render;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;

public class JspRender extends Render {
	private static Logger logger = Logger.getLogger(JspRender.class);
	
	@Override
	public void render(String view) {
		try {
			request.getRequestDispatcher(view).forward(request, response);
		} catch (ServletException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

}

package com.anverm.smartidea.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.anverm.analytics.logger.BasicAnalyticsLogger;
import com.anverm.webutil.queue.exception.QueueException;

/**
 * Servlet implementation class InitServlet
 */
@WebServlet("/InitServlet")
public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(InitServlet.class);
	private BasicAnalyticsLogger basicAnalyticsLogger;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InitServlet() {
      basicAnalyticsLogger = BasicAnalyticsLogger.init();
      logger.info("initializing basic analytics logger.");
    }

    
    /**
    * 
    */
    @Override
  public void destroy() {
    super.destroy();
    try {
      basicAnalyticsLogger.destroy();
    } catch (QueueException e) {
      logger.error("Exception occurred while destroying BasicAnalyticsLogger", e);
    }
  }
}

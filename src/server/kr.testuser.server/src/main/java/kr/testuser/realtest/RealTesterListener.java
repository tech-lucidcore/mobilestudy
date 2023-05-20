package kr.testuser.realtest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class RealTesterListener implements ServletContextListener {
	public static RealTester __realTester = null;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			__realTester = new RealTester();
			__realTester.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			__realTester.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

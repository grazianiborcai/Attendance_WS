package br.com.gda.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

//import javax.money.convert.ExchangeRateProvider;
//import javax.money.convert.MonetaryConversions;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import com.sun.jersey.spi.container.servlet.ServletContainer;

import br.com.gda.mqtt.Publisher;

public class ServletContainerGDA extends ServletContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String DRIVER = "com.mysql.jdbc.Driver";
//	public static final String DATABASE_URL = "jdbc:mysql://127.0.0.1:3306/";
//	public static final String DATABASE_URL = "jdbc:mysql://192.168.0.150:3306/";
//	public static final String USERNAME = "root";
//	public static final String PASS_W = "!qazxsw@";
//	public static final String PASS_W = "lapEborA1!";

//	public static final ExchangeRateProvider exchangeRateProvider = MonetaryConversions.getExchangeRateProvider("IDENT",
//			"ECB", "ECB-HIST", "ECB-HIST90");
	public static Publisher publisher;
	
	public static ServletContext context;

	public static ConcurrentHashMap<String, DataSource> datasource = new ConcurrentHashMap<String, DataSource>();

//	static {
//		ServletContainerGDA.putDataSource(config.getServletContext(), "jdbc/gdaDB", DATABASE_URL, USERNAME, PASS_W);
//	}

	public static final DataSource putDataSource(ServletContext sContext, String key) {
		PoolProperties p = new PoolProperties();
		
		if (sContext == null)
			sContext = context;
		
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String path =  sContext.getInitParameter("mysqlfile");
			input = sContext.getResourceAsStream(path);
			// load a properties file
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		String dburl = "jdbc:mysql://"+prop.getProperty("host")+":"+prop.getProperty("port")+"/";
		String dbUser = prop.getProperty("user");
		String dbPassword = prop.getProperty("password");
		
//		String dburl = DATABASE_URL;
//		String dbUser = USERNAME;
//		String dbPassword = PASS_W;

		
		p.setUrl(dburl);
		p.setDriverClassName(DRIVER);
		p.setUsername(dbUser);
		p.setPassword(dbPassword);
		p.setJmxEnabled(true);
		p.setTestWhileIdle(false);
		p.setTestOnBorrow(true);
		p.setValidationQuery("SELECT 1");
		p.setTestOnReturn(false);
		p.setValidationInterval(30000);
		p.setTimeBetweenEvictionRunsMillis(30000);
		p.setMaxActive(100);
		p.setInitialSize(10);
		p.setMaxWait(100);
		p.setRemoveAbandonedTimeout(60);
		p.setMinEvictableIdleTimeMillis(30000);
		p.setMinIdle(10);
		p.setMaxIdle(50);
		p.setLogAbandoned(true);
		p.setRemoveAbandoned(true);
		p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
				+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
		DataSource d = new DataSource();
		d.setPoolProperties(p);

		DataSource dS = datasource.putIfAbsent(key, d);

		if (dS == null)
			dS = d;

		return dS;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		context = config.getServletContext();
		putDataSource(config.getServletContext(), "jdbc/gdaDB");
//		publisher = new Publisher();

	}

	@Override
	public void destroy() {
		super.destroy();
		publisher.disconnect();
	}

}

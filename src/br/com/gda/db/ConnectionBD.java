package br.com.gda.db;

import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.sql.DataSource;
//import javax.naming.Context;
//import javax.naming.InitialContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

//import org.apache.tomcat.jdbc.pool.DataSource;

import br.com.gda.servlet.ServletContainerGDA;

public class ConnectionBD extends GdaDB {

	private static final String CLOSE_CONNECTION_PROBLEM = "There is a problem to close the connection";
	private static final String OPEN_CONNECTION_PROBLEM = "There is a problem to open the connection";

	protected static final String INSERT_OK = "Records inserted";
	protected static final String UPDATE_OK = "Records updated";
	protected static final String DELETE_OK = "Records deleted";

	public void closeConnection(Connection conn, List<PreparedStatement> stmt, ResultSet rs) {
		try {
			if (conn != null) {
				conn.setAutoCommit(true);
				conn.close();
				conn = null;
			}
			if (stmt != null && !stmt.isEmpty()) {
				for (PreparedStatement eachStmt : stmt) {
					if (eachStmt != null) {
						eachStmt.close();
						eachStmt = null;
					}
				}
			}
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		Connection con = null;
		DataSource datasource = null;
		try {

			// Context initContext = new InitialContext();
			// Context envContext = (Context)
			// initContext.lookup("java:/comp/env");
			// DataSource datasource = new DataSource();
			// datasource = (DataSource) envContext.lookup("jdbc/gdaDB");
			// con = datasource.getConnection();

			datasource = ServletContainerGDA.datasource.get("jdbc/gdaDB");

			if (datasource == null) {

				datasource = ServletContainerGDA.putDataSource(null, "jdbc/gdaDB");
			}

			con = datasource.getConnection();

			// Class.forName(DRIVER);
			// con = DriverManager.getConnection(DATABASE_URL, USERNAME,
			// PASS_W);
		} catch (Exception e) {
			try {
				ServletContainerGDA.datasource.remove("jdbc/gdaDB");
				datasource = ServletContainerGDA.putDataSource(null, "jdbc/gdaDB");
				con = datasource.getConnection();
			} catch (Exception e1) {
				System.out.println(OPEN_CONNECTION_PROBLEM);
				e.printStackTrace();
				throw new WebApplicationException(Status.PRECONDITION_FAILED);
			}
		}
		return con;
	}

}

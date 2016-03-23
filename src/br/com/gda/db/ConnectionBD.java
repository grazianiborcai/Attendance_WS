package br.com.gda.db;

import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

	public Connection getConnection() {
		Connection con = null;
		DataSource datasource = null;
		try {

//			 Context initContext = new InitialContext();
//			 Context envContext = (Context)
//			 initContext.lookup("java:/comp/env");
//			 DataSource datasource = new DataSource();
//			 datasource = (DataSource) envContext.lookup("jdbc/gdaDB");
//			 con = datasource.getConnection();

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

	public void closeConnection(PreparedStatement stmt) {
		try {
			close(null, stmt, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
					null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(PreparedStatement stmt, PreparedStatement stmt2) {
		try {
			close(null, stmt, stmt2, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
					null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(PreparedStatement stmt, PreparedStatement stmt2, PreparedStatement stmt3) {
		try {
			close(null, stmt, stmt2, stmt3, null, null, null, null, null, null, null, null, null, null, null, null,
					null, null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(PreparedStatement stmt, PreparedStatement stmt2, PreparedStatement stmt3,
			PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6, PreparedStatement stmt7) {
		try {
			close(null, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, stmt7, null, null, null, null, null, null, null, null,
					null, null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(PreparedStatement stmt, ResultSet rs) {
		try {
			close(null, stmt, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
					null, null, rs);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(PreparedStatement stmt, PreparedStatement stmt2, PreparedStatement stmt3,
			ResultSet rs) {
		try {
			close(null, stmt, stmt2, stmt3, null, null, null, null, null, null, null, null, null, null, null, null,
					null, null, null, rs);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, ResultSet rs) {
		try {
			close(conn, stmt, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
					null, null, rs);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2, ResultSet rs) {
		try {
			close(conn, stmt, stmt2, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
					null, null, rs);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, ResultSet rs) {
		try {
			close(conn, stmt, stmt2, stmt3, null, null, null, null, null, null, null, null, null, null, null, null,
					null, null, null, rs);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, ResultSet rs) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, null, null, null, null, null, null, null, null, null, null, null,
					null, null, null, rs);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6,
			ResultSet rs) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, null, null, null, null, null, null, null, null, null,
					null, null, null, rs);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6,
			PreparedStatement stmt7, ResultSet rs) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, stmt7, null, null, null, null, null, null, null, null,
					null, null, null, rs);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6,
			PreparedStatement stmt7, PreparedStatement stmt8, ResultSet rs) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, stmt7, stmt8, null, null, null, null, null, null, null,
					null, null, null, rs);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt) {
		try {
			close(conn, stmt, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
					null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2) {
		try {
			close(conn, stmt, stmt2, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
					null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3) {
		try {
			close(conn, stmt, stmt2, stmt3, null, null, null, null, null, null, null, null, null, null, null, null,
					null, null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, null, null, null, null, null, null, null, null, null, null, null,
					null, null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, PreparedStatement stmt5) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, stmt5, null, null, null, null, null, null, null, null, null, null,
					null, null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, null, null, null, null, null, null, null, null, null,
					null, null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6,
			PreparedStatement stmt7) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, stmt7, null, null, null, null, null, null, null, null,
					null, null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6,
			PreparedStatement stmt7, PreparedStatement stmt8) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, stmt7, stmt8, null, null, null, null, null, null, null,
					null, null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6,
			PreparedStatement stmt7, PreparedStatement stmt8, PreparedStatement stmt9) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, stmt7, stmt8, stmt9, null, null, null, null, null,
					null, null, null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6,
			PreparedStatement stmt7, PreparedStatement stmt8, PreparedStatement stmt9, PreparedStatement stmt10) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, stmt7, stmt8, stmt9, stmt10, null, null, null, null,
					null, null, null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6,
			PreparedStatement stmt7, PreparedStatement stmt8, PreparedStatement stmt9, PreparedStatement stmt10,
			PreparedStatement stmt11) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, stmt7, stmt8, stmt9, stmt10, stmt11, null, null, null,
					null, null, null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6,
			PreparedStatement stmt7, PreparedStatement stmt8, PreparedStatement stmt9, PreparedStatement stmt10,
			PreparedStatement stmt11, PreparedStatement stmt12) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, stmt7, stmt8, stmt9, stmt10, stmt11, stmt12, null,
					null, null, null, null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6,
			PreparedStatement stmt7, PreparedStatement stmt8, PreparedStatement stmt9, PreparedStatement stmt10,
			PreparedStatement stmt11, PreparedStatement stmt12, PreparedStatement stmt13) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, stmt7, stmt8, stmt9, stmt10, stmt11, stmt12, stmt13,
					null, null, null, null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6,
			PreparedStatement stmt7, PreparedStatement stmt8, PreparedStatement stmt9, PreparedStatement stmt10,
			PreparedStatement stmt11, PreparedStatement stmt12, PreparedStatement stmt13, PreparedStatement stmt14) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, stmt7, stmt8, stmt9, stmt10, stmt11, stmt12, stmt13,
					stmt14, null, null, null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6,
			PreparedStatement stmt7, PreparedStatement stmt8, PreparedStatement stmt9, PreparedStatement stmt10,
			PreparedStatement stmt11, PreparedStatement stmt12, PreparedStatement stmt13, PreparedStatement stmt14,
			PreparedStatement stmt15) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, stmt7, stmt8, stmt9, stmt10, stmt11, stmt12, stmt13,
					stmt14, stmt15, null, null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6,
			PreparedStatement stmt7, PreparedStatement stmt8, PreparedStatement stmt9, PreparedStatement stmt10,
			PreparedStatement stmt11, PreparedStatement stmt12, PreparedStatement stmt13, PreparedStatement stmt14,
			PreparedStatement stmt15, PreparedStatement stmt16) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, stmt7, stmt8, stmt9, stmt10, stmt11, stmt12, stmt13,
					stmt14, stmt15, stmt16, null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6,
			PreparedStatement stmt7, PreparedStatement stmt8, PreparedStatement stmt9, PreparedStatement stmt10,
			PreparedStatement stmt11, PreparedStatement stmt12, PreparedStatement stmt13, PreparedStatement stmt14,
			PreparedStatement stmt15, PreparedStatement stmt16, PreparedStatement stmt17, PreparedStatement stmt18) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, stmt7, stmt8, stmt9, stmt10, stmt11, stmt12, stmt13,
					stmt14, stmt15, stmt16, stmt17, stmt18, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6,
			PreparedStatement stmt7, PreparedStatement stmt8, PreparedStatement stmt9, PreparedStatement stmt10,
			PreparedStatement stmt11, PreparedStatement stmt12, PreparedStatement stmt13, PreparedStatement stmt14,
			PreparedStatement stmt15, PreparedStatement stmt16, PreparedStatement stmt17) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, stmt7, stmt8, stmt9, stmt10, stmt11, stmt12, stmt13,
					stmt14, stmt15, stmt16, stmt17, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6,
			PreparedStatement stmt7, PreparedStatement stmt8, PreparedStatement stmt9, PreparedStatement stmt10,
			ResultSet rs) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, stmt7, stmt8, stmt9, stmt10, null, null, null, null,
					null, null, null, null, rs);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6,
			PreparedStatement stmt7, PreparedStatement stmt8, PreparedStatement stmt9, PreparedStatement stmt10,
			PreparedStatement stmt11, ResultSet rs) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, stmt7, stmt8, stmt9, stmt10, stmt11, null, null, null,
					null, null, null, null, rs);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn, PreparedStatement stmt, PreparedStatement stmt2,
			PreparedStatement stmt3, PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6,
			PreparedStatement stmt7, PreparedStatement stmt8, PreparedStatement stmt9, PreparedStatement stmt10,
			PreparedStatement stmt11, PreparedStatement stmt12, ResultSet rs) {
		try {
			close(conn, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, stmt7, stmt8, stmt9, stmt10, stmt11, stmt12, null,
					null, null, null, null, null, rs);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(Connection conn) {
		try {
			close(conn, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
					null, null, null);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	public void closeConnection(ResultSet rs) {
		try {
			close(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
					null, null, rs);
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

	private void close(Connection conn, PreparedStatement stmt, PreparedStatement stmt2, PreparedStatement stmt3,
			PreparedStatement stmt4, PreparedStatement stmt5, PreparedStatement stmt6, PreparedStatement stmt7,
			PreparedStatement stmt8, PreparedStatement stmt9, PreparedStatement stmt10, PreparedStatement stmt11,
			PreparedStatement stmt12, PreparedStatement stmt13, PreparedStatement stmt14, PreparedStatement stmt15,
			PreparedStatement stmt16, PreparedStatement stmt17, PreparedStatement stmt18, ResultSet rs) {

		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (stmt2 != null) {
				stmt2.close();
				stmt2 = null;
			}
			if (stmt3 != null) {
				stmt3.close();
				stmt3 = null;
			}
			if (stmt4 != null) {
				stmt4.close();
				stmt4 = null;
			}
			if (stmt5 != null) {
				stmt5.close();
				stmt5 = null;
			}
			if (stmt6 != null) {
				stmt6.close();
				stmt6 = null;
			}
			if (stmt7 != null) {
				stmt7.close();
				stmt7 = null;
			}
			if (stmt8 != null) {
				stmt8.close();
				stmt8 = null;
			}
			if (stmt9 != null) {
				stmt9.close();
				stmt9 = null;
			}
			if (stmt10 != null) {
				stmt10.close();
				stmt10 = null;
			}
			if (stmt11 != null) {
				stmt11.close();
				stmt11 = null;
			}
			if (stmt12 != null) {
				stmt12.close();
				stmt12 = null;
			}
			if (stmt13 != null) {
				stmt13.close();
				stmt13 = null;
			}
			if (stmt14 != null) {
				stmt14.close();
				stmt14 = null;
			}
			if (stmt15 != null) {
				stmt15.close();
				stmt15 = null;
			}
			if (stmt16 != null) {
				stmt16.close();
				stmt16 = null;
			}
			if (stmt17 != null) {
				stmt17.close();
				stmt17 = null;
			}
			if (stmt18 != null) {
				stmt18.close();
				stmt18 = null;
			}
			if (conn != null) {
				conn.setAutoCommit(true);
				conn.close();
				conn = null;
			}
		} catch (Exception e) {
			System.out.println(CLOSE_CONNECTION_PROBLEM);
			e.printStackTrace();
		}
	}

}

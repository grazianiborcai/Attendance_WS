package br.com.gda.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import br.com.gda.dao.helper.EnrollmentHelper;
import br.com.gda.dao.helper.PeopleHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.Enrollment;
import br.com.mind5.helper.People;

public class EnrollmentDAO extends ConnectionBD {

	public SQLException insertEnrollment(ArrayList<Enrollment> EnrollmentList) {

		Connection conn = null;
		PreparedStatement insertStmt01 = null;
		PreparedStatement insertStmt02 = null;
		ArrayList<PreparedStatement> stmt = new ArrayList<PreparedStatement>();

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmt01 = conn.prepareStatement(EnrollmentHelper.ST_IN_ALL_FIELD);
			insertStmt02 = conn.prepareStatement(PeopleHelper.ST_IN_ALL_FIELD);

			for (Enrollment enrollment : EnrollmentList) {
				for (People people : enrollment.getPeople()) {
					insertStmt02.setString(1, people.getCountryID());
					insertStmt02.setString(2, people.getRegionID());
					insertStmt02.setInt(3, people.getGradeID());
					insertStmt02.setString(4, people.getName());
					if (people.getBirthDateS() != null)
						insertStmt02.setDate(5, Date.valueOf(people.getBirthDateS()));
					else
						insertStmt02.setNull(5, Types.DATE);
					insertStmt02.setLong(6, people.getEnrollmentNumber());
					if (people.getGradeDateS() != null)
						insertStmt02.setDate(7, Date.valueOf(people.getGradeDateS()));
					else
						insertStmt02.setNull(7, Types.DATE);
					insertStmt02.setString(8, people.getEmail());
					insertStmt02.setString(9, people.getCelphone());
					insertStmt02.setString(10, people.getPhone());
					insertStmt02.setString(11, people.getAddress1());
					insertStmt02.setString(12, people.getAddress2());
					insertStmt02.setString(13, people.getPostalCode());
					insertStmt02.setString(14, people.getBloodType());
					insertStmt02.setString(15, people.getAllergy());
					insertStmt02.setString(16, people.getAllergyDesc());
					if (people.getGradeDateS() != null)
						insertStmt02.setDate(17, Date.valueOf(people.getNextGradeExamS()));
					else
						insertStmt02.setNull(17, Types.DATE);
					insertStmt02.setInt(18, people.getGenderID());
					insertStmt02.setInt(19, people.getWhereID());
					insertStmt02.setString(20, people.getWhereOther());
					insertStmt02.setInt(21, people.getLookID());
					insertStmt02.setString(22, people.getLookingOther());
					insertStmt02.setString(23, people.getPassword());
					insertStmt02.setInt(24, people.getEnrTypeID());

					insertStmt02.executeUpdate();

					insertStmt01.setLong(1, enrollment.getClassID());
					insertStmt01.setLong(2, enrollment.getPlaceID());
					insertStmt01.setLong(3, Long.valueOf(0));
					insertStmt01.setInt(4, enrollment.getEnrTypeID());
					if (enrollment.getBeginDate() != null)
						insertStmt01.setDate(5, Date.valueOf(enrollment.getBeginDate()));
					else
						insertStmt01.setNull(5, Types.DATE);
					if (enrollment.getEndDate() != null)
						insertStmt01.setDate(6, Date.valueOf(enrollment.getEndDate()));
					else
						insertStmt01.setNull(6, Types.DATE);

					insertStmt01.executeUpdate();
				}
			}

			stmt.add(insertStmt01);
			stmt.add(insertStmt02);

			conn.commit();

			return new SQLException(INSERT_OK, null, 200);

		} catch (SQLException | RuntimeException e) {
			try {
				conn.rollback();
				return (SQLException) e;
			} catch (SQLException e1) {
				return e1;
			}
		} finally {
			closeConnection(conn, stmt, null);
		}

	}

	public ArrayList<Enrollment> selectEnrollmentText(Long classID) throws SQLException {

		ArrayList<Enrollment> enrollmentTextList = new ArrayList<Enrollment>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;
		ArrayList<PreparedStatement> stmt = new ArrayList<PreparedStatement>();

		try {

			conn = getConnection();

			EnrollmentHelper enrollmentHelper = new EnrollmentHelper();

			selectStmt = conn.prepareStatement(enrollmentHelper.prepareSelect(classID));

			resultSet = selectStmt.executeQuery();

			stmt.add(selectStmt);

			while (resultSet.next()) {

				enrollmentHelper.assignResult(enrollmentTextList, resultSet);
			}

			return enrollmentTextList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, stmt, resultSet);
		}
	}

}

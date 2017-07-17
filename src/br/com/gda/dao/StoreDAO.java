package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.MaterialStoreHelper;
import br.com.gda.dao.helper.StoreEmployeeHelper;
import br.com.gda.dao.helper.StoreHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.MaterialStore;
import br.com.mind5.helper.RecordMode;
import br.com.mind5.helper.Store;
import br.com.mind5.helper.StoreEmployee;

public class StoreDAO extends ConnectionBD {

	public SQLException insertStore(ArrayList<Store> storeList) {

		Connection conn = null;
		PreparedStatement insertStmtT01 = null;
		PreparedStatement insertStmtT03 = null;
		PreparedStatement insertStmtT04 = null;
		PreparedStatement variableStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmtT01 = conn.prepareStatement(StoreHelper.ST_IN_ALL_FIELD);

			insertStmtT03 = conn.prepareStatement(MaterialStoreHelper.ST_IN_ALL_FIELD);

			insertStmtT04 = conn.prepareStatement(StoreEmployeeHelper.ST_IN_ALL);

			variableStmt = conn.prepareStatement(StoreHelper.VARIABLE);

			for (Store store : storeList) {

				prepareInsertStore(insertStmtT01, variableStmt, store);

				for (MaterialStore material : store.getMaterial()) {

					prepareInsertMaterialStore(insertStmtT03, store, material);
				}

				for (StoreEmployee employee : store.getEmployee()) {

					prepareInsertStoreEmployee(insertStmtT04, store, employee);
				}

				insertStmtT03.executeBatch();

				insertStmtT04.executeBatch();
			}

			conn.commit();

			return new SQLException(INSERT_OK, null, 200);

		} catch (SQLException e) {
			try {
				conn.rollback();
				return e;
			} catch (SQLException e1) {
				return e1;
			}
		} finally {
			closeConnection(conn, insertStmtT01, insertStmtT03, insertStmtT04, variableStmt);
		}

	}

	public SQLException updateStore(ArrayList<Store> storeList) {

		Connection conn = null;
		PreparedStatement insertStmtT01 = null;
		PreparedStatement insertStmtT03 = null;
		PreparedStatement insertStmtT04 = null;
		PreparedStatement updateStmtT01 = null;
		PreparedStatement updateStmtT04 = null;
		PreparedStatement updateStmtT05 = null;
		PreparedStatement updateStmtT06 = null;
		PreparedStatement updateStmtT07 = null;
		PreparedStatement deleteStmtT01 = null;
		PreparedStatement deleteStmtT02 = null;
		PreparedStatement deleteStmtT03 = null;
		PreparedStatement variableStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmtT01 = conn.prepareStatement(StoreHelper.ST_IN_ALL_FIELD);

			insertStmtT03 = conn.prepareStatement(MaterialStoreHelper.ST_IN_ALL_FIELD);

			insertStmtT04 = conn.prepareStatement(StoreEmployeeHelper.ST_IN_ALL);

			variableStmt = conn.prepareStatement(StoreHelper.VARIABLE);

			updateStmtT01 = conn.prepareStatement(StoreHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);

			updateStmtT04 = conn.prepareStatement(MaterialStoreHelper.ST_UP_RECORD_MODE_BY_STORE);

			updateStmtT05 = conn.prepareStatement(MaterialStoreHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);

			updateStmtT06 = conn.prepareStatement(StoreEmployeeHelper.ST_UP_RECORD_BY_STORE);

			updateStmtT07 = conn.prepareStatement(StoreEmployeeHelper.ST_UP_ALL_BY_FULL_KEY);

			StoreEmployeeHelper storeEmployeeHelper = new StoreEmployeeHelper();
			MaterialStoreHelper materialStoreHelper = new MaterialStoreHelper();
			for (Store store : storeList) {

				List<Integer> codMaterial = new ArrayList<Integer>();
				List<Integer> codEmployee = new ArrayList<Integer>();

				if ((store.getRecordMode() != null && store.getRecordMode().equals(RecordMode.ISNEW))
						|| store.getCodStore() == null || store.getCodStore().equals(0)) {

					prepareInsertStore(insertStmtT01, variableStmt, store);

				} else {

					updateStmtT01.setString(1, store.getCnpj());
					updateStmtT01.setString(2, store.getInscEstadual());
					updateStmtT01.setString(3, store.getInscMunicipal());
					updateStmtT01.setString(4, store.getRazaoSocial());
					updateStmtT01.setString(5, store.getName());
					updateStmtT01.setString(6, store.getAddress1());
					updateStmtT01.setString(7, store.getAddress2());
					updateStmtT01.setInt(8, store.getPostalcode());
					updateStmtT01.setString(9, store.getCity());
					updateStmtT01.setString(10, store.getCountry());
					updateStmtT01.setString(11, store.getState());
					updateStmtT01.setString(12, store.getPhone());
					updateStmtT01.setString(13, store.getCodCurr());

					if (store.getRecordMode() != null && (store.getRecordMode().equals(RecordMode.ISDELETED)
							|| store.getRecordMode().equals(RecordMode.RECORD_DELETED))) {
						updateStmtT01.setString(14, RecordMode.RECORD_DELETED);

						updateStmtT04.setString(1, RecordMode.RECORD_DELETED);
						updateStmtT04.setLong(2, store.getCodOwner());
						updateStmtT04.setInt(3, store.getCodStore());

						updateStmtT04.addBatch();

						updateStmtT06.setString(1, RecordMode.RECORD_DELETED);
						updateStmtT06.setLong(2, store.getCodOwner());
						updateStmtT06.setInt(3, store.getCodStore());

						updateStmtT06.addBatch();

					} else {
						updateStmtT01.setString(14, RecordMode.RECORD_OK);
					}
					updateStmtT01.setLong(15, store.getCodOwner());
					updateStmtT01.setInt(16, store.getCodStore());

					updateStmtT01.addBatch();

				}

				for (MaterialStore material : store.getMaterial()) {
					if ((store.getRecordMode() != null && store.getRecordMode().equals(RecordMode.ISNEW))
							|| (material.getRecordMode() != null && material.getRecordMode().equals(RecordMode.ISNEW))
							|| store.getCodStore() == null || store.getCodStore().equals(0)) {

						prepareInsertMaterialStore(insertStmtT03, store, material);

					} else if ((store.getRecordMode() != null && !store.getRecordMode().equals(RecordMode.ISDELETED)
							&& !store.getRecordMode().equals(RecordMode.RECORD_DELETED))
							|| store.getRecordMode() == null) {

						if (material.getRecordMode() != null && (material.getRecordMode().equals(RecordMode.ISDELETED)
								|| material.getRecordMode().equals(RecordMode.RECORD_DELETED))) {

							codMaterial.add(material.getCodMaterial());

						} else {

							updateStmtT05.setBigDecimal(1,
									material.getPriceStore() != null && !material.getPriceStore().equals(0)
											? material.getPriceStore() : material.getPrice());
							updateStmtT05.setInt(2,
									material.getDurationStore() != null && !material.getDurationStore().equals(0)
											? material.getDurationStore() : material.getDuration());
							updateStmtT05.setString(3, RecordMode.RECORD_OK);
							updateStmtT05.setString(4, material.getCodCurrStore());
							updateStmtT05.setLong(5, store.getCodOwner());
							updateStmtT05.setInt(6, material.getCodMaterial());
							updateStmtT05.setInt(7, store.getCodStore());

							updateStmtT05.addBatch();
						}
					}
				}

				for (StoreEmployee employee : store.getEmployee()) {
					if ((store.getRecordMode() != null && store.getRecordMode().equals(RecordMode.ISNEW))
							|| (employee.getRecordMode() != null && employee.getRecordMode().equals(RecordMode.ISNEW))
							|| store.getCodStore() == null || store.getCodStore().equals(0)) {

						prepareInsertStoreEmployee(insertStmtT04, store, employee);

					} else if ((store.getRecordMode() != null && !store.getRecordMode().equals(RecordMode.ISDELETED)
							&& !store.getRecordMode().equals(RecordMode.RECORD_DELETED))
							|| store.getRecordMode() == null) {

						if (employee.getRecordMode() != null && (employee.getRecordMode().equals(RecordMode.ISDELETED)
								|| employee.getRecordMode().equals(RecordMode.RECORD_DELETED))) {

							codEmployee.add(employee.getCodEmployee());

						} else {

							updateStmtT07.setInt(1,
									employee.getCodPositionStore() != null && !employee.getCodPositionStore().equals(0)
											? employee.getCodPositionStore() : employee.getCodPosition());
							updateStmtT07.setString(2, RecordMode.RECORD_OK);
							updateStmtT07.setLong(3, store.getCodOwner());
							updateStmtT07.setInt(4, store.getCodStore());
							updateStmtT07.setInt(5, employee.getCodEmployee());

							updateStmtT07.addBatch();
						}
					}
				}

				if (codMaterial != null && !codMaterial.isEmpty()) {

					deleteStmtT02 = conn.prepareStatement(materialStoreHelper.prepareDeleteToStore(store.getCodOwner(),
							codMaterial, store.getCodStore()));

					deleteStmtT02.execute();
				}

				if (codEmployee != null && !codEmployee.isEmpty()) {

					deleteStmtT03 = conn.prepareStatement(storeEmployeeHelper.prepareDeleteToStore(store.getCodOwner(),
							store.getCodStore(), codEmployee));

					deleteStmtT03.execute();
				}

				insertStmtT03.executeBatch();

				insertStmtT04.executeBatch();
			}

			updateStmtT01.executeBatch();

			updateStmtT04.executeBatch();

			updateStmtT05.executeBatch();

			updateStmtT06.executeBatch();

			updateStmtT07.executeBatch();

			conn.commit();

			return new SQLException(UPDATE_OK, null, 200);

		} catch (SQLException e) {
			try {
				conn.rollback();
				return e;
			} catch (SQLException e1) {
				return e1;
			}
		} finally {
			closeConnection(conn, deleteStmtT01, deleteStmtT02, deleteStmtT03, insertStmtT01, insertStmtT03,
					insertStmtT04, updateStmtT01, updateStmtT04, updateStmtT05, updateStmtT06, updateStmtT07,
					variableStmt);
		}
	}

	public SQLException deleteStore(List<Long> codOwner, List<Integer> codStore, List<String> cnpj,
			List<String> inscEstadual, List<String> inscMunicipal, List<String> razaoSocial, List<String> name,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state, List<String> codCurr, List<String> recordMode) {

		Connection conn = null;
		PreparedStatement deleteStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			deleteStmt = conn.prepareStatement(
					new StoreHelper().prepareDelete(codOwner, codStore, cnpj, inscEstadual, inscMunicipal, razaoSocial,
							name, address1, address2, postalcode, city, country, state, codCurr, recordMode));

			deleteStmt.execute();

			conn.commit();

			return new SQLException(DELETE_OK);

		} catch (SQLException e) {
			try {
				conn.rollback();
				return e;
			} catch (SQLException e1) {
				return e1;
			}
		} finally {
			closeConnection(conn, deleteStmt);
		}
	}

	public ArrayList<Store> selectStore(List<Long> codOwner, List<Integer> codStore, List<String> cnpj,
			List<String> inscEstadual, List<String> inscMunicipal, List<String> razaoSocial, List<String> name,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state, List<String> phone, List<String> codCurr, List<String> recordMode)
			throws SQLException {

		ArrayList<Store> storeList = new ArrayList<Store>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			StoreHelper storeHelper = new StoreHelper();

			selectStmt = conn.prepareStatement(
					storeHelper.prepareSelect(codOwner, codStore, cnpj, inscEstadual, inscMunicipal, razaoSocial, name,
							address1, address2, postalcode, city, country, state, phone, codCurr, recordMode));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				storeList.add(storeHelper.assignResult(resultSet));
			}

			return storeList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	public ArrayList<Store> selectStoreLoc(List<Long> codOwner, List<Integer> codStore, List<String> cnpj,
			List<String> inscEstadual, List<String> inscMunicipal, List<String> razaoSocial, List<String> name,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state, List<String> phone, List<String> codCurr, List<String> recordMode,
			Float latitude, Float longitude) throws SQLException {

		ArrayList<Store> storeList = new ArrayList<Store>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			StoreHelper storeHelper = new StoreHelper();

			selectStmt = conn.prepareStatement(
					storeHelper.prepareSelectLoc(codOwner, codStore, cnpj, inscEstadual, inscMunicipal, razaoSocial, name,
							address1, address2, postalcode, city, country, state, phone, codCurr, recordMode));
			
			selectStmt.setFloat(1, latitude);
			selectStmt.setFloat(2, longitude);
			selectStmt.setFloat(3, latitude);

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				storeList.add(storeHelper.assignResult(resultSet));
			}

			return storeList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	private void prepareInsertStore(PreparedStatement insertStmtT01, PreparedStatement variableStmt, Store store)
			throws SQLException {

		insertStmtT01.setLong(1, store.getCodOwner());
		insertStmtT01.setString(2, store.getCnpj());
		insertStmtT01.setString(3, store.getInscEstadual());
		insertStmtT01.setString(4, store.getInscMunicipal());
		insertStmtT01.setString(5, store.getRazaoSocial());
		insertStmtT01.setString(6, store.getName());
		insertStmtT01.setString(7, store.getAddress1());
		insertStmtT01.setString(8, store.getAddress2());
		insertStmtT01.setInt(9, store.getPostalcode());
		insertStmtT01.setString(10, store.getCity());
		insertStmtT01.setString(11, store.getCountry());
		insertStmtT01.setString(12, store.getState());
		insertStmtT01.setString(13, store.getPhone());
		insertStmtT01.setString(14, store.getCodCurr());
		insertStmtT01.setString(15, RecordMode.RECORD_OK);

		insertStmtT01.executeUpdate();
		variableStmt.execute();
		store.setCodStore(0);
	}

	private void prepareInsertMaterialStore(PreparedStatement insertStmtT03, Store store, MaterialStore material)
			throws SQLException {

		insertStmtT03.setLong(1, store.getCodOwner());
		insertStmtT03.setInt(2, material.getCodMaterial());
		insertStmtT03.setInt(3, store.getCodStore());
		insertStmtT03.setBigDecimal(4, material.getPriceStore() != null && !material.getPriceStore().equals(0)
				? material.getPriceStore() : material.getPrice());
		insertStmtT03.setInt(5, material.getDurationStore() != null && !material.getDurationStore().equals(0)
				? material.getDurationStore() : material.getDuration());
		insertStmtT03.setString(6, RecordMode.RECORD_OK);
		insertStmtT03.setString(7, material.getCodCurrStore());

		insertStmtT03.addBatch();
	}

	private void prepareInsertStoreEmployee(PreparedStatement insertStmtT04, Store store, StoreEmployee employee)
			throws SQLException {
		insertStmtT04.setLong(1, store.getCodOwner());
		insertStmtT04.setInt(2, store.getCodStore());
		insertStmtT04.setInt(3, employee.getCodEmployee());
		insertStmtT04.setInt(4, employee.getCodPositionStore() != null && !employee.getCodPositionStore().equals(0)
				? employee.getCodPositionStore() : employee.getCodPosition());
		insertStmtT04.setString(5, RecordMode.RECORD_OK);

		insertStmtT04.addBatch();
	}

}

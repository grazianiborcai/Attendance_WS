package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.EnrollmentDAO;
import br.com.mind5.helper.Enrollment;

public class EnrollmentModel extends JsonBuilder {
	
	public Response insertEnrollment(String incomingData) {

		@SuppressWarnings("unchecked")
		SQLException exception = new EnrollmentDAO()
				.insertEnrollment((ArrayList<Enrollment>) jsonToObjectList(incomingData, Enrollment.class));

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public ArrayList<Enrollment> selectEnrollment(Long classID) throws SQLException {

		return new EnrollmentDAO().selectEnrollmentText(classID);
	}

	public JsonObject selectEnrollmentJson(Long classID) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			jsonElement = new Gson().toJsonTree(selectEnrollment(classID));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectEnrollmentResponse(Long classID) {

		return response(selectEnrollmentJson(classID));
	}

}

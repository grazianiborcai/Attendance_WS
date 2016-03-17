package br.com.gda.model;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.PlanningTimeDAO;
import br.com.gda.helper.PayCart;
import br.com.gda.helper.PlanningTime;
import br.com.gda.helper.RecordMode;
import br.com.gda.helper.Reserve;

public class PlanningTimeModel extends JsonBuilder {

	public Response insertPlanningTime(String incomingData) {

		@SuppressWarnings("unchecked")
		ArrayList<PlanningTime> planningTimeList = (ArrayList<PlanningTime>) jsonToObjectList(incomingData,
				PlanningTime.class);

		SQLException exception = new PlanningTimeDAO().insertPlanningTime(planningTimeList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		List<Long> codOwner = planningTimeList.stream().map(p -> p.getCodOwner()).distinct()
				.collect(Collectors.toList());

		List<Integer> codStore = planningTimeList.stream().map(p -> p.getCodStore()).distinct()
				.collect(Collectors.toList());

		List<String> recordMode = new ArrayList<String>();
		recordMode.add(RecordMode.RECORD_OK);

		jsonObject = mergeJsonObject(jsonObject, selectPlanningTimeJson(codOwner, codStore, null, null, null, null,
				null, null, recordMode, null, null, null));

		return response(jsonObject);
	}

	public Response reservePlanningTime(String incomingData, Long customer) {

		@SuppressWarnings("unchecked")
		ArrayList<PlanningTime> planningTimeList = (ArrayList<PlanningTime>) jsonToObjectList(incomingData,
				PlanningTime.class);

		SQLException exception = new PlanningTimeDAO().reservePlanningTime(planningTimeList, customer, false);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		jsonObject.add(RESULTS, new Gson().toJsonTree(planningTimeList));

		return response(jsonObject);
	}

	public Response releasePlanningTime(String incomingData, Long customer) {

		@SuppressWarnings("unchecked")
		ArrayList<PlanningTime> planningTimeList = (ArrayList<PlanningTime>) jsonToObjectList(incomingData,
				PlanningTime.class);

		SQLException exception = new PlanningTimeDAO().releasePlanningTime(planningTimeList, customer);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public Response deletePlanningTime(List<Long> codOwner, List<Integer> codStore, List<Integer> codEmployee,
			List<String> beginDate, List<String> beginTime, List<Integer> group, List<Integer> weekday,
			List<Integer> codMaterial, List<String> recordMode) {

		SQLException exception = new PlanningTimeDAO().deletePlanningTime(codOwner, codStore, codEmployee, beginDate,
				beginTime, group, weekday, codMaterial, recordMode);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public ArrayList<PlanningTime> selectPlanningTime(List<Long> codOwner, List<Integer> codStore,
			List<Integer> codEmployee, List<String> beginDate, List<String> beginTime, List<Integer> group,
			List<Integer> weekday, List<Integer> codMaterial, List<String> recordMode, List<String> reservedTo,
			List<Long> codCustomer, List<Long> number) throws SQLException {

		ArrayList<PlanningTime> planningTime = new PlanningTimeDAO().selectPlanningTime(codOwner, codStore, codEmployee,
				beginDate, beginTime, group, weekday, codMaterial, recordMode, reservedTo, codCustomer, number);

		Predicate<PlanningTime> predicateDM = new Predicate<PlanningTime>() {

			@Override
			public boolean test(PlanningTime pTEach) {
				// TODO Auto-generated method stub

				// PlanningTime planningT = new PlanningTime(pTEach);
				if (pTEach.getRate() > 1)
					for (int i = 1; i < pTEach.getRate(); i++) {
						int part = pTEach.getPart();
						int min = part * i;
						LocalTime beginTime = pTEach.getBeginTime().plusMinutes(min);

//						PlanningTime planningT = null;
						
						Optional<PlanningTime> planningT = planningTime.stream()
								.filter(p -> p.getCodOwner().equals(pTEach.getCodOwner())
										&& p.getCodStore().equals(pTEach.getCodStore())
										&& p.getCodEmployee().equals(pTEach.getCodEmployee())
										&& p.getBeginDate().equals(pTEach.getBeginDate())
										&& p.getBeginTime().equals(beginTime))
								.findAny();

						if (!planningT.isPresent())
							return false;
					};

				return true;
			}
		};

		ArrayList<PlanningTime> planningTimeResult = (ArrayList<PlanningTime>) planningTime.stream().filter(predicateDM)
				.collect(Collectors.toList());

		return planningTimeResult;

	}

	public JsonObject selectPlanningTimeJson(List<Long> codOwner, List<Integer> codStore, List<Integer> codEmployee,
			List<String> beginDate, List<String> beginTime, List<Integer> group, List<Integer> weekday,
			List<Integer> codMaterial, List<String> recordMode, List<String> reservedTo, List<Long> codCustomer,
			List<Long> number) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			jsonElement = new Gson().toJsonTree(selectPlanningTime(codOwner, codStore, codEmployee, beginDate,
					beginTime, group, weekday, codMaterial, recordMode, reservedTo, codCustomer, number));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectPlanningTimeResponse(List<Long> codOwner, List<Integer> codStore, List<Integer> codEmployee,
			List<String> beginDate, List<String> beginTime, List<Integer> group, List<Integer> weekday,
			List<Integer> codMaterial, List<String> recordMode, List<String> reservedTo, List<Long> codCustomer,
			List<Long> number) {

		return response(selectPlanningTimeJson(codOwner, codStore, codEmployee, beginDate, beginTime, group, weekday,
				codMaterial, recordMode, reservedTo, codCustomer, number));
	}

	public ArrayList<PlanningTime> getCart(Long codCustomer) throws SQLException {

		return new PlanningTimeDAO().getCart(codCustomer);

	}

	public JsonObject getCartJson(Long codCustomer) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			jsonElement = new Gson().toJsonTree(getCart(codCustomer));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response getCartResponse(Long codCustomer) {

		return response(getCartJson(codCustomer));
	}

	public ArrayList<PlanningTime> getBooked(Long codCustomer) throws SQLException {

		return new PlanningTimeDAO().getBooked(codCustomer);

	}

	public JsonObject getBookedJson(Long codCustomer) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			jsonElement = new Gson().toJsonTree(getBooked(codCustomer));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response getBookedResponse(Long codCustomer) {

		return response(getBookedJson(codCustomer));
	}

	public Response payCart(String incomingData, Long customer) {

		@SuppressWarnings("unchecked")
		ArrayList<PayCart> payCart = (ArrayList<PayCart>) jsonToObjectList(incomingData, PayCart.class);

		if (payCart.get(0).getCreditCard() == null || !payCart.get(0).getCreditCard().isComplete()) {

			SQLException exception = new SQLException("Message erro", null, 30);

			JsonObject jsonObject = getJsonObjectUpdate(exception);

			jsonObject.add(RESULTS, new JsonArray());

			return response(jsonObject);
		}

		ArrayList<PlanningTime> planningTimeList = payCart.get(0).getPlanningTime();

		ArrayList<PlanningTime> planningTimeListAux = new ArrayList<PlanningTime>();

		planningTimeListAux.addAll(planningTimeList);

		PlanningTimeDAO planningTimeDAO = new PlanningTimeDAO();

		SQLException exception = planningTimeDAO.reservePlanningTime(planningTimeList, customer, true);

		if (exception.getErrorCode() == 200) {

			// Colocar o código de pagamento aqui

			LocalDateTime dateTime = ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime();

			ArrayList<Reserve> reserveList = new ArrayList<Reserve>();

			Reserve reserve = new Reserve();

			planningTimeListAux.stream().forEach(p -> {
				reserve.setCodOwner(p.getCodOwner());
				reserve.setCodStore(p.getCodStore());
				reserve.setCodCustomer(customer);
				reserve.setReservedTime(dateTime);
				reserve.setPayId("pay-hjhduu388eiudi349894kj");
				String num = p.getCodOwner().toString() + customer.toString() + String.valueOf(dateTime.getYear())
						+ String.valueOf(dateTime.getMonthValue()) + String.valueOf(dateTime.getDayOfMonth())
						+ String.valueOf(dateTime.getHour()) + String.valueOf(dateTime.getMinute())
						+ String.valueOf(dateTime.getSecond());
				reserve.setReservedNum(Long.valueOf(num));
				if (!reserveList.contains(reserve))
					reserveList.add(reserve);
			});

			exception = planningTimeDAO.insertReserve(reserveList);

			JsonObject jsonObject = getJsonObjectUpdate(exception);

			jsonObject.addProperty("reservedNumber", reserve.getReservedNum());

			jsonObject.add(RESULTS, new Gson().toJsonTree(planningTimeList));

			return response(jsonObject);

		} else {

			JsonObject jsonObject = getJsonObjectUpdate(exception);

			jsonObject.add(RESULTS, new Gson().toJsonTree(planningTimeList));

			return response(jsonObject);

		}
	}

	public Response refundResponse(Long codCustomer, Long number) {

		SQLException exception = new SQLException("Estorno do pagamento solicitado", null, 200);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

}

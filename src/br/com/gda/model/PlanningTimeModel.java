package br.com.gda.model;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.CustomerDAO;
import br.com.gda.dao.PlanningTimeDAO;
import br.com.gda.dao.StoreDAO;
import br.com.mind5.helper.PayCart;
import br.com.mind5.helper.PlanningTime;
import br.com.mind5.helper.RecordMode;
import br.com.mind5.helper.Reserve;
import br.com.mind5.helper.Store;
import moip.sdk.api.Amount;
import moip.sdk.api.CreditCard;
import moip.sdk.api.Customer;
import moip.sdk.api.Item;
import moip.sdk.api.MoipAccount;
import moip.sdk.api.Order;
import moip.sdk.api.Payment;
import moip.sdk.api.Phone;
import moip.sdk.api.Receiver;
import moip.sdk.api.Refund;
import moip.sdk.api.TaxDocument;
import moip.sdk.base.APIContext;

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
				null, null, recordMode, null, null, null, null, null));

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

		SQLException exception = releasePTime(planningTimeList, customer, RecordMode.ISRESERVED);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	private SQLException releasePTime(ArrayList<PlanningTime> planningTimeList, Long customer, String recordMode) {

		SQLException exception = new PlanningTimeDAO().releasePlanningTime(planningTimeList, customer, recordMode);
		return exception;
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
			List<Long> codCustomer, List<Long> number, String iniDate, String finDate) throws SQLException {

		ArrayList<PlanningTime> planningTime = new PlanningTimeDAO().selectPlanningTime(codOwner, codStore, codEmployee,
				beginDate, beginTime, group, weekday, codMaterial, recordMode, reservedTo, codCustomer, number, iniDate,
				finDate);

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

						// PlanningTime planningT = null;

						Optional<PlanningTime> planningT = planningTime.stream()
								.filter(p -> p.getCodOwner().equals(pTEach.getCodOwner())
										&& p.getCodStore().equals(pTEach.getCodStore())
										&& p.getCodEmployee().equals(pTEach.getCodEmployee())
										&& p.getBeginDate().equals(pTEach.getBeginDate())
										&& p.getBeginTime().equals(beginTime))
								.findAny();

						if (!planningT.isPresent())
							return false;
					}
				;

				return true;
			}
		};

		ArrayList<PlanningTime> planningTimeResult = (ArrayList<PlanningTime>) planningTime.stream().filter(predicateDM)
				.collect(Collectors.toList());

		return planningTimeResult;

	}

	public ArrayList<PlanningTime> selectPlanningTimeLoc(List<Long> codOwner, List<Integer> codStore,
			List<Integer> codEmployee, List<String> beginDate, List<String> beginTime, List<Integer> group,
			List<Integer> weekday, List<Integer> codMaterial, List<String> recordMode, List<String> reservedTo,
			List<Long> codCustomer, List<Long> number, String iniDate, String finDate, Float latitude, Float longitude)
			throws SQLException {

		ArrayList<PlanningTime> planningTime = new PlanningTimeDAO().selectPlanningTimeLoc(codOwner, codStore,
				codEmployee, beginDate, beginTime, group, weekday, codMaterial, recordMode, reservedTo, codCustomer,
				number, iniDate, finDate, latitude, longitude);

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

						// PlanningTime planningT = null;

						Optional<PlanningTime> planningT = planningTime.stream()
								.filter(p -> p.getCodOwner().equals(pTEach.getCodOwner())
										&& p.getCodStore().equals(pTEach.getCodStore())
										&& p.getCodEmployee().equals(pTEach.getCodEmployee())
										&& p.getBeginDate().equals(pTEach.getBeginDate())
										&& p.getBeginTime().equals(beginTime))
								.findAny();

						if (!planningT.isPresent())
							return false;
					}
				;

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
			List<Long> number, String iniDate, String finDate) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			jsonElement = new Gson()
					.toJsonTree(selectPlanningTime(codOwner, codStore, codEmployee, beginDate, beginTime, group,
							weekday, codMaterial, recordMode, reservedTo, codCustomer, number, iniDate, finDate));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public JsonObject selectPlanningTimeJsonLoc(List<Long> codOwner, List<Integer> codStore, List<Integer> codEmployee,
			List<String> beginDate, List<String> beginTime, List<Integer> group, List<Integer> weekday,
			List<Integer> codMaterial, List<String> recordMode, List<String> reservedTo, List<Long> codCustomer,
			List<Long> number, String iniDate, String finDate, Float latitude, Float longitude) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			jsonElement = new Gson().toJsonTree(selectPlanningTimeLoc(codOwner, codStore, codEmployee, beginDate,
					beginTime, group, weekday, codMaterial, recordMode, reservedTo, codCustomer, number, iniDate,
					finDate, latitude, longitude));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectPlanningTimeResponse(List<Long> codOwner, List<Integer> codStore, List<Integer> codEmployee,
			List<String> beginDate, List<String> beginTime, List<Integer> group, List<Integer> weekday,
			List<Integer> codMaterial, List<String> recordMode, List<String> reservedTo, List<Long> codCustomer,
			List<Long> number, String iniDate, String finDate) {

		return response(selectPlanningTimeJson(codOwner, codStore, codEmployee, beginDate, beginTime, group, weekday,
				codMaterial, recordMode, reservedTo, codCustomer, number, iniDate, finDate));
	}

	public Response selectPlanningTimeResponseLoc(List<Long> codOwner, List<Integer> codStore,
			List<Integer> codEmployee, List<String> beginDate, List<String> beginTime, List<Integer> group,
			List<Integer> weekday, List<Integer> codMaterial, List<String> recordMode, List<String> reservedTo,
			List<Long> codCustomer, List<Long> number, String iniDate, String finDate, Float latitude, Float longitude) {

		return response(selectPlanningTimeJsonLoc(codOwner, codStore, codEmployee, beginDate, beginTime, group, weekday,
				codMaterial, recordMode, reservedTo, codCustomer, number, iniDate, finDate, latitude, longitude));
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

	public Response payCart(String incomingData, Long customer, String codPayment, String phone) {

		@SuppressWarnings("unchecked")
		ArrayList<PayCart> payCart = (ArrayList<PayCart>) jsonToObjectList(incomingData, PayCart.class);

		if (payCart.get(0).getCreditCard() == null || !payCart.get(0).isComplete()) {

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

			LocalDateTime dateTime = ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime();

			// Place the payment code here

			ConcurrentHashMap<String, String> sdkConfig = new ConcurrentHashMap<String, String>();
			sdkConfig.put("mode", "sandbox");

			APIContext apiContext = new APIContext("8QLV3TOXIP0AND15ZOB5R4X5T0OYWHVR",
					"GLYGGCHTSEQO0LCUL9IJNQTEGNG2NZOHA53VRGYC", "OAuth cl6fpbl7fyqiqljnd8apq75satol8q9");
			apiContext.setConfigurationMap(sdkConfig);

			List<Integer> codStore = planningTimeListAux.stream().map(p -> p.getCodStore()).distinct()
					.collect(Collectors.toList());

			ArrayList<Store> storeList = null;
			try {
				storeList = new StoreDAO().selectStore(null, codStore, null, null, null, null, null, null, null, null,
						null, null, null, null, null, null);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JsonObject jsonObject = getJsonObjectUpdate(e);
				return response(jsonObject);
			}

			String numMulti = customer.toString() + String.valueOf(dateTime.getYear())
					+ String.valueOf(dateTime.getMonthValue()) + String.valueOf(dateTime.getDayOfMonth())
					+ String.valueOf(dateTime.getHour()) + String.valueOf(dateTime.getMinute())
					+ String.valueOf(dateTime.getSecond());

			if (codPayment == null || codPayment.isEmpty()) {
				List<Long> codCustomer = new ArrayList<Long>();
				codCustomer.add(customer);
				ArrayList<br.com.mind5.helper.Customer> customerList = null;
				ArrayList<br.com.mind5.helper.Customer> customerUpdatedList = null;
				try {
					CustomerModel customerModel = new CustomerModel();
					customerList = customerModel.selectCustomer(codCustomer, null, null, null, null, null, null, null,
							null, null, null, null, null, null);
					customerUpdatedList = customerModel.createMoipCustomer(customerList);
					codPayment = customerUpdatedList.get(0).getCodPayment();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					JsonObject jsonObject = getJsonObjectUpdate(e);
					return response(jsonObject);
				}

			}

			Order multiOrder = new Order(APIContext.MULTI).setOwnId(numMulti)
					.setCustomer(new Customer().setId(codPayment));

			Order order = null;

			int i = 0;
			Store storeBefore = null;
			BigDecimal t0 = BigDecimal.valueOf(0);
			BigDecimal t;
			BigDecimal t2;
			int ti = 0;
			int preco = 0;
			int tp = 0;
			for (PlanningTime planningTime : planningTimeListAux) {
				Store store = storeList.stream().filter(p -> p.getCodOwner().equals(planningTime.getCodOwner())
						&& p.getCodStore().equals(planningTime.getCodStore())).findAny().get();

				if (storeBefore == null || !store.getCodStore().equals(storeBefore.getCodStore())) {
					if (storeBefore != null) {

						if (i == 1) {
							t = t0.subtract(BigDecimal.valueOf(2));
							t2 = t.multiply(BigDecimal.valueOf(100));

							ti = t2.intValue();
							tp = t2.intValue();
							ti = (int) (ti - (ti * 0.068));
						} else if (i > 1) {
							// t =
							// planningTime.getPriceStore().subtract(BigDecimal.valueOf(2));
							t2 = t0.multiply(BigDecimal.valueOf(100));

							ti = t2.intValue();
							tp = t2.intValue();
							ti = (int) (ti - (ti * 0.068));
						}

						preco = tp - ti;

						order.addReceiver(new Receiver().setMoipAccount(new MoipAccount().setId("MPA-3RDTT72OP4G9"))
								.setType("PRIMARY").setAmount(new Amount().setFixed(preco)));
						// if (i == 0).setAmount(new Amount().setFixed(preco))
						order.addReceiver(new Receiver().setMoipAccount(new MoipAccount().setId(store.getCodPayment()))
								.setType("SECONDARY").setAmount(new Amount().setFixed(ti)));

						multiOrder.addOrder(order);

					}

					String num = planningTime.getCodOwner().toString() + planningTime.getCodStore().toString()
							+ customer.toString() + String.valueOf(dateTime.getYear())
							+ String.valueOf(dateTime.getMonthValue()) + String.valueOf(dateTime.getDayOfMonth())
							+ String.valueOf(dateTime.getHour()) + String.valueOf(dateTime.getMinute())
							+ String.valueOf(dateTime.getSecond());

					order = new Order();
					order.setOwnId(num);

					i++;
					t0 = BigDecimal.valueOf(0);
				}

				// BigDecimal t =
				// planningTime.getPriceStore().subtract(BigDecimal.valueOf(2));
				BigDecimal t3 = planningTime.getPriceStore().multiply(BigDecimal.valueOf(100));

				int ti2 = t3.intValue();

				order.addItem(new Item().setProduct(planningTime.getCodMaterial().toString()).setQuantity(1)
						.setDetail(planningTime.getCodMaterial().toString()).setPrice(ti2));

				storeBefore = store;

				t0 = t0.add(planningTime.getPriceStore());

			}

			if (i == 1) {
				t = t0.subtract(BigDecimal.valueOf(2));
				t2 = t.multiply(BigDecimal.valueOf(100));

				ti = t2.intValue();
				tp = t2.intValue();
				ti = (int) (ti - (ti * 0.068));
			} else if (i > 1) {
				// t =
				// planningTime.getPriceStore().subtract(BigDecimal.valueOf(2));
				t2 = t0.multiply(BigDecimal.valueOf(100));

				ti = t2.intValue();
				tp = t2.intValue();
				ti = (int) (ti - (ti * 0.068));
			}
			//

			preco = tp - ti;

			order.addReceiver(new Receiver().setMoipAccount(new MoipAccount().setId("MPA-3RDTT72OP4G9"))
					.setType("PRIMARY").setAmount(new Amount().setFixed(preco)));
			// .setAmount(new Amount().setFixed(preco))
			order.addReceiver(new Receiver().setMoipAccount(new MoipAccount().setId(storeBefore.getCodPayment()))
					.setType("SECONDARY").setAmount(new Amount().setFixed(ti)));

			multiOrder.addOrder(order);

			Order multiOrderCreated = null;
			Payment paymentCreated = null;

			CreditCard creditCard = payCart.get(0).getCreditCard();

			Phone phoneCreditCard = new Phone().setCountryCode("55").setAreaCode(phone.substring(1, 3))
					.setNumber(phone.substring(5, 14));

			creditCard.getHolder().setPhone(phoneCreditCard);

			moip.sdk.api.FundingInstrument fundingInstrument = new moip.sdk.api.FundingInstrument();
			fundingInstrument.setCreditCard(creditCard);

			try {
				multiOrderCreated = multiOrder.create(apiContext);
				if (multiOrderCreated.getId() == null)
					if (multiOrderCreated.getErrors() != null)
						throw new SQLException(multiOrderCreated.getErrors().get(0).getDescription(), null, 44);
					else if (multiOrderCreated.getERROR() != null)
						throw new SQLException(multiOrderCreated.getERROR(), null, 44);
				paymentCreated = new Payment(APIContext.MULTI, multiOrderCreated.getId()).setInstallmentCount(1)
						.setFundingInstrument(fundingInstrument).createAndAuthorized(apiContext);
				if (paymentCreated.getId() == null)
					if (paymentCreated.getErrors() != null)
						throw new SQLException(paymentCreated.getErrors().get(0).getDescription(), null, 44);
					else if (paymentCreated.getERROR() != null)
						throw new SQLException(paymentCreated.getERROR(), null, 44);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				exception = new SQLException(e.getMessage(), null, 55);
				JsonObject jsonObject = getJsonObjectUpdate(exception);
				return response(jsonObject);
			} catch (SQLException e) {
				JsonObject jsonObject = getJsonObjectUpdate(e);
				return response(jsonObject);
			}

			List<Order> orderList = multiOrderCreated.getOrders();

			ArrayList<Reserve> reserveList = new ArrayList<Reserve>();

			Reserve reserve = new Reserve();

			i = 0;
			storeBefore = null;
			for (PlanningTime planningTime : planningTimeListAux) {
				Store store = storeList.stream().filter(p -> p.getCodOwner().equals(planningTime.getCodOwner())
						&& p.getCodStore().equals(planningTime.getCodStore())).findAny().get();

				if (storeBefore == null || !store.getCodStore().equals(storeBefore.getCodStore())) {

					reserve.setCodOwner(planningTime.getCodOwner());
					reserve.setCodStore(planningTime.getCodStore());
					reserve.setCodCustomer(customer);
					reserve.setReservedTime(dateTime);
					String payId = orderList.get(i).getId();
					reserve.setPayId(payId);
					// String num = planningTime.getCodOwner().toString() +
					// customer.toString() + String.valueOf(dateTime.getYear())
					// + String.valueOf(dateTime.getMonthValue()) +
					// String.valueOf(dateTime.getDayOfMonth())
					// + String.valueOf(dateTime.getHour()) +
					// String.valueOf(dateTime.getMinute())
					// + String.valueOf(dateTime.getSecond());
					reserve.setReservedNum(paymentCreated.getId());
					if (!reserveList.contains(reserve)) {
						reserveList.add(reserve);
					}
					i++;
				}

				storeBefore = store;
			}

			// planningTimeList.stream().forEach(p -> {
			// reserve.setCodOwner(p.getCodOwner());
			// reserve.setCodStore(p.getCodStore());
			// reserve.setCodCustomer(customer);
			// reserve.setReservedTime(dateTime);
			// reserve.setPayId("pay-hjhduu388eiudi349894kj");
			// String num = p.getCodOwner().toString() + customer.toString() +
			// String.valueOf(dateTime.getYear())
			// + String.valueOf(dateTime.getMonthValue()) +
			// String.valueOf(dateTime.getDayOfMonth())
			// + String.valueOf(dateTime.getHour()) +
			// String.valueOf(dateTime.getMinute())
			// + String.valueOf(dateTime.getSecond());
			// reserve.setReservedNum(Long.valueOf(num));
			// if (!reserveList.contains(reserve)) {
			// reserveList.add(reserve);
			// }
			// });

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

	public Response refundResponse(String incomingData, Long codCustomer) {

		@SuppressWarnings("unchecked")
		ArrayList<PlanningTime> planningTimeList = (ArrayList<PlanningTime>) jsonToObjectList(incomingData,
				PlanningTime.class);

		ConcurrentHashMap<String, String> sdkConfig = new ConcurrentHashMap<String, String>();
		sdkConfig.put("mode", "sandbox");

		APIContext apiContext = new APIContext("8QLV3TOXIP0AND15ZOB5R4X5T0OYWHVR",
				"GLYGGCHTSEQO0LCUL9IJNQTEGNG2NZOHA53VRGYC", "OAuth cl6fpbl7fyqiqljnd8apq75satol8q9");
		apiContext.setConfigurationMap(sdkConfig);

		Refund refund = null;
		SQLException exception = null;
		try {
			refund = new Refund(planningTimeList.get(0).getPayId()).create(apiContext);
			exception = releasePTime(planningTimeList, codCustomer, RecordMode.ISPAYED);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			exception = new SQLException(e.getMessage(), null, 5000);
		}

		JsonObject jsonObject = getJsonObjectUpdate(exception);
		jsonObject.addProperty("refund", refund.getId());

		return response(jsonObject);
	}

}

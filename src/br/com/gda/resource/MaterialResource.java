package br.com.gda.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.multipart.FormDataMultiPart;

import br.com.gda.model.MaterialModel;

@Path("/Material")
public class MaterialResource {

	private static final String INSERT_MATERIAL = "/insertMaterial";
	private static final String UPDATE_MATERIAL = "/updateMaterial";
	private static final String DELETE_MATERIAL = "/deleteMaterial";
	private static final String SELECT_MATERIAL = "/selectMaterial";
	private static final String INSERT_MATERIAL_WITH_IMAGE = "/insertMaterialWithImage";
	private static final String UPDATE_MATERIAL_WITH_IMAGE = "/updateMaterialWithImage";

	@POST
	@Path(INSERT_MATERIAL)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertMaterial(String incomingData) {

		return new MaterialModel().insertMaterial(incomingData);
	}

	@POST
	@Path(INSERT_MATERIAL_WITH_IMAGE)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response insertMaterialWithImage(FormDataMultiPart multiPart) {

		return new MaterialModel().insertMaterialWithImage(multiPart);
	}

	@POST
	@Path(UPDATE_MATERIAL)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMaterial(String incomingData) {

		return new MaterialModel().updateMaterial(incomingData);
	}

	@POST
	@Path(UPDATE_MATERIAL_WITH_IMAGE)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response updateMaterialWithImage(FormDataMultiPart multiPart) {

		return new MaterialModel().updateMaterialWithImage(multiPart);
	}

	@DELETE
	@Path(DELETE_MATERIAL)
	public Response deleteMaterial(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codMaterial") List<Integer> codMaterial, @QueryParam("codCategory") List<Integer> codCategory,
			@QueryParam("codType") List<Integer> codType, @QueryParam("image") List<String> image,
			@QueryParam("barCode") List<String> barCode, @QueryParam("recordMode") List<String> recordMode) {

		return new MaterialModel().deleteMaterial(codOwner, codMaterial, codCategory, codType, image, barCode,
				recordMode);
	}

	@GET
	@Path(SELECT_MATERIAL)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectMaterial(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codMaterial") List<Integer> codMaterial, @QueryParam("codCategory") List<Integer> codCategory,
			@QueryParam("codType") List<Integer> codType, @QueryParam("image") List<String> image,
			@QueryParam("barCode") List<String> barCode,
			@DefaultValue(" ") @QueryParam("recordMode") List<String> recordMode,
			@QueryParam("language") List<String> language, @QueryParam("name") List<String> name,
			@QueryParam("description") List<String> description, @QueryParam("textLong") List<String> textLong) {

		// System.out.println("*******************" + ZonedDateTime.now());
		// List<WebSocketHandler> socketList = WebSocketHandler.sockets.get(01);
		// if (socketList != null)
		// for (WebSocketHandler socket : socketList) {
		// try {
		// socket.session.getBasicRemote().sendText(
		// "Material Updated and informed to Store 01");
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// System.out.println("*******************" + ZonedDateTime.now());

		Response response = new MaterialModel().selectMaterialResponse(codOwner, codMaterial, codCategory, codType,
				image, barCode, recordMode, language, name, description, textLong);

		// Thread t = new Thread(new Runnable() {
		// public void run(){
		// ServletContainerGDA.publisher.publishMessage(response.getEntity().toString(),
		// "home/message/");
		// }
		// });
		// t.start();

		// try {
		// t.join();
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// ServletContainerGDA.publisher.publishMessage(response.getEntity().toString(),
		// "home/message/");
		// for (int i = 0; i < 500; i++) {
		// ServletContainerGDA.publisher.publishMessage("Teste01",
		// "home/message/");
		// }

		// ExchangeRate rate =
		// ServletContainerGDA.exchangeRateProvider.getExchangeRate("USD",
		// "BRL");
		// System.out.println("BRL -> USD (today) -> " +
		// rate.getFactor().toString());
		//
		// ExchangeRate exchangeRate =
		// ServletContainerGDA.exchangeRateProvider.getExchangeRate(ConversionQueryBuilder.of()
		// .setBaseCurrency("USD").setTermCurrency("BRL").set(LocalDate.of(2015,
		// 10, 1)).build());
		//
		// Double db = null;
		// if (exchangeRate != null)
		// db = exchangeRate.getFactor().doubleValue();
		//
		// System.out.println("BRL -> USD (1.10.2015) -> " + db);

		// Collection<CurrencyUnit> units= Monetary.getCurrencies("IDENT",
		// "ECB", "ECB-HIST", "ECB-HIST90");
		//
		// for (CurrencyUnit currencyUnit : units) {
		// System.out.println(currencyUnit.getCurrencyCode());
		// }

		return response;
	}

}

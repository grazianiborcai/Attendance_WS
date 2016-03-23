package br.com.gda.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Security;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.paypal.api.payments.CreditCardToken;
import com.paypal.api.payments.FundingInstrument;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

import moip.sdk.api.CreditCard;
import moip.sdk.api.Customer;
import moip.sdk.api.Payment;
import moip.sdk.api.Phone;
import moip.sdk.api.TaxDocument;
import moip.sdk.base.APIContext;

//Testanto segundo commit

@Path("/PaymentTest")
public class PaymentoTest {

	private static final String GET_TIME = "getTime";

	@GET
	@Path(GET_TIME)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectTime() {

		// Map<String, String> sdkConfig = new HashMap<String, String>();
		// sdkConfig.put("mode", "sandbox");
		//
		// String accessToken = null;
		//
		// Payment createdPayment = null;
		// try {
		// accessToken = new OAuthTokenCredential(
		// "AfwtOtnV9Mef9qHO3WmF57KaaCUn_OdPUYPcjcrsoL9Y9JVF0ojF3lbITPi3m73ebBc1S5b268ibxFwZ",
		// "EJoNhe0jZQHEgT8fwEeTUJMLF9IQigwdjwHmYpBTF4OTB7oimIv4QtfGQhkZWEY_lyY98R0a586G-uLo",
		// sdkConfig)
		// .getAccessToken();
		//
		// APIContext apiContext = new APIContext(accessToken);
		// apiContext.setConfigurationMap(sdkConfig);
		//
		// CreditCard creditCard = new CreditCard();
		// creditCard.setType("visa");
		// creditCard.setNumber("4446283280247004");
		// creditCard.setExpireMonth(11);
		// creditCard.setExpireYear(2018);
		// creditCard.setFirstName("Joe");
		// creditCard.setLastName("Shopper");
		// // CreditCard createdCreditCard = creditCard.create(apiContext);
		//
		// // CreditCardToken creditCardToken = new CreditCardToken();
		// // creditCardToken.setCreditCardId(createdCreditCard.getId());
		//
		// FundingInstrument fundingInstrument = new FundingInstrument();
		// // fundingInstrument.setCreditCardToken(creditCardToken);
		// fundingInstrument.setCreditCard(creditCard);
		//
		// List<FundingInstrument> fundingInstrumentList = new
		// ArrayList<FundingInstrument>();
		// fundingInstrumentList.add(fundingInstrument);
		//
		// Amount amount = new Amount();
		// amount.setCurrency("BRL");
		// amount.setTotal("12");
		//
		// Transaction transaction = new Transaction();
		// transaction.setDescription("creating a payment");
		// transaction.setAmount(amount);
		//
		// List<Transaction> transactions = new ArrayList<Transaction>();
		// transactions.add(transaction);
		//
		// Payer payer = new Payer();
		// payer.setFundingInstruments(fundingInstrumentList);
		// payer.setPaymentMethod("credit_card");
		//
		// Payment payment = new Payment();
		// payment.setIntent("sale");
		// payment.setPayer(payer);
		// payment.setTransactions(transactions);
		//
		// createdPayment = payment.create(apiContext);
		// } catch (PayPalRESTException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		ConcurrentHashMap<String, String> sdkConfig = new ConcurrentHashMap<String, String>();
		sdkConfig.put("mode", "sandbox");

		APIContext apiContext = new APIContext("8QLV3TOXIP0AND15ZOB5R4X5T0OYWHVR",
				"GLYGGCHTSEQO0LCUL9IJNQTEGNG2NZOHA53VRGYC", "OAuth cl6fpbl7fyqiqljnd8apq75satol8q9");
		apiContext.setConfigurationMap(sdkConfig);

		Phone phone = new Phone();
		phone.setCountryCode("55");
		phone.setAreaCode("21");
		phone.setNumber("981273533");

		TaxDocument taxDocument = new TaxDocument();
		taxDocument.setType("CPF");
		taxDocument.setNumber("22222222222");

		Customer customer = new Customer();
		customer.setPhone(phone);
		customer.setTaxDocument(taxDocument);
		customer.setFullname("José Aldo");
		customer.setBirthdate("1984-03-15");

		CreditCard creditCard = new CreditCard();
		creditCard.setHash(
				"FSFKSdEtUSJBdf5rANxQ2cqlWI74rRb7PX8PzwdZqWDN6zTSxDQIay9ThokIKa8zbXaDwTHXD8Wp9YEhDpomZBvyli9C6UHSfrJKAIP/RLzn7C8IrPuY3FTWq0eFj1nDSgrIyDgW5mQjFdpzyUXHNAuyQReKMu3nOO6U+uDBWk8EesQYDVQoA4TOFPWyKHXPQHr/0J//DipiUh6gj0i+x7NsHC9z41U5M6Q+srsmCldOQIVndD1xsHKNDx4gYYRRrMmkbRbY4GFQ2GQCFhreElpwcTXAuLVshjzqyRUzNCwT5sXKMQa6Fub2TzEENhmMWjBqN2BojaFt8f4tOgpx8w==");
		creditCard.setHolder(customer);
		
		moip.sdk.api.FundingInstrument fundingInstrument = new moip.sdk.api.FundingInstrument();
		fundingInstrument.setCreditCard(creditCard);
		
		Payment payment = new Payment(Payment.MULTI, "MOR-NFBMKJWA52JW");
		payment.setInstallmentCount(1);
		payment.setFundingInstrument(fundingInstrument);
		Payment paymentCreated = payment.create(apiContext);
		
		String resp = new Gson().toJsonTree(paymentCreated).toString();
		
//		CloseableHttpClient httpClient = HttpClients.createDefault();
//		HttpGet httpGet = new HttpGet("https://sandbox.moip.com.br/v2/customers/CUS-XALXPTOGIZM6");
//		httpGet.addHeader("Content-Type", "application/json");
//		httpGet.addHeader("Authorization",
//				"Basic OFFMVjNUT1hJUDBBTkQxNVpPQjVSNFg1VDBPWVdIVlI6R0xZR0dDSFRTRVFPMExDVUw5SUpOUVRFR05HMk5aT0hBNTNWUkdZQw==");
//
//		CloseableHttpResponse httpResponse;
//
//		StringBuffer response = new StringBuffer();
//		try {
//			httpResponse = httpClient.execute(httpGet);
//
//			// System.out.println("GET Response Status:: " +
//			// httpResponse.getStatusLine().getStatusCode());
//
//			BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
//
//			String inputLine;
//
//			while ((inputLine = reader.readLine()) != null) {
//				response.append(inputLine);
//			}
//			reader.close();
//
//			// print result
//			// System.out.println(response.toString());
//			httpClient.close();
//
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// JsonObject jsonObject = new JsonObject();
		// jsonObject.addProperty("zoneId",
		// "Created payment with id = " + createdPayment.getId() + " and status
		// = " + createdPayment.getState());
		// jsonObject.addProperty("time",
		// ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime().toString());

		//
		// final String PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n"+
		// "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp/A2eCh7W+gxbYXLFR8D\n"+
		// "aTEDZDONK87zKclPPewICYYBBIFEuDqLe+RNvkgXSPjYmkTvd3ENcD24edHAvh9j\n"+
		// "7a+qDlLtnQ6LRhb8Uj+S/oVQHAkfeN/BsKGoTrJb2/mQFNtk0ZwZTo4jXZ3htZiG\n"+
		// "GCpGs/VNTVpVcGvjMq1YMlwhyC9J8S3B/h0ycPlbpJg8l7nBm0d1vJcupHVcbXQn\n"+
		// "0S5PaLbwFq2rLAsgJRGmbjtgfqKWX6yQeeN8aoS1gZb77QWXSGL+k1adaDACX0Cs\n"+
		// "dFWT7A2FlXmTT5YsB+LE2FodnyfHWMSAW606f7bFteFQdyITD8VWsgk1oS1KvabW\n"+
		// "8QIDAQAB\n"+
		// "-----END PUBLIC KEY-----";
		//
		//
		//
		//
		//
		//
		//
		//
		//
		//
		// CreditCard creditCard = new CreditCard();
		// creditCard.setCvc("123");
		// creditCard.setNumber("4340948565343648");
		// creditCard.setExpirationMonth("12");
		// creditCard.setExpirationYear("2030");
		// creditCard.setPublicKey(PUBLIC_KEY);
		//
		// String hash = null;
		//
		// Security.addProvider(new BouncyCastleProvider());
		//
		// try{
		// hash = creditCard.encrypt();
		// }catch(MoipEncryptionException mee){
		//
		// }
		//
		// JsonObject jsonObject = new JsonObject();
		// jsonObject.addProperty("zoneId", hash);
		// jsonObject.addProperty("time",
		// ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime().toString());
		//

		// return
		// Response.status(Response.Status.OK).entity(jsonObject.toString()).type(MediaType.APPLICATION_JSON)
		// .build();

//		return Response.status(Response.Status.OK).entity(response.toString()).type(MediaType.APPLICATION_JSON).build();
		
		return Response.status(Response.Status.OK).entity(resp).type(MediaType.APPLICATION_JSON).build();

	}

}
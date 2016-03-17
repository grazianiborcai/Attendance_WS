package br.com.gda.resource;

import java.security.Security;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.CreditCard;
import com.paypal.api.payments.CreditCardToken;
import com.paypal.api.payments.FundingInstrument;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

@Path("/PaymentTest")
public class PaymentoTest {

	private static final String GET_TIME = "getTime";

	@GET
	@Path(GET_TIME)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectTime() {
		
		Map<String, String> sdkConfig = new HashMap<String, String>();
		sdkConfig.put("mode", "sandbox");

		String accessToken = null;
		
		Payment createdPayment = null;
		try {
			accessToken = new OAuthTokenCredential("AfwtOtnV9Mef9qHO3WmF57KaaCUn_OdPUYPcjcrsoL9Y9JVF0ojF3lbITPi3m73ebBc1S5b268ibxFwZ", "EJoNhe0jZQHEgT8fwEeTUJMLF9IQigwdjwHmYpBTF4OTB7oimIv4QtfGQhkZWEY_lyY98R0a586G-uLo", sdkConfig).getAccessToken();
			
			APIContext apiContext = new APIContext(accessToken);
			apiContext.setConfigurationMap(sdkConfig);
			
			CreditCard creditCard = new CreditCard();
			creditCard.setType("visa");
			creditCard.setNumber("4446283280247004");
			creditCard.setExpireMonth(11);
			creditCard.setExpireYear(2018);
			creditCard.setFirstName("Joe");
			creditCard.setLastName("Shopper");
//			CreditCard createdCreditCard = creditCard.create(apiContext);
			
//			CreditCardToken creditCardToken = new CreditCardToken();
//			creditCardToken.setCreditCardId(createdCreditCard.getId());

			FundingInstrument fundingInstrument = new FundingInstrument();
//			fundingInstrument.setCreditCardToken(creditCardToken);
			fundingInstrument.setCreditCard(creditCard);

			List<FundingInstrument> fundingInstrumentList = new ArrayList<FundingInstrument>();
			fundingInstrumentList.add(fundingInstrument);

			Amount amount = new Amount();
			amount.setCurrency("BRL");
			amount.setTotal("12");

			Transaction transaction = new Transaction();
			transaction.setDescription("creating a payment");
			transaction.setAmount(amount);

			List<Transaction> transactions = new ArrayList<Transaction>();
			transactions.add(transaction);

			Payer payer = new Payer();
			payer.setFundingInstruments(fundingInstrumentList);
			payer.setPaymentMethod("credit_card");

			Payment payment = new Payment();
			payment.setIntent("sale");
			payment.setPayer(payer);
			payment.setTransactions(transactions);

			createdPayment = payment.create(apiContext);
		} catch (PayPalRESTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("zoneId", "Created payment with id = " + createdPayment.getId()
		+ " and status = " + createdPayment.getState());
		jsonObject.addProperty("time", ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime().toString());
//		
//		final String PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n"+
//		        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp/A2eCh7W+gxbYXLFR8D\n"+
//		        "aTEDZDONK87zKclPPewICYYBBIFEuDqLe+RNvkgXSPjYmkTvd3ENcD24edHAvh9j\n"+
//		        "7a+qDlLtnQ6LRhb8Uj+S/oVQHAkfeN/BsKGoTrJb2/mQFNtk0ZwZTo4jXZ3htZiG\n"+
//		        "GCpGs/VNTVpVcGvjMq1YMlwhyC9J8S3B/h0ycPlbpJg8l7nBm0d1vJcupHVcbXQn\n"+
//		        "0S5PaLbwFq2rLAsgJRGmbjtgfqKWX6yQeeN8aoS1gZb77QWXSGL+k1adaDACX0Cs\n"+
//		        "dFWT7A2FlXmTT5YsB+LE2FodnyfHWMSAW606f7bFteFQdyITD8VWsgk1oS1KvabW\n"+
//		        "8QIDAQAB\n"+
//		        "-----END PUBLIC KEY-----";
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
//		CreditCard creditCard = new CreditCard();
//        creditCard.setCvc("123");
//        creditCard.setNumber("4340948565343648");
//        creditCard.setExpirationMonth("12");
//        creditCard.setExpirationYear("2030");
//        creditCard.setPublicKey(PUBLIC_KEY);
//        
//        String hash = null;
//        
//        Security.addProvider(new BouncyCastleProvider());
//        
//        try{
//            hash = creditCard.encrypt();
//        }catch(MoipEncryptionException mee){
//
//        }
//		
//		JsonObject jsonObject = new JsonObject();
//		jsonObject.addProperty("zoneId", hash);
//		jsonObject.addProperty("time", ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime().toString());
//
		return Response.status(Response.Status.OK).entity(jsonObject.toString()).type(MediaType.APPLICATION_JSON)
				.build();

	}

}
package br.com.gda.model;

import java.sql.SQLException;
import java.util.Random;

import javax.ws.rs.core.Response;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import com.google.gson.JsonObject;

public class CodePasswordModel extends JsonBuilder {

	public JsonObject getCodeJson(String email, String password, String name) {

		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		Random rand = new Random();

		int randomNum = rand.nextInt((10000 - 1000) + 1) + 1000;
		 
		 String msgBody = "\nPrezado " + name + ",";
		 msgBody+= "\n\nPara alterar sua senha, informe o código abaixo no aplicativo e então estará autorizado a definir uma nova senha.";
		 msgBody+= "\n\nCódigo: "+randomNum;
		 msgBody+= "\n\n\nAtenciosamente,";
		 msgBody+= "\n\nSuporte Mind5";
		 msgBody+= "\n\nNão resposda esse email, caso não consiga redefinir sua senha entre em contato com suporte@mind5.com.br ";

		try {
			Email email2 = new SimpleEmail();
//			email2.setHostName("smtp.googlemail.com");
			email2.setHostName("email-ssl.com.br");
//			email2.setSmtpPort(465);
			email2.setSmtpPort(587);
//			email2.setSmtpPort(25);
			email2.setAuthenticator(new DefaultAuthenticator("admin@mind5.com.br", "Mind5@web"));
//			email2.setSSLOnConnect(true);
//			email2.setSSLCheckServerIdentity(true);
			email2.setStartTLSEnabled(true);
			email2.setStartTLSRequired(true);
			email2.setFrom("admin@mind5.com.br");
			email2.setSubject("[MIND5] Mudança de senha");
			email2.setMsg(msgBody);
			email2.addTo(email);
//			email2.addTo("gbastos@mind5.com.br");
//			email2.addTo("portugal@mind5.com.br");
//			email2.addTo("mmaciel@mind5.com.br");
//			email2.addTo("rfonseca@mind5.com.br");
			email2.send();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JsonObject jsonObject = getJsonObjectUpdate(exception);
		jsonObject.addProperty("code", randomNum);

		return jsonObject;
	}

	public Response getCodeResponse(String email, String password, String name) {

		return response(getCodeJson(email, password, name));
	}

}

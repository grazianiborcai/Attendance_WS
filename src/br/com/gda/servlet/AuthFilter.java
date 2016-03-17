package br.com.gda.servlet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import br.com.gda.dao.CustomerDAO;
import br.com.gda.encrypt.MD5;
import br.com.gda.helper.Customer;
import br.com.gda.helper.Owner;
import br.com.gda.model.OwnerModel;

public class AuthFilter implements ContainerRequestFilter {

	private static final String CLIENT_LOGIN = "client";

	// Header parameters key
	private static final String AUTHORIZATION = "authorization";
	private static final String TOKENREQ = "token";
	private static final String APP = "app";
	private static final String ZONE_ID = "zoneId";

	// Parameter that is passed in the HTTP header parameter
	private static final String COD_OWNER = "codOwner";
	private static final String PHONE = "phone";
	private static final String COD_CUSTOMER = "codCustomer";
	private static final String PASSWORD = "password";
	private static final String CPF = "cpf";
	private static final String EMAIL = "email";
	private static final String EMAIL_LOGIN = "emailLogin";

	/**
	 * Apply the filter : check input request, validate or not with user auth
	 * 
	 * @param containerRequest
	 *            The request from Tomcat server
	 */
	@Override
	public ContainerRequest filter(ContainerRequest containerRequest) throws WebApplicationException {
//		// GET, POST, PUT, DELETE, ...
//		String method = containerRequest.getMethod();
//		// Token/GetToken for example
//		String path = containerRequest.getPath(true);
//
//		if (method.equals("DELETE")) {
//			throw new WebApplicationException(Status.UNAUTHORIZED);
//		}
//
//		if (method.equals("GET") && (path.equals("Token/getToken") || path.equals("Time/getTime")
//				|| path.equals("PaymentTest/getTime"))) {
//			return containerRequest;
//		}
//
//		// Get the authentification passed in HTTP header parameters
//		String auth = containerRequest.getHeaderValue(AUTHORIZATION);
//
//		// If the user does not have the right (does not provide any HTTP Basic
//		// Auth)
//		if (auth == null && (!method.equals("POST") || !path.equals("Owner/insertOwner"))
//				&& (!method.equals("POST") || !path.equals("Customer/insertCustomer"))
//				&& (!method.equals("GET") || !path.equals("CodePassword/getCode"))
//				&& (!method.equals("GET") || !path.equals("Customer/changePassword"))) {
//			throw new WebApplicationException(Status.UNAUTHORIZED);
//		}
//
//		// Check if the request is from a trusty client
//		String token = containerRequest.getHeaderValue(TOKENREQ);
//		String app = containerRequest.getHeaderValue(APP);
//		String zoneId = containerRequest.getHeaderValue(ZONE_ID);
//
//		if (zoneId == null || zoneId.isEmpty())
//			zoneId = "Z";
//
//		MD5 md5 = new MD5();
//
//		// if (token == null || zoneId == null || !md5.isTokenValid(token, path,
//		// auth, zoneId)) {
//		// throw new WebApplicationException(Status.UNAUTHORIZED);
//		// }
//
//		if (method.equals("POST") && (path.equals("Owner/insertOwner") || path.equals("Customer/insertCustomer")))
//			return containerRequest;
//		else {
//
//			String[] lap = null;
//			if (!path.equals("CodePassword/getCode") && !path.equals("Customer/changePassword")) {
//				// lap : loginAndPassword
//				lap = BasicAuth.decode(auth);
//
//				// If login or password fail
//				if (lap == null || lap.length != 2) {
//					throw new WebApplicationException(Status.UNAUTHORIZED);
//				}
//			}
//
//			// Add Owner code in HTTP header parameters
//			InBoundHeaders header = new InBoundHeaders();
//			MultivaluedMap<String, String> m = containerRequest.getRequestHeaders();
//
//			if (app.equals(CLIENT_LOGIN)) {
//
//				for (Entry<String, List<String>> entry : m.entrySet()) {
//
//					if (!entry.getKey().equals(COD_CUSTOMER) && !entry.getKey().equals(PHONE)
//							&& !entry.getKey().equals(EMAIL) && !entry.getKey().equals(CPF)
//							&& !entry.getKey().equals(PASSWORD))
//						if (entry.getValue().size() > 1)
//							header.addObject(entry.getKey(), entry.getValue());
//						else
//							header.add(entry.getKey(), entry.getValue().get(0));
//				}
//
//				if (path.equals("Customer/loginCustomer")) {
//					header.add(EMAIL, lap[0]);
//					header.add(PASSWORD, lap[1]);
//					containerRequest.setHeaders(header);
//				} else {
//
//					ArrayList<Customer> customerList = null;
//					try {
//						if (!path.equals("CodePassword/getCode") && !path.equals("Customer/changePassword")){
//							List<String> emailList = new ArrayList<String>();
//							emailList.add(lap[0]);
//
//							List<String> passwordList = new ArrayList<String>();
//							passwordList.add(lap[1]);
//							
//							customerList = new CustomerDAO().selectCustomer(null, null, passwordList, null, null, null,
//									null, emailList, null, null, null, null, null, null);
//						} else {
//							String emailLogin = containerRequest.getHeaderValue(EMAIL_LOGIN);
//							List<String> emailLoginList = new ArrayList<String>();
//							emailLoginList.add(emailLogin);
//							customerList = new CustomerDAO().selectCustomer(null, null, null, null, null, null, null,
//									emailLoginList, null, null, null, null, null, null);
//						}
//					} catch (SQLException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//					if (customerList == null || customerList.size() == 0)
//						throw new WebApplicationException(Status.UNAUTHORIZED);
//					else {
//						Customer customer = customerList.get(0);
//						header.add(COD_CUSTOMER, customer.getCodCustomer().toString());
//						header.add(EMAIL, customer.getEmail());
//						header.add(PASSWORD, customer.getPassword());
//						header.add("name", customer.getName());
//						containerRequest.setHeaders(header);
//					}
//
//				}
//			} else {
//
//				for (Entry<String, List<String>> entry : m.entrySet()) {
//
//					if (!entry.getKey().equals(COD_OWNER) && !entry.getKey().equals(EMAIL)
//							&& !entry.getKey().equals(CPF) && !entry.getKey().equals(PASSWORD))
//						if (entry.getValue().size() > 1)
//							header.addObject(entry.getKey(), entry.getValue());
//						else
//							header.add(entry.getKey(), entry.getValue().get(0));
//				}
//
//				if (path.equals("Owner/selectOwner")) {
//					header.add(EMAIL, lap[0]);
//					header.add(PASSWORD, lap[1]);
//					containerRequest.setHeaders(header);
//				} else {
//					ArrayList<Owner> ownerList = null;
//					try {
//						// Check Login
//						ownerList = new OwnerModel().selectOwner(lap[0], null, lap[1], null, false, false, false, false,
//								false, false, false, false, false, false, "Z");
//					} catch (SQLException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//					if (ownerList != null && ownerList.size() > 0) {
//						Owner owner = ownerList.get(0);
//						header.add(COD_OWNER, owner.getCodOwner().toString());
//						containerRequest.setHeaders(header);
//					}
//				}
//			}
//		}

		return containerRequest;
	}
}

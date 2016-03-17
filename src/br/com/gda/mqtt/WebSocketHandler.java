package br.com.gda.mqtt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/WebSocket/{store-name}")
public class WebSocketHandler {

	public final static ConcurrentHashMap<Integer, List<WebSocketHandler>> sockets = new ConcurrentHashMap<>();
	public Session session;
	private Integer store;

	@OnOpen
	public void myOnOpen(Session session,
			@PathParam("store-name") Integer storeName) throws IOException {
		this.session = session;
		this.store = storeName;
		List<WebSocketHandler> socketList = WebSocketHandler.sockets.get(this.store);
		if (socketList == null) {
			socketList = new ArrayList<WebSocketHandler>();
			WebSocketHandler.sockets.put(this.store, socketList);
		}
		socketList.add(this);

		System.out.println("WebSocket opened: " + session.getId());
		RemoteEndpoint.Basic other = session.getBasicRemote();
		try {
			other.sendText("it's connected");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@OnClose
	public void myOnClose(Session session, CloseReason reason) {
		List<WebSocketHandler> socketList = WebSocketHandler.sockets.get(this.store);
		socketList.remove(this);
		if (socketList.size() == 0)
			WebSocketHandler.sockets.remove(this.store);
		System.out.println("Closing a WebSocket " + session.getId()
				+ " due to " + reason.getReasonPhrase());
	}

	@OnMessage
	public void myOnMessage(String txt) throws IOException {
		List<WebSocketHandler> socketList = WebSocketHandler.sockets.get(this.store);
		if (socketList != null)
			for (WebSocketHandler socket : socketList) {
				if (socket.session.isOpen() && socket.session != this.session)
					socket.session.getBasicRemote().sendText(txt);
			}
	}
}

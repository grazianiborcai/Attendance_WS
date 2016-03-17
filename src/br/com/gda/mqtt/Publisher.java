package br.com.gda.mqtt;

import org.eclipse.paho.client.mqttv3.*;

import br.com.gda.mqtt.Utils;


public class Publisher implements MqttCallback {

	
//	public static final String BROKER_URL = "tcp://kkraken.no-ip.org:5132";
	public static final String BROKER_URL = "tcp://localhost:1883";
	private static final String USER_NAME = "gda";
	private static final String PASSWORD = "!qazxsw@";
	private static final String CLIENTID_PREFIX = "!Gda#Mind5!-";

	private MqttClient client;

	public Publisher() {

		connect();
	}

	private void connect() {
		// We have to generate a unique Client id.
		String clientId = CLIENTID_PREFIX + Utils.getMacAddress() + "-pub";

		try {

			client = new MqttClient(BROKER_URL, clientId);

			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(false);
			options.setWill(client.getTopic("home/LWT"), "I'm gone :(".getBytes(), 0, false);
			options.setUserName(USER_NAME);
			options.setPassword(PASSWORD.toCharArray());

			client.setCallback(this);
			client.connect(options);

			System.out.println("Connected to Broker");

		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public void publishMessage(String message, String topic) {

		// this.message = message;
		// this.topic = topic;

		try {

			final MqttTopic temperatureTopic = client.getTopic(topic);

			MqttDeliveryToken token = null;

			token = temperatureTopic.publish(new MqttMessage(message.getBytes()));

			token.waitForCompletion();

			Thread.sleep(100);

		} catch (MqttException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {

		try {

			client.disconnect();

			System.out.println("Disconnected to Broker");

		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
		connect();

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		// TODO Auto-generated method stub

	}

}
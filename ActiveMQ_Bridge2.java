import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQDestination;

public class MQMain {

	// URL of the JMS server
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	// default broker URL is : tcp://localhost:61616"

	// Name of the queue we will receive messages from
	private static String topic = "TestTopic";
	private static String topic2 = "GGGGG";

	public static void dododo() throws JMSException {
		// Getting JMS connection from the server
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		Connection connection = connectionFactory.createConnection("admin", "admin");

		connection.start();

		// Creating session for seding messages
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// Getting the queue 'JCG_QUEUE'
		Destination destination1 = session.createTopic(topic);

		Destination destination2 = session.createTopic(topic2);

		// MessageConsumer is used for receiving (consuming) messages
		MessageConsumer consumer1 = session.createConsumer(destination1);
		MessageConsumer consumer2 = session.createConsumer(destination2);

		MessageProducer producer1 = session.createProducer(destination2);
		MessageProducer producer2 = session.createProducer(destination1);

		// Here we receive the message.

		consumer1.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				try {

					ActiveMQDestination destination = (ActiveMQDestination) message.getJMSDestination();

					System.out.println(destination.getQualifiedName() + " Received message: " + textMessage.getText());

					try {
						int sendCnt = 0;
						try {
							sendCnt = message.getIntProperty("count");
						} catch (Exception e) {

						}

						if (sendCnt < 2) {
							Message message2 = session.createTextMessage("translated...");
							message2.setIntProperty("count", sendCnt + 1);
							producer1.send(message2);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		consumer2.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				try {
					ActiveMQDestination destination = (ActiveMQDestination) message.getJMSDestination();

					System.out.println(destination.getQualifiedName() + " Received message: " + textMessage.getText());

					try {
						int sendCnt = 0;
						try {
							sendCnt = message.getIntProperty("count");
						} catch (Exception e) {

						}

						if (sendCnt < 2) {
							Message message2 = session.createTextMessage("translated...");
							message2.setIntProperty("count", sendCnt + 1);
							producer2.send(message2);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	public static void main(String[] args) throws JMSException {

		dododo();

	}
}

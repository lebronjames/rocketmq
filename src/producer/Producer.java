package producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import constants.Constants;

/**
 * RocketMQ 生产者
 *
 */
public class Producer {

	public static void main(String[] args) throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer("group");
		producer.setNamesrvAddr(Constants.ADDRESS);
		producer.start();
		for(int i=0;i<100;i++){
			Message msg = new Message("orders",("order"+i).getBytes());
			SendResult result = producer.send(msg);
			System.out.println(result);
			System.out.println(msg+" send out");
			Thread.sleep(500);
		}
		producer.shutdown();
	}
}

package consumer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.common.message.MessageQueue;

import constants.Constants;

/**
 * PullConsumer，拉模式，订阅消息（不推荐）
 *
 */
public class PullConsumer {
	private static final Map<MessageQueue, Long> offsetTable = new HashMap<MessageQueue, Long>();
	
	public static void main(String[] args) throws Exception {
		DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("consumergroup");
		consumer.setNamesrvAddr(Constants.ADDRESS);
		consumer.start();
		
		Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues("orders");
		System.out.println(mqs.size());
		
		int i=0;
		for(MessageQueue mq:mqs){
			System.out.println("Consume from the queue: "+i++ + " " +mq);
			PullResult pullResult = consumer.pull(mq, null, getMessageQueueOffset(mq), 32);
			pullResult.getMsgFoundList().forEach((m)->{
				System.out.println(new String(m.getBody()));
			});
		}
	}
	
	public static long getMessageQueueOffset(MessageQueue mq){
		return 0L;
	}
}

package consumer;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import constants.Constants;

/**
 * PushConsumer，推模式，订阅消息，基于发布订阅模式（推荐）
 *
 */
public class PushConsumer {

	public static void main(String[] args) {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumers");
		consumer.setNamesrvAddr(Constants.ADDRESS);
		try{
			//订阅PushTopic下Tag的消息
			consumer.subscribe("order", (MessageSelector)null);
			//程序第一次启动从消息队列头取数据
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
			consumer.registerMessageListener(new MessageListenerConcurrently(){

				@Override
				public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
						ConsumeConcurrentlyContext context) {
					Message msg = list.get(0);
					System.out.println(new String(msg.getBody()));
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
				
			});
			consumer.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

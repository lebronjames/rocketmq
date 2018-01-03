RocketMQ部署开发
一、RocketMq官网下载
wget http://mirrors.tuna.tsinghua.edu.cn/apache/rocketmq/4.2.0/rocketmq-all-4.2.0-bin-release.zip

二、解压、移至/usr/local
unzip rocketmq-all-4.2.0-bin-release.zip
cp -r rocketmq/ /usr/local

三、修改NameServer配置文件runserver.sh
-server -Xms256m -Xmx256m -Xmn256m

四、启动name server
nohup ./mqnamesrv > namesvr.out 2>&1 &

五、查看name server端口、进程
netstat -nlap|grep 9876
jps查看NamesrvStartup进程

六、修改Broker配置文件runbroker.sh
-server -Xms512m -Xmx512m -Xmn256m

七、启动broker
nohup sh ./mqbroker -n localhost:9876 -c ../conf/2m-noslave/broker-a.properties > broker.out &

八、jps查看BrokerStartup进程

九、使用./mqadmin clusterList -n localhost:9876 观察namesrv、master情况

十、使用maven构建项目，由于系统采用RocketMq版本4.2.0比较新，Maven中央仓库还没有更新。
所以采用手动导包的方式（依赖包在/usr/local/rocketmq/lib目录下）

十一、创建生产者
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

十二、采用发布订阅模式，消费消息
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

十三、查看RocketMq的Topic信息
./mqadmin topicList -n 10.5.2.241:9876

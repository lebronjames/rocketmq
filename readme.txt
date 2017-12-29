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


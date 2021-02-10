-javaagent:D:\java_utils\skywalking\apache-skywalking-apm-7.0.0-es7\agent\skywalking-agent.jar
-Dskywalking.agent.service_name=scac-online-sv
-Dskywalking.collector.backend_service=192.168.43.84:11800


dev,druid.common,druid.mysql.dev,feign.common

dev,bus.common,rabbit.common,rabbit.dev

#服务同时作为消息的生产者和消费者
dev,rabbit.common,rabbit.dev,bus.disable,bustream.rabbit.binders,stream.common.bindings.output,stream.common.bindings.input,stream.rabbit.bindings.output,stream.rabbit.bindings.input,stream.common,stream.dev

#服务只作为消息的生产者
dev,rabbit.common,rabbit.dev,bus.disable,bustream.rabbit.binders,stream.common.bindings.output,stream.rabbit.bindings.output,stream.common,stream.dev

#服务开启总线模式
dev,rabbit.common,rabbit.dev,bus.enable,bustream.rabbit.binders,bus.common.bindings.output,bus.common.bindings.input,bus.rabbit.bindings.output,bus.rabbit.bindings.input,bus.common

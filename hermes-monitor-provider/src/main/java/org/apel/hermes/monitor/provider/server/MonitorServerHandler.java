package org.apel.hermes.monitor.provider.server;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import org.apel.hermes.monitor.api.report.HeartbeatReport;
import org.apel.hermes.monitor.provider.service.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitorServerHandler extends ChannelHandlerAdapter{
	
	private static Logger LOG = LoggerFactory.getLogger(MonitorServerHandler.class);
	
	private ConnectionManager connectionManager;
	
	public MonitorServerHandler(ConnectionManager connectionManager){
		this.connectionManager = connectionManager;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		LOG.info("服务端接收到客户端连接请求，建立连接成功");
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		super.userEventTriggered(ctx, evt);
		if (evt instanceof IdleStateEvent) {  
            IdleStateEvent event = (IdleStateEvent) evt;  
            if (event.state().equals(IdleState.READER_IDLE)) { 
            	LOG.info("客户端10S都还没有发送心跳报告，则代表客户端已挂掉，即断开与客户端之间的连接");
            	this.connectionManager.remove(ctx.channel());
            	ctx.channel().close();
            } 
        }
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		HeartbeatReport report = (HeartbeatReport)msg;
		report.setId(ctx.channel().id().toString());
		//读取心跳报告
		this.connectionManager.addChannel(ctx.channel(), (HeartbeatReport)msg);
		LOG.info("监控中心当前环境挂载点个数" + this.connectionManager.getAllChannels().size());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		LOG.error("监控服务端异常：" + cause.getMessage());
		cause.printStackTrace();
		this.connectionManager.remove(ctx.channel());
		ctx.channel().close();
	}
	
	
	
}

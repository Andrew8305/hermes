package org.apel.hermes.core.monitor;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apel.hermes.core.context.ETLContext;

/**
 * 监控中心netty客户端
 * @author lijian
 *
 */
public class MonitorClient {
	
	private static Logger LOG = Logger.getLogger(MonitorClient.class);

	private Bootstrap bootstrap = new Bootstrap();
	private SocketAddress addr;
	private Channel channel;
	private Timer timer = new Timer();
	
	private ETLContext etlContext;
	
	public MonitorClient(String host, int port, ETLContext etlContext) {
		this(new InetSocketAddress( host, port), etlContext);
	}
	
	class MyChannelInitializer extends ChannelInitializer<SocketChannel>{
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ch.pipeline().addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
        	ch.pipeline().addLast(new ObjectEncoder());
       	 	ch.pipeline().addLast(new ObjectDecoder(1024 * 1024 * 5, ClassResolvers.weakCachingConcurrentResolver(this
                    .getClass().getClassLoader())));
			ch.pipeline().addLast(new MonitorClientHandler(etlContext));
		}
		
	}
	
	public MonitorClient(SocketAddress addr, ETLContext etlContext) {
		this.addr = addr;
		this.etlContext = etlContext;
		bootstrap.group( new NioEventLoopGroup() );
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.handler(new MyChannelInitializer());
	}

	private void doConnect() {
		try {						
			ChannelFuture f = bootstrap.connect(addr);
			f.addListener( new ChannelFutureListener() {
				@Override public void operationComplete(ChannelFuture future) throws Exception {
					if( !future.isSuccess() ) {
						TimeUnit.SECONDS.sleep(3);
						LOG.info("连接监控服务端失败，客户端自动断线重连...............");
						future.channel().close();
						bootstrap.connect(addr).addListener(this);
					} else {
						LOG.info("连接监控中心成功...............");
						channel = future.channel();
						addCloseDetectListener(channel);
					}
				}

				private void addCloseDetectListener(Channel channel) {
					//设置客户端被关闭异常时的监听器，当断开时进行自动重连
					channel.closeFuture().addListener(new ChannelFutureListener() {
						@Override
						public void operationComplete(ChannelFuture future )
								throws Exception {
							connectionLost();
							scheduleConnect(2000);
						}
					});
				} 
				
			});					
		}catch( Exception ex ) {
			scheduleConnect(2000);
		}
	}
	
	public void connect(){
		scheduleConnect(2000);
	}
	
	private void scheduleConnect(long millis) {
		timer.schedule( new TimerTask() {
			@Override
			public void run() {
				doConnect();
			}
		}, millis);
	}
	
	private void connectionLost() {
		LOG.info("监控中心客户端连接断开，准备启动客户端自动重连");
	}
	
}

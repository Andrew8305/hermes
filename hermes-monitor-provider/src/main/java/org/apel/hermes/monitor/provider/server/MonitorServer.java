package org.apel.hermes.monitor.provider.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

import org.apel.hermes.monitor.provider.service.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**监控中心netty服务器端
 * @author lijian
 *
 */
public class MonitorServer {
	
	private static Logger LOG = LoggerFactory.getLogger(MonitorServer.class);

	private int port;
	
	private ConnectionManager connectionManager;

    public MonitorServer(int port, ConnectionManager connectionManager) {
        this.port = port;
        this.connectionManager = connectionManager;
    }

    public void run(){
        EventLoopGroup bossGroup = new NioEventLoopGroup(); 
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); 
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class) 
             .childHandler(new ChannelInitializer <SocketChannel>() { 
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                	 ch.pipeline().addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
                	 ch.pipeline().addLast(new ObjectEncoder());
                	 ch.pipeline().addLast(new ObjectDecoder(1024 * 1024 * 5, ClassResolvers.weakCachingConcurrentResolver(this
                             .getClass().getClassLoader())));
                     ch.pipeline().addLast(new MonitorServerHandler(connectionManager));
                 }
             })
             .option(ChannelOption.SO_BACKLOG, 128)          
             .childOption(ChannelOption.SO_KEEPALIVE, true); 

            ChannelFuture f = b.bind(port).sync();
            LOG.info("监控服务已启动");
            f.channel().closeFuture().sync();
        }catch(Exception e){
        	LOG.info("监控服务启动异常：" + e.getMessage());
        	e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
	
}

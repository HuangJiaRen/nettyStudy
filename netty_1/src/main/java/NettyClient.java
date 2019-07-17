import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


public class NettyClient {
    private int port;
    private String host;
    private SocketChannel socketChannel;

    public NettyClient(int port, String host) {
        this.port = port;
        this.host = host;
        start();
    }

    private void start(){
        EventLoopGroup eventLoopGroup= new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.remoteAddress(host, port);
        bootstrap.handler(new ChannelInitializer<io.netty.channel.socket.SocketChannel>() {
            @Override
            protected void initChannel(io.netty.channel.socket.SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new NettyClientHandler());
            }
        });

        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            if(channelFuture.isSuccess()){
            socketChannel = (SocketChannel) channelFuture.channel();
                System.out.println("链接服务器成功");
            }

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient(8080, "localhost");
    }

}

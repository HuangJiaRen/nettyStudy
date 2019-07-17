import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

/**
 * @author huangli
 */
public class NettyClientHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws UnsupportedEncodingException {
        //发送消息
        ctx.writeAndFlush(getSendByteBuf("Client ---> Server Hello"));
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        //收到消息
        ByteBuf byteBuf = (ByteBuf) msg;
        //获取服务传来的msg
        System.out.println("-------收到消息-------");
        System.out.println(byteBuf.toString());
    }

    public void excetionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }

    private ByteBuf getSendByteBuf(String message) throws UnsupportedEncodingException {
        byte[] req = message.getBytes("UTF-8");
        ByteBuf pingMessage = Unpooled.buffer();
        pingMessage.writeBytes(req);
        return pingMessage;
    }
}

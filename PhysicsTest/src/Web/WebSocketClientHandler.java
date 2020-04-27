package Web;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.util.CharsetUtil;

/**
 * WebSocket消息处理类
 * 请在此类中处理：
 * 1. （可选）用户已扫码消息
 * 2. 用户完成登录消息
 *
 * @author Mr.YAO
 */
public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    private final WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;

    public WebSocketClientHandler(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        handshaker.handshake(ctx.channel());
        System.out.println("WebSocket Client connected!");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("WebSocket Client disconnected!");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object object) throws Exception {
        Channel ch = ctx.channel();
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ch, (FullHttpResponse) object);
                handshakeFuture.setSuccess();
            } catch (WebSocketHandshakeException e) {
                System.out.println("WebSocket Client failed to connect");
                handshakeFuture.setFailure(e);
            }
            return;
        }

        if (object instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) object;
            throw new IllegalStateException(
                    "Unexpected FullHttpResponse (getStatus=" + response.status() +
                            ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        WebSocketFrame frame = (WebSocketFrame) object;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            // 接收到来自server的消息
            String text = textFrame.text();

            if ("SCANNED".equals(text)) {
                // 接收到用户已扫码消息
                // 请在此处处理 用户已扫码 事件
                System.out.println("Scanned QR code successfully");
            } else {
                // 接收到用户已登录消息
                // 请在此处处理 用户已登录 事件
                WxaPublicUserInfo wxaPublicUserInfo = JSON.parseObject(text, WxaPublicUserInfo.class);
                Integer userId = wxaPublicUserInfo.getUserId();
                String nickName = wxaPublicUserInfo.getNickName();
                Integer gender = wxaPublicUserInfo.getGender();
                String language = wxaPublicUserInfo.getLanguage();
                String city = wxaPublicUserInfo.getCity();
                String province = wxaPublicUserInfo.getProvince();
                String country = wxaPublicUserInfo.getCountry();
                String avatarUrl = wxaPublicUserInfo.getAvatarUrl();
                Integer timestamp = wxaPublicUserInfo.getTimestamp();
                System.out.println("userId = " + userId);
                System.out.println("nickName = " + nickName);
                System.out.println("gender = " + gender);
                System.out.println("language = " + language);
                System.out.println("city = " + city);
                System.out.println("province = " + province);
                System.out.println("country = " + country);
                System.out.println("avatarUrl = " + avatarUrl);
                System.out.println("timestamp = " + timestamp);
            }
        } else if (frame instanceof PongWebSocketFrame) {
            System.out.println("WebSocket Client received pong");
        } else if (frame instanceof CloseWebSocketFrame) {
            System.out.println("WebSocket Client received closing");
            ch.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }
}
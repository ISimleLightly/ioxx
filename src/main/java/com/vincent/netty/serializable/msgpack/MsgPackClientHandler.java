package com.vincent.netty.serializable.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MsgPackClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final int sendNumber;

    public MsgPackClientHandler(int sendNumber) {
        this.sendNumber = sendNumber;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("client accept: " + byteBuf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        User[] users = makeUsers();
        //发送数据
        for(User user:users){
            System.out.println("Send user: "+user);
            ctx.write(user);
        }
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private User[] makeUsers(){
        User[] users=new User[sendNumber];
        User user =null;
        for(int i=0;i<sendNumber;i++){
            user=new User();
            user.setAge(i);
            String userName = "ABCDEFG --->"+i;
            user.setUserName(userName);
            user.setId("No:"+(sendNumber-i));
            user.setUserContact(
                    new UserContact(userName+"@xiangxue.com","133"));
            users[i]=user;
        }
        return users;
    }
}

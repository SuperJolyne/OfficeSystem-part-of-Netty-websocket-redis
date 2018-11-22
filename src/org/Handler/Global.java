package org.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Global {

    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static Map<String, ChannelHandlerContext> map = new HashMap<String, ChannelHandlerContext>();

    public static Collection<ChannelHandlerContext> col = map.values();
}

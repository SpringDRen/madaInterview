package com.rlc.http.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Set;

import com.rlc.http.util.HttpServerConfig;

public class HttpServerSocketNIO implements HttpServerSocket {
  private static final int port = HttpServerConfig.getHttpServerPort();
  private Selector selector;

  public Selector getSelector() {
    return selector;
  }

  @Override
  public void startServer() {

    int activeSockets = 0;
    try {
      selector = Selector.open();// 打开选择器
      ServerSocketChannel socketChannel = ServerSocketChannel.open();// 定义一个ServerSocketChannel通道
      ServerSocket socket = socketChannel.socket();
      socket.bind(new InetSocketAddress(port));// ServerSocketChannel绑定端口
      socketChannel.configureBlocking(false);// 配置通道使用非阻塞模式
      socketChannel.register(selector, SelectionKey.OP_ACCEPT);// 该通道在selector上注册
    } catch (IOException e) {
      e.printStackTrace();
    }
    // 接受连接的动作
    System.out.println("httpserver开始运行，端口" + port + "，等待连接.......");
    // while (true) {
    try {
      selector.select();
    } catch (IOException e) {
      // continue;
    }
    System.out.println(111);
    Set<SelectionKey> keys = selector.selectedKeys();
    activeSockets = keys.size();
    System.out.println("SelectionKeys:" + activeSockets);
    if (activeSockets == 0) {
      // continue;
    }

    for (SelectionKey key : keys) {
      if (key.isConnectable()) {

      } else if (key.isAcceptable()) {// 可连接
        System.out.println("isAcceptable");
        doServerSocketEvent(key);
      } else if (key.isReadable()) {// 可读
        System.out.println("isReadable");
        doClientReadEvent(key);
      } else if (key.isWritable()) {// 可写
        System.out.println("isWritable");
        doClientWriteEvent(key);
      }
      keys.remove(key);
    }
    // }
  }

  /**
   * 连接操作
   * 
   * @param key
   */
  private void doServerSocketEvent(SelectionKey key) {
    SocketChannel client = null;
    ServerSocketChannel server = (ServerSocketChannel) key.channel();
    try {
      client = server.accept();
      if (client == null) {
        return;
      }
      client.configureBlocking(false);// 设置非阻塞模式
      // 注册读写操作
      client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
      // try {
      // Thread.sleep(1 * 1000);
      // } catch (InterruptedException e) {
      // // TODO Auto-generated catch block
      // e.printStackTrace();
      // }
    } catch (IOException e) {
      try {
        client.close();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
  }

  /**
   * 读数据操作
   * 
   * @param key
   */
  private void doClientReadEvent(SelectionKey key) {
    CharsetDecoder decoder = Charset.forName("GB2312").newDecoder();

    ByteBuffer receiveBuffer;
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    SocketChannel socketChannel = (SocketChannel) key.channel();
    if (socketChannel.isOpen()) {
      int nBytes = 0;
      buffer.clear();
      try {
        nBytes = socketChannel.read(buffer);
      } catch (IOException e) {
        e.printStackTrace();
      }
      buffer.flip();
      CharBuffer charBuffer;
      try {
        charBuffer = decoder.decode(buffer);
        String command = charBuffer.toString();
        System.out.println(command);
      } catch (CharacterCodingException e) {
        e.printStackTrace();
      }
      try {
        socketChannel.close();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    // try {
    // Thread.sleep(1 * 1000);
    // } catch (InterruptedException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
  }

  /**
   * 写操作
   * 
   * @param key
   */
  private void doClientWriteEvent(SelectionKey key) {
    SocketChannel socketChannel = (SocketChannel) key.channel();
    // 要输出的内容
    StringBuffer strbuff = new StringBuffer();
    strbuff.append("some info");
    byte[] data = strbuff.toString().getBytes();
    ByteBuffer buffer = ByteBuffer.allocate(data.length);
    buffer.put(data);
    buffer.flip();
    try {
      socketChannel.write(buffer);
      socketChannel.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      Thread.sleep(5 * 1000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void shutdownServer() {

  }
}

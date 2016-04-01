package com.rlc.http.httpserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

//import org.apache.log4j.Logger;

import com.rlc.http.threads.HttpServerThreadPool;
import com.rlc.http.util.HttpServerConfig;

public class HttpServerSocketIO implements HttpServerSocket {

  // Logger log = Logger.getLogger(this.getClass());
  private ServerSocket serverSocket = null;

  /**
   * 每次从流中读取的字节数
   */
  private int readoncebytes = 512;
  /*
   * request头部信息较小，获取request头部信息输入流字节数的大小，对性能影响较小，即使为1，也只差1ms
   */
  /**
   * 每次读取资源，往流中写入得字节数
   */
  private int writeoncebytes = 2048;
  /*
   * 从512开始测试，越大，处理速度越快，截止到2048，数据稳定，不发生异常，没有失败连接 2048速度明显快于1024，超过2500性能稳定受到影响
   * 1024 * 3会出现请求失败，而且性能下降 这里不确定是读取本地文件影响速度，还是往客户端写入数据影响速度，待考证。
   * 不过一次写入的数据过多导致请求失败，或者其他的系统异常应该是往客户端写入导致的。
   */

  /**
   * 服务端口
   */
  private final int port = HttpServerConfig.getHttpServerPort();

  @Override
  public void startServer() {
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      // log.error("在端口" + port + "创建serverSocket失败", e.getCause());
      e.printStackTrace();
      return;
    }
    // log.debug("开始监听" + port + "，等待连接.....");
    System.out.println("开始监听" + port + "，等待连接.....");
    while (true) {
      try {
        Socket socket = serverSocket.accept();
        HttpServerThreadPool.INSTANCE.executeTask(() -> doprogress(socket));
      } catch (IOException e) {
        continue;
      }
    }
  }

  /**
   * 处理socket请求
   * 
   * @param socket
   */
  private void doprogress(Socket socket) {
    RequestHeader header;
    try {
      header = doRequest(socket);
      // 根据解析的header处理响应.....
      doResponse(socket, header);
    } catch (RequestHeaderParseException e1) {
      e1.printStackTrace();
    }

    try {
      if (socket != null && !socket.isClosed()) {
        System.out.println("关闭连接......");
        socket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 处理response
   * 
   * @param socket
   * @param header
   */
  private void doResponse(Socket socket, RequestHeader header) {
    try (OutputStream out = socket.getOutputStream()) {
      // 向客户端输出信息....
      StringBuilder bld = new StringBuilder();
      ResponseHeader response = new ResponseHeader(header);
      // 协议状态
      bld.append(response.getStatus() + "\r\n");
      // 请求长度
      // bld.append(response.getContentLength() + "\r\n");
      // 输出类型
      bld.append(response.getContentType() + "\r\n");
      // 连接状态
      bld.append(response.getConnection() + "\r\n");
      bld.append("\r\n");
      // 输出response头部信息
      // System.out.println(bld.toString());
      out.write(bld.toString().getBytes());

      if (response.isSourcesIsReadable()) {
        // 输出请求的资源文件
        try (InputStream inputstream = new FileInputStream(
            response.getSourceFile())) {
          byte[] bytes = new byte[writeoncebytes];
          int len = inputstream.read(bytes);
          while (len > 0) {
            out.write(bytes, 0, len);
            len = inputstream.read(bytes);
          }
          // out.flush();//flush并不影响性能，也不能影响浏览器体验
          // 相反，如果在while循环中实时flush，浏览器将无法访问图片，而ab测试速度会下降
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 处理请求头
   * 
   * @param socket
   * @return
   * @throws RequestHeaderParseException
   * @throws IOException
   */
  private RequestHeader doRequest(Socket socket)
      throws RequestHeaderParseException {
    try {
      InputStream in = socket.getInputStream();
      int rest = in.available();
      if (rest <= 0) {
        System.out.println("请求头不含任何数据");
        return null;
      }
      // 读取信息，使用stringbuilder
      byte[] bytes = new byte[readoncebytes];
      StringBuilder bld = new StringBuilder();
      int len = in.read(bytes);
      while (len > 0 && rest > 0) {
        bld.append(new String(bytes, 0, len));
        rest = rest - readoncebytes;
        if (rest > 0) {
          len = in.read(bytes);
        }
      }
      // request头部信息
      // System.out.println(br.toString());
      return new RequestHeader(bld.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void shutdownServer() {
    if (serverSocket != null) {
      try {
        serverSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}

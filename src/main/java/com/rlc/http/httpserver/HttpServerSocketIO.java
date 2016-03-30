package com.rlc.http.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.rlc.http.threads.HttpServerThreadPool;
import com.rlc.http.util.HttpServerConfig;

public class HttpServerSocketIO {

  Logger log = Logger.getLogger(this.getClass());
  private ServerSocket serverSocket = null;
  /**
   * 服务端口
   */
  private final int port = HttpServerConfig.getHttpServerPort();

  /**
   * 启动服务器
   */
  public void startServer() {
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      log.error("在端口" + port + "创建serverSocket失败", e.getCause());
      e.printStackTrace();
      return;
    }
    log.debug("开始监听" + port + "，等待连接.....");
    System.out.println("开始监听" + port + "，等待连接.....");
    while (true) {
      try {
        Socket socket = serverSocket.accept();
        HttpServerThreadPool.INSTANCE.executeTask(new Runnable() {

          @Override
          public void run() {
            doprogress(socket);
          }
        });
      } catch (IOException e) {
        continue;
      }
    }
  }

  private void doprogress(Socket socket) {
    try {
      //如何判断做什么动作......？？？
      RequestHeader header = doRequest(socket);
      // 根据解析的header处理响应.....
      doResponse(socket, header);
    } catch (RequestHeaderParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      socket.close();
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
    OutputStream out = null;
    try {
      out = socket.getOutputStream();
      // 向客户端输出信息....
      byte[] content = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 请求成功").getBytes("GBK");
      StringBuilder bld = new StringBuilder();
      bld.append("HTTP/1.0 200 OK\r\n");
      bld.append("Content-Type: text/html\r\n");
      bld.append("Connection: close\r\n");
      bld.append("\r\n");
      System.out.println(bld.toString());
      out.write(bld.toString().getBytes());
      out.write(content);
      
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
      throws RequestHeaderParseException, IOException {
    InputStream in = socket.getInputStream();
    int rest = in.available();
    System.out.println(rest);
    if (rest <= 0) {
      in.close();
      throw new RequestHeaderParseException("请求不包含任何可读数据");
    }
    int oncebytes = 1024;
    byte[] bytes = new byte[oncebytes];
    StringBuffer br = new StringBuffer();
    int len = in.read(bytes);
    while (len > 0 && rest > 0) {
      br.append(new String(bytes, 0, len));
      rest = rest - oncebytes;
      if (rest > 0) {
        len = in.read(bytes);
      }
    }
    System.out.println(br.toString());
    return new RequestHeader(br.toString());
  }

  /**
   * 关闭服务器
   */
  public void shutdownServer() {
    if (serverSocket != null) {
      try {
        serverSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {
    HttpServerSocketIO hio = new HttpServerSocketIO();
    hio.startServer();
  }

}

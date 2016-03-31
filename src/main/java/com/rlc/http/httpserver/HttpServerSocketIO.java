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

  //Logger log = Logger.getLogger(this.getClass());
  private ServerSocket serverSocket = null;
  /**
   * 服务端口
   */
  private final int port = HttpServerConfig.getHttpServerPort();

  @Override
  public void startServer() {
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      //log.error("在端口" + port + "创建serverSocket失败", e.getCause());
      e.printStackTrace();
      return;
    }
    //log.debug("开始监听" + port + "，等待连接.....");
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
      String status = response.getStatus();
      if (status == null) {
        bld.append("HTTP/1.0 204 error header\r\n");
      } else if ("200".equals(status)) {
        bld.append("HTTP/1.0 200 OK\r\n");
      } else if ("404".equals(status)) {
        bld.append("HTTP/1.0 404 Not Found\r\n");
      }
      bld.append(response.getContentType() + "\r\n");
      bld.append(response.getConnection() + "\r\n");
      bld.append("\r\n");
      System.out.println(bld.toString());
      out.write(bld.toString().getBytes());

      if (response.isSourcesIsReadable()) {
        try (InputStream inputstream = new FileInputStream(
            response.getSourceFile())) {
          byte[] bytes = new byte[1024];
          int len = inputstream.read(bytes);
          while (len > 0) {
            out.write(bytes, 0, len);
            len = inputstream.read(bytes);
          }
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

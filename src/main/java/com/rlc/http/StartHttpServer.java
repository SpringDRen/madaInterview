package com.rlc.http;

import com.rlc.http.httpserver.HttpServerSocket;
import com.rlc.http.httpserver.HttpServerSocketIO;
import com.rlc.http.httpserver.HttpServerSocketNIO;
import com.rlc.http.util.HttpServerConfig;

public class StartHttpServer {

  public static void main(String[] args) {

    if (args != null && args.length > 0) {
      String path = args[0];
      HttpServerConfig.init(path);
    }
    
    try {
      HttpServerSocket hpptserver;
      if(HttpServerConfig.SERVER_BY_IO.equalsIgnoreCase(HttpServerConfig.getIOType())){
        hpptserver = new HttpServerSocketIO();
        System.out.println("使用一般IO建立服务器.....");
      } else {
        hpptserver = new HttpServerSocketNIO();
        //System.out.println("使用NIO建立服务器.....");
        System.out.println("暂时未实现NIO模式的服务器.....");
        return;
      }
      hpptserver.startServer();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  

}

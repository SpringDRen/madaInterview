package com.rlc.http;

import com.rlc.http.httpserver.HttpServerSocket;
import com.rlc.http.util.HttpServerConfig;

public class StartHttpServer {

  public static void main(String[] args) {

    if (args != null && args.length > 0) {
      String path = args[0];
      HttpServerConfig.init(path);
    }
    
    try {
      new HttpServerSocket().startListener();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  

}

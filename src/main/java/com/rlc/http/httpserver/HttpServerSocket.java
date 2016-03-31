package com.rlc.http.httpserver;

public interface HttpServerSocket {

  /**
   * 启动服务器
   */
  public void startServer();

  /**
   * 关闭服务器
   */
  public void shutdownServer();

}

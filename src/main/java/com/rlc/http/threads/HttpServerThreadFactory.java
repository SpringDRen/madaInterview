package com.rlc.http.threads;

import java.util.concurrent.ThreadFactory;

public class HttpServerThreadFactory implements ThreadFactory {
  /**
   * 线程数量
   */
  private int threadCont = 0;
  /**
   * 线程名称前缀
   */
  private String prefix = "";
  
  public HttpServerThreadFactory(){
    this.prefix = "httprequest";
  }
  
  public HttpServerThreadFactory(String prefix){
    this.prefix = prefix;
  }

  @Override
  public Thread newThread(Runnable r) {
    Thread t = new Thread(r, prefix + "-" + threadCont++);
    //添加对线程的一些设置....
    return t;
  }
}

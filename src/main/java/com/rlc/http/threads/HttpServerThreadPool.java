package com.rlc.http.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum HttpServerThreadPool {

  INSTANCE;
  
  /**
   * 根据java虚拟机可用处理器的数目初始化固定大小的线程池
   */
  private final ExecutorService fixedThreadPool = Executors
      .newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
          new HttpServerThreadFactory());

  public void executeTask(Runnable r) {
    fixedThreadPool.execute(r);
  }
}

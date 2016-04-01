package com.rlc.http.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum HttpServerThreadPool {

  INSTANCE;

  private final int initThreadCounts = Runtime.getRuntime()
      .availableProcessors();

  /**
   * 返回线程池初始化线程的数量
   * 
   * @return
   */
  public int getThreadPoolSize() {
    return initThreadCounts;
  }

  /**
   * 根据java虚拟机可用处理器的数目初始化固定大小的线程池
   */
  private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(
      initThreadCounts, new HttpServerThreadFactory());

  public void executeTask(Runnable r) {
    fixedThreadPool.execute(r);
  }
}

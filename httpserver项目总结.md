#知识运用
1、之前就熟练掌握网络编程，会使用socket编程开发聊天服务器之类的简单应用  
2、对线程池有一定的了解  
3、熟练掌握IO编程
#学习
1、深入http协议，学习如何解析request头，以及如何处理response头部信息。  
一个http相关的在线工具网站http://tool.oschina.net/commons  
2、nio，开发前期学习了大量的nio知识，也根据网上一些资料，写了一点雏形，但由于不是很熟练，且时间有限，未实现nio服务器  
一个nio的教程：http://www.iteye.com/magazines/132-Java-NIO  
3、学会使用ab进行性能测试  
#其他成长
1、线程池使用过程中，学会了Lambda表达式  
    HttpServerThreadPool.INSTANCE.executeTask(() -> doprogress(socket));  
2、基于IO的编程，掌握了自动资源管理  
    try (OutputStream out = socket.getOutputStream()) {
    代码块}

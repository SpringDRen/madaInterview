##全栈测试题

####道琼斯成分股股价分析界面
* 我们可以做一个可视化界面，交互式地分析道琼斯成分股在过去一年的表现，方便看到个股每天的开盘价、收盘价、交易量等信息
* 可视化界面前端用 React 来完成，用户可以自由的选择看什么个股，最好可以比较几个个股
* 这个可视化界面背后由一个简单的 web server 支持，除了发送基本的静态文件（HTML，CSS 和 JavaScript）之外，还提供一个 API 接口，背后用一个很简单的 in memory sqlite 数据来支持（因为数据量不大）
* 不要求用户验证，登录等其他不重要的功能


##后端测试题

Concurrent HTTP Server

* Implement an HTTP Server to serve static files and directories from a local file system path<br>
* The HTTP Server should support HTTP/1.0 protocol, multiple MIME types including HTML, TXT, JavaScript, CSS, PNG, and JPG files
* The HTTP server can support only GET requests for now
* The HTTP Server should safely support multiple real (e.g. from Chrome, Safari browsers) concurrent client requests
* You must not use any existing HTTP frameworks and libraries, and instead should be only based on client/server socket operations (e.g. ServerSocket in Java
* You are free to use other common system programming language
* You should use ab, httperf or other benchmark tools to analyze the performance of your server and try to optimize it - we need you to share your experience on benchmarking
* After the functionalities are all completed, you are encouraged to implement extra features, e.g. caching (which involves LastModified header).

###Some Tips

* Try to complete functional requirements before rushing to performance optimization
* Setup auto-testing scripts and/or tools along with real browser tests
* Refer to HTTP standard for protocol detail and header types
* You might even take a look at ulimit for file descriptor limit


----------


1. socket实现http服务，只能get请求

2. HTTP /1.0协议，多种MIME类型包括HTML，TXT，JavaScript中，CSS，PNG，和JPG文件
3. AB性能测试


----------
JDK：1.8没有其他依赖。
使用步骤：
mvn package -Dmaven.test.skip=true

在target目录下，生成mada.jar。本系统暂时未引入其他依赖，如果有会复制到jar的同目录，即target/lib下

java -jar mada.jar

ps：运行服务器可以指定一个参数，http.properties的绝对路径，从文件外读取配置。
http.properties的配置如下：  

 #http服务器端口号  
httpserver.port=80  
 #资源路径  
httpserver.source=E\:/thing-for-work/allworks/MyWork/madaInterview/src/main/webapp/resources/  
 #使用哪种实现方式 IO NIO(暂未实现)  
httpserver.iotype=IO  


----------

性能测试截图：  
50并发，10000请求  
![](https://github.com/SpringDRen/madaInterview/blob/master/src/main/webapp/resources/img/io_50_10000.png)  
50并发，20000请求  
![](https://github.com/SpringDRen/madaInterview/blob/master/src/main/webapp/resources/img/io_50_20000.png)  
100并发，10000请求  
![](https://github.com/SpringDRen/madaInterview/blob/master/src/main/webapp/resources/img/io_100_10000.png)  
100并发，20000请求  
![](https://github.com/SpringDRen/madaInterview/blob/master/src/main/webapp/resources/img/io_100_20000.png)  
----------
0.0.1-version存在的问题  
1、NIO服务器未实现  
2、无法读取jar包中的配置：java.io.FileNotFoundException: file:\E:\thing-for-work\allworks\MyWork\madaInter  
view\target\mada.jar!\http.properties (文件名、目录名或卷标语法不正确。)

3、未限制POST请求  
4、浏览器访问貌似有些问题。。。  


----------
0.0.2-version主要是对io实现的服务器进行优化，解决了0.0.1-version中jar包配置文件读取的问题，其他问题未解决。  
通过AB性能测试，辅助jconsole监视内存跟线程，得出如下结论：  
1、影响性能的地方主要有两点，一是IO，二是线程。  
2、工程使用concurrent包中的线程池，线程方面并未相处相关优化方案，不过性能测试得出如下结论：直接new线程速度慢于使用单线程线程池，而且耗费大量的堆内存，创建了大量的线程。在双核4线程的处理器上，虽然java可获取的处理器数目为4，但是线程池配置2、3、4甚至是8或者更大，性能测试效果均差别不大。  
3、IO影响，这个是影响性能的最主要因素。涉及的主要IO有3出，读取requestheader的inputstream，根据性能测试，因为request header字节数非常少，即使字节数设置为1，性能影响也区别不大；另外两处就是读资源文件到内存的inputstream跟输出response的outputstream，这两个是同时进行的，因此未分开测试。根据性能测试得出结论，单次操作的字节数在一定的范围内，数字越大，性能越好。测试范围从512到3072。截止到2500，均是单次读取字节设置越大，性能越快，超过2500,性能出现不稳定状态，数据异常，丢失连接等。到底是那个stream影响了性能，以及为什么出现问题，还待考证，网上并没有直接找到相关答案。  
4、其他细节影响，比如拼接字符串尽量使用stringbuilder，处理请求头时工程使用了bufferreader，可能不如直接解析字符串效率高，或者有更高效的解析方案  
  
附上性能测试截图：  
直接new线程测试：  
![](https://github.com/SpringDRen/madaInterview/blob/master/src/main/webapp/resources/img/threadsab/newthread.png)  
线程池单线程：  
![](https://github.com/SpringDRen/madaInterview/blob/master/src/main/webapp/resources/img/threadsab/pool-1-thread.png)  
线程池线程数为2：  
![](https://github.com/SpringDRen/madaInterview/blob/master/src/main/webapp/resources/img/threadsab/pool-2-thread.png)  
线程池线程数位3：  
![](https://github.com/SpringDRen/madaInterview/blob/master/src/main/webapp/resources/img/threadsab/pool-3-thread.png)  
线程池线程数为4：  
![](https://github.com/SpringDRen/madaInterview/blob/master/src/main/webapp/resources/img/threadsab/pool-4-thread.png)  
线程池线程数为8：  
![](https://github.com/SpringDRen/madaInterview/blob/master/src/main/webapp/resources/img/threadsab/pool-8-thread.png)  
  
将response操作的单次字节数设置为2048后的性能测试：  
![](https://github.com/SpringDRen/madaInterview/blob/master/src/main/webapp/resources/img/300_30000_2048.png)  

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



存在的问题：
1、NIO服务器未实现
2、无法读取jar包中的配置：java.io.FileNotFoundException: file:\E:\thing-for-work\allworks\MyWork\madaInter
view\target\mada.jar!\http.properties (文件名、目录名或卷标语法不正确。)

3、未限制POST请求
4、浏览器访问貌似有些问题。。。

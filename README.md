# madaInterview
马达数据面试

全栈测试题
道琼斯成分股股价分析界面

我们可以做一个可视化界面，交互式地分析道琼斯成分股在过去一年的表现，方便看到个股每天的开盘价、收盘价、交易量等信息
可视化界面前端用 React 来完成，用户可以自由的选择看什么个股，最好可以比较几个个股
这个可视化界面背后由一个简单的 web server 支持，除了发送基本的静态文件（HTML，CSS 和 JavaScript）之外，还提供一个 API 接口，背后用一个很简单的 in memory sqlite 数据来支持（因为数据量不大）
不要求用户验证，登录等其他不重要的功能


后端测试题

Concurrent HTTP Server

Implement an HTTP Server to serve static files and directories from a local file system path
The HTTP Server should support HTTP/1.0 protocol, multiple MIME types including HTML, TXT, JavaScript, CSS, PNG, and JPG files
The HTTP server can support only GET requests for now
The HTTP Server should safely support multiple real (e.g. from Chrome, Safari browsers) concurrent client requests
You must not use any existing HTTP frameworks and libraries, and instead should be only based on client/server socket operations (e.g. ServerSocket in Java
You are free to use other common system programming language
You should use ab, httperf or other benchmark tools to analyze the performance of your server and try to optimize it - we need you to share your experience on benchmarking
After the functionalities are all completed, you are encouraged to implement extra features, e.g. caching (which involves LastModified header).
Some Tips

Try to complete functional requirements before rushing to performance optimization
Setup auto-testing scripts and/or tools along with real browser tests
Refer to HTTP standard for protocol detail and header types
You might even take a look at ulimit for file descriptor limit

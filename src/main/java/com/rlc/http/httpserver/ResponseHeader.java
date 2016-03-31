package com.rlc.http.httpserver;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import com.rlc.http.util.HttpServerConfig;

public class ResponseHeader {
  // Accept-Ranges:bytes
  public static final String ACCEPT = "Accept-Ranges";
  /**
   * 默认字节
   * <pre>Accept-Ranges:bytes</pre>
   * @return
   */
  public String getAccept() {
    return ACCEPT + ":bytes";
  }

  // Cache-Control:max-age=0
  public static final String CACHE = "Cache-Control";
  /**
   * 默认不缓存
   * <pre>Cache-Control:max-age=0</pre>
   * @return
   */
  public String getCache() {
    return CACHE + ":" + "max-age=0";
  }

  // Connection:keep-alive
  public static final String CONNECTION = "Connection";
  /**
   * 默认保持连接
   * <pre>Connection:keep-alive</pre>
   * @return
   */
  public String getConnection() {
    return CONNECTION + ":" + "keep-alive";
  }

  // Content-Length:0
  public static final String CONTENT_LENGTH = "Content-Length";
  // Content-Type:image/gif
  public static final String CONTENT_TYPE = "Content-Type";
  // Date:Thu, 31 Mar 2016 14:21:39 GMT
  public static final String DATE = "Date";
  // Expires:Thu, 31 Mar 2016 14:21:39 GMT
  public static final String EXPIRES = "Expires";
  
  // Last-Modified:Mon, 25 Aug 2014 02:18:09 GMT
  public static final String LAST_MODIFIED = "Last-Modified";
  /**
   * 获取资源最后修改时间，GMT格式
   * <pre>Last-Modified:Mon, 25 Aug 2014 02:18:09 GMT</pre>
   * @return
   */
  public String getLastmodified() {
    return LAST_MODIFIED + ":" + this.sourceLastMod;
  }
  private String sourceLastMod;
  
  /**
   * 默认编码，暂时写死。。
   */
  private String encoding = "UTF-8";

  private RequestHeader request;
  /**
   * 文件是否存在
   */
  private boolean sourcesIsReadable = false;
  private File sourceFile;
  public File getSourceFile(){
    return this.sourceFile;
  }

  public ResponseHeader(RequestHeader request) {
    this.request = request;
  }

  /**
   * 请求的资源是否可读
   * 
   * @return
   */
  public boolean isSourcesIsReadable() {
    return this.sourcesIsReadable;
  }

  /**
   * 返回响应状态，200位成功，404文件请求不到，null表示失败
   * 
   * @return
   */
  public String getStatus() {
    if (request == null) {
      return null;
    }

    File f = new File(HttpServerConfig.getSourcedir() + decodeURI(request.getRequestURI()));
    if (f.exists()) {
      this.sourceFile = f;
      this.sourcesIsReadable = true;
      this.sourceLastMod = new Date(f.lastModified()).toGMTString();
      return "200";
    }
    return "404";
  }

  /**
   * 返回响应类型
   * 
   * @return
   */
  public String getContentType() {
    if (request == null) {
      return CONTENT_TYPE + ":" + HTML;
    }
    String uri = request.getRequestURI();
    // 简单处理文件类型。。。仅以后缀名判断
    String type = uri.substring(uri.lastIndexOf(".") + 1);
    String resultype = HTML;
    switch (type.toLowerCase()) {
    case "html":
      resultype = HTML;
      break;
    case "txt":
      resultype = TXT;
      break;
    case "js":
      resultype = JAVASCRIPT;
      break;
    case "css":
      resultype = CSS;
      break;
    case "png":
      resultype = PNG;
      break;
    case "jpeg":
      resultype = JPG;
      break;
    case "jpg":
      resultype = JPG;
      break;

    default:
      break;
    }
    return CONTENT_TYPE + ":" + resultype;
  }

  public String decodeURI(String uri) {
    try {
      return URLDecoder.decode(uri, encoding);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String encodeURI(String uri) {
    try {
      return URLEncoder.encode(uri, encoding);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static final String HTML = "text/html";
  public static final String TXT = "text/plain";
  public static final String JAVASCRIPT = "application/x-javascript";
  public static final String CSS = "text/css";
  public static final String PNG = "image/png";
  public static final String PNG_D = "application/x-png";
  public static final String JPG = "image/jpeg";
  public static final String JPG_D = "application/x-jpg";

}

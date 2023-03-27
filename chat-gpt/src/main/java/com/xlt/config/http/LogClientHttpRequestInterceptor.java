package com.xlt.config.http;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StopWatch;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 自定义的拦截器
 * <p>
 * 主要打印日志
 */
@Slf4j
public class LogClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        //获取请求内容
        String reqBody = "";
        //这里可以自定义想打印什么方法或者请求的内容，如下只打印post请求的日志，因为这时候才会带上body
        if (httpRequest.getHeaders().getContentType() != null && "POST".equals(httpRequest.getMethod().name())) {
            // String contentType = httpRequest.getHeaders().getContentType().toString();
            // 这里可以对contentType做一些判断，针对其格式化请求体的内容，如"application/x-www-form-urlencoded"格式会附带一些boundary(分割线)的内容
            reqBody = new String(body, StandardCharsets.UTF_8);
        }
        RestLog request = RestLog.builder()
                .headers(httpRequest.getHeaders())
                .method(httpRequest.getMethodValue())
                .reqUrl(httpRequest.getURI().toString())
                .reqBody(reqBody)
                .build();
        log.info("Call API as following\n >>>>>>Request={}", JSON.toJSONString(request));
        //记录请求开始时间
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        //执行请求
        ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, body);
        //执行完毕后，这里要创建备份，要不然会报io提前关闭的异常错误
        ClientHttpResponse responseCopy = new BufferingClientHttpResponseWrapper(response);
        //记录请求结束时间
        stopWatch.stop();
        //获取响应内容
        StringBuilder resBody = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(responseCopy.getBody(),
                StandardCharsets.UTF_8))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                resBody.append(line);
                line = bufferedReader.readLine();
            }
        }

        RestLog responseLog = RestLog.builder().costTime(stopWatch.getLastTaskTimeMillis()).resBody(resBody.toString()).resStatus(responseCopy.getRawStatusCode()).build();
        log.info("\n >>>>>>Response={}", JSON.toJSONString(responseLog));
        return responseCopy;
    }
}


/**
 * 响应内容备份
 */
final class BufferingClientHttpResponseWrapper implements ClientHttpResponse {

    private final ClientHttpResponse response;

    private byte[] body;


    BufferingClientHttpResponseWrapper(ClientHttpResponse response) {
        this.response = response;
    }


    @Override
    public HttpStatus getStatusCode() throws IOException {
        return this.response.getStatusCode();
    }

    @Override
    public int getRawStatusCode() throws IOException {
        return this.response.getRawStatusCode();
    }

    @Override
    public String getStatusText() throws IOException {
        return this.response.getStatusText();
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.response.getHeaders();
    }

    @Override
    public InputStream getBody() throws IOException {
        if (this.body == null) {
            this.body = StreamUtils.copyToByteArray(this.response.getBody());
        }
        return new ByteArrayInputStream(this.body);
    }

    @Override
    public void close() {
        this.response.close();
    }
}

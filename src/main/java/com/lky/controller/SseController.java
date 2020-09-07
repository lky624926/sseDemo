package com.lky.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@CrossOrigin()
public class SseController {

    private static ConcurrentHashMap<String,SseEmitter> userMap = new ConcurrentHashMap();
    /**
     * sse
     */
    @GetMapping(value = "/sse")
    public SseEmitter getSSE(
            HttpServletRequest request) {
        SseEmitter emitter;
        String userId = request.getParameter("userId");

        //设置超时时间，不然相应一次后会关闭连接，然后再建立新连接
            emitter = new SseEmitter(0L);
            userMap.put(userId, emitter);
        return emitter;
    }

    /**
     * sse发送请求
     */
    @GetMapping("sendToUser")
    public Object sendToUser(@RequestParam("userId")String userId){
        SseEmitter sseEmitter = userMap.get(userId);
        String s = "aaa";
        try { ;
            SseEmitter.SseEventBuilder test = SseEmitter.event().data(s);
                sseEmitter.send(s);
                //调该方法后立即关闭连接
//            sseEmitter.complete();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "OK";
    }
}

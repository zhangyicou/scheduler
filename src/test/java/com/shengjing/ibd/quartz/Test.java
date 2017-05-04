package com.shengjing.ibd.quartz;

import org.springframework.web.client.RestTemplate;

/**
 * Created by gongye1 on 2017/5/4.
 */
public class Test {
    private RestTemplate restTemplate = new RestTemplate();

    @org.junit.Test
    public void health(){
        String url = "http://192.168.93.203:30288/api/v1/scheduler";
        //url = "http://192.168.93.9:9604/sapi/v1/dataApi";
        //url = "http://116.62.102.152:31337/sapi/v1/dataApi";
//        url = "http://192.168.13.222:9604/sapi/v1/dataApi";
//        Object resp = this.restTemplate.getForObject(url.concat("/health"),  Object.class);
//        System.out.println(resp.toString());
    }
}

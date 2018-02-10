package jp.gr.java_conf.pekokun.springbootdemo.externalapi.app;

import org.springframework.web.bind.annotation.GetMapping;

@Api
public class DemoRestController {

    @GetMapping(path = "demo")
    public String get() {
        return "DEMO";
    }

}

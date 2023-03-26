package com.zhengqing.demo.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/9/04 18:30
 */
@Slf4j
@RestController
@RequestMapping("api/ognl")
@Api(tags = "test-ognl")
public class OgnlController {

    private static Integer LOCAL_NUM = 0;

    /**
     * curl http://127.0.0.1:666/api/ognl/test
     */
    @GetMapping("test")
    @ApiOperation("test")
    public String test() {
        LOCAL_NUM++;
        return "66";
    }

}

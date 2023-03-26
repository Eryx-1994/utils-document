# Java

### RedisTemplate

在Sentinel集群监管下的Redis主从集群，其节点会因为自动故障转移而发生变化，Redis的客户端必须感知这种变化，及时更新连接信息。
Spring的RedisTemplate底层利用lettuce实现了节点的感知和自动切换。

#### 1、`pom.xml`中引入依赖

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

#### 2、`application.yml`配置

```yml
spring:
  redis:
    sentinel:
      # 集群名
      master: mymaster
      # 集群节点
      nodes:
        - 127.0.0.1:26379
        - 127.0.0.1:26380
        - 127.0.0.1:26381
      # sentinel认证密码
      password: 123456
    # redis认证密码
    password: 123456
```

#### 3、配置读写分离

```
@Bean
public LettuceClientConfigurationBuilderCustomizer clientConfigurationBuilderCustomizer() {
    return clientConfigurationBuilder -> clientConfigurationBuilder.readFrom(ReadFrom.REPLICA_PREFERRED);
}
```

这个bean中配置的就是读写策略，包括四种：

- `MASTER`：从主节点读取
- `MASTER_PREFERRED`：优先从master节点读取，master不可用才读取replica
- `REPLICA`：从slave（replica）节点读取
- `REPLICA_PREFERRED`：优先从slave（replica）节点读取，所有的slave都不可用才读取master

#### 4、测试类

> tips: 可查看相应日志来判断读写分离等

```java
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("")
@Api(tags = "测试-接口")
public class TestController {

    @Resource
    private StringRedisTemplate redisTemplate;

    @PostMapping("redis")
    @ApiOperation("redis-保存数据")
    public String saveData(String key) {
        this.redisTemplate.opsForValue().set(key, "hello world - reids");
        return "SUCCESS";
    }

    @GetMapping("redis")
    @ApiOperation("redis-获取数据")
    public String getData(String key) {
        String dataStr = this.redisTemplate.opsForValue().get(key);
        log.info("{}", dataStr);
        return dataStr;
    }

}
```
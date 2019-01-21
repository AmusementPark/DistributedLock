package zsh.demos.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class CuratorConfiguration implements InitializingBean {

    @Autowired
    CuratorConfigurationProperties curatorConfiguration;
//    @Autowired
    CuratorFramework curatorFramework;

    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework() {
        RetryPolicy retryPolicy = new RetryNTimes(curatorConfiguration.getRetryCount(), curatorConfiguration.getElapsedTimeMs());
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
            .connectString(curatorConfiguration.getConnectString())
            .sessionTimeoutMs(curatorConfiguration.getSessionTimeoutMs())
            .connectionTimeoutMs(curatorConfiguration.getConnectionTimeoutMs())
            .retryPolicy(retryPolicy)
        .build();
        return curatorFramework;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(curatorFramework);
    }

    @Component
    class Runner implements InitializingBean {
        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println("12313");
        }
    }
}

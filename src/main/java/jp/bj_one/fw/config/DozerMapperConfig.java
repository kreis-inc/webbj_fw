package jp.bj_one.fw.config;

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class DozerMapperConfig {

  @Bean
  public DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean(
      @Value("classpath*:/META-INF/dozer/**/*-mapping.xml") Resource[] resources) throws Exception {
    final DozerBeanMapperFactoryBean dozerBean = new DozerBeanMapperFactoryBean();
    dozerBean.setMappingFiles(resources);
    return dozerBean;
  }
}

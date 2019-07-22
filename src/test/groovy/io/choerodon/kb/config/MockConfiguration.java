package io.choerodon.kb.config;

import io.choerodon.core.convertor.ApplicationContextHelper;
import io.choerodon.kb.app.service.impl.PageCommentServiceImpl;
import io.choerodon.kb.app.service.impl.PageLogServiceImpl;
import io.choerodon.kb.app.service.impl.PageVersionServiceImpl;
import io.choerodon.kb.app.service.impl.WorkSpaceServiceImpl;
import io.choerodon.kb.infra.feign.FileFeignClient;
import io.choerodon.kb.infra.feign.IamFeignClient;
import io.choerodon.kb.infra.feign.fallback.FileFeignClientFallback;
import io.choerodon.kb.infra.feign.fallback.IamFeignClientFallback;
import io.choerodon.kb.infra.feign.vo.UserDO;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * @author shinan.chen
 * @since 2019/7/22
 */
@Configuration
public class MockConfiguration {

    @Bean
    IamFeignClient iamFeignClient() {
        IamFeignClient iamFeignClient = Mockito.mock(IamFeignClientFallback.class);
        UserDO user = new UserDO();
        user.setId(1L);
        user.setRealName("test");
        Mockito.when(iamFeignClient.listUsersByIds(ArgumentMatchers.anyObject(), ArgumentMatchers.anyBoolean())).thenReturn(new ResponseEntity<>(Arrays.asList(user), HttpStatus.OK));
        WorkSpaceServiceImpl workSpaceService = ApplicationContextHelper.getSpringFactory().getBean(WorkSpaceServiceImpl.class);
        workSpaceService.setIamFeignClient(iamFeignClient);
        PageVersionServiceImpl pageVersionService = ApplicationContextHelper.getSpringFactory().getBean(PageVersionServiceImpl.class);
        pageVersionService.setIamFeignClient(iamFeignClient);
        PageCommentServiceImpl pageCommentService = ApplicationContextHelper.getSpringFactory().getBean(PageCommentServiceImpl.class);
        pageCommentService.setIamFeignClient(iamFeignClient);
        PageLogServiceImpl pageLogService = ApplicationContextHelper.getSpringFactory().getBean(PageLogServiceImpl.class);
        pageLogService.setIamFeignClient(iamFeignClient);
        return iamFeignClient;
    }

    @Bean
    FileFeignClient fileFeignClient() {
        FileFeignClient fileFeignClient = Mockito.mock(FileFeignClientFallback.class);
        String imageUrl = "https://minio.choerodon.com.cn/agile-service/file_56a005f56a584047b538d5bf84b17d70_blob.png";
        Mockito.when(fileFeignClient.uploadFile(anyString(), anyString(), any(MultipartFile.class))).thenReturn(new ResponseEntity<>(imageUrl, HttpStatus.OK));
        Mockito.when(fileFeignClient.deleteFile(anyString(), anyString())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        return fileFeignClient;
    }
}
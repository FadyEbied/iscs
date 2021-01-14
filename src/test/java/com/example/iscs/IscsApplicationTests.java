package com.example.iscs;

import com.example.iscs.controllers.HelloController;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@SpringBootTest
class IscsApplicationTests {

    private String footballFileName = "files/football.dat";
    private String wealthFilename = "files/wealth.dat";

    @Autowired
    private HelloController helloController;

    @Test
    void contextLoads() {
    }

    @Test
    public void testHelloPage(){
        log.info(helloController.getHelloPage());
    }

    @Test
    public void testUpload() throws IOException {
        File file = ResourceUtils.getFile("classpath:"+footballFileName);
        Assert.isTrue(file != null,"File exists");
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        MockMultipartFile footballMultipartFile = new MockMultipartFile("football.dat",bufferedInputStream );
        //helloController.uploadFile(footballMultipartFile);


    }

}

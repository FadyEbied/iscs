package com.example.iscs.controllers;

import com.example.iscs.utils.FileStrategy;
import com.example.iscs.utils.FootballFileStrategy;
import com.example.iscs.utils.WeatherFileStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller("/hello")
public class HelloController {

    @Autowired
    ResourceLoader resourceLoader;



    @GetMapping
    public String getHelloPage(){
        return "hello";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        String fileName = file.getOriginalFilename();

        //Validate FileName
        List<String> errorMessages = validateFileName(file);
        if (errorMessages.size()>0) {
            model.addAttribute("messages",errorMessages);
            return "hello";
        }

        // Store file
        //storeFile(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(),"UTF-8"));

        FileStrategy fileStrategy;
        if (fileName.equals("weather.dat")) {
            fileStrategy = new WeatherFileStrategy();
            fileStrategy.determineIndexes(reader.readLine());
            fileStrategy.doOperation(reader);
            model.addAttribute("type",WeatherFileStrategy.FILETYPE);
            model.addAttribute("day", ((WeatherFileStrategy) fileStrategy).getProperty().getKey());
            model.addAttribute("mnt", ((WeatherFileStrategy) fileStrategy).getProperty().getValue());
            model.addAttribute("ignoreRows", ((WeatherFileStrategy) fileStrategy).getIgnoreRows());
            return "result";
        }else if (fileName.equals("football.dat")) {
            fileStrategy = new FootballFileStrategy();
            fileStrategy.determineIndexes(reader.readLine());
            fileStrategy.doOperation(reader);
            model.addAttribute("type",FootballFileStrategy.FILETYPE);
            model.addAttribute("team", ((FootballFileStrategy) fileStrategy).getProperty().getKey());
            model.addAttribute("diff", ((FootballFileStrategy) fileStrategy).getProperty().getValue());
            model.addAttribute("ignoreRows", ((FootballFileStrategy) fileStrategy).getIgnoreRows());
            return "result";
        }
        return "hello";
    }


    private void storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        String destinationFolder = ClassLoader.getSystemClassLoader().getResource("files").getPath()+"/download/";
        //Path path = Paths.get(fileBasePath);
        File folderDownload = new File(destinationFolder);
        folderDownload.mkdirs();
        File fileDownload = new File(folderDownload.getAbsolutePath()+"/"+fileName);
        file.transferTo(fileDownload);
    }

    private List<String> validateFileName(MultipartFile file){
        List<String> errorMessages = new ArrayList<>();

        String fileName = file.getOriginalFilename();

        // Validate File
        if (file.getSize()>10000000L)
            errorMessages.add("Uploaded file is too large");

        if (fileName.contains(".")){
            String extension = file.getOriginalFilename().substring(fileName.length()-4);
            if (!extension.equals(".dat"))
                errorMessages.add("Wrong in extension file");
        }else{
            errorMessages.add("Upload file doesn't have extension");
        }

        return errorMessages;
    }

}

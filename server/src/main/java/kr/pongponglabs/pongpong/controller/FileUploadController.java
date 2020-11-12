package kr.pongponglabs.pongpong.controller;

import kr.pongponglabs.pongpong.*;
import kr.pongponglabs.pongpong.message.Response;
import kr.pongponglabs.pongpong.service.StorageService;
import kr.pongponglabs.pongpong.sql.User;
import kr.pongponglabs.pongpong.sql.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;

@Controller
public class FileUploadController {
    @Autowired
    private UserRepository userRepository;
    private final StorageService storageService;

    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload-file")
    @ResponseBody
    public Response<java.io.Serializable> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        User user = new User(fileName, "Uploading", "{}");

        user = userRepository.save(user);

        storageService.store(file);

        user.setState("Unzipping");
        user = userRepository.save(user);

        UnzipFile unzipFile = new UnzipFile(storageService.load(fileName));
        unzipFile.unzip(fileName);

        user.setState("Analyzing");
        user = userRepository.save(user);
        SonarScannerRunner runner = new SonarScannerRunner();

        runner.run(fileName, new Language(Language.Type.Other));

        user.setState("Parsing report");
        user = userRepository.save(user);

        SonarQubeRequester requester = new SonarQubeRequester(fileName);

        JSONObject report = requester.getSonarReport();

        if(report != null) {
            SonarReportParser parser = new SonarReportParser(report);

            var massages = parser.parse();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < massages.size(); i++) {
                sb.append(massages.get(i));

                if(i != massages.size() - 1)
                    sb.append(",");
            }

            user.setState("Finish");
            user.setResult(sb.toString());

            userRepository.save(user);

            return new Response<>(true, "Report", massages);
        } else
            System.out.println("Fail to get report");

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(fileName)
                .toUriString();

        return new Response<>(false, "Not a report", "");
    }
}
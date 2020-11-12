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

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Controller
public class CodeUploadController {
    @Autowired
    private UserRepository userRepository;

    public CodeUploadController() { }

    @PostMapping("/upload-code")
    @ResponseBody
    public Response<Serializable> uploadCode(@RequestBody User user) {
        String fileName = user.getFileName();
        Language language = new Language(fileName);

        user.setState("Ready To Analyzing");
        user = userRepository.save(user);

        CodeToProjectConverter converter = new CodeToProjectConverter();

        converter.run(fileName, language, user.getCode());

        user.setState("Analyzing");
        user = userRepository.save(user);

        if(language.type == Language.Type.C || language.type == Language.Type.CPP) {
            CppCheckRunner runner = new CppCheckRunner();

            String res = runner.run(fileName, language);

            user.setState("Finish");
            user.setResult(res);

            userRepository.save(user);

            return new Response<>(true, "Report", "Fin");
        } else {
            SonarScannerRunner runner = new SonarScannerRunner();

            runner.run(fileName, language);

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
        }

        return new Response<>(false, "Not a report", user.getCode());
    }
}

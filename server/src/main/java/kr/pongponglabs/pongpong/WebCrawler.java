package kr.pongponglabs.pongpong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebCrawler {
    public String run(ArrayList<String> massages) {
        JSONObject obj = new JSONObject();
        JSONArray errorsArray = new JSONArray();

        for (String m : massages) {
            JSONArray array = new JSONArray();

            array.put(m);

            for(int i = 1; i <= 5; i++) {
                try {
                    JSONObject error = new JSONObject();

                    error.put("title", "Test Title " + String.valueOf(i));
                    error.put("link", "Test Link " + String.valueOf(i));

                    array.put(error);
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }

            errorsArray.put(array);
        }

//        for (String m : massages) {
//            System.out.println("Message : " + m + '\n');
//
//            ProcessBuilder processBuilder = new ProcessBuilder();
//
//            processBuilder.command("python3", "crawling.py", m);
//
//            try {
//                var process = processBuilder.start();
//
//                try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                    JSONArray array = new JSONArray();
//                    JSONObject errors = new JSONObject();
//                    String line;
//                    int cnt = 0;
//
//                    array.put(m);
//
//                    while ((line = reader.readLine()) != null) {
//                        if(cnt++ % 2 == 0)
//                            errors.put("title", line);
//                        else
//                            errors.put("link", line);
//                    }
//
//                    array.put(errors);
//
//                    errorsArray.put(array);
//
//                    int len;
//                    if ((len = process.getErrorStream().available()) > 0) {
//                        byte[] buf = new byte[len];
//                        process.getErrorStream().read(buf);
//                        System.err.println("Command error:\t\"" + new String(buf) + "\"");
//                    }
//                }
//            } catch (IOException | JSONException e) {
//                e.printStackTrace();
//            }
//        }

        try {
            obj.put("errors", errorsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj.toString();
    }
}

package kr.pongponglabs.pongpong;

import ch.qos.logback.core.joran.util.beans.BeanDescriptionFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SonarQubeRequester {
    String projectName;

    public SonarQubeRequester(String projectName) {
        this.projectName = projectName;
    }

    private String checkUploaded() {
        try {
            String url = "http://164.125.219.21:6379/api/qualitygates/project_status?projectKey=" + projectName;

            OkHttpClient client = new OkHttpClient();

            Request.Builder builder = new Request.Builder().url(url).get();

            Request request = builder.build();

            for(int i = 0; i < 5; i++) {
                TimeUnit.SECONDS.sleep(1);

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    String responseStr = response.peekBody(2048).string();

                    if(responseStr.length() == 0)
                        continue;

                    JSONObject jsonObj = new JSONObject(responseStr);
                    String projectStatus = jsonObj.getJSONObject("projectStatus").getString("status");

                    if (projectStatus.equals("OK"))
                        return "Success";
                }
                else
                    System.out.println("Response Fail");
            }

            System.out.println("Fail to get report");
        } catch(Exception e) {
            e.printStackTrace();
        }

        return "Fail";
    }

    public JSONObject getSonarReport() {
        String uploadState = checkUploaded();

        if(uploadState.equals("Fail"))
            return null;

        try {
            String url = "http://164.125.219.21:6379/api/issues/search?severities=BLOCKER,CRITICAL,MAJOR,MINOR,INFO&componentKeys=" + projectName;

            OkHttpClient client = new OkHttpClient();

            Request.Builder builder = new Request.Builder().url(url).get();
            Request request = builder.build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                ResponseBody body = response.body();

                if(body != null)
                    return new JSONObject(body.string());
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

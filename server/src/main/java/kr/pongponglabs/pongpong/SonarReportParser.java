package kr.pongponglabs.pongpong;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SonarReportParser {
    JSONObject report;

    public SonarReportParser(JSONObject report) {
        this.report = report;
    }

    public ArrayList<String> parse() {
        ArrayList<String> massages = new ArrayList<String>();

        try {
            int total = report.getInt("total");
            JSONArray issues = report.getJSONArray("issues");

            for(int i = 0; i < total; i++) {
                JSONObject issue = issues.getJSONObject(i);

                massages.add(issue.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return massages;
    }
}

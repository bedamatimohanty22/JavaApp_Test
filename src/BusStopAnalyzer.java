import java.io.IOException;
import java.util.*;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONObject;

public class BusStopAnalyzer {
    private static final String LINE_DATA_API = "https://api.sl.se/api2/LineData.json?key=caaf3097ac44441e93dc9e92c1024d3c&model=";
    private static final String STOP_DATA_API = "https://api.sl.se/api2/LineData.json?key=caaf3097ac44441e93dc9e92c1024d3c&model=StopPoint&DefaultTransportModeCode=BUS";

    public static void main(String[] args) {
        Map<String, Integer> lineStopCount = new HashMap<>();

        try {
            // Get all lines and their corresponding stops
            JSONObject response = getJSONResponse(LINE_DATA_API + "JourneyPatternPointOnLine&DefaultTransportModeCode=BUS");
            JSONArray lines = response.getJSONObject("ResponseData").getJSONArray("Result");

            for (int i = 0; i < lines.length(); i++) {
                JSONObject line = lines.getJSONObject(i);
                String lineNum = line.getString("LineNumber");
                String dirCode = line.getString("DirectionCode");

                // Filter out lines with DirectionCode = 2
                if (dirCode.equals("2")) {
                    continue;
                }

                int count = lineStopCount.getOrDefault(lineNum, 0);
                lineStopCount.put(lineNum, count + 1);
            }

            // Sort lines by stop count in descending order
            List<Map.Entry<String, Integer>> sortedLines = new ArrayList<>(lineStopCount.entrySet());
            sortedLines.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

            // Get top 10 lines
            List<String> top10Lines = new ArrayList<>();
            for (int i = 0; i < 10 && i < sortedLines.size(); i++) {
                top10Lines.add(sortedLines.get(i).getKey());
            }

            // Get all bus stops
            JSONArray stops = getJSONResponse(STOP_DATA_API).getJSONObject("ResponseData").getJSONArray("Result");

            // Print top 10 lines and their stops
            for (String lineNum : top10Lines) {
                System.out.println("Line " + lineNum + " - " + lineStopCount.get(lineNum) + " stops:");
                for (int i = 0; i < stops.length(); i++) {
                    JSONObject stop = stops.getJSONObject(i);
                    if (stop.getString("StopPointNumber").startsWith(lineNum)) {
                        System.out.println("  " + stop.getString("StopPointName"));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONObject getJSONResponse(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        URLConnection connection = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return new JSONObject(response.toString());
    }
}

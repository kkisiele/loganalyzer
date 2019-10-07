package loganalyzer.infrastructure.log;

import loganalyzer.Log;
import loganalyzer.OpsSupport;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

final class JsonLogAdapter {
    private final OpsSupport opsSupport;
    private final Gson gson = new Gson();

    public JsonLogAdapter(OpsSupport opsSupport) {
        this.opsSupport = opsSupport;
    }

    public Log toLog(String json) {
        if(json.trim().length() > 0) {
            return deserialize(json);
        }
        return null;
    }

    private Log deserialize(String json) {
        try {
            return gson.fromJson(json, Log.class);
        } catch(JsonSyntaxException ex) {
            opsSupport.invalidLogFormat(json);
            return null;
        }
    }
}

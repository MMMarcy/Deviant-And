package mmmarcy.github.deviantand.deviantart;

import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;


import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.Assert.assertTrue;

/**
 * Created by marcello on 07/06/15.
 */
public class DeviationTest {

    String jsonString;
    byte[] buffer = new byte[8168];

    @Before
    public void setUp() throws Exception {
        ClassLoader.getSystemResourceAsStream("deviations.json").read(buffer);
        jsonString = new String(buffer);
    }

    @Test
    public void testDeserialization() {
        JsonParser parser = new JsonParser();
        JsonReader reader = new JsonReader(new StringReader(jsonString));
        reader.setLenient(true);
        JsonObject object = parser.parse(reader).getAsJsonObject();
        JsonArray array = object.get("results").getAsJsonArray();
        Gson gson = new GsonBuilder().create();
        for(JsonElement element: array){
            gson.fromJson(array.get(0), Deviation.class);
        }
        assertTrue(true);
    }
}
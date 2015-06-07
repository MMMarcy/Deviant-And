package mmmarcy.github.deviantand.deviantart;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by marcello on 07/06/15.
 */
public class Thumbnail extends GenericJson {

    @Key
    private String src;

    @Key
    private Integer height;

    @Key
    private Integer width;

    @Key
    private Boolean transparency;

}

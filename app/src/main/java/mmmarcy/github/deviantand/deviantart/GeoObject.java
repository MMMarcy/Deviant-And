package mmmarcy.github.deviantand.deviantart;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by marcello on 07/06/15.
 */
public class GeoObject extends GenericJson {

    @Key
    private String country;

    @Key
    private Integer countryid;

    @Key
    private String timezone;

}

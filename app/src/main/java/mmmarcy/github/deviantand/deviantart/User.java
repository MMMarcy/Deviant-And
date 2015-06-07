package mmmarcy.github.deviantand.deviantart;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by marcello on 07/06/15.
 */
public class User extends GenericJson{

    @Key
    private String userid;

    @Key
    private String username;

    @Key
    private String usericon;

    @Key
    private String type;

    @Key
    private Boolean is_watching;

    @Key
    private UserDetail details;

    @Key
    private GeoObject geo;

    @Key
    private UserProfile profile;

    @Key
    private UserStats stats;
}

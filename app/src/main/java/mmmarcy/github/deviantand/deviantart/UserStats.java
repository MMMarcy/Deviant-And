package mmmarcy.github.deviantand.deviantart;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by marcello on 07/06/15.
 */
public class UserStats extends GenericJson {

    @Key
    private Integer watchers;

    @Key
    private Integer friends;
}

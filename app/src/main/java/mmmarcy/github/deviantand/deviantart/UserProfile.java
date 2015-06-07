package mmmarcy.github.deviantand.deviantart;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 *
 * Created by Marcello Steiner on 07/06/15.
 */
public class UserProfile extends GenericJson {

    @Key
    private Boolean user_is_artist;

    @Key
    private String artist_level;

    @Key
    private String artist_speciality;

    @Key
    private String real_name;

    @Key
    private String tagline;

    @Key
    private String website;

    @Key
    private String cover_photo;

    @Key
    private Deviation profile_pic;
}

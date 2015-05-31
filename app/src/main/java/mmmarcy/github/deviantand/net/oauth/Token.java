package mmmarcy.github.deviantand.net.oauth;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import java.io.Serializable;

/**
 * Created by marcello on 20/05/15.
 */
public class Token extends GenericJson implements Serializable {

    @Key
    private Double expires_in;

    @Key
    private String status;

    @Key
    private String access_token;

    @Key
    private String token_type;

    @Key
    private String refresh_token;

    @Key
    private String scope;

}

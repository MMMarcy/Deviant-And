package mmmarcy.github.deviantand.deviantart;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by marcello on 07/06/15.
 */
public class UserDetail extends GenericJson {


    @Key
    private String sex;

    @Key
    private Integer age;

    @Key
    private String joindate;


}

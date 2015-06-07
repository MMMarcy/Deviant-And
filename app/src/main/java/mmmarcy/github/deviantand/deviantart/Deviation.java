package mmmarcy.github.deviantand.deviantart;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by marcello on 07/06/15.
 */
public class Deviation extends GenericJson {

    @Key
    private String deviationId;

    @Key
    private String printId;

    @Key
    private String url;

    @Key
    private String category;

    @Key
    private String category_path;

    @Key
    private Boolean is_favourited;

    @Key
    private Boolean is_deleted;

    @Key
    private DeviationStats stats;

    @Key
    private Integer published_time;

    @Key
    private Boolean allows_comments;

    @Key
    private DeviationPreview preview;

    @Key
    private Boolean is_mature;

}

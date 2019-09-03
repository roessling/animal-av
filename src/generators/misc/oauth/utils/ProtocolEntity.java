package generators.misc.oauth.utils;

/**
 * Created by Vincent on 04/07/16.
 */
public abstract class ProtocolEntity implements Comparable<OAuthEntity> {


    public int id;

    /**
     * Sets the Animal Properties
     */

    public ProtocolEntity(){
        init();
    }
    abstract void init();

    @Override
    public int compareTo(OAuthEntity entity) {
        int comparedSize = entity.id;

        if (this.id > comparedSize) {
            return 1;
        } else if (this.id == comparedSize) {
            return 0;
        } else {
            return -1;
        }
    }

}

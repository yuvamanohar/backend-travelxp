package cdn;

import java.io.File;

/**
 * Created by yuva on 25/4/17.
 */
public class CdnRequest {
    File src ;
    String cdnRelativePath ;
    public String key ;
    boolean status ;

    private CdnRequest(File src, String cdnRelativePath, String key) {
        this.src = src ;
        this.cdnRelativePath = cdnRelativePath ;
        this.key = key ;
    }

    public static CdnRequest get(File src, String cdnRelativePath, String key) {
        return new CdnRequest(src, cdnRelativePath, key) ;
    }

    CdnRequest setStatus(boolean status) {
        this.status = status ;
        return this ;
    }

    public boolean getStatus() {
        return status ;
    }
}

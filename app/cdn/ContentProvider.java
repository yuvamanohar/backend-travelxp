package cdn;

import com.google.inject.Inject;

/**
 * Created by yuva on 25/4/17.
 */
public class ContentProvider {
    private CdnExecutionContext cdnExecutionContext ;

    @Inject
    public ContentProvider(CdnExecutionContext cdnExecutionContext) {
        this.cdnExecutionContext = cdnExecutionContext ;
    }



}

package cdn;

import com.google.inject.Inject;
import org.apache.commons.io.FileUtils;
import play.libs.F;
import utils.Config;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Created by yuva on 25/4/17.
 */
public class LocalCdnHelper implements ICdn {
    private CdnExecutionContext cdnExecutionContext ;

    @Inject
    public LocalCdnHelper(CdnExecutionContext cdnExecutionContext) {
        this.cdnExecutionContext = cdnExecutionContext ;
    }

    private CdnRequest pushToCdn(CdnRequest request) {
        try {
            File cdnFile = new File(Config.CDN_LOCATION + request.cdnRelativePath) ;
            if(cdnFile.exists())
                cdnFile.delete() ;
            FileUtils.moveFile(request.src, cdnFile) ;
        } catch (IOException ioe) {
            ioe.printStackTrace() ;
            return request.setStatus(false) ;
        }
        return request.setStatus(true) ;
    }

    private List<CdnRequest> pushToCdnBatch(List<CdnRequest> cdnRequests) {
        for(CdnRequest cdnRequest : cdnRequests) {
            pushToCdn(cdnRequest) ;
        }

        return cdnRequests ;
    }

    @Override
    public CompletionStage<CdnRequest> pushToCdnAsync(CdnRequest cdnRequest) {
        return supplyAsync(() -> pushToCdn(cdnRequest), cdnExecutionContext) ;
    }

    @Override
    public CompletionStage<List<CdnRequest>> pushToCdnAsyncBatch(List<CdnRequest> cdnRequests) {
        return supplyAsync(() -> pushToCdnBatch(cdnRequests), cdnExecutionContext);
    }

}

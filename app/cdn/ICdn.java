package cdn;

import com.google.inject.ImplementedBy;

import javax.persistence.Tuple;
import java.io.File;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by yuva on 25/4/17.
 */
@ImplementedBy(LocalCdnHelper.class)
public interface ICdn {
    public CompletionStage<CdnRequest> pushToCdnAsync(CdnRequest cdnRequest) ;

    public CompletionStage<List<CdnRequest>> pushToCdnAsyncBatch(List<CdnRequest> cdnRequests) ;
}

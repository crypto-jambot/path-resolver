package dev.rabbit.transform;

import com.google.api.services.bigquery.model.TableRow;
import dev.rabbit.doFn.DepthFirstSearch;
import org.apache.beam.sdk.transforms.Combine;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;

public class ResolvePaths extends PTransform<PCollection<TableRow>, PCollection<TableRow>> {

    public static ResolvePaths of() {
        return new ResolvePaths();
    }

    @Override
    public PCollection<TableRow> expand(PCollection<TableRow> input) {
        return input
                .apply(Combine.globally(CombineToList.of()))
                .apply(ParDo.of(DepthFirstSearch.of()));
    }
}

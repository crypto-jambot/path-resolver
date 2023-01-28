package dev.rabbit;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.StorageOptions;
import dev.rabbit.doFn.ResolvePaths;
import dev.rabbit.function.MakeImmutableFn;
import dev.rabbit.transform.BuildGraph;
import dev.rabbit.util.Mapper;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.transforms.Combine;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.TypeDescriptors;

public class PathResolverPipeline {

    public static void run(PathResolverPipelineOptions options) {

        StorageOptions.getDefaultInstance().getService().get("resolved-paths").list().iterateAll().forEach(Blob::delete);

        Pipeline pipeline = Pipeline.create(options);

        pipeline.apply("Query for graph data", BigQueryIO.readTableRows()
                        .fromQuery(options.getQueryString())
                        .withoutValidation()
                        .withTemplateCompatibility()
                        .usingStandardSql())
                .apply("Build the graph", Combine.globally(BuildGraph.of()))
                .apply("Make the graph Immutable", MapElements.via(MakeImmutableFn.of()))
                .apply("Run a DFS and resolve all possible paths", ParDo.of(ResolvePaths.of()))
                .apply("Map to JSON", MapElements.into(TypeDescriptors.strings()).via(Mapper.INSTANCE::toJsonString))
                .apply("Write to GCS", TextIO.write().to("gs://resolved-paths/paths").withNumShards(options.getShardNumber()).withSuffix(".jsonl"));

        pipeline.run();
    }
}

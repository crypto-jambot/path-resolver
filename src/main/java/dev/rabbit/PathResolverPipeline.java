package dev.rabbit;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableRow;
import com.google.api.services.bigquery.model.TableSchema;
import dev.rabbit.doFn.ResolvePaths;
import dev.rabbit.function.MakeImmutableFn;
import dev.rabbit.pojo.Path;
import dev.rabbit.transform.BuildGraph;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.transforms.Combine;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.Reshuffle;
import org.apache.beam.sdk.values.TypeDescriptor;

import java.util.Arrays;

public class PathResolverPipeline {

    public static void run(PathResolverPipelineOptions options) {

        Pipeline pipeline = Pipeline.create(options);

        pipeline.apply("Query for graph data", BigQueryIO.readTableRows()
                        .fromQuery(options.getQueryString())
                        .withoutValidation()
                        .withTemplateCompatibility()
                        .usingStandardSql())
                .apply("Build the graph", Combine.globally(BuildGraph.of()))
                .apply("Make the graph Immutable", MapElements.via(MakeImmutableFn.of()))
                .apply("Run a DFS and resolve all possible paths", ParDo.of(ResolvePaths.of(options.getSearchDepth())))
                .apply("Break Fusion", Reshuffle.viaRandomKey())
                .apply("Map Path objects to TableRows", MapElements.into(TypeDescriptor.of(TableRow.class)).via(Path::toTableRow))
                .apply("Write paths to table after truncating.", BigQueryIO.writeTableRows()
                        .to(options.getDestinationTable())
                        .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
                        .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_TRUNCATE)
                        .withSchema(getSchema()));

        pipeline.run();
    }

    public static TableSchema getSchema() {
        return new TableSchema().setFields(
                Arrays.asList(
                        new TableFieldSchema()
                                .setName("tokenPath")
                                .setType("STRUCT")
                                .setMode("REPEATED")
                                .setDescription("The path of trading. The first trade would be between the 1st and 2nd element in the array and so on.")
                                .setFields(
                                        Arrays.asList(
                                                new TableFieldSchema().setName("name").setType("STRING").setMode("NULLABLE"),
                                                new TableFieldSchema().setName("symbol").setType("STRING").setMode("NULLABLE"),
                                                new TableFieldSchema().setName("address").setType("STRING").setMode("REQUIRED"),
                                                new TableFieldSchema().setName("decimals").setType("INTEGER").setMode("NULLABLE"))),
                        new TableFieldSchema()
                                .setName("swapPath")
                                .setType("STRUCT")
                                .setMode("REPEATED")
                                .setDescription("Options for each trade along the path. A list of pools that the trade in question can be executed on.")
                                .setFields(
                                        Arrays.asList(
                                                new TableFieldSchema().setName("exchanges").setType("STRUCT").setMode("REPEATED")
                                                        .setFields(
                                                                Arrays.asList(
                                                                        new TableFieldSchema().setName("name").setType("STRING").setMode("REQUIRED"),
                                                                        new TableFieldSchema().setName("fee").setType("INTEGER").setMode("REQUIRED"),
                                                                        new TableFieldSchema().setName("pool").setType("STRING").setMode("REQUIRED"))),
                                                new TableFieldSchema().setName("ordered").setType("BOOL").setMode("REQUIRED")))));
    }
}

package dev.rabbit;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableRow;
import com.google.api.services.bigquery.model.TableSchema;
import dev.rabbit.transform.ResolvePaths;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.values.TypeDescriptors;

import java.util.Arrays;

public class PathResolverPipeline {

    public static void run(PathResolverPipelineOptions options) {

        System.out.println("Starting the algorithm.");
        long startTime = System.currentTimeMillis();

        Pipeline pipeline = Pipeline.create(options);
        options.setEnableStreamingEngine(true);

        pipeline.apply("Query for graph data", BigQueryIO.readTableRows()
                        .fromQuery(options.getQueryString())
                        .withoutValidation()
                        .withTemplateCompatibility()
                        .usingStandardSql())
                .apply("Resolve Paths", ResolvePaths.of())
                .apply(MapElements.into(TypeDescriptors.strings()).via(TableRow::toString))
                .apply(TextIO.write().to("output/test.jsonl").withoutSharding());
//                .apply("Write paths to table after truncating.", BigQueryIO.writeTableRows()
//                        .to(options.getDestinationTable())
//                        .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
//                        .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_TRUNCATE)
//                        .withSchema(getSchema()));

        pipeline.run();

        long endTime = System.currentTimeMillis();
        System.out.println("Time Taken: " + (endTime - startTime) / 1000 + "seconds.");
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
                                .setName("exchangePath")
                                .setType("STRUCT")
                                .setMode("REPEATED")
                                .setDescription("Options for each trade along the path. A list of pools that the trade in question can be executed on.")
                                .setFields(
                                        Arrays.asList(
                                                new TableFieldSchema().setName("options").setType("STRUCT").setMode("REPEATED")
                                                        .setFields(
                                                                Arrays.asList(
                                                                        new TableFieldSchema().setName("name").setType("STRING").setMode("REQUIRED"),
                                                                        new TableFieldSchema().setName("fee").setType("INTEGER").setMode("REQUIRED"),
                                                                        new TableFieldSchema().setName("pool").setType("STRING").setMode("REQUIRED"))),
                                                new TableFieldSchema().setName("ordered").setType("BOOL").setMode("REQUIRED")
                                        ))));
    }
}

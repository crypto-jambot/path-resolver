package dev.rabbit;

import org.apache.beam.sdk.options.PipelineOptionsFactory;

public class App {

    public static void main(String[] args) {
        PathResolverPipelineOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().as(PathResolverPipelineOptions.class);
        PathResolverPipeline.run(options);
    }
}

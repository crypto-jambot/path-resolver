package dev.rabbit;

import org.apache.beam.sdk.options.PipelineOptionsFactory;

public class App {

    public static void main(String[] args) {
        System.out.println("hello world");
        PathResolverPipelineOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().as(PathResolverPipelineOptions.class);
        PathResolverPipeline.run(options);
    }
}

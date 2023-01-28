package dev.rabbit;

import org.apache.beam.sdk.extensions.gcp.options.GcpOptions;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.ValueProvider;

import static org.apache.beam.sdk.options.Validation.Required;

public interface PathResolverPipelineOptions extends GcpOptions {
    @Required
    @Default.String("crypto-351521")
    ValueProvider<String> getGcpProject();

    void setGcpProject(ValueProvider<String> value);

    @Required
    @Default.String("SELECT * FROM `crypto-351521.ethereum_v2.graph`")
    ValueProvider<String> getQueryString();

    void setQueryString(ValueProvider<String> value);

    @Required
    @Default.String("crypto-351521:ethereum_v2.paths")
    ValueProvider<String> getDestinationTable();

    void setDestinationTable(ValueProvider<String> value);

    @Required
    @Default.Integer(1000)
    ValueProvider<Integer> getShardNumber();

    void setShardNumber(ValueProvider<Integer> value);
}

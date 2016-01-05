package application;

import application.configuration.TestWhateverConfiguration;
import application.resources.TestingResource;
import aspects.GraphiteAspect;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by omryhay on 01/11/2015.
 */
public class TestWhateverApplication extends Application<TestWhateverConfiguration>
{
    public static void main(String[] args) throws Exception
    {
        new TestWhateverApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<TestWhateverConfiguration> bootstrap)
    {
        bootstrap.addBundle(new SwaggerBundle<TestWhateverConfiguration>()
        {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(TestWhateverConfiguration configuration)
            {
                return configuration.swaggerBundleConfiguration;
            }
        });

        bootstrap.addBundle(new AssetsBundle("/assets", "/assets", "assets/test_page.html"));
    }

    @Override
    public void run(TestWhateverConfiguration testWhateverConfiguration, Environment environment) throws UnknownHostException
    {
        StatsDClient statsDClient = new NonBlockingStatsDClient(String.format("wochit.test.dev.%s",
                InetAddress.getLocalHost().getHostName()), "grapfana.wochit.com", 8125);
        GraphiteAspect.initialize(statsDClient);

        environment.jersey().setUrlPattern("/api/*");

        // register the plan resource
        environment.jersey().register(TestingResource.class);
    }
}
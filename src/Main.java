import com.google.inject.Guice;
import com.google.inject.Injector;
import config.InjectorConfig;
import view.ViewInitializer;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/20/14.
 */
class Main {

    public static void main(String... args) {
        Injector injector = Guice.createInjector(new InjectorConfig());
        ViewInitializer initializer = injector.getInstance(ViewInitializer.class);
        initializer.run(args);
    }
}

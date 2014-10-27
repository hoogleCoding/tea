package config;

import com.google.inject.AbstractModule;
import controller.Database;
import controller.SQLite;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/26/14.
 */
public class InjectorConfig extends AbstractModule {
    @Override
    protected void configure() {
        bind(Database.class).to(SQLite.class).asEagerSingleton();
    }
}

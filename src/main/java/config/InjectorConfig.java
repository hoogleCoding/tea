package config;

import viewmodel.account.AccountEditViewModel;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import controller.database.Database;
import controller.database.DatabaseController;
import controller.database.SQLite;
import controller.layout.OverlayProvider;
import view.MainWindow;
import view.transaction.TransactionEditView;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Injector config for Guice.
 * Created by Florian Hug <florian.hug@gmail.com> on 10/26/14.
 */
public class InjectorConfig extends AbstractModule {
    @Override
    protected void configure() {
        bind(Database.class).to(SQLite.class).asEagerSingleton();
        bind(DatabaseController.class);
        bind(OverlayProvider.class).to(MainWindow.class);
        bind(AccountEditViewModel.class);
        bind(TransactionEditView.class);
        bind(ResourceBundle.class)
                .annotatedWith(Names.named("i18n-resources"))
                .toInstance(ResourceBundle.getBundle("fugger", Locale.forLanguageTag("en")));
    }
}

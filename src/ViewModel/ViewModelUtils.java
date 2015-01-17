package ViewModel;

import java.util.List;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 1/5/15.
 */
public class ViewModelUtils {
    private ViewModelUtils() {
    }

    public static String flattenMessages(final List<String> messages) {
        return messages
                .stream()
                .reduce("", (a, b) -> String.format("%s\n%s", a, b)).replaceFirst("\n", "");
    }
}

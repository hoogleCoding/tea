package controller.layout;

import javafx.scene.Node;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 12/12/14.
 */
public interface OverlayProvider {
    /**
     * Shows the provided node in an overlay. Returns false if another node is already shown.
     *
     * @param node The node to show.
     * @return True if the node is shown; false otherwise.
     */
    boolean show(final Node node);

    /**
     * Disposes the node which is currently overlayed and clears the overlay.
     */
    void dispose();
}

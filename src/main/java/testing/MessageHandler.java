package testing;

import Utilities.Message;

/**
 * Provides an interface to handle messages from the software bus
 */
@FunctionalInterface
public interface MessageHandler {
    void handle(Message message);
}
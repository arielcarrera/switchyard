package org.switchyard.security.jboss.wildfly;

import org.jboss.logging.Messages;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;

/**
 * <p/>
 * This file is using the subset 14700-14799 for logger messages.
 * <p/>
 */
@MessageBundle(projectCode = "SWITCHYARD")
public interface WildflySecurityMessages {

    /**
     * Default messages.
     */
    WildflySecurityMessages MESSAGES = Messages.getBundle(WildflySecurityMessages.class);

    /**
     * credentialsNotSet method definition.
     * @return IllegalStateException
     */
    @Message(id = 14700, value = "Credentials not set")
    IllegalStateException credentialsNotSet();

}

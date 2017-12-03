package org.apache.airavata.agents.core;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public enum AsyncCommandStatus {

    PENDING,
    RUNNING,
    SUCCESS,
    PAUSE,
    WARNING,
    ERROR,
    CANCEL,
    MANUAL_RECOVERY;
}

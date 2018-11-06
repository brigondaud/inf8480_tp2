package com.inf8480_tp2.shared.response;

import java.io.Serializable;

/**
 * A Response sent by a calculator server.
 * 
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public abstract class Response implements Serializable {

    private boolean isSuccessful;

    public Response(boolean success) {
        this.isSuccessful = success;
    }

    /**
     * Denotes if the operation associated to this response was successful on the server or not.
     *
     * @return a Boolean set to true if the operation was successful, false otherwise.
     */
    public boolean isSuccessful() {
        return this.isSuccessful;
    }
}

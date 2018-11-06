package com.inf8480_tp2.shared.response;

/**
 * @author Baptiste Rigondaud and Loïc Poncet
 */
public class ComputeResponse extends Response {

    private int result;

    public ComputeResponse(int result) {
        super(true);
        this.result = result;
    }

    /**
     * Get the result of the computation made by the server
     *
     * @return The result associated to this Response object
     */
    public int getResult() {
        return this.result;
    }
}

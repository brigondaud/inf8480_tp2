package com.inf8480_tp2.repartitor.executor;

import com.inf8480_tp2.repartitor.Repartitor;
import com.inf8480_tp2.shared.operations.Operation;
import com.inf8480_tp2.shared.response.ComputeResponse;
import com.inf8480_tp2.shared.response.Response;

/**
 * The check executor makes sure that a computation of a task gives the same
 * result on two different computation servers before giving the result
 * to the repartitor.
 * 
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public class NoCheckExecutor extends Executor {

    public NoCheckExecutor(Repartitor repartitor) {
        super(repartitor);
    }
    
    /**
     * If the response is successful, the executor returns the result
     * of the computation. Otherwise it uncompiles the task.
     * 
     * @param task The task for which a response was received.
     * @param response The response sent by the server.
     */
    @Override
    public void onReceive(Operation task, Response response) {
        if(!response.isSuccessful()) {
            // Either out of capacity or bad credentials.
            uncompileTask(task);
            return;
        }
        ComputeResponse computation = (ComputeResponse)response;
        getRepartitor().computeResult(computation.getResult());
    }
    
}

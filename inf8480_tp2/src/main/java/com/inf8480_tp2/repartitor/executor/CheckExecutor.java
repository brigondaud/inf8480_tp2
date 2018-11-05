package com.inf8480_tp2.repartitor.executor;

import com.inf8480_tp2.repartitor.Repartitor;
import com.inf8480_tp2.shared.operations.Operation;
import com.inf8480_tp2.shared.response.ComputeResponse;
import com.inf8480_tp2.shared.response.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * The check executor has a simple behavior. On the reception of a successful
 * compute response, it takes the result as correct.
 * 
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public class CheckExecutor extends Executor {
    
    /**
     * Holds the information of which task has already received a response,
     * to compare the results before sending them to the repartitor.
     */
    private Map<Operation, Response> responseReceived;

    public CheckExecutor(Repartitor repartitor) {
        super(repartitor);
        this.responseReceived = new HashMap();
    }
    
    /**
     * When a response to a task is received, the executor checks if a response
     * has already been received to compare the results before sending one to
     * the repartiror.
     * 
     * @param task The task for which a response was received.
     * @param response The response sent by the server.
     */
    @Override
    public void onReceive(Operation task, Response response) {
        if(!responseReceived.containsKey(task)) {
            responseReceived.put(task, response);
            return;
        }
        if(!response.isSuccessful()) {
            // Either out of capacity or bad credentials.
            uncompileTask(task);
            // If one server had already sent a response, we ignore it since
            // the operations are rescheduled most likely into several
            // different tasks.
            responseReceived.remove(task);
            return;
        }
        // Comparison of the two responses for the task.
        ComputeResponse storedComputation = (ComputeResponse)responseReceived.get(task);
        ComputeResponse computation = (ComputeResponse)response;
        if(storedComputation.getResult() == computation.getResult())
            getRepartitor().computeResult(computation.getResult());
        else
            uncompileTask(task);
        // In every case remove the stored information on the task.
        responseReceived.remove(task);
    }
    
}

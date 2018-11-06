package com.inf8480_tp2.repartitor.executor;

import com.inf8480_tp2.repartitor.Repartitor;
import com.inf8480_tp2.shared.operations.Operation;
import com.inf8480_tp2.shared.response.ComputeResponse;
import com.inf8480_tp2.shared.response.Response;
import com.inf8480_tp2.shared.server.ComputeServer;
import com.inf8480_tp2.shared.server.ServerInfo;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The executors submits tasks to a computation server and verifies or not its
 * result if the unsafe mode is set.
 * 
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public abstract class Executor {
    
    /**
     * The executor holds a reference to its repartitor to call operations
     * to interact on its buffers.
     */
    Repartitor repartitor;
    
    /**
     * Sends operations to the computation servers.
     */
    ExecutorService executor;
    
    /**
     * An executor is created by a given repartitor with a certain number of
     * threads. The number of thread relies on the number of computation
     * servers available.
     * 
     * @param repartitor The repartitor creating the executor.
     */
    public Executor(Repartitor repartitor) {
        this.repartitor = repartitor;
        this.executor = null;
    }
    
    /**
     * Sets the executor with the number of correct thread. Must be called
     * before any task submition.
     * 
     * @param threadNumber The number of thread that will be used by the executor.
     */
    public void setThreadNumber(int threadNumber) {
        this.executor = Executors.newFixedThreadPool(threadNumber);
    }
    
    public void submit(Operation task, ServerInfo serverInfo) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ComputeServer server = getRepartitor().getComputationServers().get(serverInfo);
                    Response resp = server.executeOperation(task, repartitor.LOGIN, repartitor.PASSWORD);
                    onReceive(task, resp, serverInfo);
                    // The server is now available to compute a new task.
                    getRepartitor().setServerState(serverInfo, false);
                } catch (RemoteException ex) {
                    // Uncompile the task on network error to reschedule the
                    // operations in the task.
                    uncompileTask(task);
                    // In case of non verified mode to avoid uncompiling
                    // the task two times
                    getRepartitor().removeFromVerification(task, serverInfo);
                    // Remove the server from the repartitor.
                    getRepartitor().removeServer(serverInfo);
                }
            }
        });
    }
    
    /**
     * Reception callback to a computation server response.
     * 
     * @param task The task for which a response was given.
     * @param response The response of the task execution.
     */
    public abstract void onReceive(Operation task, Response response, ServerInfo serverInfo);
    
    /**
     * Getter for the repartitor's executor.
     * 
     * @return The repartitor that created the executor.
     */
    public Repartitor getRepartitor() {
        return repartitor;
    }
    
    /**
     * The uncompilation of a task is useful on network error or if the
     * computation server is out of capacity. It reschedules the operations
     * of a task by giving them to the repartitor which created the executor.
     * 
     * @param task The task to uncompile.
     */
    protected void uncompileTask(Operation task) {
        repartitor.addOperations(task.getOperations());
    }
    
    /**
     * Shutdowns the executor.
     */
    public void shutdown() {
        executor.shutdown();
    }
}

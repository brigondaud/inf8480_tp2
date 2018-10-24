package com.inf8480_tp2.repartitor.factory;

import com.inf8480_tp2.repartitor.strategy.OptimalRepartitionStrategy;
import com.inf8480_tp2.repartitor.strategy.RepartitionStrategy;
import com.inf8480_tp2.shared.operations.AtomicOperation;
import com.inf8480_tp2.shared.operations.Task;
import java.util.Queue;

/**
 * The task factory builds tasks for the repartitor. 
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public class TaskFactory {
    
    /**
     * The repartition strategy sizes the tasks.
     */
    private RepartitionStrategy strategy;

    /**
     * The task factory is built using a given repartition strategy.
     * 
     * @param strategy The strategy to built the task factory with.
     */
    public TaskFactory(RepartitionStrategy strategy) {
        setStrategy(strategy);
    }
    
    /**
     * Builds the task factory with the optimal repartition strategy by default.
     */
    public TaskFactory() {
        this(new OptimalRepartitionStrategy());
    }
    
    /**
     * Setter for the repartition strategy.
     * 
     * @param strategy The repartition strategy to set.
     */
    public final void setStrategy(RepartitionStrategy strategy) {
        this.strategy = strategy;
    }
    
    /**
     * Builds a task given a computation server capacity, and a buffer in which
     * the operations are transfered from.
     * 
     * @param operationBuffer The buffer containing the operations.
     * @return The created task.
     */
    public Task buildTask(Queue<AtomicOperation> operationBuffer) {
        Task task = new Task();
        for(int i = 0; i < strategy.computeSize(); i++) {
            task.addOperation(operationBuffer.poll());            
        }
        return task;
    }
}

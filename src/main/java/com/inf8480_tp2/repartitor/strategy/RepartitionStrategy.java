package com.inf8480_tp2.repartitor.strategy;

/**
 * A repartition strategy is used to size a task. Each strategy can take
 * different parameters into account when sizing a task.
 * 
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public interface RepartitionStrategy {
    
    /**
     * Gives the size of a task.
     * 
     * @return The task size.
     */
    public int computeSize();
}

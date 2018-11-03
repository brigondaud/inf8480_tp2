package com.inf8480_tp2.repartitor.strategy;

/**
 * The optimal repartition strategy minimizes the following equation:
 * n(1-T), where n is the size of the task to compute and T is the failure
 * rate. Since T is computed using Ci the capacity of a computation server,
 * this strategy is built using this capacity.
 * 
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public class OptimalRepartitionStrategy implements RepartitionStrategy {
    
    /**
     * The capacity for which the repartition is computed.
     */
    private int capacity;

    /**
     * The optimal repartition strategy is built given a computation
     * server capacity.
     * 
     * @param capacity The computation server capacity.
     */
    public OptimalRepartitionStrategy(int capacity) {
        setCapacity(capacity);
    }

    public OptimalRepartitionStrategy() {
        this(1);
    }
    
    /**
     * Setter for the strategy capacity.
     * 
     * @param capacity The capacity to set.
     */
    public final void setCapacity(int capacity) {
        assert capacity >= 0;
        this.capacity = capacity;
    }

    /**
     * The solution of the equation min(n(1-T)) is n = 5Ci/2.
     * 
     * @return 5/2 of the capacity.
     */
    @Override
    public int computeSize() {
        return (5*capacity)/2;
    }
    
}

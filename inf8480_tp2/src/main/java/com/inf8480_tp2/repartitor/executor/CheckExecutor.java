package com.inf8480_tp2.repartitor.executor;

import com.inf8480_tp2.repartitor.Repartitor;
import com.inf8480_tp2.shared.operations.Operation;
import com.inf8480_tp2.shared.response.Response;

/**
 * The check executor has a simple behavior. On the reception of a successful
 * compute response, it takes the result as correct.
 * 
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public class CheckExecutor extends Executor {

    public CheckExecutor(Repartitor repartitor) {
        super(repartitor);
    }
    
    @Override
    public void onReceive(Operation task, Response response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}


package ptts.entities.controllers;

import ptts.entities.GateOperator;

/**
 *
 * @author Siri@MnS
 */
public class GateOperatorController {
    //<editor-fold defaultstate="collapsed" desc="Singleton implementation">
    private static GateOperatorController instance= null;
    private GateOperatorController(GateOperator gateOperator){ //stop creation of instances by other entities
        this.currentGateOperator = gateOperator;
    }
    public static GateOperatorController getInstance(GateOperator gateOperator){
        if (instance == null) {
            instance = new GateOperatorController(gateOperator);
        }
        return instance;
    }
//</editor-fold>

    private GateOperator currentGateOperator=null;
    
    public GateOperator getCurrentGateOperator(){
        return this.currentGateOperator;
    }
}

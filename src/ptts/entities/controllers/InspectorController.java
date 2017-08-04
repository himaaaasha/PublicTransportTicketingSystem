
package ptts.entities.controllers;

import ptts.entities.Inspector;

/**
 *
 * @author Siri@MnS
 */
public class InspectorController {
    //<editor-fold defaultstate="collapsed" desc="Singleton implementation">
    private static InspectorController instance= null;
    private InspectorController(Inspector inspector){ //stop creation of instances by other entities
        this.currentInspector = inspector;
    }
    public static InspectorController getInstance(Inspector inspector){
        if (instance == null) {
            instance = new InspectorController(inspector);
        }
        return instance;
    }
//</editor-fold>

    private Inspector currentInspector=null;
    
    public Inspector getCurrentInspector(){
        return this.currentInspector;
    }
}

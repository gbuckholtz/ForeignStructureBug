package ca.queensu.collagreesrv;

import ca.queensu.beans.sror.tSimpleStructure;

import java.util.List;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNDecisionResult;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ForeignStructureBug {

    private static final Logger kielogger = LoggerFactory.getLogger(ForeignStructureBug.class);

    public static void main(String[] args) {
        ForeignStructureBug bug = new ForeignStructureBug();
        bug.run();
    }

    public void run() {
        kielogger.info("Starting ForeignStructureBug...");

        KieServices kieServices = KieServices.Factory.get();
        KieContainer kContainer;
        try {
            kContainer = kieServices.newKieContainer(
                    kieServices.newReleaseId("ca.queensu", "sror", "1.0-SNAPSHOT"));
            System.out.println("Kie container initialized");
        } catch (Exception e) {
            System.out.println("Kie container failed to initialize");
            e.printStackTrace();
            return;
        }

        KieBase kBase = kContainer.getKieBase();
        KieSession kSession = kBase.newKieSession();
        
        try {
            DMNRuntime dmnRuntime = kSession.getKieRuntime(DMNRuntime.class);

            DMNModel dmnModel = dmnRuntime.getModel("https://kiegroup.org/dmn/_4F6F62EE-B86D-41C1-9CA7-CCB59703F758",
                    "SpecificROR");

            kSession.addEventListener(new DebugAgendaEventListener());

            // Create a new DMN context
            DMNContext dmnContext = dmnRuntime.newContext();

            // adding test for the SimpleStructure
            tSimpleStructure simpleStructure = new tSimpleStructure();
            simpleStructure.setNumberField(1.0);
            simpleStructure.setStringField("Test");

            dmnContext.set("SimpleInput", simpleStructure);
          
            // adding test for the ForeignSimpleStructure
            tSimpleStructure foreignSimpleStructure = new tSimpleStructure();
            foreignSimpleStructure.setNumberField(2.0);
            foreignSimpleStructure.setStringField("ForeignTest");

            dmnContext.set("ForeignTypeInput", foreignSimpleStructure);

            evaluateAndLogDecision(dmnRuntime, dmnModel, dmnContext, "Test2String", null, null);
            evaluateAndLogDecision(dmnRuntime, dmnModel, dmnContext, "Test2Number", null, null);
            evaluateAndLogDecision(dmnRuntime, dmnModel, dmnContext, "ForeignTest3String", null, null);
            evaluateAndLogDecision(dmnRuntime, dmnModel, dmnContext, "ForeignTest3Number", null, null);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            kSession.dispose();
        }
    }

    private void evaluateAndLogDecision(DMNRuntime dmnRuntime, DMNModel dmnModel, DMNContext dmnContext, String decisionName, List<String> srorGrantedCourses, String courseKey) {
        System.out.println("Evaluating decision: " + decisionName);
        DMNResult dmnResult = dmnRuntime.evaluateByName(dmnModel, dmnContext, decisionName);
        DMNDecisionResult decisionResult = dmnResult.getDecisionResultByName(decisionName);
        if (decisionResult != null) {
            Object result = decisionResult.getResult();
            System.out.println(decisionName + " Result: " + result);
            if (result instanceof Boolean) {
                Boolean booleanResult = (Boolean) result;
                System.out.println(decisionName + " Boolean Result: " + booleanResult);
            } else if (result instanceof Number) {
                Number numberResult = (Number) result;
                System.out.println(decisionName + " Number Result: " + numberResult);
            } else if (result instanceof String) {
                String stringResult = (String) result;
                System.out.println(decisionName + " String Result: " + stringResult);
            } else {
                System.out.println(decisionName + " Result: " + result);
            }
        } else {
            System.out.println("Decision '" + decisionName + "' not found.");
        }
    }
}
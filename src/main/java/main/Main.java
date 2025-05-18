package main;

import lipid.*;
import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        //Instance the Drools rules
        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        try {
            // TODO INTRODUCE THE CODE IF DESIRED TO INSERT FACTS AND TRIGGER RULES
            Annotation annotation1 = new Annotation(
                    new Lipid(1, "PC 16:0/18:1", "C42H82NO8P", "phosphatidylcholine", 34, 1),
                    760.585,  // m/z
                    100000.0, // intensidad
                    5.32,     // RT
                    IoniationMode.POSITIVE
            );
            lipidScoreUnit.getAnnotations().add(annotation1);

            //Make the rules work
            instance.fire();

            // TODO INTRODUCE THE QUERIES IF DESIRED

        } finally {
            instance.close();
        }
    }
}

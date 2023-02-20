package org.matsim.run;

import com.google.common.collect.Lists;
import org.checkerframework.checker.units.qual.C;
import org.junit.Assert;
import org.junit.Test;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.population.*;
import org.matsim.application.MATSimApplication;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.run.MultiModeDrtConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.ControlerUtils;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RunModalExperimentTest {

    private static final String URL = "https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/leipzig/leipzig-v1.1/input/";

    @Test
    public final void runDifferentModesForSameTripTest() {

        String drtMode = "drtNorth";
        String outputDir = "modalExperimentsOutput";

        List<String> modes = Lists.newArrayList(TransportMode.car, TransportMode.bike, drtMode, TransportMode.pt);
        List<String> outputEntries = new ArrayList<>();

        Config config = ConfigUtils.loadConfig("scenarios/input/leipzig-v1.1-25pct.config_with-drt-intermodal.xml");

        config.global().setNumberOfThreads(1);
        config.qsim().setNumberOfThreads(1);
        config.plans().setInputFile(URL + "leipzig-v1.1-0.1pct.plans.xml.gz");
        config.controler().setLastIteration(0);
        config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);

        for(String mode : modes) {

            config.controler().setOutputDirectory("./" + outputDir + "/mode-" + mode);

            Population population = PopulationUtils.readPopulation(config.plans().getInputFile());

            Person personLongDistance = population.getPersons().get(Id.createPersonId("102865"));
            Person personMiddleDistance = population.getPersons().get(Id.createPersonId("133181"));
            Person personShortDistance = population.getPersons().get(Id.createPersonId("105553"));

            List<Person> samplePersons = Lists.newArrayList(personLongDistance, personMiddleDistance, personShortDistance);

            for(Person person : samplePersons) {
                Plan selected = person.getSelectedPlan();
                for (Plan plan : Lists.newArrayList(person.getPlans())) {
                    if (plan != selected)
                        person.removePlan(plan);
                }
                for(PlanElement element : person.getSelectedPlan().getPlanElements()) {
                    if(element instanceof Leg) {
                        ((Leg) element).setMode(mode);
                    }
                }
//                Person test = population.getPersons().get(person.getId());
//                System.out.println(test.getSelectedPlan().getPlanElements());
            }
            String inputPop = outputDir + "/" + mode + "-input-plans.xml.gz";
            PopulationUtils.writePopulation(population, inputPop);
            config.plans().setInputFile("../../" + inputPop);

//            ScenarioUtils.ScenarioBuilder builder = new ScenarioUtils.ScenarioBuilder(config);
//            builder.setPopulation(population);
//            Scenario scenario = builder.build();


            MultiModeDrtConfigGroup multiModeDrtConfigGroup = ConfigUtils.addOrGetModule(config, MultiModeDrtConfigGroup.class);
            for(DrtConfigGroup drtConfigGroup : multiModeDrtConfigGroup.getModalElements()) {
                //drtNorth in this example acts as our drtMode for the whole city. The other drtMode is ignored
                if(drtConfigGroup.mode.equals("drtNorth")) {
                    drtConfigGroup.transitStopFile = URL + "../../projects/namav/flexa-scenario/output/namav-cases/namav-case-wholeCity/leipzig-v1.1-drt-stops-caseNamav-wholeCity.xml";
                    drtConfigGroup.vehiclesFile = URL + "../../projects/namav/flexa-scenario/output/namav-cases/namav-case-wholeCity/leipzig-flexa-25pct-scaledFleet-caseNamav-randomFleet-wholeCity.drt_vehicles.xml.gz";
                }
            }

//            Person test2 = PopulationUtils.readPopulation(config.plans().getInputFile()).getPersons().get(personLongDistance.getId());
            MATSimApplication.execute(RunLeipzigScenario.class, config, "run", "--with-drt", "--post-processing", "disabled");

            File dir = new File(config.controler().getOutputDirectory());
            String[] files = dir.list();
            String outputPlans = null;

            for(String file : files) {
                if(file.contains(".output_plans.xml.gz")) {
                    outputPlans = "/" + file;
                    break;
                }
            }
            Population outputPop = PopulationUtils.readPopulation(dir + outputPlans);

            for(Person p : samplePersons) {
                for(PlanElement el : outputPop.getPersons().get(p.getId()).getSelectedPlan().getPlanElements()) {
                    if(el instanceof Leg) {

                        //TODO we need to ignore access and egress walks

//                        Person test1 = outputPop.getPersons().get(p.getId());

                        System.out.println(outputPop.getPersons().get(p.getId()).getSelectedPlan().getPlanElements());
                        Assert.assertEquals(mode, ((Leg) el).getMode());

                        Double distance = ((Leg) el).getRoute().getDistance();
                        Double travelTime = ((Leg) el).getRoute().getTravelTime().seconds();
                        Double speed = distance / travelTime;

                        String csvFileEntry = p.getId() + ";" + mode + ";" + distance + ";" + travelTime + ";" + speed;
                        outputEntries.add(csvFileEntry);
                    }
                }
            }
        }

        BufferedWriter writer = IOUtils.getBufferedWriter("./" + outputDir + "/samplePersons.csv");

        try {
            writer.write("personId;mode;travelledDistance[m];travelledTime[s];travelledSpeed[m/s]");

            for(String entry : outputEntries) {
                writer.newLine();
                writer.write(entry);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("samplePersons.csv has been written to " + outputDir);
    }
}

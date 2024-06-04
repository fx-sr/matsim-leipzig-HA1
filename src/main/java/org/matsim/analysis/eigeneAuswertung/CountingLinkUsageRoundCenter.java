package org.matsim.analysis.eigeneAuswertung;
import org.matsim.core.events.EventsUtils;


public class CountingLinkUsageRoundCenter {

	public static void main(String[] args) {

		var handler = new CenterLinkIntel();
		var manager = EventsUtils.createEventsManager();
		manager.addHandler(handler);
		EventsUtils.readEvents(manager, "./output-NO-CARS/policy-10pct-2it/leipzig-0.1pct.output_events.xml.gz");
		System.out.println("Anzahl Link-Enter-Events auf den Betroffenen Links: "+handler.getCounter());
		for (String ID : handler.NoMoreCarsIDs) {
			System.out.println("Vehicle-ID and time of Link-Enter-Event:"ID);
		}


	}
}

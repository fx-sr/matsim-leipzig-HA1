package org.matsim.run.prepare;

import org.junit.Assert;
import org.junit.Test;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.network.NetworkUtils;


public class FixNetworkTest {

	private static final String inputNetwork = "input/v1.1/leipzig-v1.1-network.xml.gz";

	@Test
	public void runFixNetworkTest() {
		String outputNetworkPath = "test/output/FixNetworkTest/leipzig-v1.1-network.xml.gz";

		new FixNetwork().execute("--output", outputNetworkPath, "INPUT " + inputNetwork);

		Network outputNetwork = NetworkUtils.readNetwork(outputNetworkPath);

		Link bridgeLink1 = outputNetwork.getLinks().get("24020319");

		Assert.assertNotNull(bridgeLink1);
		Assert.assertEquals("From node", bridgeLink1.getFromNode().toString(), "260443657");
		Assert.assertEquals("To node", bridgeLink1.getToNode().toString(), "206313940");
		Assert.assertTrue(bridgeLink1.getAllowedModes().contains(TransportMode.car));
		Assert.assertTrue(bridgeLink1.getAllowedModes().contains(TransportMode.bike));
		Assert.assertTrue(bridgeLink1.getAllowedModes().contains(TransportMode.ride));
		Assert.assertTrue(bridgeLink1.getAllowedModes().contains("freight"));
		Assert.assertEquals(bridgeLink1.getLength(), 62.47, 0.);
		Assert.assertEquals(bridgeLink1.getCapacity(), 1500, 0.);
		Assert.assertEquals(bridgeLink1.getFreespeed(), 12.50, 0.);
		Assert.assertEquals(bridgeLink1.getNumberOfLanes(), 1, 0.);
		Assert.assertEquals((double) bridgeLink1.getAttributes().getAttribute("allowed_speed"), 13.89, 0.);
		Assert.assertEquals(bridgeLink1.getAttributes().getAttribute("type"), "highway.primary");
		Assert.assertEquals(bridgeLink1.getAttributes().getAttribute("name"), "Richard-Lehmann-Straße");

		Link bridgeLink2 = outputNetwork.getLinks().get("-24020319");

		Assert.assertNotNull(bridgeLink2);
		Assert.assertEquals("From node", bridgeLink2.getFromNode().toString(), "206313940");
		Assert.assertEquals("To node", bridgeLink2.getToNode().toString(), "260443657");
		Assert.assertTrue(bridgeLink2.getAllowedModes().contains(TransportMode.car));
		Assert.assertTrue(bridgeLink2.getAllowedModes().contains(TransportMode.bike));
		Assert.assertTrue(bridgeLink2.getAllowedModes().contains(TransportMode.ride));
		Assert.assertTrue(bridgeLink2.getAllowedModes().contains("freight"));
		Assert.assertEquals(bridgeLink2.getLength(), 62.47, 0.);
		Assert.assertEquals(bridgeLink2.getCapacity(), 1500, 0.);
		Assert.assertEquals(bridgeLink2.getFreespeed(), 12.50, 0.);
		Assert.assertEquals(bridgeLink2.getNumberOfLanes(), 1, 0.);
		Assert.assertEquals((double) bridgeLink2.getAttributes().getAttribute("allowed_speed"), 13.89, 0.);
		Assert.assertEquals(bridgeLink2.getAttributes().getAttribute("type"), "highway.primary");
		Assert.assertEquals(bridgeLink2.getAttributes().getAttribute("name"), "Richard-Lehmann-Straße");
	}
}

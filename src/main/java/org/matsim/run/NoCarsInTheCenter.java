package org.matsim.run;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.network.NetworkUtils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class NoCarsInTheCenter {


	public static void main(String[] args) throws IOException {

		var network = NetworkUtils.readNetwork("input/v1.3/leipzig-v1.3-network-with-pt.xml.gz");

		/*

		String filePath = "C:/Users/xilef/Desktop/MATSim Local/matsim-leipzig-HA1/input/v1.3/ausweichCase/Links Altstadtring.xlsx"; // Change this to the path of your Excel file
		int columnNumber = 0; // Column number to read (0-indexed)

		String[] NoCarLinks;

		FileInputStream fis = new FileInputStream(new File(filePath));
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0); // Get the first sheet

		ArrayList<String> columnData = new ArrayList<>();

		for (Row row : sheet) {
			Cell cell = row.getCell(columnNumber);
			if (cell != null) {
				cell.setCellType(CellType.STRING); // Ensure the cell is read as a string
				columnData.add(cell.getStringCellValue());
			}
		}

		// Convert the ArrayList to an array
		NoCarLinks = new String[columnData.size()];
		NoCarLinks = columnData.toArray(NoCarLinks);

		System.out.println(NoCarLinks);

		workbook.close();

		try {
			fis.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}


		*/

		String[] NoCarLinks = new String[]{"-1047711995","-132031436","-132052481","-135097551","-135097883","-135175874","-150289829","-150289832","-150289835","-150289836","-150289855","-164014258","-192089792","-19792750","-205872712","-205872718","-217286074","-226226954#0","-226226954#1","-24582070#0","-24582070#1","-250505804","-250505805","-253313625#0","-253313625#1","-253315898","-253331797","-253332003#0","-253333757","-262540120#0","-263386073","-30747139","-313758465","-313758466","-34602734","-35174050","-380114014","-39634529#2","-4310322","-4362252","-4362254","-4779327","-58606903","-58606926","-624705238","-648592594","-759612435","-78049211","-78510055","-78834376","-7947542#0","-7947542#1","-7947554","-7947559","-7947560#0","-7947560#1","-80044573","-8566054","-8566057","-8571419","-85746111#0","-85746111#1","-866272809","-903177001","1047711995","113763515","132031436","132052481","135097551","135097883","135175874","138438926","150289828","150289829","150289832","150289834#0","150289835","150289836","150289855","192089792","19792724","19792750","205872712","205872719#0","206892573#0","217286074","226226954#0","226226954#1","226226954#2","24582070#0","24582070#1","24596527","250505804","250505805","252565244","252565248","253313625#1","253331791","253331797","253332003#1","253333757","262540120#0","262540120#1","263386073","28263279","30747139","31003190","313758466","34602734","35174050","366695718","380114014","39634529#0","4068372","4307251","4310322","4310324","4362252","4362254","4362263#0","4362270","4779327","58606926","58606929","58606940#0","624705238","648592600","648592602","648592603","648592604","74595428","759612435","77935098","78049211","78828913","78834376","7947542#0","7947542#1","7947543","7947544","7947547","7947548","7947554","7947559","7947560#0","7947560#1","7947560#2","80044573","8399017#1","85160619#0","85281043#0","8566054","8566057","8571419","8571422#0","8571422#1","85746111#0","85746111#1","903177001"
		};

		java.lang.String toSet = new java.lang.String("bike,freight,ride");

		for (Link Link : network.getLinks().values()) {
			 for (String NoCarLink : NoCarLinks) {
						if (Link.getId().equals(Id.createLinkId(NoCarLink))) {
							System.out.println("Link ID altered "+Link.getId());
							Link.setFreespeed(0.1);
						}
			 }
		}
				NetworkUtils.writeNetwork(network, "input/v1.3/leipzig-v1.3-network-POLICY-SLO-CARS.xml.gz");
	}

}



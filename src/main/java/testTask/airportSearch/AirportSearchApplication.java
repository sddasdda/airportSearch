package testTask.airportSearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.*;

@SpringBootApplication
public class AirportSearchApplication implements CommandLineRunner {

	private static Logger LOG = LoggerFactory
			.getLogger(AirportSearchApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AirportSearchApplication.class, args);
	}

	@Value("${default-column}")
	private int defaultColumn;

	@Override
	public void run(String... args) throws Exception {
		var file = new File(System.getProperty("user.dir") + "\\airports.dat");
		LOG.info(file.getPath());
		if (!file.exists()){
		    LOG.info("File not found");
		    return;
		}

		List<Airport> records = new ArrayList<>();
		try (Scanner scanner = new Scanner(file, "UTF-8");) {
			while (scanner.hasNextLine()) {
				records.add(getRecordFromLine(scanner.nextLine()));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


		defaultColumn -= 1;
		if (args != null && args.length != 0)
			defaultColumn = Integer.parseInt(args[0]) - 1;
		LOG.info(String.valueOf(records.size()));
		var scan = new Scanner(System.in);
		var pattern = scan.next();

		Airport.setFilterField(defaultColumn);
		sortList(records);
		var time = System.currentTimeMillis();
		var filteredChunk = filterList(records, pattern.toLowerCase());
		time = System.currentTimeMillis() - time;
		if ((filteredChunk[1] - filteredChunk[0]) <= 0) {
			LOG.info("not found");

		} else {
			for (int i = filteredChunk[0]; i <= filteredChunk[1]; i++) {
				LOG.info(records.get(i).toString());
			}
			LOG.info("amount finded lines: " + ((filteredChunk[1] - filteredChunk[0]) + 1));
		}
		LOG.info("Time spent searching: " + time + " ms.");
	}

	private void sortList(List<Airport> data) {
		data.sort(Airport::compareTo);
	}

	private int[] filterList(List<Airport> data, String pattern) {

		if (pattern == null) {
			LOG.info("Filter pattern not defined");
			return new int[]{-1, -1};
		}

		var stringStart = bound(data, pattern, true, (leftProcessedString, rightProcessedString) -> {


			if (leftProcessedString.length() < rightProcessedString.length()) {
				return -1;
			} else {

				var result = 0;
				for (int i = 0; i < rightProcessedString.length(); i++){
					if (leftProcessedString.charAt(i) < rightProcessedString.charAt(i)){
						result = -1;
						break;
					} else if (leftProcessedString.charAt(i) > rightProcessedString.charAt(i)){
						result = 1;
						break;
					}
				}
				return result;
			}
		});

		var stringEnd = bound(data, pattern, false, 0,  (leftProcessedString, rightProcessedString) -> {


			if (leftProcessedString.length() < rightProcessedString.length()) {
				return -1;
			} else {
				var result = 0;
				for (int i = 0; i < rightProcessedString.length(); i++){
					if (leftProcessedString.charAt(i) < rightProcessedString.charAt(i)){
						result = -1;
						break;
					} else if (leftProcessedString.charAt(i) > rightProcessedString.charAt(i)){
						result = 1;
						break;
					}
				}

				return result;
			}
		});

		LOG.info(stringStart + " " + stringEnd);

		return new int[]{stringStart, stringEnd};
	}

	//binary search
	private Integer bound(final List<Airport> A, String B, boolean searchFirst, Comparator<String> C) {
		return bound(A,B, searchFirst, 0, C);
	}

	private Integer bound(final List<Airport> A, String B, boolean searchFirst, int start , Comparator<String> C) {
		var n = A.size();
		var low = start;
		var high = n - 1;
		var res = -1;
		int mid;
		while (low <= high) {
			mid = low + (high - low) / 2;
			if (C.compare(A.get(mid).get(), B) == 0) {

				res = mid;
				if (searchFirst) {
					high = mid - 1;
				}
				else {
					low = mid + 1;
				}
			} else if (C.compare(A.get(mid).get(), B) < 0) {
				low = mid + 1;
			} else {
				high = mid - 1;
			}
		}
		return res;
	}


	private Airport getRecordFromLine(String line) {
		var values = new Airport();

		var counter = 0;
		try (Scanner rowScanner = new Scanner(line)) {
			rowScanner.useDelimiter(",");
			while (rowScanner.hasNext()) {
				StringBuilder text = new StringBuilder(rowScanner.next());
				if (text.charAt(0) == '\"' && text.charAt(text.length() - 1) != '\"') {
					var newText = rowScanner.next();
					while (newText.charAt(newText.length() - 1) != '\"') {
						text.append(",").append(newText);
						newText = rowScanner.next();
					}
				}
				if (text.length() >= 2 && text.charAt(0) == '\"' && text.charAt(text.length() - 1) == '\"')
					text = new StringBuilder(text.substring(1, text.length() - 1));
				var field = text.toString();
				switch (counter) {
					case 0:
						values.id = field;
						break;
					case 1:
						values.name = field;
						break;
					case 2:
						values.city = field;
						break;
					case 3:
						values.country = field;
						break;
					case 4:
						values.iataAirportCode = field;
						break;
					case 5:
						values.icaoAirportCode = field;
						break;
					case 6:
						try {
							values.latitude = Double.parseDouble(field);
						} catch (NumberFormatException e) {
							values.latitude = Double.MAX_VALUE;
						}
						break;
					case 7:
						try {
							values.longitude = Double.parseDouble(field);
						} catch (NumberFormatException e) {
							values.longitude = Double.MAX_VALUE;
						}
						break;
					case 8:
						try {
							values.height = Integer.parseInt(field);
						} catch (NumberFormatException e) {
							values.height = Integer.MAX_VALUE;
						}
						break;
					case 9:
						try {
							values.timeZone = Double.parseDouble(field);
						} catch (NumberFormatException e) {
							values.timeZone = Double.MAX_VALUE;
						}
						break;
					case 10:
						try {
							values.dst = Dst.valueOf(field);
						} catch (IllegalArgumentException e) {
							values.dst = Dst.NOT_DEFINED;
						}

						break;
					case 11:
						values.timeZoneName = field;
						break;
					case 12:
						try {
							values.type = Type.valueOf(field);
						} catch (IllegalArgumentException e) {
							values.type = Type.NOT_DEFINED;
						}
						break;
					case 13:
						try {
							values.source = Source.valueOf(field);
						} catch (IllegalArgumentException e) {
							values.source = Source.NOT_DEFINED;
						}
						break;
				}
				counter++;
			}
		}
		return values;
	}

}

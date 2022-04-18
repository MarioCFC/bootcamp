package competition;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")

public class Tournament {
	private String name;

	@JsonIdentityReference(alwaysAsId = true)
	private ArrayList<Car> participants;
	@JsonIdentityReference(alwaysAsId = true)

	private ArrayList<Race> races;
	private static RacesResults resultsManages = null;
	private int numberOfRaces;
	private int lastRaceRunned = 0;

	private Tournament() {
	}
	public Tournament(String name, ArrayList<Car> participants, int numberOfRaces) {
		this.name = name;
		this.participants = participants;
		this.numberOfRaces = numberOfRaces;
		races = new ArrayList<Race>();
		resultsManages = RacesResults.getInstance();
	}

	public String getName() {
		return name;
	}

	public void addRace(Race newRace) {
		races.add(newRace);
		newRace.setEventWichItBelongs(this);
	}

	public void runRace() {
		if (races.size() - 1 >= lastRaceRunned) {
			races.get(lastRaceRunned++).run();
		}
	}

	// HashMap que almacena las puntuaciones luego se guardan las parejas en un
	// ArrayList que se ordena
	public ArrayList getRanking() {
		HashMap<Car, Integer> tempRanking = new HashMap();
		for (Race race : races) {

			for (ResultOfCarInARace carResultInRace : resultsManages.getResultOfARace(race)) {

				Car actualCar = carResultInRace.getCar();

				if (tempRanking.containsKey(actualCar)) {
					int kepedStorer = tempRanking.get(actualCar);
					tempRanking.put(actualCar, kepedStorer + carResultInRace.getScore());
				} else {
					tempRanking.put(actualCar, carResultInRace.getScore());
				}
			}
		}
		ArrayList<Entry<Car, Integer>> ranking = new ArrayList(tempRanking.entrySet());
		sortRanking(ranking);
		return ranking;
	}

	@JsonIgnore
	public ArrayList<Entry<Car, Integer>> getPodium() {
		return (ArrayList<Entry<Car, Integer>>) getPodium().subList(0, 3);
	}

	private void sortRanking(ArrayList<Entry<Car, Integer>> ranking) {
		Comparator<Entry<Car, Integer>> comp = new Comparator<Entry<Car, Integer>>() {
			@Override
			public int compare(Entry<Car, Integer> o1, Entry<Car, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		};

		ranking.sort(comp.reversed());
	}

}

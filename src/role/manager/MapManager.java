package role.manager;

import java.util.ArrayList;

import role.data.Map;

public class MapManager extends CsvManager{
	private static MapManager INSTANCE = new MapManager();
	
	private MapManager(){
		list = new ArrayList<String[]>();
		load("/res/csv/map.csv");
	}

	public static MapManager getInstance(){
		return INSTANCE;
	}

	public Map getMap(int index){
		String[] map = list.get(index);
		String mapFile = map[1];
		String eventFile = map[2];
		return new Map(mapFile, eventFile);
	}
}

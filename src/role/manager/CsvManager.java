package role.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public abstract class CsvManager {
	protected ArrayList<String[]> list;

	protected void load(String path){
		BufferedReader br = null;
		try{
			br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path)));
			String line;

			while((line = br.readLine()) != null){
				if("".equals(line) || line.startsWith("#") || line.startsWith(",")) continue;
				String[] st = line.split(",", 0);
				list.add(st);
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				if(br != null) br.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}

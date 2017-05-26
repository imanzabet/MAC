package MASC_FIS_3;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Func{
	
	public Func(){
	}
	public boolean fault_state,restore_state,steady_state,is_junction;
	public int ticker = 5000;
	public String convertint2str(int i) {
        return String.format("%d", i);
    }

	public boolean isstrequal(String st1, String st2) {
		if (st1.equals(st2))
			return true;
		else
			return false;
    }
	
	public String getDateTime(){
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
}

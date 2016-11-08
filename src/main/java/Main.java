import static spark.Spark.*;

import com.skinme.config.WebConfig;
import com.skinme.service.impl.*;

public class Main {

	
	public static void main(String[] args){
		new WebConfig(new SkinService());
	}
}


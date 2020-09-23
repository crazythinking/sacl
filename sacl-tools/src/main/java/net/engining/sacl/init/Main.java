package net.engining.sacl.init;

import com.google.common.collect.Maps;
import net.engining.sacl.init.kettle.KettleManagerService;

public class Main {

	public static void main(String[] args) throws Exception{

		KettleManagerService kettleManagerService = new KettleManagerService(
				"D:\\workspaces\\sacl\\sacl-tools\\src\\main\\resources\\",
				"KettleFileRepository",
				"test",
				"detail"
		);

//		kettleManagerService.runTransformationFromFile(
//				"test_num.ktr",
//				Maps.newHashMap()
//				);
//
//		kettleManagerService.runJobFromFile(
//				"test_no.kjb",
//				Maps.newHashMap()
//		);

		kettleManagerService.runJobFromRepository(
				"test_repo",
				Maps.newHashMap()
		);
	}

}
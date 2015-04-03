package com.will.practice;

import java.util.Map;

import com.will.practice.controller.PracticeController;

public class Driver {

	private static final String PRINT_FORMAT = ">>>>> key: %1$s, value: %2$s. <<<<<";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PracticeController controller = new PracticeController();
		if (null == args || args.length <= 0) {
			System.out.println("Please use parameter: user, parent, or child.");
			return;
		}
		String arg = args[0];
		if ("user".equals(arg)) {
			System.out.println("Search users:");
			Map<String, String> map = controller.searchUser("");
			printRender(map);
		} else if ("parent".equals(arg)) {
			System.out.println("Search parents:");
			Map<String, String> map = controller.searchParent("");
			printRender(map);
		} else if ("child".equals(arg)) {
			System.out.println("Search children:");
			Map<String, String> map = controller.searchChild("");
			printRender(map);
		} else {
			System.out.println("Please use parameter: user, parent, or child.");
		}
	}

	public static void printRender(Map<String, String> map) {
		if (null == map || map.isEmpty()) {
			System.out.println("Nothing to print.");
			return;
		}
		for (String key: map.keySet()) {
			if (key == null || !map.containsKey(key)) {
				continue;
			}
			String value = map.get(key);
			System.out.println(String.format(PRINT_FORMAT, key, value));
		}
	}
}

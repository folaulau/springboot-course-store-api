package com.springboot.utility;

import java.util.Arrays;
import java.util.List;

public final class SlackServiceUtils {

	// <@UCBERKD2A> - Rosselle
	// <@U8AFAU4EB> - Ramesh
	// <@U9S8ZA65R> - Folau
	// <@UKEJ10MPA> - Ketul
	public static final List<String> EMPLOYEE_SLACK_IDS = Arrays.asList("<@UCBERKD2A>","<@U8AFAU4EB>","<@U9S8ZA65R>","<@UKEJ10MPA>");
	
	public static String getEmployeeSlackIdsForTagging() {
		StringBuilder ids = new StringBuilder();
		for(String id : EMPLOYEE_SLACK_IDS) {
			ids.append(id+" ");
		}
		return ids.toString();
	}
}

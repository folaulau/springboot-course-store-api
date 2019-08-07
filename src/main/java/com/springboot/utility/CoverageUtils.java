package com.springboot.utility;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CoverageUtils {

	public static final Set<String> coveredStates = new HashSet<>(Arrays.asList("TX","AZ"));
    
    public static boolean isInCoveredState(String state) {
    	if(state!=null && coveredStates.contains(state)) {
    		return true;
    	}
    	return false;
    }
}

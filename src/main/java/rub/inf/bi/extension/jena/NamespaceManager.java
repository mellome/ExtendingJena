package rub.inf.bi.extension.jena;

import java.util.HashMap;

public class NamespaceManager {
	
	private HashMap<String, String> prefixes = null;
	
	private static NamespaceManager instance = null;
	
	public static NamespaceManager getInstance() {
		if(instance == null) {
			System.err.println("NamespaceManager is not initialised! Returned Null. ");
		}
		return instance;
	}
	
	public static void initialize(HashMap<String, String> knownPrefixes) {
		instance = new NamespaceManager(knownPrefixes);
	}
	
	private NamespaceManager(HashMap<String, String> knownPrefixes) {
		prefixes = knownPrefixes;
	}
	
	public String getNamespace(String prefix) {
		return prefixes.get(prefix);
	}

	public HashMap<String, String> getAllPrefixes(){
		return prefixes;
	}
}

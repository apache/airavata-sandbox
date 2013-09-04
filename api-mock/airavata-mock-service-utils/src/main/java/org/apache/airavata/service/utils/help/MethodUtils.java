package org.apache.airavata.service.utils.help;


public class MethodUtils {
	public static String getHelpString(HelpData data){
		String help=data.getTitle()+"\n\n";
		help+="Usage: "+data.getDescription()+"\n\n";
		if (data.getSyntax()!=null){
			help+="Syntax:\n\t"+data.getSyntax();
		}
		help+="\n\n";
		help+="Parameters\n\n";
		for (String parameterName : data.getParameters().keySet()) {
			help+=parameterName+"\t\t"+data.getParameters().get(parameterName)+"\n";
		} 
		help+="\n";
		if (data.getExamples().size()>0){
			help+="Examples:\n";
			for (String example : data.getExamples()) {
				help+="\t"+example+"\n";
			}
			help+="\n";
		}
		if (data.getNotes().size()>0){
			help+="Notes:\n";
			for (String note : data.getNotes()) {
				help+=note+"\n";
			}
		}
			
		return help;
	}
}

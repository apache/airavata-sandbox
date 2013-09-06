package org.apache.airavata.service.utils.help;


public class MethodUtils {
	
	public static String getHelpString(HelpData data){
		if (data instanceof HTMLHelpData){
			return getHTMLHelp(data);
		}else{
			return getPlainHelp(data);
		}
	}
	
	private static String getHTMLHelp(HelpData data) {
		String help="<html><body>";
		help+="<h1>"+data.getTitle()+"</h1>"+"\n\n";
		help+="<p>Usage: "+data.getDescription()+"</p>\n\n";
		help+="<br />";
		if (data.getSyntax().size()>0){
			help+="<p>"+"Syntax:\n<br />";
			for (String syntax : data.getSyntax()) {
				help+="\t"+syntax+"\n";
			}
		}
		help+="\n\n";
		help+="Supported Parameters/Operations\n\n";
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
		help+="</body></html>";
		return help;
	}
	private static String getPlainHelp(HelpData data) {
		String help=data.getTitle()+"\n\n";
		help+="Usage: "+data.getDescription()+"\n\n";
		if (data.getSyntax().size()>0){
			help+="Syntax:\n";
			for (String syntax : data.getSyntax()) {
				help+="\t"+syntax+"\n";
			}
		}
		help+="\n\n";
		help+="Supported Parameters/Operations\n\n";
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

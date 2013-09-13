package org.apache.airavata.service.utils.help;


public class MethodUtils {
	
	public static String getHelpString(HelpData data){
		if (data instanceof HTMLHelpData){
			return getHTMLHelp((HTMLHelpData)data);
		}else{
			return getPlainHelp(data);
		}
	}
	
	private static String getHTMLHelp(HTMLHelpData data) {
		String help="<html><body>";
		help+="<h1>"+data.getTitle()+"</h1>"+"\n\n";
		help+="<p>"+data.getDescription()+"</p>\n\n";
		help+="<br />";
		if (data.getSyntax().size()>0){
			help+="<p>"+"Syntax:\n<br />";
			for (String syntax : data.getSyntax()) {
				help+="\t"+syntax+"\n";
			}
		}
		help+="\n\n";
		if (data.getParameters().size()>0){
			help+="<h2>Supported Parameters/Operations</h2>\n\n";
			help+="<table>";
			for (String parameterName : data.getParameters().keySet()) {
				help+="<tr>";
				help+="<td><b>"+parameterName+"</b></td><td>"+data.getParameters().get(parameterName)+"</td>\n";
				help+="</tr>";
			}
			help+="</table>";
		}
		help+="\n";
		if (data.getExamples().size()>0){
			help+="<h2>Examples</h2>\n";
			for (String example : data.getExamples()) {
				help+="\t<p>"+example+"</p>\n";
			}
			help+="\n";
		}
		if (data.getNotes().size()>0){
			help+="<h2>Notes</h2>\n";
			for (String note : data.getNotes()) {
				help+="<p>"+note+"</p>\n";
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

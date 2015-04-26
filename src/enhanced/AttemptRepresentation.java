package enhanced;

public class AttemptRepresentation 
{
	public String username,question;
	public double metricScore,timestamp;
	public int noOfViewPossibleStrings, noOfTestInputs, noOfAcceptStringClicks, noOfRejectStringClicks;
	public AttemptRepresentation(String username,String question,double timestamp, double metricScore, int noOfViewPossibleStrings,
			int noOfTestInputs,int noOfAcceptStringClicks, int noOfRejectStringClicks)
	{
		this.username = username;
		this.question = question;
		this.timestamp = timestamp;
		this.metricScore = metricScore;
		this.noOfTestInputs = noOfTestInputs;
		this.noOfViewPossibleStrings = noOfViewPossibleStrings;
		this.noOfRejectStringClicks = noOfRejectStringClicks;
		this.noOfAcceptStringClicks = noOfAcceptStringClicks;
		
	}
}

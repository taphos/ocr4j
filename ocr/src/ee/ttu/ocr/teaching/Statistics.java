package ee.ttu.ocr.teaching;

/**
 * Network learning statistics collector.
 * @author Filipp Keks
 */
public interface Statistics {
	
	public boolean continueTeaching(int iteration, float error);
	
	public void addExaminationResponse(float[] correctAnswer, float[] answer);

}

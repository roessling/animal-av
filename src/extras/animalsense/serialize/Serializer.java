package extras.animalsense.serialize;

import extras.animalsense.evaluate.Exercise;
import extras.animalsense.evaluate.Question;

public interface Serializer {

	public abstract void serializeExercise(Exercise exercise, String fileName)
			throws SerializerException;

	public abstract Exercise deserializeExercise(String fileName)
			throws SerializerException;

	public abstract void serializeQuestion(Question question, String fileName)
			throws SerializerException;

	public abstract Question deserializeQuestion(String fileName)
			throws SerializerException;

}
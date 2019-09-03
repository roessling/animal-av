/**
 * 
 */
package extras.animalsense.serialize;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import translator.ResourceLocator;
import extras.animalsense.evaluate.Exercise;
import extras.animalsense.evaluate.Question;
import extras.lifecycle.query.function.Comment;
import extras.lifecycle.query.function.CommentIf;
import extras.lifecycle.query.function.Dump;
import extras.lifecycle.query.function.NumberOfCheckpoints;
import extras.lifecycle.query.function.Retrieve;
import extras.lifecycle.query.workflow.AbstractBox;
import extras.lifecycle.query.workflow.AssignBox;
import extras.lifecycle.query.workflow.ConstantValue;
import extras.lifecycle.query.workflow.ScriptBox;
import extras.lifecycle.query.workflow.ValueOfIdentifier;

/**
 * 
 * @author Mihail Mihaylov
 * 
 */
public class SerializerImpl implements Serializer {

	/**
	 * From extras.animalsense.evaluator.*, From extras.lifecycle.query.workflow
	 */
	private final static Class<?>[] CONTEXT_CLASSES = {

	Exercise.class, Question.class, AbstractBox.class, AssignBox.class,
			Comment.class, CommentIf.class, ConstantValue.class,
			ScriptBox.class, ValueOfIdentifier.class, Dump.class,
			NumberOfCheckpoints.class, Retrieve.class

	};

	/* (non-Javadoc)
	 * @see extras.animalsense.serialize.Serializer#serializeExercise(extras.animalsense.evaluate.Exercise, java.lang.String)
	 */
	public void serializeExercise(Exercise exercise, String fileName) throws SerializerException {
		JAXBContext context;
		Writer w = null;
		try {
			context = JAXBContext.newInstance(CONTEXT_CLASSES);

			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			w = new OutputStreamWriter (new FileOutputStream(fileName), "UTF-8");
			//w = new FileWriter(fileName);
			m.marshal(exercise, w);
			//m.marshal(exercise, System.out);
		} catch (JAXBException e) {
			throw new SerializerException(e);
		} catch (IOException e) {
			throw new SerializerException(e);
		} finally {
			try {
				w.close();
			} catch (Exception e) {
				// Do not throw a SerializerException in this case
				e.printStackTrace();
			}
		}

	}

	/* (non-Javadoc)
	 * @see extras.animalsense.serialize.Serializer#deserializeExercise(java.lang.String)
	 */
	public Exercise deserializeExercise(String fileName) throws SerializerException {
		boolean executedInJar = false;
		Exercise result = null;
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(CONTEXT_CLASSES);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			ResourceLocator rl = ResourceLocator.getResourceLocator();
			InputStream is = rl.getResourceStream(fileName);
			InputStreamReader reader = new InputStreamReader(is, "UTF-8");
//			InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
			//FileReader reader = new FileReader(fileName);
			result = (Exercise) unmarshaller.unmarshal(reader);
		} catch (IllegalArgumentException e){
			executedInJar = true;
		} catch (JAXBException e) {
			throw new SerializerException(e);
//		} catch (FileNotFoundException e) {
//			throw new SerializerException(e);
		} catch (UnsupportedEncodingException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
		if(executedInJar){
			try {
				context = JAXBContext.newInstance(CONTEXT_CLASSES);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
				result = (Exercise) unmarshaller.unmarshal(reader);
			} catch (IllegalArgumentException e){
				executedInJar = true;
			} catch (JAXBException e) {
				throw new SerializerException(e);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see extras.animalsense.serialize.Serializer#serializeQuestion(extras.animalsense.evaluate.Question, java.lang.String)
	 */
	public void serializeQuestion(Question question, String fileName) throws SerializerException {
		JAXBContext context;
		Writer w = null;
		try {
			context = JAXBContext.newInstance(CONTEXT_CLASSES);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			w = new OutputStreamWriter (new FileOutputStream(fileName), "UTF-8");
			//w = new FileWriter(fileName);
			m.marshal(question, w);
			m.marshal(question, System.out);

		} catch (JAXBException e) {
			throw new SerializerException(e);
		} catch (IOException e) {
			throw new SerializerException(e);
		} finally {
			try {
				w.close();
			} catch (Exception e) {
				// Do not throw a SerializerException in this case
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see extras.animalsense.serialize.Serializer#deserializeQuestion(java.lang.String)
	 */
	public Question deserializeQuestion(String fileName) throws SerializerException {
		Question result = null;
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(CONTEXT_CLASSES);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
      //FileReader reader = new FileReader(fileName);
			result = (Question) unmarshaller.unmarshal(reader);
		} catch (JAXBException e) {
			throw new SerializerException(e);
		} catch (FileNotFoundException e) {
			throw new SerializerException(e);
		} catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

		return result;
	}

}

package animalscript.extensions;

import java.lang.reflect.Method;

import animal.animator.MethodInvocator;
import animal.graphics.PTPoint;
import animal.misc.AnimatedAlgorithm;
import animal.misc.MessageDisplay;
import animalscript.core.BasicParser;

public class MethodInvocationProducer extends BasicParser {
	public static void createObject(String objectID, String className,
			boolean performNow) {
		if (performNow) {
			try {
				Class<?> c = Class.forName(className);
				Object invocationTarget = c.newInstance();
				MethodInvocator.insertObjectID(objectID, invocationTarget);
			} catch (Exception e) {
				MessageDisplay.errorMsg("failed generating instance of class '"
						+ className + "' / " + e.getMessage(), MessageDisplay.RUN_ERROR);
			}
		} else {
			PTPoint p = new PTPoint(0, 0);
			BasicParser.addGraphicObject(p, anim);
			getObjectIDs().put(objectID, p.getNum(false));
			MethodInvocator anInvocator = new MethodInvocator(currentStep, p
					.getNum(false), 0, "create", className, true, 0);
			BasicParser.addAnimatorToAnimation(anInvocator, anim);
		}
	}

	public void getScriptingCode(String targetObjectID, int tag, boolean doNow) {
		if (doNow) {
			Object targetObject = MethodInvocator.getObject(targetObjectID);
			String code = null;
			if (targetObject != null && targetObject instanceof AnimatedAlgorithm)
				code = ((AnimatedAlgorithm) targetObject).getAnimationCode(tag);
			System.err.println("code read in: " + code);
		} else {
			int objectID = getObjectIDs().getIntProperty(targetObjectID);
			// String accessKey = String.valueOf(objectID);
			// insert directly here on LOAD!
			System.err.println("ID: " + targetObjectID + " numeric ID: " + objectID);
			MethodInvocator anInvocator = new MethodInvocator(currentStep, objectID,
					0, "getAnimationCode", "getAnimationCode", false, tag);
			BasicParser.addAnimatorToAnimation(anInvocator, anim);
		}
	}

	public void invokeMethod(String methodName, String targetObjectID,
			boolean invokeNow) {
		if (invokeNow) {
			Object targetObject = MethodInvocator.getObject(targetObjectID);
			if (targetObject != null) {
				try {
					Method method = 
            targetObject.getClass().getDeclaredMethod(methodName,
							(Class[])null);
					if (method != null)
						method.invoke(targetObject, (Object[])null);
				} catch (Exception e) {
					MessageDisplay.errorMsg("method invocation failed for method '"
							+ methodName + "' in class" + targetObject.getClass().getName(),
							MessageDisplay.RUN_ERROR);
				}
			}
		} else {
			int objectID = getObjectIDs().getIntProperty(targetObjectID);
			System.err.println("ID: " + targetObjectID + " numeric ID: " + objectID);
			MethodInvocator anInvocator = new MethodInvocator(currentStep, objectID,
					0, "invoke", methodName, false, 0);
			BasicParser.addAnimatorToAnimation(anInvocator, anim);
		}
	}
}

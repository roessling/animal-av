package generators.maths.vogelApprox.io;

import generators.maths.vogelApprox.io.Writer;

import java.io.FileOutputStream;


public class AsuFileWriter implements Writer{
	
	private String fileName;
	
	public AsuFileWriter(String fileName) {
		super();
		this.fileName = fileName;
	}


	public void write(String content){
		try {
			byte[] newBytes = content.getBytes("UTF8");
			FileOutputStream fos = new FileOutputStream(fileName + ".asu");
			fos.write(newBytes);
			fos.close();
			
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

}

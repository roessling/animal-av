package generators.maths.vogelApprox;

import generators.maths.vogelApprox.MatrixElement;

import java.util.ArrayList;
import java.util.List;

public class MatrixElement {

	private Integer value = null;
	private boolean active = true;
	private List<MatrixElement> oldValues = new ArrayList<MatrixElement>();
	
	public MatrixElement(int value, boolean active) {
		this.value = value;
		this.active = active;
	}
	
	public MatrixElement() {
		
	}
	
	public Integer getValue() {
		return value;
	}
	public void setValue(int value) {
				
		if (this.value != null) {
			MatrixElement element = new MatrixElement(this.value, this.active);
			oldValues.add(element);
		}
		
		this.value = value;
		
	}
	
	public void modifyValue(int value) {
			
		this.value = value;		
	}
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public List<MatrixElement> getAllValues() {
		return oldValues;
	}
	
	public String toString() {
		String s = "";
		for (MatrixElement e : oldValues) {
			if (e.isActive()) {
				s = Integer.toString(e.getValue()) + "|";
			} else {
				s = "(" + Integer.toString(e.getValue()) + ")|";
			}
		}
		s = s + value;
		return s;
	}
}

package generators.graphics;


import java.util.HashMap;

import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;
import algoanim.util.Timing;

public  class JavaSourceCodeField extends SourceCodeField
{

    private HashMap<Integer, IndexPath> scopesMap;
    
	
	public JavaSourceCodeField(Language language, Coordinates upperLeft, String sourceCode, String methodName ,int initialRow, int maxRows, int highlightedScope, Timing defaultTiming)
	{
		super(language, upperLeft, sourceCode, methodName, initialRow, maxRows, highlightedScope, defaultTiming);
	}
	
    public void generateSourceCodeFromSourceString(Coordinates upperLeft, String sourceString, String methodname ,int initialRow, int maxRows, int highlightedScope, Timing timing)
    {
        
        String[] sourceLines = sourceString.split("\n");
        
        scopesMap = new HashMap<Integer, IndexPath>();
        
                
        for (int i = 0; i < sourceLines.length; i++)
        {
    		String sourceLine = sourceLines[i];

        	if (sourceLine.contains("public") || sourceLine.contains("private"))
    		{
    			String currentMethodname = this.getMethodNameFromString(sourceLine);
    			
    			if (currentMethodname.equals(methodname))
    			{
    				int lastScopeLevel = 0;
    				int scopeIdentifier = 0;
    		        int initialScopeLayerOffset = 0;
    		        
    		        int scopeStartIndex = 0;
    		        
    		        int maxLines = Math.min(i + initialRow + maxRows, sourceLines.length);
    	        	for (int methodScopeIndex =  Math.min(i + initialRow, sourceLines.length); methodScopeIndex < maxLines; methodScopeIndex++)
    	        	{
    	        		String methodLine = sourceLines[methodScopeIndex];
    	        		
    	        		int scopeLevel = this.getLeadingTabs(methodLine);
    	        		
        	        	if (methodScopeIndex-i-initialRow <= 0 && scopeLevel > 0) initialScopeLayerOffset = scopeLevel;
        	        	
        	        	scopeLevel -= initialScopeLayerOffset;
        	        	
        	        	if (scopeLevel != lastScopeLevel || methodScopeIndex == maxLines-1)
        	        	{
        	        		if (methodScopeIndex == maxLines-1) methodScopeIndex++;
        	        		IndexPath p = new IndexPath(scopeStartIndex , methodScopeIndex-i-initialRow - scopeStartIndex);
        	        		scopesMap.put(scopeIdentifier, p);
        	        		lastScopeLevel = scopeLevel;
        	        		scopeIdentifier++;
        	        		scopeStartIndex = methodScopeIndex-i-initialRow;
        	        	}
        	        	
    	        		if ((methodScopeIndex-i-initialRow > 0 && (methodLine.contains("public") || methodLine.contains("private")))) break;
    	        		
    	        		//System.out.println("" + methodLine + " S:"+scopeIdentifier);
        	        	this.addSourceCodeLine(methodLine, timing);
    	        		//src.addCodeLine(methodLine, methodname, scopeIdentifier, null);
    	        	}
    	        	
    	        	break;
    			}
    		}
        	
        }
    }
    
    private int getLeadingTabs(String s)
    {
        int indentation=0;
        while (s.length() > indentation && s.charAt(indentation)=='\t') 
          indentation++;
        return indentation;
    }
    
    /**
     * returns the method name if possible.
     * @param sourceCode
     * @return
     */
    private String getMethodNameFromString(String sourceCode)
    {
    	String methodName = "main";
    	
    	String[] firstSplit = sourceCode.split("\\(");
    	if (firstSplit != null && firstSplit.length > 0)
    	{
    		String tmp = firstSplit[0];
        	String[] secondSplit = tmp.split(" ");
        	if (secondSplit != null && secondSplit.length > 0)
        	{
            	methodName = secondSplit[secondSplit.length - 1];
        	}
    	}
    	
    	
    	return methodName;
    }
    
    public void highlightScope(int level)
    {
    	if (scopesMap != null){
    		IndexPath p = scopesMap.get(level);
        	if (p != null)
        	{
        		int start = p.getSection();
        		int count = p.getRowsCount();
        		
        		for (int i = start; i < start + count; i++){
        			this.highlight(i, null);
        		}
        	}
    	}
    }
    
    public void unhighlightScope(int level)
    {
    	if (scopesMap != null){
    		IndexPath p = scopesMap.get(level);
        	if (p != null)
        	{
        		int start = p.getSection();
        		int count = p.getRowsCount();
        		for (int i = start; i < start + count; i++){
        			this.unhighlight(i, null);
        		}
        	}
    	}
    }
    
}
package generators.graphics.helpers;
import generators.helpers.OffsetCoords;

import java.awt.Color;
import java.util.List;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.Rect;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.RectProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class WSTAnim {
	private Language lang;
	private Coordinates bottomLeft;
	private WSTRect[] rects;
	private Rect waterRect;
	private int waterlevel;
	private Color waterColor;

	private int max;

	private StringArray sa;
	private ArrayProperties arrayProps;
	TwoValueView tvv;

	public WSTAnim(Language lang, Coordinates bottomLeft, List<Integer> arr, ArrayProperties arrayProps, Color waterColor) {
		super();
		this.lang = lang;
		this.bottomLeft = bottomLeft;
		this.arrayProps = arrayProps;
		this.max =  WSTAlgo.arrayMax(arr);
		this.waterColor = waterColor;

		initializeStringArray(arr);
		initializeWSTRects(arr);
		waterRect = initializeWaterRect();

		TwoValueCounter counter = lang.newCounter(sa); // Zaehler anlegen 
		CounterProperties cp = new CounterProperties(); // Zaehler-Properties anlegen 
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // gefuellt... 
		cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE); // ...mit Blau
		// view anlegen, Parameter:
		// 1. Counter
		//   2. linke obere Ecke;
		//   3. CounterProperties;
		//   4. Anzeige Zaehlerwert als Zahl?
		// 5. Anzeige Zaehlerwert als Balken?
		// Alternativ: nur Angabe Counter, Koordinate und Properties
		tvv = lang.newCounterView(counter, new Offset(0, 20, sa, AnimalScript.DIRECTION_SW), cp, true, false);
		
	}

	private void initializeWSTRects(List<Integer> arr) {
		rects = new WSTRect[arr.size()];
		for (int i = 0; i < arr.size(); i++)
		{
			rects[i] = new WSTRect(i, arr.get(i), this);
			rects[i].updateRect();
		}
	}
	
	private Rect initializeWaterRect() {
		RectProperties rectProperties = new RectProperties();

		rectProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, waterColor);
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, waterColor);
		rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY,
				Boolean.TRUE);
		rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		return lang.newRect(
				new OffsetCoords(getBottomLeft(), 0,
						-Tools.heightForValue(waterlevel, getMax())),
				new OffsetCoords(getBottomLeft(), 26 + (getElementCount()-1) * 26, 0),
				"waterRect", null, rectProperties);
	}
	
	public void updateAnim() {
		Rect tmp = initializeWaterRect();
		waterRect.hide();
		waterRect = tmp;
		for (int i = 0; i < getElementCount(); i++)
		{
			if (rects[i].setValue(getArrElementWithoutCounter(i)))
			{
				//sa.put(i, Tools.intToStr(getArrElementWithoutCounter(i)), null, null);
				//sa.highlightElem(i, null, null);
			}
			else
				sa.unhighlightElem(i, null, null);
		}
	}
	
	public void setArrElement(Integer index, Integer value)
	{
		sa.put(index, Tools.intToStr(value), null, null);
		sa.highlightElem(index, null, null);
		updateAnim();
	}

	private void initializeStringArray(List<Integer> arr) {
		sa = lang.newStringArray(bottomLeft, Tools.intToStrArray(arr),
				"intArray", null, arrayProps);
	}

	public Language getLang() {
		return lang;
	}

	public Coordinates getBottomLeft() {
		return bottomLeft;
	}

	public WSTRect[] getRects() {
		return rects;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getWaterlevel() {
		return waterlevel;
	}

	public void setWaterlevel(int waterlevel) {
		this.waterlevel = waterlevel;
		updateAnim();
	}
	
	public StringArray getSA() {
		return sa;
	}

	public Integer getArrElement(int index) {
		return Integer.parseInt(getSA().getData(index).trim());
	}
	
	public Integer getArrElementWithoutCounter(int index) {
		return Integer.parseInt(getSA().getData(index).trim());
	}
	
	public Integer getElementCount() {
		return getSA().getLength();
	}
	
	public void hide()
	{
		sa.hide();
		waterRect.hide();
		for (WSTRect r: rects)
			r.hide();
		tvv.hide();
	}
}

package engine.utilities;

/**{@link Deprecated}
 * PrimitiveRange is use for bounding values.
 * All methods are static except for toString().
 * If PrimitiveRange is <b>null</b> it is treated as an.
 * unbound PrimitiveRange (-infinity, infinity)
 * @author BobbyL2k
 */
public class PrimitiveRange{
	public float min,max;
	
	public PrimitiveRange(float min, float max) {
		super();
		if(max < min)
			max = min;
		this.min = min;
		this.max = max;
	}

	@Override
	public String toString() {
		return "PrimitiveRange from " + this.min + " to " + this.max;
	}
	
	public static float bound(PrimitiveRange o, float in){
		if(o == null)
			return in;
		else
			if(in < o.min)
				return o.min;
			else if(in > o.max)
				return o.max;
			else return in;
	}
	
	public static float normalize(float value, PrimitiveRange from, PrimitiveRange to){
		if(from == null || to == null)
			throw new RuntimeException("Can't normalize an unbound PrimitiveRange");
		value = bound(from, value);
		return to.min + ((value - from.min) *size(to) / size(from));
	}
	
	public static PrimitiveRange offset(PrimitiveRange o, float offsetValue){
		if(o == null)
			return null;
		else
			return new PrimitiveRange(o.min + offsetValue, o.max + offsetValue);
	}
	
	public static float random(PrimitiveRange o){
		return (float) ( o.min + Math.random()*size(o) );
	}
	
	public static boolean overlap(PrimitiveRange o1, PrimitiveRange o2){
		if(o1 == null || o2 == null)
			return true;
		return inPrimitiveRange(o1, o2.max) || inPrimitiveRange(o1, o2.min) || inPrimitiveRange(o2, o1.min);
	}

	public static boolean inPrimitiveRange(PrimitiveRange o, float in){
		return o.min <= in && in <= o.max;
	}
	public static boolean outsidePrimitiveRange(PrimitiveRange o, float in){
		return !inPrimitiveRange(o, in);
	}
	public static float size(PrimitiveRange o){
		if(o == null)
			return Float.NaN;
		else
			return o.max-o.min;
	}
	
	public static float center(PrimitiveRange o){
		if(o == null)
			return Float.NaN;
		else
			return (o.max+o.min)/2;
	}
}

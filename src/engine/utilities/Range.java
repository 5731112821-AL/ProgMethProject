package engine.utilities;

public class Range{
	public float min,max;
	
	public Range(float min, float max) {
		super();
		if(max < min)
			max = min;
		this.min = min;
		this.max = max;
	}

	public float bound(float in){
		if(in < min)
			return min;
		if(in > max)
			return max;
		return in;
	}
	
	public static float normalize(float value, Range from, Range to){
		value = from.bound(value);
		return to.min + ((value - from.min) *to.size() / from.size());
		// TODO recheck formular
	}
	
	public static float map(float value, Range from, Range to){
		return value *to.max / from.max;
	}
	
	public static Range offset(Range o, float offsetValue){
		return new Range(o.min + offsetValue, o.max + offsetValue);
	}
	
	public float random(){
		return (float) ( min + Math.random()*size() );
	}
	
	public boolean overlap(Range o){
		return this.inRange(o.max) || this.inRange(o.min) || o.inRange(this.min);
	}
	
	@Override
	public String toString() {
		return "Range from " + this.min + " to " + this.max;
	}

	public boolean inRange(float in){
		return min <= in && in <= max;
	}
	public boolean outsideRange(float in){
		return !inRange(in);
	}
	public float size(){
		return max-min;
	}
	
	public float center() {
		return (max + min)/2;
	}
}

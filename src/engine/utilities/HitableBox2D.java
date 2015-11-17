package engine.utilities;

/**
 * @author L2k-nForce
 * HitableBox2D adds a 2-D hit box to an object
 * The hit box's boundary is relative to the object therefore HitableBox2D
 * extends off from Cordinate2D in order to get absolute hit box location
 */
public interface HitableBox2D extends Cordinate2D{

	/**
	 * @return Object's hit box X-axis boundary is relative to the object
	 */
	public Range getHitBoxXRange();
	/**
	 * @return Object's hit box Y-axis boundary is relative to the object
	 */
	public Range getHitBoxYRange();
	
	/**
	 * @param obj - Other HitableBox2D object
	 * @return true - if the Other object's hit box is hitting this object hit box 
	 */
	default boolean isHitting(HitableBox2D obj){
		Range 
			objRealHitBoxX  = 	Range.offset( obj.getHitBoxXRange(), 	obj.getX()  ),
			objRealHitBoxY  = 	Range.offset( obj.getHitBoxYRange(),	obj.getY()  ),
		
			thisRealHitBoxX = 	Range.offset( this.getHitBoxXRange(), 	this.getX() ),
			thisRealHitBoxY = 	Range.offset( this.getHitBoxYRange(), 	this.getY() );
		
		return 
			thisRealHitBoxX.overlap(objRealHitBoxX)
			&&
			thisRealHitBoxY.overlap(objRealHitBoxY);
	}
}

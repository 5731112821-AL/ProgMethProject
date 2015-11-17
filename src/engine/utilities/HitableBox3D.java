package engine.utilities;

public interface HitableBox3D extends HitableBox2D, Cordinate3D{
	
	public Range getHitBoxXRange();
	public Range getHitBoxYRange();
	public Range getHitBoxZRange();
	
	default boolean isHitting(HitableBox3D obj){
		Range 
			objRealHitBoxX  = 	Range.offset( obj.getHitBoxXRange(), 	obj.getX()  ),
			objRealHitBoxY  = 	Range.offset( obj.getHitBoxYRange(),	obj.getY()  ),
			objRealHitBoxZ  = 	Range.offset( obj.getHitBoxZRange(),	obj.getZ()  ),
		
			thisRealHitBoxX = 	Range.offset( this.getHitBoxXRange(), 	this.getX() ),
			thisRealHitBoxY = 	Range.offset( this.getHitBoxYRange(), 	this.getY() ),
			thisRealHitBoxZ = 	Range.offset( this.getHitBoxZRange(), 	this.getZ() );
		
		return 
			thisRealHitBoxX.overlap(objRealHitBoxX)
			&&
			thisRealHitBoxY.overlap(objRealHitBoxY)
			&&
			thisRealHitBoxZ.overlap(objRealHitBoxZ);
	}
}

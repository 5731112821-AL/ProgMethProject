package engine.utilities;

import engine.game.Logic.Updatable;

public interface Moving2D extends Cordinate2D, Updatable {

	public float getSpeedX();
	public float getSpeedY();

	void setX(float x);
	void setY(float y);
	
	@Override
	default void update(long frameTime) {
		setX( getX() + getSpeedX()*frameTime );
		setY( getY() + getSpeedY()*frameTime );
	}
}

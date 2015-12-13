package flyerGame.factory;

import flyerGame.gameObject.Bullet;
import flyerGame.gameObject.Target;

public abstract class BulletFactory {
	
	private Class <? extends Target> parentClass;
	
	public Class<? extends Target> getParentClass() {
		return parentClass;
	}

	public BulletFactory(Class <? extends Target> parentClass) {
		super();
		this.parentClass = parentClass;
	}

	public abstract Bullet createBullet();
}


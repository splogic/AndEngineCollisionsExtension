package com.makersf.andengine.extension.collisions.entity.sprite;

import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.makersf.andengine.extension.collisions.entity.shape.IPixelPerfectShape;
import com.makersf.andengine.extension.collisions.opengl.texture.region.PixelPerfectTextureRegion;
import com.makersf.andengine.extension.collisions.pixelperfect.PixelPerfectCollisionChecker;
import com.makersf.andengine.extension.collisions.pixelperfect.masks.IPixelPerfectMask;
import com.makersf.andengine.extension.collisions.pixelperfect.masks.RectangularPixelPerfectMask;
import com.makersf.andengine.extension.collisions.pixelperfect.masks.RectangularPixelPerfectMaskPool;

/**
 * 
 * @author Francesco Zoffoli
 * @since 01.08.2012
 *
 */
public class PixelPerfectSprite extends Sprite implements IPixelPerfectShape{
	
	private static boolean USE_PIXELPERFECT_COLLISION_FOR_EVERY_RECTANGULAR_SHAPE = true;

	private IPixelPerfectMask mPixelMask;

	public PixelPerfectSprite(float pX, float pY,
			PixelPerfectTextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		mPixelMask = pTextureRegion.getPixelMask();
	}

	public PixelPerfectSprite(float pX, float pY,
			ITextureRegion pTextureRegion,
			IPixelPerfectMask pPixelMask,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		mPixelMask = pPixelMask;
	}

	public PixelPerfectSprite(float pX, float pY, float pWidth, float pHeight,
			PixelPerfectTextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		mPixelMask = pTextureRegion.getPixelMask();
	}

	public PixelPerfectSprite(float pX, float pY, float pWidth, float pHeight,
			PixelPerfectTextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			DrawType pDrawType, ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager,
				pDrawType, pShaderProgram);
		mPixelMask = pTextureRegion.getPixelMask();
	}

	@Override
	public IPixelPerfectMask getPixelPerfectMask() {
		return mPixelMask;
	}
	
	public boolean collidesWith(final RectangularShape pOtherShape) {
		if(super.collidesWith(pOtherShape))
		{
			if(pOtherShape instanceof IPixelPerfectShape)
				return PixelPerfectCollisionChecker.checkCollision(this, this.getPixelPerfectMask(), pOtherShape, ((IPixelPerfectShape)pOtherShape).getPixelPerfectMask());
			else
			{
				if(!USE_PIXELPERFECT_COLLISION_FOR_EVERY_RECTANGULAR_SHAPE)
					return true;
				
				RectangularPixelPerfectMaskPool rectangularPixelPerfectMaskPool = RectangularPixelPerfectMaskPool.getInstance();
				
				RectangularPixelPerfectMask reusableRectangularPixelPerfectMask = rectangularPixelPerfectMaskPool.obtainPoolItem();
				reusableRectangularPixelPerfectMask.setTo((int) pOtherShape.getWidth(), (int) pOtherShape.getHeight());
				
				boolean result = PixelPerfectCollisionChecker.checkCollision(this, this.getPixelPerfectMask(), pOtherShape, reusableRectangularPixelPerfectMask);
				
				rectangularPixelPerfectMaskPool.recyclePoolItem(reusableRectangularPixelPerfectMask);
				
				return result;
			}
		}
		else
			return false;
	}

	public static void setUsePixelPerfectCollisionForEveryRectangularShape(final boolean pUsePixelPerfectCollisionForEveryRectangularShape) {
		USE_PIXELPERFECT_COLLISION_FOR_EVERY_RECTANGULAR_SHAPE = pUsePixelPerfectCollisionForEveryRectangularShape;
	}

}

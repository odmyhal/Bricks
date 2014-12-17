package org.bricks.enterprise.d3.help;


import com.badlogic.gdx.math.Vector3;

public class Vector3Helper {
	
	private Vector3 normalTmp1 = new Vector3();
	private Vector3 normalTmp2 = new Vector3();
	
	private Vector3Helper(){}

	private static final ThreadLocal<Vector3Helper> localHelper = new ThreadLocal<Vector3Helper>() {
        @Override protected Vector3Helper initialValue() {
            return new Vector3Helper();
        }
    };
    
    public static Vector3Helper instance(){
    	return localHelper.get();
    }
	
	public Vector3 vectorSquareEquation(Vector3 a, Vector3 b, Vector3 c){
		float baz = -1 * a.z / b.z;
		b.scl(baz).add(a);
		float caz = -1 * a.z / c.z;
		c.scl(caz).add(a);
		float cby = -1 * b.y / c.y;
		c.scl(cby).add(b);
		float X = c.x;
		float Y = -1 * X * b.x / b.y;
		float Z = -1 * (X * a.x + Y * a.y) / a.z;
		return new Vector3(X, Y, Z);
	}
	
	public Vector3 calcNormal(Vector3 zero, Vector3 one, Vector3 two){
		normalTmp1.set(one.x - zero.x, one.y - zero.y, one.z - zero.z);
		normalTmp2.set(two.x - zero.x, two.y - zero.y, two.z - zero.z);
		Vector3 res = new Vector3();
		return calcVectorNormal(normalTmp1, normalTmp2, res);
/*		res.x = normalTmp2.y * normalTmp1.z - normalTmp2.z * normalTmp1.y;
		res.y = normalTmp2.z * normalTmp1.x - normalTmp2.x * normalTmp1.z;
		res.z = normalTmp2.x * normalTmp1.y - normalTmp2.y * normalTmp1.x;
		return res.nor();*/
	}
	
	public Vector3 calcVectorNormal(Vector3 one, Vector3 two, Vector3 result){
		result.x = two.y * one.z - two.z * one.y;
		result.y = two.z * one.x - two.x * one.z;
		result.z = two.x * one.y - two.y * one.x;
		return result.nor();
	}
}

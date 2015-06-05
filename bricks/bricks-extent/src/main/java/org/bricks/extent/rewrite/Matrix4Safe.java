package org.bricks.extent.rewrite;


import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Matrix4Safe extends Matrix4 {

	private static final ThreadLocal<SafeData> safeData = new ThreadLocal<SafeData>(){
		@Override protected SafeData initialValue() {
            return new SafeData();
        }
	};

	private static class SafeData{
		private final float tmp[] = new float[16];
		
		private final Quaternion quat = new Quaternion();
		private Quaternion quat2 = new Quaternion();
		
		private final Vector3 tmpVec = new Vector3();
		private final Matrix4 tmpMat = new Matrix4();
		
		private final Vector3 right = new Vector3();
		private final Vector3 tmpForward = new Vector3();
		private final Vector3 tmpUp = new Vector3();
	}
	
	public static final Matrix4 safeTmpMatrix(){
		return safeData.get().tmpMat;
	}
	
	public static final float[] safeTmpArray(){
		return safeData.get().tmp;
	}
	
	public static final Vector3 safeTmpVector(){
		return safeData.get().right;
	}
	
	public static final Quaternion safeTmpQuaternion(){
		return safeData.get().quat;
	}
	
	public Matrix4 mulLeft (Matrix4 matrix) {
		Matrix4 tmpMat = safeData.get().tmpMat;
		tmpMat.set(matrix);
		mul(tmpMat.val, this.val);
		return set(tmpMat);
	}

	/** Transposes the matrix.
	 * 
	 * @return This matrix for the purpose of chaining methods together. */
	public Matrix4 tra () {
		float[] tmp = safeData.get().tmp;
		tmp[M00] = val[M00];
		tmp[M01] = val[M10];
		tmp[M02] = val[M20];
		tmp[M03] = val[M30];
		tmp[M10] = val[M01];
		tmp[M11] = val[M11];
		tmp[M12] = val[M21];
		tmp[M13] = val[M31];
		tmp[M20] = val[M02];
		tmp[M21] = val[M12];
		tmp[M22] = val[M22];
		tmp[M23] = val[M32];
		tmp[M30] = val[M03];
		tmp[M31] = val[M13];
		tmp[M32] = val[M23];
		tmp[M33] = val[M33];
		return set(tmp);
	}
	
	public Matrix4 inv () {
		float[] tmp = safeData.get().tmp;
		float l_det = val[M30] * val[M21] * val[M12] * val[M03] - val[M20] * val[M31] * val[M12] * val[M03] - val[M30] * val[M11]
			* val[M22] * val[M03] + val[M10] * val[M31] * val[M22] * val[M03] + val[M20] * val[M11] * val[M32] * val[M03] - val[M10]
			* val[M21] * val[M32] * val[M03] - val[M30] * val[M21] * val[M02] * val[M13] + val[M20] * val[M31] * val[M02] * val[M13]
			+ val[M30] * val[M01] * val[M22] * val[M13] - val[M00] * val[M31] * val[M22] * val[M13] - val[M20] * val[M01] * val[M32]
			* val[M13] + val[M00] * val[M21] * val[M32] * val[M13] + val[M30] * val[M11] * val[M02] * val[M23] - val[M10] * val[M31]
			* val[M02] * val[M23] - val[M30] * val[M01] * val[M12] * val[M23] + val[M00] * val[M31] * val[M12] * val[M23] + val[M10]
			* val[M01] * val[M32] * val[M23] - val[M00] * val[M11] * val[M32] * val[M23] - val[M20] * val[M11] * val[M02] * val[M33]
			+ val[M10] * val[M21] * val[M02] * val[M33] + val[M20] * val[M01] * val[M12] * val[M33] - val[M00] * val[M21] * val[M12]
			* val[M33] - val[M10] * val[M01] * val[M22] * val[M33] + val[M00] * val[M11] * val[M22] * val[M33];
		if (l_det == 0f) throw new RuntimeException("non-invertible matrix");
		float inv_det = 1.0f / l_det;
		tmp[M00] = val[M12] * val[M23] * val[M31] - val[M13] * val[M22] * val[M31] + val[M13] * val[M21] * val[M32] - val[M11]
			* val[M23] * val[M32] - val[M12] * val[M21] * val[M33] + val[M11] * val[M22] * val[M33];
		tmp[M01] = val[M03] * val[M22] * val[M31] - val[M02] * val[M23] * val[M31] - val[M03] * val[M21] * val[M32] + val[M01]
			* val[M23] * val[M32] + val[M02] * val[M21] * val[M33] - val[M01] * val[M22] * val[M33];
		tmp[M02] = val[M02] * val[M13] * val[M31] - val[M03] * val[M12] * val[M31] + val[M03] * val[M11] * val[M32] - val[M01]
			* val[M13] * val[M32] - val[M02] * val[M11] * val[M33] + val[M01] * val[M12] * val[M33];
		tmp[M03] = val[M03] * val[M12] * val[M21] - val[M02] * val[M13] * val[M21] - val[M03] * val[M11] * val[M22] + val[M01]
			* val[M13] * val[M22] + val[M02] * val[M11] * val[M23] - val[M01] * val[M12] * val[M23];
		tmp[M10] = val[M13] * val[M22] * val[M30] - val[M12] * val[M23] * val[M30] - val[M13] * val[M20] * val[M32] + val[M10]
			* val[M23] * val[M32] + val[M12] * val[M20] * val[M33] - val[M10] * val[M22] * val[M33];
		tmp[M11] = val[M02] * val[M23] * val[M30] - val[M03] * val[M22] * val[M30] + val[M03] * val[M20] * val[M32] - val[M00]
			* val[M23] * val[M32] - val[M02] * val[M20] * val[M33] + val[M00] * val[M22] * val[M33];
		tmp[M12] = val[M03] * val[M12] * val[M30] - val[M02] * val[M13] * val[M30] - val[M03] * val[M10] * val[M32] + val[M00]
			* val[M13] * val[M32] + val[M02] * val[M10] * val[M33] - val[M00] * val[M12] * val[M33];
		tmp[M13] = val[M02] * val[M13] * val[M20] - val[M03] * val[M12] * val[M20] + val[M03] * val[M10] * val[M22] - val[M00]
			* val[M13] * val[M22] - val[M02] * val[M10] * val[M23] + val[M00] * val[M12] * val[M23];
		tmp[M20] = val[M11] * val[M23] * val[M30] - val[M13] * val[M21] * val[M30] + val[M13] * val[M20] * val[M31] - val[M10]
			* val[M23] * val[M31] - val[M11] * val[M20] * val[M33] + val[M10] * val[M21] * val[M33];
		tmp[M21] = val[M03] * val[M21] * val[M30] - val[M01] * val[M23] * val[M30] - val[M03] * val[M20] * val[M31] + val[M00]
			* val[M23] * val[M31] + val[M01] * val[M20] * val[M33] - val[M00] * val[M21] * val[M33];
		tmp[M22] = val[M01] * val[M13] * val[M30] - val[M03] * val[M11] * val[M30] + val[M03] * val[M10] * val[M31] - val[M00]
			* val[M13] * val[M31] - val[M01] * val[M10] * val[M33] + val[M00] * val[M11] * val[M33];
		tmp[M23] = val[M03] * val[M11] * val[M20] - val[M01] * val[M13] * val[M20] - val[M03] * val[M10] * val[M21] + val[M00]
			* val[M13] * val[M21] + val[M01] * val[M10] * val[M23] - val[M00] * val[M11] * val[M23];
		tmp[M30] = val[M12] * val[M21] * val[M30] - val[M11] * val[M22] * val[M30] - val[M12] * val[M20] * val[M31] + val[M10]
			* val[M22] * val[M31] + val[M11] * val[M20] * val[M32] - val[M10] * val[M21] * val[M32];
		tmp[M31] = val[M01] * val[M22] * val[M30] - val[M02] * val[M21] * val[M30] + val[M02] * val[M20] * val[M31] - val[M00]
			* val[M22] * val[M31] - val[M01] * val[M20] * val[M32] + val[M00] * val[M21] * val[M32];
		tmp[M32] = val[M02] * val[M11] * val[M30] - val[M01] * val[M12] * val[M30] - val[M02] * val[M10] * val[M31] + val[M00]
			* val[M12] * val[M31] + val[M01] * val[M10] * val[M32] - val[M00] * val[M11] * val[M32];
		tmp[M33] = val[M01] * val[M12] * val[M20] - val[M02] * val[M11] * val[M20] + val[M02] * val[M10] * val[M21] - val[M00]
			* val[M12] * val[M21] - val[M01] * val[M10] * val[M22] + val[M00] * val[M11] * val[M22];
		val[M00] = tmp[M00] * inv_det;
		val[M01] = tmp[M01] * inv_det;
		val[M02] = tmp[M02] * inv_det;
		val[M03] = tmp[M03] * inv_det;
		val[M10] = tmp[M10] * inv_det;
		val[M11] = tmp[M11] * inv_det;
		val[M12] = tmp[M12] * inv_det;
		val[M13] = tmp[M13] * inv_det;
		val[M20] = tmp[M20] * inv_det;
		val[M21] = tmp[M21] * inv_det;
		val[M22] = tmp[M22] * inv_det;
		val[M23] = tmp[M23] * inv_det;
		val[M30] = tmp[M30] * inv_det;
		val[M31] = tmp[M31] * inv_det;
		val[M32] = tmp[M32] * inv_det;
		val[M33] = tmp[M33] * inv_det;
		return this;
	}

	public Matrix4 setToLookAt (Vector3 position, Vector3 target, Vector3 up) {
		SafeData sd = safeData.get();
		Vector3 tmpVec = sd.tmpVec;
		Matrix4 tmpMat = sd.tmpMat;
		
		tmpVec.set(target).sub(position);
		setToLookAt(tmpVec, up);
		this.mul(tmpMat.setToTranslation(-position.x, -position.y, -position.z));

		return this;
	}
	
	public Matrix4 setToWorld (Vector3 position, Vector3 forward, Vector3 up) {
		SafeData sd = safeData.get();
		Vector3 tmpForward = sd.tmpForward;
		Vector3 right = sd.right;
		Vector3 tmpUp = sd.tmpUp;
		
		tmpForward.set(forward).nor();
		right.set(tmpForward).crs(up).nor();
		tmpUp.set(right).crs(tmpForward).nor();

		this.set(right, tmpUp, tmpForward.scl(-1), position);
		return this;
	}
	
	public Matrix4 avg (Matrix4 other, float w) {
		SafeData sd = safeData.get();
		Vector3 tmpForward = sd.tmpForward;
		Vector3 tmpVec = sd.tmpVec;
		Quaternion quat = sd.quat;
		Quaternion quat2 = sd.quat2;
		Vector3 tmpUp = sd.tmpUp;
		Vector3 right = sd.right;
		
		getScale(tmpVec);
		other.getScale(tmpForward);

		getRotation(quat);
		other.getRotation(quat2);

		getTranslation(tmpUp);
		other.getTranslation(right);

		setToScaling(tmpVec.scl(w).add(tmpForward.scl(1 - w)));
		rotate(quat.slerp(quat2, 1 - w));
		setTranslation(tmpUp.scl(w).add(right.scl(1 - w)));

		return this;
	}

	public Matrix4 avg (Matrix4[] t) {
		SafeData sd = safeData.get();
		Vector3 tmpForward = sd.tmpForward;
		Vector3 tmpVec = sd.tmpVec;
		Quaternion quat = sd.quat;
		Quaternion quat2 = sd.quat2;
		Vector3 tmpUp = sd.tmpUp;
		
		final float w = 1.0f / t.length;

		tmpVec.set(t[0].getScale(tmpUp).scl(w));
		quat.set(t[0].getRotation(quat2).exp(w));
		tmpForward.set(t[0].getTranslation(tmpUp).scl(w));

		for (int i = 1; i < t.length; i++) {
			tmpVec.add(t[i].getScale(tmpUp).scl(w));
			quat.mul(t[i].getRotation(quat2).exp(w));
			tmpForward.add(t[i].getTranslation(tmpUp).scl(w));
		}
		quat.nor();

		setToScaling(tmpVec);
		rotate(quat);
		setTranslation(tmpForward);

		return this;
	}

	public Matrix4 avg (Matrix4[] t, float[] w) {
		SafeData sd = safeData.get();
		Vector3 tmpForward = sd.tmpForward;
		Vector3 tmpVec = sd.tmpVec;
		Quaternion quat = sd.quat;
		Quaternion quat2 = sd.quat2;
		Vector3 tmpUp = sd.tmpUp;
		
		tmpVec.set(t[0].getScale(tmpUp).scl(w[0]));
		quat.set(t[0].getRotation(quat2).exp(w[0]));
		tmpForward.set(t[0].getTranslation(tmpUp).scl(w[0]));

		for (int i = 1; i < t.length; i++) {
			tmpVec.add(t[i].getScale(tmpUp).scl(w[i]));
			quat.mul(t[i].getRotation(quat2).exp(w[i]));
			tmpForward.add(t[i].getTranslation(tmpUp).scl(w[i]));
		}
		quat.nor();

		setToScaling(tmpVec);
		rotate(quat);
		setTranslation(tmpForward);

		return this;
	}
	
	public Matrix4 translate (float x, float y, float z) {
		float[] tmp = safeData.get().tmp;
		tmp[M00] = 1;
		tmp[M01] = 0;
		tmp[M02] = 0;
		tmp[M03] = x;
		tmp[M10] = 0;
		tmp[M11] = 1;
		tmp[M12] = 0;
		tmp[M13] = y;
		tmp[M20] = 0;
		tmp[M21] = 0;
		tmp[M22] = 1;
		tmp[M23] = z;
		tmp[M30] = 0;
		tmp[M31] = 0;
		tmp[M32] = 0;
		tmp[M33] = 1;

		mul(val, tmp);
		return this;
	}

	public Matrix4 rotate (Vector3 axis, float degrees) {
		Quaternion quat = safeData.get().quat;
		
		if (degrees == 0) return this;
		quat.set(axis, degrees);
		return rotate(quat);
	}

	public Matrix4 rotateRad (Vector3 axis, float radians) {
		Quaternion quat = safeData.get().quat;
		
		if (radians == 0) return this;
		quat.setFromAxisRad(axis, radians);
		return rotate(quat);
	}

	public Matrix4 rotate (float axisX, float axisY, float axisZ, float degrees) {
		Quaternion quat = safeData.get().quat;
		
		if (degrees == 0) return this;
		quat.setFromAxis(axisX, axisY, axisZ, degrees);
		return rotate(quat);
	}

	public Matrix4 rotateRad (float axisX, float axisY, float axisZ, float radians) {
		Quaternion quat = safeData.get().quat;
		
		if (radians == 0) return this;
		quat.setFromAxisRad(axisX, axisY, axisZ, radians);
		return rotate(quat);
	}

	public Matrix4 rotate (Quaternion rotation) {
		float[] tmp = safeData.get().tmp;
		
		rotation.toMatrix(tmp);
		mul(val, tmp);
		return this;
	}

	public Matrix4 rotate (final Vector3 v1, final Vector3 v2) {
		Quaternion quat = safeData.get().quat;
		
		return rotate(quat.setFromCross(v1, v2));
	}


	public Matrix4 scale (float scaleX, float scaleY, float scaleZ) {
		float[] tmp = safeData.get().tmp;
		
		tmp[M00] = scaleX;
		tmp[M01] = 0;
		tmp[M02] = 0;
		tmp[M03] = 0;
		tmp[M10] = 0;
		tmp[M11] = scaleY;
		tmp[M12] = 0;
		tmp[M13] = 0;
		tmp[M20] = 0;
		tmp[M21] = 0;
		tmp[M22] = scaleZ;
		tmp[M23] = 0;
		tmp[M30] = 0;
		tmp[M31] = 0;
		tmp[M32] = 0;
		tmp[M33] = 1;

		mul(val, tmp);
		return this;
	}
}

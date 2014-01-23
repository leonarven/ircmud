package com.cb2.ircmud.domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public final class Vec3 implements Serializable {
	private static final long serialVersionUID = 1L;
	private double x;
	private double y;
	private double z;
	
	public Vec3() {
		this.x = this.y = this.z = 0;
	}
	
	public Vec3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setX(double x) { this.x = x; }
	public double getX() { return x; }
	public void setY(double y) { this.y = y; }
	public double getY() { return y; }
	public void setZ(double z) { this.z = z; }
	public double getZ() { return z; }
	
	public double length() {
		return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}
	
	
	private void readObject( ObjectInputStream aInputStream) throws ClassNotFoundException, IOException {
		 aInputStream.defaultReadObject();
	}

	private void writeObject( ObjectOutputStream aOutputStream) throws IOException {
		aOutputStream.defaultWriteObject();
	}
	
}

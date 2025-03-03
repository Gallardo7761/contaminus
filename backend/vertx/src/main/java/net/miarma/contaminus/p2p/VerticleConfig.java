package net.miarma.contaminus.p2p;

public class VerticleConfig {
	private String name;
	private int num;
	private boolean isDup;
	
	public VerticleConfig() {
		
	}
	
	public VerticleConfig(String name, int num, boolean isDup) {
		this.isDup = isDup;
		this.num = num;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public boolean isDup() {
		return isDup;
	}

	public void setDup(boolean isDup) {
		this.isDup = isDup;
	}
	
	
}

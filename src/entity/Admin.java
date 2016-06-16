package entity;

public class Admin {
	private int id;
	private String  name;
	private String  addr;
	private Double height;
	private Info info;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	public Info getInfo(){
		return info;
	}
	public void setInfo(Info info) {
		this.info = info;
	}
}

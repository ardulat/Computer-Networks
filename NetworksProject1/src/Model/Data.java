package Model;

public class Data {

	// Instances
	private String filename;
	private String format;
	private String size;
	private String lastModified;
	private String ip;
	private String port;
	
	// Initializer
	public Data(String filename,
				String format,
				String size,
				String lastModified,
				String ip,
				String port) {
		this.filename = filename;
		this.format = format;
		this.size = size;
		this.lastModified = lastModified;
		this.ip = ip;
		this.port = port;
	}
	
	// MARK: - Setters
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	public void setSize(String size) {
		this.size = size;
	}
	
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public void setPort(String port) {
		this.port = port;
	}
	
	// MARK: - Getters
	
	public String getFilename() {
		return filename;
	}
	
	public String getFormat() {
		return format;
	}
	
	public String getSize() {
		return size;
	}
	
	public String getLastModified() {
		return lastModified;
	}
	
	public String getIp() {
		return ip;
	}
	
	public String getPort() {
		return port;
	}
	
	// Generate one tuple
	public String toString() {
		return "<" + filename + 
				", " + format +
				", " + size +
				", " + lastModified +
				", " + ip + 
				", " + port + ">";
	}
}

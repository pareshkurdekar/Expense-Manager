package tableDAO;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "data_read_table")
public class data_read_table {
	
	@Lob
	@Type(type = "org.hibernate.type.TimestampType")
	private Timestamp read_time;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String status;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String month;
	
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String year;
	
	@Id
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String file_name;
	
	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String file_path;

	public Timestamp getRead_time() {
		return read_time;
	}

	public void setRead_time(Timestamp read_time) {
		this.read_time = read_time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	

}

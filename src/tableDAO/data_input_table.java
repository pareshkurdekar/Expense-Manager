package tableDAO;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "data_input_table")
public class data_input_table {
	

	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getWithdrawal() {
		return withdrawal;
	}

	public void setWithdrawal(String withdrawal) {
		this.withdrawal = withdrawal;
	}

	public String getDeposit() {
		return deposit;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Lob
	@Type(type = "org.hibernate.type.TimestampType")
	private Timestamp date;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String tx_month;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String tx_year;
	
	public String getTx_month() {
		return tx_month;
	}

	public void setTx_month(String tx_month) {
		this.tx_month = tx_month;
	}

	public String getTx_year() {
		return tx_year;
	}

	public void setTx_year(String tx_year) {
		this.tx_year = tx_year;
	}

	public int getSr_num() {
		return sr_num;
	}

	public void setSr_num(int sr_num) {
		this.sr_num = sr_num;
	}

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String mode;
	
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String description;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String deposit;

	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String withdrawal;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String balance;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type = "org.hibernate.type.IntegerType")
	private int sr_num;

	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String category;
	
	
}

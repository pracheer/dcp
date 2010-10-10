package branch.server;
public class TrxnResponse {
	private String serialNum_;
	private String erroMsg_;
	private Double amt_;
	private boolean status_;

	public TrxnResponse(String serialNum_, Double amt_, boolean status_,
			String erroMsg_) {
		super();
		this.serialNum_ = serialNum_;
		this.erroMsg_ = erroMsg_;
		this.amt_ = amt_;
		this.status_ = status_;
	}

	public String toString() {
		return serialNum_ + Trxn.msgSeparator + amt_
				+ Trxn.msgSeparator + status_ + Trxn.msgSeparator
				+ erroMsg_;
	}

	public static TrxnResponse parseString(String str) {		
		String[] subStrings = str.split(Trxn.msgSeparator);
		
		return new TrxnResponse(
				subStrings[0],
				Double.parseDouble(subStrings[1]),
				Boolean.parseBoolean(subStrings[2]),
				subStrings[3]);
	}

	public String getSerialNum_() {
		return serialNum_;
	}

	public String getErroMsg_() {
		return erroMsg_;
	}

	public Double getAmt_() {
		return amt_;
	}

	public boolean isStatus_() {
		return status_;
	}

}

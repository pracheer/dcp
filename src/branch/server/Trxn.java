package branch.server;

public class Trxn {

	public static final String msgSeparator = "::";

	public static enum TransxType {
		DEPOSIT, WITHDRAW, QUERY, TRANSFER, TAKESNAPSHOT
	}

	private String serialNum_;
	private TransxType type_;

	private int sourceBranch_ = -1;
	private String sourceAccount_ = null;

	private int destBranch_ = -1;
	private String destAccount_ = null;

	private Double amount_ = (double) 0;

	public Trxn(String type, String serialNum, String amt, String acNo,
			String srcAcNo, String destAcNo) {
		type_ = TransxType.valueOf(type);
		serialNum_ = serialNum;
		amount_ = Double.parseDouble(amt);

		sourceAccount_ = (type_ == TransxType.TRANSFER) ? srcAcNo : acNo;
		try {
			this.sourceBranch_ = Integer.parseInt(sourceAccount_
					.substring(0, 2));
		} catch (NumberFormatException e) {
			System.err
					.println("Invalid source branch Id. Branch Id has to be an integer");
			System.err.println(e.getMessage());
		}

		if (type_ == TransxType.TRANSFER) {
			destAccount_ = destAcNo;
			try {
				this.destBranch_ = Integer.parseInt(destAccount_
						.substring(0, 2));
			} catch (NumberFormatException e) {
				System.err
						.println("Invalid dest branch Id. Branch Id has to be an integer");
				System.err.println(e.getMessage());
			}
		}
	}

	public Trxn(String serialNo, TransxType type, int sourceBranch,
			String sourceAccount, int destBranch, String destAccount,
			Double amount) {
		super();
		this.serialNum_ = serialNo;
		this.type_ = type;
		try {
			this.sourceBranch_ = Integer
					.parseInt(sourceAccount.substring(0, 2));
		} catch (NumberFormatException e) {
			System.err
					.println("invalid source branch Id. Branch Id has to be an integer");
			System.err.println(e.getMessage());
		}
		this.sourceAccount_ = sourceAccount;

		if (this.type_ == Trxn.TransxType.TRANSFER) {
			this.destBranch_ = destBranch;
			this.destAccount_ = destAccount;
		}

		this.amount_ = amount;
	}

	protected Trxn(String str) {
		String[] subStrings = str.split(msgSeparator);
		type_ = TransxType.valueOf(subStrings[0]);
		serialNum_ = subStrings[1];
		sourceAccount_ = subStrings[2];
		amount_ = Double.parseDouble(subStrings[3]);
		sourceBranch_ = Integer.parseInt(subStrings[4]);
		if (type_ == TransxType.TRANSFER) {
			destBranch_ = Integer.parseInt(subStrings[5]);
			destAccount_ = subStrings[6];
		}
	}

	public String toString() {
		return type_
				+ msgSeparator
				+ serialNum_
				+ msgSeparator
				+ sourceAccount_
				+ msgSeparator
				+ amount_
				+ msgSeparator
				+ sourceBranch_
				+ (type_ == TransxType.TRANSFER ? msgSeparator + destBranch_
						+ msgSeparator + destAccount_ : "");
	}

	public static Trxn parseString(String str) {
		return new Trxn(str);
	}

	public static String getMsgseparator() {
		return msgSeparator;
	}

	public String getSerialNum() {
		return serialNum_;
	}

	public TransxType getType() {
		return type_;
	}

	public int getSourceBranch() {
		return sourceBranch_;
	}

	public String getSourceAccount() {
		return sourceAccount_;
	}

	public int getDestBranch() {
		return destBranch_;
	}

	public String getDestAccount() {
		return destAccount_;
	}

	public Double getAmount() {
		return amount_;
	}

	public void setSerialNum(String serialNum) {
		serialNum_ = serialNum;
	}
}

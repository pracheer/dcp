package branch.server;
import java.util.HashMap;

public class TransactionLog {
	
	public static class TrxnStatus {
		
		private Trxn trxn;
		private ReturnVal returnVal;
		
		TrxnStatus(Trxn transaction, ReturnVal returnVal) {
			this.trxn = transaction;
			this.returnVal = returnVal;
		}
		
		public Trxn getTransaction() {
			return trxn;
		}
		
		public ReturnVal getReturnVal() {
			return returnVal;
		}
	}
	
	private static HashMap<String, TrxnStatus> trxnLog = new HashMap<String, TrxnStatus>();

	public static void addTrxn(ReturnVal returnVal, Trxn transaction) {
		trxnLog.put(returnVal.getSer_number(), new TrxnStatus(transaction, returnVal));
	}
	
	public static boolean containsTrxn(String serialNum) {
		return trxnLog.containsKey(serialNum);
	}
	
	public static TrxnStatus getTrxn(String serialNum) {
		if (containsTrxn(serialNum)) {
			return trxnLog.get(serialNum);
		}
		else 
			return null;
		
	}
}

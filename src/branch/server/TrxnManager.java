package branch.server;

public class TrxnManager {
	private Trxn trxn_;
	private ServerNetworkWrapper networkManager_;
	private BranchServerProp properties_;

	public TrxnManager(Trxn ts, ServerNetworkWrapper nm,
			BranchServerProp properties) {
		trxn_ = ts;
		networkManager_ = nm;
		properties_ = properties;
	}

	/**
	 * Execute the transaction.
	 */
	public void processTransaction() {
		OperationsImpl operations = new OperationsImpl();
		ReturnVal returnVal = null;

		String serialNum = trxn_.getSerialNum();

		String newSerialNum = null;

		if (serialNum != null && !serialNum.isEmpty()
				&& !serialNum.equalsIgnoreCase("0")) {
			newSerialNum = serialNum;
			if (TransactionLog.containsTrxn(serialNum)) {
				TransactionLog.TrxnStatus transactionStatus = TransactionLog
						.getTrxn(serialNum);
				
				ReturnVal prevReturnVal = transactionStatus.getReturnVal();
				
				if (prevReturnVal.getStatus()) {
					
					Trxn transaction = transactionStatus
							.getTransaction();
					
					String sourceAccount = transaction.getSourceAccount();
					
					if (transaction.getType() != Trxn.TransxType.TRANSFER) {
						Double balance = AccountDetailsSingleton
								.getAccountDetails().get(sourceAccount);
						ReturnVal returnVal2 = new ReturnVal(serialNum,
								balance, true);
						networkManager_.sendResponse(returnVal2.toString());
						networkManager_.close();
						return;
					}
				}
			} /*
			 * else { ReturnVal returnVal2 = new ReturnVal(0.0,
			 * "There is no transaction with this serial number", serialNum,
			 * false);
			 * 
			 * networkManager_.sendResponse(returnVal2.toString());
			 * networkManager_.close(); return; }
			 */
		}

		switch (trxn_.getType()) {
		case DEPOSIT:
			returnVal = operations.deposit(trxn_.getSourceAccount(),
					trxn_.getAmount());

			if (newSerialNum == null || newSerialNum.isEmpty()
					|| newSerialNum.equalsIgnoreCase("0"))
				newSerialNum = SerialNumberGenerator
						.getNewSerial(Trxn.TransxType.DEPOSIT);

			break;

		case WITHDRAW:
			returnVal = operations.withdraw(trxn_.getSourceAccount(),
					trxn_.getAmount());

			if (newSerialNum == null || newSerialNum.isEmpty()
					|| newSerialNum.equalsIgnoreCase("0"))
				newSerialNum = SerialNumberGenerator
						.getNewSerial(Trxn.TransxType.WITHDRAW);

			break;

		case QUERY:
			returnVal = operations.query(trxn_.getSourceAccount());

			if (newSerialNum == null || newSerialNum.isEmpty()
					|| newSerialNum.equalsIgnoreCase("0"))
				newSerialNum = SerialNumberGenerator
						.getNewSerial(Trxn.TransxType.QUERY);

			break;

		case TRANSFER:

			if (serialNum != null && !serialNum.isEmpty()) {
				if (TransactionLog.containsTrxn(serialNum)) {
					TransactionLog.TrxnStatus transactionStatus = TransactionLog
							.getTrxn(serialNum);
					returnVal = transactionStatus.getReturnVal();
				}
			}

			Node destinationNode = new Node(false, trxn_.getDestBranch());

			// See if Destination Server is reachable.
			if (trxn_.getDestBranch() != trxn_.getSourceBranch()) {

				Topology topology = properties_.getTopology();

				if (!topology.isReachable(destinationNode.toString())) {
					ReturnVal query = operations.query(trxn_
							.getSourceAccount());

					// TODO Serialnumber??
					returnVal = new ReturnVal(query.getAmt(),
							"Error: Destination Server Not reachable", "-1",
							false);

					networkManager_.sendResponse(returnVal.toString());

					networkManager_.close();

					return;
				}
			}

			if (newSerialNum == null || newSerialNum.isEmpty()
					|| newSerialNum.equalsIgnoreCase("0"))
				newSerialNum = SerialNumberGenerator
						.getNewSerial(Trxn.TransxType.TRANSFER);

			// Withdraw required amount
			if (returnVal == null || !returnVal.getStatus()) {
				returnVal = operations.withdraw(
						trxn_.getSourceAccount(), trxn_
								.getAmount());
			}

			// Deposit the amount to the destination account.
			if (returnVal.getStatus()) {
				Trxn newTransaction = new Trxn(newSerialNum,
						Trxn.TransxType.DEPOSIT, trxn_
								.getDestBranch(),
						trxn_.getDestAccount(), -1, "-1", trxn_
								.getAmount());

				if (trxn_.getSourceBranch() == trxn_
						.getDestBranch()) {

					// do not need to return the value of this transaction
					ReturnVal returnVal2 = operations.deposit(trxn_
							.getSourceAccount(), trxn_.getAmount());

					addTransactionToLog(trxn_, returnVal2);

				} else {
					Message msg = new Message(properties_.getNode(),
							Message.MsgType.REQ, newTransaction, null);

					if (!networkManager_.sendRequest(destinationNode, msg
							.toString())) {
						System.err
								.println("Not able to send message to destination Server.");

					}
				}
			}

		}
		
		returnVal.setSerialNum(newSerialNum);

		addTransactionToLog(trxn_, returnVal);

		if (returnVal == null)
			returnVal = new ReturnVal(-1.0, "Error: Wrong type of transaction",
					"-1", false);

		networkManager_.sendResponse(returnVal.toString());

		networkManager_.close();

		return;
	}

	/**
	 * adds transaction details to the log.
	 * 
	 * @param transaction
	 * @param returnVal
	 */
	private void addTransactionToLog(Trxn transaction,
			ReturnVal returnVal) {
		if (trxn_.getSerialNum() == null) {
			trxn_.setSerialNum(returnVal.getSer_number());
			TransactionLog.addTrxn(returnVal, trxn_);
		}
	}
}

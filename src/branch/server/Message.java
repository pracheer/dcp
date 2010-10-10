package branch.server;

import java.net.Socket;

public class Message {

	public static enum MsgType {
		REQ, RESP,
	}

	Node srcNode_;
	MsgType type_;
	Trxn trxn_ = null;
	TrxnResponse trxnResponse_ = null;
	Socket clientSocket_ = null;

	public static Message parseString(String str, Socket clientSocket) {
		return new Message(str, clientSocket);
	}

	public Message(Node sourceNode, MsgType type, Trxn trxn,
			TrxnResponse trxnResp) {
		super();
		this.srcNode_ = sourceNode;
		this.type_ = type;
		this.trxn_ = trxn;
		this.trxnResponse_ = trxnResp;
	}

	/**
	 * accepts str in the format of TransactionType::sourceName::
	 * 
	 * @param str
	 * @param clientSocket 
	 */
	protected Message(String str, Socket clientSocket) {

		clientSocket_ = clientSocket;
		System.out.println("Received a client request:"+str);

		int index1 = str.indexOf(Trxn.msgSeparator);

		int index2 = str.indexOf(Trxn.msgSeparator, index1
				+ Trxn.msgSeparator.length());

		type_ = MsgType.valueOf(str.substring(0, index1));

		srcNode_ = new Node(str.substring(index1
				+ Trxn.msgSeparator.length(), index2));

		if (type_ == MsgType.REQ)
			trxn_ = Trxn.parseString(str.substring(index2
					+ Trxn.msgSeparator.length()));
		else if (type_ == MsgType.RESP)
			trxnResponse_ = TrxnResponse.parseString(str
					.substring(index1 + Trxn.msgSeparator.length()));
	}

	public String toString() {
		return type_
		+ Trxn.msgSeparator
		+ srcNode_
		+ Trxn.msgSeparator
		+ (type_ == MsgType.REQ ? trxn_.toString()
				: trxnResponse_.toString());
	}

	/**
	 * @return the sourceName_
	 */
	public Node getSrcNode() {
		return srcNode_;
	}

	/**
	 * @return the type_
	 */
	public MsgType getType() {
		return type_;
	}

	/**
	 * @return the transaction_
	 */
	public Trxn getTrxn() {
		return trxn_;
	}

	public TrxnResponse getTrxnResponse() {
		return trxnResponse_;
	}
	
	public Socket getClientSocket() {
		return clientSocket_;
	}
}

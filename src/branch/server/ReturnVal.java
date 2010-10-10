package branch.server;
/*
 * %W% %E% pg298@cornell.edu
 *
 * Copyright (c) 1993-2010 CS5414, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of CS5414, 
 * Inc. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with CS5414.
 *
 * CS5414 MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. CS5414 SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

/**
 * @author pg298@cornell.edu
 * 
 */
public class ReturnVal {

	private String ser_number;
	private Double amt;
	private boolean status = false;
	private String error;

	public ReturnVal(Double amt, String error, String ser_number, boolean status) {
		super();
		this.amt = amt;
		this.error = error;
		this.ser_number = ser_number;
		this.status = status;
	}

	/**
	 * @param ser_number
	 * @param amt
	 * @param status
	 *            TODO
	 */
	public ReturnVal(String ser_number, Double amt, boolean status) {
		super();
		this.ser_number = ser_number;
		this.amt = amt;
		this.status = status;
	}
	
	public ReturnVal(Double amt, boolean status) {
		super();
		this.ser_number = ser_number;
		this.amt = amt;
		this.status = status;
	}

	public ReturnVal(String str) {
		String[] list = str.split(Trxn.msgSeparator);
		ser_number = list[0];
		amt = Double.parseDouble(list[1]);
		error = list[2];
		status = Boolean.parseBoolean(list[3]);
	}

	/**
	 * @return the ser_number
	 */
	public String getSer_number() {
		return ser_number;
	}

	/**
	 * @return the amt
	 */
	public Double getAmt() {
		return amt;
	}

	public boolean getStatus() {
		return status;
	}

	public boolean isError() {
		return (error != null && !error.isEmpty());
	}

	public String getError() {
		return error;
	}

	public void setError(String errorMsg) {
		this.error = errorMsg;
	}

	public String toString() {
		return ser_number + Trxn.msgSeparator + amt
				+ Trxn.msgSeparator + 
				((error != null && !error.isEmpty() && !error.equalsIgnoreCase("null")) ? 
					error : "" )+ Trxn.msgSeparator
				+ status;
	}
	
	public static ReturnVal parseString(String str) {
		return new ReturnVal(str);
	}

	public void setSerialNum(String newSerialNum) {
		ser_number = newSerialNum;	
	}

}

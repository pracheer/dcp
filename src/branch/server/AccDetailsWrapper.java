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

import java.util.HashMap;

/**
 * @author pg298@cornell.edu
 * 
 */
public class AccDetailsWrapper {

	protected AccDetailsWrapper() {
	}

	private static HashMap<String, Double> accountDetails = 
		new HashMap<String, Double>(100);

	public static synchronized Double deposit(String acnt, Double depositAmt) {
		Double finalAmt;
		if (accountDetails.containsKey(acnt)) {
			finalAmt = accountDetails.get(acnt) + depositAmt;
		} else {
			finalAmt = depositAmt;
		}

		accountDetails.put(acnt, finalAmt);

		return finalAmt;
	}

	public static synchronized Double withdraw(String acnt, Double withdrawAmt) {
		
		Double existingAmt = 0.0;
		if (accountDetails.containsKey(acnt)) 
			existingAmt = accountDetails.get(acnt);

		Double finalAmt = existingAmt - withdrawAmt;

		accountDetails.put(acnt, finalAmt);

		return finalAmt;
	}

	public static synchronized Double query(String acnt) {
		if (!accountDetails.containsKey(acnt))
			accountDetails.put(acnt, 0.0);
		
		return accountDetails.get(acnt);
	}

	public static synchronized HashMap<String,Double> getAllAccnts() {
		return accountDetails;
	}
}

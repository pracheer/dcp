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
public interface IOperations {
	
	ReturnVal deposit (String acnt, Double amt);
	
	ReturnVal withdraw (String acnt, Double amt);
	
	ReturnVal query (String acnt);
	
}

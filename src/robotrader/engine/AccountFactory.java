package robotrader.engine;

import robotrader.engine.model.IAccount;

/**
*  AccountFactory   description: facory implementation for IAccount objects,
* creates SimpleAccount using a predefined initialCash as starting balance.
 * <br>
*/
class AccountFactory 
{
	private float _initialCash = 100;

	AccountFactory() {
	}

	/**
	 * Method setInitialCash: stes the initial cash for accounts that will be
	 * created afterwards.
	 * @param balance
	 */
	void setInitialCash(float balance) 
  {
		_initialCash = balance;
	}

	/**
	 * Method getInstance: creates a new account.
	 * @return IAccount
	 */
	IAccount getInstance() 
  {
		return new SimpleAccount(_initialCash);
	}
}
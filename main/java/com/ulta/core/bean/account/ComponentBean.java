package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;

public class ComponentBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 727864200891616884L;



	private String _email;
	
	private String _balancePoints;
	
	public String get_beautyClubNumber() {
		return _beautyClubNumber;
	}

	public void set_beautyClubNumber(String _beautyClubNumber) {
		this._beautyClubNumber = _beautyClubNumber;
	}

	private String _beautyClubNumber;
	
	private String _dateOfBirth;
	
	private String _firstName;
	
	private String favoritesItemCount;

	private String shoppingCartCount;

	public String get_firstName() {
		return _firstName;
	}

	public void set_firstName(String _firstName) {
		this._firstName = _firstName;
	}

	public String get_dateOfBirth() {
		return _dateOfBirth;
	}

	public void set_dateOfBirth(String _dateOfBirth) {
		this._dateOfBirth = _dateOfBirth;
	}

	public String get_balancePoints() {
		return _balancePoints;
	}

	public void set_balancePoints(String _balancePoints) {
		this._balancePoints = _balancePoints;
	}
	public String getFavoritesItemCount() {
		return favoritesItemCount;
	}

	public void setFavoritesItemCount(String favoritesItemCount) {
		this.favoritesItemCount = favoritesItemCount;
	}

	public String getShoppingCartCount() {
		return shoppingCartCount;
	}

	public void setShoppingCartCount(String shoppingCartCount) {
		this.shoppingCartCount = shoppingCartCount;
	}
	
	public String get_email() {
		return _email;
	}

	public void set_email(String _email) {
		this._email = _email;
	}

	public String creditCardType;
	public boolean emailOptIn;

	public String getCreditCardType() {
		return creditCardType;
	}

	public void setCreditCardType(String creditCardType) {
		this.creditCardType = creditCardType;
	}

	public boolean getEmailOptIn() {
		return emailOptIn;
	}

	public void setEmailOptIn(boolean emailOptIn) {
		this.emailOptIn = emailOptIn;
	}
}

package controller;

import model.BreweryFields;
import model.PropertyFields;
import model.ShipFields;

public class LandOnFieldCTRL {
	
	public void propertyField() {
		//ProbertyField
		int propertyValue = (((PropertyFields)fields[newPlayerPosition]).getPropertyValue());
		int[] fieldRent = (((PropertyFields)fields[newPlayerPosition]).getReturnValue());
		int numberOfHouses = fieldRent[6];
		int propertyRent = fieldRent[numberOfHouses];
		if(owner == 0) {
			boolean	answer = view.getUserAnswer(players[playerNumber].getPlayerName() + " er landet på " + fields[newPlayerPosition].getName() + " vil du købe grunden", "ja", "nej");		
			if(answer == true) {
				view.writeText(players[playerNumber].getPlayerName() + " har købt " + fields[newPlayerPosition].getName() + " for " + propertyValue + " kr."); //gui tekst til spilleren
				toolbox.payMoney(playerNumber, owner, players, fields, propertyValue); 				//spiller køber grunden af brættet
				view.updatePlayerAccount(playerNumber, players[playerNumber].getBalance()); 		//Update af gui.
				PropertyFields wantedFieldChange = ((PropertyFields)fields[newPlayerPosition]);
				//Herunder bliver feltets ejer skiftet.
				wantedFieldChange.setOwner(playerNumber);
				view.updateOwnership(playerNumber, newPlayerPosition);
			}
		}
		if(owner != 0 && owner != playerNumber) {
			if ((toolbox.checkPropertyGroupOwnership(owner, fields, newPlayerPosition) == true) && (fieldRent[6] == 0)) {
				propertyRent *= 2;
			}
			view.writeText(players[playerNumber].getPlayerName() + " er landet på " + fields[newPlayerPosition].getName() + " du skal betale " + propertyRent + " til " + players[owner].getPlayerName()); //Gui i tekst til spilleren
			toolbox.payMoney(playerNumber, owner, players, fields, propertyRent);				 //transaction mellem to spiller.
			view.updatePlayerAccount(playerNumber, players[playerNumber].getBalance());			 //update af den aktive spillerens konto
			view.updatePlayerAccount(owner, players[owner].getBalance());						 //Update af den spiller som modtager penge
		}
		//Her lander den aktivespiller på et felt som han selv ejer. 
		if((owner == playerNumber)) {  
			view.writeText(players[playerNumber].getPlayerName() +  " er landet på " + fields[newPlayerPosition].getName() + " du ejer selv denne grund");
		}
s
	}
	
	public void breweryField() {
		int breweryPropertyValue = (((BreweryFields)fields[newPlayerPosition]).getPropertyValue());
		int[] breweryFieldRent = (((BreweryFields)fields[newPlayerPosition]).getReturnValue());
		int numOfOwnedBrewFields = (toolbox.getNumberOfOwnedPropertiesInGroup(newPlayerPosition, fields, owner));

		if(owner == 0) {
			boolean answer = view.getUserAnswer(players[playerNumber].getPlayerName() + " er landet på " + fields[newPlayerPosition].getName() + " vil du købe grunden", "ja", "nej");
			if(answer == true) {
				view.writeText(players[playerNumber].getPlayerName() + " har købt " + fields[newPlayerPosition].getName() + " for " + breweryPropertyValue + " kr");
				toolbox.payMoney(playerNumber, owner, players, fields, breweryPropertyValue);
				view.updatePlayerAccount(playerNumber, players[playerNumber].getBalance());
				view.updateOwnership(playerNumber, newPlayerPosition);
				BreweryFields wantedFieldChange = ((BreweryFields)fields[newPlayerPosition]);
				wantedFieldChange.setOwner(playerNumber);
			}
		}
		if(owner != 0 && owner != playerNumber) {
			int breweryRent = breweryFieldRent[numOfOwnedBrewFields-1];
			view.writeText(players[playerNumber].getPlayerName() + " er landet på " + fields[newPlayerPosition].getName() + " du skal betale " + breweryRent + " til " + players[owner].getPlayerName());
			toolbox.payMoney(playerNumber, owner, players, fields, breweryRent);
			view.updatePlayerAccount(playerNumber, players[playerNumber].getBalance());
			view.updatePlayerAccount(owner, players[owner].getBalance());
		}

		if(owner == playerNumber) {
			view.writeText(players[playerNumber].getPlayerName() + " er landet på " + fields[newPlayerPosition].getName() + " du ejer selv dette bryggeri");
		}
	}
	
	public void taxField() {
		int[] taxValue = (((TaxField)fields[newPlayerPosition]).getReturnValue());
		view.writeText(players[playerNumber].getPlayerName() + " er landet på " + fields[newPlayerPosition].getName() + " du skal betale " + taxValue[0] + " i skat"); // tekst til spilleren
		toolbox.payMoney(playerNumber, owner, players, fields, taxValue[0]); // Transaction som sker på spilleren ud fra hvilket taxfield han lander på
		view.updatePlayerAccount(playerNumber, players[playerNumber].getBalance()); // update af gui.
	}
	
	public void chanceField() {
		view.writeText(players[playerNumber].getPlayerName() + " er landet på 'Prøv lykken', du trækker et chance kort"); //Tekst fra gui 
		chanceCard.draw();   														//ChanceCardCRTL trækker et kort	
		view.showChanceCard(chanceCard.getDescription());							//Teksten fra Chancekortet vises i gui 
		chanceCardRules(playerNumber, newPlayerPosition); //kald af metode som fortæller hvilket slags kort man har trukket.
	}
	
	public void goToJailField() {
		view.writeText(players[playerNumber].getPlayerName() + " er landet på " + fields[newPlayerPosition].getName() + " du skal nu i fængsel"); //tekst til spilleren.
		players[playerNumber].setPosition(10); // spilleren position bliver rykket til felt nr 10
		players[playerNumber].setTurnsInJail(1); // Spilleren sidder i fængsel.
		view.updatePlayerPosition(playerNumber, newPlayerPosition, 10); //update af gui
	}

	/**
	 * shippingFieldRules() - En metode som bestemmer logikken for når man lander på et rederifelt og for hvor mange rederier man har
	 * @param playerNumber - modtager et spillernummer
	 * @param multiplier - Hvis feltet er ejet af en spiller ganges den totale leje med multiplier.
	 */
	public void shippingFieldRules(int playerNumber, int multiplier, int newPlayerPosition) {
		int shippingPropertyValue = (((ShipFields)fields[newPlayerPosition]).getPropertyValue());
		int owner = (((ShipFields)fields[newPlayerPosition]).getOwner());
		int[] fieldRent = (((ShipFields)fields[newPlayerPosition]).getReturnValue());
		int numOfOwnedShipFields = (toolbox.getNumberOfOwnedPropertiesInGroup(newPlayerPosition, fields, owner));

		if(owner == 0) {
			boolean answer = view.getUserAnswer(players[playerNumber].getPlayerName() + " er landet på " + fields[newPlayerPosition].getName() + " vil du købe grunden", "ja", "nej"); //Spiller for mulighed for at købe grunden
			if(answer == true) {
				view.writeText(players[playerNumber].getPlayerName() + " har købt " + fields[newPlayerPosition].getName() + " for " + shippingPropertyValue + " kr" );	//Tekst til gui
				toolbox.payMoney(playerNumber, owner, players, fields, shippingPropertyValue);				//transaktionen forgår mellem spiller og bræt
				view.updatePlayerAccount(playerNumber, players[playerNumber].getBalance());								//Update af spillerens konto i gui
				view.updateOwnership(playerNumber, newPlayerPosition);									//Update af spillerens ejerskab.
				ShipFields wantedFieldChange = ((ShipFields)fields[newPlayerPosition]);
				wantedFieldChange.setOwner(playerNumber);													//Køberen bliver sat til ejer af feltet
			}
		}

		if(owner != 0 && owner != playerNumber) {
			int shipRent = ((fieldRent[numOfOwnedShipFields -1])*multiplier);
			view.writeText(players[playerNumber].getPlayerName() + " er landet på " + fields[newPlayerPosition].getName() + " du skal betale " + shipRent + " til " + players[owner].getPlayerName());
			toolbox.payMoney(playerNumber, owner, players, fields, shipRent);
			view.updatePlayerAccount(playerNumber, players[playerNumber].getBalance());
			view.updatePlayerAccount(owner, players[owner].getBalance());
		}

		if(owner == playerNumber) {
			view.writeText(players[playerNumber].getPlayerName() + " er landet på " + fields[newPlayerPosition].getName() + " du ejer selv dette rederi");
		}

	}

	/**
	 * chanceCardRules - En metode som switcher på hvilken type ChanceCard man har trukket.
	 * @param playerNumber - modtager et playernummer.
	 */

	public void chanceCardRules (int playerNumber, int newPlayerPosition) {
		int chanceCardType = chanceCard.getType();
		int[] chanceCardValueArray = chanceCard.getReturnValue();
		switch (chanceCardType) {

		case 1: // TransactionCard
			int transactionValue = chanceCardValueArray[0];
			if (transactionValue < 0) {
				toolbox.payMoney(playerNumber, 0, players, fields, transactionValue);

			} else {
				players[playerNumber].recieveMoney(transactionValue);
			}
			view.updatePlayerAccount(playerNumber, players[playerNumber].getBalance());
			break;

		case 2: // MoveToCards
			MoveToCardsRules(playerNumber, newPlayerPosition); // logik og viewCTRL-kald ligger i denne metode.
			break;

		case 3: // ReleaseCards
			players[playerNumber].addReleaseCard();
			break;

		case 4: //TaxCards
			int numberofhouses = toolbox.getNumberOfHousesFromPlayer(playerNumber, fields);
			int numberofhotels = toolbox.getNumberOfHotelsFromPlayer(playerNumber, fields);
			int totalSum = (chanceCardValueArray[0] * numberofhouses)+(chanceCardValueArray[1] * numberofhotels);
			players[playerNumber].removeMoney(totalSum);
			view.updatePlayerAccount(playerNumber, players[playerNumber].getBalance());
			break;
		}
	}

	/**
	 * MoveToCardRules - En Metode som switcher på hvilket type MoveToCard man har trukket.
	 * @param playerNumber - modtager et playernummer.
	 */
	public void MoveToCardsRules (int playerNumber, int newPlayerPosition) {
		int[] chanceCardValueArray = chanceCard.getReturnValue();
		int moveToField = chanceCardValueArray[0];

		if(toolbox.CheckForPassingStart(newPlayerPosition, moveToField) == true)
			view.updatePlayerPosition(playerNumber, newPlayerPosition, moveToField);

		switch (chanceCardValueArray[1]){

		case 1:
			// Blot flyttekort til et bestemt felt.
			if((chanceCard.cardNumber == 19) || (chanceCard.cardNumber == 22)) {
				players[playerNumber].setPosition(moveToField);
				view.updatePlayerPosition(playerNumber,newPlayerPosition, moveToField);
				players[playerNumber].setTurnsInJail(1);
			}
			else {
				players[playerNumber].setPosition(moveToField);
				view.updatePlayerPosition(playerNumber, newPlayerPosition, moveToField);
				fieldRulesSwitch(playerNumber, moveToField);
			}
			break;

		case 2: // Et flyttekort, hvor man flytter til det nærmeste felt med redderi.	
			//			
			//			while(fields[fieldNumber]) {
			//				
			//			}

			int[] shippingArray = new int[4];
			// Der oprettes et loop, som smider lokationenerne fra feltnumrene, ind i et array, hvis typen er "1" - som er shippingField.
			for(int i=0 ; i < 39 ; i++) {
				if (fields[i].getType() == 1) {
					shippingArray[i] = i;
				}
			}
			// Herefter kommer der et tjek om hvilket efterfølgende shippingField er nærmest.
			if( newPlayerPosition < shippingArray[0]) {
				players[playerNumber].setPosition(shippingArray[0]);
				view.updatePlayerPosition(playerNumber, newPlayerPosition, shippingArray[0]);
				shippingFieldRules(playerNumber, 2, shippingArray[0]);
			}

			else if( newPlayerPosition > shippingArray[0] && newPlayerPosition < shippingArray[1]) {
				players[playerNumber].setPosition(shippingArray[1]);
				view.updatePlayerPosition(playerNumber, newPlayerPosition, shippingArray[1]);
				shippingFieldRules(playerNumber, 2, shippingArray[1]);
			}

			else if( newPlayerPosition > shippingArray[1] && newPlayerPosition < shippingArray[2]) {
				players[playerNumber].setPosition(shippingArray[2]);
				view.updatePlayerPosition(playerNumber, newPlayerPosition, shippingArray[2]);
				shippingFieldRules(playerNumber, 2, shippingArray[2]);
			}

			else if( newPlayerPosition > shippingArray[2] && newPlayerPosition < shippingArray[3]) {
				players[playerNumber].setPosition(shippingArray[3]);
				view.updatePlayerPosition(playerNumber, newPlayerPosition, shippingArray[3]);
				shippingFieldRules(playerNumber, 2, shippingArray[3]);
			}
			break;

		case 3: // Ryk tre felter tilbage.
			players[playerNumber].setPosition(newPlayerPosition - 3);
			view.updatePlayerPosition(playerNumber, newPlayerPosition, newPlayerPosition - 3);
			fieldRulesSwitch(playerNumber, (newPlayerPosition - 3));
			break;
		}
	}
}



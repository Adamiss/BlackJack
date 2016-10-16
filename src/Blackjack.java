package com.deitel.java.blackjack;

import java.util.ArrayList;
import java.util.Random;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

@WebServise (name= "Blackjack", serviceName= "BlackjackService")
public class Blackjack 
{
	//χρησιμοποιεί το @Resource για να δημιουργήσει ένα WebServiceContext για παρακολούθηση της συνόδου 
	private @Resource WebServiceContext WebServiceContext;
	private MessageContext messagecontext; //χρησιμοποιείται στην παρακολούθηση της συνόδου
	private HttpSession session; //αποθηκεύει ιδιότητες στης συνόδου
	
	//μοιράζει ένα χαρτί
	@WebMethod( operationName = "dealCard" )
	public String dealCard()
	{
		String card = " ";
		
		ArrayList< String > deck = 
				( ArrayList< String > ) session.getAttribute( "deck" );
		
		card = deck.get(0); //λήψη κορυφαίου χαρτιού τράπουλας
		deck.remove(0); //αφαιρεί το κορυφαίο χαρτί της τράπουλας
		
		return card;
		
	}//τέλος της WebMethod dealCard
	
	//ανακατεύει τη τράπουλα
	@WebMethod( operationName = "shuffle" )
	public void shuffle()
	{
		//λαμβάνει το αντικείμενο HttpSession για αποθήκευση της τράπουλας για τον τρέχοντα πελάτη
		messageContext = webServiceContext.getMessageContext();
		session = ( ( HttpServletRequest ) messageContext.get(MessageContext.SERVLET_REQUEST ) ).getsession();
		
		//γεμίζει την τράπουλα
		ArrayList< String > deck = new ArrayList <String > ();
		
		for ( int face =1; face<=13; face++) //διασχίζει τις τιμές
			for (int suit =0; suit<=3; suit++) //διασχίζει τα είδη των χαρτιών
				deck.add(face + " " + suit); //προσθήκη κάθε χαρτιού στην τράπουλα
		
		String tempCard; //κρατά χαρτιά προσωρινά στη διάρκεια της ενναλαγής
		Random randomObject = new Random (); //δημιουργεί τυχαίους αριθμούς
		int index; //δείκτης τυχαίου επιλεγμένου χαρτιού
		
		for( int i =0; i<deck.size(); i++) //ανακάτεμα
		{
			index = randomObject.nextInt (deck.size() -1);
			
			//ενναλαγή χαρτιού στη θέση i με τυχαίο επιλεγμένο χαρτί
			tempCard = deck.get(i);
			deck.set(i,deck.get(index));
			deck.set(index, tempcard);
			
		}//τέλος for
		
		//προσθέτει αυτήν τη τράπουλα στη σύνοδο του χρήστη
		session.setAttribute( "deck", deck);
		
	}//τέλος της WebMethod
	
	//προσδιορίζει τη τιμή ενός μοιράσματος
	@WebMethod ( operationName = " getHandValue" );
	public int getHandValue( @WebParam ( name = "hand") String hand )
	{
		//χωρίζει το μοίρασμα σε χαρτιά
		String[] cards = hand.split("\t");
		int total = 0; //συνολκή τιμή των χαρτιών στο χέρι
		int face; //φιγούρα τρέχοντος χαρτιού
		int aceCount = 0 ; //αριθμός άσσων στο μοίρασμα
		
		for (int i = 0 ; i< cards.length; i++)
		{
			//ανάλυση συμβολοσειράς και λήψη πρώτου int στο String
			face = Integer.parseInt( cards[i].substring(0, cards[i].indexOf(" ")));
			
			switch (face)
			{
			case 1: //αν είναι άσσος, αυξάνει το aceCount
				++aceCount;
				break;
			case 11: //βαλές
			case 12: //βασίλισσα
			case 13: //βασιλιάς
				total += 10;
				break;
			default: //διαφορετικα ,προσθέτει την τιμή 
				total += face;
				break;
			}//τέλος switch
		}//τέλος της for
		
		//υπολογίζει την καλύτερη χρήση των άσσων
		if (aceCount > 0)
		{
			//αν είναι δυνατόν , μετρά τον άσσο σαν 11
			if(total +11 + aceCount -1 <=21)
				total+=11+aceCount-1;
			else //διαφορετικά μετρά όλους τους άσσους σαν 1
				total += aceCount;
		}//τέλος if
		
		return total;
	}//τέλος της WebMethod getHandValue
	
}//τέλος της κλάσης Blackjack

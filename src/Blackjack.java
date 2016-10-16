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
	//������������ �� @Resource ��� �� ������������ ��� WebServiceContext ��� ������������� ��� ������� 
	private @Resource WebServiceContext WebServiceContext;
	private MessageContext messagecontext; //��������������� ���� ������������� ��� �������
	private HttpSession session; //���������� ��������� ���� �������
	
	//�������� ��� �����
	@WebMethod( operationName = "dealCard" )
	public String dealCard()
	{
		String card = " ";
		
		ArrayList< String > deck = 
				( ArrayList< String > ) session.getAttribute( "deck" );
		
		card = deck.get(0); //���� ��������� ������� ���������
		deck.remove(0); //������� �� �������� ����� ��� ���������
		
		return card;
		
	}//����� ��� WebMethod dealCard
	
	//���������� �� ��������
	@WebMethod( operationName = "shuffle" )
	public void shuffle()
	{
		//�������� �� ����������� HttpSession ��� ���������� ��� ��������� ��� ��� �������� ������
		messageContext = webServiceContext.getMessageContext();
		session = ( ( HttpServletRequest ) messageContext.get(MessageContext.SERVLET_REQUEST ) ).getsession();
		
		//������� ��� ��������
		ArrayList< String > deck = new ArrayList <String > ();
		
		for ( int face =1; face<=13; face++) //��������� ��� �����
			for (int suit =0; suit<=3; suit++) //��������� �� ���� ��� �������
				deck.add(face + " " + suit); //�������� ���� ������� ���� ��������
		
		String tempCard; //����� ������ ��������� ��� �������� ��� ���������
		Random randomObject = new Random (); //���������� �������� ��������
		int index; //������� ������� ����������� �������
		
		for( int i =0; i<deck.size(); i++) //���������
		{
			index = randomObject.nextInt (deck.size() -1);
			
			//�������� ������� ��� ���� i �� ������ ���������� �����
			tempCard = deck.get(i);
			deck.set(i,deck.get(index));
			deck.set(index, tempcard);
			
		}//����� for
		
		//��������� ����� �� �������� ��� ������ ��� ������
		session.setAttribute( "deck", deck);
		
	}//����� ��� WebMethod
	
	//������������ �� ���� ���� �����������
	@WebMethod ( operationName = " getHandValue" );
	public int getHandValue( @WebParam ( name = "hand") String hand )
	{
		//������� �� �������� �� ������
		String[] cards = hand.split("\t");
		int total = 0; //������� ���� ��� ������� ��� ����
		int face; //������� ��������� �������
		int aceCount = 0 ; //������� ����� ��� ��������
		
		for (int i = 0 ; i< cards.length; i++)
		{
			//������� ������������� ��� ���� ������ int ��� String
			face = Integer.parseInt( cards[i].substring(0, cards[i].indexOf(" ")));
			
			switch (face)
			{
			case 1: //�� ����� �����, ������� �� aceCount
				++aceCount;
				break;
			case 11: //�����
			case 12: //���������
			case 13: //��������
				total += 10;
				break;
			default: //����������� ,��������� ��� ���� 
				total += face;
				break;
			}//����� switch
		}//����� ��� for
		
		//���������� ��� �������� ����� ��� �����
		if (aceCount > 0)
		{
			//�� ����� ������� , ����� ��� ���� ��� 11
			if(total +11 + aceCount -1 <=21)
				total+=11+aceCount-1;
			else //����������� ����� ����� ���� ������ ��� 1
				total += aceCount;
		}//����� if
		
		return total;
	}//����� ��� WebMethod getHandValue
	
}//����� ��� ������ Blackjack

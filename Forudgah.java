package MASC_FIS_3;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class Forudgah extends Agent {
 	private static final long serialVersionUID = 1L;
	Agent a1 = new Agent();
	//public AID MASC_A_AID;
	Func func = new Func();
	
	int offsettime = 0;

	String adjag1 = "Forudgah_New";
	String adjag2 = "HavaPeyma_22";
	String adjag3 = "";
	String adjag4 = "";
	String adjag5 = "";
	String adjag6 = "";
	
 	public Forudgah(){
 		
	}
 	protected void setup() {
	//MASC_A_AID = new AID("MASC_A", AID.ISLOCALNAME);
 		
 	System.out.println(func.getDateTime()+"   "+"Hello World! My name is "+ this.getLocalName());
 
/////////////////
	addBehaviour(new CyclicBehaviour(this) {
		private static final long serialVersionUID = 1L;
		
		public void action(){  
			ACLMessage msg = receive();
			if(msg != null){
				System.out.println(func.getDateTime()+"   "
						+"Agent ("+myAgent.getLocalName()+") from Agent ("+msg.getSender()+") get : " +msg.getContent());
				//String con = msg.getContent();
				//String content[] = {"F","R","S"}; 
				//for(int i=0;i<=3;i++)
				//	content[i] = msg.getContent()[i];
				if(func.isstrequal(msg.getContent(), "FRS")){
					ACLMessage sendmsg = new ACLMessage(ACLMessage.AGREE);
					//msg.setPerformative(ACLMessage.AGREE);
		        	//msg.clearAllReceiver();
		        	sendmsg.addReceiver(msg.getSender());
		        	//msg.setLanguage("English");
		        	//msg.setOntology("Weather-forecast-ontology");
		        	sendmsg.setContent("fault(1)");
		        	
		        	try{
		        		send(sendmsg);
		                System.out.println(func.getDateTime()+"   "
		                		+"Agent "+myAgent.getLocalName()+"	to Agent "+msg.getSender()+" :		send: "+msg.getContent());
		        	}catch(Exception e){
		        		System.out.println(func.getDateTime()+"   "+"Agent "+myAgent.getLocalName()+" : can't send");    		
		        	}
		         } 
				if(func.isstrequal(msg.getContent(), "fault(1)")){
				}
			 }
			else{
				try{
				Thread.sleep(5);
				}
				catch(InterruptedException ie){
					ie.printStackTrace();
				}
			}
		}
	});
////////////////////
    // Add the TickerBehaviour (period 20 sec)
    addBehaviour(new TickerBehaviour(this, func.ticker+offsettime) {
 	private static final long serialVersionUID = 1L;
	protected void onTick() {
		String receiveragent = adjag1;
    	ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
    	msg.addReceiver(new AID(receiveragent,AID.ISLOCALNAME));
    	//msg.setLanguage("English");
    	//msg.setOntology("Weather-forecast-ontology");
    	msg.setContent("FRS");//Fault Req State
    	
    	try{
    		send(msg);
            System.out.println(func.getDateTime()+"   "
            		+"Agent "+myAgent.getLocalName()+"	to Agent "+receiveragent+" :		send: "+msg.getContent());
    	}catch(Exception e){
    		System.out.println(func.getDateTime()+"		"
    				+"Agent "+myAgent.getLocalName()+"	to Agent "+receiveragent+" :		can't send");    		
    	}
      } 
    });
    
/////////////////
    // Add the TickerBehaviour (period 20 sec)
    addBehaviour(new TickerBehaviour(this, func.ticker+offsettime) {
 	private static final long serialVersionUID = 1L;
	protected void onTick() {
		String receiveragent = adjag2;
    	ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
    	msg.addReceiver(new AID(receiveragent,AID.ISLOCALNAME));
    	//msg.setLanguage("English");
    	//msg.setOntology("Weather-forecast-ontology");
    	msg.setContent("FRS");//Fault Req State
    	
    	try{
    		send(msg);
            System.out.println(func.getDateTime()+"   "
            		+"Agent "+myAgent.getLocalName()+"	to Agent "+receiveragent+" :		send: "+msg.getContent());
    	}catch(Exception e){
    		System.out.println(func.getDateTime()+"		"
    				+"Agent "+myAgent.getLocalName()+"	to Agent "+receiveragent+" :		can't send");    		
    	}
      } 
    });
    
/////////////////
/////////////////


  	} 
}


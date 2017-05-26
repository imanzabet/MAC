package MASC_FIS_3;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class Standard_Parvaz extends Agent {
 	private static final long serialVersionUID = 1L;
	Agent a1 = new Agent();
	public AID MASC_H_AID;
	Func func = new Func();

	
	int offsettime = 600;

	String adjag1 = "Sukht_New";
	String adjag2 = "";
	String adjag3 = "";
	String adjag4 = "";
	String adjag5 = "";
	String adjag6 = "";
	
	public Standard_Parvaz(){
 		
	}
 	protected void setup() {
	MASC_H_AID = new AID("MASC_H", AID.ISLOCALNAME);
 		
 	System.out.println(func.getDateTime()+"   "+"Hello World! My name is "+ this.getLocalName());
 
/////////////////
	addBehaviour(new CyclicBehaviour(this) {
		private static final long serialVersionUID = 1L;
		
		public void action(){  
			ACLMessage msg = receive();
			if(msg != null){
				System.out.println(func.getDateTime()+"   "+"Agent "+myAgent.getLocalName()+ " get : " +msg.getContent());
				//String content = "Fault Req State";
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
		                System.out.println(func.getDateTime()+"   "+"Agent "+myAgent.getLocalName()+" : send : "+msg.getContent());
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
  	} 
}


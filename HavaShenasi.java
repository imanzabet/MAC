package MASC_FIS_3;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class HavaShenasi extends Agent {
 	private static final long serialVersionUID = 1L;
	Agent a1 = new Agent();
	public AID MASC_E_AID;
	Func func = new Func();

	
	int offsettime = 300;

	String adjag1 = "HavaPeyma_2";
	String adjag2 = "Sukht_New";
	String adjag3 = "Amuzeshgah";
	String adjag4 = "ZaminShenasi";
	String adjag5 = "";
	String adjag6 = "";
	
	public HavaShenasi(){
 		
	}
 	protected void setup() {
	MASC_E_AID = new AID("MASC_E", AID.ISLOCALNAME);
 		
 	System.out.println(func.getDateTime()+"   "+"Hello World! My name is "+ this.getLocalName());
 
/////////////////
	addBehaviour(new CyclicBehaviour(this) {
		private static final long serialVersionUID = 1L;
		
		public void action(){  
			ACLMessage msg = receive();
			if(msg != null){
				System.out.println(func.getDateTime()+"   "+"Agent "+myAgent.getLocalName()+ " get : " +msg.getContent());
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
    addBehaviour(new TickerBehaviour(this, 20300) {
 	private static final long serialVersionUID = 1L;
	protected void onTick() {
    	ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
    	msg.addReceiver(new AID("HavaPeyma_2",AID.ISLOCALNAME));
    	//msg.setLanguage("English");
    	//msg.setOntology("Weather-forecast-ontology");
    	msg.setContent("FRS");//Fault Req State
    	
    	try{
    		send(msg);
            System.out.println(func.getDateTime()+"   "+"Agent "+myAgent.getLocalName()+" : send : "+msg.getContent());
    	}catch(Exception e){
    		System.out.println(func.getDateTime()+"   "+"Agent "+myAgent.getLocalName()+" : can't send");    		
    	}
      } 
    });
    
/////////////////
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
    // Add the TickerBehaviour (period 20 sec)
    addBehaviour(new TickerBehaviour(this, func.ticker+offsettime) {
 	private static final long serialVersionUID = 1L;
	protected void onTick() {
		String receiveragent = adjag3;
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
		String receiveragent = adjag4;
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
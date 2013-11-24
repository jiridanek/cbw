/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.PNEngineTool;
import fri.cbw.GenericTool.AbstractEngineTool;
import org.openide.util.lookup.ServiceProvider;
import java.util.HashMap;
/**
 *
 * @author Mark Rolih
 */


/**
 *
 * @author Mark Rolih - reachable at rolih8@gmail.com for further questions
 */

//                                  Petri Nets
/* Step 1
* First we define DM_matrix, which takes data from model structure of the 
* program and combines transitions and places into a matrix which has 
* m rows = number of transitions, n colums = number of places in the Petri Net.
* For each position in D-matrix a pair (m,n)=1 if transiton m has input 
* from place n. Else (m,n)=0.
*/

/* Step 2
* Next we define DP_matrix, which has the same dimensions as previous
* DM_matrix. A pair (m,n)=1 if transition m has output from place n in Petri Net. 
* Else (m,n)=0.
*/

/* Step 3
* We find the D_matrix (composite change matrix). It is result of subtraction 
* between DM_matrix and DP_matrix.
*/

/* Step 4
* We construct 1 x m matrix transitions that represents firing (action) in Petri Nets. 
* Each postion (1,j) we place the number of times transition j is to fire.
* Moved to constants; made change to k x m matrix, where k is number of firing, each 
* transitions[k] represent one firing with specific transitions. 
*/

/* Step 5
* A 1 x n matrix marking to represent the current marking of the Petri Net. Each 
* position [1,j], we place the number of tokens in position j.
*/

/* Step 6
* To determine the marking of the Petri Net after the transtion(s) in the 
* firing matrix we compute:
* transitions*D+marking=NextMarking
*/

/*                                  Notes:
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * DM_matrix - matrix of input transition connections
 * DP_matrix - matrix of output transition connections
 * transitions - matrix of vectors that defines one firing at a time
 * Example: t3 - (0,0,1,0), t2 - (0,1,0,0), t4 - (0,0,0,1)
 * marking - defines tokens 
 * -----------------------------------------------------------------------------------
 * Petri Nets define set of connections, places and transitions. Place can be 
 * connected to transition and transtion to place - other connection are not 
 * allowed. Places contatin from 0 to n tokens that are consumed by firings of 
 * transitions, each transition consumes/produces as many tokens as it is specified 
 * on connection that goes into/out of transition. Usually represented in matrix 
 * form.
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * DM_red_matrix and DP_red_matrix are same as DM_matrix and DP_matrix only 
 * that are used for second color ("red") as well as red_marking is simmilar 
 * to marking - tokens. At least one -> marking or red_marking must be defined.
 * -----------------------------------------------------------------------------------
 * ColouredPN - we have black and red connections and black and red tokens. At 
 * each firing (certain transition) we fire both places with single transition. 
 * Black tokens go through black connections, red tokens through red connections. 
 * Result of firing is token of colour that is specified by output connection of 
 * certain transition.
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * HashMap inhibitors (index of transtion, index of place). Such connection must not 
 * appear in DM_matrix.
 * -----------------------------------------------------------------------------------
 * Inhibitors define inhibitors connections for certain transition from some 
 * place. When in such place token is present (one or more), inhibited connection 
 * stops firing of transition. When tokens are released and place is empty firing is 
 * again possible.
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * TransitionTime - vector of values - how much time firing of transitions 
 * (t1,t2,t3,...) in miliseconds takes. Result is displayed at the end of execution 
 * of program.
 * timeConstraint - defines maximum duration of all firings.
 * -----------------------------------------------------------------------------------
 * TimedPN appends transitions time of execution so we can mesure duration of firings. 
 * At end of execution all time till now is displayed with time constraint.
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * reachable_marking - expected tokens for marking - active, {} inactive
 * red_reachable_marking - expected tokens for red_marking - active, {} inactive
 * depth - depth of tree
 * -----------------------------------------------------------------------------------
 * Reachability in PN -> we define vector of tokes we would like to reach from start 
 * state defined in marking. Program then with recursion searches for such state, 
 * if it is found we have solution. Similar for red_marking. Depth defines depth 
 * of state generator tree.
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
*/

@ServiceProvider(service = AbstractEngineTool.class)
public class PNEngine extends AbstractEngineTool{

    //constants -> they get data from module
    //currently filled with data from 
    //http://www.techfak.uni-bielefeld.de/~mchen/BioPNML/Intro/MRPN.html
    
    //matrix of inputs specified at Step 1 -> t1 inputs place p5, t2 inputs 
    //place p1, ... which are not inhibited
    private static int[][] DM_matrix={{0,0,0,0,1},
                                        {1,0,0,0,0},
                                        {0,1,0,0,0},
                                        {0,0,1,1,0}};
    
    //matrix of outputs specified at Step 2 -> t1 outputs p1,p2, t2 outputs 
    //p3,p4, ...
    private static int[][] DP_matrix={{1,1,0,0,0},
                                        {0,0,1,1,0},
                                        {0,0,0,1,0},
                                        {0,0,0,0,1}};
    
    //matrix similar to DM_matrix only that it has inputs for t1,...,tn where 
    //connections are red
    private static int[][] DM_red_matrix={{0,0,0,0,0},
                                            {0,1,0,0,0},
                                            {0,0,0,0,0},
                                            {0,0,0,0,0}};
    
    //matrix similar to DP_matrix only that it has outputs for t1,...,tn 
    //where connections are red
    private static int[][] DP_red_matrix={{0,0,0,0,0},
                                            {0,0,0,0,0},
                                            {0,0,0,0,0},
                                            {0,0,0,0,0}};
    
    //matrix of transitions to fire specified at Step 4, only one position 
    //at each vector transitions[z] can be active at once -> We fist fire 
    //t2, then t3, then t2, ...
    private static int[][] transitions={{0,1,0,0},
                                        {0,0,1,0},
                                        {0,1,0,0},
                                        {0,0,0,1},
                                        {1,0,0,0},
                                        {0,0,0,1},
                                        {1,0,0,0},
                                        {0,1,0,0},
                                        {0,0,1,0}};
    
    //matrix of tokens specified at Step 5 -> p1 has 2 tokens, p2 has 1 
    //token, ... - at beginning which also defines reachability - if it is {} 
    //reachability will not search reachable_marking
    private static int[] marking={2,1,0,0,0};
    
    //matrix of tokens similar as marking only it contains red tokens which
    //also defines reachability - value {} reachability will not search 
    //red_reachable_marking
    private static int[] red_marking={0,5,0,0,0};
    
    //two vectors that defines wanted markings if we choose to look if such 
    //state is reachable (one for black, other for red tokens) and marking 
    //and red_marking must be defined respectively
    private static int[] reachable_marking={};//{1,1,1,3,0};
    private static int[] red_reachable_marking={};//{0,2,0,0,0};
    
    //depth of recursion for reachability
    public static int depth=15;
    

    //HashMap of inhibitor connections - constant, acts as skip firing 
    //symbol - consist of pairs (transitionID,placeID) where input into 
    //certain transition is inhibited by certain place -> p2 inhibits t3,
    //p5 inhibits t1, ... DM_matrix does not contain inputs that are 
    //inhibited because the connection can only be of one type
    private static HashMap<Integer, Integer> inhibitors=new HashMap<Integer, Integer>(){{
        //for test we must change DM_matrix for t3 at p2 on 0
        //put(3,2);
        //for test we must change DM_matrix for t1 at p5 on 0
        //put(1,5);
        //for test we must change DM_matrix for t4 at p4 on 0
        //put(4,4);
    }};
    
    //time for each transition defined as vector=(t1,t2,...), 
    //where t1 is time for transtition t1 in ms
    private static int[] transitionTime={1000, 1000, 1000, 1000};
    
    //time constraint in which firings must occur
    private static int timeConstraint=10000;
    
    //sum of time that displays duration of accumulated transition after 
    //each firing - inicialization
    private static int sumTime=0;
    
    //boolean needed for recursion not to return at first loop
    private static boolean border=false;
    
    //matrix automaticlly filled later
    private static int[][] D_matrix;
    private static int[][] D_red_matrix;
    
    //boolean needed to exit recursion
    private static boolean end=false;
    
    @Override
    public void calculate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName() {
        return "Petri nets engine tool";
    }

    @Override
    public String getAuthor() {
        return "Mark Rolih";
    }

    @Override
    public Class getTopComponentClass() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
     
    
    public void calculateData(){
        
        //general validation of necessary elements
        if(marking.length==0){
            reachable_marking=new int[]{};
        }
        if(red_marking.length==0){
            red_reachable_marking=new int[]{};
        }
        if(marking.length==0 && red_marking.length==0){
            System.out.println("No tokens defined");
            return;
        }
       
        //construction D matrix which is DM_matrix-DP_matrix
        D_matrix=constructDMatrix(DM_matrix, DP_matrix);
        
        //the same for red inputs and outputs
        D_red_matrix=constructDMatrix(DM_red_matrix, DP_red_matrix);
        
        //actual firing and cheching if user wants to determine 
        //reachability or not
        if (reachable_marking.length>0 || red_reachable_marking.length>0){
            
            //initialization of required temporary matrix for one transition 
            //firing at time - short vector (1,0,0,0,...)
            int[] temp=new int[DM_matrix.length];
            
            for (int p=0; p<DM_matrix.length; p++){
                if (p==0){
                    temp[p]=1;
                }
                else {
                    temp[p]=0;
                }
            }
            
            //transition is 2D because it can contain more than one vector
            int[][] transition={temp};   
            
            //call recursion - rechability
            canIReachYou(transition, 1, marking, red_marking, 1, sumTime);
            System.out.println("Recursion finished searching solution. Set depth++ if it doesn't return solution.");
        }
        else {
            //if we do not want to search reachability - normal petri nets/coloured/inhibited/timed PN
            int[][] S=triggerCertainTransitionsOrValidReachability(transitions, marking, red_marking, sumTime);
            
            //print of result
            System.out.println();
            for (int z=0; z<S[0].length; z++){
                System.out.print(S[0][z]);
            }
            System.out.println();
            for (int z=0; z<S[1].length; z++){
                System.out.print(S[1][z]);
            }
            System.out.println();
        }          
    }

    private static void canIReachYou(int[][] transition, int maxTransition, int[] previousMarking, int[] red_previousMarking, int D, int time){
        //method which in recursion generates all possible tuples of transitions
        //that lead to reachable_marking or red_reachable_marking
        
        //we must choose if we search results for one colour of two colours
        boolean decide=false;
        if(previousMarking.length>0 && red_previousMarking.length>0){
            decide=(!validateIfFiringEverHappened(previousMarking, reachable_marking) && !validateIfFiringEverHappened(red_previousMarking, red_reachable_marking));
        }
        else if (previousMarking.length>0) {
            decide=(!validateIfFiringEverHappened(previousMarking, reachable_marking));
        }
        else if (red_previousMarking.length>0){
            decide=(!validateIfFiringEverHappened(red_previousMarking, red_reachable_marking));
        }
        
        //final exit term - if we found result
        if (decide){
            System.out.println("-----------");
            System.out.println("The reachability IS confirmed.");
            System.out.println("-----------");
            
            for (int e=0; e<previousMarking.length; e++){
                System.out.print(previousMarking[e]+" ");
            }
            System.out.println();
            
            for (int e=0; e<red_previousMarking.length; e++){
                System.out.print(red_previousMarking[e]+" ");
            }
            System.out.println();
            
            end=true;
            return;
        }
        
        //exit term - depth or non-existant transition 
        if (maxTransition>DM_matrix.length || D>depth){
            return;
        }
        
        int[][] R=new int[2][previousMarking.length];
        
        //we make firing if possible for selected arguments
        int[][] temp=triggerCertainTransitionsOrValidReachability(transition, previousMarking, red_previousMarking, time);
        
        //option based on one/two colours
        boolean option=false;
        if(previousMarking.length>0 && red_previousMarking.length>0){
            option=(validateIfFiringEverHappened(temp[0], previousMarking) || validateIfFiringEverHappened(temp[1], red_previousMarking)|| border==false);
        }
        else if (previousMarking.length>0) {
            option=(validateIfFiringEverHappened(temp[0], previousMarking) || border==false);
        }
        else if (red_previousMarking.length>0){
            option=(validateIfFiringEverHappened(temp[1], red_previousMarking)|| border==false);
        }
        
        //we validate if firing was possible, if not we return, else we continue
        if (option){
            
            border=true;
            R[0]=(int[])temp[0].clone();
            R[1]=(int[])temp[1].clone();
            
            int[][] transitionCopy=new int[1][transition[0].length];
            transitionCopy[0]=(int[])transition[0].clone();
            
            //change of selected transition (example: from (1,0,0,0) to (0,1,0,0) etc.
            for (int v=0; v<DM_matrix.length; v++){
                for (int h=0; h<transitionCopy[0].length; h++){
                    if (h==v){
                        transitionCopy[0][h]=1;
                    }
                    else{
                        transitionCopy[0][h]=0;
                    }
                }
                //recursive step and exit condition
                if (end){
                    return;
                }
                canIReachYou(transitionCopy, v+1, R[0], R[1], D+1, time+transitionTime[v]);
            }
        }
    }
    
    private static int[][] triggerCertainTransitionsOrValidReachability(int[][] transitions, int[] marking, int[] red_marking, int time){
        //we make actual firing based on one or two colors for k firings
        
        int[] NextMarking=marking;
        int[] RedNextMarking=red_marking;
        
        for(int k=0; k<transitions.length; k++){
            
            //both colors
            if (marking.length>0 && red_marking.length>0){
                
                //we make firing if it was possible
                int[] NextMarkingNew=makeAction(inhibitors ,D_matrix , transitions[k], NextMarking);
                
                //again we check if it was possible as we need actual value - in 
                //case that was possible we save new token value, else we forget
                boolean possible=comparationOfNextState(NextMarkingNew, NextMarking);

                boolean happened=false;       
                
                //firing as above just here for other colour
                int[] RedNextMarkingNew=makeAction(inhibitors ,D_red_matrix , transitions[k], RedNextMarking);
                
                boolean red_possible=comparationOfNextState(RedNextMarkingNew, RedNextMarking);
                
                boolean red_happened=false;
                
                //if both colours possible we save new values
                //values happened determine to count time or not - if firing
                //possible or not
                if (possible && red_possible){
                    
                    NextMarkingNew=vadlidateIfFiringPossible(NextMarkingNew, NextMarking);
                
                    happened=validateIfFiringEverHappened(NextMarking, NextMarkingNew);
                    
                    RedNextMarkingNew=vadlidateIfFiringPossible(RedNextMarkingNew, RedNextMarking);
                
                    red_happened=validateIfFiringEverHappened(RedNextMarking, RedNextMarkingNew);
                    
                    NextMarking=NextMarkingNew;
                    
                    RedNextMarking=RedNextMarkingNew;
                }
                else {
                    //if impossible we do not want to increase time variable
                    continue;
                }
                
                //increasing time variable for certain color
                if (happened){
                    time=printTimeDurationAndConstraint(happened, transitions[k], time);
                }
                else if (red_happened){
                    time=printTimeDurationAndConstraint(red_happened, transitions[k], time);
                }
                
            }
            else if (marking.length>0){
                //if we are firing only one colour PN - black
                
                int[] NextMarkingNew=makeAction(inhibitors ,D_matrix , transitions[k], NextMarking);
                
                NextMarkingNew=vadlidateIfFiringPossible(NextMarkingNew, NextMarking);

                boolean happened=validateIfFiringEverHappened(NextMarking, NextMarkingNew);

                NextMarking=NextMarkingNew;

                time=printTimeDurationAndConstraint(happened, transitions[k], time);
                
            }
            else if (red_marking.length>0){
                //if we are firing only one colour PN - red
                
                int[] NextMarkingNew=makeAction(inhibitors ,D_red_matrix , transitions[k], RedNextMarking);
                
                NextMarkingNew=vadlidateIfFiringPossible(NextMarkingNew, RedNextMarking);

                boolean happened=validateIfFiringEverHappened(RedNextMarking, NextMarkingNew);

                RedNextMarking=NextMarkingNew;

                time=printTimeDurationAndConstraint(happened, transitions[k], time);
            }
            else{
                //if something wrong
                System.out.println("Wrong input data.");
                System.exit(1);
            }
        }
        
        //return value of tokens needed by some functions
        int[][] B={NextMarking, RedNextMarking};
        return B;
    }
    
    private static int[][] constructDMatrix(int[][] DM_matrix, int[][] DP_matrix){
        //basic subtracion of DM-DP matrix to create D matrix - for 
        //calculating next states of tokens
        
        int D_matrix[][]= new int[DM_matrix.length][DM_matrix[0].length];
        
        for (int m=0; m<DM_matrix.length; m++){
            for (int n=0; n<DM_matrix[0].length; n++){
                
                D_matrix[m][n]=DP_matrix[m][n]-DM_matrix[m][n];
            }
        }
        return D_matrix;
    }

    private static int[] makeAction(HashMap<Integer, Integer> inhibitors,int[][] D_matrix, int[] transitions, int[] marking){
        //we calcualte next tokens values
        
        int NextMarking[]= new int[D_matrix[0].length];
        for (int i=0; i<NextMarking.length; i++){
            NextMarking[i]=0;
        }
        
        for (int n=0; n<D_matrix[0].length; n++){
            for (int m=0; m<D_matrix.length; m++){
                
                //looking for inhibitors 
                if (!inhibitors.isEmpty() && inhibitors.containsKey(m+1) && transitions[m]>0 && marking[inhibitors.get(m+1)-1]>0){
                    NextMarking[n]+=0*D_matrix[m][n];
                }
                else {
                    //no inhibitors
                    NextMarking[n]+=transitions[m]*D_matrix[m][n];
                }
            }
        }        
        return NextMarking;
    }
    
    private static int[] vadlidateIfFiringPossible(int[] NextMarking, int[] marking){
        //validation if firing is possible, if not we renew old state
        
        boolean lean=comparationOfNextState(NextMarking, marking);
        
        for(int i=0; i<NextMarking.length; i++){
            if (lean==false){
                NextMarking[i]=marking[i];
            }
            else {
                NextMarking[i]+=marking[i];
            }
        }
        return NextMarking;
    }
    
    private static boolean comparationOfNextState(int[] NextMarking, int[] marking){
        //validating of next state - we return true if possible to fire 
        //to that state - else false
        
        boolean lean=true;
        
        for(int i=0; i<NextMarking.length; i++){
            if (NextMarking[i]+marking[i]<0){
                lean=false;
                break;
            }
        }
        return lean;
    }
    
    private static boolean validateIfFiringEverHappened(int[] NextMarking, int[] marking){
        //validating if firing ever happened
        //useful for duration of firings
        
        boolean lean=false;
        if (transitionTime.length>0){
            for (int i=0; i<NextMarking.length; i++){
                if (NextMarking[i]!=marking[i]){
                    lean=true;
                    break;
                }
            }
        }
        return lean;
    }
    
    private static int printTimeDurationAndConstraint(boolean lean, int[] transitions, int sumTime){
        //we print duration and constraint of our firing sequence
        
        if (lean==true){
            for (int i=0; i<transitions.length; i++){
                sumTime+=transitions[i]*transitionTime[i];
            }
        }

        System.out.println("Duration of all firings until now: "+sumTime+" ms");
        if (sumTime<=timeConstraint){
            System.out.println("Still in time constraint.");
        }
        else{
            System.out.println("Out of time constraint.");
        }  
        return sumTime;
    }
    
    //set the inital data
    public void setDM_matrix(int[][] DM_matrix){
        this.DM_matrix=DM_matrix;
    }
    
    public void setDP_matrix(int[][] DP_matrix){
        this.DP_matrix=DP_matrix;
    }
    
    public void setDM_red_matrix(int[][] DM_red_matrix){
        this.DM_red_matrix=DM_red_matrix;
    }
    
    public void setDP_red_matrix(int[][] DP_red_matrix){
        this.DP_red_matrix=DP_red_matrix;
    }
     
    public void setTransitions(int[][] transitions){
        this.transitions=transitions;
    }
    
    public void setMarking(int[] marking){
        this.marking=marking;
    }
    
    public void setRed_marking(int[] red_marking){
        this.red_marking=red_marking;
    }
     
    public void setReachable_marking(int[] reachable_marking){
        this.reachable_marking=reachable_marking;
    }
     
    public void setRed_reachable_marking(int[] red_reachable_marking){
        this.red_reachable_marking=red_reachable_marking;
    }
     
    public void setDepth(int depth){
        this.depth=depth;
    }
     
    public void setInhibitors(HashMap<Integer, Integer> inhibitors){
        this.inhibitors=inhibitors;
    }
     
    public void setTransitionTime(int[] transitionTime){
        this.transitionTime=transitionTime;
    }
     
    public void setTimeConstraint(int timeConstraint){
        this.timeConstraint=timeConstraint;
    }
}
    

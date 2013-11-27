/* BoolNetLibtaty.java
 * Avtorji: Ziga Elsner, Mirko Mijuskovic in Tadej Jagodnik
 */
package fri.cbw.BooleanModul;

import java.util.Vector;

/* Razred BoolNetLibrary
 * Funkcije:
 * getSetOfAllElements
 * getBoolTransferFunctions
 * getTruthTableFromBooleanFunctions
 * getTransitions
 * getAllSInputs
 * getTruthTable
 */

public class BoolNetLibrary {
    
   /* Funkcija getSetOfAllElements:
    * Zgenerira mnozico vseh elementov, ki se pojavijo v povezanem grafu.
    * Vhod: Vse povezave grafa (aktivator/represor)
    * Izhod: Vektor elementov
    */
    public static Vector<String> getSetOfAllElements(Vector<String> lines){
        Vector<String> s = new Vector<String>();
        String[] tmp;
        
        // Gremo čez vse vrstice v datoteki,
        // vzamemo prvi in drugi znak in 
        // pogledamo ali sta ze vsebovana v mnozici s,
        // ce nista ju dodamo.
        for(int i = 0; i < lines.size(); i++){
            tmp = lines.get(i).split(",");
            String p = tmp[0];
            String d = tmp[1];
            if(!s.contains(p)){
                s.add(p);
            }
            if(!s.contains(d)){
                s.add(d);
            }
        }
        return s;
    } 
    
    /* Funkcija getBoolTransferFunctions: 
     * Vrne vektor vseh boolovih funkcij, ki so zgenerirane iz grafa.
     * Funkcija deluje tako, da za vsako vozlišče pregleda vse vhodne povezave
     * in kakšnega tipa je ta povezava, in iz tega naredi boolovo funkcijo.
     * Vhod: Vektor/(vrstice v datoteki) povezav.
     * Izhod: Vektor boolovih funkcij.
     */
    public static Vector<String> getBoolTransferFunctions(Vector<String> lines){
        Vector<String> functions = new Vector<String>();
        Vector<String> elements = getSetOfAllElements(lines);
        
        // Gremo cez vsa vozlisca
        for(int i = 0; i < elements.size(); i++){
            Vector<String> inpts = new Vector<String>();
            
            // Gremo cez vse povezave
            for(int j = 0; j < lines.size(); j++){
                
                // Pogledamo vse povezave, ki gredo v elements[i].
                // Ce gredo jih dodamo v vektor inpts
                String[] tmp = lines.get(j).split(",");
                if(tmp[0].compareTo(elements.get(i)) == 0){
                    //System.out.println("Lines" + lines.get(j));
                    inpts.add(lines.get(j));
                }
            }
            
            // Za vsak element sestavimo boolovo funkcijo, ki je oblike
            // npr.: x = !y & z
            int n = inpts.size();
            String f_s = elements.get(i) + "=";
            for(int k = 0; k < inpts.size(); k++){
                String[] tmp2 = inpts.get(k).split(",");
                if(tmp2[0].compareTo(elements.get(i)) == 0){
                    if(tmp2[2].compareTo("0") == 0){
                        f_s = f_s + "!" + tmp2[1];
                    }
                    if(tmp2[2].compareTo("1") == 0){
                        f_s = f_s + tmp2[1];
                    }
                    if(n > 1){
                        f_s = f_s + "&";
                        n = n - 1;
                    }
                }
            }
            
            // Ce inpts ni prazen dodamo funkcijo v vektor functions,
            // sicer pa dodamo niz element=element (trik)
            if(n != 0){
                functions.add(f_s);
            }else{
                functions.add(elements.get(i) + "=" + elements.get(i));
            }
        }
        return functions;
    }
    
    /* Funkcija getTruthTableFromBooleanFunctions:
     * Funkcija vzame vektor boolovih funkcij in izracuna njihove rezultate za
     * vse kombinacije pravilnostne tabele.
     * Vhod: Vektor (vozlišč) elementov, vektor boolovih funkcij.
     * Izhod: Vektor rezultatov funkcij
     */
    public static Vector<String> getTruthTableFromBooleanFunctions(Vector<String> elements, Vector<String> functions){
        Vector<String> truthTableOutput = new Vector<String>();
        
        // Zgeneriramo vse kombinacije pravilnostne tabele,
        // ki je dolga toliko kot je stevilo vozlisc (elementov)
        Vector<String> truth = getTruthTable(elements.size());
        truthTableOutput.add(elements.toString());
        
        //System.out.println(elements);
        
        // Gremo cez vsako vrstico pravilnostne tabele
        for(int i=0;i<truth.size();i++) {
            
            //Vektor predstavlja eno tranzicijo
            Vector<String> next = new Vector<String>();
            
            // Za vsako vrstico vzamemo vse funkcije in izracunamo rezultat 
            for(int j=0;j<functions.size();j++) {
                String[] tmp = functions.get(j).split("=");
                String output = tmp[0];
                String rule = tmp[1];
                System.out.println("output" + output);
                System.out.println("rule" + rule);
                
                // Vsako funkcijo razbijemo po AND-ih
                String[] tmp2 = rule.split("&");             
                Vector<String> result = new Vector<String>();
                
                // Gremo cez vse spremenljivke enacbe (desni del funkcije)
                for(int k=0;k<tmp2.length;k++) {
                    
                    // Preverimo ce je pred vsako spremenljivko !, 
                    // kar pomeni, da gre za negacijo
                    if((tmp2[k].charAt(0)+"").compareTo("!") == 0) {
                        int indexOfEle = elements.indexOf(tmp2[k].charAt(1)+"");
                        //System.out.println(indexOfEle);
                        //System.out.println(tmp2[1]);
                        
                        String value = truth.get(i).charAt(indexOfEle)+"";
                        
                        // Negacija
                        //System.out.println("negacija");
                        //System.out.println("negacija-pred" + value);
                        if(value.compareTo("0") == 0) {
                            value = "1";
                        } else {
                            value = "0";
                        }
                        //System.out.println("negacija-po" + value);
                        result.add(value);
                    }else{
                        //System.out.println("and");
                        int indexOfEle = elements.indexOf(tmp2[k].charAt(0)+"");
                        //System.out.println("and-indeks" + indexOfEle);
                        //System.out.println(indexOfEle);
                        String value = truth.get(i).charAt(indexOfEle)+"";
                        //System.out.println("and-vrednost" + value);
                        result.add(value);
                    }
                }
                
                // result vsebuje vrednosti vseh spremenljivk
                // ce result vsebuje eno ali vec 0, je resitev funkcije 0,
                // drugace je 1
                if(result.size() > 1) {
                    String result_value = "";
                    if(result.contains("0")) {
                        result_value = "0";    
                    } else {
                        result_value = "1";
                    }
                    //System.out.println("rez_value" + result_value);
                    //System.out.println("rez_value_index" + elements.indexOf(output));
                    next.add(elements.indexOf(output), result_value);
                
                // Ce je samo ena spremenljivka, samo dodamo njeno vrednost v
                // vektor next
                } else {
                    //next.add(elements.indexOf(output), result.get(0));
                    //System.out.println("rez_value_else" + result.get(0));
                    //System.out.println("rez_value_else_index" + elements.indexOf(output));
                    next.add(elements.indexOf(output), result.get(0));
                    
                }     
            }
            //System.out.println("next" + next);
            //truthTableOutput.add(next.toString());
            
            //Prepis vektorja v string
            String s = "";
            for(int c=0;c<next.size();c++) {
                s = s + next.get(c);
            }
            
            // Dodamo tranzicijo v rezultat
            truthTableOutput.add(s);
        }  
        //for(int a=0; a<truthTableOutput.size();a++){
        //    System.out.println(truthTableOutput.get(a));
        //}
        return truthTableOutput;
    }
    
    /* Funkcija: getTransitions
     * Funkcija vrne vektor vseh tranzicij glede na podano
     * zacetno stanje in vektor rezultatov, ustavi se ko pride do 
     * prvega cikla.
     * Vhod: zacetno stanje, pravilnostna tabela, rezultati tranzicij
     * Izhod: vektor tranzicij
     */
    public static Vector<String> getTransitions( String start_state, 
                                                 Vector<String> truth_table, 
                                                 Vector<String> results) {
        //System.out.println("truth");
        //System.out.println(truth_table);
        //System.out.println("results");
        //System.out.println(results);
        boolean cicle_found = false;
        Vector<String> visited = new Vector<String>();
        visited.add(start_state);
        
        // Dokler ne najdemo cikla
        while(cicle_found != true) {
            
            // Vzamemo zacetno stanje, iz tega dobimo index in iz rezultatov
            // pridobimo naslednje stanje
            int ind = truth_table.indexOf(start_state);
            String next_state = results.get(ind+1);
            
            // Ce je naslednje stanje vsebovano v vektorju visited,
            // smo nasli cikel, sicer ga dodamo v vektor
            if(visited.contains(next_state)) {
                cicle_found = true;
            } else {
                visited.add(next_state);
            }
            start_state = next_state;
        }
        return visited;
    }
    
    /* Funkcija: getTruthTable 
     * Zgenerira pravilnostno tabelo glede na podano velikost
     * Vhod: stevilo vozlisc
     * Izhod: Vektor vseh kombinacij pravilnostne tabele
     */
    public static Vector<String> getTruthTable(int n) {
        Vector<String> truth = new Vector<String>();
        int rows = (int) Math.pow(2,n);

        for (int i=0; i<rows; i++) {
            String comb = "";
            for (int j=n-1; j>=0; j--) {
                comb = comb + (String)((i/(int) Math.pow(2, j))%2 + "");
            }
            //System.out.println(comb);
            truth.add(comb);
        }
        return truth;
    }
}

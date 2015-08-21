/*
*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*
*/
package org.apache.airavata.datacat.parsers.gridchem.cbsQ;

import java_cup.runtime.lr_parser;
import org.apache.airavata.datacat.parsers.gridchem.GridChemQueueParser;

import javax.swing.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;


public class CbsQParser extends java_cup.runtime.lr_parser implements GridChemQueueParser {

  /** Default constructor. */
  public CbsQParser() {super();}

  /** Constructor which sets the default scanner. */
  public CbsQParser(java_cup.runtime.Scanner s) {super(s);}

    /**
     * Constructor which uses a file reader.
     */
    public CbsQParser(final FileReader fileReader) {
        super(new CBSQLexer(fileReader));
    }


    /** Production table. */
  protected static final short _production_table[][] = 
    lr_parser.unpackFromStrings(new String[]{
            "\000\025\000\002\003\007\000\002\002\004\000\002\004" +
                    "\003\000\002\005\004\000\002\005\003\000\002\021\002" +
                    "\000\002\006\006\000\002\022\002\000\002\007\007\000" +
                    "\002\010\004\000\002\011\004\000\002\012\004\000\002" +
                    "\013\003\000\002\014\004\000\002\014\003\000\002\023" +
                    "\002\000\002\016\006\000\002\024\002\000\002\015\007" +
                    "\000\002\017\004\000\002\020\004"});

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    lr_parser.unpackFromStrings(new String[]{
            "\000\052\000\004\004\005\001\002\000\004\007\012\001" +
                    "\002\000\004\007\uffff\001\002\000\004\002\007\001\002" +
                    "\000\004\002\000\001\002\000\006\005\027\007\012\001" +
                    "\002\000\006\005\ufffd\007\ufffd\001\002\000\004\022\013" +
                    "\001\002\000\004\006\ufffc\001\002\000\004\006\016\001" +
                    "\002\000\006\005\ufffb\007\ufffb\001\002\000\004\020\017" +
                    "\001\002\000\004\010\ufffa\001\002\000\004\010\022\001" +
                    "\002\000\004\011\024\001\002\000\004\023\023\001\002" +
                    "\000\004\011\ufff8\001\002\000\004\024\026\001\002\000" +
                    "\006\005\ufff9\007\ufff9\001\002\000\006\005\ufff7\007\ufff7" +
                    "\001\002\000\004\012\031\001\002\000\006\005\ufffe\007" +
                    "\ufffe\001\002\000\004\014\ufff5\001\002\000\004\017\054" +
                    "\001\002\000\004\014\035\001\002\000\006\014\035\017" +
                    "\ufff6\001\002\000\004\025\037\001\002\000\006\014\ufff3" +
                    "\017\ufff3\001\002\000\004\013\ufff2\001\002\000\004\013" +
                    "\041\001\002\000\004\021\043\001\002\000\006\014\ufff1" +
                    "\017\ufff1\001\002\000\004\015\ufff0\001\002\000\004\015" +
                    "\046\001\002\000\004\016\051\001\002\000\004\026\047" +
                    "\001\002\000\004\016\uffee\001\002\000\006\014\uffef\017" +
                    "\uffef\001\002\000\004\027\052\001\002\000\006\014\uffed" +
                    "\017\uffed\001\002\000\006\014\ufff4\017\ufff4\001\002\000" +
                    "\004\002\001\001\002"});

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    lr_parser.unpackFromStrings(new String[]{
            "\000\052\000\006\003\005\004\003\001\001\000\006\005" +
                    "\007\006\010\001\001\000\002\001\001\000\002\001\001" +
                    "\000\002\001\001\000\004\006\027\001\001\000\002\001" +
                    "\001\000\002\001\001\000\004\021\013\001\001\000\004" +
                    "\007\014\001\001\000\002\001\001\000\002\001\001\000" +
                    "\004\022\017\001\001\000\004\010\020\001\001\000\004" +
                    "\011\024\001\001\000\002\001\001\000\002\001\001\000" +
                    "\002\001\001\000\002\001\001\000\002\001\001\000\006" +
                    "\012\031\013\032\001\001\000\002\001\001\000\002\001" +
                    "\001\000\002\001\001\000\006\014\033\016\035\001\001" +
                    "\000\004\016\052\001\001\000\002\001\001\000\002\001" +
                    "\001\000\004\023\037\001\001\000\004\015\041\001\001" +
                    "\000\002\001\001\000\002\001\001\000\004\024\043\001" +
                    "\001\000\004\017\044\001\001\000\004\020\047\001\001" +
                    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
                    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
                    "\001\001"});

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$parser$actions action_obj;

  /** Action encapsulation object initializer. */
  protected void init_actions()
    {
      action_obj = new CUP$parser$actions(this);
    }

  /** Invoke a user supplied parse action. */
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    /* call code in generated class */
    return action_obj.CUP$parser$do_action(act_num, parser, stack, top);
  }

    //Each string is of the format
    //rmsForce, maximum force, iteration and energy
    private ArrayList<String> result = new ArrayList<String>();
    private String tempStr = "";


    public ArrayList<String> getResult() {
        return result;
    }

    public void addToResult(String value) {
        result.add(value);
    }

    public String getTempStr() {
        return tempStr;
    }

    public void setTempStr(String s) {
        this.tempStr = s;
    }


    /* Adding the parsed data to the hash map */
    @Override
    public HashMap<String, String> getParsedData() throws Exception {
        parse();
        HashMap<String,String> results= new HashMap<String,String>();
        int mpRMSForceCount=0;
        int mpMaximumForceCount=0;
        int mpIterationCount=0;
        int energyCount=0;
        int rmsForceCount=0;
        int maximumForceCount=0;
        int iterationCount=0;
        for(int i=0;i<result.size();i++){
            String singleString= result.get(i);
            String[] temp = singleString.split(" ");
            String secondElement=temp[1];
            String thirdElement=temp[2];
            if(thirdElement.equals("RMS")){
                results.put("CbsQ_Mp_RMS_Force_"+mpRMSForceCount,temp[4]);
                mpRMSForceCount++;
            }else if (thirdElement.equals("Maximum")){
                results.put("CbsQ_MP_Maximum_Force_"+mpMaximumForceCount,temp[4]);
                mpMaximumForceCount++;
            }else if (thirdElement.equals("ITERATION")&&temp[1].equals("MP")){
                results.put("CbsQ_MP_Iteration_"+mpIterationCount,temp[3]);
                mpIterationCount++;
            }else if(thirdElement.equals("ENERGY")){
                results.put("CbsQ_Energy_"+energyCount,temp[3]);
                energyCount++;
            }else if(secondElement.equals("RMS")){
                results.put("CbsQ_RMS_Force"+rmsForceCount,temp[3]);
                rmsForceCount++;
            }else if(secondElement.equals("Maximum")){
                results.put("CbsQ_Maximum_Force"+maximumForceCount,temp[3]);
                maximumForceCount++;
            }else if(thirdElement.equals("ITERATION")){
                results.put("CbsQ_Iteration_"+iterationCount,temp[3]);
                iterationCount++;
            }


        }
        return null;
    }



  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 1;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}


}

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$parser$actions {

 
  //__________________________________
  public static boolean DEBUG = true;
  private static JTable table;               
  private static final String tableLabel = "SCF Intermediate Results:";
// private static String cycle = "0";
 
  
  public static JTable getTable() {
    return table;
  }

  public static String getTableLabel() {
    return tableLabel;
  }

//   }

  private final CbsQParser CbsQParser;

  /** Constructor */
  CUP$parser$actions(CbsQParser CbsQParser) {
    this.CbsQParser = CbsQParser;
  }

  /** Method with the actual generated action code. */
  public final java_cup.runtime.Symbol CUP$parser$do_action(
    int                        CUP$parser$act_num,
    java_cup.runtime.lr_parser CUP$parser$parser,
    java.util.Stack            CUP$parser$stack,
    int                        CUP$parser$top)
    throws java.lang.Exception
    {
      /* Symbol object for return from actions */
      java_cup.runtime.Symbol CUP$parser$result;

      /* select the action based on the action number */
      switch (CUP$parser$act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 20: // force2 ::= MPRms MPRGRAD 
            {
              Object RESULT = null;
		int mprgleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int mprgright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Float mprg = (Float)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 //___________________________________________________________________
   if (DEBUG) System.out.println("CUP:g1: MP RMS Force "+mprg);
   CbsQParser.addToResult("CUP:g1: MP RMS Force "+mprg);
 
              CUP$parser$result = new java_cup.runtime.Symbol(14/*force2*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 19: // force1 ::= MPMax MPMGRAD 
            {
              Object RESULT = null;
		int mpmgleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int mpmgright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Float mpmg = (Float)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 //___________________________________________________________________
   if (DEBUG) System.out.println("CUP:g1: MP Maximum Force "+mpmg);
   CbsQParser.addToResult("CUP:g1: MP Maximum Force "+mpmg);
 
              CUP$parser$result = new java_cup.runtime.Symbol(13/*force1*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 18: // mpcycle ::= NMP MPITER NT$3 force1 force2 
            {
              Object RESULT = null;
              // propagate RESULT from NT$3
              if ( ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value != null )
                RESULT = (Object) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int itleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).left;
		int itright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).right;
		Integer it = (Integer)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-3)).value;

              CUP$parser$result = new java_cup.runtime.Symbol(11/*mpcycle*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 17: // NT$3 ::= 
            {
              Object RESULT = null;
		int itleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int itright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Integer it = (Integer)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
 //___________________________________________________________________
   if (DEBUG) System.out.println("CUP:g1:  MP ITERATION "+it);
   CbsQParser.addToResult("CUP:g1:  MP ITERATION "+it);
 
              CUP$parser$result = new java_cup.runtime.Symbol(18/*NT$3*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 16: // mpcycle1 ::= MPEnerg MPENERGY NT$2 mpcycle 
            {
              Object RESULT = null;
              // propagate RESULT from NT$2
              if ( ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value != null )
                RESULT = (Object) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		int mpenleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int mpenright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Float mpen = (Float)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;

              CUP$parser$result = new java_cup.runtime.Symbol(12/*mpcycle1*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 15: // NT$2 ::= 
            {
              Object RESULT = null;
		int mpenleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int mpenright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Float mpen = (Float)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
 //___________________________________________________________________
   if (DEBUG) System.out.println("CUP:g1:  ENERGY "+mpen);
   CbsQParser.addToResult("CUP:g1:  ENERGY "+mpen);
 
              CUP$parser$result = new java_cup.runtime.Symbol(17/*NT$2*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 14: // mppat ::= mpcycle1 
            {
              Object RESULT = null;

              CUP$parser$result = new java_cup.runtime.Symbol(10/*mppat*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 13: // mppat ::= mppat mpcycle1 
            {
              Object RESULT = null;
		
  if (DEBUG) System.out.println("CUP:g1: in mppat");
 
              CUP$parser$result = new java_cup.runtime.Symbol(10/*mppat*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // mpintro ::= MPStart 
            {
              Object RESULT = null;
		
   if (DEBUG) System.out.println("CUP:g1: MPSTart ");
 
              CUP$parser$result = new java_cup.runtime.Symbol(9/*mpintro*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // mp2 ::= mpintro mppat 
            {
              Object RESULT = null;
		
    if (DEBUG) System.out.println("CUP:g1: in mp2 mpintro mppat");
 
              CUP$parser$result = new java_cup.runtime.Symbol(8/*mp2*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // grad2 ::= RmsGrad1 RGRAD1 
            {
              Object RESULT = null;
		int rgleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int rgright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Float rg = (Float)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 //___________________________________________________________________
   if (DEBUG) System.out.println("CUP:gopt: RMS Force "+rg);
   CbsQParser.addToResult("CUP:gopt: RMS Force "+rg);
 
              CUP$parser$result = new java_cup.runtime.Symbol(7/*grad2*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // grad1 ::= MaxGrad1 MGRAD1 
            {
              Object RESULT = null;
		int mgleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int mgright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Float mg = (Float)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 //___________________________________________________________________
   if (DEBUG) System.out.println("CUP:gopt: Maximum Force "+mg);
   CbsQParser.addToResult("CUP:gopt: Maximum Force "+mg);
 
              CUP$parser$result = new java_cup.runtime.Symbol(6/*grad1*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // cycle ::= NSearch1 ITERATION1 NT$1 grad1 grad2 
            {
              Object RESULT = null;
              // propagate RESULT from NT$1
              if ( ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value != null )
                RESULT = (Object) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int cleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).left;
		int cright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).right;
		Integer c = (Integer)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-3)).value;

              CUP$parser$result = new java_cup.runtime.Symbol(5/*cycle*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // NT$1 ::= 
            {
              Object RESULT = null;
		int cleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int cright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Integer c = (Integer)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
 //___________________________________________________________________
   if (DEBUG) System.out.println("CUP:gopt:  ITERATION "+c);
   CbsQParser.addToResult("CUP:gopt:  ITERATION "+c);
 
              CUP$parser$result = new java_cup.runtime.Symbol(16/*NT$1*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // scfcycle ::= Energ1 ENERGY1 NT$0 cycle 
            {
              Object RESULT = null;
              // propagate RESULT from NT$0
              if ( ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value != null )
                RESULT = (Object) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Float e = (Float)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;

              CUP$parser$result = new java_cup.runtime.Symbol(4/*scfcycle*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // NT$0 ::= 
            {
              Object RESULT = null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Float e = (Float)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
 //___________________________________________________________________
   if (DEBUG) System.out.println("CUP:gopt:  ENERGY "+e);
   CbsQParser.addToResult("CUP:gopt:  ENERGY "+e);
 
              CUP$parser$result = new java_cup.runtime.Symbol(15/*NT$0*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // scfpat ::= scfcycle 
            {
              Object RESULT = null;

              CUP$parser$result = new java_cup.runtime.Symbol(3/*scfpat*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // scfpat ::= scfpat scfcycle 
            {
              Object RESULT = null;
		 if (DEBUG) System.out.println("CUP:gopt: in scfpat"); 
              CUP$parser$result = new java_cup.runtime.Symbol(3/*scfpat*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // scfintro ::= FOUNDITER1 
            {
              Object RESULT = null;
		 if (DEBUG) System.out.println("CUP:gopt:  found the start of Iteration"); 
              CUP$parser$result = new java_cup.runtime.Symbol(2/*scfintro*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // $START ::= startpt EOF 
            {
              Object RESULT = null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		Object start_val = (Object)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		RESULT = start_val;
              CUP$parser$result = new java_cup.runtime.Symbol(0/*$START*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          /* ACCEPT */
          CUP$parser$parser.done_parsing();
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // startpt ::= scfintro scfpat SCFDONE1 mp2 MPDONE 
            {
              Object RESULT = null;
		 if (DEBUG) System.out.println("CUP:gopt:  end of parse tree "); 
 	     
              CUP$parser$result = new java_cup.runtime.Symbol(1/*startpt*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number found in internal parse table");

        }
    }
}


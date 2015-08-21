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
package org.apache.airavata.datacat.parsers.gridchem.scfb;


import java_cup.runtime.lr_parser;
import org.apache.airavata.datacat.parsers.gridchem.GridChemQueueParser;

import javax.swing.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class SCFbParser extends java_cup.runtime.lr_parser implements GridChemQueueParser{

  /** Default constructor. */
  public SCFbParser() {super();}

  /** Constructor which sets the default scanner. */
  public SCFbParser(java_cup.runtime.Scanner s) {super(s);}

    /**
     * Constructor which uses a file reader.
     */
    public SCFbParser(final FileReader fileReader) {
        super(new SCFbLexer(fileReader));
    }


    /** Production table. */
  protected static final short _production_table[][] = 
    lr_parser.unpackFromStrings(new String[]{
            "\000\016\000\002\015\002\000\002\003\006\000\002\002" +
                    "\004\000\002\016\002\000\002\004\006\000\002\017\002" +
                    "\000\002\020\002\000\002\021\002\000\002\022\002\000" +
                    "\002\023\002\000\002\024\002\000\002\025\002\000\002" +
                    "\026\002\000\002\014\043"});

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    lr_parser.unpackFromStrings(new String[]{
            "\000\054\000\004\004\006\001\002\000\004\033\014\001" +
                    "\002\000\004\002\012\001\002\000\004\005\ufffe\001\002" +
                    "\000\004\005\010\001\002\000\004\050\011\001\002\000" +
                    "\004\033\ufffd\001\002\000\004\002\uffff\001\002\000\004" +
                    "\046\001\001\002\000\004\034\ufffc\001\002\000\004\034" +
                    "\016\001\002\000\004\044\017\001\002\000\004\051\020" +
                    "\001\002\000\004\040\ufffb\001\002\000\004\040\022\001" +
                    "\002\000\004\047\023\001\002\000\004\045\024\001\002" +
                    "\000\004\035\ufffa\001\002\000\004\035\026\001\002\000" +
                    "\004\044\027\001\002\000\004\051\030\001\002\000\004" +
                    "\037\ufff9\001\002\000\004\037\032\001\002\000\004\044" +
                    "\033\001\002\000\004\051\034\001\002\000\004\036\ufff8" +
                    "\001\002\000\004\036\036\001\002\000\004\044\037\001" +
                    "\002\000\004\051\040\001\002\000\004\041\ufff7\001\002" +
                    "\000\004\041\042\001\002\000\004\044\043\001\002\000" +
                    "\004\051\044\001\002\000\004\042\ufff6\001\002\000\004" +
                    "\042\046\001\002\000\004\044\047\001\002\000\004\051" +
                    "\050\001\002\000\004\043\ufff5\001\002\000\004\043\052" +
                    "\001\002\000\004\044\053\001\002\000\004\051\054\001" +
                    "\002\000\004\046\ufff4\001\002\000\004\046\056\001\002" +
                    "\000\004\002\000\001\002"});

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    lr_parser.unpackFromStrings(new String[]{
            "\000\054\000\006\003\004\004\003\001\001\000\004\014" +
                    "\012\001\001\000\002\001\001\000\004\016\006\001\001" +
                    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
                    "\002\001\001\000\004\015\054\001\001\000\004\017\014" +
                    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
                    "\001\000\004\020\020\001\001\000\002\001\001\000\002" +
                    "\001\001\000\002\001\001\000\004\021\024\001\001\000" +
                    "\002\001\001\000\002\001\001\000\002\001\001\000\004" +
                    "\022\030\001\001\000\002\001\001\000\002\001\001\000" +
                    "\002\001\001\000\004\023\034\001\001\000\002\001\001" +
                    "\000\002\001\001\000\002\001\001\000\004\024\040\001" +
                    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
                    "\000\004\025\044\001\001\000\002\001\001\000\002\001" +
                    "\001\000\002\001\001\000\004\026\050\001\001\000\002" +
                    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
                    "\001\000\002\001\001\000\002\001\001"});

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$parser$actions action_obj;

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


    /*Adding the parsed data to the hash map */
    @Override
    public HashMap<String, String> getParsedData() throws Exception {
        parse();
        HashMap<String,String> results= new HashMap<String,String>();
        int eeCount=0;
        int peCount=0;
        int keCount=0;
        int s2Count=0;
        int minUSVTCount=0;
        int convgCount=0;
        int auCount=0;
        int erhfCount=0;
        int lenxCount=0;
        for(int i=0;i<result.size();i++){
            String singleString = result.get(i);
            String[] temp= singleString.split(" ");
            if(temp.length>1){
                String keyString= temp[0];
                String dataString=temp[1];
                if(keyString.equalsIgnoreCase("EE")){
                    results.put("SCFbParser_EE_"+eeCount,dataString);
                    eeCount++;
                }else if(keyString.equalsIgnoreCase("PE")){
                    results.put("SCFbParser_PE_"+peCount,dataString);
                    peCount++;
                }else if(keyString.equalsIgnoreCase("KE")){
                    results.put("SCFbParser_KE_"+keCount,dataString);
                    keCount++;
                }else if(keyString.equalsIgnoreCase("S2")) {
                    results.put("SCFbParser_S2_" + s2Count, dataString);
                    s2Count++;
                }else if(keyString.equalsIgnoreCase("MINUSVT")) {
                    results.put("SCFbParser_MINUSVT_" + minUSVTCount, dataString);
                    minUSVTCount++;
                }else if(keyString.equalsIgnoreCase("CONVG")) {
                    results.put("SCFbParser_CONVG_" + convgCount, dataString);
                    convgCount++;
                }else if(keyString.equalsIgnoreCase("AU")) {
                    results.put("SCFbParser_AU_" + auCount, dataString);
                    auCount++;
                }else if(keyString.equalsIgnoreCase("ERHF")) {
                    results.put("SCFbParser_ERHF_" + erhfCount, dataString);
                    erhfCount++;
                }else if(keyString.equalsIgnoreCase("LENX")) {
                    results.put("SCFbParser_LENX_" + lenxCount, dataString);
                   lenxCount++;
                }


            }

        }
        return results;
    }

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

  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 2;}

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
  private static final String tableLabel = "SCF Final Results:";
  
  public static JTable getTable() {
    return table;
  }

  public static String getTableLabel() {
    return tableLabel;
  }

  private final SCFbParser SCFbParser;

  /** Constructor */
  CUP$parser$actions(SCFbParser SCFbParser) {
    this.SCFbParser = SCFbParser;
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
          case 13: // scffinal ::= SCFDONE NT$2 ERHF EQUALS FLOAT NT$3 AU INT DUMMY NT$4 CONVG EQUALS FLOAT NT$5 MINUSVT EQUALS FLOAT NT$6 S2 EQUALS FLOAT NT$7 KE EQUALS FLOAT NT$8 PE EQUALS FLOAT NT$9 EE EQUALS FLOAT 
            {
              Object RESULT = null;
              // propagate RESULT from NT$2
              if ( ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-31)).value != null )
                RESULT = (Object) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-31)).value;
              // propagate RESULT from NT$3
              if ( ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-27)).value != null )
                RESULT = (Object) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-27)).value;
              // propagate RESULT from NT$4
              if ( ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-23)).value != null )
                RESULT = (Object) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-23)).value;
              // propagate RESULT from NT$5
              if ( ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-19)).value != null )
                RESULT = (Object) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-19)).value;
              // propagate RESULT from NT$6
              if ( ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-15)).value != null )
                RESULT = (Object) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-15)).value;
              // propagate RESULT from NT$7
              if ( ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-11)).value != null )
                RESULT = (Object) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-11)).value;
              // propagate RESULT from NT$8
              if ( ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-7)).value != null )
                RESULT = (Object) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-7)).value;
              // propagate RESULT from NT$9
              if ( ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-3)).value != null )
                RESULT = (Object) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-3)).value;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-28)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-28)).right;
		String e = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-28)).value;
		int ileft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-25)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-25)).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-25)).value;
		int cleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-20)).left;
		int cright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-20)).right;
		String c = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-20)).value;
		int mleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-16)).left;
		int mright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-16)).right;
		String m = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-16)).value;
		int s2left = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-12)).left;
		int s2right = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-12)).right;
		String s2 = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-12)).value;
		int keleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-8)).left;
		int keright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-8)).right;
		String ke = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-8)).value;
		int peleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)).left;
		int peright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)).right;
		String pe = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-4)).value;
		int eeleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int eeright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String ee = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 //____________________________________________________
if (DEBUG) System.out.println("CUP:scfb:  EE "+ee);
SCFbParser.addToResult("EE "+ee);
//parseSCF.putField("EE", ee);
 
              CUP$parser$result = new java_cup.runtime.Symbol(10/*scffinal*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-32)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // NT$9 ::= 
            {
              Object RESULT = null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-24)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-24)).right;
		String e = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-24)).value;
		int ileft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-21)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-21)).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-21)).value;
		int cleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-16)).left;
		int cright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-16)).right;
		String c = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-16)).value;
		int mleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-12)).left;
		int mright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-12)).right;
		String m = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-12)).value;
		int s2left = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-8)).left;
		int s2right = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-8)).right;
		String s2 = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-8)).value;
		int keleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)).left;
		int keright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)).right;
		String ke = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-4)).value;
		int peleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int peright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String pe = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
 //____________________________________________________
if (DEBUG) System.out.println("CUP:scfb:  PE "+pe);
SCFbParser.addToResult("PE "+pe);
//parseSCF.putField("PE", pe);
 
              CUP$parser$result = new java_cup.runtime.Symbol(20/*NT$9*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // NT$8 ::= 
            {
              Object RESULT = null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-20)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-20)).right;
		String e = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-20)).value;
		int ileft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-17)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-17)).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-17)).value;
		int cleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-12)).left;
		int cright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-12)).right;
		String c = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-12)).value;
		int mleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-8)).left;
		int mright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-8)).right;
		String m = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-8)).value;
		int s2left = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)).left;
		int s2right = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)).right;
		String s2 = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-4)).value;
		int keleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int keright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String ke = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
 //____________________________________________________
if (DEBUG) System.out.println("CUP:scfb:  KE "+ke);
SCFbParser.addToResult("KE "+ke);
//parseSCF.putField("KE", ke);
 
              CUP$parser$result = new java_cup.runtime.Symbol(19/*NT$8*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // NT$7 ::= 
            {
              Object RESULT = null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-16)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-16)).right;
		String e = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-16)).value;
		int ileft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-13)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-13)).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-13)).value;
		int cleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-8)).left;
		int cright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-8)).right;
		String c = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-8)).value;
		int mleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)).left;
		int mright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)).right;
		String m = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-4)).value;
		int s2left = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int s2right = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String s2 = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
 //____________________________________________________
if (DEBUG) System.out.println("CUP:scfb:  S2 "+s2);
SCFbParser.addToResult("S2 "+s2);
//parseSCF.putField("S2", s2);
 
              CUP$parser$result = new java_cup.runtime.Symbol(18/*NT$7*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // NT$6 ::= 
            {
              Object RESULT = null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-12)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-12)).right;
		String e = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-12)).value;
		int ileft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-9)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-9)).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-9)).value;
		int cleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)).left;
		int cright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)).right;
		String c = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-4)).value;
		int mleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int mright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String m = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
 //________________________________________________________
if (DEBUG) System.out.println("CUP:scfb:  MINUSVT "+m);
SCFbParser.addToResult("MINUSVT "+m);
//parseSCF.putField("-V/T", m);
 
              CUP$parser$result = new java_cup.runtime.Symbol(17/*NT$6*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // NT$5 ::= 
            {
              Object RESULT = null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-8)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-8)).right;
		String e = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-8)).value;
		int ileft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-5)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-5)).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-5)).value;
		int cleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int cright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String c = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
 //______________________________________________________
if (DEBUG) System.out.println("CUP:scfb:  CONVG "+c);
SCFbParser.addToResult("CONVG "+c);
//parseSCF.putField("CONVG", c);
 
              CUP$parser$result = new java_cup.runtime.Symbol(16/*NT$5*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // NT$4 ::= 
            {
              Object RESULT = null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)).right;
		String e = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-4)).value;
		int ileft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
 //___________________________________________________
if (DEBUG) System.out.println("CUP:scfb:  AU "+i);
                SCFbParser.addToResult("AU "+i);
//parseSCF.putField("AU", i);
 
              CUP$parser$result = new java_cup.runtime.Symbol(15/*NT$4*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // NT$3 ::= 
            {
              Object RESULT = null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String e = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
 //_____________________________________________________
if (DEBUG) System.out.println("CUP:scfb:  ERHF "+e);
                SCFbParser.addToResult("ERHF "+e);
//parseSCF.putField("E(RHF)", e);
 
              CUP$parser$result = new java_cup.runtime.Symbol(14/*NT$3*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // NT$2 ::= 
            {
              Object RESULT = null;
 if (DEBUG) System.out.println("CUP:scfb:  scfdone "); 
              CUP$parser$result = new java_cup.runtime.Symbol(13/*NT$2*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // scfintro ::= FOUNDSCF NT$1 FOUNDLENX LENXINT 
            {
              Object RESULT = null;
              // propagate RESULT from NT$1
              if ( ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value != null )
                RESULT = (Object) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int ileft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 if (DEBUG) System.out.println("CUP:scfb:  LENX --> "+i);
                SCFbParser.addToResult("LENX "+i);
 
              CUP$parser$result = new java_cup.runtime.Symbol(2/*scfintro*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // NT$1 ::= 
            {
              Object RESULT = null;
 if (DEBUG) System.out.println("CUP:scfb:  found SCF"); 
              CUP$parser$result = new java_cup.runtime.Symbol(12/*NT$1*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // $START ::= startpt EOF 
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
          case 1: // startpt ::= scfintro scffinal NT$0 LEAVE 
            {
              Object RESULT = null;
              // propagate RESULT from NT$0
              if ( ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value != null )
                RESULT = (Object) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		 if (DEBUG) System.out.println("CUP:scfb:  found LEAVE"); 
             
              CUP$parser$result = new java_cup.runtime.Symbol(1/*startpt*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // NT$0 ::= 
            {
              Object RESULT = null;
 if (DEBUG) System.out.println("CUP:scfa:  found scfintro scffinal");
 	       table = new JTable();
	      // table = parseSCF.getTable();
             
              CUP$parser$result = new java_cup.runtime.Symbol(11/*NT$0*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number found in internal parse table");

        }
    }
}


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
package org.apache.airavata.datacat.parsers.gridchem.gfinalcoord;


import java_cup.runtime.lr_parser;
import org.apache.airavata.datacat.parsers.gridchem.GridChemQueueParser;

import javax.swing.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;


public class GFinalCoordParser extends java_cup.runtime.lr_parser implements GridChemQueueParser {

  /** Default constructor. */
  public GFinalCoordParser() {super();}

  /** Constructor which sets the default scanner. */
  public GFinalCoordParser(java_cup.runtime.Scanner s) {super(s);}

    /**
     * Constructor which uses a file reader.
     */
    public GFinalCoordParser(final FileReader fileReader) {
        super(new GFinalCoordLexer(fileReader));
    }

  /** Production table. */
  protected static final short _production_table[][] = 
    lr_parser.unpackFromStrings(new String[]{
            "\000\012\000\002\003\005\000\002\002\004\000\002\004" +
                    "\004\000\002\005\004\000\002\005\003\000\002\006\007" +
                    "\000\002\007\003\000\002\010\003\000\002\011\003\000" +
                    "\002\012\003"});

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    lr_parser.unpackFromStrings(new String[]{
            "\000\023\000\004\006\005\001\002\000\004\005\011\001" +
                    "\002\000\004\004\010\001\002\000\004\002\007\001\002" +
                    "\000\004\002\000\001\002\000\004\005\uffff\001\002\000" +
                    "\004\010\017\001\002\000\006\005\011\007\014\001\002" +
                    "\000\006\005\ufffd\007\ufffd\001\002\000\004\002\001\001" +
                    "\002\000\006\005\ufffe\007\ufffe\001\002\000\004\011\020" +
                    "\001\002\000\004\011\ufffb\001\002\000\004\012\ufffa\001" +
                    "\002\000\004\012\022\001\002\000\004\013\ufff9\001\002" +
                    "\000\004\013\024\001\002\000\006\005\ufff8\007\ufff8\001" +
                    "\002\000\006\005\ufffc\007\ufffc\001\002"});

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    lr_parser.unpackFromStrings(new String[]{
            "\000\023\000\006\003\005\004\003\001\001\000\006\005" +
                    "\011\006\012\001\001\000\002\001\001\000\002\001\001" +
                    "\000\002\001\001\000\002\001\001\000\004\007\015\001" +
                    "\001\000\004\006\014\001\001\000\002\001\001\000\002" +
                    "\001\001\000\002\001\001\000\004\010\020\001\001\000" +
                    "\002\001\001\000\002\001\001\000\004\011\022\001\001" +
                    "\000\002\001\001\000\004\012\024\001\001\000\002\001" +
                    "\001\000\002\001\001"});

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$parser$actions action_obj;

    //Each string is of the format
    //center# atomic# x_coord y_coord z_coord
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
        int e6Count=0;
        int e5Count=0;
        int e4Count=0;
        int e=0;
        HashMap<String, String> results = new HashMap<String, String>();
        for (int i = 0; i < result.size(); i++) {
            String singleString= result.get(i);
            String[] temp= singleString.split(" ");
            String firstElement=temp[0];
            String dataElement=temp[1];
            if(firstElement.equals("e6")){
                results.put("GFinalCoord_e6_"+e6Count,dataElement);
                e6Count++;
            }else if(firstElement.equals("e5")){
                results.put("GFinalCoord_e5_"+e5Count,dataElement);
                e5Count++;
            }else if(firstElement.equals("e4")){
                results.put("GFinalCoord_e4_"+e4Count,dataElement);
                e4Count++;
            }else if(firstElement.equals("e")){
                results.put("GFinalCoord_e_"+e,dataElement);
                e++;
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


  private final GFinalCoordParser GFinalCoordParser;

  /** Constructor */
  CUP$parser$actions(GFinalCoordParser GFinalCoordParser) {
    this.GFinalCoordParser = GFinalCoordParser;
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
          case 9: // inp6 ::= INPUT6 
            {
              Object RESULT = null;
		int e6left = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int e6right = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Float e6 = (Float)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
        GFinalCoordParser.addToResult("e6 "+e6);

              CUP$parser$result = new java_cup.runtime.Symbol(8/*inp6*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // inp5 ::= INPUT5 
            {
              Object RESULT = null;
		int e5left = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int e5right = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Float e5 = (Float)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
        GFinalCoordParser.addToResult("e5 "+e5);

              CUP$parser$result = new java_cup.runtime.Symbol(7/*inp5*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // inp4 ::= INPUT4 
            {
              Object RESULT = null;
		int e4left = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int e4right = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Float e4 = (Float)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
        GFinalCoordParser.addToResult("e4 "+e4);

              CUP$parser$result = new java_cup.runtime.Symbol(6/*inp4*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // inp3 ::= INPUT3 
            {
              Object RESULT = null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Float e = (Float)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
        GFinalCoordParser.addToResult("e "+e);

              CUP$parser$result = new java_cup.runtime.Symbol(5/*inp3*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // scfcycle ::= INPUT2 inp3 inp4 inp5 inp6 
            {
              Object RESULT = null;

              CUP$parser$result = new java_cup.runtime.Symbol(4/*scfcycle*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-4)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
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

              CUP$parser$result = new java_cup.runtime.Symbol(3/*scfpat*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // scfintro ::= FOUNDITER INPUT1 
            {
              Object RESULT = null;

              CUP$parser$result = new java_cup.runtime.Symbol(2/*scfintro*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
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
          case 0: // startpt ::= scfintro scfpat SCFDONE 
            {
              Object RESULT = null;

              CUP$parser$result = new java_cup.runtime.Symbol(1/*startpt*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number found in internal parse table");

        }
    }
}


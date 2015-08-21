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
package org.apache.airavata.datacat.parsers.gridchem.mp2to5b;


import java_cup.runtime.lr_parser;
import org.apache.airavata.datacat.parsers.gridchem.GridChemQueueParser;
import org.apache.airavata.datacat.parsers.gridchem.Settings;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;


public class MP2to5bParser extends java_cup.runtime.lr_parser implements GridChemQueueParser{

  /** Default constructor. */
  public MP2to5bParser() {super();}

  /** Constructor which sets the default scanner. */
  public MP2to5bParser(java_cup.runtime.Scanner s) {super(s);}

    /**
     * Constructor which uses a file reader.
     */
    public MP2to5bParser(final FileReader fileReader) {
        super(new MP2to5bLexer(fileReader));
    }

    /** Production table. */
  protected static final short _production_table[][] = 
    lr_parser.unpackFromStrings(new String[]{
            "\000\005\000\002\003\004\000\002\002\004\000\002\003" +
                    "\003\000\002\004\003\000\002\004\003"});

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    lr_parser.unpackFromStrings(new String[]{
            "\000\007\000\006\017\004\020\005\001\002\000\010\002" +
                    "\ufffe\017\ufffe\020\ufffe\001\002\000\010\002\ufffd\017\ufffd" +
                    "\020\ufffd\001\002\000\010\002\uffff\017\uffff\020\uffff\001" +
                    "\002\000\010\002\011\017\004\020\005\001\002\000\010" +
                    "\002\001\017\001\020\001\001\002\000\004\002\000\001" +
                    "\002"});

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    lr_parser.unpackFromStrings(new String[]{
            "\000\007\000\006\003\006\004\005\001\001\000\002\001" +
                    "\001\000\002\001\001\000\002\001\001\000\004\004\007" +
                    "\001\001\000\002\001\001\000\002\001\001"});

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$parser$actions action_obj;

    //Each string is of the format
    //mp2SpecificOption
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
        HashMap<String, String> results = new HashMap<String,String>();
        int j=0;
        for (int i=0; i<result.size();i++){
            String resultString= result.get(i);
            String[] temp=resultString.split(" ");
            if(temp.length<3){
                continue;
            }
            String element=temp[2]+" "+temp[3]+" "+temp[4];
            if (element.equalsIgnoreCase("Frozen Core Option:")){
                j++;
                String tempString= "MP2to5b_Frozen Core Option_" + j;
                results.put(tempString,temp[6]);
            }else{
                continue;
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


  /** User initialization code. */
  public void user_init() throws java.lang.Exception
    {
 if(Settings.DEBUG) System.out.println("CUP:mp2to5b:  entry");
    }



    ///public static boolean DEBUG = false;
}

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$parser$actions {
  private final MP2to5bParser MP2to5bParser;

  /** Constructor */
  CUP$parser$actions(MP2to5bParser MP2to5bParser) {
    this.MP2to5bParser = MP2to5bParser;
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
          case 4: // element ::= FCOPT2 
            {
              Object RESULT = null;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 if(Settings.DEBUG) System.out.println("CUP:mp2to5b:  Frozen Core Option:  "+s);
         MP2to5bParser.addToResult("CUP:mp2to5b:  Frozen Core Option:  "+s);
               	//   ParseMP2to5.put("Frozen Core Option?  (Check raw output)  ", s);
              CUP$parser$result = new java_cup.runtime.Symbol(2/*element*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // element ::= FCOPT 
            {
              Object RESULT = null;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 if(Settings.DEBUG) System.out.println("CUP:mp2to5b:  Frozen Core Option:  "+s);
         MP2to5bParser.addToResult("CUP:mp2to5b:  Frozen Core Option:  "+s);

	//   ParseMP2to5.put("Frozen Core Option:  ", s);
              CUP$parser$result = new java_cup.runtime.Symbol(2/*element*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // startpt ::= element 
            {
              Object RESULT = null;

              CUP$parser$result = new java_cup.runtime.Symbol(1/*startpt*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
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
          case 0: // startpt ::= startpt element 
            {
              Object RESULT = null;

              CUP$parser$result = new java_cup.runtime.Symbol(1/*startpt*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number found in internal parse table");

        }
    }
}


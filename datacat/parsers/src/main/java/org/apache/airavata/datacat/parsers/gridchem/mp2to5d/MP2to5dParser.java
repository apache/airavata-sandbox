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
package org.apache.airavata.datacat.parsers.gridchem.mp2to5d;


import java_cup.runtime.lr_parser;
import org.apache.airavata.datacat.parsers.gridchem.GridChemQueueParser;
import org.apache.airavata.datacat.parsers.gridchem.Settings;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;


public class MP2to5dParser extends java_cup.runtime.lr_parser implements GridChemQueueParser{

  /** Default constructor. */
  public MP2to5dParser() {super();}

  /** Constructor which sets the default scanner. */
  public MP2to5dParser(java_cup.runtime.Scanner s) {super(s);}

  /** Constructor which uses the file reader */
    public MP2to5dParser(final FileReader fileReader){super(new MP2to5dLexer(fileReader));}

  /** Production table. */
  protected static final short _production_table[][] = 
    lr_parser.unpackFromStrings(new String[]{
            "\000\016\000\002\003\004\000\002\002\004\000\002\003" +
                    "\003\000\002\004\004\000\002\004\004\000\002\004\004" +
                    "\000\002\004\004\000\002\004\004\000\002\004\004\000" +
                    "\002\004\004\000\002\004\004\000\002\004\004\000\002" +
                    "\004\004\000\002\004\004"});

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    lr_parser.unpackFromStrings(new String[]{
            "\000\033\000\030\004\017\005\014\006\004\007\016\010" +
                    "\020\011\013\012\011\013\012\014\015\015\010\016\006" +
                    "\001\002\000\004\025\035\001\002\000\032\002\034\004" +
                    "\017\005\014\006\004\007\016\010\020\011\013\012\011" +
                    "\013\012\014\015\015\010\016\006\001\002\000\004\025" +
                    "\032\001\002\000\032\002\uffff\004\uffff\005\uffff\006\uffff" +
                    "\007\uffff\010\uffff\011\uffff\012\uffff\013\uffff\014\uffff\015" +
                    "\uffff\016\uffff\001\002\000\004\025\031\001\002\000\004" +
                    "\025\030\001\002\000\004\025\027\001\002\000\004\025" +
                    "\026\001\002\000\004\025\025\001\002\000\004\025\024" +
                    "\001\002\000\004\025\023\001\002\000\004\025\022\001" +
                    "\002\000\004\025\021\001\002\000\032\002\ufffa\004\ufffa" +
                    "\005\ufffa\006\ufffa\007\ufffa\010\ufffa\011\ufffa\012\ufffa\013" +
                    "\ufffa\014\ufffa\015\ufffa\016\ufffa\001\002\000\032\002\ufffe" +
                    "\004\ufffe\005\ufffe\006\ufffe\007\ufffe\010\ufffe\011\ufffe\012" +
                    "\ufffe\013\ufffe\014\ufffe\015\ufffe\016\ufffe\001\002\000\032" +
                    "\002\ufffb\004\ufffb\005\ufffb\006\ufffb\007\ufffb\010\ufffb\011" +
                    "\ufffb\012\ufffb\013\ufffb\014\ufffb\015\ufffb\016\ufffb\001\002" +
                    "\000\032\002\ufff6\004\ufff6\005\ufff6\006\ufff6\007\ufff6\010" +
                    "\ufff6\011\ufff6\012\ufff6\013\ufff6\014\ufff6\015\ufff6\016\ufff6" +
                    "\001\002\000\032\002\ufffd\004\ufffd\005\ufffd\006\ufffd\007" +
                    "\ufffd\010\ufffd\011\ufffd\012\ufffd\013\ufffd\014\ufffd\015\ufffd" +
                    "\016\ufffd\001\002\000\032\002\ufff9\004\ufff9\005\ufff9\006" +
                    "\ufff9\007\ufff9\010\ufff9\011\ufff9\012\ufff9\013\ufff9\014\ufff9" +
                    "\015\ufff9\016\ufff9\001\002\000\032\002\ufff7\004\ufff7\005" +
                    "\ufff7\006\ufff7\007\ufff7\010\ufff7\011\ufff7\012\ufff7\013\ufff7" +
                    "\014\ufff7\015\ufff7\016\ufff7\001\002\000\032\002\ufff8\004" +
                    "\ufff8\005\ufff8\006\ufff8\007\ufff8\010\ufff8\011\ufff8\012\ufff8" +
                    "\013\ufff8\014\ufff8\015\ufff8\016\ufff8\001\002\000\032\002" +
                    "\ufff5\004\ufff5\005\ufff5\006\ufff5\007\ufff5\010\ufff5\011\ufff5" +
                    "\012\ufff5\013\ufff5\014\ufff5\015\ufff5\016\ufff5\001\002\000" +
                    "\032\002\ufff4\004\ufff4\005\ufff4\006\ufff4\007\ufff4\010\ufff4" +
                    "\011\ufff4\012\ufff4\013\ufff4\014\ufff4\015\ufff4\016\ufff4\001" +
                    "\002\000\032\002\001\004\001\005\001\006\001\007\001" +
                    "\010\001\011\001\012\001\013\001\014\001\015\001\016" +
                    "\001\001\002\000\004\002\000\001\002\000\032\002\ufffc" +
                    "\004\ufffc\005\ufffc\006\ufffc\007\ufffc\010\ufffc\011\ufffc\012" +
                    "\ufffc\013\ufffc\014\ufffc\015\ufffc\016\ufffc\001\002"});

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    lr_parser.unpackFromStrings(new String[]{
            "\000\033\000\006\003\004\004\006\001\001\000\002\001" +
                    "\001\000\004\004\032\001\001\000\002\001\001\000\002" +
                    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
                    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
                    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
                    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
                    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
                    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
                    "\000\002\001\001\000\002\001\001\000\002\001\001"});

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
        HashMap<String, String> results= new HashMap<String,String>();
        parse();
        int j=0,k=0,a=0,b=0,c=0,d=0,f=0, m=0, n=0 , p=0, q=0;
        for(int i=0;i<result.size();i++){
            String singleResult=result.get(i);
            String[] temp = singleResult.split(" ");
            String secondElement=temp[2];
            if(temp.length>6){
                String fourthElement=temp [4];
                if (fourthElement.equalsIgnoreCase("UMP4(SDTQ)")) {
                    results.put("MP2to5d_UMP4(SDTQ)_" + k, temp[5]);
                    k++;
                }else if (fourthElement.equalsIgnoreCase("UMP4(SDQ)")) {
                    results.put("MP2to5d_UMP4(SDQ)_" + a, temp[5]);
                    a++;
                }else if(fourthElement.equalsIgnoreCase("UMP4(DQ)")) {
                    results.put("MP2to5d_UMP4(DQ)_" + b, temp[5]);
                    b++;
                }else if(fourthElement.equalsIgnoreCase("EUMP3")) {
                    results.put("MP2to5d_EUMP3_" + c, temp[5]);
                    c++;
                }else if (fourthElement.equalsIgnoreCase("EUMP2")) {
                    results.put("MP2to5d_EUMP2_" + q, temp[5]);
                    q++;
                }

            }

            if(secondElement.equalsIgnoreCase("E2")) {
                results.put("MP2to5d_E2_" + j, temp[3]);
                j++;
            }else  if(secondElement.equalsIgnoreCase("E4(SDTQ)")) {
                results.put("MP2to5d_E4(SDTQ)_" + d, temp[3]);
                d++;
            }else if (secondElement.equalsIgnoreCase("E4(SDQ)")) {
                results.put("MP2to5d_E4(SDQ)_" + f, temp[3]);
                f++;
            }else if (secondElement.equalsIgnoreCase("E4(DQ)")) {
                results.put("MP2to5d_E4(DQ)_" + m, temp[3]);
                m++;
            }else if (secondElement.equalsIgnoreCase("E3")) {
                results.put("MP2to5d_E3_" + n, temp[3]);
                n++;
            }else if (secondElement.equalsIgnoreCase("MP4(T)")) {
                results.put("MP2to5d_MP4(T)_" + p, temp[3]);
                p++;

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
 if(Settings.DEBUG) System.out.println("CUP:mp2to5d:  entry");
    }



    ///public static boolean DEBUG = false;
}

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$parser$actions {
  private final MP2to5dParser MP2to5dParser;

  /** Constructor */
  CUP$parser$actions(MP2to5dParser MP2to5dParser) {
    this.MP2to5dParser = MP2to5dParser;
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
          case 13: // element ::= UMP4_SDTQ FLOAT 
            {
              Object RESULT = null;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String f = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 if(Settings.DEBUG) System.out.println("CUP:mp2to5d:  MP4 correction UMP4(SDTQ)"+f);
         MP2to5dParser.addToResult("CUP:mp2to5d:  MP4 correction UMP4(SDTQ) "+f);
           //ParseMP2to5.put("MP4 correction UMP4(SDTQ)", f);
              CUP$parser$result = new java_cup.runtime.Symbol(2/*element*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // element ::= UMP4_SDQ FLOAT 
            {
              Object RESULT = null;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String f = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 if(Settings.DEBUG) System.out.println("CUP:mp2to5d:  MP4 correction UMP4(SDQ)"+f);
         MP2to5dParser.addToResult("CUP:mp2to5d:  MP4 correction UMP4(SDQ) "+f);
           //ParseMP2to5.put("MP4 correction UMP4(SDQ)", f);
              CUP$parser$result = new java_cup.runtime.Symbol(2/*element*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // element ::= UMP4_DQ FLOAT 
            {
              Object RESULT = null;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String f = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 if(Settings.DEBUG) System.out.println("CUP:mp2to5d:  MP4 correction UMP4(DQ)"+f);
         MP2to5dParser.addToResult("CUP:mp2to5d:  MP4 correction UMP4(DQ) "+f);
	  // ParseMP2to5.put("MP4 correction UMP4(DQ)", f);
              CUP$parser$result = new java_cup.runtime.Symbol(2/*element*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // element ::= EUMP3 FLOAT 
            {
              Object RESULT = null;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String f = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 if(Settings.DEBUG) System.out.println("CUP:mp2to5d:  MP3 correction EUMP3"+f);
         MP2to5dParser.addToResult("CUP:mp2to5d:  MP3 correction EUMP3 "+f);
	   //ParseMP2to5.put("MP3 correction EUMP3", f);
              CUP$parser$result = new java_cup.runtime.Symbol(2/*element*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // element ::= E4_SDTQ FLOAT 
            {
              Object RESULT = null;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String f = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 if(Settings.DEBUG) System.out.println("CUP:mp2to5d:  E4(SDTQ)"+f);
         MP2to5dParser.addToResult("CUP:mp2to5d:  E4(SDTQ) "+f);
	   //ParseMP2to5.put("E4(SDTQ)", f);
              CUP$parser$result = new java_cup.runtime.Symbol(2/*element*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // element ::= E4_SDQ FLOAT 
            {
              Object RESULT = null;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String f = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 if(Settings.DEBUG) System.out.println("CUP:mp2to5d:  E4(SDQ)"+f);
         MP2to5dParser.addToResult("CUP:mp2to5d:  E4(SDQ) "+f);
	  // ParseMP2to5.put("E4(SDQ)", f);
              CUP$parser$result = new java_cup.runtime.Symbol(2/*element*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // element ::= E4_DQ FLOAT 
            {
              Object RESULT = null;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String f = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 if(Settings.DEBUG) System.out.println("CUP:mp2to5d:  E4(DQ)"+f);
         MP2to5dParser.addToResult("CUP:mp2to5d:  E4(DQ) "+f);
	   //ParseMP2to5.put("E4(DQ)", f);
              CUP$parser$result = new java_cup.runtime.Symbol(2/*element*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // element ::= E3 FLOAT 
            {
              Object RESULT = null;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String f = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 if(Settings.DEBUG) System.out.println("CUP:mp2to5d:  E3 "+f);
         MP2to5dParser.addToResult("CUP:mp2to5d:  E3 "+f);
	   //ParseMP2to5.put("E3", f);
              CUP$parser$result = new java_cup.runtime.Symbol(2/*element*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // element ::= MP4_T FLOAT 
            {
              Object RESULT = null;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String f = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 if(Settings.DEBUG) System.out.println("CUP:mp2to5d:  MP4(T)"+f);
         MP2to5dParser.addToResult("CUP:mp2to5d:  MP4(T) "+f);
	   //ParseMP2to5.put("MP4(T)", f);
              CUP$parser$result = new java_cup.runtime.Symbol(2/*element*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // element ::= EUMP2 FLOAT 
            {
              Object RESULT = null;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String f = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 if(Settings.DEBUG) System.out.println("CUP:mp2to5d:  MP2 energy EUMP2"+f);
         MP2to5dParser.addToResult("CUP:mp2to5d:  MP2 energy EUMP2 "+f);
          // ParseMP2to5.put("MP2 energy EUMP2", f);
              CUP$parser$result = new java_cup.runtime.Symbol(2/*element*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // element ::= E2 FLOAT 
            {
              Object RESULT = null;
		int fleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int fright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String f = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 if(Settings.DEBUG) System.out.println("CUP:mp2to5d:  E2"+f);
         MP2to5dParser.addToResult("CUP:mp2to5d:  E2 "+f);
          // ParseMP2to5.put("E2", f);
              CUP$parser$result = new java_cup.runtime.Symbol(2/*element*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
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


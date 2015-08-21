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
package org.apache.airavata.datacat.parsers.gridchem.whichProgram;

import java_cup.runtime.lr_parser;

import javax.swing.*;


public class WhichProgramParser extends java_cup.runtime.lr_parser {

  /** Default constructor. */
  public WhichProgramParser() {super();}

  /** Constructor which sets the default scanner. */
  public WhichProgramParser(java_cup.runtime.Scanner s) {super(s);}

  /** Production table. */
  protected static final short _production_table[][] = 
    lr_parser.unpackFromStrings(new String[]{
            "\000\013\000\002\003\005\000\002\002\004\000\002\004" +
                    "\003\000\002\005\004\000\002\005\003\000\002\012\002" +
                    "\000\002\006\006\000\002\013\002\000\002\007\007\000" +
                    "\002\010\004\000\002\011\004"});

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    lr_parser.unpackFromStrings(new String[]{
            "\000\026\000\004\004\005\001\002\000\004\007\011\001" +
                    "\002\000\004\007\uffff\001\002\000\004\002\007\001\002" +
                    "\000\004\002\000\001\002\000\006\005\027\007\011\001" +
                    "\002\000\004\013\013\001\002\000\006\005\ufffd\007\ufffd" +
                    "\001\002\000\004\006\ufffc\001\002\000\004\006\015\001" +
                    "\002\000\004\012\017\001\002\000\006\005\ufffb\007\ufffb" +
                    "\001\002\000\004\010\ufffa\001\002\000\004\010\021\001" +
                    "\002\000\004\014\026\001\002\000\004\011\024\001\002" +
                    "\000\006\005\ufff9\007\ufff9\001\002\000\004\015\025\001" +
                    "\002\000\006\005\ufff7\007\ufff7\001\002\000\004\011\ufff8" +
                    "\001\002\000\004\002\001\001\002\000\006\005\ufffe\007" +
                    "\ufffe\001\002"});

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    lr_parser.unpackFromStrings(new String[]{
            "\000\026\000\006\003\005\004\003\001\001\000\006\005" +
                    "\007\006\011\001\001\000\002\001\001\000\002\001\001" +
                    "\000\002\001\001\000\004\006\027\001\001\000\002\001" +
                    "\001\000\002\001\001\000\004\012\013\001\001\000\004" +
                    "\007\015\001\001\000\002\001\001\000\002\001\001\000" +
                    "\004\013\017\001\001\000\004\010\021\001\001\000\002" +
                    "\001\001\000\004\011\022\001\001\000\002\001\001\000" +
                    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
                    "\001\001\000\002\001\001"});

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

  private final WhichProgramParser parser;

  /** Constructor */
  CUP$parser$actions(WhichProgramParser WhichProgramParser) {
    this.parser = WhichProgramParser;
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
          case 10: // grad2 ::= RmsGrad RGRAD 
            {
              Object RESULT = null;
		int rgleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int rgright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Float rg = (Float)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 //___________________________________________________________________
   if (DEBUG) System.out.println("CUP:gopt: RMS Force "+rg);
 
              CUP$parser$result = new java_cup.runtime.Symbol(7/*grad2*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // grad1 ::= MaxGrad MGRAD 
            {
              Object RESULT = null;
		int mgleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int mgright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Float mg = (Float)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 //___________________________________________________________________
   if (DEBUG) System.out.println("CUP:gopt: Maximum Force "+mg);
 
              CUP$parser$result = new java_cup.runtime.Symbol(6/*grad1*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // cycle ::= NSearch ITERATION NT$1 grad1 grad2 
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
 
              CUP$parser$result = new java_cup.runtime.Symbol(9/*NT$1*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // scfcycle ::= Energ ENERGY NT$0 cycle 
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
 
              CUP$parser$result = new java_cup.runtime.Symbol(8/*NT$0*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
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
          case 2: // scfintro ::= FOUNDITER 
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
          case 0: // startpt ::= scfintro scfpat SCFDONE 
            {
              Object RESULT = null;
		 if (DEBUG) System.out.println("CUP:gopt:  end of parse tree "); 
 	       table = new JTable();
	
//       table = parseSCF.getTable();
 	     
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


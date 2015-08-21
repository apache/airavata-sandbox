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
package org.apache.airavata.datacat.parsers.gridchem.gopt;


import java_cup.runtime.Symbol;
import java_cup.runtime.lr_parser;
import org.apache.airavata.datacat.parsers.gridchem.GridChemQueueParser;

import javax.swing.*;
import java.io.FileReader;
import java.util.*;


public class GoptParser extends java_cup.runtime.lr_parser implements GridChemQueueParser {

    /**
     * Default constructor.
     */
    public GoptParser() {
        super();
    }

    /**
     * Constructor which sets the default scanner.
     */
    public GoptParser(java_cup.runtime.Scanner s) {
        super(s);
    }

    /**
     * Constructor which uses a file reader.
     */
    public GoptParser(final FileReader fileReader) {
        super(new GOPTLexer(fileReader));
    }


    /**
     * Production table.
     */
    protected static final short _production_table[][] =
            lr_parser.unpackFromStrings(new String[]{
                    "\000\014\000\002\003\005\000\002\002\004\000\002\004" +
                            "\003\000\002\005\004\000\002\005\003\000\002\006\004" +
                            "\000\002\006\003\000\002\007\004\000\002\007\003\000" +
                            "\002\007\003\000\002\010\004\000\002\011\004"});

    /**
     * Access to production table.
     */
    public short[][] production_table() {
        return _production_table;
    }

    /**
     * Parse-action table.
     */
    protected static final short[][] _action_table =
            lr_parser.unpackFromStrings(new String[]{
                    "\000\024\000\004\004\005\001\002\000\012\006\016\007" +
                            "\011\010\012\011\010\001\002\000\012\006\uffff\007\uffff" +
                            "\010\uffff\011\uffff\001\002\000\004\002\007\001\002\000" +
                            "\004\002\000\001\002\000\004\015\026\001\002\000\004" +
                            "\013\025\001\002\000\004\014\024\001\002\000\014\005" +
                            "\ufffb\006\ufffb\007\ufffb\010\ufffb\011\ufffb\001\002\000\014" +
                            "\005\ufff8\006\ufff8\007\ufff8\010\ufff8\011\ufff8\001\002\000" +
                            "\014\005\ufff9\006\ufff9\007\ufff9\010\ufff9\011\ufff9\001\002" +
                            "\000\004\012\023\001\002\000\014\005\022\006\016\007" +
                            "\011\010\012\011\010\001\002\000\014\005\ufffd\006\ufffd" +
                            "\007\ufffd\010\ufffd\011\ufffd\001\002\000\014\005\ufffe\006" +
                            "\ufffe\007\ufffe\010\ufffe\011\ufffe\001\002\000\004\002\001" +
                            "\001\002\000\014\005\ufffa\006\ufffa\007\ufffa\010\ufffa\011" +
                            "\ufffa\001\002\000\014\005\ufff7\006\ufff7\007\ufff7\010\ufff7" +
                            "\011\ufff7\001\002\000\014\005\ufffc\006\ufffc\007\ufffc\010" +
                            "\ufffc\011\ufffc\001\002\000\014\005\ufff6\006\ufff6\007\ufff6" +
                            "\010\ufff6\011\ufff6\001\002"});

    /**
     * Access to parse-action table.
     */
    public short[][] action_table() {
        return _action_table;
    }

    /**
     * <code>reduce_goto</code> table.
     */
    protected static final short[][] _reduce_table =
            lr_parser.unpackFromStrings(new String[]{
                    "\000\024\000\006\003\005\004\003\001\001\000\014\005" +
                            "\016\006\017\007\012\010\014\011\013\001\001\000\002" +
                            "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
                            "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
                            "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
                            "\012\006\020\007\012\010\014\011\013\001\001\000\002" +
                            "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
                            "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
                            ""});

    /**
     * Access to <code>reduce_goto</code> table.
     */
    public short[][] reduce_table() {
        return _reduce_table;
    }

    /**
     * Instance of action encapsulation class.
     */
    protected CUP$parser$actions action_obj;

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

    /*Adding the parsed data to the hash map */
    public HashMap<String, String> getParsedData() throws Exception {
        parse();
        int result_size = result.size();
        int iteration = 0;
        String tempString = "";
        HashMap<String, String> results = new HashMap<String, String>();
        for (int i = 0; i < result_size; i++) {
            String singleResultString = result.get(i);
            String[] strs = result.get(i).split(" ");
            String secondElement = strs[2];
            if (strs.length < 2)
                continue;
            if (secondElement.equals("ENERGY")){
                results.put("GoptEnergy", strs[3]);
            }else if (secondElement.equals("ITERATION")) {
                results.put("Iteration_" + iteration+"_x", tempString);
                iteration++;
                tempString = "";
            } else if (strs[2] != null)
                tempString = tempString + "_" + singleResultString;
            else {
                continue;
            }


        }
        return results;
    }


    /**
     * Action encapsulation object initializer.
     */
    protected void init_actions() {
        action_obj = new CUP$parser$actions(this);
    }

    /**
     * Invoke a user supplied parse action.
     */
    public java_cup.runtime.Symbol do_action(
            int act_num,
            java_cup.runtime.lr_parser parser,
            java.util.Stack stack,
            int top)
            throws java.lang.Exception {
    /* call code in generated class */
        return action_obj.CUP$parser$do_action(act_num, parser, stack, top);
    }

    /**
     * Indicates start state.
     */
    public int start_state() {
        return 0;
    }

    /**
     * Indicates start production.
     */
    public int start_production() {
        return 1;
    }

    /**
     * <code>EOF</code> Symbol index.
     */
    public int EOF_sym() {
        return 0;
    }

    /**
     * <code>error</code> Symbol index.
     */
    public int error_sym() {
        return 1;
    }

}

/**
 * Cup generated class to encapsulate user supplied action code.
 */
class CUP$parser$actions {


    //__________________________________
    public static boolean DEBUG = false;
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

    private final GoptParser GoptParser;

    /**
     * Constructor
     */
    CUP$parser$actions(GoptParser GoptParser) {
        this.GoptParser = GoptParser;
    }

    /**
     * Method with the actual generated action code.
     */
    public final Symbol CUP$parser$do_action(
            int CUP$parser$act_num,
            lr_parser CUP$parser$parser,
            Stack CUP$parser$stack,
            int CUP$parser$top)
            throws Exception {
      /* Symbol object for return from actions */
        Symbol CUP$parser$result;

      /* select the action based on the action number */
        switch (CUP$parser$act_num) {
          /*. . . . . . . . . . . . . . . . . . . .*/
            case 11: // grad2 ::= RmsGrad RGRAD
            {
                Object RESULT = null;
                int rgleft = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                int rgright = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                Float rg = (Float) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                //___________________________________________________________________
                if (DEBUG) System.out.println("CUP:gopt: RMS Force " + rg);
                GoptParser.addToResult("CUP:gopt: RMS Force " + rg);

                CUP$parser$result = new Symbol(7/*grad2*/, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
            }
            return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
            case 10: // grad1 ::= MaxGrad MGRAD
            {
                Object RESULT = null;
                int mgleft = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                int mgright = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                Float mg = (Float) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                //___________________________________________________________________
                if (DEBUG) System.out.println("CUP:gopt: Maximum Force " + mg);
                GoptParser.addToResult("CUP:gopt: Maximum Force " + mg);


                CUP$parser$result = new Symbol(6/*grad1*/, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
            }
            return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
            case 9: // cycle ::= grad2
            {
                Object RESULT = null;

                CUP$parser$result = new Symbol(5/*cycle*/, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
            }
            return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
            case 8: // cycle ::= grad1
            {
                Object RESULT = null;

                CUP$parser$result = new Symbol(5/*cycle*/, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
            }
            return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
            case 7: // cycle ::= NSearch ITERATION
            {
                Object RESULT = null;
                int cleft = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                int cright = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                Integer c = (Integer) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                //___________________________________________________________________
                if (DEBUG) System.out.println("CUP:gopt:  ITERATION " + c);
                GoptParser.addToResult("CUP:gopt:  ITERATION " + c);


                CUP$parser$result = new Symbol(5/*cycle*/, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
            }
            return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
            case 6: // scfcycle ::= cycle
            {
                Object RESULT = null;

                CUP$parser$result = new Symbol(4/*scfcycle*/, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
            }
            return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
            case 5: // scfcycle ::= Energ ENERGY
            {
                Object RESULT = null;
                int eleft = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left;
                int eright = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right;
                Float e = (Float) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).value;
                //___________________________________________________________________
                if (DEBUG) System.out.println("CUP:gopt:  ENERGY " + e);
                GoptParser.addToResult("CUP:gopt:  ENERGY " + e);


                CUP$parser$result = new Symbol(4/*scfcycle*/, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
            }
            return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
            case 4: // scfpat ::= scfcycle
            {
                Object RESULT = null;

                CUP$parser$result = new Symbol(3/*scfpat*/, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
            }
            return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
            case 3: // scfpat ::= scfpat scfcycle
            {
                Object RESULT = null;
                if (DEBUG) System.out.println("CUP:gopt: in scfpat");
                //GoptParser.addToResult("CUP:gopt: in scfpat");

                CUP$parser$result = new Symbol(3/*scfpat*/, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
            }
            return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
            case 2: // scfintro ::= FOUNDITER
            {
                Object RESULT = null;
                if (DEBUG) System.out.println("CUP:gopt:  found the start of Iteration");
                GoptParser.addToResult("CUP:gopt:  found the start of Iteration");

                CUP$parser$result = new Symbol(2/*scfintro*/, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).left, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
            }
            return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
            case 1: // $START ::= startpt EOF
            {
                Object RESULT = null;
                int start_valleft = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
                int start_valright = ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
                Object start_val = (Object) ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
                RESULT = start_val;
                CUP$parser$result = new Symbol(0/*$START*/, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 1)).left, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
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

                CUP$parser$result = new Symbol(1/*startpt*/, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 2)).left, ((Symbol) CUP$parser$stack.elementAt(CUP$parser$top - 0)).right, RESULT);
            }
            return CUP$parser$result;

          /* . . . . . .*/
            default:
                throw new Exception(
                        "Invalid action number found in internal parse table");

        }
    }
}


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
package org.apache.airavata.datacat.parsers.gridchem.inchi;

import org.apache.airavata.datacat.parsers.gridchem.util.Constants;
import org.apache.airavata.datacat.parsers.gridchem.util.GridChemProperties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InChIGenerator {

    private String filePrefix;

    public InChIGenerator(String filePrefix) {
        this.filePrefix = filePrefix;
    }

    private BufferedReader runInChI(String molfile) throws IOException {
        String[] command = {
                "/bin/sh",
                "-c",
                "echo \"" + molfile + "\" | ./inchi-1 -STDIO -AuxNone -NoLabels -Key 2>/dev/null"
        };
        Process inchi = Runtime.getRuntime().exec(command);
        try {
            inchi.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new BufferedReader(new InputStreamReader(inchi.getInputStream()));
    }

    public InChI getInChI() throws IOException {
        convertGaussian2Mol();
        String molfile = readFile(GridChemProperties.getInstance().getProperty(
                Constants.TMP_DIR,"") + "/" + filePrefix + ".mol");
        BufferedReader input = runInChI(molfile);

        String result = input.readLine();
        String inchiStr = result == null ? "" : result;

        result = input.readLine();
        String inchiKey = result == null ? "" : result.substring(9);
        InChI inChI = new InChI(inchiStr, inchiKey);
        input.close();

        return inChI;
    }

    private void convertGaussian2Mol() throws IOException {
        Process inchi = Runtime.getRuntime().exec("babel -i g09 "
                + GridChemProperties.getInstance().getProperty(Constants.TMP_DIR,"") + "/" + filePrefix + ".out -o mol "
                + GridChemProperties.getInstance().getProperty(Constants.TMP_DIR,"") + "/" + filePrefix + ".mol\n");
        try {
            inchi.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String readFile(String filename) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String str = "";
        String molfile = "";

        while ((str = in.readLine()) != null) {
            molfile = molfile + str + "\n";
        }

        in.close();

        return molfile;
    }
}

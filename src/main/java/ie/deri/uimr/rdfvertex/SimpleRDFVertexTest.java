/*
* Copyright ©2010-2012, Digital Enterprise Research Institute (DERI), NUI Galway, http://www.deri.ie
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*     * Redistributions of source code must retain the above copyright
*       notice, this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright
*       notice, this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*     * Neither the name of DERI nor the names of its contributors may be
*       used to endorse or promote products derived from this software without
*       specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
* ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL DERI BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package ie.deri.uimr.rdfvertex;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.giraph.utils.InternalVertexRunner;

import org.apache.log4j.Logger;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;



import junit.framework.TestCase;

public class SimpleRDFVertexTest extends TestCase {

//	final Logger l = LoggerFactory.getLogger(SpreadingActivationVertexTest.class);

	public static final Logger LOG = Logger.getLogger(SimpleRDFVertexTest.class);
	
	final String textInputFile = "data/futurama_adjacency.csv";
	
	public void testSmallProfile() throws Exception {
		
//		File zeFile = new File(textInputFile);
//		
//		LineIterator it = FileUtils.lineIterator(zeFile, "UTF-8");
//		int counter = 0;
//		try {
//			while (it.hasNext()) {
//				String line = it.nextLine();
//				counter++;
//				/// do something with line
//			}
//		} finally {
//			LineIterator.closeQuietly(it);
//		}
//		
//		assertTrue(counter==2);
		
		String textInput = FileUtils.readFileToString(new File(textInputFile));
		
//		int counter = StringUtils.countMatches(textInput, "\n");
//		assertTrue(counter==1);
		
//		System.out.println(textInput);
		
//		if (LOG.isInfoEnabled()) {
//			LOG.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% just a test of teh logging system");
//		}

		
        Iterable<String> results = InternalVertexRunner.run(
                SimpleRDFVertex.class,
                null, // do not use a combiner for the messages between nodes
                SimpleRDFVertexInputFormat.class,
                SimpleRDFVertexOutputFormat.class,
                Maps.<String,String>newHashMap(), textInput);
        
        
    	FileWriter file = new FileWriter("target/giraph-output.csv");
        PrintWriter printer = new PrintWriter(file);
        long lineCounter = 0;
        for (String line : results) {
        	printer.println(line);
        	lineCounter++;
        }
        assertTrue(lineCounter > 0);
        printer.close();
        file.close();


	}
}

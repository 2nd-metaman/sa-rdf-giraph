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


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.giraph.graph.BasicVertex;
import org.apache.giraph.graph.BspUtils;
import org.apache.giraph.graph.VertexReader;
import org.apache.giraph.lib.TextVertexInputFormat;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import com.google.common.collect.Lists;

public class SimpleRDFVertexInputFormat extends
		TextVertexInputFormat<Text, Text, Text, NullWritable> {

	@Override
	public VertexReader<Text, Text, Text, NullWritable> createVertexReader(
			InputSplit split, TaskAttemptContext context) throws IOException {
		return new SimpleRDFVertexReader(textInputFormat.createRecordReader(split, context));
	}	
	
	public class SimpleRDFVertexReader extends TextVertexInputFormat.TextVertexReader<Text, Text, Text, NullWritable> {

	    /** Separator of the vertex and neighbors */
	    private final Pattern SEPARATOR = Pattern.compile("[\t ]");

		
		public SimpleRDFVertexReader(
				RecordReader<LongWritable, Text> lineRecordReader) {
			super(lineRecordReader);
		}

		@Override
		public boolean nextVertex() throws IOException, InterruptedException {
			return getRecordReader().nextKeyValue();
		}

		
		@Override
		public BasicVertex<Text, Text, Text, NullWritable> getCurrentVertex()
				throws IOException, InterruptedException {
			
			BasicVertex<Text, Text, Text, NullWritable> 
			vertex = BspUtils.<Text, Text, Text, NullWritable>createVertex(getContext().getConfiguration());

		    String[] tokens = SEPARATOR.split(getRecordReader().getCurrentValue().toString());
		    
			// we load the following format (VERSION 2)
			// field seperator: \t
			// subjectURI edgeCount isStartNode predicate1 object1 ... predicateN objectN

		    Text vertexURI = new Text(tokens[0]);

		    Text vertexValue = new Text("");
		    
		    // dont do anything with the edgeCount right now (tokens[1])	    
		    // dont do anything with isStartNode right now (tokens[2])
		    
		    // edges maps objectURI to predicateURI
		    Map<Text, Text> edges = new HashMap<Text, Text>();
		    if (tokens.length > 2 ) {
				for (int n = 3; n < tokens.length; n = n + 2) {
					edges.put(new Text(tokens[n]), new Text(tokens[n+1]));
				}
		    } else {
		    	// pass an empty list of edges to vertex.initialize()
		    }
		    
		    vertex.initialize(vertexURI, vertexValue, edges, Lists.<NullWritable>newArrayList());
		    
		    return vertex;
		}

	}


}

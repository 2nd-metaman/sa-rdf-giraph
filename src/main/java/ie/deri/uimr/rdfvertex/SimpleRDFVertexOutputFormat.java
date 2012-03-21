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

import org.apache.giraph.graph.BasicVertex;
import org.apache.giraph.graph.VertexWriter;
import org.apache.giraph.lib.TextVertexOutputFormat;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class SimpleRDFVertexOutputFormat extends
		TextVertexOutputFormat<Text, Text, Text> {

	@Override
	public VertexWriter<Text, Text, Text> createVertexWriter(
			TaskAttemptContext context) throws IOException,
			InterruptedException {
		
		RecordWriter<Text, Text> recordWriter = textOutputFormat.getRecordWriter(context);
		return new SimpleRDFVertexWriter(recordWriter);
	}
	
	public class SimpleRDFVertexWriter extends
	TextVertexOutputFormat.TextVertexWriter<Text, Text, Text> {

		public SimpleRDFVertexWriter(RecordWriter<Text, Text> lineRecordWriter) {
			super(lineRecordWriter);
		}

		@Override
		public void writeVertex(BasicVertex<Text, Text, Text, ?> vertex)
				throws IOException, InterruptedException {
			StringBuilder output = new StringBuilder();
			
			// URI of the node
			output.append(vertex.getVertexId().toString());
			output.append(", ");

			getRecordWriter().write(new Text(output.toString()), null);
		}

	}

}

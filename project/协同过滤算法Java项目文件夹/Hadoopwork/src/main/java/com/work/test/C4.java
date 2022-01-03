package com.work.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class C4 {
	public static class C4Mapper extends Mapper< LongWritable, Text, Text, Text>{

		String filename;
		@Override
		protected void setup(Context context) throws IOException,InterruptedException {
			// TODO Auto-generated method stub
			InputSplit input = context.getInputSplit();
			filename = ((FileSplit) input).getPath().getParent().getName();
//			System.out.println("FileName：" +filename);
		}
		/*
		 * C2_1:<0       博客ID:博客ID 同时对两个博客发生行为的总人数>
		 * C3:<0       博客ID 用户ID:评分>
		 */
		@Override
		protected void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException {
			if (filename.equals("c2_1output")) {
				String line1 = value.toString();
				String[] list1 = line1.split("\t");
				//list1[0] 101:101
				//list1[1] 5 
				String[] one1 = list1[0].split(":");
				//one1[0] 101
				//one1[1] 101
				Text key1 = new Text(one1[0]);
				Text value1 = new Text("A:" + one1[1] +"," +list1[1]);
				context.write(key1,value1);
				System.out.println(key1+" "+value1);
			}
			else {
				String line = value.toString();
				String[] list = line.split("\t");
				//list[0] 101
				//list[1] 1:5.0 
//				System.out.println(list[0]);
				String[] one = list[1].split(":");
				//one[0] 100850003
				//one[1] 5.0
				Text key2 = new Text(list[0]);
				Text value2 = new Text("B:" + one[0] + "," + one[1]);
				context.write(key2,value2);
				System.out.println(key2+" "+value2);
			}
		}	
	}
	/*
	 * <博客ID A:博客ID,同时对两个博客发生行为的总人数>
	 * <博客ID B:用户ID,评分>
	 */
	//reduce阶段，键相同的数据作为一个集合传入
	public static class C4Reducer extends Reducer<Text,Text,Text,Text>{
		static Text result = new Text();
		protected void reduce(Text key,Iterable<Text> values,Context context) throws IOException, InterruptedException {
			Map<String, String> mapA = new HashMap<String, String>();
			Map<String, String> mapB = new HashMap<String, String>();
			
			for(Text line : values){
				String val = line.toString();
				if(val.startsWith("A")){
					
					String[] kv = val.substring(2).split(",");
					mapA.put(kv[0], kv[1]); //ItemID, num
				}else if(val.startsWith("B")){
					
					String[] kv = val.substring(2).split(",");
					mapB.put(kv[0], kv[1]); //userID, score
				}
			}
			
			double result = 0;
//			Iterator是迭代器类
			Iterator<String> iterA = mapA.keySet().iterator();
			while(iterA.hasNext()){
				String mapkA = (String) iterA.next();  //itemID
				int num = Integer.parseInt((String) mapA.get(mapkA));     // num
				Iterator<String> iterB = mapB.keySet().iterator();
				while(iterB.hasNext()){
					String mapkB = (String)iterB.next(); //UserID
					double score = Double.parseDouble((String) mapB.get(mapkB));  //score
					result = num * score; //矩阵乘法结果
					
					Text key2 = new Text(mapkB);
					Text value2 = new Text(mapkA + "," +result);
					context.write(key2,value2); //userID \t  itemID,result
					System.out.println(key2+ "\t" + value2);
				}
			}
		}
	}
	/*
	 * <用户ID 推荐的博客ID,推荐分数>
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
		conf.set("mapreduce.framework.name", "local");
		Job c4job = Job.getInstance(conf);
		
		c4job.setJarByClass(C4.class);
		
		c4job.setMapperClass(C4Mapper.class);
		c4job.setReducerClass(C4Reducer.class);
		
		c4job.setMapOutputKeyClass(Text.class);
		c4job.setMapOutputValueClass(Text.class);

		c4job.setOutputKeyClass(Text.class);
		c4job.setOutputValueClass(Text.class);
		
//		FileInputFormat.setInputPaths(c4job, new Path(args[0]),new Path(args[1]));
//		FileOutputFormat.setOutputPath(c4job, new Path(args[2]));
		FileInputFormat.setInputPaths(c4job, new Path("C:\\Users\\lenovo\\Desktop\\hadoopbigwork\\c2_1output"),
				new Path("C:\\Users\\lenovo\\Desktop\\hadoopbigwork\\c3output"));
		FileOutputFormat.setOutputPath(c4job, new Path("C:\\Users\\lenovo\\Desktop\\hadoopbigwork\\c4output"));

		boolean res = c4job.waitForCompletion(true);
		System.exit(res ? 0 : 1);
	}

}

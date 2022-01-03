package com.work.test;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class C4_1 {
	public static class C4_1Mapper extends Mapper<LongWritable, Text, Text, Text>{
		/*
		 * <0          用户ID 推荐的博客ID,推荐分数>
		 */
		@Override
		protected void map(LongWritable key, Text value, Context context)throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line1 = value.toString();
			String[] list = line1.split("\t");
			//list[1] 101,25.0
			String[] list1 = list[1].split(",");
			Text key1 = new Text(list1[0]+ "," +list[0]);//userID
			Text value1 = new Text(list1[1]);
			context.write(key1, value1);    //itemID,result
			System.out.println(key1+"\t"+value1);
		}
		
	}
	/*
	 * <推荐的博客ID,用户ID        推荐分数>
	 */
	public static class C4_1Reducer extends Reducer< Text, Text, Text, Text>{

		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context)throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			float num = 0;
			for (Text value:values) {
				Double result = Double.parseDouble(value.toString());
				num += result;
//				System.out.println(key+":"+value);
			}
			String[] oneList = key.toString().split(",");
			//oneList[0] 101
			//oneList[1] 1
			Text k1 = new Text(oneList[1]);
			Text v1 = new Text(oneList[0]+"\t"+String.valueOf(num));
			context.write(k1, v1);
			System.out.println(k1+"\t"+v1);
		}
		
	}
	/*
	 * <用户ID       推荐的博客ID 推荐分数>
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
		conf.set("mapreduce.framework.name", "local");
		Job c4_1job = Job.getInstance(conf);
		
		c4_1job.setJarByClass(C4_1.class);
		
		c4_1job.setMapperClass(C4_1Mapper.class);
		c4_1job.setReducerClass(C4_1Reducer.class);
		
		c4_1job.setMapOutputKeyClass(Text.class);
		c4_1job.setMapOutputValueClass(Text.class);

		c4_1job.setOutputKeyClass(Text.class);
		c4_1job.setOutputValueClass(Text.class);
		
//		FileInputFormat.setInputPaths(c4_1job, new Path(args[0]));
//		FileOutputFormat.setOutputPath(c4_1job, new Path(args[1]));
		FileInputFormat.setInputPaths(c4_1job, new Path("C:\\Users\\lenovo\\Desktop\\hadoopbigwork\\c4output"));
		FileOutputFormat.setOutputPath(c4_1job, new Path("C:\\Users\\lenovo\\Desktop\\hadoopbigwork\\resultoutput"));

		boolean res = c4_1job.waitForCompletion(true);
		System.exit(res ? 0 : 1);
	}
}

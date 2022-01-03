package com.work.test;


import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class C2_1 {
	/*
	 * <0         用户ID 博客ID:博客ID;博客ID:博客ID;博客ID:博客ID>
	 */
	//map阶段
	public static class C2Mapper extends Mapper<LongWritable,Text,Text,Text>{
		static Text k1 = new Text();
		static Text v1 = new Text();
		protected void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException {
			String line = value.toString();
			String[] list = line.split("\t");
//			System.out.println(list[0]+" "+list[1]);
			String[] list1 = list[1].split(";");
			for (String one:list1) {
				k1.set(one);
				v1.set("1");
//				System.out.println(one+"_"+v1);
				context.write(k1, v1);
				System.out.println(k1+" "+v1);
			}
		}
	}
	/*
	 * <博客ID:博客ID      1>
	 */
	//reduce阶段
	public static class C2Reducer extends Reducer<Text,Text,Text,IntWritable>{
		static Text result = new Text();
		protected void reduce(Text key,Iterable<Text> values,Context context) throws IOException, InterruptedException {
			int count = 0;
			for (@SuppressWarnings("unused") Text value:values) {
				count += 1;
			}
			context.write(key, new IntWritable(count));
			System.out.println(key+"\t"+new IntWritable(count));
		}
	}
	/*
	 * <博客ID:博客ID      同时对两个博客发生行为的总人数>
	 */
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		conf.set("mapreduce.framework.name", "local");
		Job c2job = Job.getInstance(conf);
		
		c2job.setJarByClass(C2_1.class);
		
		c2job.setMapperClass(C2Mapper.class);
		c2job.setReducerClass(C2Reducer.class);
		
		c2job.setMapOutputKeyClass(Text.class);
		c2job.setMapOutputValueClass(Text.class);

		c2job.setOutputKeyClass(Text.class);
		c2job.setOutputValueClass(IntWritable.class);
		
//		FileInputFormat.setInputPaths(c2job, new Path(args[0]));
//		FileOutputFormat.setOutputPath(c2job, new Path(args[1]));
		FileInputFormat.setInputPaths(c2job, "C:\\Users\\lenovo\\Desktop\\hadoopbigwork\\c2output");
		FileOutputFormat.setOutputPath(c2job, new Path("C:\\Users\\lenovo\\Desktop\\hadoopbigwork\\c2_1output"));
		
		boolean res = c2job.waitForCompletion(true);
		System.exit(res ? 0 : 1);
	}

}
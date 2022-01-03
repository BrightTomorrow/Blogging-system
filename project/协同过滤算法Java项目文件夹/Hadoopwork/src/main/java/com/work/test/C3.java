package com.work.test;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class C3 {
	/*
	 * <0                 用户ID 博客ID:评分,博客ID:评分,博客ID:评分>
	 */
	//map阶段
	public static class C3Mapper extends Mapper<LongWritable,Text,Text,Text>{
		static Text k1 = new Text();
		static Text v1 = new Text();
		protected void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException {
			String line = value.toString();
			String[] list = line.split("\t");
			String[] list1 = list[1].split(",");
			for (String one:list1) {
				String[] oneList = one.split(":");
				k1.set(oneList[0]);
				v1.set(list[0]+":"+oneList[1]);
				context.write(k1, v1);
				System.out.println(k1+" "+v1);
			}
		}
	}
	/*
	 * <博客ID                 用户ID:评分>
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		conf.set("mapreduce.framework.name", "local");
		Job c3job = Job.getInstance(conf);
		
		c3job.setJarByClass(C3.class);
		
		c3job.setMapperClass(C3Mapper.class);
//		c3job.setReducerClass(C3Reducer.class);
		
		c3job.setMapOutputKeyClass(Text.class);
		c3job.setMapOutputValueClass(Text.class);

		c3job.setOutputKeyClass(Text.class);
		c3job.setOutputValueClass(Text.class);
		
//		FileInputFormat.setInputPaths(c3job, new Path(args[0]));
//		FileOutputFormat.setOutputPath(c3job, new Path(args[1]));
		FileInputFormat.setInputPaths(c3job, "C:\\Users\\lenovo\\Desktop\\hadoopbigwork\\c1output");
		FileOutputFormat.setOutputPath(c3job, new Path("C:\\Users\\lenovo\\Desktop\\hadoopbigwork\\c3output"));
		
		boolean res = c3job.waitForCompletion(true);
		System.exit(res ? 0 : 1);
	}

}


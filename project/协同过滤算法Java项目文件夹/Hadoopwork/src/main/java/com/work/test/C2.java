package com.work.test;


import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class C2 {
	/*
	 * <0            用户ID 博客ID 评分>
	 */
	//map阶段
	public static class C2Mapper extends Mapper<LongWritable,Text,Text,Text>{
		static Text k1 = new Text();
		static Text v1 = new Text();
		protected void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException {
			String line = value.toString();
			String[] list = line.split("\t");
			k1.set(list[0]);
			v1.set(list[1]);
			context.write(k1, v1);
			System.out.println(k1+":"+v1);
		}
	}
	/*
	 * <用户ID              博客ID>
	 */
	//reduce阶段，键相同的数据作为一个集合传入
	public static class C2Reducer extends Reducer<Text,Text,Text,Text>{
		static Text result = new Text();
		protected void reduce(Text key,Iterable<Text> values,Context context) throws IOException, InterruptedException {
			ArrayList<String> c2List = new ArrayList<String>();
			for (Text value:values) {
				String value1 = value.toString();
				c2List.add(value1);
			}
			String one = new String();
			while (true) {
				for (String c2one:c2List) {
					if (c2List.get(0)!=c2one)
						one+=c2List.get(0)+":"+c2one+";"+c2one+":"+c2List.get(0)+";";
					else
						one+=c2List.get(0)+":"+c2one+";";
				}
				if (c2List!=null)
					c2List.remove(0);
				
				if (c2List.size()==0)
					break;
			}
//			System.out.println(one);
			result.set(one);
			context.write(key, result);
			System.out.println(key+"\t"+result);
		}
	}
	/*
	 * <用户ID           博客ID:博客ID;博客ID:博客ID;博客ID:博客ID>
	 */
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		conf.set("mapreduce.framework.name", "local");
		Job c2job = Job.getInstance(conf);
		
		c2job.setJarByClass(C2.class);
		
		c2job.setMapperClass(C2Mapper.class);
		c2job.setReducerClass(C2Reducer.class);
		
		c2job.setMapOutputKeyClass(Text.class);
		c2job.setMapOutputValueClass(Text.class);

		c2job.setOutputKeyClass(Text.class);
		c2job.setOutputValueClass(Text.class);
//		FileInputFormat.setInputPaths(c2job, new Path(args[0]));
//		FileOutputFormat.setOutputPath(c2job, new Path(args[1]));
		FileInputFormat.setInputPaths(c2job, "C:\\Users\\lenovo\\Desktop\\hadoopbigwork\\input");
		FileOutputFormat.setOutputPath(c2job, new Path("C:\\Users\\lenovo\\Desktop\\hadoopbigwork\\c2output"));
		
		boolean res = c2job.waitForCompletion(true);
		System.exit(res ? 0 : 1);
	}

}

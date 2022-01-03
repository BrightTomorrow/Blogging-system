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

public class C1 {
	/*
	 * <0             用户ID 博客ID 评分>
	 */
	//map阶段
	public static class  C1Mapper extends Mapper<LongWritable,Text,Text,Text>{
		static Text k1 = new Text(); //key
		static Text v1 = new Text();//value
		@Override
		protected void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException {
			String line=value.toString();
			String[] ones=line.split("\t");
			k1.set(ones[0]);
			v1.set(ones[1]+":"+ones[2]);
			context.write(k1,v1);
			System.out.println(k1+" "+v1);
		}
	}
	/*
	 * <用户ID           博客ID:评分>
	 */
	//reduce阶段
	public static class  C1Reducer extends Reducer<Text,Text,Text,Text>{
		static Text result = new Text();
		protected void reduce(Text key,Iterable<Text> values,Context context) throws IOException, InterruptedException {
			String c1List = new String();
//			System.out.println(key);
			for (Text value:values) {
				c1List += value.toString()+",";
			}
			result.set(c1List);
			context.write(key, result);
			System.out.println(key+" "+result);
		}
	}
	/*
	 * <用户ID        博客ID:评分,博客ID:评分,博客ID:评分>
	 */

	public static void main(String[] args) throws Exception {
		// 通过Job来封装本次MR的相关信息
		Configuration conf = new Configuration();
		conf.set("mapreduce.framework.name", "local");
		Job c1job = Job.getInstance(conf);
		// 指定MR Job jar包运行主类
		c1job.setJarByClass(C1.class);
		// 指定本次MR所有的Mapper Reducer类
		c1job.setMapperClass(C1Mapper.class);
		c1job.setReducerClass(C1Reducer.class);

//		// 设置我们的业务逻辑 Mapper类的输出 key和 value的数据类型
		c1job.setMapOutputKeyClass(Text.class);
		c1job.setMapOutputValueClass(Text.class);
//
//		// 设置我们的业务逻辑 Reducer类的输出 key和 value的数据类型
		c1job.setOutputKeyClass(Text.class);
		c1job.setOutputValueClass(Text.class);
		
		//设置Combiner组件
//		wcjob.setCombinerClass(WordCountCombiner.class);

		// 指定要处理的数据所在的位置
//		FileInputFormat.setInputPaths(c1job, new Path(args[0]));
		FileInputFormat.setInputPaths(c1job, "C:\\Users\\lenovo\\Desktop\\hadoopbigwork\\input");
//		// 指定处理完成之后的结果所保存的位置
//		FileOutputFormat.setOutputPath(c1job, new Path(args[1]));
		FileOutputFormat.setOutputPath(c1job, new Path("C:\\Users\\lenovo\\Desktop\\hadoopbigwork\\c1output"));

		// 提交程序并且监控打印程序执行情况
		boolean res = c1job.waitForCompletion(true);
		System.exit(res ? 0 : 1);
	}
}

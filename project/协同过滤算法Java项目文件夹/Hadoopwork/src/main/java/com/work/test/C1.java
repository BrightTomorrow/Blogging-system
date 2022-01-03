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
	 * <0             �û�ID ����ID ����>
	 */
	//map�׶�
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
	 * <�û�ID           ����ID:����>
	 */
	//reduce�׶�
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
	 * <�û�ID        ����ID:����,����ID:����,����ID:����>
	 */

	public static void main(String[] args) throws Exception {
		// ͨ��Job����װ����MR�������Ϣ
		Configuration conf = new Configuration();
		conf.set("mapreduce.framework.name", "local");
		Job c1job = Job.getInstance(conf);
		// ָ��MR Job jar����������
		c1job.setJarByClass(C1.class);
		// ָ������MR���е�Mapper Reducer��
		c1job.setMapperClass(C1Mapper.class);
		c1job.setReducerClass(C1Reducer.class);

//		// �������ǵ�ҵ���߼� Mapper������ key�� value����������
		c1job.setMapOutputKeyClass(Text.class);
		c1job.setMapOutputValueClass(Text.class);
//
//		// �������ǵ�ҵ���߼� Reducer������ key�� value����������
		c1job.setOutputKeyClass(Text.class);
		c1job.setOutputValueClass(Text.class);
		
		//����Combiner���
//		wcjob.setCombinerClass(WordCountCombiner.class);

		// ָ��Ҫ������������ڵ�λ��
//		FileInputFormat.setInputPaths(c1job, new Path(args[0]));
		FileInputFormat.setInputPaths(c1job, "C:\\Users\\lenovo\\Desktop\\hadoopbigwork\\input");
//		// ָ���������֮��Ľ���������λ��
//		FileOutputFormat.setOutputPath(c1job, new Path(args[1]));
		FileOutputFormat.setOutputPath(c1job, new Path("C:\\Users\\lenovo\\Desktop\\hadoopbigwork\\c1output"));

		// �ύ�����Ҽ�ش�ӡ����ִ�����
		boolean res = c1job.waitForCompletion(true);
		System.exit(res ? 0 : 1);
	}
}

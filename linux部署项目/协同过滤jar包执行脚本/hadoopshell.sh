#!/bin/bash
#必须导入环境变量，否则Hadoop命令无法执行
source /etc/profile
#Hadoop大作业在HDFS中的工作路径
work_dir=/hadoopbigwork/
#打分表存放地址
score_dir=/hadoopwork/score/
score_file=scoredata.txt
#待上传文件HDFS路径
score_dfs_dir=/hadoopbigwork/score/
#mapreduce的jar包执行结果输出路径本地
maybe_text=/hadoopworkmybe/maybe/
#jar包保存路径
jarpath=/hadoopwork/jars/
#判断打分目录是否存在，不存在创建
if [ ! -d  $score_dir ]
then
        mkdir -p $score_dir
        echo -e "this is $score_dir success ! "
else
        echo -e "directory already exists !"
      #清空/export/score/下的打分文件
        rm -rf $score_dir
		mkdir -p $score_dir
fi
# text表提取用户id、博客id、打分（点赞*0.4+收藏*0.6）
mysql -uroot -p123456 boke -N -e "select user_id,text_id,text_good*0.4+text_save*0.6 score from behavior;">$score_dir$score_file
#删除HDFS下的/score/目录，创建空白的/score/
#Hadoop离开安全状态
hdfs dfsadmin -safemode leave
hdfs dfs -rm -r -f $work_dir
hdfs dfs -mkdir -p $score_dfs_dir
#将打分表上传到HDFS下的/score/路径
hdfs dfs -put $score_dir$score_file $score_dfs_dir
#执行协同过滤的jar包
hadoop jar $jarpath"ItemCF.jar" com.work.test.C1 $score_dfs_dir $work_dir"c1output"
hadoop jar $jarpath"ItemCF.jar" com.work.test.C2 $score_dfs_dir $work_dir"c2output"
hadoop jar $jarpath"ItemCF.jar" com.work.test.C2_1 $work_dir"c2output" $work_dir"c2_1output"
hadoop jar $jarpath"ItemCF.jar" com.work.test.C3 $work_dir"c1output" $work_dir"c3output"
hadoop jar $jarpath"ItemCF.jar" com.work.test.C4 $work_dir"c2_1output" $work_dir"c3output" $work_dir"c4output"
hadoop jar $jarpath"ItemCF.jar" com.work.test.C4_1 $work_dir"c4output" $work_dir"resultoutput"
#判断是否存在本地的/hadoopwork/maybe/目录，没有则创，有则清空目录下的文件
if [ ! -d $maybe_text ]
then
    mkdir -p $maybe_text
    echo -e "this is $maybe_text success ! "
else
    echo -e "directory already exists !"
    #清空/export/score/下的打分文件
    rm -rf $maybe_text
	mkdir -p $maybe_text
fi
#将结果文件从HDFS中导出到本地
hdfs dfs -get $work_dir"resultoutput/part-r-00000" $maybe_text
#如果表不存在，则创建
mysql -uroot -p123456 --local-infile boke -N -e "
CREATE TABLE if not exists item_cf (user_id varchar(20) NOT NULL COMMENT '用户ID',
text_id varchar(20) NOT NULL COMMENT '博文ID',
score float NOT NULL COMMENT '分数')
DEFAULT CHARSET=utf8;"
#清空推荐表
mysql -uroot -p123456 --local-infile boke -N -e "truncate table item_cf;"
#将结果汇总到MySQL中
mysql -uroot -p123456 --local-infile boke -N -e "set global local_infile=1;load data local infile'$maybe_text"part-r-00000"'
into table item_cf fields terminated by '\t' 
lines terminated by '\n';"

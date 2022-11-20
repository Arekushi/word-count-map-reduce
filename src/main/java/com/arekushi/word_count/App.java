package com.arekushi.word_count;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class App {
    public static class TextArrayWritable extends ArrayWritable {
        public TextArrayWritable() {
            super(Text.class);
        }

        public TextArrayWritable(String[] strings) {
            super(Text.class);

            Text[] texts = new Text[strings.length];

            for (int i = 0; i < strings.length; i++) {
                texts[i] = new Text(strings[i]);
            }

            set(texts);
        }
    }

    public static class Map extends Mapper<LongWritable, Text, Text, ArrayWritable> {
        public void map(
                LongWritable key,
                Text value,
                Context context
        ) throws IOException, InterruptedException {
            String[] lineParts = value.toString().split(":::");
            String title = lineParts[2];
            String authorsString = lineParts[1];

            String[] authors = authorsString.split("::");
            List<String> words = new ArrayList<String>();
            StringTokenizer tokenizer = new StringTokenizer(title);

            while (tokenizer.hasMoreTokens()) {
                value.set(StringUtils.stripAccents(tokenizer.nextToken()));
                words.add(value.toString().toLowerCase());
            }

            for (String author : authors) {
                String[] arrayWords = words.stream().toArray(String[]::new);
                context.write(new Text(author), new TextArrayWritable(arrayWords));
            }
        }
    }

    public static class Reduce extends Reducer<Text, TextArrayWritable, NullWritable, Text> {

        private final HashSet stopWords = new HashSet(Arrays.asList(StopWords.STOP_WORDS));

        public void reduce(
                Text key,
                Iterable<TextArrayWritable> values,
                Context context
        ) throws IOException, InterruptedException {
            JSONObject jsonObj = new JSONObject();
            JSONArray wordsJsonArray = new JSONArray();
            HashMap<Text, Integer> wordCount = new HashMap<Text, Integer>();

            for (ArrayWritable array : values) {
                for (Writable value : array.get()) {
                    Text word = (Text) value;

                    if (!stopWords.contains(word.toString())) {
                        if(!wordCount.containsKey(word)) {
                            wordCount.put(word, 1);
                        } else {
                            wordCount.put(word, wordCount.get(word) + 1);
                        }
                    }
                }
            }

            LinkedHashMap<Text, Integer> sortedWordCount = wordCount.entrySet()
                    .stream()
                    .sorted(java.util.Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(
                            java.util.Map.Entry::getKey,
                            java.util.Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new
                    ));

            sortedWordCount.forEach((k, v) -> {
                JSONObject obj = new JSONObject();
                obj.put(k.toString(), v);
                wordsJsonArray.add(obj);
            });

            jsonObj.put(key.toString(), wordsJsonArray);
            context.write(NullWritable.get(), new Text(jsonObj.toString()));
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Word Count");
        Path outputPath = new Path(args[1]);

        job.setJarByClass(App.class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        job.setInputFormatClass(TextInputFormat.class);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(TextArrayWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        outputPath.getFileSystem(conf).delete(outputPath, true);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

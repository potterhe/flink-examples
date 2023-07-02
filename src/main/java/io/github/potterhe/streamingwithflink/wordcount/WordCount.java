/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.potterhe.streamingwithflink.wordcount;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * Skeleton for a Flink Streaming Job.
 *
 * <p>For a tutorial how to write a Flink streaming application, check the
 * tutorials and examples on the <a href="https://flink.apache.org/docs/stable/">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution, run
 * 'mvn clean package' on the command line.
 *
 * <p>If you change the name of the main class (with the public static void main(String[] args))
 * method, change the respective entry in the POM.xml file (simply search for 'mainClass').
 */
public class WordCount {

	public static void main(String[] args) throws Exception {

		//https://nightlies.apache.org/flink/flink-docs-master/zh/docs/dev/datastream/application_parameters/
		ParameterTool parameter = ParameterTool.fromArgs(args);
		String configFilePath = parameter.get("app-config-file");
		System.out.println("app-config-file: " + configFilePath);

		// https://bitbucket.org/snakeyaml/snakeyaml/wiki/Documentation
		InputStream inputStream = new FileInputStream(configFilePath);
		// 创建 SnakeYaml 实例
		Yaml yaml = new Yaml();
		YamlObj yamlobj = yaml.loadAs(inputStream, YamlObj.class);

		// 使用 load 方法加载并解析 YAML 文件
		System.out.println("yaml data: " + yamlobj.getMykey());
		for (Map.Entry<String, String> e: yamlobj.getMyapp().entrySet()) {
			System.out.println("yaml map data: " + e.getKey() + ":" + e.getValue());
		}

		// 关闭输入流
		inputStream.close();


		// set up the streaming execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();


		ConfigOption<String> HOSTNAME = ConfigOptions.key("mykey")
				.stringType()
				.noDefaultValue();

		ReadableConfig config = env.getConfiguration();
		String value1 = config.get(HOSTNAME);

		System.out.println("key1: " + config);
		System.out.println("key2: " + value1);



		/*
		 * Here, you can start creating your execution plan for Flink.
		 *
		 * Start with getting some data from the environment, like
		 * 	env.readTextFile(textPath);
		 *
		 * then, transform the resulting DataStream<String> using operations
		 * like
		 * 	.filter()
		 * 	.flatMap()
		 * 	.join()
		 * 	.coGroup()
		 *
		 * and many more.
		 * Have a look at the programming guide for the Java API:
		 *
		 * https://flink.apache.org/docs/latest/apis/streaming/index.html
		 *
		 */

		// nc -lk 9999
		//DataStreamSource<String> source = env.socketTextStream("localhost", 9999);

		String[] WORDS =
				new String[] {
						"To be, or not to be,--that is the question:--",
						"Whether 'tis nobler in the mind to suffer",
						"The slings and arrows of outrageous fortune",
						"Or to take arms against a sea of troubles,",
						"And by opposing end them?--To die,--to sleep,--",
						"No more; and by a sleep to say we end",
						"The heartache, and the thousand natural shocks",
						"That flesh is heir to,--'tis a consummation",
						"Devoutly to be wish'd. To die,--to sleep;--",
						"To sleep! perchance to dream:--ay, there's the rub;",
						"For in that sleep of death what dreams may come,",
						"When we have shuffled off this mortal coil,",
						"Must give us pause: there's the respect",
						"That makes calamity of so long life;",
						"For who would bear the whips and scorns of time,",
						"The oppressor's wrong, the proud man's contumely,",
						"The pangs of despis'd love, the law's delay,",
						"The insolence of office, and the spurns",
						"That patient merit of the unworthy takes,",
						"When he himself might his quietus make",
						"With a bare bodkin? who would these fardels bear,",
						"To grunt and sweat under a weary life,",
						"But that the dread of something after death,--",
						"The undiscover'd country, from whose bourn",
						"No traveller returns,--puzzles the will,",
						"And makes us rather bear those ills we have",
						"Than fly to others that we know not of?",
						"Thus conscience does make cowards of us all;",
						"And thus the native hue of resolution",
						"Is sicklied o'er with the pale cast of thought;",
						"And enterprises of great pith and moment,",
						"With this regard, their currents turn awry,",
						"And lose the name of action.--Soft you now!",
						"The fair Ophelia!--Nymph, in thy orisons",
						"Be all my sins remember'd."
				};
		DataStreamSource<String> source = env.fromElements(WORDS);
		source.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
			@Override
			public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
				String[] words = s.split("\\s+");
				for (String word : words) {
					collector.collect(new Tuple2<String, Integer>(word, 1));
				}
			}
		}).keyBy(r -> r.f0)
				.sum(1)
				.print();

		// execute program
		env.execute("Flink Streaming Java API Skeleton");
	}
}
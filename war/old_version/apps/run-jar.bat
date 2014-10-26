@echo off
set APP_JAR_NAME="./out/artifacts/processor_word_frequency_index_jar/processor-word-frequency-index.jar"
set APP_PKG=com.github.zaqwes8811.text_processor
cd ..
rem java -Dfile.encoding=UTF8  -cp  %APP_JAR_NAME% %APP_PKG%.write_chain.spiders_extractors.SpiderExtractor
java -Dfile.encoding=UTF8  -cp  %APP_JAR_NAME% %APP_PKG%.write_chain.spiders_processors.MinimalSpiderProcessor
java -Dfile.encoding=UTF8  -cp  %APP_JAR_NAME% %APP_PKG%.write_chain.mapreduce.SentencesLevelProcessor
cd apps
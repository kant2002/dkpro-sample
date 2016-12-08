package simple;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

import java.util.logging.LogManager;

import org.apache.log4j.Logger;

import de.tudarmstadt.ukp.dkpro.core.io.conll.Conll2006Writer;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.languagetool.LanguageToolLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.maltparser.MaltParser;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpSegmenter;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;

// text similarity
import dkpro.similarity.algorithms.api.TextSimilarityMeasure;
import dkpro.similarity.algorithms.lexical.ngrams.WordNGramJaccardMeasure;

public class helloworld {
   // private static final Logger logger = LogManager.getLogger("HelloWorld");
  public static void main(String[] args) throws Exception {
	  // lexicalAnalysis();
	  textSimilarity();
  }

  public static void textSimilarity() throws Exception {
	// this similarity measure is defined in the dkpro.similarity.algorithms.lexical-asl package
	// you need to add that to your .pom to make that example work
	// there are some examples that should work out of the box in dkpro.similarity.example-gpl
	TextSimilarityMeasure measure = new WordNGramJaccardMeasure(3);    // Use word trigrams

	String[] tokens1 = "This is a short example text .".split(" ");
	String[] tokens2 = "A short example text could look like that .".split(" "); // 0.090
	// String[] tokens2 = "This is short example text .".split(" "); // Similarity 0.286
	// String[] tokens2 = "this short example text .".split(" "); // Similarity 0.333
	// String[] tokens2 = "short example text .".split(" "); // Similarity 0.4
	// String[] tokens2 = "This is a short example text .".split(" "); // Similarity 1.0

	double score = measure.getSimilarity(tokens1, tokens2);

	System.out.println("Similarity: " + score);
  }

  public static void lexicalAnalysis() throws Exception {
	  runPipeline(
        createReaderDescription(TextReader.class,
            TextReader.PARAM_SOURCE_LOCATION, "input/*.txt",
            TextReader.PARAM_LANGUAGE, "en"),

        createEngineDescription(OpenNlpSegmenter.class),
        createEngineDescription(OpenNlpPosTagger.class),
        createEngineDescription(LanguageToolLemmatizer.class),
        createEngineDescription(MaltParser.class),
        createEngineDescription(Conll2006Writer.class,
            Conll2006Writer.PARAM_TARGET_LOCATION, "output",
    		XmiWriter.PARAM_OVERWRITE, true),
        createEngineDescription(XmiWriter.class,
        		XmiWriter.PARAM_TARGET_LOCATION, "output",
        		XmiWriter.PARAM_OVERWRITE, true,
        		XmiWriter.PARAM_TYPE_SYSTEM_FILE, "output\\typesystem.xml"));
  }
  
}
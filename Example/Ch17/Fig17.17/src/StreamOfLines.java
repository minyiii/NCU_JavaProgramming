import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StreamOfLines {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Pattern pattern = Pattern.compile("\\s+");
		
		Map<String, Long> wordsCount = 
			Files.lines(Paths.get("Paragraph.txt"))
				 .map(line -> line.replaceAll("(?!')\\p{P}", ""))
				 .flatMap(line -> pattern.splitAsStream(line))
				 .collect(Collectors.groupingBy(String::toLowerCase, TreeMap::new, Collectors.counting()));
		wordsCount.entrySet()
				.stream()
				.collect(Collectors.groupingBy(entry -> entry.getKey().charAt(0), TreeMap::new, Collectors.toList()))
				.forEach((letter, wordList)->
					{
						System.out.printf("\n%C\n", letter);
						wordList.stream().forEach(word
								-> System.out.printf("%15s: %d\n", word.getKey(), word.getValue()));
					}
				);
		
	}

}
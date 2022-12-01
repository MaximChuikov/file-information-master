package com.example.fileinformation.modules;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class TextModule extends Module {
    
    private static Map<String, String> searchNgram(String[][] parsedText, int count) {
        Map<String, Map<String, Integer>> counter = new HashMap<>();
        for (String[] strings : parsedText) {
            for (int i = 0; i < strings.length - count + 1; i++) {
                StringBuilder ngramBuilder = new StringBuilder();
                for (int c = 0; c < count - 1; c++) {
                    ngramBuilder.append(strings[i + c]).append(" ");
                }
                String ngram = ngramBuilder.substring(0, ngramBuilder.length() - 1);
                Map<String, Integer> integerMap = counter.getOrDefault(ngram, new HashMap<>());
                integerMap.put(strings[i + count - 1], integerMap.getOrDefault(strings[i + count - 1], 0) + 1);
                counter.put(ngram, integerMap);
            }
        }
        Map<String, String> ngrams = new HashMap<>();
        counter.forEach((key, value) -> value.entrySet().stream().max(Map.Entry.comparingByValue()).ifPresent(entry1 -> ngrams.put(key, entry1.getKey())));
        return ngrams;
    }
    
    private static String[][] readParsedText(File file) {
        StringBuilder text = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return parseText(text.toString());
    }
    
    private static String[][] parseText(String text) {
        String[] sentences = Arrays.stream(text.toLowerCase().split("[.!?;:()\n]")).filter(s -> s.length() > 0).toArray(String[]::new);
        String[][] parsedText = new String[sentences.length][];
        int i = 0;
        for (String sentence : sentences) {
            parsedText[i] = Arrays.stream(sentence.trim().split("[^a-zA-Zа-яА-ЯёЁ']")).filter(s -> s.length() > 0).toArray(String[]::new);
            i++;
        }
        return parsedText;
    }
    
    @Override
    public boolean isMyExtension(File file) {
        String extension = getExtension(file);
        return extension != null && extension.equals("txt");
    }
    
    @Override
    public String functionalDescription() {
        return "count - Вывод количества строк\n" + "symbols - Вывод частоты входа каждого символа\n" + "ngram - Вывод n-грамм";
    }
    
    @Override
    public void doCommand(String command, File file) {
        switch (command.toLowerCase()) {
            case "count":
                printCountLines(file);
                break;
            case "symbols":
                printCountsChars(file);
                break;
            case "ngram":
                printNgrams(file);
                break;
        }
    }
    
    private void printCountLines(File file) {
        int lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) {
                lines++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Количество строк: " + lines);
    }
    
    private void printCountsChars(File file) {
        Map<Character, Integer> chars = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (char c : line.toCharArray()) {
                    chars.put(c, chars.getOrDefault(c, 0) + 1);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        chars.forEach((character, integer) -> System.out.println("'" + character + "': " + integer));
    }
    
    private void printNgrams(File file) {
        int signs = 0;
        char[] signArr = new char[] {',', ';', '.', ':', '!', '?'};
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while (line != null) {
                for (char x: line.toCharArray()) {
                    if (Arrays.asList(signArr).contains(x)) {
                        signs++;
                    }
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Количество знаков припинания: " + signs);
    }
}

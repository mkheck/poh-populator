package com.thehecklers.pohpopulator;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VectorFeeder {
    private final VectorStore vectorStore;

    public VectorFeeder(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void testing() {
//        List<Document> docsList = loadText(new ClassPathResource("OldPOH.pdf"));
        var docsList = loadText(new ClassPathResource("NewPOH.pdf"));
        var textList = splitText(docsList.get(0));
        textList.forEach(System.out::println);
        //textList.stream().spliterator().trySplit() (do this based on count, work it out!)
        textList.forEach(t -> vectorStore.add(List.of(new Document(t))));
    }

//    @Value("classpath:/word-sample.docx") // This is the word document to load
//    @Value("classpath:/POH.pdf") // This is the PDF to load
//    private Resource resourceUri;

    List<Document> loadText(Resource resourceUri) {
        var tikaDocumentReader = new TikaDocumentReader(resourceUri);
        return tikaDocumentReader.get();
    }

    List<String> splitText(Document doc) {
        var splitter = new TokenTextSplitter();
        // MH: Protected splitText(String) method exists with default chunk size of 800, sigh
        return splitter.split(doc.getFormattedContent(), 800);
    }
}

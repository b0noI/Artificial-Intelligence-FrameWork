package com.aif.common;

import com.aif.model.memory.short_time.Sentence;
import com.aif.model.memory.short_time.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * User: b0noI
 * Date: 01.02.13
 * Time: 16:40
 */
public class ParagraphSearcher extends RecursiveTask<ParagraphSearcher> {

    private static final String TAG = ParagraphSearcher.class.getSimpleName();

    private static final ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool();

    private final Text text;

    private final int from;

    private final int to;

    private final int pargraphSize;

    private double paragraphWeight = 0;

    private ParagraphSearcher(Text text, int from, int to, int pargraphSize){
        this.text = text;
        this.from = from;
        this.to = to;
        this.pargraphSize = pargraphSize;
    }

    public static List<Sentence> findParagraph(Text text, int paragraphSize){

        if (paragraphSize <= 0)
            throw new IllegalArgumentException(TAG + ": findParagraph(Text text, int paragraphSize): paragraphSize <= 0");

        ParagraphSearcher paragraphSearcher =
                new ParagraphSearcher(text, 0, text.getSentences().size(), paragraphSize);
        paragraphSearcher = FORK_JOIN_POOL.invoke(paragraphSearcher);
        return paragraphSearcher.getSentences();
    }

    @Override
    protected ParagraphSearcher compute() {

        if (to == from  + 1){
            caculatePargraphWeight();
            return this;
        }

        int midl = (from + to) / 2;
        ParagraphSearcher left = new ParagraphSearcher(text, from, midl, pargraphSize);
        ParagraphSearcher right = new ParagraphSearcher(text, midl, to, pargraphSize);

        double lefRes = 0;
        double rigRes = 0;

        left.fork();

        left = left.join();
        right = right.compute();

        lefRes = left.getParagraphWeight();
        rigRes = right.getParagraphWeight();

        if (lefRes > rigRes)
            return left;
        return right;
    }

    private double getParagraphWeight() {
        return paragraphWeight;
    }

    private List<Sentence> getSentences(){
        List<Sentence> sentences = new ArrayList<>(pargraphSize);
        for (int i=from; i<from + pargraphSize && i<text.getSentences().size();i++){
            sentences.add(text.getSentences().get(i));
        }
        return sentences;
    }

    private double calculateSentencesWeight(int n){

        Sentence sent = text.getSentences().get(n);
        if (sent == null || sent.getSentenceWords().size() == 0)
            return 0.0;
        return text.getSentencesWeight(sent);

    }

    private void caculatePargraphWeight(){
        for (int i = from ; i<from + pargraphSize && i<text.getSentences().size();i++)
            paragraphWeight += calculateSentencesWeight(i);
        paragraphWeight /= (double)pargraphSize;
    }

}

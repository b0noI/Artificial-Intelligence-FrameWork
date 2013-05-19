package com.aif.model.memory.short_time;

import com.aif.common.WordComparator;

/**
 * User: b0noI
 * Date: 09.12.12
 * Time: 20:03
 */
public class WordForm {

    private final String prefix;

    private final String suffix;

    private long count = 1;

    private WordForm(String prefix, String suffix){
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WordForm){
            WordForm wordForm = (WordForm) obj;
            if (getPrefix().equals(wordForm.getPrefix()))
                if (getSuffix().equals(wordForm.getSuffix())){
                    count++;
                    return true;
                }
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getPrefix().hashCode() + getSuffix().hashCode();
    }

    public String getPrefix(){
        return prefix;
    }

    public String getSuffix(){
        return suffix;
    }

    public boolean isWordInWordForm(String word){
        String prefix = getPrefix();
        if (prefix != null && !prefix.isEmpty()){
            if (word.indexOf(prefix) != 0)
                return false;
        }

        String suffix = getSuffix();
        if (suffix != null && !suffix.isEmpty()){
            if (word.indexOf(suffix) != word.length() - suffix.length())
                return false;
        }

        return true;
    }

    static WordForm generateWordForm(String baseWord, String wordFormStr){
        if (baseWord.length() < wordFormStr.length())
            return null;

        String sameString = WordComparator.longestSubstring(baseWord, wordFormStr);
        if (sameString == null || sameString.length() == 0)
            return null;

        int startIndex = wordFormStr.indexOf(sameString);
        if (startIndex == -1)
            return null;
        String prefix = wordFormStr.substring(0, startIndex);
        String suffix = wordFormStr.substring(startIndex + sameString.length());
        WordForm wordForm = new WordForm(prefix, suffix);
        if (!wordForm.isWordFormValid())
            return null;
        return wordForm;
    }

    boolean isWordFormValid(){
        if ((getPrefix() == null || getPrefix().isEmpty()) &&
                (getSuffix() == null || getSuffix().isEmpty()))
            return false;

        return true;
    }

}

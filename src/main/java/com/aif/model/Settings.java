package com.aif.model;

public class Settings {

    public enum WeightCalculationMode {
        WITH_CONNECTION_COUNT,
        WITHOUT_CONNECTION_COUNT
    }

    public enum WordFormMode {
        PARS_WORD_FORM,
        IGNORE_WORD_FORM
    }

    @Deprecated
    public static WeightCalculationMode sWeightCalculationMode = WeightCalculationMode.WITH_CONNECTION_COUNT;

    public static final WordFormMode sCurrentWordFormMode = WordFormMode.PARS_WORD_FORM;

    public static final int MAX_THEME_LENGTH = 15;

    public static final double DEEP_LEVEL_THEME_SEARCH = 0.1;

    public static final int MINIMUM_WORD_LENGTH = 1;

    /**
     * Comparing level witch using method equal
     * @see model.memory.short_time.Word.seequals(Object)
     */
    public static final double WORDS_COMPARE_MAX_LEVEL = 0.85;

}

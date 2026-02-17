package com.wfh.drawio.spi;

import java.util.Optional;
import java.util.ServiceLoader;

/**
 * @Title: ParserFactory
 * @Author wangfenghuan
 * @Package com.wfh.drawio.spi
 * @Date 2026/2/17 19:22
 * @description: 解析器工厂类
 */
public class ParserFactory {

    public static Optional<LanguageParser> getParser(String projectDir){
        ServiceLoader<LanguageParser> loader = ServiceLoader.load(LanguageParser.class);

        // 遍历所有的解析器，找到第一个能解析这个文件的解析器

        for (LanguageParser languageParser : loader) {
            if (languageParser.canParse(projectDir)){
                return Optional.of(languageParser);
            }
        }
        return Optional.empty();
    }
}
